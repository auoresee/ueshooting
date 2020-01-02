package ueshooting.sprite;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ueshooting.map.Building;
import ueshooting.map.GameObject;
import ueshooting.map.Map;

public abstract class Sprite implements GameObject {
	protected static String dir_name = "";
	protected static HashMap<SpriteCategory,String> chip_names = new HashMap<SpriteCategory,String>();
	protected static HashMap<SpriteCategory,ArrayList<Image>> chips = new HashMap<SpriteCategory,ArrayList<Image>>();
	protected SpriteCategory category = SpriteCategory.NONE;
	protected int type;
	protected int subType;
	protected double x,y,z;
	public double rotation;
	public double x_speed,y_speed;
	public int time;
	protected int xsize = 32,ysize = 32;
	public double collision_range = 16;
	public int belong;	//0:Player side
	public boolean exist = true;
	public boolean visiblity = false;
	public int skip_frame = 0;
	protected boolean action_flag = false;
	protected boolean move_flag = false;
	protected boolean collision_flag = false;
	public boolean collision_building = false;
	public boolean flash = false;
	
	public static void init(){
		chip_names.put(SpriteCategory.SHOT, "shot");
		chip_names.put(SpriteCategory.NPC, "mob");
		chip_names.put(SpriteCategory.OTHER, "other");
		chips.put(SpriteCategory.SHOT, new ArrayList<Image>());
		chips.put(SpriteCategory.NPC, new ArrayList<Image>());
		chips.put(SpriteCategory.OTHER, new ArrayList<Image>());
	}
	
	public void collision_check(Map map,Sprite sprite,double distance){
		if(distance < collision_range)return;
		collision(map,sprite);
	}
	
	public abstract void collision(Map map,Sprite sprite);
	
	public void collision(Map map,Building building) {}
	
	public abstract void action(Map map);
	
	public boolean move(){
		if(!move_flag)return false;
		x += x_speed;
		y += y_speed;
		return true;
	}
	
	protected void setSize() {
		List<Image> list = chips.get(category);
		Image image = list.get(type);
		xsize = image.getWidth(null);
		ysize = image.getHeight(null);
	}
	
	public static String getChipPath(SpriteCategory category,int type){
		return "sprite\\" + chip_names.get(category) + "\\" + chip_names.get(category) + "_" + String.valueOf(type) + ".png";
	}
	
	public Image getChip(SpriteCategory p_category){
		return chips.get(p_category).get(type);
	}
	
	public static void setChip(Image image,SpriteCategory p_category){
		chips.get(p_category).add(image);
	}
	
	public static void setChips(ArrayList<Image> p_chips,SpriteCategory p_category){
		chips.put(p_category,p_chips);
	}
	
	public void setCollisionFlag(boolean arg){collision_flag = arg;}
	
	//intÇÕï`âÊèàóùóp
	public int getX_int(){
		return (int)Math.round(x);
	}
	
	public int getY_int(){
		return (int)Math.round(y);
	}
	
	public int getZ_int(){
		return (int)Math.round(z);
	}
	
	public double getX_double(){
		return x;
	}
	
	public double getY_double(){
		return y;
	}
	
	public double getZ_double(){
		return z;
	}
	
	public int getXSize(){
		return xsize;
	}
	
	public int getYSize(){
		return ysize;
	}

	public Image getChip() {
		//System.out.printf("%s %s\n",category.name(),this.toString());
		return chips.get(category).get(type);
	}

	//only used in Enemy
	public void update(Map map) {}

	public void setX(double src) {
		x = src;
	}
	
	public void setY(double src) {
		y = src;
	}
}