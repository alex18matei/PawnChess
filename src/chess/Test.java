package chess;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Roxana on 11/1/2016.
 */
public class Test {

    private char[][] initialState;

    public char[][] getInitialState() {
        return initialState;
    }

    public void setInitialState(char[][] initialState) {
        this.initialState = copy(initialState);
    }

    public static char[][] copy(char[][] src) {
        char[][] dst = new char[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return dst;
    }

    public char[] toChar(String line) {
        char[] myChar = new char[8];
        int j = 0;
        for (int i = 1; i < line.length() && j < 8; i++)
            if (line.charAt(i) != ' ')
                myChar[j++] = line.charAt(i);
        return myChar;
    }

    public char[][] readState() throws Exception {
        char[][] state = new char[8][8];

        try {
            String line = null;
            int i = 0, j = 0;
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Roxana/Documents/GitHub/PawnChess/input.txt"));
            while ((line = reader.readLine()) != null) {
                if (i >= 1 && i <= 8) {
                    state[j++] = toChar(line);
                }
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file doesn't exist.");
        }
        return state;
    }

    public void writeState(String currentState) {
        try {

            FileWriter fw = new FileWriter("C:/Users/Roxana/Documents/GitHub/PawnChess/input.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(currentState);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws Exception {
        char[][] initialState = this.readState();
        char[][] currentState;

        Chessboard chessboard = new Chessboard();
        System.out.println(chessboard.toString(initialState));

        if (!chessboard.isComputerTurn()) {
            System.out.println("Player 1 (White)");
        } else {
            System.out.println("Player 2 (Black)");
        }

        do {
            currentState = chessboard.getPlayerMove(initialState);
            if (currentState == initialState) {
                System.out.println("Mutarea nu este valida. Introdu din nou");
            }
        } while (currentState == initialState);
        this.writeState(chessboard.toString(currentState));
        System.out.println(chessboard.toString(currentState));
    }
}
