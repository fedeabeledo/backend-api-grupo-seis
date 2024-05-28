package edu.uade.patitas_peludas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    private Double score;
    @JsonProperty("score_voters")
    private Short scoreVoters;
    private Double price;
    private Short discount;
    private Short stock;
    private Boolean bestseller;
}
