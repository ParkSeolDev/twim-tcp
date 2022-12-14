package jpabook.jpashop.common.util;

import org.springframework.stereotype.Component;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


@Component
public class RSAEncryptionUtil {

    private static final int KEY_SIZE = 512;
    /**

     * 키페어 생성
     * @return

     */

    public HashMap<String, String> createKeypairAsString() throws NoSuchAlgorithmException {

        HashMap<String, String> stringKeypair = new HashMap<>();

        SecureRandom secureRandom = new SecureRandom();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(KEY_SIZE, secureRandom);

        KeyPair keyPair = keyPairGenerator.genKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();


        String stringPublicKey = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String stringPrivateKey = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());

        stringKeypair.put(stringPublicKey, stringPrivateKey);
//			stringKeypair.put("key", stringPublicKey);
//			stringKeypair.put("value", stringPrivateKey);

        return stringKeypair;

    }



    /**

     * 암호화

     */

    public String encode(String plainData, String stringPublicKey) {

        String encryptedData = null;

        try {

            //평문으로 전달받은 공개키를 공개키객체로 만드는 과정

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] bytePublicKey = java.util.Base64.getDecoder().decode(stringPublicKey.getBytes());

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);

            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);



            //만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);



            //평문을 암호화하는 과정

            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());

            encryptedData = java.util.Base64.getEncoder().encodeToString(byteEncryptedData);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return encryptedData;

    }



    /**

     * 복호화

     */

    public String decode(String encryptedData, String stringPrivateKey) {

        String decryptedData = null;

        try {

            //평문으로 전달받은 개인키를 개인키객체로 만드는 과정

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			String str = stringPrivateKey.replaceAll(" ","");
//			String str2 = stringPrivateKey.replaceAll("\\+", "%2B");
//			String str4 = str2.replaceAll("=", "%3D");
//			String str3 = URLEncoder.encode(stringPrivateKey, StandardCharsets.UTF_8);

            byte[] bytePrivateKey = Base64.getMimeDecoder().decode((stringPrivateKey.getBytes()));

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);

            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);



            //만들어진 개인키객체를 기반으로 암호화모드로 설정하는 과정

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);



            //암호문을 평문화하는 과정

            byte[] byteEncryptedData = Base64.getMimeDecoder().decode(encryptedData.getBytes());

            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);

            decryptedData = new String(byteDecryptedData);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return decryptedData;

    }
}