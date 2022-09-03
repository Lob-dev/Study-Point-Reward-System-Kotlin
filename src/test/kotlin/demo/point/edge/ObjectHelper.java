package demo.point.edge;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ObjectUtils;

@Component
public class ObjectHelper {

  private static Map<Class<?>, Object> defaultValues;
  private static ObjectMapper objectMapper;
  {
    defaultValues = new HashMap<>();
    objectMapper = new ObjectMapper();
    defaultValues.put(Long.class, Long.MIN_VALUE);
    defaultValues.put(Integer.class, Integer.MIN_VALUE);
    defaultValues.put(BigDecimal.class, BigDecimal.ZERO);
    defaultValues.put(Boolean.class, Boolean.TRUE);
    defaultValues.put(String.class, "Hello");
    defaultValues.put(LocalTime.class, LocalTime.now());
    defaultValues.put(LocalDate.class, LocalDate.now());
    defaultValues.put(LocalDateTime.class, LocalDateTime.now());
    defaultValues.put(List.class, Collections.emptyList());
  }

  public static <T> T newInstance(Class<T> clazz, Map<Class<?>, Object> initContext) throws Exception {
    T newInstance = clazz.getDeclaredConstructor().newInstance();
    Stream.of(newInstance.getClass().getDeclaredFields())
        .filter(field -> ObjectUtils.isEmpty(field.getAnnotation(Id.class)))
        .forEach(field -> ReflectionTestUtils.setField(
            newInstance,
            field.getName(),
            initContext.getOrDefault(
                field.getClass(),
                getTypeDefaultValue(field.getClass())
            )
        ));
    return newInstance;
  }

  private static <T> Object getTypeDefaultValue(Class<T> clazz) {
    if (defaultValues.containsKey(clazz)) {
      return defaultValues.get(clazz);
    } else if (clazz.isEnum()) {
      return clazz.getEnumConstants()[0];
    }
    return null;
  }
}
