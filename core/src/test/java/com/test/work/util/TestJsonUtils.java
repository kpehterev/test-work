package test.java.com.test.work.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;
@UtilityClass
public class TestJsonUtils {

    @NonNull
    public static String readResource(@NonNull Resource resource) {
        try {
            return Files.readString(resource.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException("Filed to read resource", e);
        }
    }

    @NonNull
    public static <T> T readJsonData(@NonNull Resource resource, ObjectMapper objectMapper, TypeReference<T> typeReference) {
        try {
            var jsonData = TestJsonUtils.readResource(resource);
            return objectMapper.readValue(jsonData, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse resource", e);
        }
    }

}