/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * PLayer class is extension of Rectangle
 * It's a rectangle that the player can move around and hit things
 */

package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Player extends Rectangle{
	
	public static final int PlayerWidth = 26, PlayerHeight = 50; // dimensions of player
	
	public static final int HorozontalSPEED = 5, VerticalSPEED = 15; // How fast the player can move
	// Player has attributes: x and y velocities, beginning location, corresponding controls, is it flying
	int yVelocity, xVelocity;
	int startX, startY;
	public char upButton, leftButton, rightButton;
	public boolean inAir;
	Color colour; //Colour just indicates if it is fireboy or watergirl
	//Each player has a corresponding door to go to, an image and its file path
	Door destination;
	Image image; 
	String imageFilePath;
	
	// Constructor for player takes button controls, starting locations, colour, and coords of destination door
	public Player(char upButton, char leftButton, char rightButton, int startX, int startY, Color colour, int doorX, int doorY) {
		super(startX, startY, PlayerWidth, PlayerHeight); // call parent constructor
		// Store starting location
		this.startX = startX; this.startY = startY;
		
		// Create image depending on which character: red = fireboy, blue = watergirl
		if(colour == Color.red) imageFilePath = "src/Images/Fireboy.png";
		else imageFilePath = "src/Images/Watergirl.png";
		image = new ImageIcon(imageFilePath).getImage();
		
		// Store corresponding buttons and colour
		this.upButton = upButton;
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.colour = colour;
		
		// Initialize the corresponding destination door with coords and colour
		destination = new Door(doorX, doorY, this.colour);
		
		xVelocity = 0; yVelocity = 0; // beginning velocities are 0
		inAir = true; // starts in air always
	}
	
	// Called frorm gamepanel when a key is pressed and passes the keyEvent
	public void keyPressed(KeyEvent e) {
		// If the leftbutton is being pressed
		if (e.getKeyCode() == leftButton || e.getKeyChar() == leftButton) {
			// Set x direction to negative of speed
			xVelocity = -HorozontalSPEED;
			move();
		}
		
		// If rightbutton is pressed
		if (e.getKeyCode() == rightButton || e.getKeyChar() == rightButton) {
			// Set x direction to positive of speed
			xVelocity = HorozontalSPEED;
			move();
		}
		
		// if up button is pressed
		if (e.getKeyCode() == upButton || e.getKeyChar() == upButton) {
			if(!inAir) { // If player is not in air, player can only jump if not in air
				// Set y direction to negative of VerticalSpeed: essentially a jump up
				inAir = true;
				yVelocity = -VerticalSPEED;
			}
			move();
		}
	}

	// called from GamePanel when any key is released (no longer being pressed down)
	public void keyReleased(KeyEvent e) {
		// If the key that stopped being pressed is the leftbutton
		if (e.getKeyCode() == leftButton || e.getKeyChar() == leftButton) {
			// Stop moving left
			xVelocity = 0;
			move();
		}
		
		// If the key that stopped being pressed is rightbutton
		if (e.getKeyCode() == rightButton || e.getKeyChar() == rightButton) {
			//stop moving right
			xVelocity = 0;
			move();
		}
	}
	
	// called by gamepanel to see if player is at destination
	public boolean atDestination() {
		if(xVelocity != 0 || yVelocity != 0) return false; // if player is moving, they arent there yet (just my rules)
		// If any part of player is outside the 4 boundaries of the door, they aren't at destination
		if(x < destination.x) return false;
		if(y < destination.y) return false;
		if(x + PlayerWidth > destination.x + Door.DOOR_WIDTH) return false;
		if(y + PlayerHeight > destination.y + Door.DOOR_HEIGHT) return false;
		return true; // returns true only if player has stationary and totally within door
	}
	
	// Called by gamePanel to reset player position and velocities
	public void resetPosition() {
		// Move back to starting location, velocities are 0, is now in air
		x = startX;
		y = startY;
		inAir = true;
		yVelocity = 0;
		xVelocity = 0;
	}
	
	// Called frequently to update the players location
	public void move() {
		y += yVelocity; // increment y position
		if(yVelocity >= 2) inAir = true; // if player is falling then they are clearly in air and cannot jump
		yVelocity++; // increment y velocity, this is like gravity
		x += xVelocity; // increment x position
	}
	
	// Called to draw the player
	public void draw(Graphics2D g2D) {
		g2D.drawImage(image, x, y, null); // draw image of player at player's positions
	}
}
