package router;

import static spark.Spark.*;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
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
		super(SettingsHelper.getSettings(AuthoritySettings.class).getPort());
	}

	@Override
	public void start() {
		
		put("/membership", (request, response) -> {

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

		post("/membership", (request, response) -> {
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

		get("/group", (request, response) -> {
			List<DbGroup> groupList = DatabaseHelper.Get(DbGroup.class);
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(groupList);
		});
		
		get("/groups/:id", (request, response) -> {
			try {
				int id = Integer.parseInt(request.params(":id"));
				DbGroup group = DatabaseHelper.Get(DbGroup.class, id);
				if (group == null) {
					response.status(Consts.HttpBadRequest);
					return "";
				}
				response.status(Consts.HttpStatuscodeOk);
				return gson.toJson(group);
			} catch (Exception e) {
				// todo error handling
				response.status(Consts.HttpInternalServerError);
			}
			return "";
		});

		post("/login", (request, response) -> {
			try {
				DbUser user = (DbUser) gson.fromJson(request.body(), DbUser.class);
				if (user == null || !CredentialHelper.IsValid(user.getId(), user.getPassword())) {
					response.status(Consts.HttpStatuscodeUnauthorized);
					return "";
				}
				user = CredentialHelper.getUser(user.getId(), user.getPassword());

				DbMembership membership = MembershipHelper.getMembership(user);
				if (membership != null && membership.getApproved()) {
					response.status(Consts.HttpNotImplemented);
					return "";
				}
				if(membership != null && !membership.getApproved()) {
					DatabaseHelper.Delete(membership);
				}

				DbGroup group = GroupHelper.getGroup();
				DbJoinSession session = SessionHelper.getSession(user);
				membership = new DbMembership(user, group);
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
