package pieces;

import util.Move;

/**
 * Abstract class for chess piece.
 */
public abstract class Piece {

    public enum Color {
        WHITE, BLACK
    }
    
    public enum Type {
        KING, ROOK, BISHOP, QUEEN, KNIGHT, PAWN
    }
    
    protected Color color;
    protected Type type;
    protected boolean capture;
    protected boolean enpassant;
    protected boolean promoted;
    
    public Piece(Color color) {
        this.color = color;
        this.capture = false;
        this.enpassant=false;
        this.promoted=false;
    }

    public abstract boolean validateMove(Move move);

    public String getImageFileName() {
        String fileName = "/pieces/";
        switch (color) {
            case WHITE:
                fileName += "white_";
                break;
            case BLACK:
                fileName += "black_";
                break;
        }
        switch (type) {
            case KING:
                fileName += "king";
                break;
            case ROOK:
                fileName += "rook";
                break;
            case BISHOP:
                fileName += "bishop";
                break;
            case QUEEN:
                fileName += "queen";
                break;
            case KNIGHT:
                fileName += "knight";
                break;
            case PAWN:
                fileName += "pawn";
                break;
        }
        fileName += ".png";
        return fileName;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }
    
    public void changeType(Piece.Type which) {
        this.type=which;
    }

    public void setCapture(boolean isCaptured) {
        this.capture = isCaptured;
    }

    public boolean getCapture() {
        return this.capture;
    }

    public boolean getEnpassant() {
        return this.enpassant;
    }
    
    public void setEnpassant(boolean b) {
        this.enpassant=b;
    }

    public boolean getPromoted() {
        return this.promoted;
    }
    
    public void setPromoted(boolean b) {
        this.promoted=b;
    }


}
