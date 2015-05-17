import processing.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.function.*;

public class Main extends PApplet {
	
	
	final boolean RUN_AFTER_LOAD = true;
	final String IMAGE_LIST_FILE_NAME = "imagelist";
	final String WORLD_FILE = "gaia.sav";
	
	final int WORLD_WIDTH_SCALE = 2;
	final int WORLD_HEIGHT_SCALE = 2;
	final int SCREEN_WIDTH = 640;
	final int SCREEN_HEIGHT= 480;
	final int TILE_WIDTH = 32;
	final int TILE_HEIGHT = 32;
	private WorldView view;
	private WorldModel world;
	private List<PImage>img;
	private List<PImage>e;
	
	private HashMap <String, List<PImage>> map;
	
	private static final int MIN_ARGS = 1;
	
	final static String DEFAULT_IMAGE_NAME = "background_default";
	
	private boolean run;
	
	final int PROPERTY_KEY = 0;

    private String BGND_KEY = "background";
	final int BGND_NUM_PROPERTIES = 4;
	final int BGND_NAME = 1;
	final int BGND_COL = 2;
	final int BGND_ROW = 3;

	final String MINER_KEY = "miner";
	final int MINER_NUM_PROPERTIES = 7;
	final int MINER_NAME = 1;
	final int MINER_LIMIT = 4;
	final int MINER_COL = 2;
	final int MINER_ROW = 3;
	final int MINER_RATE = 5;
	final int MINER_ANIMATION_RATE = 6;

	final String OBSTACLE_KEY = "obstacle";
	final int OBSTACLE_NUM_PROPERTIES = 4;
	final int OBSTACLE_NAME = 1;
	final int OBSTACLE_COL = 2;
	final int OBSTACLE_ROW = 3;

	final String ORE_KEY = "ore";
	final int ORE_NUM_PROPERTIES = 5;
	final int ORE_NAME = 1;
	final int ORE_COL = 2;
	final int ORE_ROW = 3;
	final int ORE_RATE = 4;

	final String SMITH_KEY = "blacksmith";
	final int SMITH_NUM_PROPERTIES = 7;
	final int SMITH_NAME = 1;
	final int SMITH_COL = 2;
	final int SMITH_ROW = 3;
	final int SMITH_LIMIT = 4;
	final int SMITH_RATE = 5;
	final int SMITH_REACH = 6;

	final String VEIN_KEY = "vein";
	final int VEIN_NUM_PROPERTIES = 6;
	final int VEIN_NAME = 1;
	final int VEIN_RATE = 4;
	final int VEIN_COL = 2;
	final int VEIN_ROW = 3;
	final int VEIN_REACH = 5;
	
	private List<PImage> MYIMAGE;
	
	public Background create_default_background(List<PImage> img)
	{
		Background b = new Background(DEFAULT_IMAGE_NAME, img);
		return b;
	}
	
	public void setup()
	{
		
		size(SCREEN_WIDTH, SCREEN_HEIGHT);
		PApplet screen = this;
		
		int num_cols = SCREEN_WIDTH / TILE_WIDTH * WORLD_WIDTH_SCALE;
		int num_rows = SCREEN_HEIGHT / TILE_HEIGHT * WORLD_HEIGHT_SCALE;

		
		try
	      {
	         
	            Scanner in = new Scanner(new FileInputStream(WORLD_FILE));
	            useScanner(in);
	         
	         
	      }
	      catch (FileNotFoundException e)
	      {
	         System.err.println(e.getMessage());
	      }
	    try
	    {
	      
	          Scanner im = new Scanner(new FileInputStream(IMAGE_LIST_FILE_NAME));
	          useScannerImages(im);
	      
	    }
	    catch (FileNotFoundException e)
	    {
	       System.err.println(e.getMessage());
	    }
	    
		
		
		Background default_background = create_default_background(
				get_images(map, DEFAULT_IMAGE_NAME));
		
		world = new WorldModel(num_rows, num_cols, default_background);
	    view = new WorldView(SCREEN_WIDTH/TILE_WIDTH, SCREEN_HEIGHT/TILE_HEIGHT,
				screen, world, TILE_WIDTH, TILE_HEIGHT);
	    
	   
	    
	    
	    
	    
	}
	
