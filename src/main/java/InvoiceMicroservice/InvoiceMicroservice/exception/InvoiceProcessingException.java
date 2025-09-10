package InvoiceMicroservice.InvoiceMicroservice.exception;

import lombok.Getter;

@Getter
public class InvoiceProcessingException extends RuntimeException{
    private final String orderNumber;

    public InvoiceProcessingException(String message, String orderNumber) {
        super(message);
        this.orderNumber = orderNumber;
    }

    public InvoiceProcessingException(String message, String orderNumber, Throwable cause) {
        super(message, cause);
        this.orderNumber = orderNumber;
    }
}
