package jpabook.jpashop.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class AES256Util {
    // Util class do not have public constructor


    public String encrypt(final String key, final String data) {
        try {
            byte[] keyData = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivData = key.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyData, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivData));

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                 | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.error("AES Util Encrypt Error", e);
        }
        return key;
    }

    public String decrypt(final String key, final String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyData;

        keyData = key.getBytes(StandardCharsets.UTF_8);
        byte[] ivData = key.substring(0, 16).getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyData, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivData));
        byte[] decrypted = Base64.getMimeDecoder().decode(encryptedData.getBytes(StandardCharsets.UTF_8));
        return new String(cipher.doFinal(decrypted), StandardCharsets.UTF_8);

    }
}
