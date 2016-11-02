package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Chessboard {

    private char[][] state;
    private boolean isComputerTurn = false;
    private int enPassantI, enPassantJ;
    private boolean wasInitialMove = false;

    public int getEnPassantI() {
        return enPassantI;
    }

    public void setEnPassantI(int enPassantI) {
        this.enPassantI = enPassantI;
    }

    public int getEnPassantJ() {
        return enPassantJ;
    }

    public void setEnPassantJ(int enPassantJ) {
        this.enPassantJ = enPassantJ;
    }

    public boolean isWasInitialMove() {
        return wasInitialMove;
    }

    public void setWasInitialMove(boolean wasInitialMove) {
        this.wasInitialMove = wasInitialMove;
    }

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

    public char[][] getState() {
        return state;
    }

    public void setState(char[][] state) {
        this.state = copy(state);
    }

    public boolean isComputerTurn() {
        return isComputerTurn;
    }

    public void setComputerTurn(boolean computerTurn) {
        isComputerTurn = computerTurn;
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
            //enPassantI = 5;
        } else {
            initialTrasition[1][currentJ] = '_';
            initialTrasition[3][currentJ] = 'B';
            //enPassantI = 2;
        }
        //enPassantJ = currentJ;
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

        if (isOutOfMatrix(currentI, currentJ) || isOutOfMatrix(currentI - 1) || isOutOfMatrix(currentI + 1))
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
        if (isOutOfMatrix(currentI, currentJ) || isOutOfMatrix(targetJ)
                || isOutOfMatrix(currentI - 1) || isOutOfMatrix(currentI + 1))
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
        if (isOutOfMatrix(currentI, currentJ)) {
            return false;
        }
        if (!wasInitialMove) {
            return false;
        }
        if (!isComputerTurn) {
            if (currentI != enPassantI + 1 && (currentJ != enPassantJ - 1 || currentJ != enPassantJ + 1)) {
                return false;
            }
            if (currentJ == 0) {
                if (currentState[currentI][currentJ] != 'W' || currentState[enPassantI + 1][enPassantJ + 1] != 'W') {
                    return false;
                }
            } else if ((currentJ == 7)) {
                if (currentState[currentI][currentJ] != 'W' || currentState[enPassantI + 1][enPassantJ - 1] != 'W') {
                    return false;
                }
            } else {
                //System.out.println("enPassantI " + enPassantI + "enPassantJ " + enPassantJ);
                if (currentState[currentI][currentJ] != 'W' || (currentState[enPassantI + 1][enPassantJ - 1] != 'W' &&
                        currentState[enPassantI + 1][enPassantJ + 1] != 'W')) {
                    return false;
                }
            }
        } else {
            if (currentI != enPassantI - 1 && (currentJ != enPassantJ - 1 || currentJ != enPassantJ + 1)) {
                return false;
            }
            if (currentJ == 0) {
                if (currentState[currentI][currentJ] != 'W' || currentState[enPassantI - 1][enPassantJ + 1] != 'W') {
                    return false;
                }
            } else if ((currentJ == 7)) {
                if (currentState[currentI][currentJ] != 'W' || currentState[enPassantI - 1][enPassantJ - 1] != 'W') {
                    return false;
                }
            } else {
                if (currentState[currentI][currentJ] != 'B' || (currentState[enPassantI - 1][enPassantJ + 1] != 'B' &&
                        currentState[enPassantI - 1][enPassantJ - 1] != 'B')) {
                    return false;
                }
            }
        }
        return true;
    }

    public void run2Players() {
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

    public void run() {
        char[][] initialState = init();
        System.out.println(this.toString(initialState));
        char[][] currentState = copy(initialState);// = getPlayerMove(initialState);

        while (!isFinal(initialState)) {
            if (!isComputerTurn) {
                System.out.println("Player 1 (White)");
                do {
                    currentState = getPlayerMove(initialState);
                    if (currentState == initialState) {
                        System.out.println("Mutarea nu este valida. Introdu din nou");
                    }
                } while (currentState == initialState);
            } else {
                System.out.println("Computer (Black)");
                currentState = minimax(initialState, 'B', 5);
            }
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

    public char[][] getPlayerMove(char[][] initialState) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Introdu coordonatele pionului:");
        System.out.println("Linie:");
        int linie = scan.nextInt();
        System.out.println("Coloana:");
        int coloana = scan.nextInt();


        int move = getMove(scan);
        //scan.close();
        if (move == 1) { //initialMove
            if (validateInitial(initialState, linie, coloana)) {

                char[][] tempState = initialTransition(initialState, coloana);
                wasInitialMove = true;
                if (!isComputerTurn) {
                    enPassantI = 5;
                } else {
                    enPassantI = 2;
                }
                enPassantJ = coloana;
                return tempState;
            }
        } else if (move == 2) {
            if (validateNormal(initialState, linie, coloana)) {
                wasInitialMove = false;
                return normalTransition(initialState, linie, coloana);
            }
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
            if (validateCapture(initialState, linie, coloana, targetJ)) {
                wasInitialMove = false;
                return captureTransition(initialState, linie, coloana, targetJ);
            }
        } else if (move == 4) {
            if (validateEnPassant(initialState, linie, coloana)) {
                wasInitialMove = false;
                return enPassantTransition(initialState, linie, coloana);
            }
        }

        return initialState;
    }

    public List<char[][]> getAllPosibleMoves(char[][] currentState, char player) {

        List<char[][]> posibleStates = new ArrayList<>();
        List<Pawn> pawns = getPawnList(currentState, player);
        for (Pawn pawn : pawns) {
            posibleStates.addAll(getAllMovesForPawn(currentState, pawn));
        }
        return posibleStates;
    }

    public List<char[][]> getAllMovesForPawn(char[][] currentState, Pawn pawn) {
        List<char[][]> posibleStates = new ArrayList<>();

        //System.out.println("Pawn[ " + pawn.getLine() + " ][ " + pawn.getCol() + " ]");
        if (validateEnPassant(currentState, pawn.getLine(), pawn.getCol())) {
            posibleStates.add(enPassantTransition(currentState, pawn.getLine(), pawn.getCol()));
        }
        if (validateInitial(currentState, pawn.getLine(), pawn.getCol()))
            posibleStates.add(initialTransition(currentState, pawn.getCol()));
        if (validateNormal(currentState, pawn.getLine(), pawn.getCol()))
            posibleStates.add(normalTransition(currentState, pawn.getLine(), pawn.getCol()));
        if (validateCapture(currentState, pawn.getLine(), pawn.getCol(), pawn.getCol() + 1))
            posibleStates.add(captureTransition(currentState, pawn.getLine(), pawn.getCol(), pawn.getCol() + 1));
        if (validateCapture(currentState, pawn.getLine(), pawn.getCol(), pawn.getCol() - 1))
            posibleStates.add(captureTransition(currentState, pawn.getLine(), pawn.getCol(), pawn.getCol() - 1));

        /*for (char[][] state : posibleStates) {
            System.out.println(toString(state));
        }*/
        return posibleStates;
    }

    public float evaluate(char[][] currentState, char player) {
        float hashScore = stateIsFinal(currentState);
        char oponent;
        if( player == 'W'){
            oponent = 'B';
        } else {
            oponent = 'W';
        }
        float captureScore = captureScore(currentState, player);
        float pawnScore = scoreOfPawns(currentState, player) - scoreOfPawns(currentState, oponent);
        float mobility = getAllPosibleMoves(currentState, player).size() - getAllPosibleMoves(currentState, oponent).size();
        float movesNo = movesNo(currentState, oponent) - movesNo(currentState, player);
        return pawnScore + 0.1f * mobility + 0.2f*movesNo + hashScore + captureScore;
    }

    private float captureScore(char[][] currentState, char player) {
        List<Pawn> pawns = getPawnList(currentState, player);
        for( Pawn pawn : pawns){
            if (validateCapture(currentState, pawn.getLine(), pawn.getCol(), pawn.getCol() + 1)
                    || (validateCapture(currentState, pawn.getLine(), pawn.getCol(), pawn.getCol() - 1)) ){
                return 10;
            }
        }
        return 0;
    }

    private float stateIsFinal(char[][] currentState) {
        if( isFinal(currentState)){
            return Float.POSITIVE_INFINITY;
        }
        return 0;
    }
    /*private float evaluate(char[][] currentState, char player) {
        return getAllPosibleMoves(currentState).size() + scoreOfPawns(currentState, player);
        //- 0.5 * blockedPieces(currentState);
    }*/

    private int scoreOfPawns(char[][] currentState, char player) {
        List<Pawn> pawns = getPawnList(currentState, player);
        return pawns.size();
    }

    private float movesNo(char[][] currentState, char player){
        float score = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (currentState[i][j] == player) {
                    if( player == 'W'){
                        score += i;
                    } else {
                        score += (7 - i);
                    }
                }
            }
        }
        return score;
    }

    private List<Pawn> getPawnList(char[][] currentState, char player) {
        List<Pawn> pawns = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (currentState[i][j] == player) {
                    Pawn pawn = new Pawn(i, j);
                    pawns.add(pawn);
                }
            }
        }
        return pawns;
    }

    public float min_play(char[][] currentState, int depth, char player) {
        if (isFinal(currentState)|| depth == 0) {
            return evaluate(currentState, player);
        }
        List<char[][]> moves = getAllPosibleMoves(currentState, player);
        float best_score = Float.POSITIVE_INFINITY;
        for (char[][] move : moves) {
            char[][] clone = copy(move);
            float score = max_play(clone, depth - 1, player);
            if (score < best_score) {
                //best_move = move
                best_score = score;
            }
        }
        return best_score;
    }

    private float max_play(char[][] currentState, int depth, char player) {
        if (isFinal(currentState) || depth == 0) {
            return evaluate(currentState, player);
        }
        List<char[][]> moves = getAllPosibleMoves(currentState, player);
        float best_score = Float.NEGATIVE_INFINITY;
        for (char[][] move : moves) {
            char[][] clone = copy(move);
            float score = min_play(clone, depth - 1, player);
            if (score > best_score) {
                //best_move = move
                best_score = score;
            }
        }
        return best_score;
    }

    public char[][] minimax(char[][] currentState, char player, int depth) {
        List<char[][]> moves = getAllPosibleMoves(currentState, player);
        char[][] bestMove = moves.get(0);
        float best_score = Float.NEGATIVE_INFINITY;
        for (char[][] move : moves) {
            char[][] clone = copy(move);
            float score = min_play(clone, depth, player);
            if (score > best_score) {
                bestMove = copy(move);
                best_score = score;
            }
        }
        //System.out.println(best_score);
        //System.out.println(toString(bestMove));
        return bestMove;
    }
}