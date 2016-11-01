package chess;

import java.util.Arrays;

public class Chessboard {

    private char[][] state;
    private boolean isComputerTurn = false;
    private int enPassantI, enPassantJ;
    private boolean wasInitialMove = false;

    public char[][] init() {
        state = new char[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if( i == 1){
                    state[i][j] = 'B';
                } else if( i == 6){
                    state[i][j] = 'W';
                }else{
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
            for (int j = 0; j < 8; ++j) {
                sb.append(state[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean isFinal(char[][] state){
        for( int j = 0; j< 8;++j){
            if( state[0][j] == 'W'){
                return true;
            }
        }
        for( int j = 0; j< 8;++j){
            if( state[7][j] == 'B'){
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

    public char[][] initialTransition(char[][] currentState, int currentJ){
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if( !isComputerTurn ){
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

    public char[][] normalTransition(char[][] currentState,int currentI, int currentJ){
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if( !isComputerTurn ){
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI - 1][currentJ] = 'W';
        } else {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI + 1][currentJ] = 'B';
        }
        wasInitialMove = false;
        return initialTrasition;
    }

    public char[][] captureTransition(char[][] currentState,int currentI, int currentJ,int targetJ){
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if( !isComputerTurn ){
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI - 1][targetJ] = 'W';
        } else {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[currentI + 1][targetJ] = 'B';
        }
        wasInitialMove = false;
        return initialTrasition;
    }

    public char[][] enPassantTransition(char[][] currentState,int currentI, int currentJ){
        char[][] initialTrasition;
        initialTrasition = copy(currentState);
        if( !isComputerTurn ){
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[enPassantI][enPassantJ] = 'W';
            initialTrasition[enPassantI - 1][enPassantJ] = '_';
        } else {
            initialTrasition[currentI][currentJ] = '_';
            initialTrasition[enPassantI][enPassantJ] = 'B';
            initialTrasition[enPassantI + 1][enPassantJ] = '_';
        }
        return initialTrasition;
    }
}