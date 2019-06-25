package ueshooting.graphic;

public enum GraphicEffectType {
	//compound			(他のエフェクトを呼び出す)
	SPELLCARD_START,
	SPELLCARD_END,
	DRAW_SPELLCARD_NAME,
	BOSS_BATTLE_END,
	
	//background layer	(背景の上、スプライトの下に描画される)
	TRANSIT_BACKGROUND,
	
	//foreground layer	(スプライトの上に描画される)
	EXPLODE,
	
	//surface layer		(前面(foreground)レイヤーのエフェクトの上に描画される)
	//優先されるエフェクトや、UIのうち色調変更の影響を受けるもの
	
	
	//screen layer		(surfaceレイヤーのエフェクトの上に描画される)
	FLASH,
	
	//UI layer			(screenレイヤーのエフェクトの上に描画される)
	//UIのうち色調変更の影響を受けないもの
	DRAW_STRING,
}
