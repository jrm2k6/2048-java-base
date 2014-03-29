public class GameManager implements IMovableManager {
	private KeyboardManager keyboardManager;
	private Board boardModel;
	private BoardView boardView;

	private boolean isFirstRound=true;

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
		if (!boardModel.canMoveLeft() && !isFirstRound) {
			return;
		}
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.LEFT);
		boardView.update(updatedBoard);
		nextRound();
		isFirstRound = false;
	}

	@Override
	public void right() {
		if (!boardModel.canMoveRight() && !isFirstRound) {
			return;
		}
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.RIGHT);
		boardView.update(updatedBoard);
		nextRound();
		isFirstRound = false;
	}

	@Override
	public void up() {
		if (!boardModel.canMoveUp() && !isFirstRound) {
			return;
		}
		Tile[][] updatedBoard = boardModel.moveGridTo(Direction.UP);
		boardView.update(updatedBoard);
		nextRound();
		isFirstRound = false;
	}

	@Override
	public void down() {
		if (!boardModel.canMoveDown() && !isFirstRound) {
			return;
		}
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
