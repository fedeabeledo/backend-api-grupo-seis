package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.InvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.InvoiceResponseDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.dto.ProductInvoiceDTO;
import edu.uade.patitas_peludas.entity.Invoice;
import edu.uade.patitas_peludas.entity.InvoiceProduct;
import edu.uade.patitas_peludas.entity.Product;
import edu.uade.patitas_peludas.exception.InvoiceNotFoundException;
import edu.uade.patitas_peludas.exception.ProductNotFoundException;
import edu.uade.patitas_peludas.repository.InvoiceProductRepository;
import edu.uade.patitas_peludas.repository.InvoiceRepository;
import edu.uade.patitas_peludas.repository.ProductRepository;
import edu.uade.patitas_peludas.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private ObjectMapper mapper;


    String PRODUCT_NOT_FOUND_ERROR = "Could not find product with ID: %d.";
    String NOT_ENOUGH_STOCK_ERROR = "Not enough stock for product with ID: %d.";
    String INVOICE_NOT_FOUND_ERROR = "Could not find invoice with ID: %d.";


    @Override
    public InvoiceResponseDTO create(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = new Invoice(invoiceRequestDTO.getDiscount(), invoiceRequestDTO.getShippingCost());
        invoiceRepository.save(invoice);
        List<InvoiceProduct> invoiceProducts = createInvoiceProducts(invoiceRequestDTO, invoice);
        invoiceProducts.stream().forEach(invoiceProduct -> invoiceProductRepository.save(invoiceProduct));
        return getInvoiceById(invoice.getId());
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException("Invoice not found")
        );
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByInvoiceId(invoice.getId());
        List<ProductInvoiceDTO> products = invoiceProducts.stream().map(invoiceProduct -> {
            Product product = invoiceProduct.getProduct();
            ProductDTO productDTO = mapper.convertValue(product, ProductDTO.class);
            return new ProductInvoiceDTO(productDTO, invoiceProduct.getQuantity());
        }).collect(Collectors.toList());
        Double total = invoiceProducts.stream().mapToDouble(invoiceProduct -> invoiceProduct.getUnitPrice() * invoiceProduct.getQuantity()).sum();
        total = total - (total * (invoice.getDiscount() / 100.0));

        if (total > 50000) {
            invoice.setShippingCost((short) 0);
            invoiceRepository.save(invoice);
        }

        total += invoice.getShippingCost();

        return new InvoiceResponseDTO(products, invoice.getDiscount(), invoice.getShippingCost(), total);
    }

    private List<InvoiceProduct> createInvoiceProducts(InvoiceRequestDTO invoiceRequestDTO, Invoice invoice) {
        List<InvoiceProduct> invoiceProducts = new ArrayList<>();
        try {
            for (Map.Entry<Long, Short> products : invoiceRequestDTO.getProducts().entrySet()) {
                Product product = productRepository.findById(products.getKey()).orElseThrow(
                        () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, products.getKey())));
                if (product.getStock() < products.getValue()) {
                    throw new ProductNotFoundException(String.format(NOT_ENOUGH_STOCK_ERROR, product.getId()));
                }
                product.setStock((short) (product.getStock() - products.getValue()));
                productRepository.save(product);
                Double unitPrice = product.getPrice() - (product.getPrice() * (product.getDiscount() / 100.0));
                InvoiceProduct invoiceProduct = new InvoiceProduct(invoice, product, products.getValue(), unitPrice);
                invoiceProducts.add(invoiceProduct);
            }
        } catch (Exception e) {
            invoiceRepository.delete(invoice);
            throw e;
        }
        return invoiceProducts;
    }
}
