import application.PropertiesScanner;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

@ComponentScan(basePackages = {"application"})
public class Main {
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
//        new application.Server((request, response) -> "<html><body><h1>Hello From Tomcat-2.0</h1></body></html>").bootstrap();
        new PropertiesScanner().scanPropertiesFile();
    }
}

