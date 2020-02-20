package ueshooting.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.*;

import ueshooting.graphic.GraphicEffect;
import ueshooting.graphic.GraphicEffectType;
import ueshooting.sprite.ControlledByAI;
import ueshooting.sprite.ControlledShot;
import ueshooting.sprite.DelegatedAI;
import ueshooting.sprite.DelegatedAI1_1;
import ueshooting.sprite.DelegatedAI1_2;
import ueshooting.sprite.Enemy;
import ueshooting.sprite.Mob;
import ueshooting.sprite.Player;
import ueshooting.sprite.ScrollDirection;
import ueshooting.sprite.Shot;
import ueshooting.sprite.Sprite;
import ueshooting.sprite.SpriteCategory;
import ueshooting.stage.Script;
import ueshooting.stage.Stage;
import ueshooting.system.SystemMain;

public class Map {
	private static final int TILE_XNUM = 20;
	private static final int TILE_YNUM = 20;
	public Stage cur_stage;
	private ScrollDirection scroll = ScrollDirection.VERTICAL;
	//private List<Building> buildings = new ArrayList<Building>();
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<GraphicEffect> effects;
	private int player_index = -1;
	private int tile_map[][] = new int[TILE_XNUM][TILE_YNUM];
	private int building_map[][] = new int[TILE_XNUM][TILE_YNUM];
	
	public boolean inBossBattle = false;
	public boolean inSpellCard = false;
	
	public int stage_time = 0;			//ボス・中ボス戦の間は停止
	
	private int stage_state = 0;		//ステージ内の進行状況(スペルカードなど)
	public int getStageState(){return stage_state;}				public void setStageState(int value){stage_state = value;}
	
	private int stage_state_time = 0;	//スペルカードなどの内部時間
	public int getStageStateTime(){return stage_state_time;}	public void setStageStateTime(int value){stage_state_time = value;}
	
	private BufferedImage bgimage_normal;
	private BufferedImage bgimage_spellcard;
	
	private boolean stageWinFlag = false;
	public boolean stageClearFlag;		//これがtrueになるとステージクリア
	
	public Map(Stage stage){
		cur_stage = stage;
	}
	
	/*private void initMap() {
	 * int count = 0;
		for(int y = 0;y < TILE_YNUM;y++){
			for(int x = 0;x < TILE_XNUM;x++){
				buildings.add(new Building(x,y));
				building_map[x][y] = count;
				count++;
			}
		}
	}*/
	
	public void action(){
		//System.out.printf("%d\n", this.getSpriteNum());
		
		if(stageWinFlag && stage_state_time >= 100){
			stageClearFlag = true;
		}
		/*int size = buildings.size();
		for(int i = 0;i < size;i++){
			buildings.get(i).action(this);
		}*/
		int size = sprites.size();
		for(int i = 0;i < size;i++){
			Sprite temp = sprites.get(i);
			if(!temp.exist){
				sprites.remove(i);
				i--;
				size--;
				continue;
			}
			//if(temp instanceof Enemy) System.out.printf("NPC id %d\n of %s\n", i, ((Enemy)temp).ai.getClass().getName());
			boolean ret = temp.move();
			if(ret){
				if(temp.getX_int() < -SystemMain.screen_edge_size || temp.getX_int() > SystemMain.main_screen_xsize + SystemMain.screen_edge_size ||
						temp.getY_int() < -SystemMain.screen_edge_size | temp.getY_int() > SystemMain.main_screen_ysize + SystemMain.screen_edge_size){
					sprites.remove(i);
					i--;
					size--;
					continue;
				}
			}
			sprites.get(i).action(this);
		}
		check_collision();
		for(int i = 0;i < size;i++){
			sprites.get(i).update(this);
		}
		if(!inBossBattle && !inSpellCard){
			stage_time++;
		}
		stage_state_time++;
	}
	
