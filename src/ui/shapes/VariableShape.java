package ui.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import ui.UI;

public class VariableShape extends Shape {

	public static double SIDE = 25;
	public boolean _hovering;

	public VariableShape(double x, double y) {
		super(x, y);
		_hovering = false;
	}

	//override draw
		@Override
		public void draw(Graphics2D g2, int state, boolean preliminary) {
			super.draw(g2, state, preliminary);
			g2.draw(new Rectangle2D.Double(_x - SIDE / 2, _y - SIDE / 2, SIDE, SIDE));
			
			g2.drawString(_name, (float) _x, (float) _y - ((float) SIDE / 2 + 5));
			
			//shape objects must clean up after themselves
			g2.setComposite(UI.generateAlpha(1.0f));
		}
	
	

	@Override
	public double distanceFromCenter(double x, double y) {
		return Math.sqrt((double) (x - _x) * (x - _x) + (y - _y) * (y - _y));
	}

	@Override
	public boolean contains(double x, double y) {
		boolean goodX = x >= (_x - SIDE / 2) && x <= (_x + SIDE / 2);
		boolean goodY = y >= (_y - SIDE / 2) && y <= (_y + SIDE / 2);

		return goodX && goodY;
	}

	@Override
	public double getX() {
		return _x;
	}

	@Override
	public double getY() {
		return _y;
	}

	@Override
	public void setX(double x) {
		_x = x;	
	}

	@Override
	public void setY(double y) {
		_y = y;
	}

	@Override
	public String getType() {
		return "Variable";
	}

	@Override
	public void dispose() {
		_defunct = true;
	}

	@Override
	public boolean isDefunct() {
		return _defunct;
	}
}