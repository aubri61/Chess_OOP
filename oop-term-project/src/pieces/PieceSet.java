package pieces;

import java.util.*;

import ui.BoardPanel;
import ui.MoveHistoryPanel;
import util.Move;
import util.MoveLogger;
import util.GameModel;
import board.Board;
import util.MoveValidator;


public class PieceSet {

    private static Map<Piece.Color, Map<Piece.Type, List<Piece>>> pieceSet = null;
    private static Map<Piece.Color, Stack<Piece>> capturedPieceSet;

    private static char whiteKingFile;
    private static int whiteKingRank;
    private static char blackKingFile;
    private static int blackKingRank;

    private static PieceSet pieceSetInstance = new PieceSet();

    public static PieceSet getInstance() {
        return pieceSetInstance;
    }

    private PieceSet() {
        initialize();
    }

    private static void initialize() {
        initializePieceSet();
        initializeCapturedPieceSet();
        initializeKingsCoordinates();
    }

    public static List<Piece> getPieces(Piece.Color color) {
        List<Piece> piecesSameColor = new ArrayList<Piece>();
        for (Map.Entry<Piece.Type, List<Piece>> piecesEntry : pieceSet.get(color).entrySet()) {
            for (Piece piece : piecesEntry.getValue()) {
                piecesSameColor.add(piece);
            }
        }
        return piecesSameColor;
    }

    public static List<Piece> getPieces(Piece.Color color, Piece.Type type) {
        return pieceSet.get(color).get(type);
    }

    public static void addCapturedPiece(Piece piece) {
        piece.setCapture(true);
        capturedPieceSet.get(piece.getColor()).push(piece);
    }

    public static List<Piece> getCapturedPieces(Piece.Color color) {
        return capturedPieceSet.get(color);
    }

    public static char getOpponentKingFile(Piece.Color color) {
        if (color.equals(Piece.Color.WHITE)) {
            return blackKingFile;
        } else {
            return whiteKingFile;
        }
    }

    public static int getOpponentKingRank(Piece.Color color) {
        if (color.equals(Piece.Color.WHITE)) {
            return blackKingRank;
        } else {
            return whiteKingRank;
        }
    }

    public static void setKingAgain(Piece.Color color, char file, int rank) {
        if (color.equals(Piece.Color.WHITE)) {
            whiteKingFile=file;
            whiteKingRank=rank;
        } else {
            blackKingFile=file;
            blackKingRank=rank;
        }
    }

