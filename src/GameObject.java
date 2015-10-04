import javax.swing.*;
import java.awt.*;
public abstract class GameObject {
	private int xPos,yPos,width,height;
	private ImageIcon imgObject;
	
	public GameObject(){
		
	}
	
	public abstract void move();
	public void draw(Graphics2D g2){
		g2.drawImage(imgObject.getImage(),xPos,yPos,null);
	}
	
	public int getX(){
		return xPos;
	}
	
	public int getY(){
		return yPos;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public ImageIcon getImage(){
		return imgObject;
	}
	
	public void setPosition(int x,int y){
		xPos=x;
		yPos=y;
	}
	
	public void setX(int x){
		xPos=x;
	}
	
	public void setY(int y){
		yPos=y;
	}
	
	public void setWidth(int w){
		width=w;
	}
	
	public void setHeight(int h){
		height=h;
	}
	
	public void setImage(ImageIcon i){
		imgObject=i;
	}
	
	public Rectangle getRect(){
		return new Rectangle(xPos,yPos,width,height);
	}
	
}
