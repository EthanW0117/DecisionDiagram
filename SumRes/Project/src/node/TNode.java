package node;

public class TNode extends Node {
	public double value;
	public TNode(int ID, double value) {
		super(ID);
		this.value = value;
		this._max = value;
		this._min = value;
		// TODO Auto-generated constructor stub
	}
	public double getValue() {
		return this.value;
	}
	public String toString() {
		return this.getId() + ":" + this.value;
	}

}
