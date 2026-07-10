package de.schulung.java.samples;

public class ValidationDemo {

	public static void main(String[] args) {
		
		Book book = new Book();
		
		MyValidator validator = new MyValidator();
		validator.validate(book);
		
	}
	
}
