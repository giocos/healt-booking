package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import entity.Impiegato;
import factory.DatabaseManager;
import repository.ImpiegatoDao;

@SuppressWarnings("serial")
public class RipristinoPassword extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuffer jsonReceived = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));		
		String line = reader.readLine();
		
		while(line != null) {
			jsonReceived.append(line);
			line = reader.readLine();
		}		
		
		try {
			JSONObject json = new JSONObject(jsonReceived.toString());
		
			String username = json.getString("username");
			String newPassword = json.getString("password");
			
			ImpiegatoDao impiegatoDao = DatabaseManager.getInstance().getDaoFactory().getImpiegatoDao();
			Impiegato impiegato = impiegatoDao.findByPrimaryKey(username);
				
			if(impiegato != null) {
				
				impiegato.setPassword(newPassword);
				impiegatoDao.update(impiegato);
				response.getWriter().write("Password ripristinata correttamente");
				
			} else {
				response.getWriter().write("Impiegato con user '" + username + "' non presente");
			}
		
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
}
