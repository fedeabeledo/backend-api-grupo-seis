package edu.uade.patitas_peludas.service.specification;

import edu.uade.patitas_peludas.entity.Product;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> categorySpec(String category) {
        return (root, query, criteriaBuilder) -> {
            Category petCategory = Category.valueOf(category.toUpperCase());
            return criteriaBuilder.equal(root.get("petCategory"), petCategory);
        };
    }

    public static Specification<Product> brandSpec(String brand) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand"), brand);
    }

    public static Specification<Product> priceSpec(Double min, Double max) {
        return (root, query, criteriaBuilder) -> {
            Path<Double> price = root.get("price");

            if (min != null && max != null) {
                return criteriaBuilder.between(price, min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(price, min);
            } else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(price, max);
            }

            return null;
        };
    }

    public static Specification<Product> titleContainingSpec(String keyword) {
        String[] keywords = keyword.toLowerCase().split("\\s+");

        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[keywords.length];

            for (int i = 0; i < keywords.length; i++) {
                predicates[i] = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + keywords[i] + "%"
                );
            }

            return criteriaBuilder.and(predicates);
        };
    }

    private enum Category {CAT, DOG, FISH, HAMSTER}
}
