import java.awt.*;
import java.util.HashMap;

public class MergeTracker {
	private HashMap<Point, Point> merges;

	public MergeTracker() {
		merges = new HashMap<Point, Point>();
	}

	public void addMerge(Point destroyedCell, Point mergedCell) {
		System.out.println("addMerge " + destroyedCell.toString() + " has merged with " + mergedCell.toString());
		merges.put(destroyedCell, mergedCell);
	}

	public void clear() {
		merges.clear();
	}
}
