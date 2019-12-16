package ui;

import util.GameModel;
import util.Move;
import util.MoveLogger;
import pieces.*;

import javax.swing.*;

import board.Board;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class ScoreBox extends JPanel implements Observer {

    private GameModel gameModel;
    private JScrollPane scoreScrollPane;
    private JTextArea scoreTextArea;
    private String score;
    private String scoreText;
    int whiteScore;
    int blackScore;

    public ScoreBox(GameModel gameModel) {
        this.gameModel = gameModel;
        whiteScore = 0;
        blackScore = 0;
        initialize();
    }

    public void printScore(Move move) {
        String ddangddang = " : ";
        
        if ((move.getCapturedPiece() != null) && (move.getPiece().getColor().equals(Piece.Color.WHITE))) {
            if(move.getCapturedPiece().getType().equals(Piece.Type.PAWN)){
                whiteScore += 1;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.KNIGHT)){
                whiteScore += 3;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.BISHOP)){
                whiteScore += 3;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.ROOK)){
                whiteScore += 5;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.QUEEN)){
                whiteScore += 9;
            }
        }else if ((move.getCapturedPiece() != null) && (move.getPiece().getColor().equals(Piece.Color.BLACK))) {
            if(move.getCapturedPiece().getType().equals(Piece.Type.PAWN)){
                blackScore += 1;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.KNIGHT)){
                blackScore += 3;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.BISHOP)){
                blackScore += 3;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.ROOK)){
                blackScore += 5;
            }else if(move.getCapturedPiece().getType().equals(Piece.Type.QUEEN)){
                blackScore += 9;
            }
        }  
        scoreText = Integer.toString(whiteScore)+ddangddang+Integer.toString(blackScore);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scoreTextArea.setText(scoreText);
            }
        });
    }

    private void initialize() {
        
        //scoreTextArea = new JTextArea(score);
        //scoreTextArea.setBackground(Color.GRAY);
        //this.setPreferredSize(new Dimension(300, 60));
        //this.add(moveHistoryScrollPane);

        //score = new String("I will tell you score!\n");
        scoreTextArea = new JTextArea(scoreText);
        scoreTextArea.setBackground(Color.GRAY);
        scoreScrollPane = new JScrollPane(scoreTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scoreScrollPane.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreScrollPane.setViewportView(scoreTextArea);
        scoreScrollPane.setPreferredSize(new Dimension(300, 60));
        this.setPreferredSize(new Dimension(300, 60));
        this.add(scoreScrollPane);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}