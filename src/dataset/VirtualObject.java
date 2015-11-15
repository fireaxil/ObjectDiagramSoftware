package dataset;

import java.util.ArrayList;


public class VirtualObject {

	private VirtualClass _type;
	private ArrayList<VirtualInstanceVariable> _instanceVariables;
	
	public VirtualObject(VirtualClass type) {
		_type = type;
		_instanceVariables = new ArrayList<VirtualInstanceVariable>();
	}
	
	public VirtualClass getType() {
		return _type;
	}
	
	public void addInstanceVariable(VirtualInstanceVariable v) {
		_instanceVariables.add(v);
	}
	
	public ArrayList<VirtualInstanceVariable> getInstanceVariables() {
		return _instanceVariables;
	}
	
}
