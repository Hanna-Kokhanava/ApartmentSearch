package utils.tools;

import utils.tools.files.ProjectDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static com.google.common.base.Strings.isNullOrEmpty;

public final class PropertyLoader {

    /**
     * Load all properties from file.
     *
     * @param propertyFile the properties file
     * @return the properties
     */
    public static Properties loadPropertiesFromFile(File propertyFile) {
        Properties result = new Properties();
        File testPropertiesResourceFile = Objects.requireNonNull(propertyFile,
                String.format("Not found '%s' resource", propertyFile));

        try (InputStream stream = new FileInputStream(testPropertiesResourceFile)) {
            result.load(stream);
        } catch (IOException e) {
        }
        return result;
    }
}
