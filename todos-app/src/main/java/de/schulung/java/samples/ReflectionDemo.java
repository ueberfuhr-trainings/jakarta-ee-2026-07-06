package de.schulung.java.samples;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Start mit JVM-Parameter "--add-opens java.base/java.lang=ALL-UNNAMED"

public class ReflectionDemo {

	// Macht das NICHT!!!!!!
	static {
		Integer i = 5;
		try {
			Field field = Integer.class.getDeclaredField("value");
			field.setAccessible(true);
			field.set(i, 4);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		{
			Book book = new Book();
			book.setTitle("Mein tolles Buch");
			System.out.println(book);
		}
		
		{
			Class<?> c = Class.forName("de.schulung.java.samples.Book");
			Object book = c.getDeclaredConstructor().newInstance();
			Method method = c.getDeclaredMethod("setTitle", String.class);
			method.invoke(book, "Mein tolles Buch");
			System.out.println(book);
		}
		
		{
			Integer i1 = 5;
			Integer i2 = 6;
			Integer i3 = 5;
			System.out.println(i1+i2);
			
			
			
		}
		
		
	}
	
}
