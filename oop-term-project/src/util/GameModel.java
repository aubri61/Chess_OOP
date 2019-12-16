package util;

import board.*;
import pieces.Piece;
import pieces.PieceSet;
import ui.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class GameModel extends Observable {

    private GameFrame gameFrame;
    private BoardPanel boardPanel;
    private TimerPanel timerPanel;
    private ControlPanel controlPanel;
    private MoveHistoryPanel moveHistoryPanel;
    private ScoreBox scoreBox;

    private Timer whiteTimer;
    private Timer blackTimer;

    public GameModel() {
        initialize();
    }

    private void initialize() {
        initializeTimers();
        initializeUIComponents();
    }

    public void onMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        onLocalMoveRequest(originFile, originRank, destinationFile, destinationRank);
    }

    public void onUndoRequest() {
        onLocalUndoRequest();
    }

    private void onLocalMoveRequest(char originFile, int originRank, char destinationFile, int destinationRank) {
        Move move = new Move(originFile, originRank, destinationFile, destinationRank);
        if (MoveValidator.validateMove(move)) {
            executeMove(move);
        } else {
            //
        }
    }
    private void onLocalUndoRequest() {
        Move move = MoveLogger.getLastMove();
        if (MoveValidator.validateUndo(move)) {
            executeUndo(move);
        } else {}
    }

    private void executeUndo(Move move) {
        MoveLogger.undoMove();
        Board.executeUndo(move);
        moveHistoryPanel.deleteLastMove();
        boardPanel.executeUndo(move);
        //MoveValidator.validateMove(move, true);
    }


    private void executeMove(Move move) {
        MoveLogger.addMove(move);
        Board.executeMove(move);
        moveHistoryPanel.printMove(move);
        scoreBox.printScore(move);
        boardPanel.executeMove(move);
        switchTimer(move);
        if (MoveValidator.isCheckMove(move)) {
            if (MoveValidator.isRealCheckMate(move)) {
                stopTimer();
                gameFrame.showCheckmateDialog();
            } else {
                gameFrame.showCheckDialog();
                MoveValidator.validateMove(move, false);
            }
        }

        if (MoveValidator.canPromote(move)) {
            Piece somePiece=gameFrame.showPromotion(move,boardPanel);
            Move newMove=new Move(somePiece, move.getDestinationFile(), move.getDestinationRank(),move.getDestinationFile(), move.getDestinationRank());
            if (MoveValidator.isCheckMove(newMove)) {
                if (MoveValidator.isRealCheckMate(newMove)) {
                    stopTimer();
                    gameFrame.showCheckmateDialog();
                } else {
                    gameFrame.showCheckDialog();
                    MoveValidator.validateMove(newMove, false);
                }
            }            
            MoveValidator.validateMove(newMove, false);
        }

        if (MoveValidator.enPassant(move)) {
            move.getPiece().setEnpassant(true);
            MoveValidator.validateMove(move, false);
        }
        if (MoveLogger.getPreviousMove(move)!=null && MoveLogger.getPreviousMove(move).getPiece()!=null && MoveLogger.getPreviousMove(move).getPiece().getEnpassant()) {
            if (move.getPiece().getType().equals(Piece.Type.PAWN)) {
                if (move.getDestinationFile()==MoveLogger.getPreviousMove(move).getDestinationFile() && Math.abs(move.getDestinationRank()-MoveLogger.getPreviousMove(move).getDestinationRank())==1) {
                    boardPanel.removeEnPassantLabel(move);
                    MoveLogger.getPreviousMove(move).getPiece().setEnpassant(false);
                    MoveLogger.getPreviousMove(move).getPiece().setCapture(true);
                    PieceSet.addCapturedPiece(MoveLogger.getPreviousMove(move).getPiece());
                    MoveValidator.validateMove(move, false);

                }
            }
        }

        if(MoveLogger.getPreviousMove(move)!=null && MoveLogger.getPreviousMove(move).getPiece()!=null && MoveLogger.getPreviousMove(move).getPiece().getEnpassant() && !MoveLogger.getPreviousMove(move).getPiece().getCapture()) {
            System.out.println(MoveLogger.getPreviousMove(move).getDestinationFile()+" "+MoveLogger.getPreviousMove(move).getDestinationRank());
            System.out.println("sibal");
            MoveLogger.getPreviousMove(move).getPiece().setEnpassant(false);
            System.out.println(MoveLogger.getPreviousMove(move).getPiece()+" "+MoveLogger.getPreviousMove(move).getOriginFile()+" "+MoveLogger.getPreviousMove(move).getOriginRank());
        }
    }

    public Piece queryPiece(char file, int rank) {
        return Board.getSquare(file, rank).getCurrentPiece();
    }

    private void initializeUIComponents() {
        boardPanel = new BoardPanel(this);
        timerPanel = new TimerPanel(this);
        controlPanel = new ControlPanel(this);
        moveHistoryPanel = new MoveHistoryPanel(this);
        gameFrame = new GameFrame(this);
    }

    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.whiteTimerTikTok();
            }
        });
        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerPanel.blackTimerTikTok();
            }
        });
    }

    private void switchTimer(Move move) {
        /*
        TODO-timer
            start and stop whiteTimer and blackTimer
         */
        if(move.getPiece().getColor().equals(Piece.Color.BLACK)){
            whiteTimer.start();
            blackTimer.stop();
        }else{
            blackTimer.start();
            whiteTimer.stop();
        }
    }

    private void stopTimer() {
        // TODO-timer: stop timers
        blackTimer.stop();
        whiteTimer.stop();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public TimerPanel getTimerPanel() {
        return timerPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public MoveHistoryPanel getMoveHistoryPanel() {
        return moveHistoryPanel;
    }

}