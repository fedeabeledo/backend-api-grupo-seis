package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductRequestDTO;
import edu.uade.patitas_peludas.dto.ProductResponseDTO;

public interface IProductService {
    PageDTO<ProductResponseDTO> findAll(String keywords, String category, String brand, Double min, Double max,
                                        String priceSort, String bestsellerSort, Short page);

    ProductResponseDTO findById(Long id);
    PageDTO<ProductResponseDTO> findProductByUserId(Long userId, Short page);

    ProductResponseDTO save(ProductRequestDTO product);

    void deleteById(Long id);

    ProductResponseDTO update(Long id, ProductRequestDTO product);
}
