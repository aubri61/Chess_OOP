package ui;

import pieces.Pawn;
import pieces.Piece;
import pieces.PieceSet;
import util.Core;
import util.GameModel;
import util.Move;
import util.MoveLogger;

import javax.swing.*;

import board.Board;

import java.awt.*;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class BoardPanel extends JPanel implements Observer {

    public static final int SQUARE_DIMENSION = 100;

    private GameModel gameModel;
    private boolean boardReversed;
    private boolean usingCustomPieces;
    private JLayeredPane boardLayeredPane;
    private JPanel boardPanel;
    private JPanel[][] squarePanels;

    public BoardPanel(GameModel gameModel) {
        super(new BorderLayout());
        this.gameModel = gameModel;
        this.boardReversed = Core.getPreferences().isBoardReversed();
        this.usingCustomPieces = Core.getPreferences().isUsingCustomPieces();
        initializeBoardLayeredPane();
        initializeSquares();
        initializePieces();
        gameModel.addObserver(this);
    }

    public void submitMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        try {
            if (getSquarePanel(originFile, originRank)==null) {
                throw new Exception();
            }
            if (getSquarePanel(originFile, originRank).getComponent(0) != null ) {
                getSquarePanel(originFile, originRank).getComponent(0).setVisible(true);
                gameModel.onMoveRequest(originFile, originRank, destinationFile, destinationRank);
            }
        } catch (Exception e) {
            System.out.println("kkkkk");
        }
        
    }

    public void executeMove(Move move) {
        JPanel originSquarePanel = getSquarePanel(move.getOriginFile(), move.getOriginRank());
        JPanel destinationSquarePanel = getSquarePanel(move.getDestinationFile(), move.getDestinationRank());
        destinationSquarePanel.removeAll();
        destinationSquarePanel.add(originSquarePanel.getComponent(0));
        destinationSquarePanel.repaint();
        originSquarePanel.removeAll();
        originSquarePanel.repaint();
    }

    public void executeUndo(Move move) {
        JPanel originSquarePanel = getSquarePanel(move.getOriginFile(), move.getOriginRank());
        JPanel destinationSquarePanel = getSquarePanel(move.getDestinationFile(), move.getDestinationRank());
        originSquarePanel.add(destinationSquarePanel.getComponent(0));
        destinationSquarePanel.removeAll();
        if (move.getCapturedPiece() != null) {
            destinationSquarePanel.add(getPieceImageLabel(move.getCapturedPiece()));
        }
        if (MoveLogger.getPreviousMove(move) !=null && MoveLogger.getPreviousMove(move).getPiece().getEnpassant()) {
            System.out.println("yy");
            getSquarePanel(MoveLogger.getPreviousMove(move).getDestinationFile(), MoveLogger.getPreviousMove(move).getDestinationRank()).add(getPieceImageLabel(MoveLogger.getPreviousMove(move).getPiece()));
        }

        if (move.getPiece() !=null && move.getPiece().getPromoted() ) {
            System.out.println(move.getPiece().getType());
            changeImageLabelUndo(move.getPiece(), move);
            PieceSet.unPromoteUndo(move);
            System.out.println(move.getPiece().getType());
        }
        // if (MoveLogger.getLastMove() !=null && Board.getSquare(MoveLogger.getLastMove().getDestinationFile(), MoveLogger.getLastMove().getDestinationRank()).getCurrentPiece().getPromoted() ) {
        //     Piece curPiece=Board.getSquare(MoveLogger.getLastMove().getDestinationFile(), MoveLogger.getLastMove().getDestinationRank()).getCurrentPiece();
        //     changeImageLabelUndo(curPiece, MoveLogger.getLastMove());
        //     curPiece.setPromoted(false);
        //     System.out.println(curPiece.getPromoted());
        // }
        originSquarePanel.repaint();
        destinationSquarePanel.repaint();
    }

    public void preDrag(char originFile, int originRank, int dragX, int dragY) {
        Piece originPiece = gameModel.queryPiece(originFile, originRank);
        if (originPiece != null) {
            getSquarePanel(originFile, originRank).getComponent(0).setVisible(false);
            JLabel draggedPieceImageLabel = getPieceImageLabel(originPiece);
            draggedPieceImageLabel.setLocation(dragX, dragY);
            draggedPieceImageLabel.setSize(SQUARE_DIMENSION, SQUARE_DIMENSION);
            boardLayeredPane.add(draggedPieceImageLabel, JLayeredPane.DRAG_LAYER);
        }
    }

    public void executeDrag(int dragX, int dragY) {
        try {
            if (boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER).length==0) {
                throw new Exception();
            }
            JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];
            if (draggedPieceImageLabel != null) {
                draggedPieceImageLabel.setLocation(dragX, dragY);
            }
        } catch (Exception e) {
            System.out.println("ss");
        }
        
    }

    public void postDrag() {
        try {
            if (boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER).length==0) {
                throw new Exception();
            }
            JLabel draggedPieceImageLabel = (JLabel) boardLayeredPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)[0];
            boardLayeredPane.remove(draggedPieceImageLabel);
            boardLayeredPane.repaint();
        } catch (Exception e) {
            System.out.println("aa");
        }
        
    }

    public JPanel getSquarePanel(char file, int rank) {
        file = Character.toLowerCase(file);
        if (file < 'a' || file > 'h' || rank < 1 || rank > 8) {
            return null;
        } else {
            return squarePanels[file - 'a'][rank - 1];
        }
    }

    private void initializeSquares() {
        squarePanels = new JPanel[8][8];
        if (boardReversed) {
            for (int r = 0; r < 8; r ++) {
                for (int f = 7; f >= 0; f--) {
                    initializeSingleSquarePanel(f, r);
                }
            }
        } else {
            for (int r = 7; r >= 0; r --) {
                for (int f = 0; f < 8; f++) {
                    initializeSingleSquarePanel(f, r);
                }
            }
        }
    }

    private void initializeSingleSquarePanel(int f, int r) {
        squarePanels[f][r] = new JPanel(new GridLayout(1, 1));
        squarePanels[f][r].setPreferredSize(new Dimension(SQUARE_DIMENSION, SQUARE_DIMENSION));
        squarePanels[f][r].setSize(new Dimension(SQUARE_DIMENSION, SQUARE_DIMENSION));
        squarePanels[f][r].setBackground(f % 2 == r % 2 ? Color.GRAY : Color.WHITE);
        boardPanel.add(squarePanels[f][r]);
    }

    private void initializePieces() {
        /*
        TODO-piece
            Initialize pieces on board
            Check following code to implement other pieces
            Highly recommended to use same template!
         */
        // rooks
        Iterator<Piece> whiteRooksIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.ROOK).iterator();
        Iterator<Piece> blackRooksIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.ROOK).iterator();
        getSquarePanel('a', 1).add(getPieceImageLabel(whiteRooksIterator.next()));
        getSquarePanel('h', 1).add(getPieceImageLabel(whiteRooksIterator.next()));
        getSquarePanel('a', 8).add(getPieceImageLabel(blackRooksIterator.next()));
        getSquarePanel('h', 8).add(getPieceImageLabel(blackRooksIterator.next()));

         //King
         Iterator<Piece> whiteKingIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KING).iterator();
         Iterator<Piece> blackKingIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KING).iterator();
         getSquarePanel('e', 1).add(getPieceImageLabel(whiteKingIterator.next()));
         getSquarePanel('e', 8).add(getPieceImageLabel(blackKingIterator.next()));
 
 
         //Queen
         Iterator<Piece> whiteQueenIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.QUEEN).iterator();
         Iterator<Piece> blackQueenIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.QUEEN).iterator();
         getSquarePanel('d', 1).add(getPieceImageLabel(whiteQueenIterator.next()));
         getSquarePanel('d', 8).add(getPieceImageLabel(blackQueenIterator.next()));
 
 
         //bishop
         Iterator<Piece> whiteBishopIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.BISHOP).iterator();
         Iterator<Piece> blackBishopIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.BISHOP).iterator();
         getSquarePanel('c', 1).add(getPieceImageLabel(whiteBishopIterator.next()));
         getSquarePanel('f', 1).add(getPieceImageLabel(whiteBishopIterator.next()));
         getSquarePanel('c', 8).add(getPieceImageLabel(blackBishopIterator.next()));
         getSquarePanel('f', 8).add(getPieceImageLabel(blackBishopIterator.next()));
 
         //Knight
         Iterator<Piece> whiteKnightIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.KNIGHT).iterator();
         Iterator<Piece> blackKnightIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.KNIGHT).iterator();
         getSquarePanel('b', 1).add(getPieceImageLabel(whiteKnightIterator.next()));
         getSquarePanel('g', 1).add(getPieceImageLabel(whiteKnightIterator.next()));
         getSquarePanel('b', 8).add(getPieceImageLabel(blackKnightIterator.next()));
         getSquarePanel('g', 8).add(getPieceImageLabel(blackKnightIterator.next()));
 
         //Pawn
         Iterator<Piece> whitePawnIterator = PieceSet.getPieces(Piece.Color.WHITE, Piece.Type.PAWN).iterator();
         Iterator<Piece> blackPawnIterator = PieceSet.getPieces(Piece.Color.BLACK, Piece.Type.PAWN).iterator();
         getSquarePanel('a', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('b', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('c', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('d', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('e', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('f', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('g', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('h', 2).add(getPieceImageLabel(whitePawnIterator.next()));
         getSquarePanel('a', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('b', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('c', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('d', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('e', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('f', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('g', 7).add(getPieceImageLabel(blackPawnIterator.next()));
         getSquarePanel('h', 7).add(getPieceImageLabel(blackPawnIterator.next()));
 
    }

    public void changeImageLabel(Piece piece, Move move) {
        JLabel newImage=getPieceImageLabel(piece);
        getSquarePanel(move.getDestinationFile(), move.getDestinationRank()).removeAll();
        getSquarePanel(move.getDestinationFile(), move.getDestinationRank()).revalidate();
        getSquarePanel(move.getDestinationFile(), move.getDestinationRank()).add(newImage);
    }

    public void changeImageLabelUndo(Piece piece, Move move) {
        JLabel newImage=getPieceImageLabel(new Pawn(piece.getColor()));
        getSquarePanel(move.getOriginFile(), move.getOriginRank()).removeAll();
        getSquarePanel(move.getOriginFile(), move.getOriginRank()).revalidate();
        getSquarePanel(move.getOriginFile(), move.getOriginRank()).add(newImage);
    }

    public void removeEnPassantLabel(Move move) {
        getSquarePanel(MoveLogger.getPreviousMove(move).getDestinationFile(),MoveLogger.getPreviousMove(move).getDestinationRank()).removeAll();
       // getSquarePanel(MoveLogger.getPreviousMove(move).getDestinationFile(),MoveLogger.getPreviousMove(move).getDestinationRank()).removeAll();
    }

    private void initializeBoardLayeredPane() {
        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBounds(0, 0, 800, 800);
        boardLayeredPane = new JLayeredPane();
        boardLayeredPane.setPreferredSize(new Dimension(800, 800));
        boardLayeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        PieceDragAndDropListener pieceDragAndDropListener = new PieceDragAndDropListener(this);
        boardLayeredPane.addMouseListener(pieceDragAndDropListener);
        boardLayeredPane.addMouseMotionListener(pieceDragAndDropListener);
        boardLayeredPane.setVisible(true);
        this.add(boardLayeredPane, BorderLayout.CENTER);
    }

    public JLabel getPieceImageLabel(Piece piece) {
        Image pieceImage = new ImageIcon(getClass().getResource(piece.getImageFileName())).getImage();
        pieceImage = pieceImage.getScaledInstance(SQUARE_DIMENSION, SQUARE_DIMENSION, Image.SCALE_SMOOTH);
        JLabel pieceImageLabel = new JLabel(new ImageIcon(pieceImage));
        return pieceImageLabel;
    }

    public boolean isBoardReversed() {
        return boardReversed;
    }

    @Override
    public void update(Observable o, Object arg) {
        executeMove((Move) arg);
    }
}
