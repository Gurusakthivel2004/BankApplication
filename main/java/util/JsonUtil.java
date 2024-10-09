package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonUtil {
    private static Map<String, Map<String, String>> columnMappings;

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            columnMappings = objectMapper.readValue(
                new File("/home/guru-pt7672/eclipse-workspace/Bank_Application/src/main/columnMapping.json"),
                Map.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getMapping(String tableName) {
        return columnMappings.get(tableName);
    }
}
