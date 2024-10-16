package util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TableYamlUtil {
    // This Map stores mappings for each table under the 'tables' key
    private static Map<String, TableMapping> mappings;

    static {
        loadMappings();
    }

    public static void loadMappings() {
        try (InputStream inputStream = new FileInputStream("/home/guru-pt7672/eclipse-workspace/Bank_Application/src/main/Tablemappings.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, Map<String, Object>> yamlData = yaml.load(inputStream);
            Map<String, Object> tableData = (Map<String, Object>) yamlData.get("tables");

            mappings = new HashMap<>();
            for (Map.Entry<String, Object> entry : tableData.entrySet()) {
                String tableName = entry.getKey();
                Map<String, Object> tableMappingData = (Map<String, Object>) entry.getValue();

                TableMapping tableMapping = new TableMapping();
                tableMapping.setClassName((String) tableMappingData.get("className"));

                Map<String, ColumnMapping> fields = new HashMap<>();
                Map<String, Object> fieldsData = (Map<String, Object>) tableMappingData.get("columns");
                
                if (fieldsData != null) {
                    for (Map.Entry<String, Object> fieldEntry : fieldsData.entrySet()) {
                        String columnName = fieldEntry.getKey();
                        Map<String, Object> fieldMappingData = (Map<String, Object>) fieldEntry.getValue();

                        ColumnMapping fieldMapping = new ColumnMapping();
                        fieldMapping.setColumnName((String) fieldMappingData.get("fieldName"));
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

    public static TableMapping getMapping(String tableName) {
        TableMapping mapping = mappings.get(tableName);
        if (mapping != null) {
            return mapping;
        } else {
            throw new IllegalArgumentException("Mapping for table '" + tableName + "' not found in YAML.");
        }
    }

    public static class TableMapping {
        private String className;
        private String primaryKey;
        private String referenceKey;
        private Map<String, String> referencedKeys;
        private Map<String, ColumnMapping> fields;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Map<String, ColumnMapping> getFields() {
            return fields;
        }

        public void setFields(Map<String, ColumnMapping> fields) {
            this.fields = fields;
        }

		public String getPrimaryKey() {
			return primaryKey;
		}

		public void setPrimaryKey(String primaryKey) {
			this.primaryKey = primaryKey;
		}

		public Map<String, String> getReferencedKeys() {
			return referencedKeys;
		}

		public void setReferencedKeys(Map<String, String> referencedKeys) {
			this.referencedKeys = referencedKeys;
		}

		public String getReferenceKey() {
			return referenceKey;
		}

		public void setReferenceKey(String referenceKey) {
			this.referenceKey = referenceKey;
		}
    }

    public static class ColumnMapping {
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
