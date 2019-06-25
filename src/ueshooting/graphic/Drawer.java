package ueshooting.graphic;

import java.awt.*;
import java.util.List;

import ueshooting.map.*;
import ueshooting.map.Map;
import ueshooting.sprite.Player;
import ueshooting.sprite.ScrollDirection;
import ueshooting.sprite.Sprite;
import ueshooting.sprite.SpriteCategory;
import ueshooting.system.SystemMain;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.util.*;

public class Drawer {
	public static Drawer instance = new Drawer();
	private BufferedImage whole_bgimage;
	private BufferedImage main_screen_bgimage;
	private BufferedImage infobar_bgimage;
	private GraphicEffectRenderer effectRenderer = new GraphicEffectRenderer();
	private ArrayList<BufferedImage> mapchips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> building_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> house_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> business_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> industry_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> waychips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> transport_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> public_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> millitary_chips = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> nature_chips = new ArrayList<BufferedImage>();
	private boolean use_whole_bgimage = true;
	private boolean use_tile = true;
	private boolean use_building = true;
	private boolean use_sprite = true;
	private boolean use_main_screen_bgimage = true;
	private boolean use_debug_info = false;
	private boolean use_infobar = true;
	private boolean use_compound_effect = true;
	private boolean use_background_effect = true;
	private boolean use_foreground_effects = true;
	private boolean gameover_flag = false;
	private String gameover_message;
	
	String debug_imageinfo(BufferedImage mapchip_image){
		switch(mapchip_image.getTransparency()){
		case Transparency.OPAQUE:
			return "OPAQUE";
		case Transparency.BITMASK:
			return "BITMASK";
		case Transparency.TRANSLUCENT:
			return "TRANSLUCENT";
		default:
			return "OTHER_TRANSLUCENT";
		}
	}
	
	public void load_mapchips(BufferedImage mapchip_image){
		System.out.printf("Argument image: %s\n",debug_imageinfo(mapchip_image));
		mapchips = div_chips(mapchip_image);
		System.out.printf("Loaded mapchips.\n");
		System.out.printf(" Data: %s\n",debug_imageinfo(mapchips.get(0)));
	}
	
	public void load_sprite_chip(Image image,SpriteCategory category){
		Sprite.setChip(image, category);
	}
	
	//no longer used
	public void load_building_chips(BufferedImage mapchip_image,BuildingCategory category){
		ArrayList<BufferedImage> temp_chips = new ArrayList<BufferedImage>();
		System.out.printf("Argument image: %s\n",debug_imageinfo(mapchip_image));
		temp_chips = div_chips(mapchip_image);
		switch(category){
		case NONE:
			building_chips = temp_chips;
			break;
			
		case HOUSE:
			house_chips = temp_chips;
			System.out.printf("Loaded house_chips.\n");
			System.out.printf(" Data: %s\n",debug_imageinfo(house_chips.get(0)));
			break;
			
		case BUSINESS:
			business_chips = temp_chips;
			break;
			
		case INDUSTRY:
			industry_chips = temp_chips;
			break;
		
		case WAY:
			waychips = temp_chips;
			break;
			
		case TRANSPORT:
			transport_chips = temp_chips;
			break;
			
		case PUBLIC:
			public_chips = temp_chips;
			break;
			
		case MILLITARY:
			millitary_chips = temp_chips;
			break;
			
		case NATURE:
			nature_chips = temp_chips;
			break;
		}
	}
	
	//no longer used
	ArrayList<BufferedImage> div_chips(BufferedImage image){
		ArrayList<BufferedImage> ret_chips = new ArrayList<BufferedImage>();
		//Toolkit tk = Toolkit.getDefaultToolkit();
		int image_width = image.getWidth();
		int image_height = image.getHeight();
		for(int y = 0;y <= image_height - SystemMain.tile_size;y += SystemMain.tile_size){
			for(int x = 0;x <= image_width - SystemMain.tile_size;x += SystemMain.tile_size){
				ret_chips.add(image.getSubimage(x, y, SystemMain.tile_size, SystemMain.tile_size));
			}
		}
		return ret_chips;
	}
	
	public void set_use_main_screen_bgimage(boolean p1){
		use_main_screen_bgimage = p1;
	}
	
	//no longer used
	public void set_use_tile(boolean p1){
		use_tile = p1;
	}
	
	//no longer used
	public void set_use_building(boolean p1){
		use_building = p1;
	}
	
	public void set_use_sprite(boolean p1){
		use_sprite = p1;
	}
	
