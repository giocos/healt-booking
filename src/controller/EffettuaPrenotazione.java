package controller;

import facade.PrenotazioneFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class EffettuaPrenotazione extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		final PrintWriter out = response.getWriter();
		try {
			final String risposta = PrenotazioneFacade.getInstance().checkPrenotazione();
			out.println(risposta);

		} finally {
			out.flush();
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		final PrintWriter out = response.getWriter();
		try {
			final String risposta = PrenotazioneFacade.getInstance()
					.effettuaPrenotazione(request.getInputStream());
			out.println(risposta);

		} finally {
			out.flush();
			out.close();
		}
	}
}
