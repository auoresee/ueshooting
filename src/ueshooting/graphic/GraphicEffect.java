package ueshooting.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ueshooting.system.SystemMain;

public class GraphicEffect {

	public static boolean isPlayingSpellcard = false;
	public GraphicEffectType type;
	public double timeLength = 1;
	public double xPos;
	public double yPos;
	public double size;
	public double xSize;
	public double ySize;
	public double radius;
	public Color color = null;
	public Object[] parameters;
	public double[] variables = new double[20];
	public List<GraphicEffect> children;
	public boolean isTimeLengthVariable = false;
	public boolean isFinished = false;
	
	public int time = 0;
	public double timeExtended = 0;
	
	public GraphicEffect(GraphicEffectType p_type){
		type = p_type;
	}

	public GraphicEffect(double p_timeLength){
		timeLength = p_timeLength;
	}
	
	public void setTimeLength(double p_len){
		timeLength = p_len;
	}
	
	public void setPosition(double p_x, double p_y){
		xPos = p_x;
		yPos = p_y;
	}
	
	public void setSingleSize(double p_size){
		size = p_size;
	}
	
	public void setXYSize(double p_xsize, double p_ysize){
		xSize = p_xsize;
		ySize = p_ysize;
	}
	
	public void setRadius(double r){
		radius = r;
	}
	
	public void setColor(int r,int g,int b){
		color = new Color(r,g,b);
	}

	public boolean isDone() {
		//長さが変則的な変化をしない場合
		if(!isTimeLengthVariable){
			return time >= timeLength;
		}
		else{
			switch(type){
			
			default:
				if(isFinished) return true;
				return false;
			}
		}
	}

	public List<GraphicEffect> runCompoundEffect() {
		List<GraphicEffect> added_effects = new ArrayList<>();
		GraphicEffect neweffect;
		switch(type){
		case SPELLCARD_START:
			if(time == 0){
				neweffect = new GraphicEffect(GraphicEffectType.TRANSIT_BACKGROUND);
				neweffect.parameters = new Object[1];
				neweffect.setTimeLength(50);
				//画像(p0)はあらかじめ元のエフェクトに代入してある
				neweffect.parameters[0] = parameters[1];
				added_effects.add(0, neweffect);

				neweffect = new GraphicEffect(GraphicEffectType.DRAW_STRING);
				neweffect.parameters = new Object[1];
				neweffect.parameters[0] = parameters[0];
				added_effects.add(0, neweffect);

				neweffect = new GraphicEffect(GraphicEffectType.FLASH);
				neweffect.setTimeLength(10);
				added_effects.add(0, neweffect);
				isPlayingSpellcard = true;
			}
			break;
			
		case SPELLCARD_END:
			if(time == 0){
				neweffect = new GraphicEffect(GraphicEffectType.TRANSIT_BACKGROUND);
				neweffect.setTimeLength(30);
				//画像(p0)はあらかじめ元のエフェクトに代入してある
				neweffect.parameters = new Object[1];
				neweffect.parameters[0] = parameters[0];
				added_effects.add(0, neweffect);

				neweffect = new GraphicEffect(GraphicEffectType.FLASH);
				neweffect.setTimeLength(10);
				added_effects.add(0, neweffect);
				isPlayingSpellcard = false;
			}
			break;
			
		case BOSS_BATTLE_END:
			if(time == 0){
				if(isPlayingSpellcard){
					neweffect = new GraphicEffect(GraphicEffectType.TRANSIT_BACKGROUND);
					neweffect.setTimeLength(30);
					//画像(p0)はあらかじめ元のエフェクトに代入してある
					neweffect.parameters = new Object[1];
					neweffect.parameters[0] = parameters[0];
					added_effects.add(0, neweffect);
					isPlayingSpellcard = false;
				}
				neweffect = new GraphicEffect(GraphicEffectType.FLASH);
				neweffect.setTimeLength(10);
				added_effects.add(0, neweffect);
			}
			break;
			
		case DRAW_SPELLCARD_NAME:
			if(time == 0){
				this.isTimeLengthVariable = true;
				neweffect = new GraphicEffect(GraphicEffectType.DRAW_STRING);
				neweffect.setColor(255, 255, 255);
				neweffect.setPosition(0, 0);
				neweffect.setSingleSize(12);
				neweffect.parameters[0] = this.parameters[0];

				neweffect.isTimeLengthVariable = true;
				added_effects.add(0, neweffect);
				children.add(neweffect);
			}
			if(!isPlayingSpellcard){
				children.get(0).isFinished = true;
				this.isFinished = true;
			}
			break;
			
		default:
		}
		
		return added_effects;
	}

	public void runBackgroundEffect(Graphics2D g) {
		switch(type){
		//TODO: 2重にこのエフェクトを実行した場合は古いほうが中止されるようにする
		case TRANSIT_BACKGROUND:
			if(time == 0){
				Drawer.instance.load_main_screen_bgimage((BufferedImage) parameters[0]);
			}
			break;
			
		default:
		}
	}

	public void runForegroundEffect(Graphics2D g) {
		switch(type){
		case EXPLODE:
			break;
			
		default:
		}
	}
	
	public void runSurfaceEffect(Graphics2D g) {
		switch(type){
			
		default:
		}
	}

	public void runScreenEffect(Graphics2D g) {
		switch(type){
		case FLASH:
			flash(g);
			break;
			
		default:
		}
	}
	
	public void runUIEffect(Graphics2D g) {
		switch(type){
		case DRAW_STRING:
			if(color != null) {
				g.setColor(color);
			}
			drawString(g,(String)parameters[0],xPos,yPos,size);
			break;
			
		default:
		}
	}
	
	
	private void drawString(Graphics2D g, String string, double x, double y,
			double fontsize) {
		g.setFont(new Font("", 0, (int)fontsize));
		g.drawString(string, (float)x, (float)y);
	}

	private void flash(Graphics2D g) {
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, SystemMain.main_screen_xsize, SystemMain.main_screen_ysize);
	}

}
