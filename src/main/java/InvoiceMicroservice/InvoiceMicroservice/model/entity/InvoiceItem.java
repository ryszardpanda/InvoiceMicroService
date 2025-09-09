package InvoiceMicroservice.InvoiceMicroservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, scale = 2)
    private BigDecimal lineNet;

    @Column(nullable = false, scale = 2)
    private BigDecimal lineGross;
}
