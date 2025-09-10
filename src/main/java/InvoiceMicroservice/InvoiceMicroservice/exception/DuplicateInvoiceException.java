package InvoiceMicroservice.InvoiceMicroservice.exception;

public class DuplicateInvoiceException extends InvoiceProcessingException{

    public DuplicateInvoiceException(String orderNumber) {
        super("Invoice already exist for this order", orderNumber);
    }
}
