package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import data.User;

public class Credential {
	private static final String Salt = "HonoLulu";

	public static Boolean IsValid(String mail, String password) {
		
		String securePassword = GetHash(Salt + password);
		return Database.Exists(User.class,  "email = '"+mail+"' AND password = '"+ securePassword +"'");
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
