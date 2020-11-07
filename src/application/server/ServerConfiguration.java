package application.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mikhail Polivakha
 * @created 11/7/2020
 */

@Configuration
public class ServerConfiguration {
    public ServerConfiguration() {
    }

    @Bean
    public Server server() {
        return new Server();
    }
}
