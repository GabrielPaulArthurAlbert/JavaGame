package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	// dimensions
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	
	// game thread
	private Thread thread;
	private boolean running;
	
	private int FPS = 30;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	private TileMap tileMap;
	private Player player;

	public GamePanel() {
		
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	
	private void init() {
		
		running = true;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		tileMap = new TileMap("ressources/tilemap1.txt", 32);
		tileMap.loadTiles("ressources/tileset3.png");
		
		player = new Player(tileMap);
		player.setx(50);
		player.sety(50);
	}
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			render();
			drawToScreen();
			
			elapsed = (System.nanoTime() - start) / 1000000;
			
			wait = targetTime - elapsed;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
			}
			
		}
		
	}
	
	private void update() {
		
		tileMap.update();
		player.update();
		
	}
	private void render() {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		tileMap.draw(g);
		player.draw(g);
		
	}
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if(code == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if(code == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		if(code == KeyEvent.VK_UP) {
			player.setUp(true);
		}
	}
	public void keyReleased(KeyEvent key) {
int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if(code == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if(code == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		if(code == KeyEvent.VK_UP) {
			player.setUp(false);
		}
	
	}

}