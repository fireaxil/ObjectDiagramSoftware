package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import dataset.MasterSet;
import dataset.VirtualClass;
import dataset.VirtualInstanceVariable;
import dataset.VirtualObject;
import ui.shapes.ObjectShape;
import ui.shapes.Shape;
import ui.shapes.VariableShape;

/*
 	I see no need for instantiating this class, because it need not store any information. 
 	All it really needs is to be able to do is take in information and spit out a result. That is,
 	compare the information in shapes with the information in set, and state whether they agree.

 */

public class SolutionChecker {

	public static boolean checkSolution(ArrayList<Shape> shapes, MasterSet set) {

		ArrayList<VirtualClass> classes = set.getClasses();
		ArrayList<VirtualObject> instances = set.getObjects();

		boolean checkClassesNum = false;
		boolean checkClassName = false;
		boolean checkObjectNum = false;
		boolean checkObjectName = false;
		boolean checkVarNum = false;
		boolean checkVarNames = false;
		boolean checkVarNamesInObjects = false;
		boolean checkReferences = false;

		//checking class number
		HashSet<String> classCount = new HashSet<String>();

		for(int i = 0 ; i <shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(ObjectShape.class)){
				classCount.add(shapes.get(i).getName());
			}		
		}

		if(classCount.size() == classes.size()){
			checkClassesNum = true;
		}
		else{
			return false;
		}

		//checking classNames
		for(int i = 0; i < classes.size(); i ++){
			if(classCount.contains(classes.get(i).getName())){
				checkClassName = true;
			}
			else{
				return false;
			}
		}

		//checking Object number
		int ObjectNum = 0;
		for(int i = 0; i <shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(ObjectShape.class)){
				ObjectNum ++;
			}
		}

		if(ObjectNum == instances.size()){
			checkObjectNum = true;
		}
		else{
			return false;
		}


		//checking ObjectNames and corresponding numbers
		HashMap<String, Integer> existingObjects = new HashMap<String, Integer>();

		for(int i = 0; i <shapes.size(); i++ ){
			if(shapes.get(i).getClass().equals(ObjectShape.class)){
				if(existingObjects.containsKey(shapes.get(i).getName())){
					existingObjects.put(shapes.get(i).getName() , existingObjects.get(shapes.get(i).getName() + 1));
				}
				else{
					existingObjects.put(shapes.get(i).getName(), 1);
				}	
			}
		}

		HashMap<String, Integer> wantedObjects = new HashMap<String, Integer>();

		for(int i = 0; i < instances.size(); i ++){
			if(wantedObjects.containsKey(instances.get(i).getTypeName())){
				wantedObjects.put(instances.get(i).getTypeName(), wantedObjects.get(instances.get(i).getTypeName() + 1));
			}
			else{
				wantedObjects.put(instances.get(i).getTypeName(), 1);
			}
		}

		if(existingObjects.equals(wantedObjects)){
			checkObjectName = true;
		}
		else{
			return false;
		}

		//checking number of instance variables
		int varNum = 0;
		int expectedVarNum = 0;

		for(int i = 0; i < instances.size(); i ++){
			expectedVarNum += instances.get(i).getInstanceVariables().size();
		}

		for(int i = 0; i < shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(VariableShape.class)){
				varNum ++;
			}
		}

		if(varNum == expectedVarNum){
			checkVarNum = true;
		}
		else{
			return false;
		}

		//checking instance Variable Names
		HashSet<String> expectedVarNames = new HashSet<String>();
		for(int i= 0; i < instances.size(); i ++){
			ArrayList<VirtualInstanceVariable> iv = instances.get(i).getInstanceVariables();
			for(int y = 0; y < iv.size(); y++){
				expectedVarNames.add(iv.get(y).getName());
			}
		}

		HashSet<String> varNames = new HashSet<String>();
		for(int i = 0; i < shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(VariableShape.class)){
				varNames.add(shapes.get(i).getName());
			}
		}

		if(varNames.size() == expectedVarNames.size()){
			for(String s : varNames){
				if(expectedVarNames.contains(s)){
					checkVarNames = true;
				}
				else{
					return false;
				}
			}
		}
		else{
			return false;
		}

		//check variable names in each object
		ArrayList<ObjectShape> currentObjects = new ArrayList<ObjectShape>();
		for(int i = 0; i < shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(ObjectShape.class)){
				currentObjects.add((ObjectShape) shapes.get(i));
			}
		}
		
		for(int i = 0; i < currentObjects.size(); i ++){
			ArrayList<VariableShape> iv = currentObjects.get(i).getInstanceVariables();
			String s = currentObjects.get(i).getName();
			for(int y = 0; y<instances.size(); y++){
				if(instances.get(y).getTypeName().equals(s)){
					ArrayList<VirtualInstanceVariable> iv2 = instances.get(y).getInstanceVariables();
					ArrayList<String> iv2Strings = new ArrayList<String>();
					for(int z = 0; z <iv2.size(); z ++){
						iv2Strings.add(iv2.get(z).getName());	
					}
					for(int x  = 0; x < iv.size(); x ++){
						if(iv2Strings.contains(iv.get(x).getName())){
							checkVarNamesInObjects = true;
						}
						else{
							return false;
						}
					}
				}
			}
		}
		
		
		//Check References
		ArrayList<VariableShape> vars = new ArrayList<VariableShape>();
		for(int i = 0; i < shapes.size(); i++){
			if(shapes.get(i).getClass().equals(VariableShape.class)){
				vars.add((VariableShape) shapes.get(i));
			}
		}
		
		for(int i = 0; i <vars.size(); i ++){
			String s = vars.get(i).getName();
			for(VirtualObject o: instances){
				ArrayList<VirtualInstanceVariable> iv = o.getInstanceVariables();
				for(VirtualInstanceVariable v: iv){
					if(v.getName().equals(s)){
						String target = vars.get(i).getReference().getName();
						if(target.equals(v.getTarget().getTypeName())){
							checkReferences = true;
						}
						else{
							checkReferences = false;
						}
					}
				}
			}	
		}
		
		
		if(checkClassesNum && checkClassName && checkObjectNum && checkObjectName && checkVarNum && checkVarNames && checkVarNamesInObjects && checkReferences){
			return true;
		}

		return false;

	}

}
