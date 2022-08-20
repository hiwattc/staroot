package com.staroot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import com.staroot.util.amqp.*;



@SpringBootApplication(scanBasePackages = {StarootApplication.BASE_PACKAGES})
@EnableAutoConfiguration
@EnableTransactionManagement
public class StarootApplication {
	public static final String BASE_PACKAGES = "com.staroot";
	public static String UPLOAD_DIR = "./staroot_upload_dir";
	public static String UPLOAD_DIR_CKEDITOR = "./staroot_upload_dir/ckeditor";
	public static String UPLOAD_DIR_PATH;

    /*rabbit mq test */
    /*
	public final static String queueName = "hello2";
	@Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("hello2-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }	

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }  
    
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }*/
    
    
	public static void main(String[] args) {
		SpringApplication.run(StarootApplication.class, args);
	}
}
