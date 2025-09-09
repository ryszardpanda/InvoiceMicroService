package InvoiceMicroservice.InvoiceMicroservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {
    private Long productId;
    private String name;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineNet;
    private BigDecimal lineGross;
}
