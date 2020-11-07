package application;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */
public class PropertiesAssignResolver {

    private final Map<String, String> propertiesMap;

    public PropertiesAssignResolver(Map<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public void assignProperties() {
        Reflections reflections = new Reflections();

        final Set<Class<? extends PropertyHolder>> allClassesToInjectProperty =
                reflections.getSubTypesOf(PropertyHolder.class);

        allClassesToInjectProperty.forEach(aClass -> {
            final Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExternalProperty.class)) {

                    final ExternalProperty externalPropertyAnnotation =
                            field.getAnnotation(ExternalProperty.class);

                    String nameOfTheFieldToInject = externalPropertyAnnotation.name();

                    if (nameOfTheFieldToInject == null) {
                        nameOfTheFieldToInject = field.getName();

                        final String valueOfCurrentProperty = propertiesMap.get(nameOfTheFieldToInject);
                        if (valueOfCurrentProperty != null) {

                        } else {
                            // todo: logging - if property was missed
                        }

                    } else {

                    }
                }
            }
        });
    }
}
