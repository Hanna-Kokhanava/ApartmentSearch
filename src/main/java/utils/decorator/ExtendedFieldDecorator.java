package utils.decorator;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import utils.page_elements.containers.Container;
import utils.page_elements.containers.ContainerFactory;
import utils.page_elements.containers.impl.DefaultContainerFactory;
import utils.page_elements.elements.Element;
import utils.page_elements.elements.ElementFactory;
import utils.page_elements.elements.impl.DefaultElementFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;

public class ExtendedFieldDecorator extends DefaultFieldDecorator {
    private ElementFactory elementFactory = new DefaultElementFactory();
    private ContainerFactory containerFactory = new DefaultContainerFactory();

    public ExtendedFieldDecorator(final SearchContext searchContext) {
        super(new DefaultElementLocatorFactory(searchContext));
    }

    /**
     * Decorate field
     */
    @Override
    public Object decorate(final ClassLoader loader, final Field field) {
        Class<?> decoratableClass = decoratableClass(field);

        if (decoratableClass != null) {
            ElementLocator locator = factory.createLocator(field);
            if (locator == null) {
                return null;
            }

            if (List.class.isAssignableFrom(field.getType())) {
                return createList(loader, locator, (Class<Element>) decoratableClass);
            }

            if (Container.class.isAssignableFrom(field.getType())) {
                return createContainer(loader, locator, (Class<Container>) decoratableClass);
            }

            if (Element.class.isAssignableFrom(field.getType())) {
                return createElement(loader, locator, (Class<Element>) decoratableClass);
            }
        }
        return super.decorate(loader, field);
    }

    /**
     * Returns decoratable field class or null if class is not suitable for decorator
     */
    private Class<?> decoratableClass(Field field) {
        Class<?> clazz = field.getType();

        if (List.class.isAssignableFrom(clazz)) {
            if (field.getAnnotation(FindBy.class) == null &&
                    field.getAnnotation(FindBys.class) == null) {
                return null;
            }

            Type genericType = field.getGenericType();
            if (!(genericType instanceof ParameterizedType)) {
                return null;
            }
            clazz = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        }

        if (Element.class.isAssignableFrom(clazz)) {
            return clazz;
        }
        return null;
    }

    /**
     * Finds WebElement and pass it to Element custom class
     */
    private Element createElement(ClassLoader loader, ElementLocator locator, Class<Element> clazz) {
        WebElement proxy = proxyForLocator(loader, locator);
        return elementFactory.create(clazz, proxy);
    }

    /**
     * Finds WebElement and pass it to Container custom class
     */
    private Container createContainer(ClassLoader loader, ElementLocator locator, Class<Container> clazz) {
        WebElement element = proxyForLocator(loader, locator);
        Container container = containerFactory.create(clazz, element);
        PageFactory.initElements(new ExtendedFieldDecorator(element), container);
        return container;
    }

    @SuppressWarnings("unchecked")
    private List<Element> createList(ClassLoader loader, ElementLocator locator, Class<Element> clazz) {
        LocatingCustomElementListHandler handler = new LocatingCustomElementListHandler(locator, clazz);
        return (List<Element>) Proxy.newProxyInstance(loader, new Class<?>[]{List.class}, handler);
    }
}
