public class GameStateChecker {
    public GameStateChecker() {
    }

    public static boolean canMove(Tile[][] board, Tile tile) {
        boolean canMove = false;
        switch (tile.getSpecialPosition()) {
            case TOP_LEFT:
                canMove = checkNeighborsFromTopLeft(board, tile);
                break;
            case TOP_RIGHT:
                canMove = checkNeighborsFromTopRight(board, tile);
                break;
            case BOTTOM_LEFT:
                canMove = checkNeighborsFromBottomLeft(board, tile);
                break;
            case BOTTOM_RIGHT:
                canMove = checkNeighborsFromBottomRight(board, tile);
                break;
            case TOP:
                canMove = checkNeighborsFromTop(board, tile);
                break;
            case BOTTOM:
                canMove = checkNeighborsFromBottom(board, tile);
                break;
            case LEFT:
                canMove = checkNeighborsFromLeft(board, tile);
                break;
            case RIGHT:
                canMove = checkNeighborsFromRight(board, tile);
                break;
            case NONE:
                canMove = checkNeighborsFromNone(board, tile);
                break;
        }

        return canMove;
    }

    private static boolean checkNeighborsFromBottomRight(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean leftNeighborIdentical = (board[x][y-1].value == tile.value);
        boolean topNeighborIdentical = (board[x-1][y].value == tile.value);

        return leftNeighborIdentical || topNeighborIdentical;
    }

    private static boolean checkNeighborsFromTop(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean leftNeighborIdentical = (board[x][y-1].value == tile.value);
        boolean bottomNeighborIdentical = (board[x+1][y].value == tile.value);
        boolean rightNeighborIdentical = (board[x][y+1].value == tile.value);

        return leftNeighborIdentical || bottomNeighborIdentical || rightNeighborIdentical;
    }

    private static boolean checkNeighborsFromBottom(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean leftNeighborIdentical = (board[x][y-1].value == tile.value);
        boolean topNeighborIdentical = (board[x-1][y].value == tile.value);
        boolean rightNeighborIdentical = (board[x][y+1].value == tile.value);

        return leftNeighborIdentical || topNeighborIdentical || rightNeighborIdentical;
    }

    private static boolean checkNeighborsFromLeft(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean topNeighborIdentical = (board[x-1][y].value == tile.value);
        boolean bottomNeighborIdentical = (board[x+1][y].value == tile.value);
        boolean rightNeighborIdentical = (board[x][y+1].value == tile.value);

        return topNeighborIdentical || bottomNeighborIdentical || rightNeighborIdentical;
    }

    private static boolean checkNeighborsFromRight(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean topNeighborIdentical = (board[x-1][y].value == tile.value);
        boolean bottomNeighborIdentical = (board[x+1][y].value == tile.value);
        boolean leftNeighborIdentical = (board[x][y-1].value == tile.value);

        return topNeighborIdentical || bottomNeighborIdentical || leftNeighborIdentical;
    }

    private static boolean checkNeighborsFromNone(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean topNeighborIdentical = (board[x-1][y].value == tile.value);
        boolean bottomNeighborIdentical = (board[x+1][y].value == tile.value);
        boolean leftNeighborIdentical = (board[x][y-1].value == tile.value);
        boolean rightNeighborIdentical = (board[x][y+1].value == tile.value);

        return topNeighborIdentical || bottomNeighborIdentical || leftNeighborIdentical || rightNeighborIdentical;
    }

    private static boolean checkNeighborsFromBottomLeft(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean topNeighborIdentical = (board[x-1][y].value == tile.value);
        boolean rightNeighborIdentical = (board[x][y+1].value == tile.value);

        return topNeighborIdentical || rightNeighborIdentical;
    }

    private static boolean checkNeighborsFromTopRight(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean bottomNeighborIdentical = (board[x+1][y].value == tile.value);
        boolean leftNeighborIdentical = (board[x][y-1].value == tile.value);

        return bottomNeighborIdentical || leftNeighborIdentical;
    }

    private static boolean checkNeighborsFromTopLeft(Tile[][] board, Tile tile) {
        int x = tile.x;
        int y = tile.y;
        boolean bottomNeighborIdentical = (board[x+1][y].value == tile.value);
        boolean rightNeighborIdentical = (board[x][y+1].value == tile.value);

        return bottomNeighborIdentical || rightNeighborIdentical;
    }
}
