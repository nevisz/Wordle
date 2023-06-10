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

// first word - (28,93)

public class Board extends JFrame implements ActionListener {

    private BoardPanel panel;
    private JTextField textField;
    private JButton button;

    ArrayList<String> wordL;
    private String playerGuess;
    private int currRound;
    private int greenCount; // accounts for current row

    public Board() throws IOException {

        wordL = new ArrayList<String>();
        playerGuess = "";
        currRound = 0;
        greenCount = 0;

        // frame components
        panel = new BoardPanel();
        textField = new JTextField(10);
        textField.setBounds(105,455,100,25);
        button = new JButton(">");
        button.setBounds(214,453,30,30);
        button.addActionListener(this);

        // frame setup (adding components, etc)
        add(textField);
        add(button);
        add(panel); pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0x151515));
        setLocationRelativeTo(null);
        setVisible(true);

        // reading word from file
        Scanner sc = new Scanner(new File("5_letters.csv"));  
        int count = 1;

        Random rand = new Random();
        int startNum = rand.nextInt(0,25);
        int wordRow = rand.nextInt(startNum*100,(startNum*100)+101);
        
        sc.nextLine();
        while (count < wordRow) {
            sc.nextLine();
            count++;
        }
        for (String str : sc.nextLine().split(",")) { wordL.add(str); }
    }

    public void actionPerformed(ActionEvent e) { // pressing button

        greenCount = 0;

        if (!(textField.getText().equals("") ||
            textField.getText().contains(" ") ||
            textField.getText().length() != 5)) {

            playerGuess = textField.getText();
            System.out.println("Guess #" + Integer.toString(currRound+1) + ": " + playerGuess.toUpperCase());
            textField.setText("");

            // comparing playerGuess to word and making changes to the tiles (of the current row) accordingly
            String coord = "";

            for (int i = 0; i<5; i++) {

                coord = Integer.toString(currRound)+Integer.toString(i);
                System.out.println("Checking tile " + coord);
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

    public void changeLetter(String coord, String letter) {
        panel.changeLetter(coord,letter);
    }

    public void greenFill(String coord) {
        panel.changeFillColour(coord,0x5BA654);
    }

    public void yellowFill(String coord) {
        panel.changeFillColour(coord,0xBCB850);
    }

    public void greyFill(String coord) {
        panel.changeFillColour(coord,0x7D7D7D);
    }

    public boolean isDone() {
        return (currRound>5) || (greenCount == 5);
    }
    
}
