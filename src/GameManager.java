public class GameManager implements IMovableManager {
	private KeyboardManager keyboardManager;
	private Board boardModel;
	private BoardView boardView;

	public GameManager(BoardView view, Board board, KeyboardManager keyboardManager) {
		this.boardView = view;
		this.boardModel = board;
		this.keyboardManager = keyboardManager;
	}

	public void start() {
		keyboardManager.addKeyBindings(boardView.getRootPane());
		addRandomTile();
	}

	private void addRandomTile() {
		Tile t = boardModel.addRandomTile();

		if (t.value == -1) {
			return;
		} else {
			boardView.addTile(t);
		}
	}

	private void endGame() {
		keyboardManager.removeAllKeyBindings(boardView.getRootPane());
		boardView.showGameOver();
	}

	@Override
	public void left() {
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.LEFT);
		boardView.update(updatedBoard);
		nextRound();
	}

	@Override
	public void right() {
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.RIGHT);
		boardView.update(updatedBoard);
		nextRound();
	}

	@Override
	public void up() {
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.UP);
		boardView.update(updatedBoard);
		nextRound();
	}

	@Override
	public void down() {
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.DOWN);
		boardView.update(updatedBoard);
		nextRound();
	}

	private void nextRound() {
		if (!boardModel.isGameOver()) {
			addRandomTile();
		} else {
			endGame();
		}
	}
}
