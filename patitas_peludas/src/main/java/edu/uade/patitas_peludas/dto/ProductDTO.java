package edu.uade.patitas_peludas.dto;

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
    private String image_url;
    private String brand;
    private String pet_category;
    private String pet_stage;
    private Double score;
    private Short score_voters;
    private Double price;
    private Short discount;
    private Short stock;
}
