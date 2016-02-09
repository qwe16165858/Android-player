/***********************************************************************
 * Download code generation, and conversion
 */
package objects;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.Random;
import java.math.BigInteger;

public class Coupon {
	private static final int possibleMusicIDCount = 5; // define total size of
														// music ID

	//download code generation
	public static String genCoupon(int idx) {
		final char[] possibleCharacters = { '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z' };

		final int possibleCharacterCount = possibleCharacters.length;

		int i = 0;

		Random rnd = new Random(System.currentTimeMillis());

		// 16 characters: coupon code
		// 5 characters: music ID
		// 32 characters: MD5

		// create coupon code
		StringBuffer buf = new StringBuffer(16 + possibleMusicIDCount + 32);

		for (i = 16; i > 0; i--) {
			buf.append(possibleCharacters[rnd.nextInt(possibleCharacterCount)]);
		}
		// System.out.println("Coupon Code (16) ==> "+buf);

		// append Music id
		buf.append(String.format("%05d", idx));
		// System.out.println("Coupon Code + Music ID ==> "+buf);

		String password = buf.toString();

		// calculate MD5
		String MD5_result;
		MD5_result = getMD5(password);
		// System.out.println("MD5 (32) ==> "+MD5_result);

		// make final coupon code (coupon code + md5)
		buf.append(MD5_result);
		String final_result = buf.toString();

		return final_result;
	}

	// Validation: MD5 check
	public static boolean checkValidation(String password) {

		String couponID = password.substring(0, 16 + possibleMusicIDCount);
		String md5 = password.substring(16 + possibleMusicIDCount,
				16 + possibleMusicIDCount + 32);

		String MD5_check;
		MD5_check = getMD5(couponID);

		return MD5_check.equals(md5);
	}

	// Convert coupon code to element
	public static Element codeToElement(String code, Pairing pairing) {
		BigInteger bigInt = new BigInteger(code.getBytes());

		Element e = pairing.getGT().newElement(bigInt);

		return e;
	}

	// Convert element to coupon code
	public static String elementToCode(Element e, Pairing pairing) {
		BigInteger convert = e.duplicate().toBigInteger();
		String textBack = new String(convert.toByteArray());

		return textBack;
	}

	//MD5 get
	public static String getMD5(String str) {
		String MD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			MD5 = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			MD5 = null;
		}
		return MD5;
	}
}
