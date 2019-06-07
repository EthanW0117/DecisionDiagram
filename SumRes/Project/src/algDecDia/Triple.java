package algDecDia;

public class Triple {
	public String var;
	public int lowId;
	public int highId;
	
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
	
	public String toString() {
		return var+lowId+highId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Triple) {
			Triple e = (Triple) o;
				return this.var == e.var && this.lowId == e.lowId && this.highId == e.highId;
		}
		return false;
	}
	@Override
	public int hashCode() {
		int hc;
		hc = var.hashCode() + lowId * 37 + highId * 37 * 37;
		return hc;
	}
}
