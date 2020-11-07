package application.server;

import java.lang.annotation.*;

/**
 * @author Mikhail Polivakha
 * @created 11/7/2020
 */

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalProperty {
    String name();
}
