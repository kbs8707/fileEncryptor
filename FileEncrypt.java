
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.util.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileEncrypt {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    private void encryptMessage(byte[] keyBytes, File inputFile) throws InvalidKeyException, NoSuchPaddingException,
    NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        String inputName = inputFile.getName();
        String outputName = Conversion.convertSecretKeyToString(cipher.doFinal(inputName.getBytes()));
        String password = Conversion.convertSecretKeyToString(secretKey.getEncoded());

        File encryptFile = new File("encrypted", outputName);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(encryptFile);
        
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
        
        System.out.println("Your encrypted file name is: " + outputName);
        System.out.println("Your decryption key is: " + password);
    }
    
    public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
    BadPaddingException, IllegalBlockSizeException, IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the file name to encrypt");
        String original = sc.nextLine();
        
        File inputFile = new File("origin", original);

        while(!inputFile.isFile()) {
            System.out.println("File doesn't exist, enter another file name");
            original = sc.nextLine();
            inputFile = new File("origin", original);
        }

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // for example
        SecretKey secretKey = keyGen.generateKey();
        

        FileEncrypt fe = new FileEncrypt();        
        Timer encryptTimer = new Timer();
        
        encryptTimer.start();
        fe.encryptMessage(secretKey.getEncoded(), inputFile);
        encryptTimer.end();
        
        System.out.println("Encryption time: " + encryptTimer.timeElapsed() + " ms");
        
    }
}