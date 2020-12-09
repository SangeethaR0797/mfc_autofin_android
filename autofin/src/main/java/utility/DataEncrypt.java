package utility;


import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DataEncrypt {

    private static final String TAG = DataEncrypt.class.getSimpleName();
    // private String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
    private String iv = "AVVWGI37PQB6SFJE";//Dummy iv (CHANGE IT!)
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    private String SecretKey = "AVVWGI37PQB6SFJE";//Dummy secretKey (CHANGE IT!)

    public DataEncrypt() {

        ivspec = new IvParameterSpec(iv.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String decrypt(String code) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;
        String plaintext="";

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] bytes=code.trim().getBytes("UTF-8");
            byte[] enc_bytes= Base64.decode(bytes,Base64.DEFAULT);

            plaintext = new String(cipher.doFinal(enc_bytes), StandardCharsets.UTF_8).trim();
            Log.i(TAG, "decrypt: "+plaintext);
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return plaintext;
    }


    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }

        int len = data.length;
        String str = "";
        for (byte datum : data) {
            if ((datum & 0xFF) < 16)
                str = str + "0" + Integer.toHexString(datum & 0xFF);
            else
                str = str + Integer.toHexString(datum & 0xFF);
        }
        return str;
    }


    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }


    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        StringBuilder sourceBuilder = new StringBuilder(source);
        for (int i = 0; i < padLength; i++) {
            sourceBuilder.append(paddingChar);
        }
        source = sourceBuilder.toString();

        return source;
    }


}
