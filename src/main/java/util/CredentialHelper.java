/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This helper class has static methods that apply the salt to the received passwords and gets the users from database.
 */

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
	
	public static DbUser loadUser(String id, String hashPassword) {
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
			Logger.errorLogger(ex);

		}
		return "";
	}
}
