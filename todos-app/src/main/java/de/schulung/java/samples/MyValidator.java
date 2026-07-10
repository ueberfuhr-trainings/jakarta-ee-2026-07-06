package de.schulung.java.samples;

import java.lang.reflect.Field;

public class MyValidator {

	public void validate(Object object) {
		try {
			Class<?> c = object.getClass();
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
				if(null != field.getAnnotation(MyNotNull.class)) {
					field.setAccessible(true);
					Object fieldValue = field.get(object);
					if(null == fieldValue) {
						throw new RuntimeException("Validierung!");
					}
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}

}
