package InvoiceMicroservice.InvoiceMicroservice.model.dto.external;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class InvoiceGeneratorRequest {
    private String from;
    private String to;
    private String currency;
    private String number;
    private String date;
    private String logo;
    private List<InvoiceGeneratorItem> items;
    private String notes;
}
