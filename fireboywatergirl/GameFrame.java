package fireboywatergirl;

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame{

	GamePanel level1;
	
	public GameFrame() {
		level1 = new GamePanel(1); // run GamePanel constructor
		this.add(level1);
		this.setTitle("Fireboy and Lavagirl"); // set title for frame
		this.setResizable(false); // frame can't change size
		this.setBackground(new Color(43,51,1)); // background is black
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
		this.pack();// makes components fit in window - don't need to set JFrame size, as it will
					// adjust accordingly
		this.setVisible(true); // makes window visible to user
		this.setLocationRelativeTo(null);// set window in middle of screen
	}
}
