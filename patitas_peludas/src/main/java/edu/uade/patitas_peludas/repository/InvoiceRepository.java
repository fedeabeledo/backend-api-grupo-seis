package edu.uade.patitas_peludas.repository;

import edu.uade.patitas_peludas.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
