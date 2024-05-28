package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.InvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.InvoiceResponseDTO;

public interface IInvoiceService {
    InvoiceResponseDTO create(InvoiceRequestDTO invoiceRequestDTO);
    InvoiceResponseDTO getInvoiceById(Long invoiceId);
}
