package edu.uade.patitas_peludas.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.entity.Product;
import edu.uade.patitas_peludas.repository.ProductRepository;
import edu.uade.patitas_peludas.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<ProductDTO> findAll() {
        return repository.findAll().stream().map(product ->
                mapper.convertValue(product, ProductDTO.class)).collect(Collectors.toList());
    }
}
