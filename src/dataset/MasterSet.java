package dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MasterSet {

	public static String[] classNames = {"Alpha", "Beta", "Gamma", "Delta"};

	private ArrayList<VirtualClass> _classes;
	private ArrayList<VirtualObject> _instances;

	int _instanceVariableCount;

	public MasterSet() {
		_instanceVariableCount = 0;


	}

	public void randomize(int numClasses, int numObjects, int maxInstanceVariables) {

		if (numObjects > 52) {
			System.out.println("That is too many objects");
			System.exit(0);
		}

		_classes = generateClasses(numClasses, maxInstanceVariables);
		_instances = generateObjects(numClasses, numObjects);
		_instances.add(null);
		assignReferences();
	}

	private ArrayList<VirtualClass> generateClasses(int numClasses, int maxInstanceVariables) {

		if (numClasses < 1 || numClasses > 4) {
			System.err.println("you moron");
			return null;
		}

		ArrayList<VirtualClass> list = new ArrayList<VirtualClass>();
		Random r = new Random();

		for (int i = 0; i < numClasses; i++) {
			VirtualClass vc = new VirtualClass(classNames[i]);
			int x = r.nextInt(maxInstanceVariables + 1);
			for (int j = 0; j < x; j++) {
				int z = r.nextInt(numClasses);
				vc.addInstanceVariable(createInstanceVariable(classNames[z]));
			}
			list.add(vc);
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
			VirtualObject o = new VirtualObject(_classes.get(0)); 
			for (VirtualInstanceVariable v : _classes.get(0).getInstanceVars()) {
				VirtualInstanceVariable newVariable = new VirtualInstanceVariable(v.getName(), v.getType());
				o.addInstanceVariable(newVariable);
			}
			list.add(o);
		}

		return list;
	}

	private void assignReferences() {
		Random r = new Random();
		for (VirtualObject o : _instances) {
			if(o!=null){
				for (VirtualInstanceVariable vi : o.getInstanceVariables()) {
					boolean checker = false;
					while(!checker){
						int x = r.nextInt(_instances.size());
						String s = vi.getType();
						
						if(_instances.get(x) == null ||_instances.get(x).getType().equals(s)){
							vi.setTarget(_instances.get(x));
							checker = true;
						}
					}
				}
			}
		}
	}

	//this needs to be revisited
	private VirtualInstanceVariable createInstanceVariable(String type) {
		int x = 'a'+ _instanceVariableCount;
		String s = "";
		if(x > 'z'){
			s = "_" + "a" + Character.toString((char)(_instanceVariableCount - 'a'));
		} else{
			s = "_" + Character.toString((char) x);
		}

		return new VirtualInstanceVariable(s, type);
	}

	public ArrayList<VirtualClass> getClasses(){
		return _classes;
	}

	public ArrayList<VirtualObject> getObjects(){
		return _instances;
	}
}
