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

    private UserResponseDTO user;

    private Short discount;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_cost")
    private Double shippingCost;

    @JsonProperty("payment_method")
    private String paymentMethod;

    private Double total;
}
