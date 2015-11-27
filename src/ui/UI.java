package ui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.*;


public class UI implements Runnable {
	
	public static int WINDOW_WIDTH = 1050;
	public static int WINDOW_HEIGHT = 525;
	private int _paneHeight;
	public static int PADDING = 12;
	
	private JFrame _window;
	private JLayeredPane _mainContentPane;
	
//	private Model _model;
	
	public UI() {
		
		
		
	}

	public static void formatJComponent(JComponent j, Dimension d, int x, int y) {
		j.setSize(d);
		j.setLocation(x, y);
	}
	
	public static AlphaComposite generateAlpha(float alpha) {
		//http://www.javaworld.com/article/2076733/java-se/antialiasing--images--and-alpha-compositing-in-java-2d.html?page=2
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		return a;
	}
	
	public static void enableAntiAliasing(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}

	@Override
	public void run() {
		
		//setting up main frame and main panel
		_window = new JFrame("Object Diagram Analysis");
		_mainContentPane = new JLayeredPane();
		_mainContentPane.setLayout(null);
		_window.setContentPane(_mainContentPane);
		_window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_window.setVisible(true);
		_paneHeight = _window.getRootPane().getHeight();
		
		// *** SETTING UP CODE VIEWER
		CodeArea codeView = new CodeArea(this);
		UI.formatJComponent(codeView, new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT), 0, 0);
		_mainContentPane.add(codeView);
		
		// *** SETTING UP THE DIAGRAM EDITOR
		EditorPane edit = new EditorPane(this);
		UI.formatJComponent(edit, new Dimension(WINDOW_WIDTH / 2 - PADDING * 2, _paneHeight - PADDING * 2), 
				WINDOW_WIDTH / 2 + PADDING, PADDING);
		_mainContentPane.add(edit);
		
	}	
	
	public int getPaneHeight() {
		return _paneHeight;
	}
	
	public Point getLocation() {
		return _window.getLocation();
	}
}
