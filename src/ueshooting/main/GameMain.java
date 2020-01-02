package ueshooting.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import ueshooting.graphic.Drawer;
import ueshooting.graphic.GraphicEffect;
import ueshooting.io.FileLoader;
import ueshooting.io.StageLoader;
import ueshooting.io.UnsupportedStageFileException;
import ueshooting.map.*;
import ueshooting.sound.SoundManager;
import ueshooting.sprite.EnemyGenerator;
import ueshooting.sprite.Player;
import ueshooting.sprite.PlayerListener;
import ueshooting.sprite.Sprite;
import ueshooting.sprite.SpriteCategory;
import ueshooting.stage.Stage;
import ueshooting.stage.StageData;
import ueshooting.system.SystemMain;

import javax.sound.sampled.*;

@SuppressWarnings("serial")
public class GameMain extends JFrame implements ActionListener,KeyListener,PlayerListener {

	//private static final String starting_map = "map\\map.txt";
	private static final String stage_dir = "stage\\";
	static GameMain frame;
	Drawer drawer = Drawer.instance;
	SoundManager soundManager = new SoundManager();
	public Map map;
	StageData stage_data;
	EnemyGenerator generator = new EnemyGenerator(1,3);
	GameScreen screen;
	Timer timer;
	Player player;
	Point point = new Point();
	private boolean[] key_states = new boolean[7];
	
	public static void main(String[] args) {
		frame = new GameMain("タイトル");
	}
	
	void load_sprite_chips(SpriteCategory category) throws IOException{
		String filename;
		Image image;
		BufferedImage bimage;
		for(int i = 0;;i++){
			filename = Sprite.getChipPath(category,i);
			if(!new File(filename).exists()){
				break;
			}
			image = Toolkit.getDefaultToolkit().getImage(filename);
			
			// java.awt.MediaTracker でロードを待機
			MediaTracker tracker = new MediaTracker(new Component(){});
			tracker.addImage(image, 0);
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			drawer.load_sprite_chip(image,category);
			System.out.printf("Read %d\n",i);
		}
	}
	
