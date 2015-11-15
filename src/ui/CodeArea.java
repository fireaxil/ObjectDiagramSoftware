package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

public class CodeArea extends JPanel {
	
	private static int SELECTOR_PANEL_HEIGHT = 30;
	private static int PADDING = 5;
	
	JComboBox _classFileSelector;
	JTextArea _codeView;
	UI	_ui;
	
	public CodeArea(UI ui) {
		super();
		setLayout(null);
		_ui = ui;
		
		// ***** SET UP CLASS FILE SELECTOR *****
		JPanel selectorPanel = new JPanel();
		selectorPanel.setLayout(new FlowLayout());
		UI.formatJComponent(selectorPanel, new Dimension(UI.WINDOW_WIDTH / 2, SELECTOR_PANEL_HEIGHT), 
				PADDING, PADDING);
		add(selectorPanel);
		
		JLabel selectorLabel = new JLabel("Select class file to view: ");
		selectorPanel.add(selectorLabel);
		
		_classFileSelector = new JComboBox<String>();
		_classFileSelector.setPreferredSize(new Dimension(200, SELECTOR_PANEL_HEIGHT));
		selectorPanel.add(_classFileSelector);
		
		// ***** SET UP CODE VIEWER *****
		JTextArea codeView = new JTextArea();
		UI.formatJComponent(codeView, new Dimension(UI.WINDOW_WIDTH / 2, 
				UI.WINDOW_HEIGHT - PADDING), 
				PADDING, SELECTOR_PANEL_HEIGHT + PADDING * 2);
		add(codeView);
		
		
		
		
	}

}
