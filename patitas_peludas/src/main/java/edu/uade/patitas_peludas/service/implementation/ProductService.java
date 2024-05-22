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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public PageDTO<ProductDTO> findAll(String category, Short page) {
        Pageable pageable = PageRequest.of(page, 12);
        Specification<Product> spec = buildSpec(category);

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

    private Specification<Product> buildSpec(String category) {
        Specification<Product> spec = Specification.where(null);

        if (category != null) {
            spec= spec.and(ProductSpecification.categorySpec(category));
        }

        return spec;
    }
}
