package ueshooting.graphic;

public enum GraphicEffectType {
	//compound			(���̃G�t�F�N�g���Ăяo��)
	SPELLCARD_START,
	SPELLCARD_END,
	DRAW_SPELLCARD_NAME,
	BOSS_BATTLE_END,
	
	//background layer	(�w�i�̏�A�X�v���C�g�̉��ɕ`�悳���)
	TRANSIT_BACKGROUND,
	
	//foreground layer	(�X�v���C�g�̏�ɕ`�悳���)
	EXPLODE,
	
	//surface layer		(�O��(foreground)���C���[�̃G�t�F�N�g�̏�ɕ`�悳���)
	//�D�悳���G�t�F�N�g��AUI�̂����F���ύX�̉e�����󂯂����
	
	
	//screen layer		(surface���C���[�̃G�t�F�N�g�̏�ɕ`�悳���)
	FLASH,
	
	//UI layer			(screen���C���[�̃G�t�F�N�g�̏�ɕ`�悳���)
	//UI�̂����F���ύX�̉e�����󂯂Ȃ�����
	DRAW_STRING,
}
