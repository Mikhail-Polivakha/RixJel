package application.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

@ComponentScan(basePackages = {"src.application.server"})
public class Main {
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
//        new application.Server((request, response) -> "<html><body><h1>Hello From Tomcat-2.0</h1></body></html>").bootstrap();
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerConfiguration.class);
        new ExternalPropertyFileScanner().scanPropertiesFile();
    }
}