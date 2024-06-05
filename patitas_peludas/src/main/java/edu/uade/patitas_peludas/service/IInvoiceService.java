package edu.uade.patitas_peludas.service;

import edu.uade.patitas_peludas.dto.InvoiceRequestDTO;
import edu.uade.patitas_peludas.dto.InvoiceResponseDTO;
import edu.uade.patitas_peludas.dto.PageDTO;

public interface IInvoiceService {
    InvoiceResponseDTO create(InvoiceRequestDTO invoiceRequestDTO);
    InvoiceResponseDTO findById(Long invoiceId);
    PageDTO<InvoiceResponseDTO> findByUserId(Long userId, Short page);
}
