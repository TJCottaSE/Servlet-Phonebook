package edu.asupoly.ser422.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * This servlet serves content to "localhost/lab2_tcotta/"
 * 
 */
@SuppressWarnings("serial")
public class Login_Servlet extends HttpServlet {
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("\nInitializing Login_Servlet");
		super.init(config);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nLogin Servlet Processing GET ...");
		// Kill an existing sessions
		HttpSession session = request.getSession(false);
		if (session != null) { session.invalidate(); }
		// Process Headers
		String firstname = null;
		String lastname = null;
		Cookie cookies[] = request.getCookies();
		try {
			for (int i = 0; i < cookies.length; i++){
				if (cookies[i].getName().equals("firstname")){
					firstname = cookies[i].getValue();
				}
				if (cookies[i].getName().equals("lastname")){
					lastname = cookies[i].getValue();
				}
			}
		}catch (Exception np){}
		if(firstname == null){
			firstname = "Enter First Name!!";
		}
		if(lastname == null){
			lastname = "Enter Last Name!!";
		}
		
		// Set Headers
		response.setStatus(200);
		// Write Out Response
		PrintWriter pw = response.getWriter();
		pw.write("<HTML>"
				+ "<HEAD><TITLE>A Simple MVC Project</TITLE></HEAD>"
					+ "<BODY>"
						+ "<H2 ALIGN=\"CENTER\">Login</H2>"

						+ "<FORM ACTION=\"./welcome\" METHOD=\"POST\">"
							+ "<CENTER>"
								+ "First Name"
								+ "<INPUT TYPE=\"text\" NAME=\"firstname\" VALUE=" + firstname + "><br>"
								+ "Last Name"
								+ "<INPUT TYPE=\"text\" NAME=\"lastname\" VALUE=" + lastname + "><br>"
								+ "Password"
								+ "<INPUT TYPE=\"password\" NAME=\"password\" VALUE=\"Enter Password\"><br>"
								+ "<INPUT TYPE=\"submit\" VALUE=\"Login\"><br>"
							+ "</CENTER>"
						+ "</FORM>"
					+ "</BODY>"
				+ "</HTML>");
	}
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nLogin Servlet Processing POST ...");
		
		response.setStatus(response.SC_METHOD_NOT_ALLOWED);
		PrintWriter pw = response.getWriter();
		pw.write("<html><title>Method Not Allowed</title><body><h3>Error: 405 Method Not Allowed</h3></body></html>");
	}
}
