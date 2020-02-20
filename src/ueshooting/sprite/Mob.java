package ueshooting.sprite;

import java.awt.Image;

//import ueshooting.map.Building;
import ueshooting.map.GameObject;
import ueshooting.map.Map;

public class Mob extends Sprite {
	private static final double DEFAULT_DIRECT_DAMAGE = 20;
	public static String dir_name = "mob\\";
	public double hp,mp;
	public boolean immortal = false;		//HPÇ™0Ç…Ç»Ç¡ÇƒÇ‡éÄÇ»Ç»Ç¢
	public double offence,deffence;
	//protected Point[] through_point;
	protected GameObject target;
	//private int shot_interval = 30;
	private int score = 200;	//ì|ÇµÇΩÇ∆Ç´Ç…ìæÇÁÇÍÇÈÉXÉRÉA
	
	public Mob(int p1,int p2,int p3){
		hp = 50;
		offence = 10;
		deffence = 0;
		category = SpriteCategory.NPC;
		type = p1;
		x = p2;
		y = p3;
		z = 10;
		time = 0;
		setSize();
		collision_range = Math.sqrt(xsize * xsize + ysize + ysize) * 0.35;
	}

	public Mob(int p1, int p2, int p3, int p4) {
		this(p1,p2,p3);
		belong = p4;
	}

	public void create(int p_belong){
		belong = p_belong;
		x_speed = 0;
		y_speed = 0;
		visiblity = true;
		action_flag = true;
		move_flag = true;
		collision_flag = true;
		//set_states();
	}
	
	public void create(int p_type,int p_belong){
		type = p_type;
		belong = p_belong;
		x_speed = 0;
		y_speed = 0;
		visiblity = true;
		action_flag = true;
		move_flag = true;
		collision_flag = true;
		//set_states();
	}

	/*private void set_states(){
		switch(type){
		case 1:
			shot_interval = 20;
			break;
			
		case 2:
			shot_interval = 30;
			break;
			
		case 3:
			shot_interval = 90;
			break;
		}
	}*/
	
	/*void set_target(Building building){
		target = building;
	}*/
	
	/*private Point get_square_route_x(int x, int y, int t_x, int t_y) {
		return new Point(t_x,(int)y);
	}
	
	private Point get_square_route_y(int x, int y, int t_x, int t_y) {
		return new Point((int)x,t_y);
	}

	private Point get_block_building(int x, int y, int x2, int y2) {
		return null;
	}*/

	public void collision(Map map,Sprite sprite){
		if(collision_flag == false)return;
		if(sprite instanceof Shot){
			Shot shot = (Shot)sprite;
			if(shot.belong == belong){
				
			}else{
				hp -= calc_damage(shot.getOffence());
				if(!check_hp()){
					if(shot.belong == 0)map.getPlayer().addGameScore(score);
				}
				shot.hit(this);
			}
		}
		/*if(target != null)return;*/
		if(sprite instanceof Mob){
			Mob mob = (Mob)sprite;
			if(mob.belong == belong){
				
			}else{
				hp -= calc_damage(DEFAULT_DIRECT_DAMAGE);
				if(!check_hp()){
					if(mob.belong == 0)map.getPlayer().addGameScore(score);
				}
				mob.hit(this);
			}
		}
	}
	
	private void hit(Mob mob) {
		// TODO Auto-generated method stub
		
	}

	//Ç±ÇÃcheck_hpÇ≈è¡ñ≈ÇµÇΩÇ∆Ç´ÇÃÇ›false
	//(å≥Ç©ÇÁë∂ç›ÇµÇ»Ç¢èÍçáÇÕtrue)
	protected boolean check_hp() {
		if(exist && hp <= 0){
			if(!immortal){
				exist = false;
				return false;
			}
		}
		return true;
	}

	protected double calc_damage(double offence) {
		return Math.max(offence - getDeffence() / 2, 0);
	}

	public double getDeffence() {
		return deffence;
	}

	protected void set_target(Mob mob) {
		target = mob;
	}

	/*public void collision(Building building){
		if(target != null)return;
		if(building.belong == belong){
			
		}else{
			set_target(building);
		}
	}*/
	
	@Override
	public void action(Map map) {
		//if(time > 300)exist = false;
		//if(hp <= 0)exist = false;
		time++;
	}

	@SuppressWarnings("unused")
	private void shoot_360(Map map,int num,int speed) {
		Shot shot;
		for(int i = 0;i < num;i++){
			shot = new Shot(0,(int)x,(int)y,belong);
			shot.shoot_angle(speed, Math.PI * 2 / num * i);
			map.setSprite(shot);
		}
	}

	public int getScore() {
		return score;
	}

}