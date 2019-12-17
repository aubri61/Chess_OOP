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

    public static Piece.Color getCurrentMoveColor() {
        return currentMoveColor;
    }

    public static void setCurrentMoveColor(Piece.Color color) {
        currentMoveColor=color;
    }

    private static Piece.Color currentMoveColor;

    public static boolean validateMove(Move move) {
        return validateMove(move, false);
    }

    public static boolean validateUndo(Move move) {
        Piece curPiece=move.getPiece();
        if (currentMoveColor.equals(curPiece.getColor())) {
            return false;
        }
        currentMoveColor = currentMoveColor.equals(Piece.Color.WHITE) ? Piece.Color.BLACK : Piece.Color.WHITE;
        return true;
    }

    public static boolean validateMove(Move move, Piece.Color color, boolean ignoreColorCheck) {
        currentMoveColor=color;
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
        boolean originCheck=false;
        boolean anotherCheck=false;
        Move temp=new Move(move.getPiece(), move.getDestinationFile(), move.getDestinationRank(), 
                            PieceSet.getOpponentKingFile(move.getPiece().getColor()), PieceSet.getOpponentKingRank(move.getPiece().getColor()));
        originCheck=validateMove(temp, true);
        

        for (char i='a'; i<'i'; i++) {
            for (int j=1; j<9; j++) {
                Piece curPiece=Board.getSquare(i, j).getCurrentPiece();
                if (curPiece!=null && curPiece.getColor().equals( move.getPiece().getColor() )) {
                    Move temp2=new Move(curPiece, i, j, PieceSet.getOpponentKingFile(curPiece.getColor()), PieceSet.getOpponentKingRank(curPiece.getColor()));
                    anotherCheck=anotherCheck||validateMove(temp2, true);
                }
              //  if (anotherCheck) {break;}
            }
          //  if (anotherCheck) {break;}
        }
        
        return originCheck||anotherCheck;

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
        boolean kingSelfCatch=false;

        Move kingSelfMove=new Move(opponentKing, move.getPiece(), PieceSet.getOpponentKingFile(move.getPiece().getColor()), 
                                    PieceSet.getOpponentKingRank(move.getPiece().getColor()),move.getDestinationFile(), move.getDestinationRank());
        if (validateMove(kingSelfMove,false)) {
            kingSelfCatch=true;
        }
        System.out.println("kingselfcatch  :  "+kingSelfCatch);

        //kingcatched - true if checkmate.
        for (int i=0; i<dir1.length; ++i) {
            kingcatched=kingcatched&&kingAvailMove(move, opponentKing, dir1[i], dir2[i]);
        }
        System.out.println("kingcatched  :  "+kingcatched);
     

      //othercatch - true if not checkmate.
        for (char i='a'; i<'i'; i++) {
            for (int j=1; j<9; j++) {
                Piece curPiece=Board.getSquare(i, j).getCurrentPiece();
                System.out.println("otherCatch  " +i+" "+j);
                if (curPiece!=null && curPiece.getColor().equals( opponentKing.getColor() )) {
                    if (curPiece.getType().equals(Piece.Type.KING)) {continue;}
                    otherCatch=otherCatch||validateMove(new Move(curPiece, i, j, move.getDestinationFile(), move.getDestinationRank()));
                    System.out.println(curPiece.getType());
                   
                    System.out.println(otherCatch);
                }
        //        if (otherCatch==true) { break;}
            }
        //    if (otherCatch==true) {break;}
        }
        System.out.println("otherCatch= " +otherCatch);


        return kingcatched&&!otherCatch&&!kingSelfCatch;
    }

    public static boolean isRealCheckMate(Move move) {
        boolean ischeckmate=isCheckMate(move);
        boolean anotherCheck=false;
        boolean anotherCheckmateCheck=false;

        Piece opponentKing=Board.getSquare(PieceSet.getOpponentKingFile(move.getPiece().getColor()), 
                                            PieceSet.getOpponentKingRank(move.getPiece().getColor())).getCurrentPiece();
        for (char i='a'; i<'i'; i++) {
            for (int j=1; j<9; j++) {
                Piece curPiece=Board.getSquare(i, j).getCurrentPiece();
                System.out.println("isRealCheckMate "+i+" "+j);
                if (curPiece!=null && curPiece.getColor().equals( move.getPiece().getColor() )) {
                    Move newMove=new Move(curPiece, i, j, PieceSet.getOpponentKingFile(move.getPiece().getColor()), 
                                            PieceSet.getOpponentKingRank(move.getPiece().getColor()));
                    anotherCheck=validateMove(newMove, false);
                    if (anotherCheck==true) { 
                        anotherCheckmateCheck=anotherCheckmateCheck||isCheckMate(newMove);
                       // System.out.println(anotherCheckmateCheck);
                    } 
                }
                 System.out.println(anotherCheckmateCheck);

                
            }
            //if (anotherCheckmateCheck) {break;}
        }
        System.out.println("anotheCC"+anotherCheckmateCheck);
        return ischeckmate||anotherCheckmateCheck;
    }

    public static boolean kingAvailMove(Move move, Piece opponentKing, int filedir, int rankdir) {
        Piece.Color moveColor=move.getPiece().getColor();
        char kingOriFile=PieceSet.getOpponentKingFile(moveColor);
        int kingOriRank=PieceSet.getOpponentKingRank(moveColor);
        boolean kingcatched=true;
        boolean tempkingcatched=false;

        if ((char)(kingOriFile+filedir)>'h' ||  (char)(kingOriFile+filedir)<'a' || (int)(kingOriRank+rankdir)>8 ||(int)(kingOriRank+rankdir)<1 ) {
            return true;
        }
        Move kingMove=new Move( opponentKing, kingOriFile, kingOriRank, (char)(kingOriFile+filedir), (int)(kingOriRank+rankdir));
        if (validateMove(kingMove, false)) {  
            for (char i='a'; i<'i'; i++) {
                for (int j=1; j<9; j++) {
                    Piece curPiece=Board.getSquare(i, j).getCurrentPiece();
                    System.out.println("kingavailmove  "+i+" "+j);

                    if (curPiece!=null && curPiece.getColor().equals(moveColor)) {
                        tempkingcatched=validateMove(new Move(curPiece, i, j, (char)(kingOriFile+filedir), kingOriRank+rankdir));
                    }
                  
                    kingcatched=kingcatched||tempkingcatched;
                    System.out.println("kingcatched   "+ kingcatched);
                }
              //  kingcatched=kingcatched||tempkingcatched;
            }
        }
        return kingcatched;
    }

    public static boolean canPromote(Move move) {
        Piece curPiece=move.getPiece();
        if (curPiece.getType().equals(Piece.Type.PAWN)) {
            if (curPiece.getColor().equals(Piece.Color.WHITE)) {
                if (move.getDestinationRank()==8) {
                    return true;
                }
            } else {
                if (move.getDestinationRank()==1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean enPassant(Move move) {
        Piece curPiece=move.getPiece();
        char oriFile=move.getOriginFile();
        int oriRank=move.getOriginRank();
        char destFile=move.getDestinationFile();
        int destRank=move.getDestinationRank();
        Piece oppoPiece1;
        Piece oppoPiece2;

        if ((char)(oriFile+1)>'h'||(char)(oriFile+1)<'a') {
            oppoPiece1=null;
        } else {
            oppoPiece1=Board.getSquare((char)(oriFile+1), destRank).getCurrentPiece();
        }
        if ((char)(oriFile-1)>'h'||(char)(oriFile-1)<'a') {
            oppoPiece2=null;
        } else {
            oppoPiece2=Board.getSquare((char)(oriFile-1), destRank).getCurrentPiece();
        }

        if (curPiece.getType().equals(Piece.Type.PAWN)) {
            if (curPiece.getColor().equals(Piece.Color.WHITE)) {
                if (destRank==4 && oriRank==2 && ( (oppoPiece1!=null && oppoPiece1.getType().equals(Piece.Type.PAWN) && oppoPiece1.getColor().equals(Piece.Color.BLACK))|| 
                                                    (oppoPiece2!=null && oppoPiece2.getType().equals(Piece.Type.PAWN) && oppoPiece2.getColor().equals(Piece.Color.BLACK))) )  {
                    return true;
                }
            } else {
                if (destRank==5 && oriRank==7 && ( (oppoPiece1!=null && oppoPiece1.getType().equals(Piece.Type.PAWN) && oppoPiece1.getColor().equals(Piece.Color.WHITE)) || 
                                                    (oppoPiece2!=null && oppoPiece2.getType().equals(Piece.Type.PAWN) && oppoPiece2.getColor().equals(Piece.Color.WHITE))) ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String[] getEnPassantPieces(Move move) {
        String[] result=new String[2];
        Piece curPiece=move.getPiece();
        char oriFile=move.getOriginFile();
        int oriRank=move.getOriginRank();
        char destFile=move.getDestinationFile();
        int destRank=move.getDestinationRank();
        String str;


        if ((char)(oriFile+1)>'h'||(char)(oriFile+1)<'a') {
            result[0]=null;
        } else {
            str=(char)(oriFile+1)+Integer.toString(destRank);
            result[0]=str;
        }
        if ((char)(oriFile-1)>'h'||(char)(oriFile-1)<'a') {
            result[1]=null;
        } else {
            str=(char)(oriFile-1)+Integer.toString(destRank);
            result[1]=str;
        }
        if (Board.getSquare((char)(oriFile+1), destRank)==null) {result[0]=null;}
        if (Board.getSquare((char)(oriFile-1), destRank)==null) {result[1]=null;}

        System.out.println("on movevalidator");
        System.out.println(result[0]+result[1]);

        return result;
    }

    public static boolean isKingCatched(Move move) {
        if (move.getCapturedPiece().getType().equals(Piece.Type.KING) && !move.getCapturedPiece().getColor().equals(move.getPiece().getColor())) {
            return true;
        } 
        return false;
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
