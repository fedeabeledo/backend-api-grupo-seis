package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.InvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.InvoiceResponseDTO;

import java.util.List;

public interface IInvoiceService {
    InvoiceResponseDTO create(InvoiceRequestDTO invoiceRequestDTO);
    InvoiceResponseDTO findById(Long invoiceId);
    List<InvoiceResponseDTO> findByUserId(Long userId);
}
