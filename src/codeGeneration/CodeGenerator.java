package codeGeneration;

import java.util.ArrayList;

import dataset.*;

public class CodeGenerator {
	
	private int _numClasses;
	private int _numObjects;
	private int _numReferences;
	
	private ArrayList<VirtualClass> _classes;
	private ArrayList<VirtualObject> _instances;
	private ArrayList<VirtualReference> _references;
	
	private MasterSet _masterSet;
	
	public CodeGenerator(int numClasses, int numObjects, int numReferences){
		_numClasses = numClasses;
		_numObjects = numObjects;
		_numReferences = numReferences;
		_masterSet = new MasterSet();
	}
	
	public String generate(){
		_masterSet.randomize(_numClasses, _numObjects, _numReferences);
		_classes = _masterSet.getClasses();
		_instances = _masterSet.getObjects();
		_references = _masterSet.getReferences();
		String code = "";
		return code;
	}
	
	public String createClasses(ArrayList<VirtualClass> _classes){
		String classCode = "";
		for(int i = 0; i < _classes.size(); i ++){
			String classFile = "";
			classFile += "public class " + _classes.get(i).getName() + " {\n\n";
			ArrayList<VirtualInstanceVariable> instances = _classes.get(i).getInstanceVars();
		
			
		}
		return classCode;
	}

}
