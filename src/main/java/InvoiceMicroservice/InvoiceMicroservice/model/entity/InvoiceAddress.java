package InvoiceMicroservice.InvoiceMicroservice.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceAddress {
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String zip;
    private String phone;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFullAddress() {
        return street + ", " + zip + " " + city;
    }
}
