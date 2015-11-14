package dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MasterSet {
	
	public static String[] classNames = {"Alpha", "Beta", "Gamma", "Delta"};

	private ArrayList<VirtualClass> _classes;
	private ArrayList<VirtualObject> _instances;
	private ArrayList<VirtualReference> _references;
	
	private ArrayList<String> _allInstanceVarNames;
	
	public MasterSet() {
		_references = new ArrayList<>();
	}
	
	public void randomize(int numClasses, int numObjects, int numReferences) {
		
		if (numObjects > 52) {
			System.out.println("That is too many objects");
			System.exit(0);
		}
		
		_classes = generateClasses(numClasses);
		_instances = generateObjects(numClasses, numObjects);
		
		if (numReferences > numObjects * 2) {
			System.err.println("That is too many references");
		} else {
			_allInstanceVarNames = variableNames(numReferences);
			generateReferences(numReferences);
		}
		
		
		
		
		
	}
	
	private ArrayList<VirtualClass> generateClasses(int numClasses) {
		
		if (numClasses < 1 || numClasses > 4) {
			System.err.println("you moron");
			return null;
		}
		
		ArrayList<VirtualClass> list = new ArrayList<VirtualClass>();
		for (int i = 0; i < numClasses; i++) {
			list.add(new VirtualClass(classNames[i]));
		}
		
		return list;
	}
	
	private ArrayList<VirtualObject> generateObjects(int numClasses, int numObjects) {
		
		if (numObjects < numClasses) {
			System.err.println("nope");
			return null;
		}
		
		ArrayList<VirtualObject> list = new ArrayList<VirtualObject>();
		
		//here we guarantee that there will be at least one object generated for each class
		for (VirtualClass c : _classes) {
			list.add(new VirtualObject(c));
		}
		
		//now, we generate a random number of objects for each existing class
		numObjects = numObjects - list.size();
		for (int i = 0; i < numObjects; i++) {
			Collections.shuffle(_classes);
			list.add(new VirtualObject(_classes.get(0)));
		}
		
		return list;
	}
	
	private void generateReferences(int numReferences){
		Random r = new Random();
		ArrayList<VirtualInstanceVariable> variables = new ArrayList<VirtualInstanceVariable>();
		
		for(int i = 0; i < numReferences; i++){
			int x = r.nextInt(_instances.size());
			VirtualInstanceVariable var = new VirtualInstanceVariable(_allInstanceVarNames.get(i));
			VirtualReference ref = new VirtualReference(var, _instances.get(x));
			var.setReference(ref);
			variables.add(var);
			_references.add(ref);
		}
		
		for (VirtualInstanceVariable v : variables) {
			Collections.shuffle(_classes);
			_classes.get(0).addInstanceVariable(v);
		}
	}
	
	private ArrayList<String> variableNames(int numReferences) {
		ArrayList<String> allNames = new ArrayList<String>();
		for(int i = 0; i < numReferences; i++){
			int x = 'a'+ i;
			if(x > 'z'){
				String s = "a" + Character.toString((char)(i - 'a'));
				allNames.add("_" + s);
			}
			else{
			String s = Character.toString((char)x);
			allNames.add("_" + s);
			}
		}
		return allNames;
	}
	
	public ArrayList<VirtualClass> getClasses(){
		return _classes;
	}
	
	public ArrayList<VirtualObject> getObjects(){
		return _instances;
	}
	
	public ArrayList<VirtualReference> getReferences(){
		return _references;
	}
}
