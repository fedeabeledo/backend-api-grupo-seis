package edu.uade.patitas_peludas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequestDTO {
    private Map<Long, Short> products;

    @JsonProperty("user_id")
    private Long userId;

    @Min(0)
    @Max(100)
    private Short discount;

    @Min(0)
    @JsonProperty("shipping_cost")
    private Short shippingCost;
}