	public void draw(Graphics2D graphics,Map map){
		AffineTransform originalTransform = graphics.getTransform();
		if(use_whole_bgimage){
			draw_whole_background(graphics);
		}
		graphics.setClip(SystemMain.main_screen_xoffset, SystemMain.main_screen_yoffset, SystemMain.main_screen_xsize, SystemMain.main_screen_ysize);
		graphics.translate(SystemMain.main_screen_xoffset, SystemMain.main_screen_yoffset);
		if(use_main_screen_bgimage){
			draw_main_screen_background(graphics);
		}
		if(use_tile){
			for(int y_tile = 0;y_tile < SystemMain.screen_tile_ynum;y_tile++){
				for(int x_tile = 0;x_tile < SystemMain.screen_tile_xnum;x_tile++){
					graphics.drawImage(mapchips.get(map.getTile(x_tile, y_tile)), x_tile * SystemMain.tile_size, y_tile * SystemMain.tile_size, SystemMain.tile_size, SystemMain.tile_size,null);
				}
			}
		}
		if(use_compound_effect){
			process_compound_effects();
		}
		if(use_background_effect){
			draw_background_effects(graphics);
		}
		if(use_sprite){
			draw_sprites(graphics,map,0,100);
		}
		if(use_building){
			draw_buildings(graphics, map);
		}
		if(use_sprite){
			draw_sprites(graphics,map,101,1000);
		}
		if(use_foreground_effects){
			draw_foreground_effects(graphics);
			draw_surface_effects(graphics);
			draw_screen_effects(graphics);
			draw_ui_effects(graphics);
		}
		if(use_debug_info){
			draw_info(graphics,map.getPlayer().getLife(),map.stage_time,map.getPlayer().getGameScore());
		}
		if(use_infobar){
			graphics.setTransform(originalTransform);
			if(map.getScroll() == ScrollDirection.HORIZONTAL) {
				graphics.setClip(0, SystemMain.main_screen_ysize, SystemMain.infobar_xsize, SystemMain.infobar_ysize);
			}
			else if(map.getScroll() == ScrollDirection.VERTICAL) {
				graphics.setClip(SystemMain.main_screen_xoffset + SystemMain.main_screen_xsize, 0, SystemMain.infobar_xsize, SystemMain.infobar_ysize);
			}
			draw_infobar(graphics, map.getPlayer());
		}
		if(use_compound_effect || use_background_effect ||
				use_foreground_effects){
			effect_end_frame();
		}
		graphics.setClip(0, 0, SystemMain.main_screen_xsize, SystemMain.main_screen_ysize);
	}

	private void draw_whole_background(Graphics2D g) {
		g.drawImage(whole_bgimage,0,0,SystemMain.main_screen_xsize,SystemMain.main_screen_ysize,null);
	}

	//no longer used
	private void draw_buildings(Graphics2D graphics, Map map) {
		Building cur_building;
		BufferedImage cur_building_image;
		for(int y_tile = 0;y_tile < SystemMain.screen_tile_ynum;y_tile++){
			for(int x_tile = 0;x_tile < SystemMain.screen_tile_xnum;x_tile++){
				cur_building = map.getBuilding(x_tile, y_tile);
				try{
					switch(cur_building.category){
					case NONE:
						if(cur_building.type == 0) continue;
						cur_building_image = building_chips.get(cur_building.type);
						break;
					
					case HOUSE:
						cur_building_image = house_chips.get(cur_building.type);
						break;
					
					case BUSINESS:
						cur_building_image = business_chips.get(cur_building.type);
						break;
					
					case INDUSTRY:
						cur_building_image = industry_chips.get(cur_building.type);
						break;
					
					case WAY:
						cur_building_image = waychips.get(cur_building.type);
						break;
					
					case TRANSPORT:
						cur_building_image = transport_chips.get(cur_building.type);
						break;
					
					case PUBLIC:
						cur_building_image = public_chips.get(cur_building.type);
						break;
					
					case MILLITARY:
						cur_building_image = millitary_chips.get(cur_building.type);
						break;
					
					case NATURE:
						cur_building_image = nature_chips.get(cur_building.type);
						break;
					
					default:
						cur_building_image = building_chips.get(1);
						break;
					}
				}catch(IndexOutOfBoundsException e){
					cur_building_image = building_chips.get(1);
				}
				//System.out.printf("%d %d\n",x_tile,y_tile);
				graphics.drawImage(cur_building_image, x_tile * SystemMain.tile_size, y_tile * SystemMain.tile_size, SystemMain.tile_size, SystemMain.tile_size, null);
			}
		}
	}
	
	private void process_compound_effects(){
		effectRenderer.processCompoundEffect();
	}

	private void draw_background_effects(Graphics2D g) {
		effectRenderer.drawBackgroundEffect(g);
	}
	
	private void draw_foreground_effects(Graphics2D g) {
		effectRenderer.drawForegroundEffect(g);
	}
	
	private void draw_surface_effects(Graphics2D g) {
		effectRenderer.drawSurfaceEffect(g);
	}
	
	private void draw_screen_effects(Graphics2D g) {
		effectRenderer.drawScreenEffect(g);
	}
	
	private void draw_ui_effects(Graphics2D g) {
		effectRenderer.drawUIEffect(g);
	}
	
	private void effect_end_frame(){
		effectRenderer.endFrame();
	}

