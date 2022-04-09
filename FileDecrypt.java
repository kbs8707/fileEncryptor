import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.Checksum;

public class FileDecrypt {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public void decryptMessage(String password, File inputFile, String fileName) throws NoSuchPaddingException,
    NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {

        SecretKey secretKey = new SecretKeySpec(Conversion.convertStringToByte(password), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        String originalName = new String (Conversion.convertStringToByte(Conversion.convertSecretKeyToString(cipher.doFinal(Conversion.convertStringToByte(fileName)))));
        File decryptFile = new File("decrypted", originalName);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(decryptFile);

        byte[] inputBytes = new byte[8192];

        int count;
        while ((count = inputStream.read(inputBytes)) > 0) {
            byte[] outputBytes = cipher.update(inputBytes, 0, count);
            outputStream.write(outputBytes);
        }
        byte[] outputBytes = cipher.doFinal();
        outputStream.write(outputBytes);
        
        inputStream.close();
        outputStream.close();

        File original = new File("origin", originalName);

        System.out.println("Hash value of original file: " + CheckSum.checkSumMD5(original));
        System.out.println("Hash value of decrypted file: " + CheckSum.checkSumMD5(decryptFile));
    }
    
    public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
    BadPaddingException, IllegalBlockSizeException, IOException {
        // String encKeyString = "wrongkey12345678";

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name to decrypt");
        String fileName = sc.nextLine();
        
        File inputFile = new File("encrypted", fileName);

        while(!inputFile.isFile()) {
            System.out.println("File doesn't exist, enter another file name");
            fileName = sc.nextLine();
            inputFile = new File("encrypted", fileName);
        }

        System.out.println("Enter the decryption password");
        String password = sc.nextLine();

        FileDecrypt fd = new FileDecrypt();

        Timer decryptTimer = new Timer();

        decryptTimer.start();
        fd.decryptMessage(password, inputFile, fileName);
        decryptTimer.end();

        System.out.println("Decryption time: " + decryptTimer.timeElapsed() + " ms");
        
    }
    
}
