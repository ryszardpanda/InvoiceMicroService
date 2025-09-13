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
    private final ExternalInvoiceService externalInvoiceService;

    public void processInvoiceRequest(InvoiceRequestEvent event) {
        log.info("Processing invoice request for order: {}", event.getOrderNumber());

        log.info("ExternalInvoiceService is: {}", externalInvoiceService != null ? "injected" : "NULL");

        if (invoiceExists(event.getOrderId())) {
            throw new DuplicateInvoiceException(event.getOrderNumber());
        }

        try {
            Invoice invoice = createInvoiceFromEvent(event);
            Invoice savedInvoice = invoiceRepository.save(invoice);

            log.info("Invoice {} created for order {}", savedInvoice.getInvoiceNumber(), event.getOrderNumber());

            log.info("About to call generatePdfForInvoice...");

            generatePdfForInvoice(savedInvoice);

            log.info("generatePdfForInvoice completed");

        } catch (Exception e) {
            log.error("Exception in processInvoiceRequest: ", e);
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
        Invoice invoice = invoiceMapper.toInvoice(event);

        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setCreatedAt(LocalDateTime.now());

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

    private void generatePdfForInvoice(Invoice invoice) {
        log.info("ENTERED generatePdfForInvoice for: {}", invoice.getInvoiceNumber());

        invoice.setStatus(InvoiceStatus.PROCESSING);
        invoiceRepository.save(invoice);

        try {
            String pdfUrl = externalInvoiceService.generateInvoicePdf(invoice);

            invoice.setPdfUrl(pdfUrl);
            invoice.setStatus(InvoiceStatus.GENERATED);
            invoice.setGeneratedAt(LocalDateTime.now());
            invoice.setExternalInvoiceId("PDF_GENERATED");

            invoiceRepository.save(invoice);

            log.info("Invoice PDF generated for: {}", invoice.getInvoiceNumber());

        } catch (Exception e) {
            invoice.setStatus(InvoiceStatus.FAILED);
            invoiceRepository.save(invoice);
            log.error("Failed to generate PDF for invoice: {}", invoice.getInvoiceNumber(), e);
            throw e;
        }
    }
}