package application.exceptions.property;

import java.lang.reflect.Field;

/**
 * @author Mikhail Polivakha
 * @created 11/7/2020
 */
public class PropertyException extends Exception {

    private Field fieldCausedException;
    private Object valueCausedTheException;

    public PropertyException() {
    }

    public PropertyException(String message) {
        super(message);
    }
}
