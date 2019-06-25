package ueshooting.stage;

public class EnemyAction {
	int action_type;
	int action_subtype;
	int action_prm1;
	int action_prm2_int;
	String action_prm2_str;
	int time;
	public EnemyAction(int type, int subtype, int prm1, int prm2_int, int p_time) {
		action_type = type;
		action_subtype = subtype;
		action_prm1 = prm1;
		action_prm2_int = prm2_int;
		time = p_time;
	}
	public EnemyAction(int type, int subtype, int prm1, String prm2_str, int p_time) {
		action_type = type;
		action_subtype = subtype;
		action_prm1 = prm1;
		action_prm2_str = prm2_str;
		time = p_time;
	}
	public int getType() {
		return action_type;
	}
	public int getTime() {
		return time;
	}
	public int getXSpeed(){
		return action_prm1;
	}
	public int getYSpeed(){
		return action_prm2_int;
	}
	public int getXAccel(){
		return action_prm1;
	}
	public int getYAccel(){
		return action_prm2_int;
	}
	public int getX(){
		return action_prm1;
	}
	public int getY(){
		return action_prm2_int;
	}
	public int getDefaultSpeed(){
		return action_prm1;
	}
	public int getVector(){
		return action_prm1;
	}
	public int getSubType() {
		return action_subtype;
	}
}
