package ueshooting.sprite;

import ueshooting.map.Map;

/// プレイヤーのあたり判定の白い円
public class PlayerBounds extends Sprite {
	
	private Sprite target;
	
	private static double offsetX = 10;
	private static double offsetY = 10;
	
	/// @param target あたり判定を表示する対象
	public PlayerBounds(Sprite target) {
		this.target = target;
		
		// sprite: 0_other.png
		this.type = 0;
		this.category = SpriteCategory.OTHER;
	}
	
	@Override
	public void collision(Map map, Sprite sprite) {
		// pass
	}

	@Override
	public void action(Map map) {
		// target を追跡
		this.x = target.x + offsetX;
		this.y = target.y + offsetY;
	}

}
