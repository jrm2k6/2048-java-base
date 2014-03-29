import java.awt.*;

public class Tile {
	public int x;
	public int y;
	public int value;
	public int color;
	public boolean hasMerged;
	public Point previousPosition;
	public Point mergedFrom;
	public boolean hasPreviousPosition;

	public Tile(int x, int y, int value, int color) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.color = color;
	}

	public SpecialPosition getSpecialPosition() {
		if (x == 0) {
			if (y == 0) {
				return SpecialPosition.TOP_LEFT;
			} else if (y == GameConstants.GAME_DIMENSION - 1) {
				return SpecialPosition.TOP_RIGHT;
			} else {
				return SpecialPosition.TOP;
			}
		} else if (x == GameConstants.GAME_DIMENSION - 1) {
			if (y == 0) {
				return SpecialPosition.BOTTOM_LEFT;
			} else if (y == GameConstants.GAME_DIMENSION - 1) {
				return SpecialPosition.BOTTOM_RIGHT;
			} else {
				return SpecialPosition.BOTTOM;
			}
		} else if (y == 0) {
			return SpecialPosition.LEFT;
		} else if (y == GameConstants.GAME_DIMENSION - 1) {
			return SpecialPosition.RIGHT;
		} else {
			return SpecialPosition.NONE;
		}
	}

	public void setPreviousPosition(Point _previousPosition) {
		this.previousPosition = _previousPosition;
		hasPreviousPosition = true;
	}

	public void setHasPreviousPosition(boolean _hasPreviousPosition) {
		this.hasPreviousPosition = _hasPreviousPosition;
	}
}
