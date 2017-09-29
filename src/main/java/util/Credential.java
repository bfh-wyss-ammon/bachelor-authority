package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import data.DbUser;

public class Credential {

	public static Boolean IsValid(String mail, String hashPassword) {
		String securePassword = GetHash(SettingsHelper.getSettings().getSalt() + hashPassword);
		return Database.Exists(DbUser.class,  "email = '"+mail+"' AND password = '"+ securePassword +"'");
	}
	
	public static String securePassword(String hash) {
		String securePassword = GetHash(SettingsHelper.getSettings().getSalt() + hash);
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
