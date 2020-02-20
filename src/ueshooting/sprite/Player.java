package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.system.SystemMain;

public class Player extends Mob {

	public int life = 5;
	private int gamescore = 0;
	private int shot_time = 0;
	private ScrollDirection scroll = ScrollDirection.VERTICAL;
	private boolean key_code[] = new boolean[7];
	private byte key_z = SystemMain.KEY_OFF;
	private byte key_x = SystemMain.KEY_OFF;
	private byte key_left = SystemMain.KEY_OFF;
	private byte key_right = SystemMain.KEY_OFF;
	private byte key_up = SystemMain.KEY_OFF;
	private byte key_down = SystemMain.KEY_OFF;
	private byte key_shift = SystemMain.KEY_OFF;
	private int shot_interval = 3;
	private double speed = 6;
	private PlayerListener player_listener;
	private int death_time = -1;
	private int reborn_time = 0;
	public boolean invincible_mode = false;
	private int invincible_time = 0;
	private int invincible_end = 90;
	public boolean debug_invincible = false;
	
	public Player(int p1, int p2, boolean debug_invincible) {
		super(0, p1, p2);
		if(scroll == ScrollDirection.HORIZONTAL)
		{
			type = 0;
		}
		else if(scroll == ScrollDirection.VERTICAL)
		{
			type = 4;
		}
		setSize();
		this.debug_invincible = debug_invincible;
	}
	
	public void create(){
		hp = 1;
		belong = 0;
		collision_range = 3;
		x_speed = 0;
		y_speed = 0;
		visiblity = true;
		action_flag = true;
		move_flag = true;
		collision_flag = true;
	}
	
	public void changeScrollDirection(ScrollDirection d){
		if(d == scroll) return;
		scroll = d;
		if(scroll == ScrollDirection.HORIZONTAL)
		{
			type = 0;
		}
		else if(scroll == ScrollDirection.VERTICAL)
		{
			type = 1;
		}
	}
	
	public void input(boolean p_key_code[]){
		key_code = p_key_code;
	}
	
	public void shoot(Map map){
		SystemMain.soundManager.playClip("shot", 3, 30);
		if(scroll == ScrollDirection.HORIZONTAL){
			Shot shot;
			if(key_shift >= SystemMain.KEY_ON){
				shot = new Shot(16, 0, x + xsize / 2 * 0.8, y + ysize / 2 * 0.56);
			}
			else {
				shot = new Shot(17, 0, x + xsize / 2 * 0.8, y + ysize / 2 * 0.56);
			}
			map.setSprite(shot);
			shot.shoot_angle(15, 0);
		}
		else if(scroll == ScrollDirection.VERTICAL){
			Shot shot;
			if(key_shift >= SystemMain.KEY_ON){
				shot = new Shot(13, 0, x, y - ysize / 2 * 0.8);
			}
			else {
				shot = new Shot(12, 0, x, y - ysize / 2 * 0.8);
			}
			map.setSprite(shot);
			shot.shoot_angle(15, 270);
		}
	}
	
	@Override
	public boolean move(){
		boolean ret = super.move();
		if(x < 10)x = 10;
		if(x > SystemMain.main_screen_xsize - 10)x = SystemMain.main_screen_xsize - 10;
		if(y < 10)y = 10;
		if(y > SystemMain.main_screen_ysize - 10)y = SystemMain.main_screen_ysize - 10;
		return ret;
	}
	@Override
	public void collision(Map map,Sprite sprite){
		if(hp <= 0)return;
		if(action_flag == false)return;
		if(invincible_mode) return;
		//if(debug_invincible) return;
		super.collision(map,sprite);
	}
	@Override
	public void action(Map map){
		//System.out.printf("%d,%d,%d,%d",hp,)
		if(hp <= 0){
			death_time++;
			if(death_time >= reborn_time)reborn();
			return;
		}
		if(action_flag == false)return;
		if(invincible_mode){
			invincible_time++;
			if(invincible_time >= invincible_end){
				invincible_mode = false;
				invincible_time = 0;
				flash = false;
			}
		}
		getkey();
		if(key_z >= SystemMain.KEY_ON){
			if(shot_time == 0){
				shoot(map);
				shot_time = shot_interval;
			}else{
				shot_time--;
			}
		}else{
			shot_time = 0;
		}
		if(key_x == SystemMain.KEY_PRESSED){
			shoot_bomb();
		}
		x_speed = 0;
		y_speed = 0;
		if(key_shift >= SystemMain.KEY_ON){
			speed = 3;
		}
		else {
			speed = 6;
		}
		if(key_left >= SystemMain.KEY_ON){
			x_speed -= speed ;
		}
		if(key_right >= SystemMain.KEY_ON){
			x_speed += speed;
		}
		if(key_up >= SystemMain.KEY_ON){
			y_speed -= speed;
		}
		if(key_down >= SystemMain.KEY_ON){
			y_speed += speed;
		}
	}

