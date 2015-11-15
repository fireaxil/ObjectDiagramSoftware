import dataset.MasterSet;


public class MasterSetDriver {
	
	public static void main(String[] args) {
		
		MasterSet m = new MasterSet();
		m.randomize(3, 6, 2);
		m.output();
		
	}

}
