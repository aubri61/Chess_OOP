package pieces;

import util.Move;

public class Bishop extends Piece {

    public Bishop(Color color) {
        super(color);
        this.type = Type.BISHOP;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
        Piece.Type curType=move.getPiece().getType();

        char oriFile=move.getOriginFile();
        int oriRank=move.getOriginRank();
        char destFile=move.getDestinationFile();
        int destRank=move.getDestinationRank();

        int fileDiff=destFile-oriFile;
        int rankDiff=destRank-oriRank;

        int absFileDiff = Math.abs(fileDiff);
        int absRankDiff = Math.abs(rankDiff);



        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()))) {

            if(curType.equals(Type.BISHOP)){
                if(absFileDiff == absRankDiff){
                    return true;
                }else{
                    return false;
                }
            }
        }

        // all other cases
        return false;
    }
    }