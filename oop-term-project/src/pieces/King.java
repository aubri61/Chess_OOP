package pieces;

import util.Move;

public class King extends Piece {

    public King(Color color) {
        super(color);
        this.type = Type.KING;
    }

    @Override
    public boolean validateMove(Move move) {

        char oriFile=move.getOriginFile();
        int oriRank=move.getOriginRank();
        char destFile=move.getDestinationFile();
        int destRank=move.getDestinationRank();

        int fileDiff=destFile-oriFile;
        int rankDiff=destRank-oriRank;

        int absFileDiff = Math.abs(fileDiff);
        int absRankDiff = Math.abs(rankDiff);

        char oppoKingFile=PieceSet.getOpponentKingFile(move.getPiece().getColor());
        int oppoKingRank=PieceSet.getOpponentKingRank(move.getPiece().getColor());

        int absKingfd=Math.abs(oppoKingFile-oriFile);
        int absKingrd=Math.abs(oppoKingRank-oriRank);


        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                    && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()))) {
            // along file
            if ((absKingfd==2&&absKingrd==0) || (absKingfd==0&&absKingrd==2) || (absKingfd==2 && absKingrd==2)) {
                return false;
            }
            if (absFileDiff<=1 && absRankDiff <=1 ) {
                return true;
            }
        }
        // all other cases
        return false;
    }

}
