import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.io.File;
import java.io.IOException;


/*
 * This class designs a subclass of JFrame that will be the Wordle window. It has custom methods
 * that help fulfill the functions/innerworkings of the game.
 */

public class Board extends JFrame implements ActionListener {

    private BoardPanel panel;
    private JTextField textField;
    private JButton button;

    ArrayList<String> wordL; // contains the 5 letters of the word to be guessed
    private String playerGuess;
    private int currRound; // is equal to the current row number
    private int greenCount; // accounts for green tiles of the current row

    public Board() throws IOException {

        wordL = new ArrayList<String>();
        playerGuess = "";
        currRound = 0;
        greenCount = 0;

        // FRAME COMPONENTS SETUP
        panel = new BoardPanel();
        textField = new JTextField(10);
        textField.setBounds(105,455,100,25);
        button = new JButton(">");
        button.setBounds(214,453,30,30);
        button.addActionListener(this);

        // FRAME SETUP (adding components, etc)
        add(textField);
        add(button);
        add(panel); pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0x151515));
        setLocationRelativeTo(null);
        setVisible(true);

        // READING WORD FROM FILE
        Scanner sc = new Scanner(new File("5_letters.csv"));  
        int count = 2;

        Random rand = new Random();
        int startNum = rand.nextInt(0,25);
        int wordLine = rand.nextInt(startNum*100,(startNum*100)+101); // line # of the word to be guessed
        
        sc.nextLine(); // line 1 only contains numbers, sc is now at the start of line 2
        // skipping lines of the file until it reaches the desired line #
        // if wordLine = 0,1 or 2 it won't skip the current line but read it instead
        while (count < wordLine) { 
            sc.nextLine();
            count++;
        }
        for (String str : sc.nextLine().split(",")) { wordL.add(str); }

    }

    /**
     * Button action - checks the input of the text field and changes the current row of squares on the
     * board accordingly
     * 
     * @param g - ActionEvent object 
     * @return void
     */

    public void actionPerformed(ActionEvent e) { 

        greenCount = 0;

        if (!(textField.getText().equals("") ||
            textField.getText().contains(" ") ||
            textField.getText().length() != 5)) {

            playerGuess = textField.getText();
            System.out.println("Guess #" + Integer.toString(currRound+1) + ": " + playerGuess.toUpperCase());
            textField.setText("");

            String coord = "";

            for (int i = 0; i<5; i++) {

                coord = Integer.toString(currRound)+Integer.toString(i);
                System.out.println("Checking tile " + coord + "...");
                panel.changeLetter(coord,playerGuess.substring(i,i+1).toUpperCase());
            
                if (wordL.get(i).equalsIgnoreCase(playerGuess.substring(i,i+1))) {
                    greenFill(coord); greenCount++;
                }
                else if (wordL.contains(playerGuess.substring(i,i+1))){ yellowFill(coord); }
                else { greyFill(coord); }

            }
            System.out.println("Green count: " + greenCount + "\n");
            currRound++;

        }
    }

    public String getWord() {
        String word = "";
        for (String str : wordL) { word += str; }
        return word;
    }

    /**
     * Changes the letter field of a Tile object in the HashMap of the BoardPanel object
     * 
     * @param coord - the key of the Tile object
     * @param letter - the desired letter
     * @return void
	 */

    public void changeLetter(String coord, String letter) {
        panel.changeLetter(coord,letter);
    }

    /**
     * Calls changeFillColour() of the BoardPanel class, desired colour is green
     * 
     * @param coord - the key of the Tile object
     * @return void
     */

    public void greenFill(String coord) {
        panel.changeFillColour(coord,0x5BA654);
    }

    /**
     * Calls changeFillColour() of the BoardPanel class, desired colour is yellow
     * 
     * @param coord - the key of the Tile object
     * @return void
     */

    public void yellowFill(String coord) {
        panel.changeFillColour(coord,0xBCB850);
    }

    /**
     * Calls changeFillColour() of the BoardPanel class, desired colour is grey
     * 
     * @param coord - the key of the Tile object
     * @return void
     */

    public void greyFill(String coord) {
        panel.changeFillColour(coord,0x7D7D7D);
    }

    public boolean isDone() {
        return (currRound>5) || (greenCount == 5);
    }
    
}
