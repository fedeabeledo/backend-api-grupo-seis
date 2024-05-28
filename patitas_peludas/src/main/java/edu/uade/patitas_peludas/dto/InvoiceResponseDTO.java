package edu.uade.patitas_peludas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponseDTO {
    private List<ProductInvoiceDTO> products;
    private Short discount;
    private Short shippingCost;
    private Double total;
}