	private void useScanner(Scanner in)
	{
		while(in.hasNextLine())
		{
			String [] properties;
			properties = in.nextLine().split("\\s");
			//System.out.println(properties);
			if(properties != null)
			{
				Scanner new_one = new Scanner(IMAGE_LIST_FILE_NAME);
				if(properties[PROPERTY_KEY].equals(BGND_KEY))
				{
				    
					add_background(world, properties, useScannerImages(new_one));
				}
				else
				{
					
					add_entity(world, properties, useScannerImages(new_one), run);
				}
			}
			
		}
		
	}
	

	protected HashMap<String, List<PImage>> useScannerImages(Scanner in)
	{
		 map = new HashMap<String, List<PImage>>();
		while(in.hasNextLine())
		{
			
			String [] line = in.nextLine().split("\\s");
			process_image_line(map, line);
			
			
		}
		
		return map;
	}
	protected void process_image_line(HashMap<String, List<PImage>> map, String[] line)
	{
		String key;
		String image_file_name;
		PImage image;
		
		
		
		key = line[0];
		image_file_name = line[1];
		image = loadImage(image_file_name);
		
		
		
		if (image != null)
		{
			
			
			List<PImage>images = get_images_internal(map, key);
			images.add(image);
			map.put(key, images);
			
			
			
			
			
			if(line.length == 6)
			{
			   int r = Integer.parseInt(line[2]);
			   int g = Integer.parseInt(line[3]);
			   int b = Integer.parseInt(line[4]);
			   int a = Integer.parseInt(line[5]);
			   //look at the color coding thing on webste for this part
			}
		}
		
	}
	private List<PImage> get_images_internal(HashMap<String, List<PImage>> map, String key)
	{
		List<PImage> this_object_list = new ArrayList<PImage>();
		
		if(map.containsKey(key))
		{
			List<PImage> object_image = map.get(key);
			//System.out.println(object_image);
			return object_image;
			
		}
		else
		{
			return this_object_list;
		}
		
	}
	public static List<PImage> get_images(HashMap<String, List<PImage>> map, String key)
	{
		System.out.println(map);
		if(map.containsKey(key))
		{
			
           List<PImage> object_image = map.get(key);	
			return object_image;
			
		}
		else {
			 List<PImage> object_image = map.get(DEFAULT_IMAGE_NAME);
			 return object_image;
			 
		}
	}
	
