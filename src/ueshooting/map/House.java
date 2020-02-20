/*
 * This class is obsolete and no longer used.
 */

package ueshooting.map;

import ueshooting.sprite.Mob;
import ueshooting.sprite.Shot;
import ueshooting.sprite.Sprite;
import ueshooting.sprite.SpriteCategory;
import ueshooting.system.SystemMain;

public class House extends Building {
	@SuppressWarnings("unused")
	private int population;
	public House(int p_type,int p_x_tilepos,int p_y_tilepos){
		super(p_x_tilepos,p_y_tilepos);
		category = BuildingCategory.HOUSE;
		type = p_type;
		level = 1;
	}
	
	void action(Map map){
		Sprite temp;
		double angle;
		switch(type){
		case 0:
			if((time % 15) == 0){
				//System.exit(1);
				Sprite target;
				target = get_nearest_enemy(map);
				if(target != null){
					angle = Math.atan2(target.getY_double() - getY_int() , target.getX_double() - getX_int());
				}else{
					angle = Math.random() * Math.PI * 2;
				}
				temp = map.setSprite(SpriteCategory.SHOT, 0, getX_int(), getY_int(), 1);
				((Shot) temp).shoot_angle(15,angle);
			}
			break;
			
		case 1:
			if((time % 50) == 0){
				//System.exit(1);
				temp = map.setSprite(SpriteCategory.NPC, 0, getX_int(), getY_int());
				((Mob) temp).create(0,1);
			}
			break;
		}
		
		time++;
	}
	
	Sprite get_nearest_enemy(Map map){
		Sprite result = null;
		Sprite temp;
		double cur_x,cur_y;
		double min_distance = 10000,cur_distance;
		for(int i  = 0;i < map.getSpriteNum();i++){
			temp = map.getSprite(i);
			if(!(temp instanceof Mob))continue;
			if((!temp.visiblity) | (!temp.exist))continue;
			if(temp.belong == belong)continue;
			cur_x = temp.getX_double();
			cur_y = temp.getY_double();
			cur_distance = SystemMain.get_distance(getX_int(), getY_int(), cur_x, cur_y);
			System.out.printf("go by %f\n",cur_distance);
			if(cur_distance < min_distance){
				min_distance = cur_distance;
				result = temp;
			}
		}
		return result;
	}
}