	/*public void build(BuildingCategory category,int type,int xsize,int ysize,int p_x,int p_y){
		//int building_index;
		Building cur_building,temp;
		boolean flag = true;
		for(int y = p_y;y < p_y + ysize;y++){
			for(int x = p_x;x < p_x + xsize;x++){
				cur_building = buildings.get(building_map[x][y]);
				if(cur_building.category != BuildingCategory.NONE | cur_building.type != 0){
					flag = false;
					break;
				}
			}
		}
		if(flag){
			temp = Building.get_new_instance(category,type,p_x,p_y);
			buildings.add(temp);
			for(int y = p_y;y < p_y + ysize;y++){
				for(int x = p_x;x < p_x + xsize;x++){
					buildings.remove(building_map[x][y]);
					//System.out.println("removed");
					shift_map(building_map[x][y]);
					building_map[x][y] = buildings.size()-1;
				}
			}
		}
	}
	
	private void shift_map(int index) {
		for(int y = 0;y < TILE_YNUM;y++){
			for(int x = 0;x < TILE_XNUM;x++){
				if(building_map[x][y] == index){
					building_map[x][y] = -1;
					continue;
				}
				if(building_map[x][y] > index){
					building_map[x][y]--;
					continue;
				}
			}
		}
	}

	public void build(BuildingCategory category,int type,int x,int y){
		build(category,type,1,1,x,y);
	}
	
	public void setTile(int type,int x,int y){
		tile_map[x][y] = type;
	}*/
	
	public void check_collision(){
		int sprite_num = sprites.size();
		//int building_num = buildings.size();
		int src,dest;
		double distance;
		for(src = 0;src < sprite_num;src++){
			for(dest = 0;dest < sprite_num;dest++){
				if(src == dest) continue;
				distance = SystemMain.get_distance(sprites.get(src).getX_double(),sprites.get(src).getY_double(),sprites.get(dest).getX_double(),sprites.get(dest).getY_double());
				if(distance <= sprites.get(src).collision_range + sprites.get(dest).collision_range){
					sprites.get(src).collision(this,sprites.get(dest));
				}
			}
			/*if(!sprites.get(src).collision_building)continue;
			for(dest = 0;dest < building_num;dest++){
				distance = SystemMain.get_distance(sprites.get(src).getX_double(),sprites.get(src).getY_double(),buildings.get(dest).getX_int(),buildings.get(dest).getY_int());
				if(distance <= sprites.get(src).collision_range + sprites.get(dest).collision_range){
					sprites.get(src).collision(this,buildings.get(dest));
				}
			}*/
		}
	}
	
	public void startBossBattle(){
		if(!inBossBattle){
			inBossBattle = true;
			stage_state++;
			stage_state_time = 0;
		}
	}
	
	public void endBossBattle(){
		if(inBossBattle){
			removeAllEnemyShots();
			BufferedImage bgimage = bgimage_normal;
			GraphicEffect neweffect = new GraphicEffect(GraphicEffectType.BOSS_BATTLE_END);
			neweffect.parameters = new Object[1];
			neweffect.parameters[0] = bgimage;
			effects.add(neweffect);
			//SystemMain.soundManager.playClip("endspellcard", 1, 50);	//東方版
			SystemMain.soundManager.playClip("break", 8, 50);			//カービィ版
			inBossBattle = false;
			stage_state++;
			stage_state_time = 0;
		}
	}
	
	private void removeAllEnemyShots() {
		for(Sprite s : sprites){
			if(s instanceof Shot && s.belong != 0) s.exist = false;
		}
	}

	public void startSpellCard(String name){
		removeAllEnemyShots();
		BufferedImage bgimage = bgimage_spellcard;
		GraphicEffect neweffect = new GraphicEffect(GraphicEffectType.SPELLCARD_START);
		neweffect.parameters = new Object[2];
		neweffect.parameters[0] = name;
		neweffect.parameters[1] = bgimage;
		effects.add(neweffect);
		SystemMain.soundManager.playClip("startspellcard", 0, 50);
		stage_state++;
		stage_state_time = 0;
	}
	
