package route;
import static spark.Spark.*;

import com.google.gson.Gson;

import data.DbUser;
import util.Credential;
import util.Route;

public class AuthorityRoutes {
	
	private final static Gson gson;
	static {
		gson = new Gson();
	}

	public static void main(String[] args) {
		
		options("/*", (request, response) -> Route.ConfigureOptions(request, response));
		before((request, response) -> Route.ConfigureBefore(request, response));
				
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
