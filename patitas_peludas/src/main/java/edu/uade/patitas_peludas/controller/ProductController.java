package edu.uade.patitas_peludas.controller;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {
    @Autowired
    private IProductService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDTO<ProductDTO>> findAll(@RequestParam(required = false) String category,
                                                       @RequestParam(required = false) String brand,
                                                       @RequestParam(required = false) Double min,
                                                       @RequestParam(required = false) Double max,
                                                       @RequestParam(required = true) Short page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(category, brand, min, max, page));
    }
}
