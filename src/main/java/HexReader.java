import java.io.FileInputStream;
import java.io.IOException;

public class HexReader {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("test_puzzle.bin")) {
            int index = 1;
            int byteRead;
            int totalBytes = 0;
            while ((byteRead = fis.read()) != -1) {
                System.out.print(String.format("%02X ", byteRead)); // Print hex byte by byte
                if(index == 32){
                    System.out.println();
                    index = 0;
                }
                index++;
                totalBytes++;
            }
            System.out.println(totalBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
