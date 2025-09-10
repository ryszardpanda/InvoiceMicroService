package InvoiceMicroservice.InvoiceMicroservice.rabbit;

import InvoiceMicroservice.InvoiceMicroservice.exception.DuplicateInvoiceException;
import InvoiceMicroservice.InvoiceMicroservice.exception.InvoiceProcessingException;
import InvoiceMicroservice.InvoiceMicroservice.model.event.InvoiceRequestEvent;
import InvoiceMicroservice.InvoiceMicroservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceEventListener {

    private final InvoiceService invoiceService;

    @RabbitListener(queues = "invoice.queue")
    public void handleInvoiceRequest(InvoiceRequestEvent event) {
        log.info("Received invoice request for order: {}", event.getOrderNumber());

        try {
            invoiceService.processInvoiceRequest(event);
            log.info("Successfully processed invoice request for order: {}", event.getOrderNumber());

        } catch (DuplicateInvoiceException e) {
            log.warn("Duplicate invoice for order: {} - {}", e.getOrderNumber(), e.getMessage());
            // Duplicate is not critical error, no retry

        } catch (InvoiceProcessingException e) {
            log.error("Error processing invoice for order: {} - {}", e.getOrderNumber(), e.getMessage(), e);
            // Can configure retry or DLQ

        } catch (Exception e) {
            log.error("Unexpected error for order: {}", event.getOrderNumber(), e);
            throw e; // Re-throw for RabbitMQ retry mechanism
        }
    }
}
