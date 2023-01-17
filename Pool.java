package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;

public class Pool extends Rectangle{
	
	public static final int POOL_DEPTH = 10;
	public int length, type;
	public int startX, startY;
	Color colour;
	
	public Pool(int startX, int startY, int length, int type) {
		super(startX, startY, length, POOL_DEPTH);
		this.type = type;
		if(type == 0) colour = Color.green;
		else if(type == 1) colour = Color.red;
		else colour = Color.blue;
		
		this.startX = startX;
		this.startY = startY;
		this.length = length;
	}
	
	public void draw(Graphics g) {
		g.setColor(colour);
		g.fillRect(x, y, length, POOL_DEPTH);
	}
}
