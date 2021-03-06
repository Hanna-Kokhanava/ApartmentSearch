package utils.tools.files;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class ProjectDir {

    public static File getProjectResource(String identifier) {
        URL resourceURL = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(identifier),
                String.format("Not found '%s' resource", identifier));
        File resourceFile;
        try {
            resourceFile = new File(resourceURL.toURI());
        } catch (URISyntaxException e) {
            resourceFile = null;
        }
        return resourceFile;
    }
}
