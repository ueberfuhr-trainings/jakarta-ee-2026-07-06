package de.schulung.jakartaee.todos.boundary;

import de.schulung.jakartaee.todos.domain.HelloMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@WebServlet("/hello")
public class HelloWorldServlet
	extends HttpServlet {

	@Inject
	Validator validator;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String name = req.getParameter("name");
		
		HelloMessage message = new HelloMessage();
		message.setName(name);
		
		if(null == message.getName()) {
			message.setName("World");
		}

		// Validierung
		Set<ConstraintViolation<HelloMessage>> violations = validator.validate(message);
		if(!violations.isEmpty()) {
			resp.sendError(400, violations.toString());
			return;
		}
		
		// Antwortgenerierung
		req.setAttribute("message", message);
		req
			.getRequestDispatcher("/WEB-INF/displayHelloworld.jsp")
			.forward(req, resp);
	}

	
	
}
