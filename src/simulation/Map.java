package simulation;

public class Map {
	final private int height;
	final private int width;
	
	private boolean[][] obstacleMap;
	
	public Map(int height, int width) {
		this.height = height;
		this.width = width;
	}
	
	public void addObstacleMap(boolean[][] map) {
		assert(map.length == width);
		assert(map[1].length == height);
		
		this.obstacleMap = map;
	}
	
	public boolean[][] getObstacleMap(){
		return obstacleMap;
	}
}
