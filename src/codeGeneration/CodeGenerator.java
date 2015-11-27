package codeGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import dataset.*;

public class CodeGenerator {

	private int _numClasses;
	private int _numObjects;
	private int _numInstanceVars;

	private ArrayList<VirtualClass> _classes;
	private ArrayList<VirtualObject> _instances;

	private MasterSet _masterSet;

	private ArrayList<String> _cases;
	private HashMap<String, String> _caseMapping;
	private HashMap<String, Integer> _caseOrder;

	public CodeGenerator(int numClasses, int numObjects, int numInstanceVars){
		_numClasses = numClasses;
		_numObjects = numObjects;
		_numInstanceVars = numInstanceVars;
		_masterSet = new MasterSet();

		_cases = new ArrayList<String>();
		_caseMapping = new HashMap<String, String>();


		_caseOrder = new HashMap<String, Integer>();
		_caseOrder.put("Free Case", 0);
		_caseOrder.put("First Order Singular", 1);
		_caseOrder.put("Dual Restrictive Case", 2);
		_caseOrder.put("Last Order Singular", 3);
	}

	public ArrayList<String> generate(){
		ArrayList<String> code = new ArrayList<String>();
		_masterSet.randomize(_numClasses, _numObjects, _numInstanceVars);
		_masterSet.output();
		_classes = _masterSet.getClasses();
		_instances = _masterSet.getObjects();
		_instances.remove(_instances.size()-1);
		ArrayList<String> classes = createClasses(_classes);
		for(int i = 0; i< _classes.size(); i++){
			code.add(classes.get(i));
		}
		
		code.add(generateMain());
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

		System.out.println("For " + virtualClass.getName() + ", " + "The determined case is: " + cased);

		_cases.add(cased);
		_caseMapping.put(virtualClass.getName(), cased);

		ArrayList<VirtualInstanceVariable> vars = virtualClass.getInstanceVars();
		if(cased.equals("Dual Restrictive Case")){
			finalCode = "\n\tpublic " + virtualClass.getName() + "() {\n\n\t}\n\n";
			for(int i = 0; i < vars.size(); i ++){
				finalCode += "\tpublic void set" + vars.get(i).getName() + "(" + vars.get(i).getType() + " " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ") {\n\t\t" + vars.get(i).getName() + " = " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ";\n	}\n";
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
					finalCode += vars.get(i).getType() + " " + vars.get(i).getName().substring(1, vars.get(i).getName().length()) + ") {\n";
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
						decider = 2;
					}
				}

				//check to see if anything is referring to this object
				for (VirtualObject otherVO : _instances) {
					for (VirtualInstanceVariable viv : otherVO.getInstanceVariables()) {
						if (viv.getTarget() != null) {
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
				}

				//check to see if it is referring to anything
				for (VirtualInstanceVariable viv : vo.getInstanceVariables()){
					if (viv.getTarget() != null){
						if (decider == 1){
							decider = 2;
						} else {
							decider = 3;
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
		return cased;
	}

	public String generateMain(){

		// *** GENERATE VARIABLE INSTANTIATIONS ***

		String header = "public class Driver {\n\t public static void main(String[] args) {\n\n ";

		//reorder cases
		int smallestSoFar;
		for (int i = 0; i < _cases.size(); i++) {
			smallestSoFar = i;
			for (int j = i; j < _cases.size(); j++) {
				if (_caseOrder.get(_cases.get(j)) < _caseOrder.get(_cases.get(smallestSoFar))) {
					smallestSoFar = j;
				}
			}
			Collections.swap(_cases, i, smallestSoFar);
		}

		String beginning = "\t\t";
		String ending = "";
		String dualCleanup = "";

		// <object, varName>
		HashMap<VirtualObject, String> objectMap = new HashMap<VirtualObject, String>();

		//instantiate classes in order
		int charNumber = 0;
		for (String caseString : _cases) {
			for (VirtualClass vc : _classes) {
				if (_caseMapping.get(vc.getName()).equals(caseString)) {
					for (VirtualObject o : _instances) {
						if (o.getTypeName().equals(vc.getName())) {

							//first two rounds of declaration/assignment statements
							if (caseString.equals("Free Case") || caseString.equals("First Order Singular") 
									|| caseString.equals("Dual Restrictive Case")) {
								beginning += vc.getName() + " " + getChar(charNumber) + " = new " + vc.getName() + "();\n\t\t";



								objectMap.put(o, getChar(charNumber));
								charNumber++;
							}



							//finishing off with some association relationships
							if (caseString.equals("Last Order Singular")) {
								System.out.println("HIT");
								ending += vc.getName() + " " + getChar(charNumber) + " = new " + vc.getName() + "(";

								for (VirtualInstanceVariable iv : o.getInstanceVariables()) {

									if (iv.getTarget() == null) {
										ending += "null, ";	
									} 
									else {
										ending += objectMap.get(iv.getTarget()) + ", ";
									}
								}
								objectMap.put(o, getChar(charNumber));
								ending = ending.substring(0, ending.length() - 2);
								ending += "); \n\t\t";

								charNumber++;
							}
						}
					}
				}
			}
		}
		for (String caseString : _cases) {
			for (VirtualClass vc : _classes) {
				if (_caseMapping.get(vc.getName()).equals(caseString)) {
					for (VirtualObject o : _instances) {
						if (o.getTypeName().equals(vc.getName())) {
							if(caseString.equals("Dual Restrictive Case")){
								for(int i = 0; i < o.getInstanceVariables().size(); i ++){
									if(o.getInstanceVariables().get(i).getTarget() != null){
										dualCleanup += objectMap.get(o) + ".set" + o.getInstanceVariables().get(i).getName() + "(" + objectMap.get(o.getInstanceVariables().get(i).getTarget()) + ");\n";
									}
								}
							}
						}
					}
				}
			}
		}

		// *** GENERATE VARIABLE ASSIGNMENTS



		return header + beginning + ending + dualCleanup + "\n\t}\n}";
	}

	public String getChar(int charNumber){
		return Character.toString((char)('a' + charNumber));
	}

	public ArrayList<String> getCases() {
		return _cases;
	}

	/*public static void main(String[] args){

		CodeGenerator c = null;

		ArrayList<String> a = null;
		boolean first = false;
		boolean second = false;

		while (!(first && second)) {
			c = new CodeGenerator(4,10,3);
			a = c.generate();
			for (String s : c.getCases()) {
				if (s.equals("First Order Singular")) {
					first = true;
				}

				if (s.equals("Last Order Singular")) {
					second = true;
				}
			}
		}



		
		a.add(c.generateMain());
		System.out.println(a.toString());
	}*/

}
