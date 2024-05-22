package edu.uade.patitas_peludas.service.specification;

import edu.uade.patitas_peludas.entity.Product;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    private enum Category { CAT, DOG, FISH, HAMSTER }

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
}
