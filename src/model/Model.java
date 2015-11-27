package model;

import ui.shapes.Shape;
import java.util.ArrayList;

import codeGeneration.*;
import dataset.*;
import ui.UI;

/* We should be able to call generateNewCode on the model, which will:
   		- put a MasterSet object in _masterSet
   		- put a CodeGenerator object in _codeGenerator
   		
   I'm thinking if we could define a getCode() method which, when called on _codeGenerator,
   will return the generated code in this format:
   
   		ArrayList<String[]> such that each String contains:
   		
   				{ CLASSNAME, CLASS CODE }
  
 */


public class Model {

	private UI _ui;
	private CodeGenerator _codeGenerator;
	private MasterSet _masterSet;
	
	public Model(UI ui) {
		_ui = ui;
	}
	
	public void createNewCode() {
		
		_codeGenerator = new CodeGenerator(3, 10, 4);
		
		/*
		 
		 I would like to be able to call a method like this:
		 _masterSet = _codeGenerator.getMasterSet();
		 
		 This assumes that _codeGenerator will generate some indeterminate number of MasterSets
		 until it finds one that meets the appropriate conditions. It will then assign that 
		 MasterSet to an instance variable that will be accessible here.
		 
		 */
		
		
	}
	
	public ArrayList<String[]> getCode() {
		
		//return _codeGenerator.getCode();
		
		return null;
	}
	
	/* essentially a wrapper method for the sake of encapsulating model functionality 
	(besides, only the model should have access to the masterset) */
	public boolean checkSolution(ArrayList<Shape> shapes) {
		System.out.println("Checking Solution...");
		return SolutionChecker.checkSolution(shapes, _masterSet);
	}
}
