package InvoiceMicroservice.InvoiceMicroservice.mapper;

import InvoiceMicroservice.InvoiceMicroservice.model.entity.Invoice;
import InvoiceMicroservice.InvoiceMicroservice.model.entity.InvoiceItem;
import InvoiceMicroservice.InvoiceMicroservice.model.dto.InvoiceItemDto;
import InvoiceMicroservice.InvoiceMicroservice.model.event.InvoiceRequestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "generatedAt", ignore = true)
    @Mapping(target = "externalInvoiceId", ignore = true)
    @Mapping(target = "pdfUrl", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "invoiceAddress.firstName", source = "firstName")
    @Mapping(target = "invoiceAddress.lastName", source = "lastName")
    @Mapping(target = "invoiceAddress.street", source = "street")
    @Mapping(target = "invoiceAddress.city", source = "city")
    @Mapping(target = "invoiceAddress.zip", source = "zip")
    @Mapping(target = "invoiceAddress.phone", source = "phone")
    Invoice toInvoice(InvoiceRequestEvent event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoice", ignore = true)
    InvoiceItem toInvoiceItem(InvoiceItemDto dto);
}