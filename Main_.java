import javax.swing.JOptionPane;
import java.io.IOException;


public class Main_ {
    public static void main(String[] args) {
    
        while (true) {

        boolean choiceIsValid = false;
        int playAgain = -1;
            
            try {
                Board board = new Board();
                System.out.println("Word: " + board.getWord().toUpperCase() + "\n"); // can comment this out

                while(!board.isDone()) {
                    // v strange...it only exits the while loop when there's print or println
                    // otherwise it remains stuck in the loop, even if the condition is false
                    System.out.print("");
                }
                JOptionPane.showMessageDialog(null, "The word was... " + 
                board.getWord().toUpperCase() + " !!!");
            }
            catch (IOException io) {
                System.out.println(io.getMessage());
            }
            while (!choiceIsValid) {
                try {
                    playAgain = Integer.parseInt(JOptionPane.showInputDialog(null, "Play again?"
                    + "\n0 - yes\n1 - no"));
                }
                catch (NumberFormatException nfe) {
                    System.err.println("You must enter an integer.");
                    System.err.println("Exception: " + nfe + "\n");
                }
                if (playAgain == 0 || playAgain == 1) { choiceIsValid = true; }
            }
            if (playAgain == 1) { break; }
            
        }
        System.exit(0);
    }
}
