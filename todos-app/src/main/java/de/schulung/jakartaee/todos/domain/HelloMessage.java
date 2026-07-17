package de.schulung.jakartaee.todos.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelloMessage {

	@NotNull
	@NotBlank
	@Size(min = 3)
	private String name;

}
