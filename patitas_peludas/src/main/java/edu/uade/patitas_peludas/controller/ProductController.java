package edu.uade.patitas_peludas.controller;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.service.IProductService;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(value = "/api/product")
public class ProductController {
    @Autowired
    private IProductService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDTO<ProductDTO>> findAll(@RequestParam(required = false) @Pattern(regexp = "(?i)cat|dog|fish|hamster") String category,
                                                       @RequestParam(required = false) @Size(min = 3) String brand,
                                                       @RequestParam(required = false) @PositiveOrZero Double min,
                                                       @RequestParam(required = false) @Positive Double max,
                                                       @RequestParam(required = false) @Pattern(regexp = "(?i)asc|desc") String sort,
                                                       @RequestParam(required = true) @PositiveOrZero Short page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(category, brand, min, max, sort, page));
    }
}