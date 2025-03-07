import javax.crypto.SecretKey;
import java.util.Arrays;

public class Puzzle {
    private int puzzleNumber;
    private SecretKey secretKey;

    // Constructor that receives puzzle number and secret key
    public Puzzle(int puzzleNumber, SecretKey secretKey) {
        this.puzzleNumber = puzzleNumber;
        this.secretKey = secretKey;
    }

    // Method to get the puzzle number
    public int getPuzzleNumber() {
        return puzzleNumber;
    }

    // Method to get the secret key
    public SecretKey getKey() {
        return secretKey;
    }

    // Method to return the puzzle as a byte array
    public byte[] getPuzzleAsBytes() {
        byte[] puzzle = new byte[32]; // Total length of the puzzle is 32 bytes

        // Fill first 16 bytes with 0x00 (128 zero bits)
        Arrays.fill(puzzle, 0, 16, (byte) 0);

        // Insert the puzzle number as 2 bytes (16-bit integer) at positions 16 to 17
        byte[] puzzleNumberBytes = CryptoLib.smallIntToByteArray(puzzleNumber);
        System.arraycopy(puzzleNumberBytes, 0, puzzle, 16, 2);

        // Insert the secret key (64-bit, 8 bytes) at positions 18 to 25
        System.arraycopy(secretKey.getEncoded(), 0, puzzle, 18, 8);

        // Insert 6 bytes of padding (0x00) at positions 26 to 31
        Arrays.fill(puzzle, 26, 32, (byte) 0);

        return puzzle;
    }
    public static void main(String[] args) {
        try {
            // Use CryptoLib's method to generate a random DES key (no KeyGenerator)
            byte[] desKeyBytes = new byte[8];
            // Randomly generate the DES key (this is just an example for now)
            for (int i = 0; i < 8; i++) {
                desKeyBytes[i] = (byte) (Math.random() * 256 - 128); // Random byte
            }

            // Create a SecretKey using CryptoLib (this assumes CryptoLib.createKey exists)
            SecretKey secretKey = CryptoLib.createKey(desKeyBytes);

            // Create a puzzle with puzzle number 1 and the generated secret key
            Puzzle puzzle = new Puzzle(1, secretKey);

            // Print the puzzle number and the key (in hexadecimal format)
            System.out.println("Puzzle Number: " + puzzle.getPuzzleNumber());
            System.out.println("Secret Key (Hex): " + bytesToHex(secretKey.getEncoded()));


            // Get the puzzle as a byte array
            byte[] puzzleBytes = puzzle.getPuzzleAsBytes();


            System.out.println(bytesToHex(puzzleBytes));
            // Print the puzzle in hexadecimal format for inspection

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
