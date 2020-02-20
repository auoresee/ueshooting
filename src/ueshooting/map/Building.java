/*
 * This class is obsolete and no longer used.
 */

package ueshooting.map;

import java.util.*;

import ueshooting.system.SystemMain;

public class Building implements GameObject {
	public static List<BuildingInformation> info = new ArrayList<BuildingInformation>();
	public BuildingCategory category;
	public int type;
	public int belong = 0;
	int level;
	
	int status;
	int value;
	int time = 0;
	
	int x_tilepos;
	int y_tilepos;
	int x_size;
	int y_size;
	
	public Building(int p_x_tilepos,int p_y_tilepos){
		category = BuildingCategory.NONE;
		type = 0;	//air
		level = 0;
		x_tilepos = p_x_tilepos;
		y_tilepos = p_y_tilepos;
		x_size = 1;
		y_size = 1;
		initinfo();
	}
	
	public Building(int p_type,int p_x_tilepos,int p_y_tilepos){
		category = BuildingCategory.NONE;
		type = p_type;	//air
		level = 0;
		x_tilepos = p_x_tilepos;
		y_tilepos = p_y_tilepos;
		x_size = 1;
		y_size = 1;
		initinfo();
	}
	
	void action(Map map){
		time++;
	}
	
	boolean isAir(){
		if((category == BuildingCategory.NONE) && (type == 0)){
			return true;
		}else{
			return false;
		}
	}
	
	private void initinfo() {
		info.add(new BuildingInformation("Air",1,1,0));
	}

	//public BuildingCategory getCategory() {
	//	return category;
	//}

	//public void setCategory(BuildingCategory p_category) {
	//	category = p_category;
	//}

	//public int getType() {
	//	return type;
	//}

	//public void setType(int p_type) {
	//	type = p_type;
	//}
	
	public int getX_int(){
		return x_tilepos * SystemMain.tile_size + SystemMain.tile_size / 2;
	}
	
	public int getY_int(){
		return y_tilepos * SystemMain.tile_size + SystemMain.tile_size / 2;
	}

	public static Building get_new_instance(BuildingCategory category,int type,int p_x_tilepos,int p_y_tilepos) {
		switch(category){
		case NONE:
			return new Building(type,p_x_tilepos,p_y_tilepos);
		case HOUSE:
			return new House(type,p_x_tilepos,p_y_tilepos);
		case BUSINESS:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
			
		case INDUSTRY:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
		
		case WAY:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
			
		case TRANSPORT:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
			
		case PUBLIC:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
			
		case MILLITARY:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
			
		case NATURE:
			//return new Building(type,p_x_tilepos,p_y_tilepos);
			break;
		}
		return null;
	}
}

class BuildingInformation {
	String name;
	int x_tilesize;
	int y_tilesize;
	int max_level;
	boolean isHaveOrientation;
	BuildingInformation(String p1,int p2,int p3,int p4,boolean p5){
		name = p1;
		x_tilesize = p2;
		y_tilesize = p3;
		max_level = p4;
		isHaveOrientation = p5;
	}
	BuildingInformation(String p1,int p2,int p3,int p4){
		this(p1,p2,p3,p4,false);
	}
	BuildingInformation(String p1){
		this(p1,1,1,1,false);
	}
}