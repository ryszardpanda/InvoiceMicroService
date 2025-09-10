package InvoiceMicroservice.InvoiceMicroservice.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String INVOICE_EXCHANGE = "invoice.exchange";
    public static final String INVOICE_QUEUE = "invoice.queue";
    public static final String INVOICE_ROUTING_KEY = "invoice.create";

    @Bean
    public TopicExchange invoiceExchange() {
        return new TopicExchange(INVOICE_EXCHANGE);
    }

    @Bean
    public Queue invoiceQueue() {
        return new Queue(INVOICE_QUEUE, true); // durable=true
    }

    @Bean
    public Binding invoiceBinding() {
        return BindingBuilder
                .bind(invoiceQueue())
                .to(invoiceExchange())
                .with(INVOICE_ROUTING_KEY);
    }
}
