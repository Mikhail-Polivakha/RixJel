package application.server;

import application.exceptions.property.IllegalPropertyValueFormatException;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

@Component
public class PropertiesAssignResolver {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, String> propertiesMap;
    Logger logger = Logger.getLogger("Logger", PropertiesAssignResolver.class.getSimpleName());

    public PropertiesAssignResolver() {

    }

    public void setPropertiesMap(Map<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public void assignProperties() {
        Reflections reflections = new Reflections();
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            injectPropertiesIfRequired(applicationContext.getBean(name));
        }
    }

    private void injectPropertiesIfRequired(Object bean) {
        final Class<?> aClass = bean.getClass();
        final Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ExternalProperty.class)) {
                injectPropertyIntoField(field, bean);
            }
        }
    }

    private void injectPropertyIntoField(Field field, Object targetObject) {
        final ExternalProperty externalPropertyAnnotation =
                field.getAnnotation(ExternalProperty.class);

        String nameOfTheExternalFilePropertyToInject = externalPropertyAnnotation.name();

        if (nameOfTheExternalFilePropertyToInject == null || nameOfTheExternalFilePropertyToInject.isEmpty()) {
            nameOfTheExternalFilePropertyToInject = field.getName();
        }

        setToPassedFieldValue(field, targetObject, nameOfTheExternalFilePropertyToInject);
    }

    private void setToPassedFieldValue(Field field,
                                       Object targetObject,
                                       String nameOfTheExternalFilePropertyToInject) {
        final String valueOfCurrentProperty = propertiesMap.get(nameOfTheExternalFilePropertyToInject);

        if (valueOfCurrentProperty != null) {

            field.setAccessible(true);
            final Class<?> fieldType = field.getType();

            try {
                injectFieldByType(field, targetObject,
                        nameOfTheExternalFilePropertyToInject, valueOfCurrentProperty, fieldType);
            } catch (IllegalAccessException e) {
                logger.warning("WARNING!: Property with name: '" +
                        nameOfTheExternalFilePropertyToInject + "' was not injected, because of access denied");
            } catch (IllegalPropertyValueFormatException e) {
                logger.warning("WARNING!: Property with name: '" +
                        nameOfTheExternalFilePropertyToInject + "' has invalid format");
            }

        } else {
            logger.warning("Value of the property '" + nameOfTheExternalFilePropertyToInject + "' was not found.");
        }
    }

    private void injectFieldByType(Field field,
                                   Object targetObject,
                                   String nameOfTheExternalFilePropertyToInject,
                                   String valueOfCurrentProperty, Class<?> fieldType)
            throws IllegalAccessException, IllegalPropertyValueFormatException {

        if (fieldType.getSimpleName().equals(String.class.getSimpleName())) {

            // Value is a string
            field.set(targetObject, valueOfCurrentProperty);

        } else {
            // Value is a Number (integer, long, point here that value will NEVER be float or double)
            if (StringUtils.isNumeric(valueOfCurrentProperty)) {
                field.set(targetObject, Integer.parseInt(valueOfCurrentProperty));
            } else {
                throw new IllegalPropertyValueFormatException(
                        "Property value: " + String.valueOf(valueOfCurrentProperty) +
                                " set for property " + nameOfTheExternalFilePropertyToInject
                                + "is not obtainable", field, valueOfCurrentProperty);
            }
        }
    }
}
