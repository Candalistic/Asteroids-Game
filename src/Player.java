import javax.swing.*;
import java.awt.*;

public class Player extends GameObject {
	private int lives;
	private ImageIcon imgExplosion;
	private boolean isDead;
	public Player(){
		setX(5);
		setY(200);
		lives=3;
		isDead=false;
		imgExplosion=new ImageIcon("images\\exploded_ship.png");
		setImage(new ImageIcon("images\\spaceship.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
	}
	
	public Player(int x,int y){
		super();
		setX(x);
		setY(y);
		lives=3;
		isDead=false;
		imgExplosion=new ImageIcon("images\\explosion.png");
		setImage(new ImageIcon("images\\spaceship.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
	}
	
	public void move(){
		
	}
	public void draw(Graphics2D g2){
		if(!isDead)
			super.draw(g2);
		else
			g2.drawImage(imgExplosion.getImage(),getX(),getY(),null);
	}
	
	public int getLives(){
		return lives;
	}
	
	public ImageIcon getImage(){
		if(isDead){
			return imgExplosion;
		}
		else{
			return super.getImage();
		}
	}
	
	public boolean getDead(){
		return isDead;
	}
	
	public void setLives(int l){
		lives=l;
	}
	
	public void setDead(boolean d){
		isDead=d;
	}
}
