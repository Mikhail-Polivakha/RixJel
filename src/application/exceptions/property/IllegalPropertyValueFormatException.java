package application.exceptions.property;

import java.lang.reflect.Field;

/**
 * @author Mikhail Polivakha
 * @created 11/7/2020
 */
public class IllegalPropertyValueFormatException extends PropertyException {

    public IllegalPropertyValueFormatException(String message, Field fieldCausedException, Object valueCausedTheException) {
        super(message, fieldCausedException, valueCausedTheException);
    }

    public IllegalPropertyValueFormatException() {
    }

    public IllegalPropertyValueFormatException(String message) {
        super(message);
    }
}
