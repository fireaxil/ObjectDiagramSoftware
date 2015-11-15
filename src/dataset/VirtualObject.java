package dataset;

import java.util.ArrayList;


public class VirtualObject {

	private VirtualClass _type;
	private ArrayList<VirtualInstanceVariable> _instanceVariables;
	private ArrayList<VirtualObject> _targetedBy;
	
	public VirtualObject(VirtualClass type) {
		_type = type;
		_instanceVariables = new ArrayList<VirtualInstanceVariable>();
		_targetedBy = new ArrayList<VirtualObject>();
	}
	
	public VirtualClass getType() {
		return _type;
	}
	
	public String getTypeName() {
		return _type.getName();
	}
	
	public void addInstanceVariable(VirtualInstanceVariable v) {
		_instanceVariables.add(v);
	}
	
	public ArrayList<VirtualInstanceVariable> getInstanceVariables() {
		return _instanceVariables;
	}
	
	public void addToTargetedBy(VirtualObject virtualObject){
		_targetedBy.add(virtualObject);
	}
	
	@Override
	public String toString() {
		return _type.getName();
	}
	
}
