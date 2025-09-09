package InvoiceMicroservice.InvoiceMicroservice.repository;

import InvoiceMicroservice.InvoiceMicroservice.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByOrderId(Long orderId);
    Optional<Invoice> findByOrderNumber(String orderNumber);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    boolean existsByOrderId(Long orderId);

    long countByInvoiceNumberStartingWith(String prefix);
}
