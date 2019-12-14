package pieces;

import util.Move;

public class Queen extends Piece {

    public Queen(Color color) {
        super(color);
        this.type = Type.QUEEN;
    }

    @Override
    public boolean validateMove(Move move) {

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
            //Queen movement
            if(curType.equals(Piece.Type.QUEEN)){
                if((destRank == oriRank) || (destFile == oriFile) ||
                        (absFileDiff == absRankDiff)){
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