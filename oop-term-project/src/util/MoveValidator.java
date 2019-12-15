package util;

import board.Board;
import pieces.Piece;
import pieces.PieceSet;
import pieces.Piece.Color;

import java.util.List;

public class MoveValidator {

    private static MoveValidator ourInstance = new MoveValidator();

    public static MoveValidator getInstance() {
        return ourInstance;
    }

    private MoveValidator() {
        currentMoveColor = Piece.Color.WHITE;
    }

    private static Piece.Color currentMoveColor;

    public static boolean validateMove(Move move) {
        return validateMove(move, false);
    }

    public static boolean validateMove(Move move, boolean ignoreColorCheck) {
        // check for out of bounds
        if (move.getDestinationFile() < 'a' || move.getDestinationFile() > 'h'
                || move.getDestinationRank() < 1 || move.getDestinationRank() > 8) {
            return false;
        }

        // check for valid origin
        if (move.getPiece() == null) {
            return false;
        }

        // check for valid color
        if (!move.getPiece().getColor().equals(currentMoveColor) && !ignoreColorCheck) {
            return false;
        }

        // check for valid destination
        if (move.getCapturedPiece() != null) {
            if (move.getPiece().getColor().equals(move.getCapturedPiece().getColor())) {
                return false;
            }
        }

        // check for piece rule
        if (!move.getPiece().validateMove(move)) {
            return false;
        }

        // check for clear path
        if (!validateClearPath(move)) {         
            return false;
        }

        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        return true;
    }


    public static boolean isCheckMove(Move move) {
        // TODO-check
        // create new move and check if this move can catches the opponent king next time.
        //  public Move(Piece piece, char originFile, int originRank, char destinationFile, int destinationRank) 
        Move temp=new Move(move.getPiece(), move.getDestinationFile(), move.getDestinationRank(), 
                            PieceSet.getOpponentKingFile(move.getPiece().getColor()), PieceSet.getOpponentKingRank(move.getPiece().getColor()));
        return validateMove(temp, true);
    }

    public static boolean isCheckMate(Move move) {
        // TODO-check
        // if king can move (one square), ischeckmate false, if not, true.
        Piece opponentKing=Board.getSquare(PieceSet.getOpponentKingFile(move.getPiece().getColor()), 
                                           PieceSet.getOpponentKingRank(move.getPiece().getColor())).getCurrentPiece();

        int[] dir1={0, 0, 1, 1, -1, -1, -1, 1};
        int[] dir2={1, -1, 0, -1, 1, 0, -1, 1};
        boolean kingcatched=true;
        boolean otherCatch=false;

        for (int i=0; i<dir1.length; ++i) {
            kingcatched&=kingAvailMove(move, opponentKing, dir1[i], dir2[i]);
        }

        for (char i='a'; i<'i'; i++) {
            for (int j=1; j<9; j++) {
                Piece curPiece=Board.getSquare(i, j).getCurrentPiece();
                if (curPiece!=null && curPiece.getColor().equals( opponentKing.getColor() )) {
                    if (curPiece.getType().equals(Piece.Type.KING)) {continue;}
                    otherCatch=validateMove(new Move(curPiece, move.getPiece(), i, j, move.getOriginFile(), move.getOriginRank()));
                }
                if (otherCatch==true) { break;}
            }
            if (otherCatch==true) {break;}
        }

        return kingcatched&&otherCatch;
    }

    public static boolean kingAvailMove(Move move, Piece opponentKing, int filedir, int rankdir) {
        Piece.Color moveColor=move.getPiece().getColor();
        //List<Piece> currPieces=PieceSet.getPieces(moveColor);
        char kingOriFile=PieceSet.getOpponentKingFile(moveColor);
        int kingOriRank=PieceSet.getOpponentKingRank(moveColor);
        boolean kingcatched=false;

        System.out.println("ss");
        if ((char)(kingOriFile+filedir)>'h' ||  (char)(kingOriFile+filedir)<'a' || (int)(kingOriRank+rankdir)>8 ||(int)(kingOriRank+rankdir)<1 ) {
            System.out.println("nono outof bound");
            return true;
        }
        Move kingMove=new Move( opponentKing, kingOriFile, kingOriRank, (char)(kingOriFile+filedir), (int)(kingOriRank+rankdir));
        System.out.println("kk");
        if (validateMove(kingMove)) {
            for (char i='a'; i<'i'; i++) {
                for (int j=1; j<9; j++) {
                    Piece curPiece=Board.getSquare(i, j).getCurrentPiece();
                    if (curPiece!=null && curPiece.getColor().equals(moveColor)) {
                        kingcatched=validateMove(new Move(curPiece, opponentKing, i, j, (char)(kingOriFile+filedir), kingOriRank+rankdir));
                    }
                    if (kingcatched==true) { break;}
                }
                if (kingcatched==true) {break;}
            }
        }
        return kingcatched;
    }

    private static boolean validateClearPath(Move move) {
        // TODO-movement

        int fileDirection = Integer.signum(move.getDestinationFile() - move.getOriginFile());
        int rankDirection = Integer.signum(move.getDestinationRank() - move.getOriginRank());

        // one square executeMove
        if (Math.abs(move.getDestinationFile() - move.getOriginFile()) <= 1
            && Math.abs(move.getDestinationRank() - move.getOriginRank()) <= 1) {
            return true;
        }

        // l-executeMove
        if ((Math.abs(move.getDestinationFile() - move.getOriginFile()) == 1
                && Math.abs(move.getDestinationRank() - move.getOriginRank()) == 2)
            || (Math.abs(move.getDestinationFile() - move.getOriginFile()) == 2
                && Math.abs(move.getDestinationRank() - move.getOriginRank()) == 1)) {
            return true;
        }

        // diagonal executeMove
        if (Math.abs(move.getDestinationFile() - move.getOriginFile())
                == Math.abs(move.getDestinationRank() - move.getOriginRank())) {
            for (int file = move.getOriginFile() + fileDirection, rank = move.getOriginRank() + rankDirection;
                 file != move.getDestinationFile() && rank != move.getDestinationRank();
                 file += fileDirection, rank += rankDirection) {
                if (Board.getSquare((char) file, rank).getCurrentPiece() != null) {
                    return false;
                }
            }
            return true;
        }

        // along file (different rank)
        if (move.getDestinationFile() - move.getOriginFile() == 0
                && move.getDestinationRank() - move.getOriginRank() != 0) {
            for (int rank = move.getOriginRank() + rankDirection; rank != move.getDestinationRank(); rank += rankDirection) {
                if (Board.getSquare(move.getOriginFile(), rank).getCurrentPiece() != null) {
                    return false;
                }
            }
            return true;
        }

        // along rank (different file)
        if (move.getDestinationFile() - move.getOriginFile() != 0
                && move.getDestinationRank() - move.getOriginRank() == 0) {
            for (char file = (char) (move.getOriginFile() + fileDirection); file != move.getDestinationFile(); file += fileDirection) {
                if (Board.getSquare(file, move.getOriginRank()).getCurrentPiece() != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
