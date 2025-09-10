package InvoiceMicroservice.InvoiceMicroservice.service;

import InvoiceMicroservice.InvoiceMicroservice.model.entity.Invoice;
import InvoiceMicroservice.InvoiceMicroservice.model.entity.InvoiceItem;
import InvoiceMicroservice.InvoiceMicroservice.model.event.InvoiceRequestEvent;
import InvoiceMicroservice.InvoiceMicroservice.repository.InvoiceRepository;
import InvoiceMicroservice.InvoiceMicroservice.mapper.InvoiceMapper;
import InvoiceMicroservice.InvoiceMicroservice.exception.DuplicateInvoiceException;
import InvoiceMicroservice.InvoiceMicroservice.exception.InvoiceProcessingException;
import InvoiceMicroservice.InvoiceMicroservice.common.InvoiceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    public void processInvoiceRequest(InvoiceRequestEvent event) {
        log.info("Processing invoice request for order: {}", event.getOrderNumber());

        // Check if invoice already exists
        if (invoiceExists(event.getOrderId())) {
            throw new DuplicateInvoiceException(event.getOrderNumber());
        }

        try {
            // Create and save invoice
            Invoice invoice = createInvoiceFromEvent(event);
            Invoice savedInvoice = invoiceRepository.save(invoice);

            log.info("Invoice {} created for order {}", savedInvoice.getInvoiceNumber(), event.getOrderNumber());

            // TODO: Call external API to generate PDF
            // TODO: Send notification/email

        } catch (Exception e) {
            throw new InvoiceProcessingException(
                    "Failed to create invoice",
                    event.getOrderNumber(),
                    e
            );
        }
    }

    public boolean invoiceExists(Long orderId) {
        return invoiceRepository.existsByOrderId(orderId);
    }

    private Invoice createInvoiceFromEvent(InvoiceRequestEvent event) {
        // Map event to invoice entity
        Invoice invoice = invoiceMapper.toInvoice(event);

        // Generate invoice number
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setCreatedAt(LocalDateTime.now());

        // Map items
        List<InvoiceItem> items = event.getItems().stream()
                .map(itemDto -> {
                    InvoiceItem item = invoiceMapper.toInvoiceItem(itemDto);
                    item.setInvoice(invoice);
                    return item;
                })
                .collect(Collectors.toList());

        invoice.setItems(items);

        return invoice;
    }

    private String generateInvoiceNumber() {
        String yearPrefix = "INV-" + LocalDate.now().getYear() + "-";
        long count = invoiceRepository.countByInvoiceNumberStartingWith(yearPrefix);
        return yearPrefix + String.format("%06d", count + 1);
    }
}