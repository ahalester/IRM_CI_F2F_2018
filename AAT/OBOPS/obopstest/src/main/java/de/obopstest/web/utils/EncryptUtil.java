package de.obopstest.web.utils;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class EncryptUtil {

    private static Logger LOG = LoggerFactory.getLogger(EncryptUtil.class);
    private static final char[] PASSWORD = "enfldsgbnlsngdlksdsgm".toCharArray();
    private static final byte[] SALT = {(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33,
            (byte) 0x10, (byte) 0x12,};

    public static String encrypt(String property) {
        Cipher pbeCipher;
        String encoded = "";
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            encoded = base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException
                | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | UnsupportedEncodingException
                | IllegalBlockSizeException | BadPaddingException e) {
            LOG.error("Unhandled encryption exception!");
        }
        return encoded;
    }

    private static String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    public static String decrypt(String property) {
        String decoded = "";
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            decoded = new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException | IOException e) {
            LOG.error("Unhandled decryption exception!");
        }
        return decoded;
    }

    private static byte[] base64Decode(String property) throws IOException {
        return new BASE64Decoder().decodeBuffer(property);
    }

    @Ignore
    @Test
    public void encryptDecrypt() {
        System.out.println(encrypt(""));
    }
}