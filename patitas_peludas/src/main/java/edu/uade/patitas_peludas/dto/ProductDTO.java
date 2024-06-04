package edu.uade.patitas_peludas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    @JsonProperty("image_url")
    private String imageUrl;
    private String brand;
    @JsonProperty("pet_category")
    private String petCategory;
    @JsonProperty("pet_stage")
    private String petStage;
    @Min(0)
    @Max(5)
    private Double score;
    @JsonProperty("score_voters")
    private Short scoreVoters;
    @Min(0)
    private Double price;
    @Min(0)
    @Max(95)
    private Short discount;
    @Min(0)
    private Short stock;
    private Boolean bestseller;
}
