import java.util.HashMap;
import java.awt.*;
import javax.swing.JPanel;


/*
 * This class designs a subclass of JPanel that will be added to a JFrame.
 * 
 * Tile objects are instantiated and stored in a HashMap.
 * Each of these objects account for a square on a 5x6 board. Their associated key represents a
 * coordinate (row,column) ON SAID BOARD, starting from (0,0),(0,1)...(0,4),(1,0),(1,1)...(5,4)
 * 
 * Lines, string and squares are drawn.
 */

public class BoardPanel extends JPanel {

    private HashMap<String,Tile> tiles; 

    public BoardPanel() {

        tiles = new HashMap<String,Tile>(); 
        this.setPreferredSize(new Dimension(310,500));

        // populating HashMap
        // The coordinate of the first tile ON THE PANEL is (10,60)
        // The coordinate of the letter on the first tile is (28,93)
        // Moving rightwards (to the next tile) is x+60, moving downwards is y+60 
        int yValue = 60; int yLetter = 93;
        for (int i = 0; i<6; i++) {
            int xValue = 10; int xLetter = 28;
            for (int i2 = 0; i2<5; i2++) {
                tiles.put(Integer.toString(i)+Integer.toString(i2),new Tile(xValue,yValue,xLetter,yLetter));
                xValue += 60; xLetter += 60;
            }
            yValue += 60; yLetter += 60;
        }
    }

    /**
     * Draws on JPanel
     * 
     * The arguments of some Graphics2D methods utilize the fields of tempTile (Tile object)
     *   > ie. setPaint() uses fillColour so that the colours of the squares are updated on the panel
     * 
     * @param g - Graphics object 
     */
    
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        Color lightGrey = new Color(0x555555);
        
        g2.setPaint(Color.WHITE);
        g2.setFont(new Font("Georgia", Font.BOLD, 16));
        g2.drawString("WORDLE",115,24); // coordinate of bottom-left corner

        g2.setPaint(lightGrey);
        g2.drawLine(0,35, 310, 35); // starting point, endpoint
        g2.setStroke(new BasicStroke(2));

        for (int i = 0; i<6; i++) {
            for (int i2 = 0; i2<5; i2++) {

                Tile tempTile = tiles.get(Integer.toString(i)+Integer.toString(i2));

                g2.setPaint(new Color(tempTile.getFillColour()));
                g2.fillRect(tempTile.getX(),tempTile.getY(),tempTile.getSideLength(),tempTile.getSideLength());
                g2.setPaint(lightGrey);
                g2.drawRect(tempTile.getX(),tempTile.getY(),tempTile.getSideLength(),tempTile.getSideLength());

                g2.setFont(new Font("Aharoni", Font.BOLD, 20));
                g2.setPaint(Color.WHITE);
                g2.drawString(tempTile.getLetter(),tempTile.getXLetter(),tempTile.getYLetter());

            }
        }

        g2.setStroke(new BasicStroke(1));
        g2.setPaint(lightGrey);
        g2.drawLine(0,438, 310, 438); // starting point, endpoint

    }

    /**
     * Changes the letter field of a Tile object in the HashMap
     * 
     * @param coord - the key of the Tile object
     * @param letter - the desired letter
     */

    public void changeLetter(String coord, String letter) {
        tiles.get(coord).setLetter(letter);
        repaint();
    }

    /**
     * Changes the fillColour field of a Tile object in the HashMap
     * 
     * @param coord - the key of the Tile object
     * @param hexColour - the desired colour in hex
     */

    public void changeFillColour(String coord, int hexColour) {
        tiles.get(coord).setFillColour(hexColour);
        repaint();
    }

    public Tile getTile(String coord) {
        return tiles.get(coord);
    }
    
}
