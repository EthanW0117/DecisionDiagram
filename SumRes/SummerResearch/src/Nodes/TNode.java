package Nodes;

public class TNode extends Node {
	private double value;
	public TNode(int ID, double value) {
		super(ID);
		this.value = value;
		// TODO Auto-generated constructor stub
	}
	public double getValue() {
		return this.value;
	}
	public String toString() {
		return this.getId() + ": " + this.value;
	}

}
