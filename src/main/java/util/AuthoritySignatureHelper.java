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
			
		} catch (Exception e) {
			e.printStackTrace();
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
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}

        return sigBytes;
		
	}

	



}
