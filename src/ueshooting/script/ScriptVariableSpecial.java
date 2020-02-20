package ueshooting.script;

import ueshooting.sprite.ControlledShot;
import ueshooting.sprite.Enemy;
import ueshooting.sprite.Sprite;
import ueshooting.stage.ScriptRuntimeException;
import ueshooting.system.Deg;

public class ScriptVariableSpecial extends ScriptVariable {
	Sprite self;

	public ScriptVariableSpecial(String p_name, DataType p_type, Sprite p_self) {
		super(p_name, p_type);
		self = p_self;
	}
	
	public Object getValue() {
		switch(name){
		case "s_time":
			return self.time;
			
		case "s_rotation":
			if(!(self instanceof Enemy)){
				throw new RuntimeException("Unsupported operation for this sprite");
			}
			return ((Enemy)self).rotation;
		case "s_hp":
			if(!(self instanceof Enemy)){
				throw new RuntimeException("Unsupported operation for this sprite");
			}
			return ((Enemy)self).hp;
		case "s_xspeed":
			return self.x_speed;
		case "s_yspeed":
			return self.y_speed;
		case "M_PI":
			return Math.PI;
		case "s_angle":		//速度の向き (自身の向きではない)
			return Deg.atan2(self.y_speed, self.x_speed);
		case "s_speed":
			return Math.sqrt(self.x_speed * self.x_speed + self.y_speed * self.y_speed);
		case "s_x":
			return self.getX_double();
		case "s_y":
			return self.getY_double();
		case "null":
			return null;
		}
		return null;
	}
	
	public void setValue(Object src) {
		switch(name){
		case "s_rotation":
			if(!(self instanceof Enemy)){
				throw new RuntimeException("Unsupported operation for this sprite");
			}
			((Enemy)self).rotation = ((Number)src).doubleValue();
			break;
		case "s_hp":
			if(!(self instanceof Enemy)){
				throw new RuntimeException("Unsupported operation for this sprite");
			}
			((Enemy)self).hp = ((Number)src).doubleValue();
			break;
		case "s_xspeed":
			self.x_speed = ((Number)src).doubleValue();
			break;
		case "s_yspeed":
			self.y_speed = ((Number)src).doubleValue();
			break;
		case "s_angle":		//速度の向き (自身の向きではない)
			double speed = Math.sqrt(self.x_speed * self.x_speed + self.y_speed * self.y_speed);
			self.x_speed = Deg.cos(((Number)src).doubleValue()) * speed;
			self.y_speed = Deg.sin(((Number)src).doubleValue()) * speed;
			break;
		case "s_speed":
			double angle = Deg.atan2(self.y_speed, self.x_speed);
			self.x_speed = Deg.cos(angle) * ((Number)src).doubleValue();
			self.y_speed = Deg.sin(angle) * ((Number)src).doubleValue();
			break;
		case "s_x":
			self.setX(((Number)src).doubleValue());
			break;
		case "s_y":
			self.setY(((Number)src).doubleValue());
			break;
			
		case "null":
			//どこにも代入しない
			break;
		}
	}
}
