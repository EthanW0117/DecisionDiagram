package ADD;

public class Triple {
	private String var;
	private int lowId;
	private int highId;
	
	public Triple(String var, int lowId, int highId) {
		this.var = var;
		this.lowId = lowId;
		this.highId = highId;
	}
	
	public String getVar() {
		return this.var;
	}
	
	public int getLowId() {
		return this.lowId;
	}
	
	public int getHighId() {
		return this.highId;
	}
}