	private void getkey() {
		if(key_code[0]){
			if(key_z == SystemMain.KEY_OFF){
				key_z = SystemMain.KEY_PRESSED;
			}else{
				key_z = SystemMain.KEY_ON;
			}
		}else{
			key_z = SystemMain.KEY_OFF;
		}
		
		if(key_code[1]){
			if(key_x == SystemMain.KEY_OFF){
				key_x = SystemMain.KEY_PRESSED;
			}else{
				key_x = SystemMain.KEY_ON;
			}
		}else{
			key_x = SystemMain.KEY_OFF;
		}
		
		if(key_code[2]){
			if(key_left == SystemMain.KEY_OFF){
				key_left = SystemMain.KEY_PRESSED;
			}else{
				key_left = SystemMain.KEY_ON;
			}
		}else{
			key_left = SystemMain.KEY_OFF;
		}
		
		if(key_code[3]){
			if(key_right == SystemMain.KEY_OFF){
				key_right = SystemMain.KEY_PRESSED;
			}else{
				key_right = SystemMain.KEY_ON;
			}
		}else{
			key_right = SystemMain.KEY_OFF;
		}
		
		if(key_code[4]){
			if(key_up == SystemMain.KEY_OFF){
				key_up = SystemMain.KEY_PRESSED;
			}else{
				key_up = SystemMain.KEY_ON;
			}
		}else{
			key_up = SystemMain.KEY_OFF;
		}
		
		if(key_code[5]){
			if(key_down == SystemMain.KEY_OFF){
				key_down = SystemMain.KEY_PRESSED;
			}else{
				key_down = SystemMain.KEY_ON;
			}
		}else{
			key_down = SystemMain.KEY_OFF;
		}
		
		if(key_code[6]){
			if(key_shift == SystemMain.KEY_OFF){
				key_shift = SystemMain.KEY_PRESSED;
			}else{
				key_shift = SystemMain.KEY_ON;
			}
		}else{
			key_shift = SystemMain.KEY_OFF;
		}
	}

	private void shoot_bomb() {
		// TODO Auto-generated method stub
		
	}
	
	protected boolean check_hp() {
		if(hp <= 0 & death_time == -1){
			x_speed = 0;
			y_speed = 0;
			visiblity = false;
			action_flag = false;
			collision_flag = false;
			move_flag = false;
			death_time = 0;
			die();
			return false;
		}
		return true;
	}

	public void setPlayerlistener(PlayerListener listener){
		player_listener = listener;
	}
	
	private void die() {
		//死亡時のアクションなどを記述
		if(player_listener != null){
			if(life <= 0){
				player_listener.player_gameover();
			}
			player_listener.player_died();
		}
		
		SystemMain.soundManager.playClip("playerdead", 0, 50);
	}
	
	public void reborn(){
		life--;
		x = SystemMain.main_screen_xsize / 2;
		y = 400;
		death_time = -1;
		visiblity = true;
		action_flag = true;
		collision_flag = true;
		move_flag = true;
		hp = 1;
		invincible_mode = true;
		flash = true;
	}

	public void addGameScore(int p) {
		gamescore += p;
	}

	public int getGameScore() {
		return gamescore;
	}

	public int getLife() {
		// TODO Auto-generated method stub
		return life;
	}
}
