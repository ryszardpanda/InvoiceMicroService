package InvoiceMicroservice.InvoiceMicroservice.model.entity;

import InvoiceMicroservice.InvoiceMicroservice.common.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String invoiceNumber;

    @Column(nullable = false, length = 30)
    private String orderNumber;

    @Column(nullable = false)
    private Long orderId;

    @Embedded @AttributeOverrides({
            @AttributeOverride(name = "firstName",  column=@Column(name="order_first_name")),
            @AttributeOverride(name = "lastName",   column=@Column(name="order_last_name")),
            @AttributeOverride(name = "street",     column=@Column(name="order_street")),
            @AttributeOverride(name = "city",       column=@Column(name="order_city")),
            @AttributeOverride(name = "zip",        column=@Column(name="order_zip")),
            @AttributeOverride(name = "phone",      column=@Column(name="order_phone"))
    })
    private InvoiceAddress invoiceAddress;

    @Column(nullable = false, scale = 2)
    private BigDecimal totalNet;
    @Column(nullable = false, scale = 2)
    private BigDecimal totalGross;
    @Column(nullable = false, scale = 2)
    private BigDecimal shippingCost = BigDecimal.ZERO;
    @Column(nullable = false, length = 3)
    private String currency = "PLN";

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus status = InvoiceStatus.PENDING;

    @Column(nullable = false, length = 36)
    private String userId;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items = new ArrayList<>();

    private String externalInvoiceId;
    private String pdfUrl;

    private String companyName = "Cooooompoooooteeeer Sp. z o.o.";
    private String companyAddress = "ul. Główna 1, 00-001 Warszawa";
    private String companyTaxId = "PL1234567890";
}
