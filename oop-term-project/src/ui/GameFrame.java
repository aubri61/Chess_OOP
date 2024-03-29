package ui;

import util.Core;
import util.GameModel;
import util.Move;

import javax.swing.*;

import board.Board;
import pieces.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import static java.lang.System.exit;

public class GameFrame extends JFrame implements Observer{

    private GameModel gameModel;

    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGameMenuItem;
    private JMenuItem saveGameMenuItem;
    private JMenuItem preferencesMenuItem;
    private JMenuItem exitMenuItem;

    private JMenu helpMenu;
    private JMenuItem customPiecesMenuItem;
    private JMenuItem aboutMenuItem;

    private JPanel boardPanel;
    private JPanel timerPanel;
    private JPanel controlPanel;
    private JPanel moveHistoryPanel;
    private JPanel scoreBox;


    public GameFrame(GameModel gameModel) {
        super("CSI2102 at YSU | InGame");
        this.setIconImage(new ImageIcon(getClass().getResource("/ysu.jpg")).getImage());
        this.boardPanel = gameModel.getBoardPanel();
        this.timerPanel = gameModel.getTimerPanel();
        this.controlPanel = gameModel.getControlPanel();
        this.moveHistoryPanel = gameModel.getMoveHistoryPanel();
        this.scoreBox = gameModel.getScoreBox();
        loadInterface();
        gameModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void showCheckDialog() {
        JOptionPane.showMessageDialog(this, "That's a Check!", "Check", JOptionPane.WARNING_MESSAGE);
    }

    public void showCheckmateDialog() {
        SoundPlay.playSound("wow.wav");
        JOptionPane.showMessageDialog(this, "That's a Checkmate!", "Checkmate", JOptionPane.WARNING_MESSAGE);
                    
    }

    public Piece showPromotion (Move move, BoardPanel boardPanel) {
        String[] buttons={"Queen","Knight","Bishop","Rook"};
        int selected = JOptionPane.showOptionDialog(null, "Pawn promotes to...", "Select Promote Piece.",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
        if(selected == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "Pawn Piece was not changed");
            return move.getPiece();
        } else {
            return PieceSet.promotion(move, buttons[selected], boardPanel);
        }
    }

    public void showWinDialog(Move move) {
        SoundPlay.playSound("wow.wav");
        String str="";
        if (move.getPiece().getColor().equals(Piece.Color.WHITE)) {
            str+="White has won the game!";
        } else {
            str+="Black has won the game!";
        }
        JOptionPane.showMessageDialog(this, str, "Game End", JOptionPane.WARNING_MESSAGE);
    }

    private void loadInterface() {
        initializeMenuBar();
        initializePanels();
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(Core.getLaunchFrame());
        this.setVisible(true);
    }

    private void initializeMenuBar() {
        // game menu
        newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.setEnabled(false);

        saveGameMenuItem = new JMenuItem("Save Game");
        saveGameMenuItem.setEnabled(false);

        preferencesMenuItem = new JMenuItem("Preferences");
        preferencesMenuItem.setEnabled(false);

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userChoice = JOptionPane.showConfirmDialog(getContentPane(),
                        "Do you really want to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION);
                if (userChoice == JOptionPane.YES_OPTION) {
                    exit(0);
                }
            }
        });

        gameMenu = new JMenu("Game");
        gameMenu.add(newGameMenuItem);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(preferencesMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);

        // help menu
        customPiecesMenuItem = new JMenuItem("Custom pieces...");
        customPiecesMenuItem.setEnabled(false);

        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This program is made for term project of CSI2102(2019 Fall) at YSU");
            }
        });

        helpMenu = new JMenu("Help");
        helpMenu.add(customPiecesMenuItem);
        helpMenu.add(aboutMenuItem);
        helpMenu.addSeparator();

        // menu bar
        menuBar = new JMenuBar();
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        menuBar.setVisible(true);
        this.setJMenuBar(menuBar);
    }

    private void initializePanels() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.setLayout(gridBagLayout);

        // BoardPanel
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 4;
        gridBagLayout.setConstraints(boardPanel, gridBagConstraints);
        this.add(boardPanel);

         // TimerPanel
         gridBagConstraints.gridx = 5;
         gridBagConstraints.gridy = 0;
         gridBagConstraints.gridwidth = 1;
         gridBagConstraints.gridheight = 1;
         gridBagLayout.setConstraints(timerPanel, gridBagConstraints);
         this.add(timerPanel);
 
         // ControlPanel
         gridBagConstraints.gridx = 5;
         gridBagConstraints.gridy = 1;
         gridBagConstraints.gridwidth = 1;
         gridBagConstraints.gridheight = 1;
         gridBagLayout.setConstraints(controlPanel, gridBagConstraints);
         this.add(controlPanel);
 
        //ScoreBox
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagLayout.setConstraints(scoreBox, gridBagConstraints);
        this.add(scoreBox);
 
    
        // MoveHistoryPanel
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagLayout.setConstraints(moveHistoryPanel, gridBagConstraints);
        this.add(moveHistoryPanel);
    }

}