    public static Piece promotion(Move move, String selected, BoardPanel boardPanel) {
        Piece pawnPiece=move.getPiece();
        PieceSet.getPieces(pawnPiece.getColor()).remove(pawnPiece);
        Piece newPiece;
        if (selected.equals("Queen")) {
            newPiece=new Queen(pawnPiece.getColor());
            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).setCurrentPiece(newPiece);
            PieceSet.getPieces(newPiece.getColor()).add(newPiece); 

        } else if (selected.equals("Rook")) {
            newPiece=new Rook(pawnPiece.getColor());
            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).setCurrentPiece(newPiece);
            PieceSet.getPieces(newPiece.getColor()).add(newPiece); 

        } else if (selected.equals("Bishop")) {
            newPiece=new Bishop(pawnPiece.getColor());
            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).setCurrentPiece(newPiece);
            PieceSet.getPieces(newPiece.getColor()).add(newPiece); 

        } else {
            newPiece=new Knight(pawnPiece.getColor());
            Board.getSquare(move.getDestinationFile(), move.getDestinationRank()).setCurrentPiece(newPiece);
            PieceSet.getPieces(newPiece.getColor()).add(newPiece); 
        } 

        boardPanel.changeImageLabel(newPiece, move);
        PieceSet.getPieces(pawnPiece.getColor()).add(newPiece);
        pawnPiece.setPromoted(true);
        System.out.println(MoveLogger.getLastMove().getPiece() +" "+ MoveLogger.getLastMove().getDestinationFile()+" "+MoveLogger.getLastMove().getDestinationRank());
        //MoveLogger.promotedMove(pawnPiece, newPiece);

       // System.out.println("pawn has changed into"+newPiece.getType());
        return newPiece;
    }

    public static void unPromoteUndo(Move move) {
        System.out.println("siq");
        Piece newPiece=new Pawn(move.getPiece().getColor());
        System.out.println("newPiece"+" "+newPiece.getType());

        Piece oriPiece=Board.getSquare(move.getOriginFile(), move.getOriginRank()).getCurrentPiece();
        System.out.println(oriPiece);
        System.out.println(move.getOriginFile()+" "+move.getOriginRank());
        Board.getSquare(move.getOriginFile(), move.getOriginRank()).setCurrentPiece(newPiece);
        PieceSet.getPieces(move.getPiece().getColor()).add(newPiece);
        PieceSet.getPieces(move.getPiece().getColor()).remove(oriPiece);
        newPiece.setPromoted(false);
    }

    private static void initializePieceSet() {
        /*
        TODO-piece
            Initialize pieces and put to pieceSet
            The structure of pieceSet is as following
                pieceSet - collection by color - collection by category - each pieces
            The following code should be good start, but you can fix if you want
         */
        pieceSet = new LinkedHashMap<Piece.Color, Map<Piece.Type, List<Piece>>>();

        Map<Piece.Type, List<Piece>> whitePieces = new LinkedHashMap<Piece.Type, List<Piece>>();
        Map<Piece.Type, List<Piece>> blackPieces = new LinkedHashMap<Piece.Type, List<Piece>>();

        //Rooks
        List<Piece> whiteRooks = new ArrayList<Piece>();
        List<Piece> blackRooks = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteRooks.add(new Rook(Piece.Color.WHITE));
            blackRooks.add(new Rook(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.ROOK, whiteRooks);
        blackPieces.put(Piece.Type.ROOK, blackRooks);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        //King
        List<Piece> whiteKing = new ArrayList<Piece>();
        List<Piece> blackKing = new ArrayList<Piece>();
        whiteKing.add(new King(Piece.Color.WHITE));
        blackKing.add(new King(Piece.Color.BLACK));

        whitePieces.put(Piece.Type.KING, whiteKing);
        blackPieces.put(Piece.Type.KING, blackKing);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        //Queen
        List<Piece> whiteQueen = new ArrayList<Piece>();
        List<Piece> blackQueen = new ArrayList<Piece>();
        whiteQueen.add(new Queen(Piece.Color.WHITE));
        blackQueen.add(new Queen(Piece.Color.BLACK));

        whitePieces.put(Piece.Type.QUEEN, whiteQueen);
        blackPieces.put(Piece.Type.QUEEN, blackQueen);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        //Bishops
        List<Piece> whiteBishops = new ArrayList<Piece>();
        List<Piece> blackBishops = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteBishops.add(new Bishop(Piece.Color.WHITE));
            blackBishops.add(new Bishop(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.BISHOP, whiteBishops);
        blackPieces.put(Piece.Type.BISHOP, blackBishops);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        //Knight
        List<Piece> whiteKnights = new ArrayList<Piece>();
        List<Piece> blackKnights = new ArrayList<Piece>();
        for (int i = 0; i < 2; i++) {
            whiteKnights.add(new Knight(Piece.Color.WHITE));
            blackKnights.add(new Knight(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.KNIGHT, whiteKnights);
        blackPieces.put(Piece.Type.KNIGHT, blackKnights);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);

        //Pawn
        List<Piece> whitePawns = new ArrayList<Piece>();
        List<Piece> blackPawns = new ArrayList<Piece>();
        for (int i = 0; i < 8; i++) {
            whitePawns.add(new Pawn(Piece.Color.WHITE));
            blackPawns.add(new Pawn(Piece.Color.BLACK));
        }
        whitePieces.put(Piece.Type.PAWN, whitePawns);
        blackPieces.put(Piece.Type.PAWN, blackPawns);

        pieceSet.put(Piece.Color.WHITE, whitePieces);
        pieceSet.put(Piece.Color.BLACK, blackPieces);


    }

    private static void initializeCapturedPieceSet() {
        capturedPieceSet = new LinkedHashMap<Piece.Color, Stack<Piece>>();
        Stack<Piece> whiteCapturedPieces = new Stack<Piece>();
        Stack<Piece> blackCapturedPieces = new Stack<Piece>();
        capturedPieceSet.put(Piece.Color.WHITE, whiteCapturedPieces);
        capturedPieceSet.put(Piece.Color.BLACK, blackCapturedPieces);
    }

    private static void initializeKingsCoordinates() {
        whiteKingFile = blackKingFile = 'e';
        whiteKingRank = 1;
        blackKingRank = 8;
    }

}
