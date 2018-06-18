package edu.asupoly.ser422.views;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class CreateView extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder("<html>");
		HttpSession session = request.getSession();
		if (session != null){
			String firstname = null;
			String lastname = null;
			try {
				firstname = session.getAttribute("enteredFirstName").toString();
			}catch (Exception e){
				firstname = "John";
				session.setAttribute("enteredFirstName", firstname);
			}
			try {
				lastname = session.getAttribute("enteredLastName").toString();
			}catch (Exception e){
				lastname = "Smith";
				session.setAttribute("enteredLastName", lastname);
			}
			
			sb.append("<title>Create | Edit User</title><body><h3>Create | Edit User</h3>");
			// Mark errors if they exist
			
			// Check First Name errors
			try {
				String errFirstName = session.getAttribute("MalformedFirstName").toString();
				sb.append("<p>Check the first name: Must only contain characters A-Z Case insensitive, and may not contain spaces</p>");
			}catch (Exception e){
				// Continue
			}
			// Check Last Name Errors
			try {
				String errLastName = session.getAttribute("MalformedLastName").toString();
				sb.append("<p>Check the last name: Must only contain characters A-Z Case insensitive, and may not contain spaces</p>");
			}catch (Exception e){
				// Continue
			}
			// Check languages
			try {
				String errLanguages = session.getAttribute("MalformedLanguages").toString();
				sb.append("<p>Check the languages: Must only contain characters A-Z Case insensitive, or one of the following "
						+ "characters: +,-,*,#,@,.,!,/ not including commas.</p>");
			}catch (Exception e){
				// Continue
			}
			// Check days
			try {
				String errDays = session.getAttribute("MalformedDays").toString();
				sb.append("<p>You must enter days as a comma semerated list using full day names. ie Tuesday, Wednesday</p>");
			}catch (Exception e){
				// Continue
			}
			// Check hair
			try {
				String errHair = session.getAttribute("MalformedHair").toString();
				sb.append("<p>Check the hair color: Must only contain characters A-Z Case insensitive, and may not contain spaces</p>");
			}catch (Exception e){
				// Continue
			}
			
			sb.append(
					  "<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "First Name <input type=\"text\" name=\"firstname\" value=" + firstname + "><br>"
						+ "Last Name <input type=\"text\" name=\"lastname\" value=" + lastname + "><br>"
						+ "<input type=\"submit\" value=\"Next >>\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"languages\">"
					+ "</form>");
			
			sb.append("</body></html>");
			PrintWriter ps = response.getWriter();
			ps.write(sb.toString());
		}
		else {
			// Session Expired
			response.setStatus(response.SC_UNAUTHORIZED);
			PrintWriter pw = response.getWriter();
			pw.write("<html><body><p>Session Time-Out. Please log in again.</p><p><a href=\"\\lab2_tcotta\\\">Login</a></p></body></html>");
		}
	}
}
