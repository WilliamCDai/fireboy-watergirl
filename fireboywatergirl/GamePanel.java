package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.io.File;
import java.util.*;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	static BufferedReader br;
	static StringTokenizer st;
	
	static String next () throws IOException {
		while (st == null || !st.hasMoreTokens())
			st = new StringTokenizer(br.readLine().trim());
		return st.nextToken();
	}
	static int readInt () throws IOException {
		return Integer.parseInt(next());
	}
	static char readChar () throws IOException {
		return next().charAt(0);
	}
	
	public Image image;
	public Graphics graphics;
	public Thread gameThread;
	public Image background;
	
	public static final int GAME_WIDTH = 1080;
	public static final int GAME_HEIGHT = 720;
	
	int level = 0;
	Wall walls[];
	Pool pools[];
	Instruction levelComplete, returnToHome;
	Instruction[] levels = new Instruction[2];	
	Player fireboy, watergirl;
	
	public boolean gameFinished, playersAlive;
	public int framesAtFinish, framesDead;
	public boolean inGame, inHome, levelLoaded;
	int unlockedTo;

	public GamePanel() throws IOException{
		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		
		levelComplete = new Instruction(400, 300, "Level Complete!");
		returnToHome = new Instruction(480, 500, "Return", 450, 440, 630, 530, true);
		levels[0] = new Instruction(340, 450, "Level 1", 310, 390, 490, 480, true);
		//levels[1] = new Instruction(480, 450, "Level 2", 450, 390, 630, 480, false);
		levels[1] = new Instruction(620, 450, "Level 2", 590, 390, 770, 480, false);
		
		unlockedTo = 1;
		inGame = false;
		inHome = true;
		
		background = new ImageIcon("src/Images/Background.jpeg").getImage();
		
		walls = new Wall[4];
		walls[0] = new Wall(0, 0, 1080, 30);
		walls[1] = new Wall(0, 0, 30, 720);
		walls[2] = new Wall(0, 690, 1080, 720);
		walls[3] = new Wall(1050, 0, 1080, 720);
		
		addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(inHome) {
                	try {
	                	if(levels[0].clicked(e)) { loadLevel(1);}
	                	else if(levels[1].clicked(e)) loadLevel(2);
	                	//else if(levels[2].clicked(e)) loadLevel(3);
                	} catch(Exception ex) {
                		System.out.println(ex);
                	}
                }
                else if(inGame && gameFinished) {
                	if(returnToHome.clicked(e)) {
                		inHome = true;
                		inGame = false;
                		unlockedTo = level + 1;
                		if(unlockedTo <= 3) levels[unlockedTo-1].unlocked = true;
                		levelLoaded = false;
                	}
                }
            }
        });
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void draw(Graphics g) {	
		Graphics2D g2 = (Graphics2D)g;
		
		g2.drawImage(background, 0, 0, this);
		
		for(int i =0; i<4;i++) walls[i].draw(g);
		
		if(inGame && levelLoaded) {
			for(int i=4;i<walls.length;i++) walls[i].draw(g);
			for(int i=0;i<pools.length;i++) pools[i].draw(g);
			
			fireboy.destination.draw(g2);
			watergirl.destination.draw(g2);
			fireboy.draw(g2);
			watergirl.draw(g2);
			
			if(gameFinished) {
				g.setColor(new Color(150,150,150,255));
				g.fillRect(350, 220, 380, 400);
				levelComplete.draw(g);
				returnToHome.draw(g);
			}
		}
		else if(inHome) {
			for(int i=0;i<2;i++) levels[i].draw(g);
		}
	}
	
	public void paint(Graphics g) {
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics); // update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // redraw everything on the screen
	}
	
	public void loadLevel(int level) throws IOException{	
		this.level = level;
		
		inHome = false;
		inGame = true;

		if(level == 1) br = new BufferedReader(new FileReader("src/LevelFiles/Level1.txt"));
		else if(level == 2) br = new BufferedReader(new FileReader("src/LevelFiles/Level2.txt"));
		//else if(level == 3) br = new BufferedReader(new FileReader("src/LevelFiles/Level.txt"));
		
		walls = new Wall[readInt()];
		pools = new Pool[readInt()];
		for(int i=0;i<walls.length;i++) walls[i] = new Wall(readInt(), readInt(), readInt(), readInt());
		for(int i=0;i<pools.length;i++) pools[i] = new Pool(readInt(), readInt(), readInt(), readInt());
		fireboy = new Player('w', 'a', 'd', readInt(), readInt(), Color.red, readInt(), readInt());
		watergirl = new Player((char)38, (char)37, (char)39, readInt(), readInt(), Color.blue, readInt(), readInt());
		
		//TEMPORARY
		/*walls[0] = new Wall(0, 0, 1080, 30);
		walls[1] = new Wall(0, 0, 30, 720);
		walls[2] = new Wall(0, 690, 1080, 720);
		walls[3] = new Wall(1050, 0, 1080, 720);
		walls[4] = new Wall(30, 600, 1000, 630);
		fireboy = new Player('w', 'a', 'd', 100, 100, Color.red, 400, 540);
		watergirl = new Player((char)38, (char)37, (char)39, 200, 100, Color.blue, 500, 540);
		pools[0] = new Pool(550, 600, 70, 0);
		pools[1] = new Pool(630, 600, 70, 1);
		pools[2] = new Pool(710, 600, 70, 2);*/
		levelLoaded = true;
		Reset();
		//TEMPORARY
		
		
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
	
	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		if(inGame) {
			if(!gameFinished && playersAlive) {
				fireboy.keyPressed(e);
				watergirl.keyPressed(e);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if(inGame) {
			if(!gameFinished && playersAlive) {
				fireboy.keyReleased(e);
				watergirl.keyReleased(e);
			}
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
				if(inGame && levelLoaded) {
					if(!playersAlive) framesDead++;
					if(framesDead >= 6) Reset();
					if(!gameFinished && playersAlive) move();
					checkCollision();
				}
				repaint();
				delta--;
			}
		}
		
	}
}
