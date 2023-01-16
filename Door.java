package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;

public class Door extends Rectangle{
	
	public static final int DOOR_WIDTH = 40, DOOR_HEIGHT = 60;
	Color colour;
	
	public Door(int startX, int startY, Color colour) {
		super(startX, startY, DOOR_WIDTH, DOOR_HEIGHT);
		x = startX;
		y = startY;
		this.colour = colour;
	}
	
	public void draw(Graphics g) {
		g.setColor(colour);
		g.drawRect(x, y, DOOR_WIDTH, DOOR_HEIGHT);
	}
}
