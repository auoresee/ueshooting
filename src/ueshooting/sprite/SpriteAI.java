package ueshooting.sprite;

import ueshooting.map.Map;

public abstract class SpriteAI {
	protected Sprite self;
	protected Map map;
	
	public SpriteAI(Sprite sprite, Map p_map) {
		self = sprite;
		map = p_map;
	}
	
	public abstract void spawn();

	public abstract void destroy();
	
	public abstract void timeAction();
	
	public abstract void update();
}
