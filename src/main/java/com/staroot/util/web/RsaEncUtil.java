package com.staroot.util.web;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
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
    public static final int KEY_SIZE = 1024;//OK
    
    
    public void createRsaKey(HttpServletRequest request, Model model){
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE);
            
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            HttpSession session = request.getSession();
            // 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
            session.setAttribute("__rsaPrivateKey__", privateKey);

            // 공개키를 문자열로 변환하여 JavaScript RSA 라이브러리 넘겨준다.
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
            
            
            System.out.println("publicKeyModulus:"+publicKeyModulus);
            System.out.println("publicKeyExponent:"+publicKeyExponent);

            request.setAttribute("pubKeyM", publicKeyModulus);
            request.setAttribute("pubKeyE", publicKeyExponent);
            
            String contextPath = request.getContextPath();
            if(contextPath != null && !"".equals(contextPath)){
            	request.setAttribute("contextPath", "/"+request.getContextPath());
            }else{
            	request.setAttribute("contextPath", "");
            }
            
            Map<String, String> rsaInfo = new HashMap<String, String>();
            rsaInfo.put("publicKeyModulus", publicKeyModulus);
            rsaInfo.put("publicKeyExponent", publicKeyExponent);
            
            model.addAttribute("publicKeyModulus",publicKeyModulus);
            model.addAttribute("publicKeyExponent",publicKeyExponent);
            model.addAttribute("rsaInfo",rsaInfo);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }
    
    
    public void decryptRequestParam(HttpServletRequest request){
        String securedUsername = request.getParameter("userId");
        String securedPassword = request.getParameter("password");

        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute("__rsaPrivateKey__");
        session.removeAttribute("__rsaPrivateKey__"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.

        if (privateKey == null) {
            throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
        }
        try {
            String username = decryptRsa(privateKey, securedUsername);
            String password = decryptRsa(privateKey, securedPassword);
            
    		System.out.println("RsaEncUtil Decrypt Start......");
    		System.out.println("Decrypt username ::"+username);
    		System.out.println("Decrypt password ::"+password);
    		System.out.println("RsaEncUtil Decrypt End......");

            request.setAttribute("userId", username);
            request.setAttribute("password", password);
            //request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        System.out.println("will decrypt : " + securedValue);
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }

    /**
     * 16진 문자열을 byte 배열로 변환한다.
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
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
        	
        //BASE64Encoder encoder = new BASE64Encoder();
        //String encoded = encoder.encode(data);
        	String encoded = Base64.getEncoder().encodeToString(data);
        
        return encoded;
    }

    public static byte[] base64Decode(String encryptedData) throws Exception {
        //byte[] asBytes = Base64.getDecoder().decode("c29tZSBzdHJpbmc=");
    	byte[] decoded = Base64.getDecoder().decode(encryptedData);
    	
    	//String encoded = Base64.getDecoder().decodeToString(data);
        //byte[] decoded = decoder.decodeBuffer(encryptedData);
        return decoded;
    }

	
}
