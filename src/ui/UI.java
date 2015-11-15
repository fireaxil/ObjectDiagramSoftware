package ui;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;

public class UI implements Runnable {

	JFrame _mainWindow;
	JPanel _contentPane;
	
	public static int WINDOW_WIDTH = 900;
	public static int WINDOW_HEIGHT = 450;
	
	public UI() {
		
	}
	
	@Override
	public void run() {
		
		_mainWindow = new JFrame("CSE115 Object Diagram Practice");
		_mainWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		_contentPane = new JPanel();
		_contentPane.setLayout(null);
		_mainWindow.setContentPane(_contentPane);
		
		// ***** SETTING UP CODE AREA *****
		CodeArea code = new CodeArea(this);
		formatJComponent(code, new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT), 0, 0);
		_contentPane.add(code);
		
		// ***** SETTING UP EDITOR PANE *****
		JPanel jPanel = new JPanel();
        JPanel buttonHolder = new JPanel();

        formatJComponent(jPanel, new Dimension(WINDOW_WIDTH /2, WINDOW_HEIGHT), WINDOW_WIDTH /2 , 0);
        formatJComponent(buttonHolder, new Dimension(200, 200), WINDOW_WIDTH / 2 , 300);

        JLabel jLabel = new JLabel("Yooo what's uppppppp ;)");
        JButton arrow = new JButton("ARROW");
        arrow.setBounds(WINDOW_WIDTH /2, 350, 100, 100);
        jPanel.add(arrow);

        jPanel.add(jLabel);
        jPanel.add(buttonHolder);
        buttonHolder.setVisible(true);
        jPanel.setVisible(true);
		_contentPane.add(jPanel);
		_mainWindow.setVisible(true);
		
		
	}
	
	//to cut down on code repetition
	public static void formatJComponent(JComponent c, Dimension size, int x, int y) {
		c.setSize(size);
		c.setLocation(x, y);
	}
	
	public int windowPaneHeight() {
		return _mainWindow.getLayeredPane().getHeight();
	}

}
