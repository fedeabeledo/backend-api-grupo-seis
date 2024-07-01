package edu.uade.patitas_peludas.repository;

import edu.uade.patitas_peludas.entity.PetCategory;
import edu.uade.patitas_peludas.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
    Page<Product> findProductByUserId(Long userId, Pageable pageable);
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.petCategory = ?1")
    List<String> findAllBrandsByCategory(PetCategory category);
    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findAllBrands();
}
