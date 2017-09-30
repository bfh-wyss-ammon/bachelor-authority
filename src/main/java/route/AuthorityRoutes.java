package route;

import static spark.Spark.*;

import java.io.Console;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.AuthoritySettings;
import data.DbGroup;
import data.DbJoinSession;
import data.DbManagerKey;
import data.DbMembership;
import data.DbPublicKey;
import data.DbUser;
import requests.JoinRequest;
import responses.JoinResponse;
import util.BigIntegerGsonTypeAdapter;
import util.Credential;
import util.Database;
import util.GroupHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.Route;
import util.SessionHelper;
import util.SettingsHelper;

public class AuthorityRoutes {

	private final static Gson gson;
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerGsonTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();
	}

	public static void main(String[] args) {

		//set the port this app is listening on. value from config.ß
		port(SettingsHelper.getSettings().getPort());

		options("/*", (request, response) -> Route.ConfigureOptions(request, response));
		before((request, response) -> Route.ConfigureBefore(request, response));

		get("/settings", (request, response) -> {
			AuthoritySettings settings = SettingsHelper.getSettings();
			if (settings == null) {
				response.status(Route.InternalServerError);
			} else {
				response.status(Route.StatuscodeOk);
				return gson.toJson(settings);
			}
			return "";
		});

		put("/settings", (request, response) -> {
			AuthoritySettings settings = (AuthoritySettings) gson.fromJson(request.body(), AuthoritySettings.class);
			if (settings == null) {
				response.status(Route.Conflict);
			} else {
				SettingsHelper.saveSettings(settings);
			}
			return "";
		});

		post("/settings", (request, response) -> {
			AuthoritySettings settings = new AuthoritySettings();
			SettingsHelper.saveSettings(settings);
			settings = SettingsHelper.getSettings();
			if (settings == null) {
				response.status(Route.InternalServerError);
			} else {
				response.status(Route.StatuscodeOk);
				return gson.toJson(settings);
			}
			return "";
		});

		put("/group", (request, response) -> {

			DbJoinSession session = SessionHelper.getSession(request.headers(Route.TokenHeader));
			if (session == null) {
				response.status(Route.StatuscodeUnauthorized);
				return "";
			}

			DbMembership membership = MembershipHelper.getMembership(session.getUser());
			if (membership == null || membership.getApproved()) {
				response.status(Route.BadRequest);
				return "";
			}

			membership.setApproved(true);
			Database.Update(membership);
			response.status(Route.StatuscodeOk);
			return "";

		});

		get("/group", (request, response) -> {

			List<DbGroup> groupList = Database.Get(DbGroup.class);
			response.status(Route.StatuscodeOk);
			return gson.toJson(groupList);
		});

		post("/group", (request, response) -> {

			DbJoinSession session = SessionHelper.getSession(request.headers(Route.TokenHeader));
			if (session == null) {
				response.status(Route.StatuscodeUnauthorized);
				return "";
			}

			JoinRequest joinRequest = (JoinRequest) gson.fromJson(request.body(), JoinRequest.class);
			if (joinRequest == null || !joinRequest.IsComplete()) {
				response.status(Route.BadRequest);
				return "";
			}

			DbMembership membership = MembershipHelper.getMembership(session.getUser());
			membership.setBigY(joinRequest.bigY());

			JoinResponse joinResponse = JoinHelper.join(membership.getGroup().getPublicKey(),
					membership.getGroup().getManagerKey(), joinRequest);

			Database.SaveOrUpdate(membership);
			session.setCreated(new Date());
			Database.Update(session);
			response.status(Route.StatuscodeOk);
			return gson.toJson(joinResponse);

		});

		post("/login", (request, response) -> {
			try {
				DbUser user = (DbUser) gson.fromJson(request.body(), DbUser.class);
				user = Credential.getUser(user.getMail(), user.getPassword());

				if (user == null) {
					response.status(Route.StatuscodeUnauthorized);
					return "";
				}

				if (MembershipHelper.getMembership(user) != null) {
					response.status(Route.NotImplemented);
					return "";
				}

				DbGroup group = GroupHelper.getGroup();
				DbJoinSession session = SessionHelper.getSession(user);
				DbMembership membership = new DbMembership(user, group);
				Database.Save(DbMembership.class, membership);
				Database.SaveOrUpdate(session);

				response.status(Route.StatuscodeOk);
				response.header(Route.TokenHeader, session.getToken());
				return gson.toJson(group);

			} catch (Exception ex) {
				response.status(Route.StatuscodeUnauthorized);
			}
			return "";
		});

	}

}
