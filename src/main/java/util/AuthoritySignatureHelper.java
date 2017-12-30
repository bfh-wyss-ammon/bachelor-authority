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
 * This helper class has static methods that generate the signatures from the authority (used on the dispute resolving protocol result).
 */

package util;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

import data.AuthoritySettings;


public class AuthoritySignatureHelper {
	
	public static boolean verifyProviderMessage (byte[] message, byte[] signature) {
		try {
			byte[] publicKeyEncoded = Base64.decodeBase64(SettingsHelper.getSettings(AuthoritySettings.class).getProviderPublicKey());
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			Signature sig = Signature.getInstance("SHA256withDSA", "SUN");
			sig.initVerify(pubKey);
			sig.update(message);
			return sig.verify(signature);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.errorLogger(ex);
			return false;
		}
	}
	
	public static byte[] sign (byte[] message) {
		byte[] sigBytes =null;
try {
			byte[] privateKeyEncoded = Base64.decodeBase64(SettingsHelper.getSettings(AuthoritySettings.class).getPrivateKey());
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyEncoded);
	        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
	        PrivateKey priv = keyFactory.generatePrivate(spec);
	        Signature signature = Signature.getInstance("SHA256withDSA", "SUN");
	        signature.initSign(priv);
	        signature.update(message);
	        sigBytes = signature.sign();
			
		}catch (Exception ex) {
			ex.printStackTrace();
			Logger.errorLogger(ex);

			
		}

        return sigBytes;
		
	}

	



}
