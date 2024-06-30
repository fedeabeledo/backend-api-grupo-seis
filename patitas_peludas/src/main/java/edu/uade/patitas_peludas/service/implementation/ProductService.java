package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductRequestDTO;
import edu.uade.patitas_peludas.dto.ProductResponseDTO;
import edu.uade.patitas_peludas.entity.InvoiceProduct;
import edu.uade.patitas_peludas.entity.PetCategory;
import edu.uade.patitas_peludas.entity.PetStage;
import edu.uade.patitas_peludas.entity.Product;
import edu.uade.patitas_peludas.entity.User;
import edu.uade.patitas_peludas.exception.InvalidPetCategoryException;
import edu.uade.patitas_peludas.exception.InvalidPetStageException;
import edu.uade.patitas_peludas.exception.ProductNotFoundException;
import edu.uade.patitas_peludas.exception.UserNotFoundException;
import edu.uade.patitas_peludas.repository.InvoiceProductRepository;
import edu.uade.patitas_peludas.repository.ProductRepository;
import edu.uade.patitas_peludas.repository.UserRepository;
import edu.uade.patitas_peludas.service.IProductService;
import edu.uade.patitas_peludas.service.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private InvoiceProductRepository invoiceProductRepository;
    @Autowired
    private UserRepository userRepository;


    private static final String PRODUCT_NOT_FOUND_ERROR = "Could not find product with ID: %d.";
    private static final String INVALID_PET_CATEGORY_ERROR = "%s is an invalid pet category. It should be one of: CAT, DOG, FISH, HAMSTER.";
    private static final String INVALID_PET_STAGE_ERROR = "%s is an invalid pet stage. It should be one of: BABY, ADULT, SENIOR.";
    private static final String USER_NOT_FOUND_ERROR = "Could not find user with ID: %d.";

    @Override

    public PageDTO<ProductDTO> findAll(String keywords, String category, String brand, Double min, Double max,
                                       String priceSort, String bestsellerSort, Short page, String stage) {
        Pageable pageable = buildPageable(priceSort, bestsellerSort, page);
        Specification<Product> spec = buildSpec(category, brand, min, max, keywords, stage);

        Page<Product> res = repository.findAll(spec, pageable);

        List<ProductResponseDTO> content = res.getContent().stream().map(product ->
                mapper.convertValue(product, ProductResponseDTO.class)).collect(Collectors.toList());

        return new PageDTO<>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber(),
                res.getSize()
        );
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Optional<Product> product = repository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, id));
        }

        return mapper.convertValue(product.get(), ProductResponseDTO.class);
    }

    @Override
    public PageDTO<ProductResponseDTO> findProductByUserId(Long userId, Short page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<Product> res = repository.findProductByUserId(userId, pageable);

        List<ProductResponseDTO> content = res.getContent().stream().map(product ->
                mapper.convertValue(product, ProductResponseDTO.class)).collect(Collectors.toList());

        return new PageDTO<>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber(),
                res.getSize()
        );
    }

    @Override
    public ProductResponseDTO save(ProductRequestDTO product) {
        Product entity = mapProduct(product);
        Product saved = repository.save(entity);
        return mapper.convertValue(saved, ProductResponseDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, id)));
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findByProductId(id);
        invoiceProductRepository.deleteAll(invoiceProducts);
        repository.deleteById(id);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO product) {
        if (repository.existsById(id)) {
            Product oldProduct = repository.findById(id).get();
            Product mappedProduct = mapProduct(product);
            mappedProduct.setScore(oldProduct.getScore());
            mappedProduct.setScoreVoters(oldProduct.getScoreVoters());
            mappedProduct.setBestseller(oldProduct.getBestseller());
            mappedProduct.setId(id);
            Product updated = repository.save(mappedProduct);
            return mapper.convertValue(updated, ProductResponseDTO.class);
        } else {
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, id));
        }
    }

    private Product mapProduct(ProductRequestDTO product) {
        Random random = new Random();
        Double score = random.nextDouble() * 5;
        score = Math.round(score * 100.0) / 100.0;
        Integer scoreVoters = random.nextInt(1000);
        Boolean isBestseller = random.nextBoolean();
        PetCategory petCategory;
        try {
            petCategory = PetCategory.valueOf(product.getPetCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPetCategoryException(String.format(INVALID_PET_CATEGORY_ERROR, product.getPetCategory()));
        }
        PetStage petStage;
        try {
            petStage = PetStage.valueOf(product.getPetStage().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPetStageException(String.format(INVALID_PET_STAGE_ERROR, product.getPetStage()));
        }
        User user = userRepository.findById(product.getUserId()).orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND_ERROR, product.getUserId())));

        return new Product(
                null,
                user,
                product.getTitle(),
                product.getDescription(),
                product.getImageUrl(),
                product.getBrand(),
                petCategory,
                petStage,
                score,
                scoreVoters.shortValue(),
                product.getPrice(),
                product.getDiscount(),
                product.getStock(),
                isBestseller);
    }

    // sorts
    private Pageable buildPageable(String priceSort, String bestsellerSort, Short page) {
        List<Sort.Order> orders = new ArrayList<>();

        if (priceSort != null) {
            if (priceSort.equalsIgnoreCase("desc")) {
                orders.add(Sort.Order.desc("price"));
            } else if (priceSort.equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc("price"));
            }
        }

        if (bestsellerSort != null) {
            if (bestsellerSort.equalsIgnoreCase("desc")) {
                orders.add(Sort.Order.desc("bestseller"));
            } else if (bestsellerSort.equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc("bestseller"));
            }
        }

        Sort sorted = Sort.by(orders);
        return PageRequest.of(page, 12, sorted);
    }

    // search bar, shop pages, brands & price filters
    private Specification<Product> buildSpec(String category, String brand, Double min, Double max, String keywords, String stage) {
        Specification<Product> spec = Specification.where(null);

        if (category != null) {
            spec = spec.and(ProductSpecification.categorySpec(category));
        }

        if (keywords != null) {
            spec = spec.and(ProductSpecification.titleContainingSpec(keywords));
        }

        if (brand != null) {
            spec = spec.and(ProductSpecification.brandSpec(brand));
        }

        if (stage != null) {
            spec = spec.and(ProductSpecification.petStageSpec(stage));
        }

        if (min != null || max != null) {
            spec = spec.and(ProductSpecification.priceSpec(min, max));
        }

        return spec;
    }
}
