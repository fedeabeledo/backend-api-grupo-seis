package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    List<ProductDTO> findAll();
}
