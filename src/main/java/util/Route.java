package util;

import spark.Request;
import spark.Response;

public class Route {
	
	// for more: https://de.wikipedia.org/wiki/HTTP-Statuscode
	public static final int StatuscodeUnauthorized = 401;
	public static final int StatuscodeOk = 200;
	public static final int InternalServerError = 500;
	public static final int Conflict = 409;
	public static final int NotImplemented = 501;
	public static final int BadRequest = 400;
	
	public static final String TokenHeader = "x-custom-token";
	
	private static final String Origin = "http://localhost:8080";
	private static final String Methods = "POST, GET, PUT";
	private static final String Headers = "Content-Type, x-custom-token";
	
	public static String ConfigureOptions(Request request, Response response) {
		String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
		if (accessControlRequestHeaders != null) {
			response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
		}

		String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
		if (accessControlRequestMethod != null) {
			response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
		}
		return "OK";
	}
	
	public static void ConfigureBefore(Request request, Response response) {
		response.header("Access-Control-Allow-Origin", Origin);
		response.header("Access-Control-Request-Method", Methods);
		response.header("Access-Control-Allow-Headers", Headers);
		
		response.type("application/json");
	}
}
