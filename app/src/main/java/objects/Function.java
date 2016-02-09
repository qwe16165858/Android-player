/***********************************************************************
 * Static functions used in this project
 */
package objects;

import static objects.Constant.DIGEST_METHOD;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Function {
	public static String hash(String message) throws Exception {
		MessageDigest algorithm = MessageDigest.getInstance(DIGEST_METHOD);
		algorithm.update(message.getBytes());
		return toHex(algorithm.digest());
	}

	public static String toHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash)
			formatter.format("%02x", b);
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	//transfer a object to byte[]
	public static byte[] objectTObyteArray(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	//transfer byte[] to a object with given class type
	public static Object byteArrayTOobject(byte[] data) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	//AES encrypt
	public static byte[] encryptObject(Object object, String key)
			throws IOException, IllegalBlockSizeException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, BadPaddingException {
		byte[] K = key.getBytes();
		byte k[] = new byte[16];
		System.arraycopy(K, 0, k, 0, K.length);
		SecretKeySpec sks = new SecretKeySpec(k, "AES");
		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, sks);
		return cipher.doFinal(objectTObyteArray(object));
	}

	//AES decrypt
	public static Object decryptObject(byte[] data, String key)
			throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			ClassNotFoundException, IllegalBlockSizeException,
			BadPaddingException {
		byte[] K = key.getBytes();
		byte k[] = new byte[16];
		System.arraycopy(K, 0, k, 0, K.length);
		SecretKeySpec sks = new SecretKeySpec(k, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sks);

		return byteArrayTOobject(cipher.doFinal(data));
	}

	//read file method
	public static byte[] readFile(String aInputPath) {
		File file = new File(aInputPath);
		byte[] result = new byte[(int) file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while (totalBytesRead < result.length) {
					int bytesRemaining = result.length - totalBytesRead;
					int bytesRead = input.read(result, totalBytesRead,
							bytesRemaining);
					if (bytesRead > 0)
						totalBytesRead += bytesRead;
				}
				System.out.println("Num bytes read: " + totalBytesRead);
			} finally {
				input.close();
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File not found.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return result;
	}

	//write file method
	public static void writeFile(byte[] aInput, String aOutputFileName) {
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(
						aOutputFileName));
				output.write(aInput);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File not found.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	//generating recharge coupon code.
	public static String rechargeCode() {
		int code_length = 20; // define length of recharge code
		final char[] possibleCharacters = { '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
				'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z' };
		final int possibleCharacterCount = possibleCharacters.length;
		Random rnd = new Random(System.currentTimeMillis());

		// [format] 19 characters: -AAAA-BBBB-CCCC-DDDD
		StringBuffer buf = new StringBuffer(code_length);
		for (int j = 4; j > 0; --j) {
			buf.append('-');
			for (int i = 4; i > 0; --i)
				buf.append(possibleCharacters[rnd
						.nextInt(possibleCharacterCount)]);
		}
		return buf.toString().substring(1);
	}
}
