package de.schulung.jakartaee.todos;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloWorldServlet
	extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String name = req.getParameter("name");
		
		if(null == name) {
			name = "World";
		}
		
		// Antwortgenerierung
		resp.setContentType("text/html");
		try(PrintWriter out = resp.getWriter()) {
			out.println("<h1>Hello " + name.toUpperCase() + "</h1>");	
			out.println(LocalDateTime.now());
		}
	}

	
	
}
