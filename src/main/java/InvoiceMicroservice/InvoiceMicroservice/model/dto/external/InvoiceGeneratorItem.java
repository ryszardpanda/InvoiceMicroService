package InvoiceMicroservice.InvoiceMicroservice.model.dto.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceGeneratorItem {
    private String name;
    private Integer quantity;
    private String unit_cost;
}
