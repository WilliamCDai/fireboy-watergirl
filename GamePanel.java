package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	public Image image;
	public Graphics graphics;
	public Thread gameThread;
	
	public static final int GAME_WIDTH = 1080;
	public static final int GAME_HEIGHT = 720;
	int level;
	Wall walls[] = new Wall[4];
	
	Player fireboy;

	public GamePanel(int level) {
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		
		this.level = level;
		
		//TEMPORARY
		walls[0] = new Wall(0, 0, 1280, 30);
		walls[1] = new Wall(0, 0, 30, 720);
		walls[2] = new Wall(0, 690, 1280, 720);
		walls[3] = new Wall(1050, 0, 1280, 720);
		fireboy = new Player('w', 'a', 'd', 100, 100);
		//TEMPORARY
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void draw(Graphics g) {
		for(int i=0;i<4;i++) walls[i].draw(g);
	}
	
	public void paint(Graphics g) {
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics); // update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // redraw everything on the screen
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				repaint();
				delta--;
			}
		}
		
	}
}
