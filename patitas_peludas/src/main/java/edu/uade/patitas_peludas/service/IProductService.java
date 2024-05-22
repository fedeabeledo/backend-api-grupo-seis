package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;

public interface IProductService {
    PageDTO<ProductDTO> findAll(String category, Short page);
}
