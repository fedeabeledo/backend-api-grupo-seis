package edu.uade.patitas_peludas.controller;

import edu.uade.patitas_peludas.dto.InvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.InvoiceResponseDTO;
import edu.uade.patitas_peludas.dto.PageDTO;
import edu.uade.patitas_peludas.service.IInvoiceService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/api/invoice")
public class InvoiceController {
    @Autowired
    private IInvoiceService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvoiceResponseDTO> create(@RequestBody @Validated InvoiceRequestDTO invoiceRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(invoiceRequestDTO));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDTO<InvoiceResponseDTO>> getInvoicesByUserId(@PathVariable Long userId, @RequestParam(required = false) @NotNull @PositiveOrZero Short page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByUserId(userId, page));
    }

}
