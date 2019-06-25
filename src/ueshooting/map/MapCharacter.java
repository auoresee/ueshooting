package ueshooting.map;

import java.awt.image.BufferedImage;

import ueshooting.system.SystemMain;

public class MapCharacter implements GameObject {
	protected String chipname;
	protected BufferedImage chips;
	int x_halftile,y_halftile;
	public int call_type = 0;	//call by player's touch
	
	public void load_image(BufferedImage image){
		
	}
	public int getX_int(){
		return x_halftile * SystemMain.tile_size / 2;
	}
	
	public int getY_int(){
		return y_halftile * SystemMain.tile_size / 2;
	}
}
