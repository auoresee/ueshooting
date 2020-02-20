package ueshooting.sprite;

import ueshooting.map.Map;

/// �v���C���[�̂����蔻��̔����~
public class PlayerBounds extends Sprite {
	
	private Sprite target;
	
	private static double offsetX = 10;
	private static double offsetY = 10;
	
	/// @param target �����蔻���\������Ώ�
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
		// target ��ǐ�
		this.x = target.x + offsetX;
		this.y = target.y + offsetY;
	}

}
