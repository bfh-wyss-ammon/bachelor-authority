package router;

import static spark.Spark.*;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.AuthoritySettings;
import data.BaseSignature;
import data.DbGroup;
import data.DbSession;
import data.DbManagerKey;
import data.DbMembership;
import data.DbPublicKey;
import data.DbUser;
import data.Tuple;
import demo.DemoSecretKey;
import gson.BigIntegerTypeAdapter;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;
import rest.BaseRouter;
import rest.Router;
import util.Consts;
import util.CredentialHelper;
import util.DatabaseHelper;
import util.Generator;
import util.GroupHelper;
import util.HashHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.SettingsHelper;
import util.SignHelper;
import websocket.GroupCreateSocketHandler;

public class PortalRouter extends BaseRouter implements Router {
	
	public PortalRouter() {
		super(SettingsHelper.getSettings(AuthoritySettings.class).getPortalPort());
		gson = new GsonBuilder().registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter()).create();
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

		post("/login", (request, response) -> {

			DbUser user = (DbUser) gson.fromJson(request.body(), DbUser.class);
			user = CredentialHelper.getUser(user.getId(), CredentialHelper.GetHash(user.getPassword()));

			if (user == null) {
				response.status(Consts.HttpStatuscodeUnauthorized);
				return "";
			}

			if (MembershipHelper.getMembership(user) != null) {
				response.status(Consts.HttpNotImplemented);
				return "";
			}

			DbGroup group = GroupHelper.getGroup();
			DbMembership membership = new DbMembership(user, group);
			DatabaseHelper.Save(DbMembership.class, membership);

			SecretKey memberKey = new DemoSecretKey();
			JoinHelper.init(SettingsHelper.getSettings(AuthoritySettings.class), group.getPublicKey(), memberKey);

			JoinRequest joinRequest = new JoinRequest(memberKey);

			membership.setBigY(joinRequest.bigY());

			JoinResponse joinResponse = JoinHelper.join(membership.getGroup().getPublicKey(),
					membership.getGroup().getManagerKey(), joinRequest);

			DatabaseHelper.SaveOrUpdate(membership);
			memberKey.maintainResponse(joinResponse);
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(memberKey);
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
	}

}
