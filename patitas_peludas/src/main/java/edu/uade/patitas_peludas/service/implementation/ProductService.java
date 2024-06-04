package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.entity.Product;
import edu.uade.patitas_peludas.exception.ProductNotFoundException;
import edu.uade.patitas_peludas.repository.ProductRepository;
import edu.uade.patitas_peludas.service.IProductService;
import edu.uade.patitas_peludas.service.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    String PRODUCT_NOT_FOUND_ERROR = "Could not find product with ID: %d.";

    @Override
    public PageDTO<ProductDTO> findAll(String keywords, String category, String brand, Double min, Double max,
                                       String priceSort, String bestsellerSort, Short page) {
        Pageable pageable = buildPageable(priceSort, bestsellerSort, page);
        Specification<Product> spec = buildSpec(category, brand, min, max, keywords);

        Page<Product> res = repository.findAll(spec, pageable);

        List<ProductDTO> content = res.getContent().stream().map(product ->
                mapper.convertValue(product, ProductDTO.class)).collect(Collectors.toList());

        return new PageDTO<ProductDTO>(
                content,
                res.getTotalPages(),
                res.getTotalElements(),
                res.getNumber(),
                res.getSize()
        );
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> product = repository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, id));
        }

        return mapper.convertValue(product.get(), ProductDTO.class);
    }

    @Override
    public ProductDTO save(ProductDTO product) {
        Product entity = mapper.convertValue(product, Product.class);
        Product saved = repository.save(entity);

        return mapper.convertValue(saved, ProductDTO.class);
    }

    @Override
    public void deleteById(Long id) {
        repository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, id)));
        repository.deleteById(id);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO product) {
        if (repository.existsById(id)) {
            Product mappedProduct = mapper.convertValue(product, Product.class);
            mappedProduct.setId(id);
            Product updated = repository.save(mappedProduct);
            return mapper.convertValue(updated, ProductDTO.class);
        } else {
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, id));
        }
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
    private Specification<Product> buildSpec(String category, String brand, Double min, Double max, String keywords) {
        Specification<Product> spec = Specification.where(null);

        if (category != null) {
            spec = spec.and(ProductSpecification.categorySpec(category));
        }

        if (keywords != null) {
            spec = spec.and(ProductSpecification.titleContainingSpec(keywords));
        }

        if (category != null) {
            spec = spec.and(ProductSpecification.categorySpec(category));
        }

        if (brand != null) {
            spec = spec.and(ProductSpecification.brandSpec(brand));
        }

        if (min != null || max != null) {
            spec = spec.and(ProductSpecification.priceSpec(min, max));
        }

        return spec;
    }


}
