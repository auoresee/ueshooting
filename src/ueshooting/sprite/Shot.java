package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.script.ScriptAI;
import ueshooting.system.Deg;
import ueshooting.system.SystemMain;

public class Shot extends Sprite {
	public static String dir_name = "shot\\";
	//protected static String chip_name[] = {"shot_00.gif"};
	protected ScriptAI ai;
	protected boolean through;

	public Shot(int p_type, int p_subType, double p_x, double p_y) {
		category = SpriteCategory.SHOT;
		type = p_type;
		subType = p_subType;
		x = p_x;
		y = p_y;
		z = 10;
		through = false;
		time = 0;
		setSize();
		collision_range = ((xsize / 2) + (ysize / 2)) / 2;
	}

	public Shot(int p1, int p2, double p3, double p4, int p5) {
		this(p1,p2,p3,p4);
		belong = p5;
	}
	
	public Shot(int p1, int p2, double p3, double p4, double option, int p6) {
		this(p1,p2,p3,p4);
		if(option != 0) through = true;
		belong = p6;
	}
	
	public void shoot_xy(double x_speed,double y_speed){
		this.x_speed = x_speed;
		this.y_speed = y_speed;
		visiblity = true;
		action_flag = true;
		move_flag = true;
		create();
	}

	public void shoot_angle(double speed,double angle){
		x_speed = Deg.cos(angle) * speed;
		y_speed = Deg.sin(angle) * speed;
		visiblity = true;
		action_flag = true;
		move_flag = true;
		create();
	}
	
	@Override
	public boolean move() {
		if(time == 0)return false;
		boolean ret = super.move();
		rotation = Deg.atan2(y_speed, x_speed);
		if(belong == 0 && y < -ysize) exist = false;	//‰æ–ÊŠO‚Ì“G‚É“–‚½‚ç‚È‚¢‚æ‚¤‚É‚·‚é‚½‚ß
		return ret;
	}
	
	@Override
	public void collision(Map map,Sprite sprite) {
		if(!collision_flag)return;
	}

	@Override
	public void action(Map map) {
		if(!action_flag)return;
		time++;
	}

	public void hit(Sprite sprite){
		if(through){
			
		}else{
			exist = false;
		}
	}
	
	//UŒ‚—Í‚ÍŽ©‹@’eˆÈŠO‚Í‚·‚×‚Ä1
	//Ží—Þ‚ª­‚È‚¢‚Ì‚Å•Ï”‚Íì‚ç‚È‚¢
	public int getOffence() {
		if(type == 13 || type == 17) {
			return 14;
		}
		if(type == 12 || type == 16) {
			return 10;
		}
		return 1;
	}

	public void controlMovement(int type, double p1,
			double p2, double p3) {
		// TODO Auto-generated method stub
		
	}

	public void controlAction(int type, double p1,
			double p2, double p3) {
		// TODO Auto-generated method stub
		
	}

	public void create() {}

}
