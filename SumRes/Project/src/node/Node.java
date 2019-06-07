package node;

public abstract class Node {
	
	public int id;
	public double _min; // maintain during Reduce operation, display when you show graph
	public double _max;
	
	public Node(int ID) {
		this.id = ID;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Node) {
			Node e = (Node) o;
			return this.id == e.id;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
}
