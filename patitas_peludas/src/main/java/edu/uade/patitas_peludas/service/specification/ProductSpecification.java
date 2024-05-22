package edu.uade.patitas_peludas.service.specification;

import edu.uade.patitas_peludas.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    private enum Category { CAT, DOG, FISH, HAMSTER }

    public static Specification<Product> categorySpec(String category) {
        return (root, query, criteriaBuilder) -> {
            Category petCategory = Category.valueOf(category.toUpperCase());
            return criteriaBuilder.equal(root.get("petCategory"), petCategory);
        };
    }
}
