package InvoiceMicroservice.InvoiceMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InvoiceMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceMicroserviceApplication.class, args);
	}

}
