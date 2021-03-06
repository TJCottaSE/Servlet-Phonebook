package edu.asupoly.ser422.views;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class LanguagesView extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(response.SC_METHOD_NOT_ALLOWED);
		PrintWriter pw = response.getWriter();
		pw.write("<html><title>Method Not Allowed</title><body><h3>Error: 405 Method Not Allowed</h3></body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder("<html>");
		HttpSession session = request.getSession();
		if (session != null){
			String languages = null;
			try {
				languages = session.getAttribute("enteredLanguages").toString();
			}catch (Exception e){
				languages = "Java, JavaScript";
				session.setAttribute("enteredLanguages", languages);
			}
			
			String langs[] = languages.split(", ");
			sb.append("<title>Languages</title><body><h3>Languages</h3>"
					+ "<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "<input type=\"submit\" value=\"<< Previous\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"create\">"
					+ "</form>"
					+ "<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "Languages <input type=\"textarea\" name=\"languages\" value=\"");
			for (int i = 0; i < langs.length-1; i++){ // Add each language
				sb.append(langs[i] + ", ");
			}		
			sb.append(langs[langs.length-1] + " \"><br>"
						+ "<input type=\"submit\" value=\"Next >>\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"days\">"
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
