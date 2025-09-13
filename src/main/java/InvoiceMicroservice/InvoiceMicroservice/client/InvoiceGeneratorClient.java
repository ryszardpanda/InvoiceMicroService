package InvoiceMicroservice.InvoiceMicroservice.client;

import InvoiceMicroservice.InvoiceMicroservice.model.dto.external.InvoiceGeneratorRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "invoice-generator", url = "https://invoice-generator.com")
public interface InvoiceGeneratorClient {

    @PostMapping(value = "/",
            consumes = "application/json",
            produces = "application/pdf")
    ResponseEntity<byte[]> generateInvoice(
            @RequestHeader("Authorization") String apiKey,
            @RequestHeader("Content-Type") String contentType,
            @RequestBody InvoiceGeneratorRequest request
    );
}