	public void endSpellCard(){
		removeAllEnemyShots();
		BufferedImage bgimage = bgimage_normal;
		GraphicEffect neweffect = new GraphicEffect(GraphicEffectType.SPELLCARD_END);
		neweffect.parameters = new Object[1];
		neweffect.parameters[0] = bgimage;
		effects.add(neweffect);
		SystemMain.soundManager.playClip("endspellcard", 0, 50);
		stage_state++;
		stage_state_time = 0;
	}
	
	public int setSprite(Sprite sprite){
		sprites.add(sprite);
		return sprites.size()-1;
	}
	
	public Sprite setSprite(SpriteCategory category,int type,int x,int y){
		Sprite temp;
		switch(category){
		case SHOT:
			temp = new Shot(type,0,x,y);
			break;
			
		case NPC:
			temp = new Mob(type,x,y);
			break;

		default:
			return null;
			
		}
		sprites.add(temp);
		//System.out.printf("Set %d to %d at (%d,%d)\n",type,sprites.size()-1,x,y);
		return temp;
	}
	
	public Sprite setSprite(SpriteCategory category,int type,int x,int y,int belong){
		Sprite temp;
		switch(category){
		case SHOT:
			temp = new Shot(type,x,y,belong);
			break;
			
		case NPC:
			temp = new Mob(type,x,y,belong);
			break;

		default:
			return null;
			
		}
		sprites.add(temp);
		//System.out.printf("Set %d to %d at (%d,%d)\n",type,sprites.size()-1,x,y);
		return temp;
	}

	public void setPlayer(Player player){
		sprites.add(player);
		player_index = sprites.size() - 1;
	}
	
	public void setEffectList(List<GraphicEffect> p_effects){
		effects = p_effects;
	}
	
	public Player getPlayer(){
		if(player_index == -1)return null;
		return (Player) sprites.get(player_index);
	}
	
	/*public Building getBuilding(int x,int y){
		return buildings.get(building_map[x][y]);
	}*/
	
	public Sprite getSprite(int index){
		return sprites.get(index);
	}
	
	public int getSpriteNum(){
		return sprites.size();
	}
	
	public int getTile(int p_x,int p_y){
		return tile_map[p_x][p_y];
	}

	public void effect(int type, double posX,
			double posY, double parameter) {
		throw new RuntimeException("Effect is unimplemented");
	}

	public void delegate(int delegateID, Sprite self, Map map, int prm) {
		switch(delegateID){
		case 0:		//default
			((ControlledByAI)self).setAI(new DelegatedAI(self,map,prm));
			break;
			
		case 1:
			((ControlledByAI)self).setAI(new DelegatedAI1_1(self,map,prm));
			break;
			
		case 2:
			((ControlledByAI)self).setAI(new DelegatedAI1_2(self,map,prm));
			break;
			
		default:
			((ControlledByAI)self).setAI(new DelegatedAI(self,map,prm));
			break;
		}
	}

	public ControlledShot getControlledShot(int type, int subType, double x, double y, double option, int belong) {
		Script script;
		if((script = cur_stage.getShotScript(subType)) != null){
			return new ControlledShot(this, type, subType, x, y, option, belong, script);
		}
		return null;
	}

	public Script getGlobalFunctions() {
		return cur_stage.global;
	}
	
	public void changeScroll(ScrollDirection d){
		if(scroll == d) return;
		scroll = d;
		getPlayer().changeScrollDirection(d);
	}
	
	public ScrollDirection getScroll() {
		return scroll;
	}

	public void setNormalBGImage(BufferedImage image) {
		bgimage_normal = image;
	}
	
	public void setSpellcardBGImage(BufferedImage image) {
		bgimage_spellcard = image;
	}

	public void stageWin() {
		stageWinFlag = true;
		stage_state_time = 0;
	}
}