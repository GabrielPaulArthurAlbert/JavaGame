package main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Player {

	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private int width;
	private int height;
	
	private boolean horiz;
	private boolean vert;
	private boolean rotplus;
	private boolean rotmoins;
	
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private double moveSpeed;
	private double maxSpeed;
	private double stopSpeed;
	
	private TileMap tileMap;
	
	private boolean topLeft;
	private boolean topRight;
	private boolean bottomLeft;
	private boolean bottomRight;
	
	float angle = (float)(Math.atan2(x, y));
	
	Image carhoriz;
	Image carvertical;
	Image carrotm;
	Image carrotp;
	
	public Player(TileMap tm) {
		
		tileMap = tm;
		
		width = 20;
		height = 20;
		
		moveSpeed = 0.6;
		maxSpeed = 4.2;
		stopSpeed = 0.30;
		
		//Chargement Image
		
		ImageIcon j = new ImageIcon("ressources/carhoriz.png");
		carhoriz = j.getImage();
		
		ImageIcon k = new ImageIcon("ressources/carvertical.png");
		carvertical = k.getImage();
		
		ImageIcon l = new ImageIcon("ressources/carrotmoins.png");
		carrotm = l.getImage();
		
		ImageIcon m = new ImageIcon("ressources/carrotplus.png");
		carrotp = m.getImage();
		
	}
	
	public void setx(int i) { x = i; }
	public void sety(int i) { y = i; }
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	
	private void calculateCorners(double x, double y) {
		int leftTile = tileMap.getColTile((int)(x - width / 2));
		int rightTile = tileMap.getColTile((int)(x + width / 2) - 1);
		int topTile = tileMap.getRowTile((int)(y - height / 2));
		int bottomTile = tileMap.getRowTile((int)(y + height / 2) - 1);
		topLeft = tileMap.isBlocked(topTile, leftTile);
		topRight = tileMap.isBlocked(topTile, rightTile);
		bottomLeft = tileMap.isBlocked(bottomTile, leftTile);
		bottomRight= tileMap.isBlocked(bottomTile, rightTile);
	}
	
	////////////////////////////////////////////////
	public void update() {
		
		//determiner position suivante
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		if(up) {
			dy -= moveSpeed;
			if(dy < -maxSpeed){
				dy = -maxSpeed;
			}
		}
		else if(down) {
			dy += moveSpeed;
			if(dy > maxSpeed){
				dy = maxSpeed;
			}
		}
		else {
			if(dy > 0) {
				dy -= stopSpeed;
				if(dy < 0) {
					dy = 0;
				}
			}
			else if(dy < 0) {
				dy += stopSpeed;
				if(dy > 0) {
					dy = 0;
				}
			}
		}
		
		//check collisions
		
		int currCol = tileMap.getColTile((int) x);
		int currRow = tileMap.getRowTile((int) y);
		
		double tox = x + dx;
		double toy = y + dy;
		
		double tempx = x;
		double tempy = y;
		
		calculateCorners(x, toy);
		if(dy < 0)
			if(topLeft || topRight ) {
				dy = 0;
				//tempy = currRow * tileMap.getTileSize() + height /2;
			}
			else {
				tempy += dy;
			}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				//tempy = (currRow + 1)* tileMap.getTileSize() - height /2;
			}
			else {
				tempy += dy;
			}
		}
		
		calculateCorners(tox, y);
		if(dx < 0)
			if(topLeft || bottomLeft ) {
				dx = 0;
			}
			else {
				tempx += dx;
			}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
			}
			else {
				tempx += dx;
			}
		}
		x = tempx;
		y = tempy;
		
		//move the map
		tileMap.setx((int) (GamePanel.WIDTH / 2 - x));
		tileMap.sety((int) (GamePanel.HEIGHT / 2 - y));
		
		
	
		//move animation
		if(!down & !up & (!left & right | left & right | left & !right) | down & up & (!left & right | left & !right)) {
			
			horiz = true;
			vert = false;
			rotplus = false;
			rotmoins = false;
		}
		else if(!left & !right & (!down & up | down & !up | down & up) | left & right & (!down & up | down & !up)) {
			
			horiz = false;
			vert = true;
			rotplus = false;
			rotmoins = false;
		}
		else if(!left & right & !down & up | left & !right & down & !up){
			
			horiz = false;
			vert = false;
			rotplus = true;
			rotmoins = false;
		}
		else if(!left & right & down & !up | left & !right & !down & up){
			
			horiz = false;
			vert = false;
			rotplus = false;
			rotmoins = true;
		}	
	}
	

	public void draw(Graphics2D g) {
		
		int tx = tileMap.getx();
		int ty = tileMap.gety();
		
		//float angle = (float)(Math.atan2(dx, dy));
				  
        g.setColor(Color.RED);
        
		if(horiz) {
			g.drawImage(
				carhoriz,
				(int) (tx + x - width / 2),
				(int) (ty + y - height / 2),
				null
			);
		}
		else if(vert) {
			g.drawImage(
				carvertical,
				(int) (tx + x - width / 2),
				(int) (ty + y - height / 2),
				null
			);
		}
		else if(rotplus) {
			g.drawImage(
				carrotp,
				(int) (tx + x - width / 2),
				(int) (ty + y - height / 2),
				null
			);
		}
		else if(rotmoins) {
			g.drawImage(
				carrotm,
				(int) (tx + x - width / 2),
				(int) (ty + y - height / 2),
				null
			);
		}
	}
}
