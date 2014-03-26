import java.awt.*;

public class Board {
    private GameStateChecker gameStateChecker;
    private Tile[][] tiles;
    private RandomCellProvider provider;
    private int dimension;

    public Board(int _dimension) {

        dimension = _dimension;
        tiles = new Tile[dimension][dimension];
        provider = new RandomCellProvider();
        gameStateChecker = new GameStateChecker();
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

    private void moveAllCellsDown() {
        Tile[] lineUp;
        for (int i=0; i<4; i++) {
            lineUp = getColumn(i, tiles);
            lineUp = reverseLineVertical(lineUp, i);
            lineUp = moveUp(lineUp, i);
            lineUp = reverseLineVertical(lineUp, i);
            reindexCells(lineUp, i, Reordering.VERTICAL);
        }

        provider.update(tiles);
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
            moveLineLeft(tiles[i], i, true);
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

    public void translateToLeft(Tile[] line) {
        int i = 0;
        while (i < line.length) {
            if (line[i] == null) {
                int indexNonNullTile = findNextNonNullTileIndex(line, i + 1);

                if (indexNonNullTile != -1) {
                    line[i] = line[indexNonNullTile];
                    line[indexNonNullTile] = null;
                } else {
                    break;
                }
            }
            i++;
        }
    }

    public void mergeCells(Tile[] line, int i) {
        if (line[i] != null && i+1 < dimension && line[i+1] != null && line[i].value == line[i+1].value && !line[i].hasMerged) {
            line[i].value = line[i].value * 2;
            line[i].hasMerged = true;
            line[i+1] = null;
        }
    }

    public void moveLineLeft(Tile[] line, int row, Boolean reorder) {
        int column = 0;
        while (column < line.length) {
            translateToLeft(line);
            mergeCells(line, column);
            if (reorder) {
                reindexCells(line, row, Reordering.HORIZONTAL);
            }
            column++;
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
        moveLineLeft(reversedLine, row, true);
        reversedLine = reverseLineHorizontal(reversedLine, row);
        reindexCells(reversedLine, row, Reordering.HORIZONTAL);

    }

    public Tile[] moveUp(Tile[] line, int index) {
        moveLineLeft(line, index, false);
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
                    if (GameStateChecker.canMove(tiles, tiles[i][j])) {
                        return false;
                    }
                }
            }

            return true;
        }
    }
}
