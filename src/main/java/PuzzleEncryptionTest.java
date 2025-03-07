import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.util.Arrays;

public class PuzzleEncryptionTest {
    public static void main(String[] args) {
        try {
            // 🔥 Create a 24-byte puzzle (let PKCS5Padding add 8 bytes to make 32)
            byte[] puzzleBytes = new byte[24];
            Arrays.fill(puzzleBytes, (byte) 0x00); // First 16 bytes zero

            // ✅ Set the puzzle number at bytes 16-17
            byte[] puzzleNumBytes = CryptoLib.smallIntToByteArray(1);
            System.arraycopy(puzzleNumBytes, 0, puzzleBytes, 16, 2);

            // ✅ Set the DES key: [2 random bytes] + [6 zero bytes]
            byte[] keyBytes = new byte[8];
            keyBytes[0] = 0x0B; // Hardcoded to match the verified output
            keyBytes[1] = 0x0B;
            Arrays.fill(keyBytes, 2, 8, (byte) 0); // Ensure last 6 bytes are zero

            // ✅ Create DES key from CryptoLib
            SecretKey encryptionKey = CryptoLib.createKey(keyBytes);

            // ✅ Copy the final key into the puzzle structure (bytes 18-23)
            System.arraycopy(keyBytes, 0, puzzleBytes, 18, 6); // Copy only 6 bytes

            // ✅ Debug: Print raw puzzle before encryption
            System.out.println("Raw Puzzle Bytes (before encryption): " + bytesToHex(puzzleBytes));
            System.out.println("🔑 Encryption Key: " + bytesToHex(keyBytes));

            // 🔒 Encrypt the puzzle using DES with PKCS5Padding
            byte[] encryptedPuzzle = encryptPuzzle(puzzleBytes, encryptionKey);

            // ✅ Debugging: Print encrypted puzzle
            System.out.println("🔒 Encrypted Puzzle Bytes: " + bytesToHex(encryptedPuzzle));
            System.out.println("📏 Encrypted Length: " + encryptedPuzzle.length);

            // ✅ Write encrypted puzzle to binary file
            try (FileOutputStream fos = new FileOutputStream("test_puzzle.bin")) {
                fos.write(encryptedPuzzle);
            }

            System.out.println("✅ Puzzle successfully written to test_puzzle.bin");

            // 🧩 Now run `crack(0)`
            PuzzleCracker cracker = new PuzzleCracker("test_puzzle.bin");
            Puzzle crackedPuzzle = cracker.crack(0); // Crack the first (only) puzzle

            if (crackedPuzzle == null) {
                System.out.println("❌ Crack failed! No puzzle was retrieved.");
            } else {
                System.out.println("✅ Crack successful!");
                System.out.println("Cracked Puzzle Number: " + crackedPuzzle.getPuzzleNumber());

                // Debug: Check if the extracted key matches our original key
                byte[] extractedKey = crackedPuzzle.getKey().getEncoded();
                System.out.println("Extracted Key: " + bytesToHex(extractedKey));
                System.out.println("Original Key Used for Encryption: " + bytesToHex(keyBytes));

                if (Arrays.equals(extractedKey, keyBytes)) {
                    System.out.println("✅ Extracted key matches encryption key!");
                } else {
                    System.out.println("❌ Extracted key does NOT match!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] encryptPuzzle(byte[] puzzleData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES"); // Uses PKCS5Padding by default
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(puzzleData); // PKCS5Padding will now pad it to 32 bytes
    }

    // Utility to convert byte arrays to hex strings for debugging
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