	//クリップは名前順で登録されるので、番号が名前順に一致するようにする(例: shot_01.wav, shot_02.wav)
	private void load_sound_clips() {
		String path = "sound";
	    File dir = new File(path);
	    String[] files = dir.list();
		
	    for(String filename : files){
	    	try {
	    		String category = getSoundCategory(filename);
            	soundManager.loadClip(path + "\\" + filename, category, 2);
			} catch(Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	private String getSoundCategory(String filename) {
		String[] list;
		list = filename.split("_");
		return list[0];
	}
	
	private void load_BGM(java.util.List<String> files) {
		
	    for(String filename : files){
	    	try {
	            final AudioInputStream fis =
	                    AudioSystem.getAudioInputStream(new File(filename));
	            System.out.println("File AudioFormat: " + fis.getFormat());
	            final AudioInputStream ais = AudioSystem.getAudioInputStream(
	                    AudioFormat.Encoding.PCM_SIGNED, fis);
	            soundManager.registerBGM(ais);
	        } catch (final Exception e) {
	            System.out.println(e);
	        }

	    }
	}

	boolean load_stages(){
		File file;
		
		BufferedImage image;
		Sprite.init();
		StageLoader loader = null;
		List<String> string = null;
		try {
			loader = new StageLoader();
			string = FileLoader.loadStrings(stage_dir + "stage_list.txt");
		} catch (IOException e){
			if((e instanceof FileNotFoundException) | (e instanceof NoSuchFileException)) {
				System.out.println("stage\\stage_list.txtが見つかりませんでした。");
				return false;
			}
			e.printStackTrace();
			return false;
		}
		
		try {
			List<String> stage_names = loader.loadStageFile(string);
			stage_data = new StageData(stage_names.size());
			for(int i = 0;i < stage_data.getStageNum();i++){
				stage_data.setStage(loader.loadStage(FileLoader.loadBytes(stage_dir + stage_names.get(i))),i);
			}
		} catch (IOException e) {
			if((e instanceof FileNotFoundException) | (e instanceof NoSuchFileException)) {
				System.out.println("stage\\stage_list.txtが見つかりませんでした。");
				return false;
			}
			e.printStackTrace();
		} catch (UnsupportedStageFileException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*private void load_map(String mapName) {
		ArrayList<String> map_text = new ArrayList<String>();
		try{
			File file = new File(mapName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String temp;
			while((temp = br.readLine()) != null){
				map_text.add(temp);
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		int[] temp_value = getNumbers(map_text.get(0));
		int x_tilesize = temp_value[0];
		int y_tilesize = temp_value[1];
		for(int y = 0;y < y_tilesize;y++){
			temp_value = getNumbers(map_text.get(y+1));
			for(int x = 0;x < x_tilesize;x++){
				map.setTile(temp_value[x],x,y);
			}
		}
	}

	private int[] getNumbers(String p_str) {
		String[] str = p_str.split(",");
		int[] numbers = new int[str.length];
		for(int i = 0;i < str.length;i++){
			numbers[i] = Integer.parseInt(str[i]);
		}
		return numbers;
	}*/

	GameMain(String title){
	    setTitle(title);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    
	    JLayeredPane layer = new JLayeredPane();
	    
	    load_stages();
	    SystemMain.soundManager = soundManager;
	    Stage cur_stage = load_stage(0);
	    map = new Map(cur_stage);
		generator.set_stage(cur_stage);
		//SystemMain.game_time = cur_stage.getStartingFrame();
		map.stage_time = cur_stage.getStartingFrame();
		//デバッグ用
		List<String> l = new ArrayList<>();
		l.add("music/un.wav");
		load_files(cur_stage.get_bg_path(), l);
		//正式
		//load_files(cur_stage.get_bg_path(), cur_stage.get_bgm_path_list());
		
	    drawer.set_use_main_screen_bgimage(true);
	    drawer.set_use_tile(false);
	    drawer.set_use_building(false);
	    drawer.set_use_sprite(true);
	    drawer.set_use_infobar(true);
	    
	    player = new Player(320,400,cur_stage.getDebugInvincible());
	    player.setPlayerlistener(this);
	    player.create();
	    map.setPlayer(player);
	    
	    List<GraphicEffect> effects = new ArrayList<GraphicEffect>();
	    map.setEffectList(effects);
	    drawer.set_effect_list(effects);
	    
	    //Insets insets = getInsets();
	    Container contentPane = getContentPane();
	    contentPane.setPreferredSize(new Dimension(SystemMain.window_clientarea_xsize,SystemMain.window_clientarea_ysize));
	    set_components(contentPane,layer);
	    //contentPane.add(screen);
	    setVisible(true);
	    pack();
		setLocationRelativeTo(null);
		
		addKeyListener(this);
		screen.init();
		timer = new Timer(17 , this);
		timer.start();
		
		soundManager.playBGM(cur_stage.get_first_bgm());
	}

	private boolean load_files(String bg_path,List<String> bgm_path_list) {
		try {
			File file;
			if(bg_path != null && bg_path.length() > 0) {
				file = new File(bg_path);
			}
			else {
				file = new File("picture\\BG3_1.png");
			}
			BufferedImage image = ImageIO.read(file);
			drawer.load_main_screen_bgimage(image);
			map.setNormalBGImage(image);
			file = new File("picture\\bgimage2.png");
			image = ImageIO.read(file);
			map.setSpellcardBGImage(image);
			image = ImageIO.read(new File("gui\\infobar.png"));
			drawer.load_infobar_bgimage(image);
			//file = new File("mapchip\\mapchips.png");
			//image = ImageIO.read(file);
			//drawer.load_mapchips(image);
			//file = new File("mapchip\\building_chips.gif");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.NONE);
			//file = new File("mapchip\\house_chips.gif");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.HOUSE);
			//file = new File("mapchip\\business_chips.png");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.BUSINESS);
			//file = new File("mapchip\\waychips.png");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.WAY);
			//file = new File("mapchip\\transport_chips.png");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.TRANSPORT);
			//file = new File("mapchip\\public_chips.png");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.PUBLIC);
			//file = new File("mapchip\\millitary_chips.png");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.MILLITARY);
			//file = new File("mapchip\\nature_chips.png");
			//image = ImageIO.read(file);
			//drawer.load_building_chips(image,BuildingCategory.NATURE);
			load_sprite_chips(SpriteCategory.SHOT);
			load_sprite_chips(SpriteCategory.NPC);
			load_sprite_chips(SpriteCategory.OTHER);
			
			//load_map(starting_map);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.printf("Array!\n");
			return false;
		}
		
		load_BGM(bgm_path_list);
		load_sound_clips();
		return true;
	}

	private Stage load_stage(int i) {
		return stage_data.getStage(i);
	}

	private void set_components(Container contentPane, JLayeredPane layer) {
		screen = new GameScreen(map,drawer,point);
		//toolbar = new JPanel();
		//toolbar.setPreferredSize(new Dimension(128,480));
		//toolbar.setBackground(new Color(32,32,32));
		//GridBagLayout gb=new GridBagLayout();
	   // GridBagConstraints gc=new GridBagConstraints();
	    //toolbar.setLayout(gb);
	    
		//JToggleButton building_button = new JToggleButton(new ImageIcon("gui\\building_button.gif"));
		//building_button.setPressedIcon(new ImageIcon("gui\\building_button_gray.gif"));
		//building_button.setSelectedIcon(new ImageIcon("gui\\building_button_gray.gif"));
		//building_button.setContentAreaFilled(false);
		//building_button.setBorderPainted(false);
		//building_button.setFocusPainted(false);
		//building_button.addChangeListener(this);
		//JButton debug_button = new JButton(new ImageIcon("gui\\building_button.gif"));
		//debug_button.addActionListener(this);
		//contentPane.setLayout(new BorderLayout());
		contentPane.add("West",screen);
		//contentPane.add("East",toolbar);
		//toolbar.setLayout(new GridLayout(10,2));
		//toolbar.add(building_button);
		//toolbar.add(debug_button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton){
			//JButton btn = (JButton) e.getSource();
			debug_map_show();
		}
		Point temp_point = getMousePosition();
		if(temp_point == null){
			point.x = -1;
			point.y = -1;
		}else{
			convert_point_set(point,temp_point);
		}
		//System.out.printf("%d\n", SystemMain.game_time % 120);
		if((SystemMain.game_time % 120) == 0)generator.generate(map);
		map.action();
		screen.repaint();
		SystemMain.game_time++;
		if(map.stageClearFlag)game_clear();
		if(SystemMain.game_limit_time - SystemMain.game_time <= 0)game_over(1);
	}

	private void game_clear() {
		timer.stop();
		drawer.set_use_building(false);
		drawer.set_use_tile(false);
		drawer.set_use_sprite(false);
		File file = new File("picture\\game_clear.png");
		BufferedImage image;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		drawer.load_main_screen_bgimage(image);
		screen.repaint();
	}

	private void debug_map_show() {
		/*for(int y = 0;y < SystemMain.screen_tile_ynum;y++){
			for(int x = 0;x < SystemMain.screen_tile_xnum;x++){
				System.out.printf("%4d",map.map[x][y]);
			}
			System.out.printf("\n");
		}*/
	}

	public Point convert_point(Point window_pos){
		Point temp_point = new Point();
		Insets insets = getInsets();
		temp_point.x = window_pos.x - insets.left;
		temp_point.y = window_pos.y - insets.top;
		return point;
	}
	
	public Point convert_point_set(Point client_pos,Point window_pos){
		Insets insets = getInsets();
		client_pos.x = window_pos.x - insets.left;
		client_pos.y = window_pos.y - insets.top;
		return point;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key_code = e.getKeyCode();
		if(key_code == KeyEvent.VK_Z)key_states[0] = true;
		if(key_code == KeyEvent.VK_X)key_states[1] = true;
		if(key_code == KeyEvent.VK_LEFT)key_states[2] = true;
		if(key_code == KeyEvent.VK_RIGHT)key_states[3] = true;
		if(key_code == KeyEvent.VK_UP)key_states[4] = true;
		if(key_code == KeyEvent.VK_DOWN)key_states[5] = true;
		if(key_code == KeyEvent.VK_SHIFT)key_states[6] = true;
		player.input(key_states);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key_code = e.getKeyCode();
		if(key_code == KeyEvent.VK_Z)key_states[0] = false;
		if(key_code == KeyEvent.VK_X)key_states[1] = false;
		if(key_code == KeyEvent.VK_LEFT)key_states[2] = false;
		if(key_code == KeyEvent.VK_RIGHT)key_states[3] = false;
		if(key_code == KeyEvent.VK_UP)key_states[4] = false;
		if(key_code == KeyEvent.VK_DOWN)key_states[5] = false;
		if(key_code == KeyEvent.VK_SHIFT)key_states[6] = false;
		player.input(key_states);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void player_died() {

	}

	@Override
	public void player_gameover() {
		game_over(0);
	}

	private void game_over(int cause) {
		timer.stop();
		drawer.set_use_building(false);
		drawer.set_use_tile(false);
		drawer.set_use_sprite(false);
		drawer.set_use_infobar(true);
		File file = new File("picture\\game_over.png");
		BufferedImage image;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		drawer.load_main_screen_bgimage(image);
		drawer.set_gameover(true);
		if(cause == 1){
			drawer.set_gameover_message("Time Up!");
		}else{
			drawer.set_gameover_message("You died...");
		}
		screen.repaint();
	}

	//@Override
	/*public void stateChanged(ChangeEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		if (btn.isSelected()) {
			System.out.println("Selected");
	         screen.setBuildingType(1);
	      } else {
	    	 screen.setBuildingType(0);
	    }
	}*/
}

@SuppressWarnings("serial")
class GameScreen extends JPanel implements MouseListener {
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

@SuppressWarnings("serial")
class ToolBar extends JPanel {
	ToolBar(){
		
	}
}