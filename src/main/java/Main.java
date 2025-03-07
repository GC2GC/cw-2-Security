import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
        // Create an instance of PuzzleCreator
        PuzzleCreator creator = new PuzzleCreator();

        // Step 1: Create the puzzles (4096 puzzles)
        ArrayList<Puzzle> puzzles = creator.createPuzzles();

        // Print the first puzzle's details (for testing purposes)
        Puzzle firstPuzzle = puzzles.get(0);
        System.out.println("Puzzle Number: " + firstPuzzle.getPuzzleNumber());
        System.out.println("Secret Key (Hex): " + bytesToHex(firstPuzzle.getKey().getEncoded()));

        // Step 2: Encrypt the puzzles and save them to a file
        creator.encryptPuzzlesToFile("puzzles.bin", puzzles);
        System.out.println("Encrypted puzzles have been saved to 'puzzles.bin'.");

        // Step 3: Find the key for puzzle number 1
        SecretKey key = creator.findKey(1, puzzles);
        System.out.println("Found Key for Puzzle 1 (Hex): " + bytesToHex(key.getEncoded()));
    }

    // Helper method to convert byte array to Hexadecimal string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
