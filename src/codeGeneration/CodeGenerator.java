package codeGeneration;

import java.util.ArrayList;

import dataset.*;

public class CodeGenerator {

	private int _numClasses;
	private int _numObjects;
	private int _numInstanceVars;

	private ArrayList<VirtualClass> _classes;
	private ArrayList<VirtualObject> _instances;

	private MasterSet _masterSet;

	public CodeGenerator(int numClasses, int numObjects, int numInstanceVars){
		_numClasses = numClasses;
		_numObjects = numObjects;
		_numInstanceVars = numInstanceVars;
		_masterSet = new MasterSet();
	}

	public ArrayList<String> generate(){
		ArrayList<String> code = new ArrayList<String>();
		_masterSet.randomize(_numClasses, _numObjects, _numInstanceVars);
		_classes = _masterSet.getClasses();
		_instances = _masterSet.getObjects();
		_instances.remove(_instances.size()-1);
		for(int i = 0; i < createClasses(_classes).size(); i++){
			code.add(createClasses(_classes).get(i));
		}

		return code;
	}

	public ArrayList<String> createClasses(ArrayList<VirtualClass> classes){
		ArrayList<String> code = new ArrayList<String>();
		for(int i = 0; i < _classes.size(); i ++){
			String classFile = "";
			classFile += "public class " + _classes.get(i).getName() + " {\n\n";
			ArrayList<VirtualInstanceVariable> instances = _classes.get(i).getInstanceVars();
			for(VirtualInstanceVariable vi: instances){
				classFile += "\tprivate " + vi.getType() + " " + vi.getName() + ";\n"; 
			}
			String finalCode = createFinalCode(_classes.get(i));
			classFile += finalCode;


			code.add(classFile);
		}
		return code;
	}

	public String createFinalCode(VirtualClass virtualClass){
		String finalCode = "";
		String cased = determineCase(virtualClass);
		
		System.out.println("The determined case is: " + cased);
		
		ArrayList<VirtualInstanceVariable> vars = virtualClass.getInstanceVars();
		if(cased.equals("Dual Restrictive Case")){
			finalCode = "\n\tpublic " + virtualClass.getName() + "() {\n\n\t}\n\n\t";
			for(int i = 0; i < vars.size(); i ++){
				finalCode += "\tpublic void set" + vars.get(i).getName() + "(" + vars.get(i).getType() + " " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ") {\n\t" + vars.get(i).getName() + " = " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ";\n	}\n";
			}
			finalCode += "}";
		}
		if(cased.equals("Free Case")||cased.equals("First Order Singular")){
			finalCode = "\n\tpublic " + virtualClass.getName() + "() {\n\n\t}\n}";
		}
		if(cased.equals("Last Order Singular")){
			finalCode = "\n\tpublic " + virtualClass.getName() + "( ";
			for(int i = 0; i < vars.size(); i ++){
				if(i == vars.size()-1){
					finalCode += vars.get(i).getType() + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ") {\n";
				}
				else{
					finalCode += vars.get(i).getType() + " " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + " , ";
				}
			}
			for(int i = 0; i < vars.size(); i ++){
				finalCode += "\t" + vars.get(i).getName() + " = " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ";\n";
			}
			finalCode += "\t}\n}";

		}


		return finalCode;
	}

	public String determineCase(VirtualClass virtualClass){
		String cased = "";
		int decider = 0;
		
		for(VirtualObject vo: _instances){
			if(vo.getTypeName().equals(virtualClass.getName())){
				ArrayList<VirtualInstanceVariable> vars = vo.getInstanceVariables();
				
				//check to see if an object is referring to itself
				for(VirtualInstanceVariable vi: vars){
					VirtualObject tempVirtualObject = vi.getTarget();
					if(vo.equals(tempVirtualObject)){
						return "Dual Restrictive Case";
					}
				}

				//check to see if anything is referring to this object
				for (VirtualObject otherVO : _instances) {
					for (VirtualInstanceVariable viv : otherVO.getInstanceVariables()) {
						if(viv.getTarget().equals(vo)){
							vo.addToTargetedBy(otherVO);
							if (decider == 0) {
								decider = 1;
							} else {
								decider = 2;
							}
						}
					}
				}

				//check to see if it is referring to anything
				for(VirtualInstanceVariable viv : vo.getInstanceVariables()){
					if(viv.getTarget() != null){
						if(decider == 1){
							decider = 2;
						} else if (decider == 3) {
							decider = 3;
						} else if (decider == 2) {
							decider = 2;
						}
					}
				}
			}
		}
		
		switch(decider){
		case 0: 
			cased = "Free Case";
			break;
		case 1: 
			cased = "First Order Singular";
			break;
		case 2:
			cased =  "Dual Restrictive Case";
			break;
		case 3:
			cased =  "Last Order Singular";
			break;
		}
		
		System.out.println("The decider is: " + decider);
		return cased;
	}
	
	public String generateMain(){
		String s = "";
		s += "public class Driver {\n\t public static void main(String[] args) {\n\t ";
		
		return s;
	}

	public static void main(String[] args){
		CodeGenerator c = new CodeGenerator(3,6,3);
		System.out.print(c.generate().toString());
		System.out.print(c.generateMain());
	}

}
