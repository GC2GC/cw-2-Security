public class Test {

    public static void main(String[] args) {

        // 16 bytes 0s
        //2 bytes number
        //8 byte key 45 43 00 00 00 00 00 00
        // pad to 32

        PuzzleCracker cr = new PuzzleCracker("puzzles.bin");
        Puzzle pu = cr.crack(0);
        System.out.println(pu);

    }
}
