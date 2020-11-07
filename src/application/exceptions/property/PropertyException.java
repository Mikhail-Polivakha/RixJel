package application.exceptions.property;

import java.lang.reflect.Field;

/**
 * @author Mikhail Polivakha
 * @created 11/7/2020
 */
public class PropertyException extends Exception {

    private Field fieldCausedTheException;
    private Object valueToInject;
    public PropertyException() {
    }

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, Field fieldCausedException, Object valueCausedTheException) {
        super(message);
        this.valueToInject = valueCausedTheException;
        this.fieldCausedTheException = fieldCausedException;
    }

}