	private void draw_main_screen_background(Graphics2D g) {
		g.drawImage(main_screen_bgimage,0,0,SystemMain.main_screen_xsize,SystemMain.main_screen_ysize,null);
	}

	public void draw_sprites(Graphics2D g,Map map){
		int sprite_num = map.getSpriteNum();
		Sprite cur_sprite;
		for(int i = 0;i < sprite_num;i++){
			cur_sprite = map.getSprite(i);
			if(!cur_sprite.visiblity)continue;
			if(cur_sprite.flash){
				if((SystemMain.game_time % 2) != 0)continue;
			}
			g.drawImage(cur_sprite.getChip(), cur_sprite.getX_int() - cur_sprite.getXSize() / 2, cur_sprite.getY_int() - cur_sprite.getYSize() / 2, null);
			//System.out.printf("Current sprite %d size %dx%d\n",i,cur_sprite.getXSize(),cur_sprite.getYSize());
		}
	}
	
	public void draw_sprites(Graphics2D g,Map map,int z_start,int z_end){
		int sprite_num = map.getSpriteNum();
		Sprite cur_sprite;
		for(int i = 0;i < sprite_num;i++){
			cur_sprite = map.getSprite(i);
			if(!cur_sprite.visiblity)continue;
			if(cur_sprite.getZ_int() < z_start | cur_sprite.getZ_int() > z_end)continue;
			if(cur_sprite.flash){
				if((SystemMain.game_time % 2) != 0)continue;
			}
			g.drawImage(cur_sprite.getChip(), cur_sprite.getX_int() - cur_sprite.getXSize() / 2, cur_sprite.getY_int() - cur_sprite.getYSize() / 2, null);
			//System.out.printf("Current sprite %d size %dx%d\n",i,cur_sprite.getXSize(),cur_sprite.getYSize());
		}
	}
	
	public void draw_selected_tile_frame(Graphics2D graphics,int x_tile,int y_tile){
		graphics.setColor(Color.red);
		int x = x_tile * SystemMain.tile_size;
		int y = y_tile * SystemMain.tile_size;
		graphics.drawRect(x,y,SystemMain.tile_size-1,SystemMain.tile_size-1);
	}

	public void draw_info(Graphics2D g,int life,int stage_time,int gamescore) {
		if(use_debug_info){
			g.drawString(String.format("Life : %d", life), 0, 20);
			g.drawString(String.format("Time : %d", SystemMain.game_limit_time - stage_time),0,40);
			g.drawString(String.format("Score: %d", gamescore),0,60);
			g.drawString(String.format("StageScore: %d", SystemMain.game_clear_score),0,80);
		}
		if(gameover_flag){
			g.setColor(new Color(255,255,255));
			g.drawString(gameover_message, 250, 270);
		}
	}
	
	private void draw_infobar(Graphics2D g, Player player) {
		g.drawImage(infobar_bgimage,SystemMain.main_screen_xoffset + SystemMain.main_screen_xsize,0,SystemMain.infobar_xsize,SystemMain.infobar_ysize,null);
		g.setColor(new Color(255,120,120));
		g.fillRect((int)(SystemMain.main_screen_xoffset + SystemMain.main_screen_xsize + 30 + (double)SystemMain.game_time / SystemMain.game_limit_time * 164), 160, (int)(164 - (double)SystemMain.game_time / SystemMain.game_limit_time * 164), 30);
	}
	
	public void load_whole_bgimage(BufferedImage image) {
		whole_bgimage = image;
	}
	
	public void load_main_screen_bgimage(BufferedImage image) {
		main_screen_bgimage = image;
	}
	
	public void load_infobar_bgimage(BufferedImage image) {
		infobar_bgimage = image;
	}

	public void set_use_info_title(boolean b) {
		use_debug_info  = b;
	}
	
	public void set_use_infobar(boolean b) {
		use_infobar = b;
	}

	public void set_gameover(boolean b) {
		gameover_flag  = b;
	}
	
	public void set_effect_list(List<GraphicEffect> effects){
		effectRenderer.setEffectList(effects);
	}

	public void set_gameover_message(String string) {
		gameover_message = string;
	}
	
}

class TransparentFilter extends RGBImageFilter
{
	/**
	 * コンストラクタ
	 */
	public TransparentFilter()
	{
		canFilterIndexColorModel = true;
	}

	/**
	 * RGB ピクセルが黒であれば前景色に変更する。
	 *
	 * @param x 取り出し位置 X。
	 * @param y 取り出し位置 Y。
	 * @param rgb 変更前の RGB ピクセル。
	 * @return 変更後の RGB ピクセル。
	 */
	public int filterRGB(int x,int y,int rgb)
	{
		Color c = new Color(rgb);
		if(c.getRed() != 1 || c.getGreen() != 0  || c.getBlue() != 0)
			c = new Color(c.getRed(), c.getGreen(), c.getBlue(),
 							180);
		else
			c = new Color(0, 0, 0, 0);
		return c.getRGB();
	}
}
