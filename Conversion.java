import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Conversion {
    public static byte[] convertStringToByte(String encodedKey) {
        byte[] decodedKey = Base64.getUrlDecoder().decode(encodedKey);
        return decodedKey;
    }

    public static String convertSecretKeyToString(byte[] secretKey) throws NoSuchAlgorithmException {
        String encodedKey = Base64.getUrlEncoder().encodeToString(secretKey);
        return encodedKey;
    }

}
