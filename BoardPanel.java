import java.util.HashMap;
import java.awt.*;
import javax.swing.JPanel;

// first tile 00 - (10,60)
// tile to the right is x+60, tile below is y+60 

public class BoardPanel extends JPanel {

    private HashMap<String,Tile> tiles;
    Tile tile;

    public BoardPanel() {

        tiles = new HashMap<String,Tile>();
        this.setPreferredSize(new Dimension(310,500));

        // populating HashMap
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

    public void changeLetter(String coord, String letter) {
        tiles.get(coord).setLetter(letter);
        repaint();
    }

    public void changeFillColour(String coord, int hexColour) {
        tiles.get(coord).setFillColour(hexColour);
        repaint();
    }

    public Tile getTile(String coord) {
        return tiles.get(coord);
    }
    
}
