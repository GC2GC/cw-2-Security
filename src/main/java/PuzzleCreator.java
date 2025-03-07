import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PuzzleCreator {

    public static void main(String[] args) {
        PuzzleCreator cr = new PuzzleCreator();
        cr.createRandomKey();

    }

    // Constructor - no parameters needed
    public PuzzleCreator() {
    }

    // Method to create 4096 puzzles and return an ArrayList
    public ArrayList<Puzzle> createPuzzles() throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
        ArrayList<Puzzle> puzzles = new ArrayList<>();

        for (int i = 1; i <= 4096; i++) {
            // Generate a random 8-byte DES key
            byte[] desKey = createRandomKey();

            // Generate the puzzle with puzzle number i and the generated DES key
            SecretKey secretKey = CryptoLib.createKey(desKey);
            Puzzle puzzle = new Puzzle(i, secretKey);

            // Add the puzzle to the list
            puzzles.add(puzzle);
        }

        return puzzles;
    }


    public byte[] createRandomKey() {
        byte[] key = new byte[8];  // DES key is 8 bytes (64 bits)
        Random random = new Random();

        // Generate random bytes for the first 2 bytes in the range 0-255
        for (int i = 0; i < 2; i++) {
            key[i] = (byte) (random.nextInt(256) & 0xFF);  // Ensure value is between 0 and 255
        }

        // Set the last 6 bytes (48 bits) to 0x00
        Arrays.fill(key, 2, 8, (byte) 0);
        for(byte b :key){
            System.out.print(b);
        }

        return key;
    }
    // Method to encrypt a puzzle using DES encryption
    public byte[] encryptPuzzle(byte[] puzzle, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            System.out.println(puzzle.length);
            return cipher.doFinal(puzzle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to encrypt all puzzles and write them to a file
    public void encryptPuzzlesToFile(String filename, ArrayList<Puzzle> puzzles) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            for (Puzzle puzzle : puzzles) {
                byte[] puzzleBytes = puzzle.getPuzzleAsBytes();
                byte[] encryptedPuzzle = encryptPuzzle(puzzleBytes, puzzle.getKey());
                fos.write(encryptedPuzzle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to find the key associated with a given puzzle number
    public SecretKey findKey(int puzzleNumber, ArrayList<Puzzle> puzzles) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getPuzzleNumber() == puzzleNumber) {
                return puzzle.getKey();
            }
        }
        return null; // Return null if no matching puzzle found
    }
}
