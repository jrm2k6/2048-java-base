import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardView extends JFrame {
	private static final int DIMENSION = 4;

	ArrayList<Label> labelTiles = new ArrayList<Label>();
	ArrayList<JPanel> panelTiles = new ArrayList<JPanel>();

	JPanel panel;

	public BoardView() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createBoard();
		display();
	}

	private void display() {
		this.pack();
		this.setVisible(true);
	}

	private void createBoard() {
		panel = new JPanel();
		panel.setLayout(new GridLayout(DIMENSION, DIMENSION));
		panel.setSize(new Dimension(3200, 3200));

		for (int i=0; i<16; i++) {
			JPanel panelTile = new JPanel();
			panelTile.setPreferredSize(new Dimension(200, 200));
			panelTile.setBorder(BorderFactory.createLineBorder(Color.black));
			panelTile.setBackground(new Color(GameConstants.DEFAULT_TILE_COLOR));
			Label tile = new Label("");
			tile.setPreferredSize(new Dimension(50, 50));
			tile.setBackground(new Color(GameConstants.DEFAULT_TILE_COLOR));
			panelTile.add(tile);
			labelTiles.add(tile);
			panelTiles.add(panelTile);
			panel.add(panelTile);
		}

		this.setContentPane(panel);
	}

	public void addTile(Tile tile) {
		int index = getIndexTile(tile.x, tile.y);
		changeStyleTile(tile, index);
	}

	public void resetStyle(int x, int y) {
		int index = getIndexTile(x, y);
		resetStyleTile(index);
	}

	private void resetStyleTile(int index) {
		this.labelTiles.get(index).setBackground(new Color(GameConstants.DEFAULT_TILE_COLOR));
		this.panelTiles.get(index).setBackground(new Color(GameConstants.DEFAULT_TILE_COLOR));
	}

	private void changeStyleTile(Tile tile, int index) {
		Color tileColor = new Color(ColorProvider.getInstance().getColorForValue(tile.value));
		this.labelTiles.get(index).setText("" + tile.value);
		this.labelTiles.get(index).setForeground(Color.BLACK);
		this.labelTiles.get(index).setBackground(tileColor);
		this.panelTiles.get(index).setBackground(tileColor);
	}

	private int getIndexTile(int x, int y) {
		return x * DIMENSION + y;
	}

	public void update(Tile[][] tiles) {
		clearBoard();
		for (int i=0; i<tiles.length; i++) {
			for (int j=0; j<tiles[i].length; j++) {
				if (tiles[i][j] != null) {
					addTile(tiles[i][j]);
				} else {
					resetStyle(i, j);
				}
			}
		}
	}

	private void clearBoard() {
		for (Label l : labelTiles) {
			l.setText("");
		}
	}

	public void showGameOver() {
		JOptionPane.showMessageDialog(this, "Game Over");
	}
}
