public class Main {
	public static void main(String[] args) {
		BoardView view = new BoardView();
		Board board = new Board(GameConstants.GAME_DIMENSION);
		KeyboardManager keyboardManager = new KeyboardManager();
		GameManager manager = new GameManager(view, board, keyboardManager);
		keyboardManager.setWidget(manager);
		manager.start();
	}
}
