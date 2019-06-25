package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.stage.EnemyData;

public class DelegatedAI1_1 extends DelegatedAI {
	public static final String name = "フラン";
	private int old_stage_state = 0;
	boolean loop_flag = false;
	public DelegatedAI1_1(Sprite sprite, Map p_map, int prm) {
		super(sprite, p_map, prm);
		((Mob)self).immortal = true;
		p_map.startBossBattle();
	}

	@Override
	public void timeAction() {
		if(map.getStageState() != old_stage_state){
			iTime = 0;
		}
		switch(map.getStageState()){	//最後のbreakを忘れずに(特に外側の)
		case 1: {
			switch(iTime){
			case 60: {
				//移動開始
				moveToIn(232, 61, 20);
				break;
			}
			case 80: stopMoving(); break;
			case 90: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 120: {
				moveToIn(164, 91, 20);
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 140: stopMoving(); break;
			case 150: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 170: {
				moveToIn(217, 62, 20);
				break;
			}
			case 180: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 190: stopMoving(); break;
			case 210: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 240: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 250: {
				moveToIn(266, 61, 20);
				break;
			}
			case 270: {
				stopMoving();
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 300: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 330: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 350: {
				moveToIn(113, 112, 20);
				break;
			}
			case 360: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 370: stopMoving(); break;
			case 390: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 420: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 430: {
				moveToIn(107, 66, 20);
				break;
			}
			case 450: {
				stopMoving();
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 480: {
				shoot360WithDistance(50, 1, 0, 1.8, 10, 40);
				
				break;
			}
			case 500: {
				iTime = 60;
				break;
			}
			}
			break;
		}
		
		case 2: {			
			switch(iTime){
			case 10: {
				moveToIn(187, 90, 60);
				break;
			}
			case 70: stopMoving(); break;
			case 160: {		//ここで魔法陣1つめ
				if(!loop_flag){
					
					EnemyData ed = new EnemyData("", 0, 0, 0, (byte) 0, 5, 1, null);
					Enemy e = new Enemy(ed, self.belong, map);
					e.setAI(new DelegatedAI1_2(e, map, 0));
					e.immortal = true;
					e.create(1);
					map.setSprite(e);
					ed = new EnemyData("", 383, 447, 0, (byte) 0, 5, 1, null);
					e = new Enemy(ed, self.belong, map);
					e.setAI(new DelegatedAI1_2(e, map, 1));
					e.immortal = true;
					e.create(1);
					map.setSprite(e);
				}
				
				moveToIn(187, 20, 10);
				break;
			}
			case 170: stopMoving(); break;
			case 180: {
				moveToIn(77, 45, 25);
				break;
			}
			case 205: stopMoving(); break;
			case 300: {		//ここで魔法陣2つめ
				if(!loop_flag){
					EnemyData ed = new EnemyData("", 0, 0, 0, (byte) 0, 5, 1, null);
					Enemy e = new Enemy(ed, self.belong, map);
					e.setAI(new DelegatedAI1_2(e, map, 2));
					e.immortal = true;
					e.create(1);
					map.setSprite(e);
					ed = new EnemyData("", 383, 447, 0, (byte) 0, 5, 1, null);
					e = new Enemy(ed, self.belong, map);
					e.setAI(new DelegatedAI1_2(e, map, 3));
					e.immortal = true;
					e.create(1);
					map.setSprite(e);
				}
				
				moveToIn(72, 153, 60);
				break;
			}
			case 360: stopMoving(); break;
			case 600: {
				moveToIn(226, 95, 40);
				break;
			}
			case 640: stopMoving(); break;
			case 700: {
				moveToIn(215, 144, 20);
				break;
			}
			case 720: stopMoving(); break;
			case 880: {
				moveToIn(310, 74, 80);
				break;
			}
			case 960: stopMoving(); break;
			case 1030: {
				moveToIn(313, 139, 90);
				break;
			}
			case 1120: stopMoving(); break;
			case 1280: {
				moveToIn(239, 45, 35);
				break;
			}
			case 1315: stopMoving(); break;
			case 1400: {
				iTime = 0;
				loop_flag = true;
			}
			}
			break;
		}
		}
		old_stage_state = map.getStageState();
	}
	
	@Override
	public void update(){
		super.update();	//これの中でiTimeがインクリメントされる
		switch(map.getStageState()){
		case 1:
			if(((Mob)self).hp <= 0 || map.getStageStateTime() >= 1800) {
				map.startSpellCard("aa");
				((Mob)self).hp = 4000;
			}
			break;
			
		case 2:
			if(((Mob)self).hp <= 0 || map.getStageStateTime() >= 3600) {
				((Mob)self).hp = 0;
				((Mob)self).exist = false;
				map.endBossBattle();
				map.stageWin();
			}
			break;
		}
	}
}