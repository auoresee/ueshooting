package ueshooting.stage;

public class EnemyData {
	String image_path;
	int spawn_x,spawn_y;
	int spawn_time;
	byte enemy_category;
	int enemy_type;
	int hp;
	Script script;
	
	//“G‚ÍoŒ»‡‚É‹Lq‚·‚é‚±‚Æ
	public EnemyData(String p_image_path,int p_spawn_x, int p_spawn_y, int p_spawn_time,
			byte p_category, int p_type, int p_hp, Script ai2) {
		image_path = p_image_path;
		spawn_x = p_spawn_x;
		spawn_y = p_spawn_y;
		spawn_time = p_spawn_time;
		enemy_category = p_category;
		enemy_type = p_type;
		hp = p_hp;
		script = ai2;
	}
	public int getSpawnTime() {
		return spawn_time;
	}
	public int getSpawnX() {
		return spawn_x;
	}
	public int getSpawnY() {
		return spawn_y;
	}
	public int getType() {
		return enemy_type;
	}
	public int getHP() {
		return hp;
	}
	public Script getScript() {
		return script;
	}
}
