package ueshooting.sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ueshooting.map.Map;
import ueshooting.stage.EnemyData;
import ueshooting.stage.Stage;
import ueshooting.system.SystemMain;

public class EnemyGenerator {
	Stage cur_stage;
	int type_start;
	int type_num;
	int cur_enemy_index = 0;
	java.util.Map<Integer,Integer> enemyIDMap = new HashMap<>();
	
	public EnemyGenerator(){}
	
	public EnemyGenerator(int p_start,int p_num){
		type_start = p_start;
		type_num = p_num;
	}
	
	/*public void generate(Map map){
		int x,y;
		x = (int) (Math.random() * SystemMain.screen_xsize);
		y = (int) (Math.random() * 200);
		int type = (int) (type_start + Math.random() * type_num);
		Mob mob = new Mob(type,x,y,1);
		mob.create(1);
		map.setSprite(mob);
	}*/

	public void generate(Map map){
		EnemyData cur_enemydata;
		for(;cur_enemy_index < cur_stage.getEnemyNum();cur_enemy_index++){
			cur_enemydata = cur_stage.getEnemyData(cur_enemy_index);
			if(cur_enemydata.getSpawnTime() > map.stage_time)break;
			Enemy enemy = new Enemy(cur_enemydata,1,map);
			if(cur_enemydata.getSpawnTime() != -1)enemy.create(1);
			int spriteIndex = map.setSprite(enemy);
			enemyIDMap.put(cur_enemy_index, spriteIndex);
		}
		if(SystemMain.game_time == 120){
			EnemyData ed = new EnemyData("", 192, 50, 0, (byte) 0, 6, 1000, null);
			Enemy e = new Enemy(ed, 1, map);			
			e.setAI(new DelegatedAI1_1(e, map, 0));
			e.create(1);
			e.immortal = true;
			map.setSprite(e);
		}
	}
	
	public void set_stage(Stage cur_stage) {
		this.cur_stage = cur_stage;
	}

	public int getID(int valueOfElement) {
		// TODO Auto-generated method stub
		return 0;
	}
}
