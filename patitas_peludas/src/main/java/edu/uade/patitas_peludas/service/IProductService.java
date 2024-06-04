package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;

public interface IProductService {
    PageDTO<ProductDTO> findAll(String keywords, String category, String brand, Double min, Double max,
                                String priceSort, String bestsellerSort, Short page);

    ProductDTO findById(Long id);

    ProductDTO save(ProductDTO product);

    void deleteById(Long id);

    ProductDTO update(Long id, ProductDTO product);
}
