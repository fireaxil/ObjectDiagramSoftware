package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

public class CodeArea extends JPanel implements Observer {
	
	private static int SELECTOR_PANEL_HEIGHT = 30;
	
	JComboBox _classFileSelector;
	JTextArea _codeView;
	
	UI _ui;	
	private codeGeneration.CodeGenerator _cgn;
	
	public CodeArea(UI ui) {
		super();
		setLayout(null);
		_ui = ui;
		_cgn = new codeGeneration.CodeGenerator(4,10,3);

		// ***** SET UP CLASS FILE SELECTOR *****
		JPanel selectorPanel = new JPanel();
		selectorPanel.setLayout(new FlowLayout());
		UI.formatJComponent(selectorPanel, new Dimension(UI.WINDOW_WIDTH / 2 - UI.PADDING * 2, SELECTOR_PANEL_HEIGHT), 
				UI.PADDING, UI.PADDING / 2);
		add(selectorPanel);
		
		JLabel selectorLabel = new JLabel("Select class file to view: ");
		selectorPanel.add(selectorLabel);
		
		ArrayList<String> files = _cgn.generate();
		String[] fileSwitcher = new String[files.size()];
		for(int i = 0; i < files.size(); i++){
			if(i == files.size()-1){
				fileSwitcher[i] = "Driver";
			}
			else{
				fileSwitcher[i] = dataset.MasterSet.classNames[i];
			}
		}
		_classFileSelector = new JComboBox<String>(fileSwitcher);

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
		
		_classFileSelector.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JComboBox jcb = (JComboBox)e.getSource();
				
				int index = jcb.getSelectedIndex();
				System.out.println(_codeView.getText());
				
				System.out.println(files.get(index));
			}
			
		});
		add(_codeView);
		
		//_ui.getModel().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
