package edu.uade.patitas_peludas.service.specification;

import edu.uade.patitas_peludas.entity.Product;
import jakarta.persistence.criteria.*;
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

    public static Specification<Product> petStageSpec(String petStage) {
        return (root, query, criteriaBuilder) -> {
            Stage stage = Stage.valueOf(petStage.toUpperCase());
            return criteriaBuilder.equal(root.get("petStage"), stage);
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

    public static Specification<Product> getPriceAndBestsellerOrder(String priceSort, String bestsellerSort) {
        return (root, query, builder) -> {
            query.orderBy(
                    builder.asc(
                            builder.selectCase()
                                    .when(builder.equal(root.get("stock"), (short) 0), 1)
                                    .otherwise(0)
                    ),
                    ProductSpecification.getPriceOrderExpression(root, builder, priceSort),
                    ProductSpecification.getBestsellerOrderExpression(root, builder, bestsellerSort)
            );
            return null;
        };
    }

    private static Order getPriceOrderExpression(Root<Product> root, CriteriaBuilder builder, String priceSort) {
        if (priceSort != null && priceSort.equalsIgnoreCase("desc")) {
            return builder.desc(root.get("price"));
        } else {
            return builder.asc(root.get("price"));
        }
    }

    private static Order getBestsellerOrderExpression(Root<Product> root, CriteriaBuilder builder, String bestsellerSort) {
        if (bestsellerSort != null && bestsellerSort.equalsIgnoreCase("desc")) {
            return builder.desc(root.get("bestseller"));
        } else {
            return builder.asc(root.get("bestseller"));
        }
    }

    private enum Category {CAT, DOG, FISH, HAMSTER}
    private enum Stage {BABY, ADULT, SENIOR}
}
