/**
 * This is the main executable class of the authority. It specifies how the API-Paths work.
 */

package router;

import static spark.Spark.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import data.AuthoritySettings;
import data.DbGroup;
import data.DbManagerKey;
import data.DbSession;
import data.DbMembership;
import data.DbPublicKey;
import data.Discrepancy;
import data.InvoiceItems;
import data.PaymentTuple;
import data.DbUser;
import data.ResolveRequest;
import data.ResolveResult;
import data.ResolveTuple;
import data.Tuple;
import requests.JoinRequest;
import responses.JoinResponse;
import rest.BaseRouter;
import util.AuthoritySignatureHelper;
import util.Consts;
import util.CredentialHelper;
import util.DatabaseHelper;
import util.GroupHelper;
import util.HashHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.OpenHelper;
import util.SessionHelper;
import util.SettingsHelper;
import util.VerifyHelper;
import websocket.GroupCreateSocketHandler;

public class AuthorityRouter extends BaseRouter {

	public AuthorityRouter() {
		super(SettingsHelper.getSettings(AuthoritySettings.class).getPort(),
				SettingsHelper.getSettings(AuthoritySettings.class).getToken());
	}

	@Override
	public void WebSockets() {
		webSocket("/sockets/groups", GroupCreateSocketHandler.class);
	}

	@Override
	public void Routes() {

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
				if (membership != null && !membership.getApproved()) {
					DatabaseHelper.Delete(membership);
				}

				DbGroup group = GroupHelper.getGroup();
				DbSession session = SessionHelper.getSession(user);
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

		put("/memberships", (request, response) -> {
			DbSession session = SessionHelper.getSession(request.headers(Consts.TokenHeader));
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

		post("/memberships", (request, response) -> {
			DbSession session = SessionHelper.getSession(request.headers(Consts.TokenHeader));
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

		post("/dispute", (request, response) -> {
			try {
				ResolveRequest resolveRequest = (ResolveRequest) gson.fromJson(request.body(), ResolveRequest.class);
				if (resolveRequest == null || resolveRequest.getDisputeSessionId() == null
						|| resolveRequest.getGroupId() == 0 || resolveRequest.getS() == null
						|| resolveRequest.getT() == null || resolveRequest.getProviderSignature() == null) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				List<Discrepancy> discrepancies = new ArrayList<Discrepancy>();
				ResolveResult resolveResult = new ResolveResult();

				// get the group from DB
				DbGroup group = DatabaseHelper.Get(DbGroup.class, resolveRequest.getGroupId());
				if (group == null) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				// check the provider signature on resolve request
				byte[] requestSigBytes = Base64.getDecoder().decode(resolveRequest.getProviderSignature());
				byte[] requestProvidermessage = HashHelper.getHash(resolveRequest);
				if (!AuthoritySignatureHelper.verifyProviderMessage(requestProvidermessage, requestSigBytes)) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				DbManagerKey gsmk = group.getManagerKey();
				DbPublicKey gpk = group.getPublicKey();
				List<DbMembership> memberships = DatabaseHelper.GetList(DbMembership.class,
						"groupId= '" + group.getGroupId() + "'");

				// initialize arrays
				BigInteger[] yList = new BigInteger[memberships.size()];
				int[] tollDue = new int[memberships.size()];
				int[] tollPaid = new int[memberships.size()];
				String[] users = new String[memberships.size()];

				// get the identifying elements
				int i = 0;
				for (DbMembership ship : memberships) {
					yList[i] = ship.getBigY();
					users[i] = ship.getUser().getId();
					i++;
				}

				// check T (how much every user paid)
				for (PaymentTuple tuple : resolveRequest.getT()) {

					InvoiceItems items = new InvoiceItems();
					for (String hash : tuple.getTupleHashlist()) {
						items.getItems().put(hash, 1);
					}
					items.setSessionId(tuple.getSessionId());
					String providerSignature = tuple.getProviderSignature();
					byte[] lSigBytes = Base64.getDecoder().decode(providerSignature);
					byte[] lProvidermessage = HashHelper.getHash(items);

					// check if provider signature on invoice items is valid
					if (!AuthoritySignatureHelper.verifyProviderMessage(lProvidermessage, lSigBytes)) {
						response.status(Consts.HttpBadRequest);
						return "";
					}

					byte[] message = Base64.getDecoder().decode(tuple.getHash());
					int position = OpenHelper.open(gpk, gsmk, message, tuple.getUserSignature(), yList);

					if (position == -1) {
						response.status(Consts.HttpBadRequest);
						return "";
					}
					tollPaid[position] += tuple.getTollPaid();
				}

				// check S (how much every user has to pay)
				for (ResolveTuple tuple : resolveRequest.getS()) {

					byte[] message = Base64.getDecoder().decode(tuple.getHash());
					int position = OpenHelper.open(gpk, gsmk, message, tuple.getSignature(), yList);
					System.out.println("postion S:" + position);

					if (position == -1) {
						response.status(Consts.HttpBadRequest);
						return "";
					}
					tollDue[position] += tuple.getPrice();
				}

				// now calculate how much every user has left to pay
				for (int j = 0; j < yList.length; j++) {
					if (tollDue[j] > tollPaid[j]) {
						Discrepancy dis = new Discrepancy();
						dis.setUserId(users[j]);
						dis.setDeltaToll(tollDue[j] - tollPaid[j]);
						discrepancies.add(dis);
					}
				}

				// set payload
				resolveResult.setDisputeSessionId(resolveRequest.getDisputeSessionId());
				resolveResult.setRes(discrepancies);

				// sign transaction
				byte[] sigBytes = AuthoritySignatureHelper.sign(HashHelper.getHash(resolveResult));
				String authoritySignature = Base64.getEncoder().encodeToString(sigBytes);
				resolveResult.setAuthoritySignature(authoritySignature);

				response.status(Consts.HttpStatuscodeOk);
				return gson.toJson(resolveResult);

			} catch (Exception ex) {
				ex.printStackTrace();
				response.status(Consts.HttpBadRequest);
			}
			return "";
		});
	}

	@Override
	public void ProtectedRoutes() {

		get("/groups", (request, response) -> {
			List<DbGroup> groupList = DatabaseHelper.Get(DbGroup.class);
			response.status(Consts.HttpStatuscodeOk);
			return new Gson().toJson(groupList);
		});

		get("/users", (request, response) -> {
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(DatabaseHelper.Get(DbUser.class));
		});

		get("/membership", (request, response) -> {
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(DatabaseHelper.Get(DbMembership.class));
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
	}

	public static void main(String[] args) {
		new AuthorityRouter();

		// note (pa): just a useless request to warmup hibernate
		DatabaseHelper.Exists(DbGroup.class, "groupId = 1");
	}
}
