package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.script.ScriptAI;
import ueshooting.stage.EnemyData;

public class Enemy extends Mob implements ControlledByAI {
	public SpriteAI ai;
	int cur_action_index;
	protected double default_speed = 5;
	
	public Enemy(EnemyData p_enemydata, int p_belong, Map map) {
		super(p_enemydata.getType(),p_enemydata.getSpawnX(),p_enemydata.getSpawnY(),p_belong);
		hp = p_enemydata.getHP();
		ai = new ScriptAI(this, p_enemydata.getScript(), map.getGlobalFunctions(), map);
	}
	
	public Enemy(EnemyData p_enemydata, int p_belong, Map map, SpriteAI p_ai) {
		super(p_enemydata.getType(),p_enemydata.getSpawnX(),p_enemydata.getSpawnY(),p_belong);
		hp = p_enemydata.getHP();
		ai = p_ai;
	}
	
	@Override
	public void create(int p_belong){
		super.create(p_belong);
		ai.spawn();
	}
	
	public void destroyed(Mob breaker){
		ai.destroy();
	}

	@Override
	public void action(Map map){
		ai.timeAction();
		time++;
	}
	
	@Override
	public void update(Map map){
		ai.update();
	}

	public void setX(double src) {
		x = src;
	}
	
	public void setY(double src) {
		y = src;
	}

	@Override
	public SpriteAI getAI() {
		return ai;
	}

	@Override
	public void setAI(SpriteAI ai) {
		this.ai = ai;
	}

	//private void setVector(double angle) {
	//	
	//}
}
