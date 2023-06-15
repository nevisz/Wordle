import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;

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

    String wordStr; // keeps game's word in orignal state
    HashMap<String,int[]> wordMap; // letter, [# of occurences in word that need to be considered, yellowCount]
    String playerGuessStr; // keeps player's word in original state
    ArrayList<String> playerGuessL; // is modified, each element is in the format letter,index

    private int currRound; // is equal to the current row number
    private int greenCount; // accounts for green tiles of the current row

    public Board() throws IOException {

        wordStr = "";
        wordMap = new HashMap<String,int[]>();
        playerGuessStr = "";
        playerGuessL = new ArrayList<String>();

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
        int i = 0;
        // populating wordStr, wordL & wordMap
        for (String ch : sc.nextLine().split(",")) {
            wordStr += ch;
            if (!wordMap.containsKey(ch)) {
                int[] list = {1,0};
                wordMap.put(ch,list);
            }
            else {
                int[] list = wordMap.get(ch);
                list[0]++;
                wordMap.put(ch,list);
            }
            i++;
        }
    }

    /**
     * Button action - checks the input of the text field and changes the current row of squares on the
     * board accordingly
     * 
     * @param g - ActionEvent object 
     */

    public void actionPerformed(ActionEvent e) { 

        resetWordMap();
        playerGuessL.clear(); // so it only contains the letters of the player's next guess
        greenCount = 0;

        String playerGuessStr = textField.getText().toLowerCase();
        textField.setText("");

        if (!(playerGuessStr.equals("") || playerGuessStr.contains(" ") ||
            playerGuessStr.length() != 5)) {

            for (int i = 0; i<playerGuessStr.length(); i++) {
                playerGuessL.add(playerGuessStr.substring(i,i+1)+","+i);
            }

            System.out.println("---------------------------------------------------");
            System.out.println("Guess #" + Integer.toString(currRound+1) + ": " + playerGuessL.toString() + "\n");

            String coord = "";

            // IDENTIFYING GREEN AND TRUE GREY TILES (letters that aren't in the word) 
            // can't use for (String ch : wordMap.keySet()) since HashMaps don't maintain insertion order
            for (int i = 0; i<5; i++) {

                String wordChar = wordStr.substring(i,i+1);
                String guessChar = playerGuessStr.substring(i,i+1);

                coord = Integer.toString(currRound)+i;
                System.out.println("Checking tile " + coord + "...");
                panel.changeLetter(coord,guessChar.toUpperCase());

                // playerGuessL is used later, for a second linear check
                // Elements are removed from it so that there isn't a repeat (checking a verified green or true
                // grey tile

                if (wordChar.equals(guessChar)) {
                    greenFill(coord); greenCount++;
                    playerGuessL.remove(wordChar+","+i); // occurence of the letter is no longer needed

                    int[] list = wordMap.get(wordChar);
                    list[0]--; // occurence of the letter is no longer needed - letter count is decremented
                    wordMap.put(wordChar,list);
                }
                else if (!wordStr.contains(guessChar)) {
                    greyFill(coord);
                    playerGuessL.remove(guessChar+","+i); // occurence of the letter is no longer needed
                }
            }

            // IDENTIFYING YELLOW AND OTHER GREY TILES 
            // The remaining letters in playerGuessL ARE in the word but in the wrong position
            // Also the player's guess may have more than enough of this letter. Either..
            //      > occurences of the letter are already in the right positions and therefore
            //      another occurrence (in the wrong position) is insignificant
            //      > occurrences of the letter in the wrong position = the # of times the letter appears in
            //      the word and therefore another occurrence (in another wrong position) is insigificant
            for (int i = 0; i<playerGuessL.size(); i++) {

                String guessChar = playerGuessL.get(i).substring(0,1);
                coord = Integer.toString(currRound)+playerGuessL.get(i).substring(2,3);
                
                int[] list = wordMap.get(guessChar);

                // It can't be ==, otherwise a tile will turn yellow when
                // the # of yellow tiles already == the # of occurences in the word
                // But by entering the if statement, the # of yellow tiles for a specified letter in the word
                // is incremented
                if (list[1]<list[0]) { 
                    yellowFill(coord);
                    list[1]++;
                    wordMap.put(guessChar,list);
                }
                else { greyFill(coord); }
            } 
            currRound++;
            System.out.println("\nGreen Count: " + greenCount);
            System.out.println("---------------------------------------------------");
        }
    }

    public String getWord() {
        return wordStr.toUpperCase();
    }

    /**
     * Changes the letter field of a Tile object in the HashMap of the BoardPanel object
     * 
     * @param coord - the key of the Tile object
     * @param letter - the desired letter
	 */

    public void changeLetter(String coord, String letter) {
        panel.changeLetter(coord,letter);
    }

    /**
     * Calls changeFillColour() of the BoardPanel class, desired colour is green
     * 
     * @param coord - the key of the Tile object
     */

    public void greenFill(String coord) {
        panel.changeFillColour(coord,0x5BA654);
    }

    /**
     * Calls changeFillColour() of the BoardPanel class, desired colour is yellow
     * 
     * @param coord - the key of the Tile object
     */

    public void yellowFill(String coord) {
        panel.changeFillColour(coord,0xBCB850);
    }

    /**
     * Calls changeFillColour() of the BoardPanel class, desired colour is grey
     * 
     * @param coord - the key of the Tile object
     */

    public void greyFill(String coord) {
        panel.changeFillColour(coord,0x7D7D7D);
    }

    /**
     * Repopulates wordMap such that it contains the initial info on the word to be guessed
	 */

    public void resetWordMap() {
        wordMap.clear();
        for (int i = 0; i<wordStr.length(); i++) {
            String wordChar = wordStr.substring(i,i+1);
            if (!wordMap.containsKey(wordChar)) {
                int[] list = {1,0};
                wordMap.put(wordChar,list);
            }
            else {
                int[] list = wordMap.get(wordChar);
                list[0]++;
                wordMap.put(wordChar,list);
            }
        }
    }

    public boolean isDone() {
        return (currRound>5) || (greenCount == 5);
    }
    
}
