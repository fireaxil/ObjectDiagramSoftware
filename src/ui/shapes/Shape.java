package ui.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import ui.UI;

public abstract class Shape {

	protected boolean _defunct;

	protected double _x;
	protected double _y;

	protected int _alphaOscillation;
	protected int _alphaDirection;

	protected String _name;

	public Shape(double x, double y) {
		_x = x;
		_y = y;

		_alphaOscillation = 0;
		_alphaDirection = 2;

		_name = "untitled";
	}

	public void draw(Graphics2D g2, int state, boolean preliminary) {

		UI.enableAntiAliasing(g2);
		
		if (state == 0) {
			g2.setColor(Color.BLACK); //inactive
		} else if (state == 1) {
			g2.setColor(Color.GREEN);   //active
		} else if (state ==2) {
			g2.setColor(new Color(148,0,211));  //targeted [purple]
		} else if (state ==3) {
			g2.setColor(Color.RED); //superimposed
		}

		if (preliminary) {
			//set translucency level
			//http://www.javaworld.com/article/2076733/java-se/antialiasing--images--and-alpha-compositing-in-java-2d.html?page=2
			g2.setComposite(UI.generateAlpha(_alphaOscillation / 100.0f));
			_alphaOscillation += _alphaDirection;
			if (_alphaOscillation > 98) {
				_alphaDirection = -2;
			} else if (_alphaOscillation < 2) {
				_alphaDirection = 2;
			}
		}

		BasicStroke solid = new BasicStroke(2.5f);
		g2.setStroke(solid);
	}

	public boolean isTouching(Shape s) {

		if (!this.equals(s)) {

			//distance between centers of any two shapes can be used universally
			double deltaX = (this.getX() - s.getX());
			double deltaY = (this.getY() - s.getY());
			double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

			if (this.getType().equals("Object") && s.getType().equals("Object")) {
				if (distance <= ObjectShape.RADIUS * 2) {
					return true;
				}
			}
			
			if (!this.getType().equals(s.getType())) {
				if (distance <= (ObjectShape.RADIUS + VariableShape.SIDE / 2)) {
					return true;
				}
			}

			if (this.getType().equals("Variable") && s.getType().equals("Variable")) {
				boolean xInRange = (Math.abs((double) (this.getX() - s.getX())) < VariableShape.SIDE);
				boolean yInRange = (Math.abs((double) (this.getY() - s.getY())) < VariableShape.SIDE);
				if (xInRange && yInRange) {
					return true;
				}
			}
		}
		return false;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public abstract double distanceFromCenter(double x, double y);
	public abstract boolean contains(double x, double y);

	public abstract double getX();
	public abstract double getY();

	public abstract void setX(double x);
	public abstract void setY(double y);

	public abstract String getType();

	public abstract void dispose();

	public boolean isDefunct() {
		return _defunct;
	}
}
