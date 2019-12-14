package board;

import pieces.*;
import util.Core;
import util.Move;

import java.util.Iterator;

public class Board {

    public static final int DIMENSION = 8;

    private static Square[][] grid = new Square[DIMENSION][DIMENSION];

    private static Board boardInstance = new Board();

    public static Board getInstance() {
        return boardInstance;
    }

    private Board() {
        initialize();
    }

    public static void initialize() {
        initializeSquares();
        initializePieces();
    }

    public static Square getSquare(char file, int rank) {
        file = Character.toLowerCase(file);
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8) {
            return null;
        } else {
            return grid[file - 'a'][rank - 1];
        }
    }

    public static void executeMove(Move move) {
        Square originSquare = getSquare(move.getOriginFile(), move.getOriginRank());
        Square destinationSquare = getSquare(move.getDestinationFile(), move.getDestinationRank());
        if (destinationSquare.getCurrentPiece() != null) {
            destinationSquare.getCurrentPiece().setCapture(true);
            PieceSet.addCapturedPiece(destinationSquare.getCurrentPiece());
        }
        destinationSquare.setCurrentPiece(originSquare.getCurrentPiece());
        originSquare.setCurrentPiece(null);
    }

    private static void initializeSquares() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                grid[i][j] = new Square();
            }
        }
    }

    private static void initializePieces() {
        /*
        TODO-piece 
         */
        // rooks
        Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.ROOK).iterator();
        Iterator<Piece> blackRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
        getSquare('a', 1).setCurrentPiece(whiteRooksIterator.next());
        getSquare('h', 1).setCurrentPiece(whiteRooksIterator.next());
        getSquare('a', 8).setCurrentPiece(blackRooksIterator.next());
        getSquare('h', 8).setCurrentPiece(blackRooksIterator.next());


        //King
        Iterator<Piece> whiteKingIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KING).iterator();
        Iterator<Piece> blackKingIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KING).iterator();
        getSquare('e', 1).setCurrentPiece(whiteKingIterator.next());
        getSquare('e', 8).setCurrentPiece(blackKingIterator.next());


        //Queen
        Iterator<Piece> whiteQueenIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.QUEEN).iterator();
        Iterator<Piece> blackQueenIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.QUEEN).iterator();
        getSquare('d', 1).setCurrentPiece(whiteQueenIterator.next());
        getSquare('d', 8).setCurrentPiece(blackQueenIterator.next());


        //bishop
        Iterator<Piece> whiteBishopIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.BISHOP).iterator();
        Iterator<Piece> blackBishopIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.BISHOP).iterator();
        getSquare('c', 1).setCurrentPiece(whiteBishopIterator.next());
        getSquare('f', 1).setCurrentPiece(whiteBishopIterator.next());
        getSquare('c', 8).setCurrentPiece(blackBishopIterator.next());
        getSquare('f', 8).setCurrentPiece(blackBishopIterator.next());

        //Knight
        Iterator<Piece> whiteKnightIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KNIGHT).iterator();
        Iterator<Piece> blackKnightIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KNIGHT).iterator();
        getSquare('b', 1).setCurrentPiece(whiteKnightIterator.next());
        getSquare('g', 1).setCurrentPiece(whiteKnightIterator.next());
        getSquare('b', 8).setCurrentPiece(blackKnightIterator.next());
        getSquare('g', 8).setCurrentPiece(blackKnightIterator.next());

        //Pawn
        Iterator<Piece> whitePawnIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.PAWN).iterator();
        Iterator<Piece> blackPawnIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.PAWN).iterator();
        getSquare('a', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('b', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('c', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('d', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('e', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('f', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('g', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('h', 2).setCurrentPiece(whitePawnIterator.next());
        getSquare('a', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('b', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('c', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('d', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('e', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('f', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('g', 7).setCurrentPiece(blackPawnIterator.next());
        getSquare('h', 7).setCurrentPiece(blackPawnIterator.next());

    }
}
