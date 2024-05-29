package edu.uade.patitas_peludas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponseDTO {
    private List<ProductInvoiceDTO> products;
    private UserDTO user;
    private Short discount;
    @JsonProperty("shipping_cost")
    private Short shippingCost;
    private Double total;
}
