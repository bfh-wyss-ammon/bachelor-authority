package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import data.AuthoritySettings;
import data.DbUser;

public class CredentialHelper {

	public static Boolean IsValid(String id, String hashPassword) {
		String securePassword = GetHash(SettingsHelper.getSettings(AuthoritySettings.class).getSalt() + hashPassword);
		return DatabaseHelper.Exists(DbUser.class, "id = '" + id + "' AND password = '" + securePassword + "'");
	}

	public static DbUser getUser(String id, String hashPassword) {
		String securePassword = GetHash(SettingsHelper.getSettings(AuthoritySettings.class).getSalt() + hashPassword);
		return DatabaseHelper.Get(DbUser.class, "id = '" + id + "' AND password = '" + securePassword + "'");
	}

	public static String securePassword(String hash) {
		String securePassword = GetHash(SettingsHelper.getSettings(AuthoritySettings.class).getSalt() + hash);
		return securePassword;
	}

	public static String GetHash(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception ex) {
			System.out.print("Credential.GetHash: Error" + ex.getMessage());
		}
		return "";
	}
}
