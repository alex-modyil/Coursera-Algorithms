import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        char[] chars = createArray();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            char tmpin, count, tmpout;
            for (count = 0, tmpout = chars[0]; ch != chars[count]; count++) {
                tmpin = chars[count];
                chars[count] = tmpout;
                tmpout = tmpin;
            }
            chars[count] = tmpout;
            BinaryStdOut.write(count);
            chars[0] = ch;
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] chars = createArray();
        while (!BinaryStdIn.isEmpty()) {
            char count = BinaryStdIn.readChar();
            BinaryStdOut.write(chars[count], 8);
            char index = chars[count];
            while (count > 0) {
                chars[count] = chars[--count];
            }
            chars[0] = index;
        }
        BinaryStdOut.close();
    }

    private static char[] createArray() {
        char[] chars = new char[R];
        for (char i = 0; i < R; i++) {
            chars[i] = i;
        }
        return chars;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected + or -\n");
        }
        String first = args[0];
        if (first.equals("+")) {
            decode();
        } else if (first.equals("-")) {
            encode();
        } else {
            throw new IllegalArgumentException("Unknown argument: " + first + "\n");
        }
    }
}
