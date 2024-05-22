package edu.uade.patitas_peludas.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @JsonProperty("image_url")
    @Column(name = "image_url")
    private String image_url;

    @Column
    private String brand;

    @JsonProperty("pet_category")
    @Column(name = "pet_category")
    private PetCategory pet_category;

    @JsonProperty("pet_stage")
    @Column(name = "pet_stage")
    private PetStage pet_stage;

    @Column
    private Double score;

    @JsonProperty("score_voters")
    @Column(name = "score_voters")
    private Short score_voters;

    @Column
    private Double price;

    @Column
    private Short discount;

    @Column
    private Short stock;

    @Getter
    private enum PetCategory { CAT, DOG, FISH, HAMSTER }

    @Getter
    private enum PetStage { BABY, ADULT, SENIOR }
}
