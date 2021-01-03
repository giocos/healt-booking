package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.LoggingUtil;

@SuppressWarnings("serial")
public class Logout extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final HttpSession session = request.getSession();
		session.setAttribute("popUp", false);
		session.setAttribute("username", null);
		session.setAttribute("loggato", false);
			
		if (session.getAttribute("loggatoAdmin") != null && session.getAttribute("loggatoAdmin").equals(true)) {
			session.setAttribute("loggatoAdmin", false);
		} else {
			session.setAttribute("loggatoEmployee", false);
		}
		LoggingUtil.registraDisconnessione(request.getQueryString(), "logout");
		response.sendRedirect("home");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	

}
