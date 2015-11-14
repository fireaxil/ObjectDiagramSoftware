package dataset;

public class VirtualInstanceVariable {

	private VirtualReference _reference;
	private String _name;
	
	public VirtualInstanceVariable(String name) {
		_name = name;
	}
	
	public void setReference(VirtualReference reference) {
		_reference = reference;
	}
	
	public VirtualReference getReference() {
		return _reference;
	}
	
	public String getName() {
		return _name;
	}
}

