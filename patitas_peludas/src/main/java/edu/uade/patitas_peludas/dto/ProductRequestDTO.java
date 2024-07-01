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
public class ProductRequestDTO {
    @JsonProperty("user_id")
    private Long userId;
    private String title;
    private String description;
    @JsonProperty("image_url")
    private String imageUrl;
    private String brand;
    @JsonProperty("pet_category")
    private String petCategory;
    @JsonProperty("pet_stage")
    private String petStage;
    private Double price;
    private Short discount;
    private Short stock;
}
