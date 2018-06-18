package edu.asupoly.ser422.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.asupoly.ser422.model.Model;

@SuppressWarnings("serial")
public class MVC_Servlet extends HttpServlet {
	
	private static final int EXPIRATION = 1209600;
	private static final int SLEEP_DELAY = 250;
	private static String FILE_PATH = null;
	private static String LOCK_FILE = null;
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("\nInitializing MVC_Servlet");
		super.init(config);
		FILE_PATH = config.getInitParameter("personfile");
		LOCK_FILE = config.getInitParameter("_lockfile");
		if (FILE_PATH == null || FILE_PATH.length() == 0 ||
				LOCK_FILE == null || LOCK_FILE.length() == 0){
			throw new ServletException();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nMVC Servlet Processing GET ...");
		
		HttpSession session = request.getSession(false);
		Model model = new Model(FILE_PATH, LOCK_FILE, SLEEP_DELAY);
		
		// Process Headers
		Cookie fName = null;
		Cookie lName = null;
		try {
			if (session != null){
				boolean fNameSet = false;
				boolean lNameSet = false;
				Cookie cookies[] = request.getCookies();
				for (Cookie cookie : cookies){
					try {
						// Handle First Name Cookie
						if (cookie.getName().equals("firstname")){
	
							if (session.getAttribute("enteredFirstName") != null &&
									cookie.getValue().equals(session.getAttribute("enteredFirstName").toString()) ){
								fName = new Cookie("firstname", session.getAttribute("enteredFirstName").toString());
								fNameSet = true;
							}
							else {
								fName = new Cookie("firstname", session.getAttribute("firstname").toString());
								fNameSet = true;
							}
							fName.setMaxAge(EXPIRATION);
						}
						// Handle Last Name Cookie
						if (cookie.getName().equals("lastname")){
							
							if (session.getAttribute("enteredLastName") != null &&
									cookie.getValue().equals(session.getAttribute("enteredLastName").toString()) ){
								lName = new Cookie("lastname", session.getAttribute("enteredLastName").toString());
								lNameSet = true;
							}
							else {
								lName = new Cookie("lastname", session.getAttribute("lastname").toString());
								lNameSet = true;
							}
							lName.setMaxAge(EXPIRATION);
						}
							
					}catch (NullPointerException np){
						System.out.println("Catching Cookie Errors");
					} 
				}
				if (!fNameSet){
					fName = new Cookie("firstname", session.getAttribute("firstname").toString());
					fName.setMaxAge(EXPIRATION);
				}
				if (!lNameSet){
					lName = new Cookie("lastname", session.getAttribute("lastname").toString());
					lName.setMaxAge(EXPIRATION);
				}
			}
			
			
			// Process Request Parameters
			String id = request.getParameter("id");
			String remove = request.getParameter("action2");
			if(remove != null && remove.equals("remove") && id != null){
				System.out.println("Removing Patient " + id + " from the data store.");	
				model.doRemove(id);
				System.out.println("Removed");
			}
			
			
			// Perform Processing
			String tabulatedResults = model.readData(session, request);
			
		
			// Assign Response Headers
			if (fName != null){response.addCookie(fName);}
			if (lName != null){response.addCookie(lName);}
			
			try {
				response.setStatus(response.SC_OK);
			} catch (Exception e){}
			
			
			// Write out Response'
			PrintWriter pw = response.getWriter();
			pw.write("<html><title>Welcome</title><body><h5>");
			pw.write("Welcome " + session.getAttribute("firstname").toString() + " " + session.getAttribute("lastname").toString());
			pw.write("</h5><a href=\"\\lab2_tcotta\\\"><p align=\"right\">Logout</p></a>"); // Logout
			pw.write("<form action=\"./controller\" method=\"POST\">"						// Create New User
						+ "<input type=\"hidden\" name=\"action\" value=\"create\">"
						+ "<button type=\"submit\">Create User</button>"
					+ "</form>");
			
			pw.write(tabulatedResults);		
			pw.write("</body></html>");
		}
		catch (NullPointerException np){
			// Session Expired
			response.setStatus(response.SC_UNAUTHORIZED);
			PrintWriter pw = response.getWriter();
			pw.write("<html><body><p>Session Time-Out. Please log in again.</p><p><a href=\"\\lab2_tcotta\\\">Login</a></p></body></html>");
		}	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nMVC Servlet Processing POST ...");
		// Process Headers
		
		// Process Request Parameters
		String firstname = request.getParameter("firstname");
		System.out.println("firstname: " + firstname);
		String lastname = request.getParameter("lastname");
		System.out.println("lastname: " + lastname);
		String password = request.getParameter("password");
		System.out.println("password: " + password);
		
		// Perform Processing
		boolean firstNotNull = true;
		boolean lastNotNull = true;
		boolean passwordNotNull = true;
		if (firstname.equals("Enter First Name") || firstname.equals("")){
			System.out.println("Bad first name detected");
			firstNotNull = false;
		}
		if(lastname.equals("Enter Last Name") || lastname.equals("")){
			System.out.println("Bad Last Name Detected");
			lastNotNull = false;
		}
		if(password.equals("Enter Password") || password.equals("")){
			System.out.println("Bad Password Detected");
			passwordNotNull = false;
		}
		
		boolean loggedIn = false;
		if (password.equals("ser422")){
			loggedIn = true;
		}
		
		System.out.println("firstNotNull: " + firstNotNull);
		System.out.println("lastNotNull: " + lastNotNull);
		System.out.println("passwordNotNull: " + passwordNotNull);
		System.out.println("loggedIn: " + loggedIn);
		
		// Assign Response Headers
		if (!loggedIn){
			response.setStatus(response.SC_FORBIDDEN);
			// first and last name cookies here
			Cookie fName = new Cookie("firstname", firstname);
			Cookie lName = new Cookie("lastname", lastname);
			response.addCookie(fName);
			response.addCookie(lName);
		}
		
		// Write out Response		
		if (loggedIn && firstNotNull && lastNotNull && passwordNotNull){
			// Transfer control
			HttpSession session = request.getSession(true);
			session.setAttribute("firstname", firstname);
			session.setAttribute("lastname", lastname);
			this.doGet(request, response);
		}
		else {
			PrintWriter out = response.getWriter();
			out.print("<HTML><TITLE>Forbidden</TITLE><BODY><H3>Error 403 Forbidden</H3><br>");
			if (!firstNotNull){
				out.print("<H5>Invalid First Name</H5>");
			}
			if (!lastNotNull){
				out.print("<H5>Invalid Last Name</H5>");
			}
			if (!passwordNotNull || !loggedIn){
				out.print("<H5>Invalid Password</H5>");
			}
			out.print("<form action=\"\\lab2_tcotta\" method=\"GET\"><input type=\"submit\" value=\"Back\"></form></BODY></HTML>");
		}
	}
	

	
}
