package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.io.*;

public class Player extends Rectangle{
	
	public static final int PlayerWidth = 20, PlayerHeight = 40;
	
	public static final int HorozontalSPEED = 5, VerticalSPEED = 15;
	int yVelocity, xVelocity;
	int startX, startY;
	public char upButton, leftButton, rightButton;
	public boolean inAir;
	Color colour;
	Door destination;
	
	private int w, h;
	public Image playerImage;
	
	public Player(char upButton, char leftButton, char rightButton, int startX, int startY, Color colour, int doorX, int doorY) {
		super(startX, startY, PlayerWidth, PlayerHeight);
		x = startX; y = startY;
		this.startX = startX; this.startY = startY;
		
		this.upButton = upButton;
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.colour = colour;
		destination = new Door(doorX, doorY, this.colour);
		
		xVelocity = 0; yVelocity = 0;
		inAir = true;
		
		//loadImage();
	}
	
	/*public void loadImage() {
		ImageIcon ii = new ImageIcon("src/assets/Fireboy.png");
		playerImage = ii.getImage();
		
		w = playerImage.getWidth(null);
		h = playerImage.getHeight(null);
	}
	
	public Image getImage() {
		return playerImage;
	}*/
	
	public void keyPressed(KeyEvent e) {
		// If the upbutton is being pressed
		if (e.getKeyCode() == leftButton || e.getKeyChar() == leftButton) {
			// Set y direction to negative of speed
			xVelocity = -HorozontalSPEED;
			move();
		}
		
		// If downbutton is pressed
		if (e.getKeyCode() == rightButton || e.getKeyChar() == rightButton) {
			// Set y direction to positive of speed
			xVelocity = HorozontalSPEED;
			move();
		}
		
		if (e.getKeyCode() == upButton || e.getKeyChar() == upButton) {
			// Set y direction to positive of speed
			if(!inAir) {
				inAir = true;
				yVelocity = -VerticalSPEED;
			}
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
	
	public boolean atDestination() {
		if(xVelocity != 0 || yVelocity != 0) return false;
		if(x < destination.x) return false;
		if(y < destination.y) return false;
		if(x + PlayerWidth > destination.x + Door.DOOR_WIDTH) return false;
		if(y + PlayerHeight > destination.y + Door.DOOR_HEIGHT) return false;
		return true;
	}
	
	public void resetPosition() {
		x = startX;
		y = startY;
		inAir = true;
		yVelocity = 0;
		xVelocity = 0;
	}
	
	public void move() {
		y += yVelocity;
		yVelocity++;
		x += xVelocity;
	}
	
	public void draw(Graphics g) {
		g.setColor(colour); // set colour to white
		g.fillRect(x, y, PlayerWidth, PlayerHeight); // draw the rectangle with the position and dimensions
		destination.draw(g);
	}
}
