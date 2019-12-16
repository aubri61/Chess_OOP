package pieces;

import board.Board;
import util.Move;
import util.MoveLogger;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color);
        this.type = Type.PAWN;
    }

    @Override
    public boolean validateMove(Move move) {

        // executeMove or capture
        char oriFile=move.getOriginFile();
        int oriRank=move.getOriginRank();
        char destFile=move.getDestinationFile();
        int destRank=move.getDestinationRank();

        int fileDiff=destFile-oriFile;
        int rankDiff=destRank-oriRank;

        int absFileDiff = Math.abs(fileDiff);
        int absRankDiff = Math.abs(rankDiff);

        switch ( move.getPiece().getColor() ) {
            case WHITE :
                if (fileDiff==0) {
                    if (Board.getSquare(destFile, destRank).getCurrentPiece()!=null
                            && Board.getSquare(destFile, destRank).getCurrentPiece().getColor().equals(Piece.Color.BLACK) ){
                        return false;
                    }else {
                        if (oriRank == 2) {
                            if ((rankDiff == 2) || (rankDiff == 1)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if (rankDiff == 1) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (MoveLogger.getLastMove().getPiece().getEnpassant()) {
                        System.out.println(MoveLogger.getLastMove().getDestinationFile()+" "+MoveLogger.getLastMove().getDestinationRank());

                        Move lastmove=MoveLogger.getLastMove();
                        if (move.getDestinationFile()==lastmove.getDestinationFile() && (move.getDestinationRank()==lastmove.getDestinationRank()+1)) {
                            return true;
                        }
                    }
                    if (Board.getSquare(destFile, destRank).getCurrentPiece()!=null
                        && Board.getSquare(destFile, destRank).getCurrentPiece().getColor().equals(Piece.Color.BLACK) ) {
                        if (absFileDiff==1 && rankDiff==1) { return true; }
                        else { return false; }
                    }
                    return false;
                }
    
            case BLACK:
                if (fileDiff==0) {
                    if (Board.getSquare(destFile, destRank).getCurrentPiece()!=null
                            && Board.getSquare(destFile, destRank).getCurrentPiece().getColor().equals(Piece.Color.WHITE) ){
                        return false;
                    }else {
                        if (oriRank == 7) {
                            if (rankDiff == -2 || rankDiff == -1) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if (rankDiff == -1) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                } else {
                    if (MoveLogger.getLastMove().getPiece().getEnpassant()) {
                        System.out.println(MoveLogger.getLastMove().getDestinationFile()+" "+MoveLogger.getLastMove().getDestinationRank());
                        Move lastmove=MoveLogger.getLastMove();
                        if (move.getDestinationFile()==lastmove.getDestinationFile() && (move.getDestinationRank()==lastmove.getDestinationRank()-1)) {
                            return true;
                        }
                    }
                    if (Board.getSquare(destFile, destRank).getCurrentPiece()!=null
                        && Board.getSquare(destFile, destRank).getCurrentPiece().getColor().equals(Piece.Color.WHITE) ) {
                        if (absFileDiff==1 && rankDiff==-1) { return true; }
                        else { return false; }
                    }
                    return false;
                }
        }
        return false;
    }
}