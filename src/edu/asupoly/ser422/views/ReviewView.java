package edu.asupoly.ser422.views;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class ReviewView extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(response.SC_METHOD_NOT_ALLOWED);
		PrintWriter pw = response.getWriter();
		pw.write("<html><title>Method Not Allowed</title><body><h3>Error: 405 Method Not Allowed</h3></body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder("<html>");
		HttpSession session = request.getSession();
		if (session != null){
			// Display Review Content
			sb.append("<title>Review</title><body><h3>Review</h3>"
					+ "First Name: " + session.getAttribute("enteredFirstName").toString() + "<br>"
					+ "Last Name: " + session.getAttribute("enteredLastName").toString() + "<br>"
					+ "Languages: " + session.getAttribute("enteredLanguages").toString() + "<br>"
					+ "Days: " + session.getAttribute("enteredDays").toString() + "<br>"
					+ "Hair: " + session.getAttribute("enteredHair").toString() + "<br>");
			// Add the Cancel Button  STILL NEEDS LINKING WORK
			sb.append("<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"cancel\">"
						+ "<input type=\"submit\" value=\"Cancel\">"
					+ "</form>");
			// Add the Edit Button
			sb.append("<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"create\">"
						+ "<input type=\"submit\" value=\"Edit\">"
					+ "</form>");
			// Add the Confirm Button
			sb.append("<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"confirm\">"
						+ "<input type=\"submit\" value=\"Confirm\">"
					+ "</form>");
			
			sb.append("</body></html>");
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