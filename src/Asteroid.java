import javax.swing.*;

import java.awt.*;
import java.util.Random;
public class Asteroid extends GameObject{
	private int size,health;
	private ImageIcon imgExplosion;
	public final static int SMALL=0,MEDIUM=1,LARGE=2;
	
	public Asteroid(){
		Random r=new Random();
		size=r.nextInt(3);
		health=size+1;
		setImage(new ImageIcon("images\\asteroid"+size+".png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
		imgExplosion=new ImageIcon("images\\explosion.png");
		setX(1280);
		setY(r.nextInt(720-getHeight()));
	}
	
	public Asteroid(int x,int y, int s){
		setX(x);
		setY(y);
		size=s;
		setImage(new ImageIcon("images\\asteroid"+size+"png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
		imgExplosion=new ImageIcon("images\\explosion.png");
	}
	public void move(){
		setX(getX()-3);
	}
	public void draw(Graphics2D g2){
		if(health>0)
			super.draw(g2);
		else
			g2.drawImage(imgExplosion.getImage(),getX(),getY(),null);
	}
	
	public ImageIcon getImage(){
		if(health==0)
			setImage(imgExplosion);
		return super.getImage();
	}
	
	public int getSize(){
		return size;
	}
	
	public int getHealth(){
		return health;
	}
	public void setSize(int s){
		size=s;
		setImage(new ImageIcon("images\\asteroid"+size+".png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
	}
	
	public void setHealth(int h){
		health=h;
	}
	
}
