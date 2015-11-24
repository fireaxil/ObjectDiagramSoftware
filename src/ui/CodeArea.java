package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class CodeArea extends JPanel implements Observer {
	
	private static int SELECTOR_PANEL_HEIGHT = 30;
	
	JComboBox _classFileSelector;
	JTextArea _codeView;
	
	UI _ui;
	
	public CodeArea(UI ui) {
		super();
		setLayout(null);
		_ui = ui;
		
		// ***** SET UP CLASS FILE SELECTOR *****
		JPanel selectorPanel = new JPanel();
		selectorPanel.setLayout(new FlowLayout());
		UI.formatJComponent(selectorPanel, new Dimension(UI.WINDOW_WIDTH / 2 - UI.PADDING * 2, SELECTOR_PANEL_HEIGHT), 
				UI.PADDING, UI.PADDING / 2);
		add(selectorPanel);
		
		JLabel selectorLabel = new JLabel("Select class file to view: ");
		selectorPanel.add(selectorLabel);
		
		_classFileSelector = new JComboBox<String>();
		_classFileSelector.setPreferredSize(new Dimension(200, SELECTOR_PANEL_HEIGHT));
		selectorPanel.add(_classFileSelector);
		
		// ***** SET UP CODE VIEWER *****
		JScrollPane scrollPane = new JScrollPane();
		UI.formatJComponent(scrollPane, new Dimension(UI.WINDOW_WIDTH / 2 - UI.PADDING * 2, 
				_ui.getPaneHeight() - SELECTOR_PANEL_HEIGHT - UI.PADDING * 3), 
				UI.PADDING, SELECTOR_PANEL_HEIGHT + UI.PADDING * 2);
		add(scrollPane);
		
		_codeView = new JTextArea();
		scrollPane.add(_codeView);
		add(_codeView);
		
		//_ui.getModel().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
