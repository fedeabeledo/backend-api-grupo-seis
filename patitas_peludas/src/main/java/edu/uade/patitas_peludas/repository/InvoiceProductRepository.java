package edu.uade.patitas_peludas.repository;

import edu.uade.patitas_peludas.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    List<InvoiceProduct> findByInvoiceId(Long invoiceId);
}
