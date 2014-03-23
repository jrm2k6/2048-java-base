import java.util.HashMap;

public class ColorProvider {
	private static ColorProvider _instance;
	private static HashMap<Integer, Integer> colors;

	private ColorProvider() {
		colors = new HashMap<Integer, Integer>();
		colors.put(2, 0xeee4da);
		colors.put(4, 0xede0c8);
		colors.put(8, 0xf2b179);
		colors.put(16, 0xf59563);
		colors.put(32, 0xf67c5f);
		colors.put(64, 0xf65e3b);
		colors.put(128, 0xedcf72);
		colors.put(256, 0xedcc61);
		colors.put(512, 0xedc850);
		colors.put(1024, 0xedc53f);
		colors.put(2048, 0xedc22e);
	}


	public static ColorProvider getInstance() {
		if (_instance == null) {
			_instance = new ColorProvider();
		}

		return _instance;
	}

	public static int getColorForValue(int value) {
		return colors.get(value);
	}
}
