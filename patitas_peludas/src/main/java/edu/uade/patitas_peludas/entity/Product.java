package edu.uade.patitas_peludas.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String imageUrl;

    @Column
    private String brand;

    @JsonProperty("pet_category")
    @Column(name = "pet_category")
    private PetCategory petCategory;

    @JsonProperty("pet_stage")
    @Column(name = "pet_stage")
    private PetStage petStage;

    @Column
    private Double score;

    @JsonProperty("score_voters")
    @Column(name = "score_voters")
    private Short scoreVoters;

    @Column
    private Double price;

    @Column
    private Short discount;

    @Column
    private Short stock;

    @Column
    private Boolean bestseller;

    @Getter
    private enum PetCategory { CAT, DOG, FISH, HAMSTER }

    @Getter
    private enum PetStage { BABY, ADULT, SENIOR }
}