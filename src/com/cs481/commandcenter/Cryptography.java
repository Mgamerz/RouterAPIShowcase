package com.cs481.commandcenter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

/**
 * Cryptography class used for encrypting user profile PINs
 * 
 * @author Mike Perez
 * 
 */

public class Cryptography {
	/**
	 * 
	 * @param code
	 *            PIN code
	 * @param salt
	 *            Salting value to randomize the pin code per-device
	 * @return SecretKey that can be used for encrypting/decrypting
	 * @throws NoSuchAlgorithmException
	 *             Algorithm not implemented on this device
	 * @throws InvalidKeySpecException
	 *             Incorrect keyspec
	 */
	public static SecretKey generateKey(String code, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Number of PBKDF2 hardening rounds to use. Larger values increase
		// computation time. You should select a value that causes computation
		// to take >100ms.
		final int iterations = 1000;

		// Generate a 256-bit key
		final int outputKeyLength = 256;

		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec(code.toCharArray(), salt, iterations, outputKeyLength);
		SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
		return secretKey;
	}

	/**
	 * Encrypts a message with a secretkey and returns it as bytes.
	 * 
	 * @param message
	 *            String message to encrypt
	 * @param secret
	 *            Secret key to use for encrypting. Can be generated with
	 *            generateKey()
	 * @return byte array of encrypted message
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidParameterSpecException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		/* Encrypt the message. */
		// Log.i(CommandCenterActivity.TAG, "Encrypting "+message);

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
		// Log.i(CommandCenterActivity.TAG, "Encrypted ciphertext to"+
		// Base64.decode(cipherText, Base64.DEFAULT));
		return cipherText;
	}

	/**
	 * Decrypts a byte array into a string using a secret key
	 * 
	 * @param cipherText
	 *            byte array that represent the encrypted version of a string
	 * @param secret
	 *            Secret key used for encryption/decryption, made by
	 *            generateKey()
	 * @return String that has been decrypted. Throws one of the large amount of
	 *         exceptions below if an error occurs.
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidParameterSpecException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

		/*
		 * Decrypt the message, given derived encContentValues and
		 * initialization vector.
		 */
		// Log.i(CommandCenterActivity.TAG, "Decrypting"+
		// Base64.decode(cipherText, Base64.DEFAULT));

		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret);
		String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
		// Log.i(CommandCenterActivity.TAG, "Decrypted to "+decryptString);

		return decryptString;
	}

	/**
	 * Creates a UUID (Universally Unique identifier) for this device, based on
	 * a several factors. It should always remain the same on a device, but
	 * differ among devices and device resets.
	 * 
	 * @param context Context to read internal information from
	 * @return UUID string
	 */
	public static String createLocalUUID(Context context) {
		SharedPreferences crypto = context.getSharedPreferences(context.getResources().getString(R.string.crypto_prefsdb), Context.MODE_PRIVATE);
		String uuid = crypto.getString("uuid", null);

		String device_uuid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		uuid = uuid + device_uuid; // device specific. Might want to make
									// this more random.
		return uuid;
	}
}
