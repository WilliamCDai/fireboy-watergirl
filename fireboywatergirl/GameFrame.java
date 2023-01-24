/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * GameFrame class is extension of Jframe
 * contains a Jpanel which contains all elements of the game
 */

package fireboywatergirl;

import java.awt.*;
import javax.swing.*;
import java.io.*;

public class GameFrame extends JFrame{

	GamePanel level; // make JPanel
	
	public GameFrame() throws IOException{
		
		level = new GamePanel();
		this.add(level);
		this.setTitle("Fireboy and Watergirl"); // set title for frame
		this.setResizable(false); // frame can't change size
		this.setBackground(Color.black); // background is black
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
		this.pack();// makes components fit in window - don't need to set JFrame size, as it will
					// adjust accordingly
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen
	}
}
