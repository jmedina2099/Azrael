package org.azrael.main;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan({ "org.azrael.api" })
@Configuration(proxyBeanMethods=false)
public class Application {
	
	private int port = 4200;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	private String getExternalIP() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostAddress = inetAddress.getHostAddress();
			System.out.println( "host="+hostAddress );
			return hostAddress;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "localhost";
	}

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:"+port,"http://" + getExternalIP() +":"+port );
            }
        };
    }
	
}