package edu.asupoly.ser422.views;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class CustomView extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(response.SC_METHOD_NOT_ALLOWED);
		PrintWriter pw = response.getWriter();
		pw.write("<html><title>Method Not Allowed</title><body><h3>Error: 405 Method Not Allowed</h3></body></html>");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder("<html>");
		HttpSession session = request.getSession();
		if (session != null){
			String hair = null;
			try {
				hair = session.getAttribute("enteredHair").toString();
			}catch (Exception e){
				hair = "Blonde";
				session.setAttribute("enteredHair", hair);
			}
			
			sb.append("<title>Hair</title><body><h3>Hair</h3>"
					+ "<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "<input type=\"submit\" value=\"<< Previous\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"days\">"
					+ "</form>"
					+ "<form action=\"\\lab2_tcotta\\controller\" method=\"POST\">"
						+ "Hair Color <input type=\"text\" name=\"haircolor\" value=" + hair + "><br>"
						+ "<input type=\"submit\" value=\"Next >>\">"
						+ "<input type=\"hidden\" name=\"action\" value=\"review\">"
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