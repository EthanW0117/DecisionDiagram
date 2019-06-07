package Nodes;

public abstract class Node {
	
	private int id;
	
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
