package chess;

import java.util.Arrays;
import java.util.Scanner;

public class Chessboard {

    private char[][] state;
    private boolean isComputerTurn = false;
    private int enPassantI, enPassantJ;
    private boolean wasInitialMove = false;

    public char[][] init() {
        state = new char[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (i == 1) {
                    state[i][j] = 'B';
                } else if (i == 6) {
                    state[i][j] = 'W';
                } else {
                    state[i][j] = '_';
                }
            }
        }
        return state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            sb.append(i + " ");
            for (int j = 0; j < 8; ++j) {
                sb.append(state[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String toString(char[][] state) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = 0; i < 8; ++i)
            sb.append(i + " ");
        sb.append("\n");
        for (int i = 0; i < 8; ++i) {
            sb.append(i + " ");
            for (int j = 0; j < 8; ++j) {
                sb.append(state[i][j] + " ");
            }
            sb.append(i + " \n");
        }
        sb.append("  ");
        for (int i = 0; i < 8; ++i)
            sb.append(i + " ");
        sb.append("\n");
        return sb.toString();
    }

    public boolean isFinal(char[][] state) {
        for (int j = 0; j < 8; ++j) {
            if (state[0][j] == 'W') {
                return true;
            }
        }
        for (int j = 0; j < 8; ++j) {
            if (state[7][j] == 'B') {
                return true;
            }
        }
        return false;
    }

    public static char[][] copy(char[][] src) {
        char[][] dst = new char[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return dst;
    }

    public boolean isOutOfMatrix(int i, int j) {
        if (i < 0 || i >= 8)
            return true;
        if (j < 0 || j >= 8)
            return true;
        return false;
    }

    public boolean isOutOfMatrix(int i) {
        if (i < 0 || i >= 8)
            return true;
        return false;
    }

    public boolean isFree(char[][] state, int line, int col) {
        return state[line][col] == '_';
    }

    public char[][] initialTransition(char[][] currentState, int currentJ) {
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if (!isComputerTurn) {
            initialTrasition[6][currentJ] = '_';
            initialTrasition[4][currentJ] = 'W';
            enPassantI = 5;
        } else {
            initialTrasition[1][currentJ] = '_';
            initialTrasition[3][currentJ] = 'B';
            enPassantI = 2;
        }
        enPassantJ = currentJ;
        wasInitialMove = true;
        return initialTrasition;
    }

    public char[][] normalTransition(char[][] currentState, int currentI, int currentJ) {
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if (!isComputerTurn) {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI - 1][currentJ] = 'W';
        } else {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI + 1][currentJ] = 'B';
        }
        wasInitialMove = false;
        return initialTrasition;
    }

