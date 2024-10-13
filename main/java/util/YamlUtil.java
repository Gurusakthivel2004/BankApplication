package util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlUtil {
    private static Map<String, Map<String, String>>  mappings;

    static {
        loadMappings();
    }

    public static void loadMappings() {
        try (InputStream inputStream = new FileInputStream("/home/guru-pt7672/eclipse-workspace/Bank_Application/src/main/mappings.yaml")) {
            Yaml yaml = new Yaml();
            mappings = yaml.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load YAML mappings", e);
        }
    }

    public static Map<String, String> getMapping(String tableName) {
        if (mappings.containsKey(tableName)) {
            return mappings.get(tableName);
        } else {
            throw new IllegalArgumentException("Mapping for table '" + tableName + "' not found in YAML.");
        }
    }
}