
// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	
	
	
    public void run(){
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);
        // Status panel
        
        

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        

        // Main playing area
        final MineBoard court = new MineBoard(status, 15, 30);
        court.setMines();
		
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        final JButton undo = new JButton("Undo");
        
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.undo();
            }
        });
        
        String instructions = 
        "Instructions for MineSweeper\n" + 
        "\n" + 
        "You are presented with a board of squares. Some squares contain mines (bombs), others don't. "
        + "\n If you click on a square containing a bomb, you lose. If you manage to click all the squares"
        + " (without clicking on any bombs) you win.\n" + 
        "Clicking a square which doesn't have a bomb reveals the number of neighbouring squares containing "
        + "bombs. \n" + 
        "To open a square, point at the square and click on it. To mark a square you think is a "
        + "bomb, right-click.";
        
        final JButton instruct = new JButton("Instructions");
        
        instruct.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, instructions, "MineSweeper Instructions", 
						JOptionPane.INFORMATION_MESSAGE);
			}
        	
        });
        
        control_panel.add(instruct);
        control_panel.add(reset);
        control_panel.add(undo);
        

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        //court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
