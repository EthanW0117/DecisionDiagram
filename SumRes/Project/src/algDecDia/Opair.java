package algDecDia;

public class Opair {

	public int id1;
	public int id2;
	public String op;
	
	public Opair(int id1, int id2, String op) {
		this.id1 = id1;
		this.id2 = id2;
		this.op = op;
	}
	
	public String toString() {
		return id1+op+id2;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Opair) {
			Opair e = (Opair) o;
				return this.op == e.op && this.id1 == e.id1 && this.id2 == e.id2;
		}
		return false;
	}
	@Override
	public int hashCode() {
		int hc;
		hc = op.hashCode() + id1 * 37 + id2 * 37 * 37;
		return hc;
	}
}
