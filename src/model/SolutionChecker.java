package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dataset.MasterSet;
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

	public static boolean checkSolution(ArrayList<Shape> shapes, MasterSet set, HashMap<String, ArrayList<String[]>> locals) {
		JFrame frame = new JFrame();

		//ArrayList<VirtualClass> classes = set.getClasses();
		ArrayList<VirtualObject> instances = set.getObjects();

		//boolean checkClassesNum = false;
		boolean checkClassName = false;
		boolean checkObjectNum = false;
		//boolean checkObjectName = false;
		boolean checkVarNum = false;
		boolean checkVarNames = false;
		boolean checkVarNamesInObjects = false;
		boolean checkReferences = false;

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
			JOptionPane.showMessageDialog(frame, "Check your number of Objects");
			return false;
		}

		//checking class names
		HashSet<String> classCount = new HashSet<String>();
		Set<String> currentlocalVars =  locals.keySet();


		for(int i = 0 ; i <shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(ObjectShape.class)){
				classCount.add(shapes.get(i).getName());
			}		
		}

		/*if(classCount.size() == classes.size()){
			checkClassesNum = true;
		}
		else{
			JOptionPane.showInputDialog("Check your number of Classes");
			return false;
		}*/

		//checking classNames
		//for(int i = 0; i < classes.size(); i ++){
		if(classCount.equals(currentlocalVars)){
			//if(classCount.contains(classes.get(i).getName())){
			checkClassName = true;
		}
		else{
			JOptionPane.showMessageDialog(frame,"Check your Class names");
			return false;
		}
		//}


		//checking ObjectNames and corresponding numbers
		/*HashMap<String, Integer> existingObjects = new HashMap<String, Integer>();

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

		if(existingObjects.equals(locals)){
			checkObjectName = true;
		}
		else{
			JOptionPane.showInputDialog("Check your number of Objects and Corresponding number of instantiations");
			return false;
		}*/

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
			JOptionPane.showMessageDialog(frame, "Check your number of Instance Variables");
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
					JOptionPane.showMessageDialog(frame, "Check your Instance Variable Names");
					return false;
				}
			}
		}
		else{
			JOptionPane.showMessageDialog(frame, "Check your Instance Variable Names");
			return false;
		}

		//check variable names in each object
		ArrayList<ObjectShape> currentObjects = new ArrayList<ObjectShape>();
		for(int i = 0; i < shapes.size(); i ++){
			if(shapes.get(i).getClass().equals(ObjectShape.class)){
				currentObjects.add((ObjectShape) shapes.get(i));
			}
		}

		for(ObjectShape o: currentObjects){
			ArrayList<VariableShape> iv = o.getInstanceVariables();
			String s = o.getName();
			for(VirtualObject vo : instances){
				if(vo.getLocalVariable().equals(s)){
					ArrayList<VirtualInstanceVariable> iv2 = vo.getInstanceVariables();
					ArrayList<String> iv2Strings = new ArrayList<String>();
					for(VirtualInstanceVariable viv: iv2){
						iv2Strings.add(viv.getName());
					}
					for(VariableShape vs : iv){
						if(iv2Strings.contains(vs.getName())){
							checkVarNamesInObjects = true;
						}
						else{
							JOptionPane.showMessageDialog(frame, "Check your Variable Names in each Object");
							return false;
						}
					}

				}
			}
		}

		/*for(int i = 0; i < currentObjects.size(); i ++){
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
							JOptionPane.showMessageDialog(frame, "Check your Variable Names in each Object");
							return false;
						}
					}
				}
			}
		}*/


		//Check References
		/*ArrayList<VariableShape> vars = new ArrayList<VariableShape>();
		for(int i = 0; i < shapes.size(); i++){
			if(shapes.get(i).getClass().equals(VariableShape.class)){
				vars.add((VariableShape) shapes.get(i));
			}
		}*/

		int correctReferences = 0;
		int wantedReferences = 0;

		for(VirtualObject o : instances){
			ArrayList<VirtualInstanceVariable> iv =  o.getInstanceVariables();
			for(VirtualInstanceVariable v : iv){
				if(v.getTarget() == null){
					wantedReferences += 1;
				}
				else{
					wantedReferences += 2;
				}
			}
		}
		/*for(VirtualObject x: instances){
			ArrayList<VirtualInstanceVariable> ix = x.getInstanceVariables();
			System.out.println(ix.toString());
			for(VirtualInstanceVariable v : ix){
				System.out.println(v.getTarget());
			}
		}*/

		for(ObjectShape os: currentObjects){
			ArrayList<VariableShape> varso = os.getInstanceVariables();
			for(VariableShape vs: varso){
				String s = vs.getName();
				for(VirtualObject o: instances){
					ArrayList<VirtualInstanceVariable> iv = o.getInstanceVariables();
					for(VirtualInstanceVariable v: iv){
						if(v.getName().equals(s) && v.getOrigin().getLocalVariable().equals(os.getName())){
							if(vs.getReference() != null && v.getTarget() != null){
								String target = vs.getReference().getName();
								if(target.equals(v.getTarget().getLocalVariable())){
									correctReferences +=2;
									//checkReferences = true;
								}
								/*else{
									checkReferences = false;
								}*/

							}
							if(vs.getReference() == null && v.getTarget() == null){
								correctReferences +=1;

							}
						}
					}
				}	
			}
		}

		System.out.println(correctReferences);
		System.out.println(wantedReferences);
		if(correctReferences == wantedReferences){
			checkReferences = true;
		}
		else{
			JOptionPane.showMessageDialog(frame, "Check your References");
			return false;	
		}


		if(checkClassName && checkObjectNum && checkVarNum && checkVarNames && checkVarNamesInObjects && checkReferences){
			JOptionPane.showMessageDialog(frame, "Good Job!");
			return true;
		}

		return false;

	}

}
