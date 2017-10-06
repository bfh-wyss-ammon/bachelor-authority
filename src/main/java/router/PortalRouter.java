package router;

import static spark.Spark.*;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import data.AuthoritySettings;
import data.DbGroup;
import data.DbJoinSession;
import data.DbManagerKey;
import data.DbMembership;
import data.DbPublicKey;
import data.DbUser;
import requests.JoinRequest;
import responses.JoinResponse;
import rest.BaseRouter;
import rest.Router;
import util.Consts;
import util.CredentialHelper;
import util.DatabaseHelper;
import util.Generator;
import util.GroupHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.SessionHelper;
import util.SettingsHelper;

public class PortalRouter extends BaseRouter implements Router {

	public PortalRouter() {
		super(10009);
		gson = new Gson();
	}

	@Override
	public void start() {
		get("/users", (request, response) -> {
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(DatabaseHelper.Get(DbUser.class));
		});
		
		get("/membership", (request, response) -> {
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(DatabaseHelper.Get(DbMembership.class));
		});

		post("/users/:id", (request, response) -> {
			try {
				String password = request.body();
				int id = Integer.parseInt(request.params(":id"));
				DbUser user = DatabaseHelper.Get(DbUser.class, id);
				user.setPassword(CredentialHelper.securePassword(CredentialHelper.GetHash(password)));

				DatabaseHelper.Update(user);

				response.status(Consts.HttpStatuscodeOk);
			} catch (Exception e) {
				// todo error handling
				response.status(Consts.HttpInternalServerError);
			}
			return "";
		});

		post("/users", (request, response) -> {
			try {
				DbUser user = new Gson().fromJson(request.body(), DbUser.class);
				user.setPassword(CredentialHelper.securePassword(CredentialHelper.GetHash(user.getPassword())));
				DatabaseHelper.Save(DbUser.class, user);

				response.status(Consts.HttpStatuscodeOk);
			} catch (Exception e) {
				// todo error handling
				response.status(Consts.HttpInternalServerError);
			}
			return "";
		});
		
		get("/groups", (request, response) -> {
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(DatabaseHelper.Get(DbGroup.class));
		});
		
		post("/groups", (request, response) -> {
			
			DbPublicKey publicKey = new DbPublicKey();
			DbManagerKey managerKey = new DbManagerKey();
			DbGroup group = new DbGroup();
			Generator.generate(SettingsHelper.getSettings(), publicKey, managerKey);
			
			group.setManagerKey(managerKey);
			group.setPublicKey(publicKey);
			DatabaseHelper.Save(DbGroup.class, group);

			response.status(Consts.HttpStatuscodeOk);
			return "";
		});
	}

}
