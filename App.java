
public class App {
	
	private static final int WIDTH = 10;
	private static final int HEIGHT = 20;
	private static final int PIXEL_SIZE = 20;
	private static final int MOVEMENT_SPEED = 400;
	
	public static void main(String[] args) {
		Field field = new Field(PIXEL_SIZE, WIDTH, HEIGHT, MOVEMENT_SPEED);
		Game game = new Game(field);
		field.setFieldParent(game);
		game.setVisible(true);
	}
}
