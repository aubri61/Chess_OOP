package util;

import pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveLogger {

    /**
     * Wrapper class for a pair of moves.
     */
    private static class MoveRound {
        private Move whiteMove;
        private Move blackMove;

        public MoveRound(Move whiteMove, Move blackMove) {
            this.whiteMove = whiteMove;
            this.blackMove = blackMove;
        }

        public Move getMove(Piece.Color color) {
            if (color.equals(Piece.Color.WHITE)) {
                return whiteMove;
            } else {
                return blackMove;
            }
        }

    }

    private static List<MoveRound> moveHistory;
    private static List<Move> moveRoundBuffer;

    private static MoveLogger moveLoggerInstance = new MoveLogger();

    public static MoveLogger getInstance() {
        return moveLoggerInstance;
    }

    private MoveLogger() {
        initialize();
    }

    /**
     * Add a executeMove to the history
     * @param move
     */
    public static void addMove(Move move) {
        moveRoundBuffer.add(move);
        if (moveRoundBuffer.size() == 2) {
            moveHistory.add(new MoveRound(moveRoundBuffer.get(0), moveRoundBuffer.get(1)));
            moveRoundBuffer.clear();
        }
    }

    // public static void promotedMove(Piece oriPiece, Piece newPiece) {
    //     Piece.Color oriCol=oriPiece.getColor();
    //     Piece.Type newType=newPiece.getType();
    //     if (oriCol.equals(Piece.Color.BLACK)) {
    //         moveHistory.add(new RoundMove)
    //     }
    // }

    public static Move getPreviousMove(Move move) {
        if (move.getPiece().getColor().equals(Piece.Color.WHITE)) {
            if (moveHistory.size()!=0) {
                return moveHistory.get(moveHistory.size() -1 ).getMove(Piece.Color.BLACK);
            } else {return null;}
        } else {
            if (moveHistory.size()==0) {
                return moveRoundBuffer.get(0);
            } else {
                return moveHistory.get(moveHistory.size() -1).getMove(Piece.Color.WHITE);
            }
        }
    }

    public static Move getLastMove() {
        if (moveRoundBuffer.size()==1) {
            return moveRoundBuffer.get(0);
        } else {
            return moveHistory.get(moveHistory.size()-1).getMove(Piece.Color.BLACK);
        }
    }

    public static Move undoMove() {
        if (moveHistory.size()==0) { return null; }
        else {
            if (moveRoundBuffer.size()==0) {
                MoveRound currentRound = moveHistory.remove(moveHistory.size() - 1);
                moveRoundBuffer.add(currentRound.getMove(Piece.Color.WHITE));
                return currentRound.getMove(Piece.Color.BLACK);
            } else {
                return moveRoundBuffer.remove(0);
            }
        }
    }

    private void initialize() {
        moveHistory = new ArrayList<MoveRound>();
        moveRoundBuffer = new ArrayList<Move>();
    }
}
