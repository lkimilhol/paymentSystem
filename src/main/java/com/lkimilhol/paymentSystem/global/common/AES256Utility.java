package com.lkimilhol.paymentSystem.global.common;

import com.lkimilhol.paymentSystem.domain.CardPayment;
import com.lkimilhol.paymentSystem.global.CardPaymentInfo;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AES256Utility implements AutoCloseable {
    private String iv;
    private Key keySpec;

    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     *
     * @param key
     *            암/복호화를 위한 키값
     * @throws UnsupportedEncodingException
     *             키값의 길이가 16이하일 경우 발생
     */
    final static String key = "javaAesKey123465";

    public AES256Utility() {
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = new byte[0];
        try {
            b = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.keySpec = keySpec;
    }

    public String encryptCardInfo(String cardNumber, String expiryDate, String cvc) {
        String result = null;
        try (AES256Utility utility = new AES256Utility()){
            StringBuilder sb = new StringBuilder();
            sb.append(cardNumber);
            sb.append(CardPaymentInfo.CARD_INFORMATION_DELIMITER);
            sb.append(expiryDate);
            sb.append(CardPaymentInfo.CARD_INFORMATION_DELIMITER);
            sb.append(cvc);
            result = utility.encrypt(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String decryptCardInfo(String encryption) {
        String result = null;
        try (AES256Utility utility = new AES256Utility()){
            result = utility.decrypt(encryption);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * AES256 으로 암호화 한다.
     *
     * @param str
     *            암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    private String encrypt(String str) throws NoSuchAlgorithmException,
            GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     *
     * @param str
     *            복호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    private String decrypt(String str) throws NoSuchAlgorithmException,
            GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }

    @Override
    public void close() throws Exception {
    }
}