    public char[][] captureTransition(char[][] currentState, int currentI, int currentJ, int targetJ) {
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if (!isComputerTurn) {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI - 1][targetJ] = 'W';
        } else {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI + 1][targetJ] = 'B';
        }
        wasInitialMove = false;
        return initialTrasition;
    }

    public char[][] enPassantTransition(char[][] currentState, int currentI, int currentJ) {
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if (!isComputerTurn) {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[enPassantI][enPassantJ] = 'W';
            initialTrasition[enPassantI + 1][enPassantJ] = '_';
        } else {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[enPassantI][enPassantJ] = 'B';
            initialTrasition[enPassantI - 1][enPassantJ] = '_';
        }
        return initialTrasition;
    }

    public boolean validateInitial(char[][] currentState, int currentI, int currentJ) {
        if (isOutOfMatrix(currentJ))
            return false;
        if (!isComputerTurn) {
            if (!isFree(currentState, 4, currentJ) || !isFree(currentState, 5, currentJ)
                    || currentState[currentI][currentJ] != 'W' || currentI != 6)
                return false;
        } else {
            if (!isFree(currentState, 3, currentJ) || !isFree(currentState, 2, currentJ)
                    || currentState[currentI][currentJ] != 'B' || currentI != 1)
                return false;
        }
        return true;
    }

    public boolean validateNormal(char[][] currentState, int currentI, int currentJ) {

        if (isOutOfMatrix(currentI, currentJ))
            return false;
        if (!isComputerTurn) {
            if (!isFree(currentState, currentI - 1, currentJ) || currentState[currentI][currentJ] != 'W')
                return false;
        } else {
            if (!isFree(currentState, currentI + 1, currentJ) || currentState[currentI][currentJ] != 'B')
                return false;
        }
        return true;
    }

    public boolean validateCapture(char[][] currentState, int currentI, int currentJ, int targetJ) {
        if (isOutOfMatrix(currentI, currentJ) || isOutOfMatrix(targetJ))
            return false;
        if (!isComputerTurn) {
            if (currentState[currentI - 1][targetJ] != 'B' || currentState[currentI][currentJ] != 'W')
                return false;
        } else {
            if (currentState[currentI + 1][targetJ] != 'W' || currentState[currentI][currentJ] != 'B')
                return false;
        }
        return true;
    }

    public boolean validateEnPassant(char[][] currentState, int currentI, int currentJ) {
        if (isOutOfMatrix(currentI, currentJ))
            return false;
        if (!wasInitialMove)
            return false;
        if (!isComputerTurn) {
            if (currentState[currentI][currentJ] != 'W')
                return false;
        } else {
            if (currentState[currentI][currentJ] != 'B')
                return false;
        }
        return true;
    }

    public void run() {
        char[][] initialState = init();
        System.out.println(this.toString(initialState));
        char[][] currentState;// = getPlayerMove(initialState);


        while (!isFinal(initialState)) {
            if (!isComputerTurn) {
                System.out.println("Player 1 (White)");
            } else {
                System.out.println("Player 2 (Black)");
            }
            do {
                currentState = getPlayerMove(initialState);
                if (currentState == initialState) {
                    System.out.println("Mutarea nu este valida. Introdu din nou");
                }
            } while (currentState == initialState);
            initialState = currentState;
            isComputerTurn = !isComputerTurn;
            System.out.println(this.toString(currentState));
        }

        System.out.println("Done");

    }

    public boolean validateMove(int move) {
        return (move == 1 || move == 2 || move == 3 || move == 4);
    }

    public int getMove(Scanner scan) {

        int move;
        do {
            System.out.println("Introdu mutare:");
            System.out.println("1 - Initial Transition");
            System.out.println("2 - Normal Transition");
            System.out.println("3 - Capture Transition");
            System.out.println("4 - EnPassant Transition");
            move = scan.nextInt();
            if (!validateMove(move)) {
                System.out.println("Mutarea nu este valida. Introdu din nou");
            }
        } while (!validateMove(move));
        return move;
    }

    private char[][] getPlayerMove(char[][] initialState) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Introdu coordonatele pionului:");
        System.out.println("Linie:");
        int linie = scan.nextInt();
        System.out.println("Coloana:");
        int coloana = scan.nextInt();


        int move = getMove(scan);
        //scan.close();
        if (move == 1) { //initialMove
            if (validateInitial(initialState, linie, coloana))
                return initialTransition(initialState, coloana);
        } else if (move == 2) {
            if (validateNormal(initialState, linie, coloana))
                return normalTransition(initialState, linie, coloana);
        } else if (move == 3) {
            System.out.println("Introdu 0-stanga sau 1-dreapta:");
            scan = new Scanner(System.in);
            int leftOrRight = scan.nextInt();
            //scan.close();
            int targetJ = -1;
            if (leftOrRight == 0)
                targetJ = coloana - 1;
            else if (leftOrRight == 1) {
                targetJ = coloana + 1;
            }
            if (validateCapture(initialState, linie, coloana, targetJ))
                return captureTransition(initialState, linie, coloana, targetJ);
        } else if (move == 4) {
            if (validateEnPassant(initialState, linie, coloana))
                return enPassantTransition(initialState, linie, coloana);
        }

        return initialState;
    }
}