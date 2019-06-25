package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.script.ScriptAI;
import ueshooting.stage.Script;

public class ControlledShot extends Shot implements ControlledByAI {
	SpriteAI ai;
	public ControlledShot(Map map, int p1, int p2, double p3, double p4, double option,
			int p6, Script script) {
		super(p1, p2, p3, p4, p6);
		ai = new ScriptAI(this, script, map.getGlobalFunctions(), map, option);
	}

	@Override
	public void create(){
		super.create();
		ai.spawn();
	}
	
	public void hit(Sprite sprite){
		if(!through){
			ai.destroy();
		}
		super.hit(sprite);
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

	@Override
	public SpriteAI getAI() {
		return ai;
	}

	@Override
	public void setAI(SpriteAI ai) {
		this.ai = ai;
	}
}
