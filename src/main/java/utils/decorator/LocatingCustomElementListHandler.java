package utils.decorator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import utils.page_elements.elements.Element;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class LocatingCustomElementListHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<Element> clazz;

    public LocatingCustomElementListHandler(ElementLocator locator, Class<Element> clazz) {
        this.locator = locator;
        this.clazz = clazz;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elements = locator.findElements();
        List<Element> customs = new ArrayList<>();

        for (WebElement element : elements) {
            customs.add(clazz.getConstructor(WebElement.class).newInstance(element));
        }
        try {
            return method.invoke(customs, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
