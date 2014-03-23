import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomCellProvider {
	private ArrayList<Point> points;

	public RandomCellProvider() {
		points = new ArrayList<Point>();
	}

	public RandomCellProvider(ArrayList<Point> points) {
		this.points = points;
	}

	public Point getRandomCell() {
		if (points.size() == 0) {
			return new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		}

		int index = new Random().nextInt(points.size());
		return points.get(index);
	}

	public void clear() {
		points.clear();
	}


	public void add(Point point) {
		if (!contains(point)) {
			points.add(point);
		}
	}

	private boolean contains(Point point) {
		if (points.size() == 0) {
			return false;
		}

		for (Point currentPoint : points) {
			if (currentPoint.x == point.x && currentPoint.y == point.y) {
				return true;
			}
		}

		return false;
	}

	public void remove(Point point) {
		for (int i=0; i<points.size(); i++) {
			Point currentPoint = points.get(i);
			if (currentPoint.x == point.x && currentPoint.y == point.y) {
				points.remove(i);
			}
		}
	}

	public int getNumberCells() {
		return points.size();
	}

	public void update(Tile[] line, int row, int column) {
		for (int i=0; i<line.length; i++) {
			if (line[i] == null) {
				add(new Point(row, i));
			} else {
				remove(new Point(row, i));
			}
		}
	}

	public void update(Tile t) {
		remove(new Point(t.x, t.y));
	}

	public void update(Tile[][] tiles) {
		points.clear();

		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[i].length; j++) {
				if (tiles[i][j] == null) {
					points.add(new Point(i,j));
				}
			}
		}
	}
}
