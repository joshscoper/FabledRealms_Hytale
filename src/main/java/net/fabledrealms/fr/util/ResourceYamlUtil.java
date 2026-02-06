package net.fabledrealms.fr.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class ResourceYamlUtil {

    private ResourceYamlUtil() {}

    /**
     * Copies a YAML template from resources to the target path if it does not exist.
     *
     * @param resourcePath path inside resources (e.g. "templates/config-template.yml")
     * @param targetPath   destination file path
     * @param overwrite    whether to overwrite if file exists
     */
    public static void createYamlFromTemplate(
            String resourcePath,
            Path targetPath,
            boolean overwrite
    ) throws IOException {

        // Ensure parent directories exist
        Files.createDirectories(targetPath.getParent());

        if (Files.exists(targetPath) && !overwrite) {
            return;
        }

        try (InputStream in = Objects.requireNonNull(
                ResourceYamlUtil.class
                        .getClassLoader()
                        .getResourceAsStream(resourcePath),
                "Missing resource: " + resourcePath
        )) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
