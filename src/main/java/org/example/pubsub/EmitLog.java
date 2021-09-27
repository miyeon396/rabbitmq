package org.example.pubsub;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
//@PropertySource(value = "classpath:application.properties")
@ComponentScan("org.example.pubsub")
@PropertySource("classpath:/application.properties")
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static String addr;

    @Value("${spring.rabbitmq.addresses}")
    public void setAddr(String value) {
        addr = value;
    }

    public static void main(String[] argv) throws Exception {
        System.out.println("123"+addr);

//        System.out.println("host=="+host);
        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("");
    }
}
