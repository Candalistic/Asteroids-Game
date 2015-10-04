import javax.swing.*;

import java.awt.*;
public class Laser extends GameObject{
	Laser(){
		setX(0);
		setY(0);
		setImage(new ImageIcon("images\\laserbeam.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
	}
	
	Laser(int x,int y){
		setX(x);
		setY(y);
		setImage(new ImageIcon("images\\laserbeam.png"));
		setWidth(getImage().getIconWidth());
		setHeight(getImage().getIconHeight());
	}
	
	public void move(){
		setX(getX()+1);
	}
	
	public void move(int x){
		setX(getX()+x);
	}
	
}
