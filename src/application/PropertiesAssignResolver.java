package application;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Mikhail Polivakha
 * @created 11/6/2020
 */

@Component
public class PropertiesAssignResolver {

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, String> propertiesMap;
    Logger logger = Logger.getLogger("Logger", PropertiesAssignResolver.class.getSimpleName());

    public PropertiesAssignResolver(Map<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public void assignProperties() {
        Reflections reflections = new Reflections();

        final Set<Class<? extends PropertyHolder>> allClassesToInjectProperty =
                reflections.getSubTypesOf(PropertyHolder.class);

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

        String nameOfTheFieldToInject = externalPropertyAnnotation.name();

        if (nameOfTheFieldToInject == null) {
            nameOfTheFieldToInject = field.getName();

            final String valueOfCurrentProperty = propertiesMap.get(nameOfTheFieldToInject);
            if (valueOfCurrentProperty != null) {

            } else {
                // todo: logging - if property was missed
                logger.warning("property '" + nameOfTheFieldToInject + "' was not found." +
                        " Set default value as");
            }

        } else {

        }
    }
}
