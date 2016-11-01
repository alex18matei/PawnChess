package chess;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.print("PawnChess\n hei\n");
        Chessboard chessboard = new Chessboard();
        chessboard.init();
        System.out.println(chessboard.toString());
    }
}
