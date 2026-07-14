package de.schulung.jakartaee.todos.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HelloMessage {
	
	@NotNull
	@NotBlank
	@Size(min = 3)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
