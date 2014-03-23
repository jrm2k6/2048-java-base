import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyboardManager {
	static IMovableManager widget;

	public KeyboardManager() {
	}

	public void setWidget(IMovableManager _widget) {
		widget = _widget;
	}

	public void addKeyBindings(JComponent jc) {
		jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Left pressed");
		jc.getActionMap().put("Left pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				widget.left();
			}
		});

		jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Right pressed");
		jc.getActionMap().put("Right pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				widget.right();
			}
		});

		jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Up pressed");
		jc.getActionMap().put("Up pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				widget.up();
			}
		});

		jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "Down pressed");
		jc.getActionMap().put("Down pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				widget.down();
			}
		});
	}

	public void removeAllKeyBindings(JComponent jc) {
		jc.getActionMap().clear();
	}
}
