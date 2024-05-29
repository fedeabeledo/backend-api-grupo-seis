package edu.uade.patitas_peludas.controller;

import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.dto.ProductDTO;
import edu.uade.patitas_peludas.service.IProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
                                                       @RequestParam(required = false) @NotNull @PositiveOrZero Short page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(category, brand, min, max, sort, page));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> save(@RequestBody @Validated ProductDTO product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody @Validated ProductDTO product) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, product));
    }
}