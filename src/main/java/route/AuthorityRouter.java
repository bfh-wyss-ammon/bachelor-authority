package route;

import static spark.Spark.*;
import java.util.Date;
import data.AuthoritySettings;
import data.DbGroup;
import data.DbJoinSession;
import data.DbMembership;
import data.DbUser;
import requests.JoinRequest;
import responses.JoinResponse;
import rest.BaseRouter;
import rest.Router;
import util.Consts;
import util.CredentialHelper;
import util.DatabaseHelper;
import util.GroupHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.SessionHelper;
import util.SettingsHelper;

public class AuthorityRouter extends BaseRouter implements Router {

	public AuthorityRouter() {
		super(1000);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		get("/settings", (request, response) -> {
			AuthoritySettings settings = SettingsHelper.getSettings();
			if (settings == null) {
				response.status(Consts.HttpInternalServerError);
			} else {
				response.status(Consts.HttpStatuscodeOk);
				return gson.toJson(settings);
			}
			return "";
		});

		put("/settings", (request, response) -> {
			AuthoritySettings settings = (AuthoritySettings) gson.fromJson(request.body(), AuthoritySettings.class);
			if (settings == null) {
				response.status(Consts.HttpConflict);
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
				response.status(Consts.HttpInternalServerError);
			} else {
				response.status(Consts.HttpStatuscodeOk);
				return gson.toJson(settings);
			}
			return "";
		});

		put("/group", (request, response) -> {

			DbJoinSession session = SessionHelper.getSession(request.headers(Consts.TokenHeader));
			if (session == null) {
				response.status(Consts.HttpStatuscodeUnauthorized);
				return "";
			}

			DbMembership membership = MembershipHelper.getMembership(session.getUser());
			if (membership == null || membership.getApproved()) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			membership.setApproved(true);
			DatabaseHelper.Update(membership);
			response.status(Consts.HttpStatuscodeOk);
			return "";

		});

		post("/group", (request, response) -> {

			DbJoinSession session = SessionHelper.getSession(request.headers(Consts.TokenHeader));
			if (session == null) {
				response.status(Consts.HttpStatuscodeUnauthorized);
				return "";
			}

			JoinRequest joinRequest = (JoinRequest) gson.fromJson(request.body(), JoinRequest.class);
			if (joinRequest == null || !joinRequest.IsComplete()) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			DbMembership membership = MembershipHelper.getMembership(session.getUser());
			membership.setBigY(joinRequest.bigY());

			JoinResponse joinResponse = JoinHelper.join(membership.getGroup().getPublicKey(),
					membership.getGroup().getManagerKey(), joinRequest);

			DatabaseHelper.SaveOrUpdate(membership);
			session.setCreated(new Date());
			DatabaseHelper.Update(session);
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(joinResponse);

		});

		post("/login", (request, response) -> {
			try {
				DbUser user = (DbUser) gson.fromJson(request.body(), DbUser.class);
				user = CredentialHelper.getUser(user.getMail(), user.getPassword());

				if (user == null) {
					response.status(Consts.HttpStatuscodeUnauthorized);
					return "";
				}

				if (MembershipHelper.getMembership(user) != null) {
					response.status(Consts.HttpNotImplemented);
					return "";
				}

				DbGroup group = GroupHelper.getGroup();
				DbJoinSession session = SessionHelper.getSession(user);
				DbMembership membership = new DbMembership(user, group);
				DatabaseHelper.Save(DbMembership.class, membership);
				DatabaseHelper.SaveOrUpdate(session);

				response.status(Consts.HttpStatuscodeOk);
				response.header(Consts.TokenHeader, session.getToken());
				return gson.toJson(group);

			} catch (Exception ex) {
				response.status(Consts.HttpStatuscodeUnauthorized);
			}
			return "";
		});
	}

}