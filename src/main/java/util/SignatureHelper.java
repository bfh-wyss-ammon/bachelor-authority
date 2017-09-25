package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;

import data.GroupKey;
import data.ManagerKey;
import data.SecretKey;
import data.Signature;
import data.GroupJoinRequest;
import data.GroupJoinResponse;
import data.PublicKey;

public class SignatureHelper {

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
	
	public static BigInteger randValModP(SecureRandom rand, int maxlength, BigInteger p) {
		BigInteger ret = new BigInteger(maxlength, rand).mod(p);
		while (ret.bitLength() != maxlength) {
			ret = new BigInteger(maxlength, rand).mod(p);
		}
		return ret;
	}
	
	public static BigInteger randVal(SecureRandom rand, int length) {
		BigInteger ret = new BigInteger(length, rand);
		while (ret.bitLength() != length) {
			ret = new BigInteger(length, rand);
		}
		return ret;
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

	public static GroupKey generateGroupKey() {
		// todo load from config

		PublicKey publicKey = new PublicKey();
		ManagerKey managerKey = new ManagerKey();

		managerKey.setP(new BigInteger((modulus / 2), prime_certainty, rand));
		managerKey.setQ(new BigInteger((modulus / 2), prime_certainty, rand));

		publicKey.setN(managerKey.getP().multiply(managerKey.getQ()));


		// some easy checks
		if (publicKey.getN().bitLength() != modulus || !managerKey.getP().isProbablePrime(prime_certainty)
				|| !managerKey.getQ().isProbablePrime(prime_certainty))
			return generateGroupKey();

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
				return generateGroupKey();
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
			return generateGroupKey();

		publicKey.setBigF(new BigInteger(modulus, rand).mod(publicKey.getBigP()));

		publicKey.setBigF(publicKey.getBigF().modPow(
				(publicKey.getBigP().subtract(BigInteger.ONE)).divide(publicKey.getBigQ()), publicKey.getBigP()));

		managerKey.setXg(RandValModP(rand, lQ, publicKey.getBigQ()));
		managerKey.setXh(RandValModP(rand, lQ, publicKey.getBigQ()));

		publicKey.setBigG(publicKey.getBigF().modPow(managerKey.getXg(), publicKey.getBigP()));

		publicKey.setBigH(publicKey.getBigF().modPow(managerKey.getXh(), publicKey.getBigP()));

		return new GroupKey(managerKey, publicKey);
	}

	public static SecretKey initMemberKey(PublicKey publicKey) {

		SecretKey secretKey = new SecretKey();

		secretKey.setX(RandValModP(rand, modulus, publicKey.getN()));

		secretKey.setBigY(publicKey.getBigG().modPow(secretKey.getX(), publicKey.getBigP()));

		secretKey.setR(new BigInteger(modulus, prime_certainty, rand));

		// todo: gw do we need a log here?
		while (!secretKey.getR().gcd(publicKey.getN()).equals(BigInteger.ONE)) {
			secretKey.setR(new BigInteger(modulus, prime_certainty, rand));
		}

		secretKey.setCommitment(
				publicKey.getG()
				.modPow(secretKey.getX(), publicKey.getN())
				.multiply(publicKey.getH()
						.modPow(secretKey.getR(), publicKey.getN()))
				.mod(publicKey.getN()));

		return secretKey;
	}

	public static GroupJoinRequest groupJoinRequest(SecretKey secretKey) {

		// todo maybe we can to this with Memberkey extends GroupJoinRequest
		GroupJoinRequest groupJoinRequest = new GroupJoinRequest();

		groupJoinRequest.setBigY(secretKey.getBigY());
		groupJoinRequest.setCommitment(secretKey.getCommitment());

		return groupJoinRequest;
	}

	public static GroupJoinResponse groupJoinResponse(ManagerKey managerKey, PublicKey publicKey,
			GroupJoinRequest req) {
		GroupJoinResponse groupJoinResponse = new GroupJoinResponse();
		groupJoinResponse.setE(new BigInteger(le, rand));
		BigInteger twoToLE = new BigInteger("2").pow(lE - 1);
		
		groupJoinResponse.setEi(twoToLE.add(groupJoinResponse.getE()));

		boolean repeat = true;
		while (repeat) {
			groupJoinResponse.setE(new BigInteger(le, rand));
			groupJoinResponse.setEi(twoToLE.add(groupJoinResponse.getE()));
			if (groupJoinResponse.getE().bitLength() == le && groupJoinResponse.getEi().bitLength() == lE && groupJoinResponse.getEi().isProbablePrime(prime_certainty))
				repeat = false;
		}
		groupJoinResponse.setRi(new BigInteger(groupJoinResponse.getE().bitLength() - 1, prime_certainty, rand));
		BigInteger commitment = req.getCommitment().multiply(publicKey.getH().modPow(groupJoinResponse.getRi(), publicKey.getN()));
		BigInteger part = publicKey.getA().multiply(commitment).mod(publicKey.getN());

		BigInteger totient = managerKey.getP().subtract(BigInteger.ONE)
				.multiply(managerKey.getQ().subtract(BigInteger.ONE));
		BigInteger privat = groupJoinResponse.getEi().modInverse(totient);

		// encrypt res
		groupJoinResponse.setYi(part.modPow(privat, publicKey.getN()));
		groupJoinResponse.setWi(publicKey.getW().modPow(privat, publicKey.getN()));

		return groupJoinResponse;
	}

	public static void updateMemberKey(GroupJoinResponse groupJoinResponse, SecretKey secretKey) {
		secretKey.setW(groupJoinResponse.getWi());
		secretKey.setE(groupJoinResponse.getE());
		secretKey.setY(groupJoinResponse.getYi());
		secretKey.setR(groupJoinResponse.getRi().add(secretKey.getR()));
		secretKey.setBigE(groupJoinResponse.getEi());
	}
	
	public static byte[] ConvertToBytes(Object object) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(object);
			return bos.toByteArray();
		}
	}
	
	private static BigInteger GetHash(ArrayList<byte[]> values) {
		try {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		BigInteger last = new BigInteger("0");
		for (byte[] value : values){
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
			try {
				outputStream.write(last.toByteArray());
				outputStream.write(value);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte toBeHashedValue[] = outputStream.toByteArray();
			md.update(toBeHashedValue, 0, toBeHashedValue.length);
			last = new BigInteger(1, md.digest());
		}
		return last;
		}
		catch(Exception ex) {
			//todo: error handling
		}
		return null;
	}
	
	public static boolean verify(PublicKey publicKey, byte[] message, Signature signature) {
		if (signature.getZe().bitLength() != (le + lc + le)
				&& signature.getZx().bitLength() != (lQ + lc + le))
			return false;

		boolean isValid = false;
		BigInteger vPart1 = 
				publicKey.getA()
				.multiply(publicKey.getW())
				.modPow(signature.getC()
						.negate(), 
						publicKey.getN());
		BigInteger vPart2 = publicKey.getG()
				.modPow(signature.getZx()
						.negate(), 
						publicKey.getN());
		BigInteger vPart3 = 
				publicKey.getH()
				.modPow(signature.getZr(), publicKey.getN());		
		BigInteger vPart4 = signature.getC().multiply(new BigInteger("2").pow(lE - 1)).add(signature.getZe());		
		BigInteger vPart5 = signature.getU().modPow(vPart4, publicKey.getN());

		BigInteger v = 
				vPart1
				.multiply(vPart2)
				.mod(publicKey.getN())
				.multiply(vPart3)
				.mod(publicKey.getN())
				.multiply(vPart5)
				.mod(publicKey.getN());

		BigInteger bigV1 = signature.getBigU1().modPow(signature.getC().negate(), publicKey.getBigP())
				.multiply(publicKey.getBigF().modPow(signature.getZbigR(), publicKey.getBigP())).mod(publicKey.getBigP());
		
		BigInteger bigV2 = signature.getBigU2().modPow(signature.getC().negate(), publicKey.getBigP())
				.multiply(publicKey.getBigG().modPow(signature.getZbigR().add(signature.getZx()), publicKey.getBigP())).mod(publicKey.getBigP());
		
		BigInteger bigV3 = signature.getBigU3().modPow(signature.getC().negate(), publicKey.getBigP())
				.multiply(publicKey.getBigH().modPow(signature.getZbigR().add(signature.getZe()), publicKey.getBigP())).mod(publicKey.getBigP());
		
		System.out.println("v:" + v);
		System.out.println("bigV1:" + bigV1);
		System.out.println("bigV2:" + bigV2);
		System.out.println("bigV3:" + bigV3);
		System.out.println("getBigU1:" + signature.getBigU1());
		System.out.println("getBigU2:" + signature.getBigU2());
		System.out.println("getBigU3:" + signature.getBigU3());
		System.out.println("u:" + signature.getU());

		
		ArrayList<byte[]> input = new ArrayList<byte[]>();
		try {
			input.add(ConvertToBytes(publicKey));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input.add(signature.getU().toByteArray());
		input.add(v.toByteArray());
		input.add(signature.getBigU1().toByteArray());
		input.add(signature.getBigU2().toByteArray());
		input.add(signature.getBigU3().toByteArray());
		input.add(bigV1.toByteArray());
		input.add(bigV2.toByteArray());
		input.add(bigV3.toByteArray());
		input.add(message);
		BigInteger c = GetHash(input);
		isValid = c.equals(signature.getC());

		return isValid;
	}
	
	
	public static Signature sign(byte[] message, SecretKey secretKey, PublicKey publicKey) {
		
		Signature signature = new Signature();
		// all the variables we need
		BigInteger r = randVal(rand, modulus / 2);
		BigInteger bigR = randValModP(rand, lQ, publicKey.getBigQ());
		signature.setU(
				publicKey.getH()
				.modPow(r, publicKey.getN())
				.multiply(secretKey.getY())
				.mod(publicKey.getN())
				.multiply(publicKey.getW())
				.mod(publicKey.getN()));

		signature.setBigU1(publicKey.getBigF().modPow(bigR, publicKey.getBigP()));
		signature.setBigU2(publicKey.getBigG().modPow(bigR.add(secretKey.getX()), publicKey.getBigP()));
		signature.setBigU3(publicKey.getBigH().modPow(bigR.add(secretKey.getE()), publicKey.getBigP()));

		BigInteger rx = randVal(rand, lQ + lc + le);
		BigInteger rr = randVal(rand, modulus / 2 + lc + le);
		BigInteger re = randVal(rand, le + lc + le);
				
		BigInteger bigRr = randValModP(rand, lQ, publicKey.getBigQ());

		BigInteger v = 
				signature.getU()
				.modPow(re, publicKey.getN())
				.multiply(publicKey.getG()
						.modPow(rx.negate(), publicKey.getN()))
				.multiply(publicKey.getH()
						.modPow(rr, publicKey.getN()))
				.mod(publicKey.getN());
				
		BigInteger bigV1 = publicKey.getBigF().modPow(bigRr, publicKey.getBigP());
		BigInteger bigV2 = publicKey.getBigG().modPow(bigRr.add(rx), publicKey.getBigP());
		BigInteger bigV3 = publicKey.getBigH().modPow(bigRr.add(re), publicKey.getBigP());

		System.out.println("v:" + v);
		System.out.println("bigV1:" + bigV1);
		System.out.println("bigV2:" + bigV2);
		System.out.println("bigV3:" + bigV3);
		System.out.println("getBigU1:" + signature.getBigU1());
		System.out.println("getBigU2:" + signature.getBigU2());
		System.out.println("getBigU3:" + signature.getBigU3());
		System.out.println("u:" + signature.getU());

		
		// generate hashing challenge
		ArrayList<byte[]> input = new ArrayList<byte[]>();
		try {
			input.add(ConvertToBytes(publicKey));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		input.add(signature.getU().toByteArray());
		input.add(v.toByteArray());
		input.add(signature.getBigU1().toByteArray());
		input.add(signature.getBigU2().toByteArray());
		input.add(signature.getBigU3().toByteArray());
		input.add(bigV1.toByteArray());
		input.add(bigV2.toByteArray());
		input.add(bigV3.toByteArray());
		input.add(message);
		signature.setC(GetHash(input));
		signature.setZx(rx.add(signature.getC().multiply(secretKey.getX())));

		BigInteger res = secretKey.getR().negate().subtract(r.multiply(secretKey.getBigE()));
		signature.setZr(rr.add(signature.getC().multiply(res)));

		res = signature.getC().multiply(secretKey.getE());
		signature.setZe(re.add(res));

		signature.setZbigR(bigRr.add(signature.getC().multiply(bigR).mod(publicKey.getBigQ())));
		
		return signature;
	}

}
