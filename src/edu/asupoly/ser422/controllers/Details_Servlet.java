package edu.asupoly.ser422.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.asupoly.ser422.model.Model;
import edu.asupoly.ser422.model.Person;
import edu.asupoly.ser422.utils.StaxParser;

@SuppressWarnings("serial")
public class Details_Servlet extends HttpServlet {
	
	private static final int SLEEP_DELAY = 250;
	private static String FILE_PATH = null;
	private static String LOCK_FILE = null;
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("\nInitializing Details_Servlet");
		super.init(config);
		FILE_PATH = config.getInitParameter("personfile");
		LOCK_FILE = config.getInitParameter("_lockfile");
		if (FILE_PATH == null || FILE_PATH.length() == 0 ||
				LOCK_FILE == null || LOCK_FILE.length() == 0){
			throw new ServletException();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nDetails Servlet processing GET: ...");
		HttpSession session = request.getSession();
		Model model = new Model(FILE_PATH, LOCK_FILE, 250);
		boolean found = false;
		Person thePerson = null;
		String action = null;
		try {
			StringBuilder sb = new StringBuilder("<html><title>User</title><body>");
			try {
				// Process Query String Params
				String id = request.getParameter("id").toString();
				
				String regex = "[0123456789]*";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(id);
				System.out.println("About to start Matcher");
				if (matcher.matches()){
					System.out.println("Regex is number!!");
					// Set up edit env
					thePerson = model.doGet(id);
					if (thePerson != null){found = true;}
					action = request.getParameter("action2").toString();
				}
			} catch (Exception e){}
			
			if (found && action == null){
				session.setAttribute("thePerson", thePerson);
				request.getRequestDispatcher("view").forward(request, response);
			}
			else if(found && action != null && action.equals("remove")){
				session.setAttribute("thePerson", thePerson);
				request.getRequestDispatcher("remove").forward(request, response);			
			}
			else{
				sb.append("<p>The User you are looking for does not exist</p>");
				sb.append("<a href=\"\\lab2_tcotta\\welcome\">Back</a>");
			}
			
			response.setStatus(response.SC_OK);
			PrintWriter pw = response.getWriter();
			pw.write(sb.toString());
		}
		catch (NullPointerException np){
			// Session Expired
			response.setStatus(response.SC_UNAUTHORIZED);
			PrintWriter pw = response.getWriter();
			pw.write("<html><body><p>Session Time-Out. Please log in again.</p><p><a href=\"\\lab2_tcotta\\\">Login</a></p></body></html>");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nDetails Servlet processing POST: ...");
		
		response.setStatus(response.SC_METHOD_NOT_ALLOWED);
		PrintWriter pw = response.getWriter();
		pw.write("<html><title>Method Not Allowed</title><body><h3>Error: 405 Method Not Allowed</h3></body></html>");
	}
}