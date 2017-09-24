package util;

import java.math.BigInteger;
import java.security.SecureRandom;

import data.GroupSignature;
import data.ManagerKey;
import data.PublicKey;

public class Signature {

	private static final int modulus = 2048;
	private static final int lE = 504;
	private static final int lQ = 282;
	private static final int lc = 160;
	private static final int le = 60;
	private static final int prime_certainty = 100;

	private static final SecureRandom rand;

	static {
		rand = new SecureRandom();
	}

	private static boolean QuadraticResidue(BigInteger a, BigInteger p, BigInteger q) {
		BigInteger two = BigInteger.ONE.add(BigInteger.ONE);

		BigInteger test1 = p.subtract(BigInteger.ONE).divide(two);
		BigInteger test2 = q.subtract(BigInteger.ONE).divide(two);

		return a.mod(p).modPow(test1, p).equals(BigInteger.ONE) && a.mod(q).modPow(test2, q).equals(BigInteger.ONE);
	}

	private static BigInteger RandValModP(SecureRandom rand, int maxlength, BigInteger p) {
		BigInteger ret = new BigInteger(maxlength, rand).mod(p);
		while (ret.bitLength() != maxlength) {
			ret = new BigInteger(maxlength, rand).mod(p);
		}
		return ret;
	}

	private static BigInteger RandomElementOfQRn(SecureRandom rand, int modulus, BigInteger n, BigInteger p,
			BigInteger q) {
		BigInteger a = RandValModP(rand, modulus, n);

		while (!QuadraticResidue(a, p, q)) {
			a = RandValModP(rand, modulus, n);
		}
		return a;
	}

	public static GroupSignature generateGroupSignature() {
		// todo load from config
		BigInteger generator = new BigInteger("2");

		PublicKey publicKey = new PublicKey();
		ManagerKey managerKey = new ManagerKey();

 		managerKey.setP(new BigInteger((modulus / 2), prime_certainty, rand));
		managerKey.setQ(new BigInteger((modulus / 2), prime_certainty, rand));

		publicKey.setN(managerKey.getProductFromPandQ());

		BigInteger nsquared = publicKey.getProductNandN();

		// some easy checks
		if (publicKey.getN().bitLength() != modulus || !managerKey.getP().isProbablePrime(prime_certainty)
				|| !managerKey.getQ().isProbablePrime(prime_certainty))
			return null;

		BigInteger alpha = new BigInteger(modulus, prime_certainty, rand);

		publicKey.setA(RandomElementOfQRn(rand, modulus, publicKey.getN(), managerKey.getP(), managerKey.getQ()));
		publicKey.setH(RandomElementOfQRn(rand, modulus, publicKey.getN(), managerKey.getP(), managerKey.getQ()));

		publicKey.setG(publicKey.getH().modPow(alpha, publicKey.getN()));

		publicKey.setW(RandomElementOfQRn(rand, modulus, publicKey.getN(), managerKey.getP(), managerKey.getQ()));

		publicKey.setBigQ(new BigInteger(lQ, prime_certainty, rand));
		BigInteger multiplicator = new BigInteger("2").pow(modulus - lQ);

		publicKey.setBigP(publicKey.getBigQ().multiply(multiplicator).add(BigInteger.ONE));

		// todo better handling do not return null or log
		while (true) {
			if (publicKey.getBigP().bitLength() != modulus)
				return null;
			if (publicKey.getBigP().bitLength() == modulus) {
				if (publicKey.getBigP().isProbablePrime(1) && publicKey.getBigP().isProbablePrime(prime_certainty))
					break;
			}
			multiplicator = multiplicator.add(BigInteger.ONE);
			publicKey.setBigP(publicKey.getBigQ().multiply(multiplicator).add(BigInteger.ONE));
		}

		// checks
		if (!publicKey.getBigP().isProbablePrime(prime_certainty)
				|| !publicKey.getBigQ().isProbablePrime(prime_certainty))
			return null;

		publicKey.setBigF(new BigInteger(modulus, rand).mod(publicKey.getBigP()));

		publicKey.setBigF(publicKey.getBigF().modPow(
				(publicKey.getBigP().subtract(BigInteger.ONE)).divide(publicKey.getBigQ()), publicKey.getBigP()));

		managerKey.setXg(RandValModP(rand, lQ, publicKey.getBigQ()));
		managerKey.setXh(RandValModP(rand, lQ, publicKey.getBigQ()));

		publicKey.setBigG(publicKey.getBigF().modPow(managerKey.getXg(), publicKey.getBigP()));

		publicKey.setBigH(publicKey.getBigF().modPow(managerKey.getXh(), publicKey.getBigP()));

		return new GroupSignature(managerKey, publicKey);
	}

}
