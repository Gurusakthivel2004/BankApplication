package util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ColumnYamlUtil {
    // This Map stores mappings for each table under the 'tables' key
    private static Map<String, ClassMapping> mappings;

    static {
        loadMappings();
    }

    public static void loadMappings() {
        try (InputStream inputStream = new FileInputStream("/home/guru-pt7672/eclipse-workspace/Bank_Application/src/main/ColumnMappings.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, Map<String, Object>> yamlData = yaml.load(inputStream);
            Map<String, Object> tableData = (Map<String, Object>) yamlData.get("classes");

            mappings = new HashMap<>();
            for (Map.Entry<String, Object> entry : tableData.entrySet()) {
                String tableName = entry.getKey();
                Map<String, Object> tableMappingData = (Map<String, Object>) entry.getValue();

                ClassMapping tableMapping = new ClassMapping();
                tableMapping.setTableName((String) tableMappingData.get("tableName"));
                tableMapping.setAutoIncrementField((String) tableMappingData.get("autoIncrementField"));
                tableMapping.setReferenceField((String) tableMappingData.get("referenceField"));

                Map<String, FieldMapping> fields = new HashMap<>();
                Map<String, Object> fieldsData = (Map<String, Object>) tableMappingData.get("fields");
                
                if (fieldsData != null) {
                    for (Map.Entry<String, Object> fieldEntry : fieldsData.entrySet()) {
                        String columnName = fieldEntry.getKey();
                        Map<String, Object> fieldMappingData = (Map<String, Object>) fieldEntry.getValue();

                        FieldMapping fieldMapping = new FieldMapping();
                        fieldMapping.setColumnName((String) fieldMappingData.get("columnName"));
                        fieldMapping.setType((String) fieldMappingData.get("type"));

                        fields.put(columnName, fieldMapping);
                    }
                }

                tableMapping.setFields(fields);
                mappings.put(tableName, tableMapping);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load YAML mappings", e);
        }
    }

    public static ClassMapping getMapping(String className) {
    	ClassMapping mapping = mappings.get(className);
        if (mapping != null) {
            return mapping;
        } else {
            throw new IllegalArgumentException("Mapping for table '" + className + "' not found in YAML.");
        }
    }

    public static class ClassMapping {
        private String tableName;
        private String referenceField;
        private String autoIncrementField;
        private Map<String, FieldMapping> fields;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Map<String, FieldMapping> getFields() {
            return fields;
        }

        public void setFields(Map<String, FieldMapping> fields) {
            this.fields = fields;
        }

		public String getAutoIncrementField() {
			return autoIncrementField;
		}

		public void setAutoIncrementField(String autoIncrementField) {
			this.autoIncrementField = autoIncrementField;
		}

		public String getReferenceField() {
			return referenceField;
		}

		public void setReferenceField(String referenceField) {
			this.referenceField = referenceField;
		}
    }

    public static class FieldMapping {
        private String columnName;
        private String type;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
