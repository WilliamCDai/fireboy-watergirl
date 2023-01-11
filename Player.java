package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;

public class Player extends Rectangle{
	
	public static final int PlayerWidth = 20, PlayerHeight = 40;
	
	public static final int SPEED = 7;
	int yVelocity, xVelocity;
	public char upButton, leftButton, rightButton;
	
	public Player(char upButton, char leftButton, char rightButton, int startX, int startY) {
		super(startX, startY, PlayerWidth, PlayerHeight);
		x = startX; y = startY;
		this.upButton = upButton;
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		
		xVelocity = 0; yVelocity = 0;
		
	}
	
	public void keyPressed(KeyEvent e) {
		// If the upbutton is being pressed
		if (e.getKeyCode() == leftButton || e.getKeyChar() == leftButton) {
			// Set y direction to negative of speed
			xVelocity = -SPEED;
			move();
		}
		
		// If downbutton is pressed
		if (e.getKeyCode() == rightButton || e.getKeyChar() == rightButton) {
			// Set y direction to positive of speed
			xVelocity = SPEED;
			move();
		}
	}

	// called from GamePanel when any key is released (no longer being pressed down)
	public void keyReleased(KeyEvent e) {
		// If the key that stopped being pressed is the upbutton
		if (e.getKeyCode() == leftButton || e.getKeyChar() == leftButton) {
			xVelocity = 0;
			move();
		}
		
		// If the key that stopped being pressed is downbutton
		if (e.getKeyCode() == rightButton || e.getKeyChar() == rightButton) {
			xVelocity = 0;
			move();
		}
	}
	
	public void move() {
		y += yVelocity;
		x += xVelocity;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red); // set colour to white
		g.fillRect(x, y, PlayerWidth, PlayerHeight); // draw the rectangle with the position and dimensions
	}
}
