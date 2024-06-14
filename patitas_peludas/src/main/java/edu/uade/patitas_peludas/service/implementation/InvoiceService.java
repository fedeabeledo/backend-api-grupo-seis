package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.InvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.InvoiceResponseDTO;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductInvoiceResponseDTO;
import edu.uade.patitas_peludas.dto.ProductsInvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.UserInvoiceResposeDTO;
import edu.uade.patitas_peludas.entity.Invoice;
import edu.uade.patitas_peludas.entity.InvoiceProduct;
import edu.uade.patitas_peludas.entity.PaymentMethod;
import edu.uade.patitas_peludas.entity.Product;
import edu.uade.patitas_peludas.entity.ShippingMethod;
import edu.uade.patitas_peludas.entity.User;
import edu.uade.patitas_peludas.exception.InvalidFourDigitsException;
import edu.uade.patitas_peludas.exception.InvalidPaymentMethodException;
import edu.uade.patitas_peludas.exception.InvalidShippingDataException;
import edu.uade.patitas_peludas.exception.InvalidShippingMethodException;
import edu.uade.patitas_peludas.exception.InvoiceNotFoundException;
import edu.uade.patitas_peludas.exception.ProductNotFoundException;
import edu.uade.patitas_peludas.exception.UserNotFoundException;
import edu.uade.patitas_peludas.repository.InvoiceProductRepository;
import edu.uade.patitas_peludas.repository.InvoiceRepository;
import edu.uade.patitas_peludas.repository.ProductRepository;
import edu.uade.patitas_peludas.repository.UserRepository;
import edu.uade.patitas_peludas.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements IInvoiceService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceProductRepository invoiceProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;

    private static final String PRODUCT_NOT_FOUND_ERROR = "Could not find product with ID: %d.";
    private static final String NOT_ENOUGH_STOCK_ERROR = "Not enough stock for product with ID: %d.";
    private static final String INVOICE_NOT_FOUND_ERROR = "Could not find invoice with ID: %d.";
    private static final String USER_NOT_FOUND_ERROR = "Could not find user with ID: %d.";
    private static final String INVALID_PAYMENT_METHOD_ERROR = "%s is an invalid payment method.";
    private static final String INVALID_SHIPPING_METHOD_ERROR = "%s is an invalid shipping method.";
    private static final String INVALID_SHIPPING_DATA_ERROR = "Shipping data is required for shipping.";
    private static final String INVALID_LAST_FOUR_DIGITS_ERROR = "Last four digits are required for credit card payment.";


    @Override
    public InvoiceResponseDTO create(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = convertInvoiceRequestDTOToInvoice(invoiceRequestDTO);
        invoiceRepository.save(invoice);
        List<InvoiceProduct> invoiceProducts = createInvoiceProducts(invoiceRequestDTO, invoice);
        invoiceProductRepository.saveAll(invoiceProducts);
        return findById(invoice.getId());
    }

    @Override
    public InvoiceResponseDTO findById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException(String.format(INVOICE_NOT_FOUND_ERROR, invoiceId))
        );
        return convertInvoiceToInvoiceResponseDTO(invoice);
    }

    @Override
    public PageDTO<InvoiceResponseDTO> findByUserId(Long userId, Short page) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR, userId))
        );
        Pageable pageable = buildPageable(page);
        Page<Invoice> invoices = invoiceRepository.findByUserId(userId, pageable);
        List<InvoiceResponseDTO> content = invoices.getContent().stream().map(this::convertInvoiceToInvoiceResponseDTO).collect(Collectors.toList());
        return new PageDTO<>(content, invoices.getTotalPages(), invoices.getTotalElements(), invoices.getNumber(), invoices.getSize());
    }

    private Pageable buildPageable(Short page) {
        return PageRequest.of(page, 12, Sort.by("id"));
    }

    private InvoiceResponseDTO convertInvoiceToInvoiceResponseDTO(Invoice invoice) {
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoiceId(invoice.getId());
        List<ProductInvoiceResponseDTO> products = invoiceProducts.stream().map(invoiceProduct -> {
            Product product = invoiceProduct.getProduct();
            return new ProductInvoiceResponseDTO(product.getTitle(), product.getPrice(), product.getDiscount(), invoiceProduct.getQuantity());
        }).collect(Collectors.toList());
        Double total = invoiceProducts.stream().mapToDouble(invoiceProduct -> invoiceProduct.getUnitPrice() * invoiceProduct.getQuantity()).sum();
        total = total - (total * (invoice.getDiscount() / 100.0));
        total += invoice.getShippingCost();
        Optional<String> lastFourDigits = Optional.empty();
        if (invoice.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
            lastFourDigits = Optional.ofNullable(invoice.getLastFourDigits());
            if (lastFourDigits.isEmpty() || lastFourDigits.get().length() != 4) {
                throw new InvalidFourDigitsException(INVALID_LAST_FOUR_DIGITS_ERROR);
            }
        }

        Optional<String> shippingData = Optional.empty();
        if (invoice.getShippingMethod().equals(ShippingMethod.ANDREANI) || invoice.getShippingMethod().equals(ShippingMethod.CORREO_ARGENTINO)) {
            shippingData = Optional.ofNullable(invoice.getShippingData());
            if (shippingData.isEmpty() || shippingData.get().isBlank() || shippingData.get().isEmpty()) {
                throw new InvalidShippingDataException(INVALID_SHIPPING_DATA_ERROR);
            }
        }

        return new InvoiceResponseDTO(
                products,
                mapper.convertValue(invoice.getUser(), UserInvoiceResposeDTO.class),
                invoice.getDiscount(),
                invoice.getShippingMethod().name(),
                invoice.getShippingCost(),
                invoice.getPaymentMethod().name(),
                shippingData.orElse(""),
                lastFourDigits.orElse(""),
                total
        );
    }

    private Invoice convertInvoiceRequestDTOToInvoice(InvoiceRequestDTO invoiceRequestDTO) {
        User user = userRepository.findById(invoiceRequestDTO.getUserId()).orElseThrow(
                () -> new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR, invoiceRequestDTO.getUserId()))
        );
        PaymentMethod paymentMethod;

        try {
            paymentMethod = PaymentMethod.valueOf(invoiceRequestDTO.getPaymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentMethodException(String.format(INVALID_PAYMENT_METHOD_ERROR, invoiceRequestDTO.getPaymentMethod()));
        }
        Short discount = getDiscount(paymentMethod);

        Double subtotal = invoiceRequestDTO.getProducts().stream().mapToDouble(order -> {
            Product product = productRepository.findById(order.getProductId()).orElseThrow(
                    () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, order.getProductId()))
            );
            return product.getPrice() * order.getQuantity();
        }).sum();

        ShippingMethod shippingMethod;
        try {
            shippingMethod = ShippingMethod.valueOf(invoiceRequestDTO.getShippingMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidShippingMethodException(String.format(INVALID_SHIPPING_METHOD_ERROR, invoiceRequestDTO.getShippingMethod()));
        }

        Double shippingCost = getShippingCost(shippingMethod, subtotal);

        return new Invoice(user, discount, shippingMethod, shippingCost, paymentMethod, invoiceRequestDTO.getShippingData(), invoiceRequestDTO.getLastFourDigits());
    }

    private Short getDiscount(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case WIRE -> 10;
            case CREDIT_CARD -> 5;
            default -> 0;
        };
    }

    private Double getShippingCost(ShippingMethod shippingMethod, Double total) {
        if (total > 50000) {
            return 0.0;
        }
        return switch (shippingMethod) {
            case ANDREANI -> 2500.0;
            case CORREO_ARGENTINO -> 3000.0;
            default -> 0.0;
        };
    }

    private List<InvoiceProduct> createInvoiceProducts(InvoiceRequestDTO invoiceRequestDTO, Invoice invoice) {
        List<InvoiceProduct> invoiceProducts = new ArrayList<>();
        try {
            for (ProductsInvoiceRequestDTO products : invoiceRequestDTO.getProducts()) {
                Product product = productRepository.findById(products.getProductId()).orElseThrow(
                        () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, products.getProductId())));
                if (product.getStock() < products.getQuantity()) {
                    throw new ProductNotFoundException(String.format(NOT_ENOUGH_STOCK_ERROR, product.getId()));
                }
                product.setStock((short) (product.getStock() - products.getQuantity()));
                productRepository.save(product);
                Double unitPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100.0));
                InvoiceProduct invoiceProduct = new InvoiceProduct(invoice, product, products.getQuantity(), unitPrice);
                invoiceProducts.add(invoiceProduct);
            }
        } catch (Exception e) {
            invoiceRepository.delete(invoice);
            throw e;
        }
        return invoiceProducts;
    }
}
