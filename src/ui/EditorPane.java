package ui;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

import ui.shapes.*;

public class EditorPane extends JLayeredPane {

	UI _ui;
	EditorPane _self;

	EditButton _objectButton;
	EditButton _referenceButton;
	EditButton _variableButton;
	EditButton _deleteButton;
	EditButton _nameButton;

	private ArrayList<Shape> _onScreenShapes;
	private Shape _activeShape;

	private DragHandler _drag;
	private boolean _dragging;

	Timer _t;
	private Shape _preliminaryPlacing;

	private static int BUTTON_SIZE = 45;

	public EditorPane(UI ui) {
		super();

		_ui = ui;
		_self = this;

		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));

		_objectButton = new EditButton(UI.PADDING / 2, UI.PADDING / 2);
		//add image icon
		_objectButton.setText("O");
		add(_objectButton);
		_objectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewObject();
			}
		});

		_referenceButton = new EditButton(UI.PADDING / 2, (UI.PADDING / 2) * 2 + BUTTON_SIZE);
		//add image icon
		_referenceButton.setText("R");
		add(_referenceButton);

		_variableButton = new EditButton(UI.PADDING / 2, (UI.PADDING / 2) * 3 + BUTTON_SIZE * 2);
		//add image icon
		_variableButton.setText("V");
		add(_variableButton);
		_variableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewVariable();
			}
		});

		_nameButton = new EditButton(UI.PADDING / 2, (UI.PADDING / 2) * 4 + BUTTON_SIZE * 3);
		add(_nameButton);
		//image icon
		_nameButton.setText("N");
		//creates a name editor window which auto-disposes once enter is pressed
		_nameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_activeShape != null) {
					NameEditor ne = new NameEditor(_activeShape.getType(), new EnterListener() {
						@Override
						public void enterPressed(String contents) {
							_activeShape.setName(contents);
							_self.repaint();
						}
					});

					Point p = _ui.getLocation();
					p.setLocation(p.getX() + UI.WINDOW_WIDTH / 2 - ne.getWidth() / 2, p.getY() + UI.WINDOW_HEIGHT / 2);
					ne.setLocation(p);
					ne.setVisible(true);
				}
			}
		});

		_deleteButton = new EditButton(UI.PADDING / 2, (UI.PADDING / 2) * 5 + BUTTON_SIZE * 4);
		//add image icon
		_deleteButton.setText("D");
		add(_deleteButton);
		_deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_activeShape != null) {
					_activeShape.dispose();
					garbageCollect();
				}
			}
		});

		_onScreenShapes = new ArrayList<Shape>();

		addMouseListener(new MouseHandler());

		_drag = new DragHandler();
		addMouseMotionListener(_drag);
		_dragging = false;
		_preliminaryPlacing = null;

		_t = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		_t.start();

		//_ui.getModel().addObserver(this);

	}

	private void createNewObject() {
		ObjectShape o = new ObjectShape(200, 200);
		_onScreenShapes.add(o);
		_activeShape = o;
		_preliminaryPlacing = o;

		repaint();
	}

	private void createNewVariable() {
		VariableShape v = new VariableShape(200, 200);
		_onScreenShapes.add(v);
		_activeShape = v;
		_preliminaryPlacing = v;

		repaint();
	}

	private void mouseDown(int x, int y) {

		//if there is no superimposition, behave normally (this check is only necessary for preliminary placement
		if (!checkSuperImposition()) {

			//determine which shape the click was closest to
			Shape runningMin = null;
			for (Shape s : _onScreenShapes) {
				if (s.contains(x, y)) {
					if (runningMin == null) {
						runningMin = s;
					} else {
						if (s.distanceFromCenter(x, y) < runningMin.distanceFromCenter(x, y)) {
							runningMin = s;
						}
					}
				}
			}
			_activeShape = runningMin;
			_preliminaryPlacing = null;

		} else {
			//Throw some sort of GUI error dialog
			System.err.println("Drop shape in a free space!");
		}

		repaint();
	}

	public boolean checkSuperImposition() {
		if (_activeShape != null) {
			for (Shape s : _onScreenShapes) {
				if (_activeShape.isTouching(s)) {

					String a = _activeShape.getType();
					String b = s.getType();

					if (a.equals("Object") && b.equals("Variable")) {
						//if the object being dragged is not a parent to the variable it's touching
						if (!(((ObjectShape) _activeShape).isParentTo((VariableShape) s))) return true;
					} else if (b.equals("Object") && a.equals("Variable")) {
						//only prevent variables from entering objects if they are under preliminary placement
						//not a redundancy; must use conditionally evaluated statement
						if (_preliminaryPlacing != null && _preliminaryPlacing.equals(_activeShape)) return true;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void cursorDragged(double x, double y) {
		if (_activeShape != null) {

			//if an object contains the instance variable we're trying to drag, prevent the action
			boolean canBeDragged = true;
			for (Shape s : _onScreenShapes) {
				if (s.getType().equals("Object")) {
					ObjectShape o = (ObjectShape) s;
					if (_activeShape.getType().equals("Variable")) {
						if (o.isParentTo((VariableShape) _activeShape)) canBeDragged = false;
					}
				}
			}

			if (canBeDragged) {
				double tempX = _activeShape.getX();
				double tempY = _activeShape.getY();

				_activeShape.setX(_activeShape.getX() + (x - _drag._previousX));
				_activeShape.setY(_activeShape.getY() + (y - _drag._previousY));

				if (checkSuperImposition()) {
					_activeShape.setX(tempX);
					_activeShape.setY(tempY);

					try {
						Robot r = new Robot();
						r.mouseRelease(InputEvent.BUTTON1_MASK);
						_activeShape = null;
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		repaint();
	}

	public void mouseUp(int x, int y) {
		if (_activeShape != null) {
			if ("Variable".equals(_activeShape.getType())) {
				VariableShape v = (VariableShape) _activeShape;
				if (v._hovering) {

					//search for the ObjectShape that contains this instance variable
					for (Shape s : _onScreenShapes) {
						if (s.contains(v.getX(), v.getY()) && "Object".equals(s.getType())) {
							ObjectShape o = (ObjectShape) s;
							o.addInstanceVariable(v);
							_activeShape = null;
						}
					}
				}
			}
		}

		repaint();
	}

	//properly remove all defunct shapes
	private void garbageCollect() {

		Iterator<Shape> i = _onScreenShapes.iterator();
		while (i.hasNext()) {

			Shape s = i.next();

			//if a variable has been deleted, its parent object will need to remove it
			if (s.getType().equals("Object")) {
				ObjectShape o = (ObjectShape) s;
				o.garbageCollect();
			}
			
			if (s.isDefunct()) {
				i.remove();
				if (s.equals(_activeShape)) {
					_activeShape = null;
				}
			}
		}
		repaint();
	}

	@Override
	public void paint (Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		//"initialization" for active variable
		VariableShape v = null;
		if (_activeShape != null) {
			if ("Variable".equals(_activeShape.getType())) {
				v = (VariableShape) _activeShape;
				v._hovering = false;
			}
		}
		
		for (Shape s : _onScreenShapes) {

			boolean alreadyDrawn = false;

			//determine if variable is being hovered over object
			if (v != null && _dragging) {
				if ("Object".equals(s.getType())) {
					if (s.contains(_drag._previousX, _drag._previousY)) {
						s.draw(g2, 2, false); //redraw blue over the black
						v._hovering = true;
						alreadyDrawn = true;
					}
				}
			}

			//reposition object if it is under preliminary placement
			if (s.equals(_preliminaryPlacing)) {
				s.setX(_drag.getCurrentLocation().getX());
				s.setY(_drag.getCurrentLocation().getY());
			}

			//check for superimposition (draw accordingly if so)
			if (_activeShape != null && _preliminaryPlacing != null) {
				if (_activeShape.isTouching(s)) {
					s.draw(g2, 3, s.equals(_preliminaryPlacing));
					alreadyDrawn = true;
				}
			}

			if (!alreadyDrawn) {
				int state = 0; //object is inactive
				if (s.equals(_activeShape)) state = 1; //object is currently active
				s.draw(g2, state, s.equals(_preliminaryPlacing));
			}
		}


		super.paint(g); //draw the JComponents last
	}

	private class EditButton extends JButton {

		public EditButton(int x, int y) {
			super();
			UI.formatJComponent(this, new Dimension(BUTTON_SIZE, BUTTON_SIZE), x, y);
		}
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			_self.mouseDown(e.getX(), e.getY());
			_drag._previousX = e.getX();
			_drag._previousY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			_dragging = false;
			_self.mouseUp(e.getX(), e.getY());
		}
	}

	private class DragHandler extends MouseMotionAdapter {

		double _previousX;
		double _previousY;

		private Point _currentLocationInComponent;

		public DragHandler() {
			super();
			_currentLocationInComponent = new Point();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			_dragging = true;
			_self.cursorDragged(e.getPoint().getX(), e.getPoint().getY());
			_previousX = e.getPoint().getX();
			_previousY = e.getPoint().getY();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			_currentLocationInComponent = new Point((int) e.getX(), (int) e.getY());
		}

		public Point getCurrentLocation() {
			return _currentLocationInComponent;
		}
	}
}
