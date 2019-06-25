package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.sprite.SpriteAI;
import ueshooting.system.Deg;
import ueshooting.system.SystemMain;

public class DelegatedAI extends SpriteAI {
	int mode;
	int iTime; //internal time
	
	public DelegatedAI(Sprite sprite, Map p_map, int prm) {
		super(sprite, p_map);
		mode = 0;
		iTime = 0;
	}

	@Override
	public void spawn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeAction() {
	}

	protected void stopMoving() {
		self.x_speed = 0;
		self.y_speed = 0;
	}

	@Override
	public void update() {
		iTime++;
	}
	
	protected void moveToIn(double x,double y,double mTime){
		self.x_speed = (x - self.x) / mTime;
		self.y_speed = (y - self.y) / mTime;
	}
	
	protected void moveByIn(double mx,double my,double mTime){
		self.x_speed = mx / mTime;
		self.y_speed = my / mTime;
	}
	
	protected void shoot360(int num,int type,int subType,double speed,double offset) {
		Shot shot;
		for(int i = 0;i < num;i++){
			shot = new Shot(type,subType,self.getX_int(),self.getY_int(),self.belong);
			shot.shoot_angle(speed, 360 * 2 / num * i + offset);
			map.setSprite(shot);
		}
		SystemMain.soundManager.playClip("shot", 4, 20);
	}
	protected void shoot360WithDistance(int num,int type,int subType,double speed,double offset,double distance) {
		Shot shot;
		for(int i = 0;i < num;i++){
			double x_offset = Deg.cos(360.0 / num * i + offset) * distance;
			double y_offset = Deg.sin(360.0 / num * i + offset) * distance;
			shot = new Shot(type,subType,self.getX_double() + x_offset,self.getY_double() + y_offset,self.belong);
			shot.shoot_angle(speed, 360.0 / num * i + offset);
			map.setSprite(shot);
		}
		SystemMain.soundManager.playClip("shot", 4, 20);
	}

}
