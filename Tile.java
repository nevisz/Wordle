
/*
 * This class contains all of the fields of a Wordle tile, as well as getters and setters
 */

public class Tile {

    private String letter;
    private int xValue; private int yValue; // coordinate of the tile and its border
    private int xLetter; private int yLetter; // coordinate of letter on tile

    final private int sideLength = 50;
    final private int borderColour = 0x555555;
    private int fillColour;

    public Tile (int xValue, int yValue, int xLetter, int yLetter) {
        this.xValue = xValue;
        this.yValue = yValue;
        letter = "";
        this.xLetter = xLetter;
        this.yLetter = yLetter;
        fillColour = 0x151515;
    }

    public int getX() {
        return xValue;
    }

    public int getY() {
        return yValue;
    }

    public String getLetter() {
        return letter;
    }

    public int getXLetter() {
        return xLetter;
    }

    public int getYLetter() {
        return yLetter;
    }

    public int getSideLength() {
        return sideLength;
    }

    public int getFillColour() {
        return fillColour;
    }

    public int getBorderColour() {
        return borderColour;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setFillColour(int hexColour) {
        this.fillColour = hexColour;
    }

}
