package codeGeneration;

import dataset.*;

public class CodeGenerator {
	
	private int _numClasses;
	private int _numObjects;
	private int _numReferences;
	private MasterSet _masterSet;
	
	public CodeGenerator(int numClasses, int numObjects, int numReferences){
		_numClasses = numClasses;
		_numObjects = numObjects;
		_numReferences = numReferences;
		_masterSet = new MasterSet();
	}
	
	public String generate(){
		_masterSet.randomize(_numClasses, _numObjects, _numReferences);
		return "";
	}

}
