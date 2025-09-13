package InvoiceMicroservice.InvoiceMicroservice.service;
import InvoiceMicroservice.InvoiceMicroservice.client.InvoiceGeneratorClient;
import InvoiceMicroservice.InvoiceMicroservice.model.entity.Invoice;
import InvoiceMicroservice.InvoiceMicroservice.model.dto.external.InvoiceGeneratorRequest;
import InvoiceMicroservice.InvoiceMicroservice.model.dto.external.InvoiceGeneratorItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalInvoiceService {

    private final InvoiceGeneratorClient client;

    @Value("${invoice.generator.api.key}")
    private String apiKey;

    public String generateInvoicePdf(Invoice invoice) {
        log.info("Generating PDF for invoice: {}", invoice.getInvoiceNumber());

        InvoiceGeneratorRequest request = buildRequest(invoice);

        ResponseEntity<byte[]> response = client.generateInvoice(
                "Bearer " + apiKey,
                "application/json",
                request
        );

        String pdfUrl = savePdfAndGetUrl(response.getBody(), invoice.getInvoiceNumber());

        log.info("PDF generated successfully for invoice: {}", invoice.getInvoiceNumber());
        return pdfUrl;
    }

    private String savePdfAndGetUrl(byte[] pdfBytes, String invoiceNumber) {
        try {
            String fileName = "invoice_" + invoiceNumber + ".pdf";
            Path invoicesDir = Paths.get("invoices");
            Files.createDirectories(invoicesDir);

            Path filePath = invoicesDir.resolve(fileName);
            Files.write(filePath, pdfBytes);

            log.info("PDF saved to: {}", filePath.toAbsolutePath());
            return "file://" + filePath.toAbsolutePath();

        } catch (Exception e) {
            log.error("Failed to save PDF for invoice: {}", invoiceNumber, e);
            throw new RuntimeException("Failed to save PDF", e);
        }
    }

    private InvoiceGeneratorRequest buildRequest(Invoice invoice) {
        return InvoiceGeneratorRequest.builder()
                .from(buildCompanyInfo(invoice))
                .to(buildCustomerInfo(invoice))
                .currency(invoice.getCurrency())
                .number(invoice.getInvoiceNumber())
                .date(invoice.getCreatedAt().toLocalDate().toString())
                .items(mapItems(invoice))
                .notes("Dziękujemy za zakup!")
                .build();
    }

    private String buildCompanyInfo(Invoice invoice) {
        return invoice.getCompanyName() + "\n" +
                invoice.getCompanyAddress() + "\n" +
                "NIP: " + invoice.getCompanyTaxId();
    }

    private String buildCustomerInfo(Invoice invoice) {
        return invoice.getInvoiceAddress().getFullName() + "\n" +
                invoice.getInvoiceAddress().getFullAddress() + "\n" +
                "Tel: " + invoice.getInvoiceAddress().getPhone();
    }

    private List<InvoiceGeneratorItem> mapItems(Invoice invoice) {
        return invoice.getItems().stream()
                .map(item -> InvoiceGeneratorItem.builder()
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .unit_cost(item.getUnitPrice().toString())
                        .build())
                .collect(Collectors.toList());
    }
}