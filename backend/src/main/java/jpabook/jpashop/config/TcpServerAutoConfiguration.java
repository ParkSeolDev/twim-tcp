package jpabook.jpashop.config;

import jpabook.jpashop.socket.Server;
import jpabook.jpashop.socket.TcpControllerBeanPostProcessor;
import jpabook.jpashop.socket.TcpServer;
import jpabook.jpashop.socket.TcpServerAutoStarterApplicationListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TcpServerProperties.class)
@ConditionalOnProperty(prefix = "passbook.pawnshop.tcp-server", name = {"port", "autoStart"})
public class TcpServerAutoConfiguration {

    @Bean
    TcpServerAutoStarterApplicationListener tcpServerAutoStarterApplicationListener() {
        return new TcpServerAutoStarterApplicationListener();
    }

    @Bean
    TcpControllerBeanPostProcessor tcpControllerBeanPostProcessor() {
        return new TcpControllerBeanPostProcessor();
    }

    @Bean
    Server server(){
        return new TcpServer();
    }
}