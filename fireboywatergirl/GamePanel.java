/* Fireboy and Watergirl
 * William Dai and Amanda Wu
 * 
 * GamePanel class is extension of JPanel
 * It contains all the elements of the game as well as runs the collisions and menu systems
 */

package fireboywatergirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// This section is standard code for bufferedreader to read integers
	// It's William's code, he uses it for other projects
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
	
	// image and graphics are for painting the screen
	public Image image;
	public Graphics graphics;
	public Thread gameThread; // thread for running the game so game can do many things at once
	public Image background, title, pauseButtonIcon; // pictures of background, title, and pausebutton
	
	// dimensions of panel
	public static final int GAME_WIDTH = 1080;
	public static final int GAME_HEIGHT = 720;
	
	
	int level = 0; // start at lvl 0, doesn't really mean anything, will be updated later
	Wall walls[]; // all the walls
	Pool pools[]; // all the pools
	// All the instructions (which are all the buttons as well)
	Instruction levelComplete, returnToHome, retry, resume;
	Instruction[] levels = new Instruction[2];	
	Instruction pauseButton, instructions;
	Player fireboy, watergirl; // the 2 players
	
	// Condition flag variables
	public boolean gameFinished, playersAlive; // booleans for is the level done and are the players alive
	public int framesAtFinish, framesDead; // how long have they been at destinations or how long have they been dead
	public boolean inGame, inHome, gamePaused, showingInstructions; // all booleans for which screen to show
	int unlockedTo; // what level have the unlocked to
	boolean levelLoaded; // stores whether or not a map has been loaded

	public GamePanel() throws IOException{
		this.setFocusable(true); // make everything in this class appear on the screen
		this.addKeyListener(this); // start listening for keyboard input

		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		
		// Initialize all the instructions
		// they're just rectangles with text in them so the numbers are just coordinates
		levelComplete = new Instruction(400, 300, "Level Complete!");
		returnToHome = new Instruction(480, 550, "Return", 450, 490, 630, 580, true);
		retry = new Instruction(490, 430, "Retry", 450, 370, 630, 460, true);
		resume = new Instruction(468, 310, "Resume", 450, 250, 630, 340, true);
		levels[0] = new Instruction(340, 450, "Level 1", 310, 390, 490, 480, true);
		levels[1] = new Instruction(620, 450, "Level 2", 590, 390, 770, 480, false);
		pauseButton = new Instruction(1000, 50, " ", 1000, 50, 1030, 80, true);
		instructions = new Instruction(500, 600, "Help", 450, 540, 630, 630, true);
		
		// Set game beginning conditions
		unlockedTo = 1; // level 1 is unlocked
		inGame = false; // not in the game yet
		inHome = true; // is in home screen
		showingInstructions = false; // not showing instructions
		
		// make images for background, title, and pausebutton
		background = new ImageIcon("src/Images/Background.jpeg").getImage();
		title = new ImageIcon("src/Images/Title.png").getImage();
		pauseButtonIcon = new ImageIcon("src/Images/PauseButton.png").getImage();
		
		// Make border (they're walls)
		walls = new Wall[4];
		walls[0] = new Wall(0, 0, 1080, 30);
		walls[1] = new Wall(0, 0, 30, 720);
		walls[2] = new Wall(0, 690, 1080, 720);
		walls[3] = new Wall(1050, 0, 1080, 720);
		
		// Add a mouse event listener
		addMouseListener(new MouseAdapter() {
			// mousePressed is called whenever a mouse is clicked
            public void mousePressed(MouseEvent e) {
            	// This method has everything to do on every possible screen
                if(inHome && !showingInstructions) { // if in home screen and not showing instructions
                	try {
                		// if a level button is clicked, load that level
	                	if(levels[0].clicked(e)) { loadLevel(1);}
	                	else if(levels[1].clicked(e)) loadLevel(2);
                	} catch(Exception ex) {
                		System.out.println(ex);
                	}
                	if(instructions.clicked(e)) { // if help button is clicked
                		showingInstructions = true; // go to show instruction screen
                	}
                }
                else if(showingInstructions) { // if we are showing instructions
                	if(returnToHome.clicked(e)) {
                		showingInstructions = false; // stop showing instructions if return is clicked
                	}
                }
                else if(inGame && gameFinished) { // if we are in game and the level is complete
                	if(returnToHome.clicked(e)) { // if return button is clicked
                		// Go to homescreen and unlock next level
                		inHome = true;
                		inGame = false;
                		unlockedTo = level + 1;
                		if(unlockedTo <= 2) levels[unlockedTo-1].unlocked = true;
                		levelLoaded = false; // Now there is no level loaded
                	}
                }
                else if(inGame && !gamePaused) { // if game is playing and player is not paused
                	if(pauseButton.clicked(e)) { // if pause button is clicked
                		gamePaused = true; // pause game
                	}
                }
                else if(inGame && gamePaused) { // if game is paused
                	if(returnToHome.clicked(e)) { // if return is clicked
                		// go to home screen
                		inHome = true;
                		inGame = false;
                	}
                	else if(resume.clicked(e)) { // if resume if clicked
                		// stop pausing game
                		gamePaused = false;
                	}
                	else if(retry.clicked(e)) { // if retry is clicked
                		// reset and resume the game
                		gamePaused = false;
                		Reset();
                	}
                }
            }
        });
		
		// Code to start the thread so that computer can do multiple things at once (call the run() function)
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	// draw function is called by paint and draw eveything onto graphics
	public void draw(Graphics g) {	
		Graphics2D g2 = (Graphics2D)g; // make 2d graphics to draw images
		
		g2.drawImage(background, 0, 0, this); // draw background
		
		for(int i =0; i<4;i++) walls[i].draw(g); // always draw border
		
		if(inGame && levelLoaded) { // if we are in game and a level has been loaded (level needs to be loaded to be drawn)
			for(int i=4;i<walls.length;i++) walls[i].draw(g); // draw all walls
			for(int i=0;i<pools.length;i++) pools[i].draw(g); // draw all pools
			
			// draw the 2 players and their corresponding doors
			fireboy.destination.draw(g2);
			watergirl.destination.draw(g2);
			fireboy.draw(g2);
			watergirl.draw(g2);
			
			// draw pause button (clickable rectangle as well as image)
			pauseButton.draw(g);
			g2.drawImage(pauseButtonIcon, 1000, 50, this);
			
			if(gameFinished) { // if game is complete
				// Draw level complete message and draw return button
				g.setColor(new Color(150,150,150,255));
				g.fillRect(350, 220, 380, 400);
				levelComplete.draw(g);
				returnToHome.draw(g);
			}
			else if(gamePaused) { // if game is paused
				g.setColor(new Color(150,150,150,255));
				g.fillRect(350, 220, 380, 400);
				// draw retru, resume, and return buttons
				retry.draw(g);
				resume.draw(g);
				returnToHome.draw(g);
			}
		}
		else if(inHome && !showingInstructions) { // if we are in home screen and not showing instructions
			g2.drawImage(title, 180, 150, this); // draw the title
			for(int i=0;i<2;i++) levels[i].draw(g); // draw the level buttons
			instructions.draw(g); // draw the "help" button
		}
		else if(showingInstructions) { // if we are showing instructions
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.setColor(Color.white);
			// draw all the instructions
			g.drawString("Use W.A.D to move fireboy", 350, 180);
			g.drawString("Use arrow keys to move watergirl", 310, 240);
			g.drawString("Never mix fire and water", 370, 300);
			g.drawString("Acid will hurt them both", 380, 360);
			g.drawString("Get them to their exit doors", 350, 420);
			returnToHome.draw(g); // draw return button
		}
	}
	
	public void paint(Graphics g) {
		image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
		graphics = image.getGraphics();
		draw(graphics); // update the positions of everything on the screen
		g.drawImage(image, 0, 0, this); // redraw everything on the screen
	}
	
	// load level is called when a level button is pressed, it loads a level
	public void loadLevel(int level) throws IOException{	
		this.level = level; // set current level
		
		// move to in game mode instead of home screen
		inHome = false;
		inGame = true;

		// Choose which level file to read from and feed it to buffered reader
		if(level == 1) br = new BufferedReader(new FileReader("src/LevelFiles/Level1.txt"));
		else if(level == 2) br = new BufferedReader(new FileReader("src/LevelFiles/Level2.txt"));
		
		// readInt() automatically reads next integer in text file
		walls = new Wall[readInt()]; // initialize walls length
		pools = new Pool[readInt()]; // initialize pools array length
		// for each wall and pool, read its corresponding attributes
		for(int i=0;i<walls.length;i++) walls[i] = new Wall(readInt(), readInt(), readInt(), readInt());
		for(int i=0;i<pools.length;i++) pools[i] = new Pool(readInt(), readInt(), readInt(), readInt());
		// Read starting coordinates for players and pass on the control buttons and type
		fireboy = new Player('w', 'a', 'd', readInt(), readInt(), Color.red, readInt(), readInt());
		watergirl = new Player((char)38, (char)37, (char)39, readInt(), readInt(), Color.blue, readInt(), readInt());

		levelLoaded = true; // a level is now loaded
		Reset(); // reset everything for good measure
	}
	
	// reset is called when players die and resets everything in the level
	public void Reset() {
		// players and now alive and game is playing
		playersAlive = true;
		gameFinished = false;
		// players are not dead now finished the game
		framesAtFinish = 0;
		framesDead = 0;
		gamePaused = false; // game is playing
		// reset the 2 player's position and velocity
		fireboy.resetPosition();
		watergirl.resetPosition();
	}
	
	// This checks if player collides walls and deals with it correspondingly
	public void playerCollidesWall() {
		
		for(int i=0;i<walls.length;i++) { // for each wall
			if(fireboy.x + Player.PlayerWidth > walls[i].x1 && fireboy.x < walls[i].x2) { // if fireboy is above or below the walls (x coordinates are within left and right bounds)
				if(fireboy.y + Player.PlayerHeight >= walls[i].y1 && fireboy.y + Player.PlayerHeight <= walls[i].y1 + Player.PlayerHeight/2) { // if fireboy is above and intersecting wall
					// fireboy is on the wall, so not in air. fireboy's yvelocity is 0 as they are on this wall, move fireboy to be on the wall
					fireboy.inAir = false;
					fireboy.yVelocity = 0;
					fireboy.y = walls[i].y1 - Player.PlayerHeight;
				}
				else if(fireboy.y <= walls[i].y2 && fireboy.y >= walls[i].y2 - Player.PlayerHeight/2){ // if fireboy is below and intersecting wall
					// fireboy now has no yvelocity (bumped head) and move fireboy directly below wall
					fireboy.yVelocity = 0;
					fireboy.y = walls[i].y2 + 1;
				}
			}
			
			// if fireboy to the left or right of wall (y coordinates are in bounds)
			if(fireboy.y < walls[i].y2 && fireboy.y + Player.PlayerHeight > walls[i].y1) { // if fireboy is to the left and intersecting the wall
				if(fireboy.x + Player.PlayerWidth >= walls[i].x1 && fireboy.x + Player.PlayerWidth <= walls[i].x1 + Player.PlayerWidth/2) {
					fireboy.x = walls[i].x1 - Player.PlayerWidth; // move fireboy to directly left of wall
				}				
				else if(fireboy.x <= walls[i].x2 && fireboy.x >= walls[i].x2 - Player.PlayerWidth/2) { // if fireboy is to the right and intersecting the wall 
					fireboy.x = walls[i].x2; // move fireboy to right of wall
				}
			}		
		}
		
		// the following is collisions of walls with watergirl, the logic is exactly the same as above for fireboy
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
	
	// this checks if both players are at there doors
	public void playersAtDoors() {
		if(fireboy.atDestination() && watergirl.atDestination()) { // if players are both at goal
			framesAtFinish++; // add one frame to how long theyve spent at goal
			
			if(framesAtFinish >= 60) { // once they reach 60 consecutive frames at goal
				gameFinished = true; // they finish the level
			}
		}
		else framesAtFinish = 0; // if not at goal, then frames is 0
	}
	
	// this checks if a player is in a pool
	public void playersInPools() {
		for(int i=0;i<pools.length;i++) { // for each pool
			if(pools[i].type == 0 || pools[i].type == 1) { // if pool is acid or lava
				if(watergirl.x + 8 < pools[i].startX + pools[i].length && watergirl.x + Player.PlayerWidth - 8> pools[i].startX) { // if watergirl is over the pool
					if(watergirl.y + Player.PlayerHeight == pools[i].startY) // if watergirl is standing on the pool
						playersAlive = false; // kill players
				}
			}
			
			if(pools[i].type == 0 || pools[i].type == 2) { // if pool is acid or water
				if(fireboy.x + 8 < pools[i].startX + pools[i].length && fireboy.x + Player.PlayerWidth - 8 > pools[i].startX) { // if fireboy is over the pool
					if(fireboy.y + Player.PlayerHeight == pools[i].startY) // if fireboy is standing on the pool
						playersAlive = false; // kill players
				}
			}
		}
	}
	
	// this is called by run to check for collisions
	public void checkCollision() {
		// it checks collisions with walls, pools, and doors in that order
		playerCollidesWall();
		playersInPools();
		playersAtDoors();
	}
	
	public void keyTyped(KeyEvent e) {
		
	}
	
	// called when a key is pressed
	public void keyPressed(KeyEvent e) {
		if(inGame && !gamePaused) { // only accept inputs if in game and game is not paused
			if(!gameFinished && playersAlive) { // only pass inputs if game is not done and players are alive
				// pass inputs to both players
				fireboy.keyPressed(e);
				watergirl.keyPressed(e);
			}
		}
	}

	// called when a key is released
	public void keyReleased(KeyEvent e) {
		if(inGame && !gamePaused) { // only accept inputs if in game and game is not paused
			if(!gameFinished && playersAlive) { // only pass inputs if game is not done and players are alive
				// pass inputs to both players
				fireboy.keyReleased(e);
				watergirl.keyReleased(e);
			}
		}
	}
	
	// called to update positions of players
	public void move() {
		// update positions of both players
		fireboy.move();
		watergirl.move();
	}
	
	// called by the gamethread continuously
	@Override
	public void run() { 
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			// this section determines how long since the last frame
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) { // is it's been long enough
				if(inGame && !gamePaused && levelLoaded) { // is game is going on and not paused and map is loaded
					if(!playersAlive) framesDead++; // update frames dead if players are dead
					if(framesDead >= 6) Reset(); // once players have been dead for 6 frames, reset game
					if(!gameFinished && playersAlive) move(); // if game is not finished and players are alive, move players
					checkCollision(); // check for collision
				}
				repaint(); // redraw everything
				delta--;
			}
		}
		
	}
}