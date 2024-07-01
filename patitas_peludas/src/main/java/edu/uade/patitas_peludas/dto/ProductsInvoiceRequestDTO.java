package edu.uade.patitas_peludas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductsInvoiceRequestDTO {
    @JsonProperty("product_id")
    private Long productId;
    private Short quantity;
}
