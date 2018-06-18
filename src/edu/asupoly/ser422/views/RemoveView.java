package edu.asupoly.ser422.views;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.asupoly.ser422.model.Person;

@SuppressWarnings("serial")
public class RemoveView extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(response.SC_METHOD_NOT_ALLOWED);
		PrintWriter pw = response.getWriter();
		pw.write("<html><title>Method Not Allowed</title><body><h3>Error: 405 Method Not Allowed</h3></body></html>");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null){
			Person thePerson = (Person) session.getAttribute("thePerson");
			StringBuilder sb = new StringBuilder("<html><title>User</title><body>");
			sb.append("<h2>Remove this user?</h2>");
			sb.append("<p><a href=\"\\lab2_tcotta\\welcome?id=" + thePerson.getID() + "&action2=remove\">Confirm</a><br></p>");
			sb.append("<p><a href=\"\\lab2_tcotta\\usercontroller?id=" + thePerson.getID() + "\">Cancel</a></p>");
			PrintWriter pw = response.getWriter();
			pw.write(sb.toString());
		}
		else {
			// Session Expired
			response.setStatus(response.SC_UNAUTHORIZED);
			PrintWriter pw = response.getWriter();
			pw.write("<html><body><p>Session Time-Out. Please log in again.</p><p><a href=\"\\lab2_tcotta\\\">Login</a></p></body></html>");
		}
	}
}