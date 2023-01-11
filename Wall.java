package fireboywatergirl;

import java.awt.*;

public class Wall extends Rectangle{
	
	int x1, x2, y1, y2;
	
	public Wall(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2-x2, y2-y1);
		this.x1 = x1; this.x2 = x2;
		this.y1 = y1; this.y2 = y2;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}
}
