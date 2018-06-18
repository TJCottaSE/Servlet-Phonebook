package edu.asupoly.ser422.controllers;

import java.io.IOException;
import java.io.PrintWriter;
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

@SuppressWarnings("serial")
public class Controller_Servlet extends HttpServlet {
	
	private static final int SLEEP_DELAY = 250;
	private static String FILE_PATH = null;
	private static String LOCK_FILE = null;
	
	public void init(ServletConfig config) throws ServletException {
		System.out.println("\nInitializing Controller_Servlet");
		super.init(config);
		FILE_PATH = config.getInitParameter("personfile");
		LOCK_FILE = config.getInitParameter("_lockfile");
		if (FILE_PATH == null || FILE_PATH.length() == 0 ||
				LOCK_FILE == null || LOCK_FILE.length() == 0){
			throw new ServletException();
		}
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nController Servlet Processing GET ...");
		HttpSession session = request.getSession();
		Model model = new Model(FILE_PATH, LOCK_FILE, SLEEP_DELAY);
		boolean found = false;
		if (session != null){
			try {
				// Process Query String Parameters
				String id = request.getParameter("id").toString();
				String regex = "[0123456789]*";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(id);
				System.out.println("About to start Matcher");
				if (matcher.matches()){
					System.out.println("Regex is number!!");
					found = model.setUpEdit(session, id);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			
			// Call doPost
			if (found) { 
				this.doPost(request, response);
			}
			else {
				response.setStatus(response.SC_FORBIDDEN);
				PrintWriter pw = response.getWriter();
				pw.write("<html><Title>Forbidden</title><body><h2>Forbidden</h2><p>Invalid Query String Parameters</p></body></html>");
			}
		}
		else{
			// Session Expired
			response.setStatus(response.SC_UNAUTHORIZED);
			PrintWriter pw = response.getWriter();
			pw.write("<html><body><p>Session Time-Out. Please log in again.</p><p><a href=\"\\lab2_tcotta\\\">Login</a></p></body></html>");
		}	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\nController Servlet Processing POST ...");
		
		HttpSession session = request.getSession();
		Model model = new Model(FILE_PATH, LOCK_FILE, SLEEP_DELAY);
		// Process Headers
		
		
		// Process Request Parameters
		if (session != null){
			String action = null;
			if (session.getAttribute("action") != null){
				action = session.getAttribute("action").toString();
				session.removeAttribute("action");
			}else {action = request.getParameter("action");}
			
			System.out.println("PARAMETER VALUES");
			System.out.println("Action Received: " + action);
			String reqFirstName = request.getParameter("firstname");
			System.out.println("Request firstname = " + reqFirstName);
			String reqLastName = request.getParameter("lastname");
			System.out.println("Request lastname = " + reqLastName);
			String reqLanguages = request.getParameter("languages");
			System.out.println("Request languages = " + reqLanguages);
			String reqDays = request.getParameter("days");
			System.out.println("Request days = " + reqDays);
			String reqHair = request.getParameter("haircolor");
			System.out.println("Request hair = " + reqHair);
			
			String firstname = null;
			String lastname = null;
			String languages = null;
			String days = null;
			String hair = null;
			
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
			try {
				languages = session.getAttribute("enteredLanguages").toString();
			}catch (Exception e){
				languages = "Java, JavaScript";
				session.setAttribute("enteredLanguages", languages);
			}
			try {
				days = session.getAttribute("enteredDays").toString();
			}catch (Exception e){
				days = "Tuesday, Wednesday";
				session.setAttribute("enteredDays", days);
			}
			try {
				hair = session.getAttribute("enteredHair").toString();
			}catch (Exception e){
				hair = "Blonde";
				session.setAttribute("enteredHair", hair);
			}
			
			
			
			if (reqFirstName != null) {System.out.println("reqFirstname = firstname: " + reqFirstName.equalsIgnoreCase(firstname));}
			
			if ( (reqFirstName != null) && !reqFirstName.equalsIgnoreCase(firstname)){
				System.out.println("Setting session value enteredFirstName to: " + reqFirstName);
				firstname = reqFirstName;
				session.setAttribute("enteredFirstName", reqFirstName);
			}
	
			if ( (reqLastName != null) && !reqLastName.equalsIgnoreCase(lastname)){
				System.out.println("Setting session value enteredLastName to: " + reqLastName);
				lastname = reqLastName;
				session.setAttribute("enteredLastName", reqLastName);
			}
			
			if ( (reqLanguages != null) && !reqLanguages.equalsIgnoreCase(languages)){
				languages = reqLanguages;
				session.setAttribute("enteredLanguages", reqLanguages);
			}
	
			if ( (reqDays != null) && !reqDays.equalsIgnoreCase(days)){
				days = reqDays;
				session.setAttribute("enteredDays", reqDays);
			}
	
			if ( (reqHair != null) && !reqHair.equalsIgnoreCase(hair)){
				hair = reqHair;
				session.setAttribute("enteredHair", reqHair);
			}
			
	//		System.out.println("SESSION VALUES UPDATED!!");
	//		System.out.println("Session firstname = " + firstname);
	//		System.out.println("Session lastname = " + lastname);
	//		System.out.println("Session languages = " + languages);
	//		System.out.println("Session days = " + days);
	//		System.out.println("Session hair = " + hair);
	//		
	//			
	//		
	//		System.out.println("\n\nAction: ");
	//		System.out.println("Action = create: " + action.equals("create"));
	//		System.out.println("Action = languages: " + action.equals("languages"));
	//		System.out.println("Action = days: " + action.equals("days"));
	//		System.out.println("Action = custom: " + action.equals("custom"));
	//		System.out.println("Action = review: " + action.equals("review"));
	//		System.out.println("Action = cancel: " + action.equals("cancel"));
	//		System.out.println("Action = confirm: " + action.equals("confirm"));
			
			StringBuilder sb = new StringBuilder("<html>");
			// Select which view to show
			if (action.equals("create")){
				// Load Create Page
				//this.create(sb, firstname, lastname, session);
				request.getRequestDispatcher("create").forward(request, response);
			}
			else if (action.equals("languages")){
				// Load Languages
				//this.languages(sb, languages);
				request.getRequestDispatcher("languages").forward(request, response);
			}
			else if (action.equals("days")){
				// Load Days
				//this.days(sb, days);
				request.getRequestDispatcher("days").forward(request, response);
			}
			else if (action.equals("custom")){
				// Load Custom
				//this.custom(sb, hair);
				request.getRequestDispatcher("custom").forward(request, response);
			}
			else if (action.equals("review")){
				// Load Review
				//this.review(sb, session);
				request.getRequestDispatcher("review").forward(request, response);
			}
			else if (action.equals("cancel")){
				// Invalidate session objects for creation
				session.removeAttribute("enteredFirstName");
				session.removeAttribute("enteredLastName");
				session.removeAttribute("enteredLanguages");
				session.removeAttribute("enteredDays");
				session.removeAttribute("enteredHair");
				// Route back to page one
				response.sendRedirect(request.getContextPath() + "/welcome");
			}
			else if (action.equals("confirm")){
				// Validate Input
				boolean valid = this.validateInputs(session);
				System.out.println("\nValid Inputs have been provided: " + valid);
				if (valid){ //  -> Valid 
					//    -> Invalidate Malformed Attributes if they exist
					session.removeAttribute("MalformedFirstName");
					session.removeAttribute("MalformedLastName");
					session.removeAttribute("MalformedLanguages");
					session.removeAttribute("MalformedDays");
					session.removeAttribute("MalformedHair");
					//    -> Update user first and last name prefs
					Cookie fName = new Cookie("firstname", session.getAttribute("enteredFirstName").toString());
					Cookie lName = new Cookie("lastname", session.getAttribute("enteredLastName").toString());
					response.addCookie(fName);
					response.addCookie(lName);
					//    -> Write out file to persistent store
					model.write(session);
					//    -> Navigate to /welcome
					response.sendRedirect(request.getContextPath() + "/welcome");
				}
				else { //  -> Invalid
					//	  -> Navigate to create to perform editing
					request.getRequestDispatcher("create").forward(request, response);
					//this.doPost(request, response);
				}
			}
			else {
				// Something went wrong
				System.out.println("Something went wrong");
				sb.append("<body><p>Something went wrong</p>");
			}
			
			// Set Response Headers
			response.setStatus(response.SC_OK);
			
			// Write out the response
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
	
	private boolean validateInputs(HttpSession session){
		// Validate First Name
		boolean partsInvalid = false;
		try {
			String firstname = session.getAttribute("enteredFirstName").toString();
			String regex = "^[a-zA-Z]+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(firstname);
			if (!matcher.matches()){
				session.setAttribute("MalformedFirstName", true);
				partsInvalid = true;
			}
		} catch (Exception e){
			session.setAttribute("errFirstNameNull", true);
			partsInvalid = true;
		}
		// Validate Last Name
		try {
			String lastname = session.getAttribute("enteredLastName").toString();
			String regex = "^[a-zA-Z]+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(lastname);
			if (!matcher.matches()){
				session.setAttribute("MalformedLastName", true);
				partsInvalid = true;
			}
		} catch (Exception e){
			session.setAttribute("errLastNameNull", true);
			partsInvalid = true;
		}
		// Validate Languages
		try {
			String regex = "[a-zA-Z\\+\\-\\*#@\\.\\!\\/ ]*";
			Pattern pattern = Pattern.compile(regex);
			String languages = session.getAttribute("enteredLanguages").toString();
			String language[] = languages.split(",");
			for (String thisLanguage : language){
				thisLanguage = thisLanguage.trim();
				Matcher matcher = pattern.matcher(thisLanguage);
				//System.out.println("Language Pattern matches: " + matcher.matches());
				if (!matcher.matches()){
					session.setAttribute("MalformedLanguages", true);
					partsInvalid = true;
				}
			}
		}catch (Exception e){
			session.setAttribute("errMalformedLanguages", true);
			partsInvalid = true;
		}
		// Validate Days
		try {
			String days = session.getAttribute("enteredDays").toString();
			String day[] = days.split(",");
			for (String thisDay : day){
				thisDay = thisDay.trim();
				if (!thisDay.equalsIgnoreCase("Monday") && 
						!thisDay.equalsIgnoreCase("Tuesday") &&
						!thisDay.equalsIgnoreCase("Wednesday") &&
						!thisDay.equalsIgnoreCase("Thursday") &&
						!thisDay.equalsIgnoreCase("Friday") &&
						!thisDay.equalsIgnoreCase("Saturday") &&
						!thisDay.equalsIgnoreCase("Sunday")
					){
					session.setAttribute("MalformedDays", true);
					partsInvalid = true;
				}
			}
		} catch (Exception e){
			System.out.println("Days was null");
			session.setAttribute("errDaysNull", true);
			partsInvalid = true;
		}
		// Validate Hair
		try {
			String hair = session.getAttribute("enteredHair").toString();
			String regex = "^[a-zA-Z]+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(hair);
			if (!matcher.matches()){
				session.setAttribute("MalformedHair", true);
				partsInvalid = true;
			}
		} catch (Exception e){
			session.setAttribute("errHairNull", true);
			partsInvalid = true;
		}
		
		return !partsInvalid;
	}	
	

}