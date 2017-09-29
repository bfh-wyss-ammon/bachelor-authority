package route;
import static spark.Spark.*;

import com.google.gson.Gson;

import data.AuthoritySettings;
import data.DbUser;
import util.Credential;
import util.Route;
import util.SettingsHelper;

public class AuthorityRoutes {
	
	private final static Gson gson;
	static {
		gson = new Gson();
	}

	public static void main(String[] args) {
		
		options("/*", (request, response) -> Route.ConfigureOptions(request, response));
		before((request, response) -> Route.ConfigureBefore(request, response));
		
		get("/settings", (request, response) -> {
			AuthoritySettings settings = SettingsHelper.getSettings();
			if(settings == null) {
				response.status(Route.InternalServerError);
			}
			else {
				response.status(Route.StatuscodeOk);
				return gson.toJson(settings);
			}
			return "";
		});
		
		put("/settings", (request, response) -> {
			AuthoritySettings settings = (AuthoritySettings)gson.fromJson(request.body(), AuthoritySettings.class);
			if(settings == null) {
				response.status(Route.Conflict);
			}
			else {
				SettingsHelper.saveSettings(settings);
			}
			return "";
		});
		
		post("/settings", (request, response) -> {
			AuthoritySettings settings = new AuthoritySettings();
			SettingsHelper.saveSettings(settings);
			settings = SettingsHelper.getSettings();
			if(settings == null) {
				response.status(Route.InternalServerError);
			}
			else {
				response.status(Route.StatuscodeOk);
				return gson.toJson(settings);
			}
			return "";
		});
				
		post("/login", (request, response) -> {
			try {
				DbUser user = (DbUser)gson.fromJson(request.body(), DbUser.class);
				if(!Credential.IsValid(user.getMail(), user.getPassword())) {
					response.status(Route.StatuscodeUnauthorized);
					return "";
				}
				
				
				response.status(Route.StatuscodeOk);
				// todo send publickey + sessionid
			}
			catch (Exception ex) {
				response.status(Route.StatuscodeUnauthorized);
			}
			return "";
		});

	}

}
