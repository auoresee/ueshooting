package ueshooting.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import ueshooting.graphic.Drawer;
import ueshooting.map.BuildingCategory;
import ueshooting.map.Map;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements MouseListener {
	Drawer drawer_copy;
	Map map_copy;
	int inited = 0;
	Point point_copy;
	Image buf;
	Graphics ct;
	int x_tile,y_tile;
	BuildingCategory cur_category = BuildingCategory.HOUSE;
	int cur_type = 0;
	
	
	GameScreen(Map p_map,Drawer p_drawer,Point p_point){
		drawer_copy = p_drawer;
		map_copy = p_map;
		point_copy = p_point;
		inited = 1;
		setPreferredSize(new Dimension(640,480));
		addMouseListener(this);
	}
	
	public void setBuildingType(BuildingCategory category,int type){
		cur_category = category;
		cur_type = type;
	}
	
	public void setBuildingType(int type){
		cur_type = type;
	}
	
	public void init(){
		buf = createImage(640,480);
	}
	
	//public void update(Graphics g){
	//	paint(g);
	//}
	
	public void paintComponent(Graphics g){
		if(buf == null){
			ct = g;
		}else{
			ct = buf.getGraphics();
		}
	    drawer_copy.draw((Graphics2D)ct,map_copy);
		//if((point_copy.x >= 0) & (point_copy.y >= 0)){
		//	drawer_copy.draw_selected_tile_frame((Graphics2D)ct,SystemMain.get_tile_x(point_copy.x),SystemMain.get_tile_y(point_copy.y));
		//}
	    
		g.drawImage(buf, 0, 0, this);
	}

	/*@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		point = new Point(arg0.getX(),arg0.getY());
	}*/
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//x_tile = SystemMain.get_tile_x(arg0.getX());
		//y_tile = SystemMain.get_tile_x(arg0.getY());
		
		//if(map_copy != null){
		//	map_copy.build(cur_category , cur_type, x_tile, y_tile);
		//}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}