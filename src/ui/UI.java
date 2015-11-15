package ui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

import java.util.List;

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
		DrawingPanel jPanel = new DrawingPanel();
        JPanel buttonHolder = new JPanel();

        formatJComponent(jPanel, new Dimension(WINDOW_WIDTH /2, WINDOW_HEIGHT), WINDOW_WIDTH /2 , 0);
        formatJComponent(buttonHolder, new Dimension(200, 200), WINDOW_WIDTH / 2 , 300);

        JLabel jLabel = new JLabel("Edit Object Diagram Here:");
        
        JButton arrow = new JButton("ARROW");
		
        JButton circle = new JButton("CIRCLE");
		circle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Circle c = new Circle(100, 100, 50, 50);
		        jPanel.addCircle(c);
			}
		});
		
		JButton square = new JButton("SQUARE");

        arrow.setBounds(WINDOW_WIDTH /2, 40, 100, 50);
        circle.setBounds(WINDOW_WIDTH /2, 90 ,100 ,50);
		square.setBounds(WINDOW_WIDTH / 2, 140, 100, 50);

        
        
        

		_contentPane.add(arrow);
		_contentPane.add(circle);
		_contentPane.add(square);
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

    class Circle {
        int x,y, width, height;

        public Circle(int x, int y, int width, int height){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void draw(Graphics g){
            g.drawOval(x, y, width, height);
        }

    }
    class DrawingPanel extends JPanel {
        List <Circle> circles = new ArrayList<>();

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            for(Circle circle : circles){
                circle.draw(g);
            }
        }

        public void addCircle(Circle circle){
            circles.add(circle);
            repaint();
        }

        @Override
        public Dimension getPreferredSize(){
            return new Dimension(100,100);
        }

    }
}
