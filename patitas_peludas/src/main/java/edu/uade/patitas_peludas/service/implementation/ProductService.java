package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.entity.Product;
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
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public PageDTO<ProductDTO> findAll(String category, String brand, Double min, Double max, String sort, Short page) {
        Pageable pageable = buildPageable(sort, page);
        Specification<Product> spec = buildSpec(category, brand, min, max);

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

    private Pageable buildPageable(String sort, Short page) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sort != null) {
            if (sort.equalsIgnoreCase("desc")) {
                orders.add(Sort.Order.desc("title"));
            } else if (sort.equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc("title"));
            }
        }

        Sort sorted = Sort.by(orders);
        return PageRequest.of(page, 12, sorted);
    }

    private Specification<Product> buildSpec(String category, String brand, Double min, Double max) {
        Specification<Product> spec = Specification.where(null);

        if (category != null) {
            spec= spec.and(ProductSpecification.categorySpec(category));
        }

        if (brand != null) {
            spec= spec.and(ProductSpecification.brandSpec(brand));
        }

        if (min != null || max != null) {
            spec= spec.and(ProductSpecification.priceSpec(min, max));
        }

        return spec;
    }
}