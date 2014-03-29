import java.awt.*;

public class Board {
	private GameStateChecker gameStateChecker;
	private MergeTracker mergeTracker;
	private Tile[][] tiles;
	private RandomCellProvider provider;
	private int dimension;

	public Board(int _dimension) {

		dimension = _dimension;
		tiles = new Tile[dimension][dimension];
		provider = new RandomCellProvider();
		gameStateChecker = new GameStateChecker();
		mergeTracker = new MergeTracker();
		generatePositionsLeft();
	}

	public Board(int dimension, RandomCellProvider _provider) {
		tiles = new Tile[dimension][dimension];
		provider = _provider;
		generatePositionsLeft();
	}

	private void generatePositionsLeft() {
		provider.clear();
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[i].length; j++) {
				if (tiles[i][j] == null) {
					provider.add(new Point(i, j));
				}
			}
		}
	}

	public void addTile(int x, int y, int value) {
		Tile tile = new Tile(x, y, value, GameConstants.DEFAULT_TILE_COLOR);
		addTile(tile);
	}

	public void addTile(Tile tile) {
		tiles[tile.x][tile.y] = tile;
		provider.remove(new Point(tile.x, tile.y));
	}

	public void removeTile(int x, int y) {
		tiles[x][y] = null;
		provider.add(new Point(x, y));
	}

	public Tile[][] moveGridTo(Direction direction) {
		mergeTracker.clear();
		switch (direction) {
			case LEFT:
				moveAllCellsToLeft();
				break;
			case UP:
				moveAllCellsUp();
				break;
			case RIGHT:
				moveAllCellsToRight();
				break;
			case DOWN:
				moveAllCellsDown();
				break;

		}

		resetMerge();
		return tiles;
	}

	private void resetMerge() {
		for (Tile[] line : tiles) {
			for (Tile t : line) {
				if (t != null) {
					t.hasMerged = false;
				}
			}
		}
	}

	public Tile[] reverseLineHorizontal(Tile[] line, int row) {
		int length = line.length;
		Tile[] result = new Tile[length];
		for (int i=length-1; i>=0; i--) {
			result[length-i-1] = line[i];

			if (result[length-i-1] != null) {
				result[length-i-1].x = row;
				result[length-i-1].y = length-i-1;
			}
		}

		return result;
	}

	private Tile[] reverseLineVertical(Tile[] line, int columnIndex) {
		int length = line.length;
		Tile[] result = new Tile[length];
		for (int i=length-1; i>=0; i--) {
			result[length-i-1] = line[i];

			if (result[length-i-1] != null) {
				result[length-i-1].x = length-i-1;
				result[length-i-1].y = columnIndex;
			}
		}

		return result;
	}

	public Tile[] getColumn(int columnIndex, Tile[][] board) {
		Tile[] lineUp = new Tile[dimension];
		for (int i=0; i<board.length; i++) {
			lineUp[i] = board[i][columnIndex];
		}

		return lineUp;
	}

	private void moveAllCellsToLeft() {
		for (int i=0; i<tiles.length; i++) {
			moveLineLeft(tiles[i], i, true, Direction.LEFT);
		}

		provider.update(tiles);
	}

	private void moveAllCellsToRight() {
		for (int i=0; i<tiles.length; i++) {
			moveRight(tiles[i], i);
		}

		provider.update(tiles);
	}

	private void moveAllCellsUp() {
		Tile[] lineUp;

		for (int i=0; i<dimension; i++) {
			lineUp = getColumn(i, tiles);
			moveUp(lineUp, i);
		}

		provider.update(tiles);
	}

	private void moveAllCellsDown() {
		Tile[] lineUp;
		for (int i=0; i<4; i++) {
			lineUp = getColumn(i, tiles);
			lineUp = reverseLineVertical(lineUp, i);
			lineUp = moveDown(lineUp, i);
			lineUp = reverseLineVertical(lineUp, i);
			reindexCells(lineUp, i, Reordering.VERTICAL);
		}

		provider.update(tiles);
	}


	public void translateToLeft(Tile[] line, int row, Direction direction) {
		int i = 0;
		while (i < line.length) {
			if (line[i] == null) {
				int indexNonNullTile = findNextNonNullTileIndex(line, i + 1);

				if (indexNonNullTile != -1) {
					if (!line[indexNonNullTile].hasPreviousPosition) {
						Point previousPosition = getPreviousPosition(line[indexNonNullTile], row, direction);
						line[indexNonNullTile].setPreviousPosition(previousPosition);
						System.out.println("Board.translateToLeft translating " + line[indexNonNullTile].previousPosition.toString());
					}
					line[i] = line[indexNonNullTile];
					line[indexNonNullTile] = null;
				} else {
					break;
				}
			} else {
				if (!line[i].hasPreviousPosition) {
					Point previousPosition = getPreviousPosition(line[i], row, direction);
					line[i].setPreviousPosition(previousPosition);
					System.out.println("Board.translateToLeft not translating " + line[i].previousPosition.toString());
				}
			}
			i++;
		}
	}

	private Point getPreviousPosition(Tile tile, int row, Direction direction) {
		Point position = null;
		Point realCoordinates;
		if (tile == null) {
			return null;
		}

		switch (direction) {
		case LEFT:
			position = new Point(tile.x, tile.y);
			break;
		case RIGHT:
			realCoordinates = getRealCoordinatesForRight(tile);
			position = realCoordinates;
			break;
		case UP:
			realCoordinates = getRealCoordinatesForUp(tile, row);
			position = realCoordinates;
			break;
		case DOWN:
			realCoordinates = getRealCoordinatesForDown(tile, row);
			position = realCoordinates;
			break;
		}
		return position;
	}

	private Point getRealCoordinatesForDown(Tile tile, int row) {
		return new Point(GameConstants.GAME_DIMENSION - tile.x - 1, tile.y);
	}

	private Point getRealCoordinatesForUp(Tile tile, int row) {
		return new Point(tile.x, tile.y);
	}

	private Point getRealCoordinatesForRight(Tile tile) {
		return new Point(tile.x, GameConstants.GAME_DIMENSION - tile.y - 1);
	}

	public void mergeCells(Tile[] line, int row, int column, Direction direction) {
		if (line[column] != null && column+1 < dimension && line[column+1] != null && line[column].value == line[column+1].value && !line[column].hasMerged) {
			line[column].value = line[column].value * 2;
			line[column].hasMerged = true;
			addMergeToTracker(line[column], line[column+1]);
			line[column+1] = null;
		}
	}

	private void addMergeToTracker(Tile growingCell, Tile mergedCell) {
		mergeTracker.addMerge(mergedCell.previousPosition, growingCell.previousPosition);
	}

	public void moveLineLeft(Tile[] line, int row, Boolean reorder, Direction direction) {
		int column = 0;
		setUpTilesForTranslate(line);
		while (column < line.length) {
			translateToLeft(line, row, direction);
			mergeCells(line, row, column, direction);
			if (reorder) {
				reindexCells(line, row, Reordering.HORIZONTAL);
			}
			column++;
		}
	}

	private void setUpTilesForTranslate(Tile[] line) {
		for (int i=0; i<line.length; i++) {
			if (line[i] != null) {
				line[i].hasPreviousPosition = false;
			}
		}
	}

	private void reindexCells(Tile[] line, int index, Reordering reordering) {
		for (int i=0; i<line.length; i++) {
			if (reordering == Reordering.HORIZONTAL) {
				if (line[i] != null) {
					line[i].x = index;
					line[i].y = i;
				}
			} else if (reordering == Reordering.VERTICAL) {
				if (line[i] != null) {
					line[i].x = i;
					line[i].y = index;
				}
			}
		}

		updateTiles(line, index, reordering);

	}

	private void updateTiles(Tile[] line, int index, Reordering reordering) {
		if (reordering == Reordering.HORIZONTAL) {
			updateTilesHorizontal(line, index);
		} else {
			updateTilesVertical(line, index);
		}
	}

	private void updateTilesVertical(Tile[] line, int index) {
		for (int i=0; i<dimension; i++) tiles[i][index] = line[i];
	}

	private void updateTilesHorizontal(Tile[] line, int row) {
		Tile[] l = tiles[row];
		for (int i=0; i<l.length; i++) l[i] = line[i];
	}


	public void moveRight(Tile[] line, int row) {
		Tile[] reversedLine = reverseLineHorizontal(line, row);
		moveLineLeft(reversedLine, row, true, Direction.RIGHT);
		reversedLine = reverseLineHorizontal(reversedLine, row);
		reindexCells(reversedLine, row, Reordering.HORIZONTAL);

	}

	public Tile[] moveUp(Tile[] line, int index) {
		moveLineLeft(line, index, false, Direction.UP);
		reindexCells(line, index, Reordering.VERTICAL);
		return line;
	}

	public Tile[] moveDown(Tile[] line, int index) {
		moveLineLeft(line, index, false, Direction.DOWN);
		reindexCells(line, index, Reordering.VERTICAL);
		return line;
	}

	public String printVerticalLine(Tile[] line) {
		String r = "";
		for (Tile t : line) {
			r = r +((t != null)? "| " + t.value : " . " )+ " |\n__\n";
		}

		return r;
	}

	public void printGrid() {
		for (int i=0; i< tiles.length; i++) {
			System.out.println(toStringLine(tiles[i]));
		}
	}

	private void setAsColumn(Tile[] line, int index) {
		for (int i=0; i<line.length; i++) {
			if (line[i] != null) {
				line[i].x = i;
				line[i].y = index;
			}
		}
	}


	private String toStringLine(Tile[] line) {
		String r = "| ";
		for (Tile t : line) {
			r = r +((t != null)? t.value + "("+t.x+","+t.y+")": " " )+ " |";
		}

		return r;
	}

	private int findNextNonNullTileIndex(Tile[] line, int index) {
		for (int i=index; i<line.length; i++) {
			if (line[i] != null) {
				return i;
			}
		}

		return -1;
	}

	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}

	public int getNumberOfTiles() {
		int nbTiles = 0;
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[i].length; j++) {
				if (tiles[i][j] != null) {
					nbTiles+=1;
				}
			}
		}

		return nbTiles;
	}

	public Tile addRandomTile() {
		while (true) {
			Point p  = provider.getRandomCell();
			int x = p.x;
			int y = p.y;
			if (p.x == Integer.MIN_VALUE) {
				return new Tile(x, y, -1, GameConstants.DEFAULT_TILE_COLOR);
			} else if (tiles[x][y] == null) {
				Tile t = new Tile(x, y, 2, GameConstants.DEFAULT_TILE_COLOR);
				addTile(t);
				return t;
			}
		}
	}

	public int getDimension() {
		return dimension;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public boolean isGameOver() {
		if (getNumberOfTiles() != dimension * dimension) {
			return false;
		} else {
			for (int i=0; i<dimension; i++) {
				for (int j=0; j<dimension; j++) {
					if (gameStateChecker.canMove(tiles, tiles[i][j])) {
						return false;
					}
				}
			}

			return true;
		}
	}

	public boolean canMoveLeft() {
		for (int i=0; i<tiles.length; i++) {
			if (canLineMoveLeft(tiles[i])) {
				return true;
			}
		}

		return false;
	}

	private Boolean canLineMoveLeft(Tile[] line) {
		for (int i=0; i<line.length; i++) {
			if (canTileMoveLeft(line, i)) {
				return true;
			}
		}

		return false;
	}

	private boolean canTileMoveLeft(Tile[] line, int i) {
		if (i != 0) {
			if (line[i] != null && (line[i-1] == null || line[i-1].value == line[i].value)) {
				return true;
			}
		}

		return false;
	}

	public boolean canMoveUp() {
		for (int i=0; i<tiles.length; i++) {
			if (canLineMoveLeft(getColumn(i, tiles))) {
				return true;
			}
		}
		return false;
	}

	public boolean canMoveDown() {
		for (int i=0; i<tiles.length; i++) {
			if (canLineMoveLeft(reverseLineVertical(getColumn(i, tiles), i))) {
				return true;
			}
		}
		return false;
	}

	public boolean canMoveRight() {
		for (int i=0; i<tiles.length; i++) {
			if (canLineMoveLeft(reverseLineHorizontal(tiles[i]))) {
				return true;
			}
		}
		return false;
	}

	private Tile[] reverseLineHorizontal(Tile[] line) {
		int length = line.length;
		Tile[] result = new Tile[length];
		for (int i=length-1; i>=0; i--) {
			result[length-i-1] = line[i];
		}

		return result;
	}
}
