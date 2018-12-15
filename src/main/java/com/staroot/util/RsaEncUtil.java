package com.staroot.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class RsaEncUtil {
	public static final int KEY_SIZE = 1024;// OK
	//public static final int KEY_SIZE = 512;// OK
	//public static final int KEY_SIZE = 2048;// NOK
	//public static final int KEY_SIZE = 128;// NOK

	public void createRsaKey(HttpServletRequest request, Model model) {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			//KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA/ECB/PKCS1Padding");
			//KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA/None/PKCS1Padding");
			
			
			
			generator.initialize(KEY_SIZE);

			KeyPair keyPair = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			//KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
			//KeyFactory keyFactory = KeyFactory.getInstance("RSA/None/PKCS1Padding");

			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			HttpSession session = request.getSession();
			// 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
			session.setAttribute("__rsaPrivateKey__", privateKey);

			// 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

			String publicKeyModulus = publicSpec.getModulus().toString(16);
			String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

			System.out.println("publicKeyModulus:" + publicKeyModulus);
			System.out.println("publicKeyExponent:" + publicKeyExponent);

			request.setAttribute("pubKeyM", publicKeyModulus);
			request.setAttribute("pubKeyE", publicKeyExponent);

			String contextPath = request.getContextPath();
			if (contextPath != null && !"".equals(contextPath)) {
				request.setAttribute("contextPath", "/" + request.getContextPath());
			} else {
				request.setAttribute("contextPath", "");
			}

			Map<String, String> rsaInfo = new HashMap<String, String>();
			rsaInfo.put("publicKeyModulus", publicKeyModulus);
			rsaInfo.put("publicKeyExponent", publicKeyExponent);

			model.addAttribute("publicKeyModulus", publicKeyModulus);
			model.addAttribute("publicKeyExponent", publicKeyExponent);
			model.addAttribute("rsaInfo", rsaInfo);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

	}

	public void decryptRequestParam(HttpServletRequest request) {
		String securedUsername = request.getParameter("userId");
		String securedPassword = request.getParameter("password");

		HttpSession session = request.getSession();
		PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
		session.removeAttribute("__rsaPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를
														// 받도록 강제.

		if (privateKey == null) {
			throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
		}
		try {
			
			System.out.println("privateKey of decryptRsa(privateKey, securedUsername) ::" + privateKey);
			System.out.println("securedUsername of decryptRsa(privateKey, securedUsername) ::" + securedUsername);
			
			String username = decryptRsa(privateKey, securedUsername);
			String password = decryptRsa(privateKey, securedPassword);

			System.out.println("RsaEncUtil Decrypt Start......");
			System.out.println("Decrypt username ::" + username);
			System.out.println("Decrypt password ::" + password);
			System.out.println("RsaEncUtil Decrypt End......");

			request.setAttribute("userId", username);
			request.setAttribute("password", password);
			// request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request,
			// response);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
		String decryptedValue="";
		try {
			System.out.println("will decrypt123456 : " + securedValue);
			
			
			//safari에서는 잘되는데 크롬에서는 안되는 이슈발생해서
			//화면단에서 암호화된 hex 값을 base64 로 한번더 인코딩함 
			
			
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("RSA");//초기버전 
			//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			//Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			
			byte[] encryptedBytes = hexToByteArray(securedValue);
			System.out.println("bytep[] encryptedBytes::"+encryptedBytes);
			
			//byte내 제일앞에 0이붙은거를 제거한다
			//참고 : url http://kwon37xi.egloos.com/4427199
			//잘안됨

			/*
			byte[] encryptedBytes2 = new byte[encryptedBytes.length-1];
			if(encryptedBytes[0] == 0){
					System.arraycopy(encryptedBytes, 1, encryptedBytes2, 0, encryptedBytes.length-1);
			}
			*/
			
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			//byte[] decryptedBytes = cipher.doFinal(base64Decode(securedValue));
			
			
			decryptedValue = new String(decryptedBytes, "utf-8"); // 문자
																			// 인코딩
																			// 주의.
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return decryptedValue;
	}

	/**
	 * 16진 문자열을 byte 배열로 변환한다.
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}

	/**
	 * BigInteger를 사용해 hex를 byte[] 로 바꿀 경우 음수 영역의 값을 제대로 변환하지 못하는 문제가 있다.
	 */
	@Deprecated
	public static byte[] hexToByteArrayBI(String hexString) {
		return new BigInteger(hexString, 16).toByteArray();
	}

	public static String base64Encode(byte[] data) throws Exception {
		//

		// BASE64Encoder encoder = new BASE64Encoder();
		// String encoded = encoder.encode(data);
		String encoded = Base64.getEncoder().encodeToString(data);

		return encoded;
	}

	public static byte[] base64Decode(String encryptedData) throws Exception {
		// byte[] asBytes = Base64.getDecoder().decode("c29tZSBzdHJpbmc=");
		byte[] decoded = Base64.getDecoder().decode(encryptedData);

		// String encoded = Base64.getDecoder().decodeToString(data);
		// byte[] decoded = decoder.decodeBuffer(encryptedData);
		return decoded;
	}

}
