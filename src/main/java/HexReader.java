import java.io.FileInputStream;
import java.io.IOException;

public class HexReader {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("puzzles.bin")) {
            int index = 1;
            int byteRead;
            while ((byteRead = fis.read()) != -1) {
                System.out.print(String.format("%02X ", byteRead)); // Print hex byte by byte
                if(index == 32){
                    System.out.println();
                    index = 0;
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