	public void add_background(WorldModel world, String[] properties, HashMap <String, List<PImage>> map)
	{
		if(properties.length >= BGND_NUM_PROPERTIES)
		{
			Point pt = new Point(BGND_COL, BGND_ROW);
			String name = properties[BGND_NAME];
			Background b = new Background(name, get_images(map, name));
			world.set_background(pt, b);
		}
	}
	public void add_entity(WorldModel world, String[] properties, HashMap <String, List<PImage>> map,  boolean run)
	{
		Entity new_entity = create_from_properties(properties, map);
		System.out.println(map);
		if(new_entity != null)
		{
			world.add_entity(new_entity);
			if(run)
			{
				schedule_entity(world, new_entity, map);
			}
			
		}	
				
	}
	public Entity create_from_properties(String[] properties, HashMap <String, List<PImage>> map)
	{
		String key = properties[PROPERTY_KEY];
		
		if(properties != null)
		{
			if(key.equals(MINER_KEY))
			{
				//System.out.println("IT CAME HERE");
				return create_miner(properties, map);
			}
			else if(key.equals(VEIN_KEY))
			{
				return create_vein(properties, map);
			}
			else if(key.equals(ORE_KEY))
			{
				return create_ore(properties, map);
			}
			else if(key.equals(SMITH_KEY))
			{
				//System.out.println("it ran here");
				return create_blacksmith(properties, map);
			}
			else if(key.equals(OBSTACLE_KEY))
			{
				return create_obstacle(properties, map);
			}
		}
	    
		return null;
	}
	public Miner create_miner(String[] properties, HashMap <String, List<PImage>> map)
	{
		
		if(properties.length == MINER_NUM_PROPERTIES)
		{
			Point p = new Point(Integer.parseInt(properties[MINER_COL]), Integer.parseInt(properties[MINER_ROW]));
			Miner miner = new Miner(properties[MINER_NAME], p, Integer.parseInt(properties[MINER_RATE]), Integer.parseInt(properties[MINER_LIMIT]),
					  0,Integer.parseInt(properties[MINER_ANIMATION_RATE]), get_images(map, properties[PROPERTY_KEY]));
			return miner;
		}
		else
		{
			return null;
		}
			
	}
	public Vein create_vein(String[] properties, HashMap <String, List<PImage>> map)
	{
		if(properties.length == VEIN_NUM_PROPERTIES)
		{
			Point p = new Point(Integer.parseInt(properties[VEIN_COL]), Integer.parseInt(properties[VEIN_ROW]));
			Vein vein = new Vein(properties[VEIN_NAME], Integer.parseInt(properties[VEIN_RATE]), p,
					Integer.parseInt(properties[VEIN_REACH]), get_images(map, properties[PROPERTY_KEY]));
			return vein;
		}
		else
		{
			return null;
		}
		
	}
	public Ore create_ore(String[] properties, HashMap <String, List<PImage>> map)
	{
		if(properties.length == ORE_NUM_PROPERTIES)
		{
			Point p = new Point(Integer.parseInt(properties[ORE_COL]), Integer.parseInt(properties[ORE_ROW]));
			Ore ore = new Ore(properties[ORE_NAME], p, Integer.parseInt(properties[ORE_RATE]), get_images(map, properties[PROPERTY_KEY]));
			return ore;
		}
		else
		{
			return null;
		}
	}
	public Blacksmith create_blacksmith(String[] properties, HashMap <String, List<PImage>> map)
	{
		if(properties.length == SMITH_NUM_PROPERTIES)
		{
			Point p = new Point(Integer.parseInt(properties[SMITH_COL]), Integer.parseInt(properties[SMITH_ROW]));
			Blacksmith smith = new Blacksmith(properties[SMITH_NAME], p, get_images(map, properties[PROPERTY_KEY]), Integer.parseInt(properties[SMITH_LIMIT]),
					Integer.parseInt(properties[SMITH_RATE]), Integer.parseInt(properties[SMITH_REACH]), 0);
			System.out.println(smith);
			return smith;
		}
		else
		{
			return null;
		}
	}
	public Obstacle create_obstacle(String[] properties, HashMap <String, List<PImage>> map)
	{
		if(properties.length == OBSTACLE_NUM_PROPERTIES)
		{
			Point p = new Point(Integer.parseInt(properties[OBSTACLE_COL]), Integer.parseInt(properties[OBSTACLE_ROW]));
			Obstacle obstacle = new Obstacle(properties[OBSTACLE_NAME],p, get_images(map, properties[PROPERTY_KEY]));
			return obstacle;
		}
		else
		{
			return null;
		}
	}
	public void schedule_entity(WorldModel world, Entity entity, HashMap <String, List<PImage>> map)
	{
		long newtick = 0;
		if(entity instanceof MinerNotFull)
		{
			
			((Miner) entity).schedule_miner(world, newtick, map);
		}
		else if(entity instanceof Vein)
		{
			
			((Vein) entity).schedule_vein(world, newtick, map);
		}
		else if(entity instanceof Ore)
		{
			((Ore) entity).schedule_ore(world, newtick, map);
		}
	}
		
	
	
	
	
	public void draw()
	{
		
	   view.draw_viewport();	
	   
	}
	public static void main(String[] args)
	   {
	      PApplet.main("Main");
	      
	   }
	   
}

