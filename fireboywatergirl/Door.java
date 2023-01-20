package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Door extends Rectangle{
	
	public static final int DOOR_WIDTH = 55, DOOR_HEIGHT = 60;
	Color colour;
	String imageFilePath;
	Image image;
	
	public Door(int startX, int startY, Color colour) {
		super(startX, startY, DOOR_WIDTH, DOOR_HEIGHT);
		x = startX;
		y = startY;
		this.colour = colour;
		
		if(colour == Color.red) imageFilePath = "src/Images/FireboyDoor.png";
		else imageFilePath = "src/Images/WatergirlDoor.png";
		
		image = new ImageIcon(imageFilePath).getImage();
	}
	
	public void draw(Graphics g) {
		//g.setColor(colour);
		//g.drawRect(x, y, DOOR_WIDTH, DOOR_HEIGHT);
		g.drawImage(image, x, y, null);
	}
}
