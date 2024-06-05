package edu.uade.patitas_peludas.repository;

import edu.uade.patitas_peludas.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByUserId(Long userId, Pageable pageable);
}
