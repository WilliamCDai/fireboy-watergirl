package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	public Image image;
	public Graphics graphics;
	public Thread gameThread;
	public Image background;
	
	public static final int GAME_WIDTH = 1080;
	public static final int GAME_HEIGHT = 720;
	int level, framesAtFinish, framesDead;
	Wall walls[] = new Wall[5];
	Pool pools[] = new Pool[3];
	Instruction levelComplete;
	
	Player fireboy, watergirl;
	
	public boolean gameFinished, playersAlive;

	public GamePanel(int level) {
		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		levelComplete = new Instruction(400, 300, "Level Complete");
		
		this.level = level;
		gameFinished = false;
		playersAlive = true;
		framesAtFinish = 0;
		
		background = new ImageIcon("src/Images/Background.jpeg").getImage();
		
		//TEMPORARY
		walls[0] = new Wall(0, 0, 1080, 30);
		walls[1] = new Wall(0, 0, 30, 720);
		walls[2] = new Wall(0, 690, 1080, 720);
		walls[3] = new Wall(1050, 0, 1080, 720);
		walls[4] = new Wall(30, 600, 1000, 630);
		fireboy = new Player('w', 'a', 'd', 100, 100, Color.red, 400, 540);
		watergirl = new Player((char)38, (char)37, (char)39, 200, 100, Color.blue, 500, 540);
		pools[0] = new Pool(550, 600, 70, 0);
		pools[1] = new Pool(630, 600, 70, 1);
		pools[2] = new Pool(710, 600, 70, 2);
		//TEMPORARY
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void draw(Graphics g) {	
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawImage(background, 0, 0, this);
		fireboy.destination.draw(g2);
		watergirl.destination.draw(g2);
		for(int i=0;i<5;i++) walls[i].draw(g);
		for(int i=0;i<3;i++) pools[i].draw(g);
		
		fireboy.draw(g2);
		watergirl.draw(g2);
		
		if(gameFinished) levelComplete.draw(g);
	}
	
	public void paint(Graphics g) {
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics); // update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // redraw everything on the screen
	}
	
	public void Reset() {
		playersAlive = true;
		gameFinished = false;
		framesAtFinish = 0;
		framesDead = 0;
		fireboy.resetPosition();
		watergirl.resetPosition();
	}
	
	public void playerCollidesWall() {
		
		for(int i=0;i<walls.length;i++) {
			if(fireboy.x + Player.PlayerWidth > walls[i].x1 && fireboy.x < walls[i].x2) {
				if(fireboy.y + Player.PlayerHeight >= walls[i].y1 && fireboy.y + Player.PlayerHeight <= walls[i].y1 + Player.PlayerHeight/2) {
					fireboy.inAir = false;
					fireboy.yVelocity = 0;
					fireboy.y = walls[i].y1 - Player.PlayerHeight;
				}
				else if(fireboy.y <= walls[i].y2 && fireboy.y >= walls[i].y2 - Player.PlayerHeight/2){
					fireboy.yVelocity = 0;
					fireboy.y = walls[i].y2 + 1;
				}
			}
			
			if(fireboy.y < walls[i].y2 && fireboy.y + Player.PlayerHeight > walls[i].y1) {
				if(fireboy.x + Player.PlayerWidth >= walls[i].x1 && fireboy.x + Player.PlayerWidth <= walls[i].x1 + Player.PlayerWidth/2) {
					fireboy.x = walls[i].x1 - Player.PlayerWidth;
				}				
				else if(fireboy.x <= walls[i].x2 && fireboy.x >= walls[i].x2 - Player.PlayerWidth/2) {
					fireboy.x = walls[i].x2;
				}
			}		
		}
		
		for(int i=0;i<walls.length;i++) {
			if(watergirl.x + Player.PlayerWidth > walls[i].x1 && watergirl.x < walls[i].x2) {
				if(watergirl.y + Player.PlayerHeight >= walls[i].y1 && watergirl.y + Player.PlayerHeight <= walls[i].y1 + Player.PlayerHeight/2) {
					watergirl.inAir = false;
					watergirl.yVelocity = 0;
					watergirl.y = walls[i].y1 - Player.PlayerHeight;
				}
				else if(watergirl.y <= walls[i].y2 && watergirl.y >= walls[i].y2 - Player.PlayerHeight/2){
					watergirl.yVelocity = 0;
					watergirl.y = walls[i].y2 + 1;
				}
			}
			
			if(watergirl.y < walls[i].y2 && watergirl.y + Player.PlayerHeight > walls[i].y1) {
				if(watergirl.x + Player.PlayerWidth >= walls[i].x1 && watergirl.x + Player.PlayerWidth <= walls[i].x1 + Player.PlayerWidth/2) {
					watergirl.x = walls[i].x1 - Player.PlayerWidth;
				}				
				else if(watergirl.x <= walls[i].x2 && watergirl.x >= walls[i].x2 - Player.PlayerWidth/2) {
					watergirl.x = walls[i].x2;
				}
			}		
		}
	}
	
	public void playersAtDoors() {
		if(fireboy.atDestination() && watergirl.atDestination()) {
			framesAtFinish++;
			
			if(framesAtFinish >= 60) {
				gameFinished = true;
			}
		}
		else framesAtFinish = 0;
	}
	
	public void playersInPools() {
		for(int i=0;i<pools.length;i++) {
			if(pools[i].type == 0 || pools[i].type == 1) {
				if(watergirl.x + 8 < pools[i].startX + pools[i].length && watergirl.x + Player.PlayerWidth - 8> pools[i].startX) {
					if(watergirl.y + Player.PlayerHeight == pools[i].startY) 
						playersAlive = false;
				}
			}
			
			if(pools[i].type == 0 || pools[i].type == 2) {
				if(fireboy.x + 8 < pools[i].startX + pools[i].length && fireboy.x + Player.PlayerWidth - 8 > pools[i].startX) {
					if(fireboy.y + Player.PlayerHeight == pools[i].startY)
						playersAlive = false;
				}
			}
		}
	}
	
	public void checkCollision() {
		playerCollidesWall();
		playersInPools();
		playersAtDoors();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!gameFinished && playersAlive) {
			fireboy.keyPressed(e);
			watergirl.keyPressed(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!gameFinished && playersAlive) {
			fireboy.keyReleased(e);
			watergirl.keyReleased(e);
		}
	}
	
	public void move() {
		fireboy.move();
		watergirl.move();
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
				if(!playersAlive) framesDead++;
				if(framesDead >= 6) Reset();
				if(!gameFinished && playersAlive) move();
				checkCollision();
				repaint();
				delta--;
			}
		}
		
	}
}
