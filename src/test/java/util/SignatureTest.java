package util;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import data.AuthoritySettings;

public class SignatureTest {

	@Test
	public void testGenerateAndSign() {

		KeyPairGenerator keyGen = null;
		SecureRandom rand = null;

		try {
			keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			rand = SecureRandom.getInstance("SHA1PRNG", "SUN");

			keyGen.initialize(2048, rand);
			KeyPair keyPair = keyGen.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();

			System.out.println("Private Key: " + Base64.encodeBase64String(privateKey.getEncoded()));
			System.out.println("Public Key: " + Base64.encodeBase64String(publicKey.getEncoded()));

			assertTrue(privateKey != null);
			assertTrue(publicKey != null);

			byte[] message1 = new BigInteger("1337").toByteArray();
			byte[] message2 = new BigInteger("1336").toByteArray();
			byte[] sigBytes1;
			byte[] sigBytes2;

			Signature signature1 = Signature.getInstance("SHA256withDSA", "SUN");
			signature1.initSign(privateKey);
			signature1.update(message1);
			sigBytes1 = signature1.sign();
			
			Signature signature2 = Signature.getInstance("SHA256withDSA", "SUN");
			signature2.initSign(privateKey);
			signature2.update(message2);
			sigBytes2 = signature2.sign();

			assertTrue(signature1 != null);
			assertTrue(sigBytes1 != null);
			assertTrue(signature2 != null);
			assertTrue(sigBytes2 != null);

			Signature sigVer = Signature.getInstance("SHA256withDSA", "SUN");
			sigVer.initVerify(publicKey);
			
			sigVer.update(message1);
			assertTrue(sigVer.verify(sigBytes1));
			assertTrue(!sigVer.verify(sigBytes2));
			sigVer.update(message2);
			assertTrue(sigVer.verify(sigBytes2));
			assertTrue(!sigVer.verify(sigBytes1));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void loadKeyAndTest() {

		SecureRandom rand = null;

		String authorityPublicKey = SettingsHelper.getSettings(AuthoritySettings.class).getPublicKey();
		String authorityPrivateKey = SettingsHelper.getSettings(AuthoritySettings.class).getPrivateKey();

		try {
			rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");

			byte[] publicKeyEncoded = Base64.decodeBase64(authorityPublicKey);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
			PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);

			byte[] privateKeyEncoded = Base64.decodeBase64(authorityPrivateKey);
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privateKeyEncoded);
			PrivateKey privateKey = keyFactory.generatePrivate(privKeySpec);

			assertTrue(rand != null);
			assertTrue(publicKeyEncoded != null);
			assertTrue(pubKeySpec != null);
			assertTrue(publicKey != null);
			assertTrue(privateKeyEncoded != null);
			assertTrue(privKeySpec != null);
			assertTrue(privateKey != null);

			System.out.println("Private Key: " + Base64.encodeBase64String(privateKey.getEncoded()));
			System.out.println("Public Key: " + Base64.encodeBase64String(publicKey.getEncoded()));

			assertTrue(privateKey != null);
			assertTrue(publicKey != null);

			byte[] message = new BigInteger("1337").toByteArray();
			byte[] sigBytes;

			Signature signature = Signature.getInstance("SHA256withDSA", "SUN");
			signature.initSign(privateKey);
			signature.update(message);
			sigBytes = signature.sign();

			assertTrue(signature != null);
			assertTrue(sigBytes != null);

			Signature sigVer = Signature.getInstance("SHA256withDSA", "SUN");
			sigVer.initVerify(publicKey);
			sigVer.update(message);
			assertTrue(sigVer.verify(sigBytes));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
