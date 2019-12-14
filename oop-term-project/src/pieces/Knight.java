package pieces;

import util.Move;

public class Knight extends Piece {

    public Knight(Color color) {
        super(color);
        this.type = Type.KNIGHT;
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
            // along file
            if (absFileDiff==2 && absRankDiff==1 || absFileDiff==1 && absRankDiff==2) { return true;}
        }
        // all other cases
        return false;
    }

}
