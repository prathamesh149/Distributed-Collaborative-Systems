package shared.window;

import java.awt.Color;
import java.awt.Graphics;

import util.awt.ExtendibleTelepointerGlassPane;
import util.awt.GraphicsPainter;

public class ATelePointerPainter implements GraphicsPainter {

	ExtendibleTelepointerGlassPane gp;

	public ATelePointerPainter(ExtendibleTelepointerGlassPane gp) {
		this.gp = gp;
	}

	@Override
	public void paint(Graphics arg0) {

		if (gp.isShowTelePointer()) {
			arg0.setColor(Color.RED);
			int x = gp.getPointerX();
			int y = gp.getPointerY();
			int pointerWidth = gp.getPointerWidth();
			int pointerHeight = gp.getPointerHeight();

			arg0.fillOval(x, y, pointerWidth, pointerHeight);
		}

	}

}
