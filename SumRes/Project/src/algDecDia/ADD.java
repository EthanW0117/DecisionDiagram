package algDecDia;
import node.*;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgrapht.*;
import org.jgrapht.alg.*;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.*;

import io.*;
//import guru.nidi.graphviz.*;
//import guru.nidi.graphviz.engine.Format;
//import guru.nidi.graphviz.engine.Graphviz;
//import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
//import guru.nidi.graphviz.engine.GraphvizJdkEngine;
//import guru.nidi.graphviz.model.MutableGraph;
//import guru.nidi.graphviz.service.DefaultExecutor;
//
//import static guru.nidi.graphviz.model.Factory.*;

public class ADD {
	
	public HashMap<Double,TNode> _terminal;
	public HashMap<Triple,INode> _internal;
	public HashMap<Integer,Node> _nodeMap;
	public HashMap<String, Integer> _varOrder;
	public HashMap<Integer,Integer> _reduceCache;
	public HashMap<String,Integer>	_hmMin;
	public HashMap<String, Integer> _hmMax;
	public HashMap<Opair, Integer> _applyCache;
	public HashMap<Integer,Integer> _reduceCacheThreshold;
	public int idCounter;
	public int varCounter;
	public ADD() {
		
		_terminal = new HashMap<Double,TNode>();
		_internal = new HashMap<Triple,INode>();
		_nodeMap = new HashMap<Integer,Node>();
		_varOrder = new HashMap<String,Integer>();
		_reduceCache = new HashMap<Integer,Integer>();
//		minMap = new HashMap<Integer,Double>();
		_hmMax = new HashMap<String, Integer>();
		_applyCache = new HashMap<Opair, Integer>();
		_reduceCacheThreshold = new HashMap<Integer, Integer>();
		idCounter = 0;
		varCounter = 0;
	}
	
	
	public int getTNode(double value) {
		
		int id;
		if(!_terminal.containsKey(value)) {
			id = idCounter;
			TNode newTNode = new TNode(idCounter, value);
			_terminal.put(value, newTNode);
			_nodeMap.put(id, newTNode);
//			_reduceCache.put(id, id);
			//maxMap.put(id,value);
			//minMap.put(id,value);
			idCounter++;
		} else {
			id = _terminal.get(value).getId();
//			_reduceCache.put(id, id);
		}
		return id;
	}
	
	public int createINode(String var, int lowId, int highId) {
		int id = idCounter;
		
		Triple identifier = new Triple(var, lowId, highId);
		INode newINode = new INode(id, var, lowId, highId);
		_internal.put(identifier, newINode);
		_nodeMap.put(id, newINode);
		if(!_varOrder.containsKey(var)) {
			_varOrder.put(var, varCounter);
			varCounter++;
		}
		idCounter++;
		return id;
		
	}

	public int getINode(String var, int lowId, int highId) {
		
		if(lowId == highId) {
			return lowId;
		}
		Triple identifier = new Triple(var, lowId, highId);
		if (!_internal.containsKey(identifier)) {
			return createINode(var, lowId, highId);
		}else {
			return _internal.get(identifier).getId();
		}
	}
	
	public int reduce(int id){
		
		
		if(!_reduceCache.containsKey(id)) {

			Node n = (Node) _nodeMap.get(id);
			if (n instanceof TNode) {
				//TNode t = (TNode)n;
				// This assume n is already canonical
				_reduceCache.put(id, id);
				return id;
			} else {
				INode i = (INode) n;
				
				int highChild = reduce(i.getHighChild());
				int lowChild = reduce(i.getLowChild());
				int reducedId = getINode(i.getVar(), lowChild, highChild);
				// set [min,max] of reduceId here
				setMinMax(reducedId);
				_reduceCache.put(id,reducedId);
				return reducedId;
			}
		} else {
			return _reduceCache.get(id);
		}
	}

	public int reduceThreshold(int id, String op, double val){ // if < val then -infty else 0
		_reduceCacheThreshold.clear();
		return reduceThresholdInt(id, op, val);
		
	}
	
	public int reduceThresholdInt(int id,String op, double val){ // if < val then -infty else 0
		// Rename the reduce cache here
		if(!_reduceCacheThreshold.containsKey(id)) {
			Node n = (Node) _nodeMap.get(id);
			if (n instanceof TNode) {
				TNode t = (TNode)n;
				int new_t = -1;
				if (!CheckCons(t,op,val))
					new_t = getTNode(Double.NEGATIVE_INFINITY);
				else
					new_t = getTNode(0);
				_reduceCacheThreshold.put(id, new_t);
				return new_t;
			} else {
				INode i = (INode) n;
				int highChild = reduceThresholdInt(i.getHighChild(),op, val);
				int lowChild = reduceThresholdInt(i.getLowChild(),op, val);
				int reducedId = getINode(i.getVar(), lowChild, highChild);
				// set [min,max] of reduceId here
				setMinMax(reducedId);
				_reduceCacheThreshold.put(id,reducedId);
				return reducedId;
			}
		} else {
			return _reduceCacheThreshold.get(id);
		}
	}

	public boolean CheckCons(Node cons, String op, double val) {
		boolean pass = false;
		
		switch(op) {
			case ">":{
				if (cons instanceof TNode) {
					if (((TNode) cons).getValue() > val){
						pass = true;
					}else {
						pass = false;
					}
					break;
				}
			}
			case ">=":{
				if (cons instanceof TNode) {
					if (((TNode) cons).getValue() >= val){
						pass = true;
					}else {
						pass = false;
					}
					break;
				}
			}
			case "<":{
				if (cons instanceof TNode) {
					if (((TNode) cons).getValue() < val){
						pass = true;
					}else {
						pass = false;
					}
					break;
				}
			}
			case "<=":{
				if (cons instanceof TNode) {
					if (((TNode) cons).getValue() <= val){
						pass = true;
					}else {
						pass = false;
					}
					break;
				}
			}
			case "=":{
				if (cons instanceof TNode) {
					if (((TNode) cons).getValue() == val){
						pass = true;
					}else {
						pass = false;
					}
					break;
				}
			}
		}
		
		return pass;
	}
	
	public void setMinMax(int id) {
		Node n = _nodeMap.get(id);
		double lowMin, lowMax, highMin, highMax;
		if(n instanceof INode) {
			INode e = (INode) n;
			Node low = _nodeMap.get(e.lowChild);
			Node high = _nodeMap.get(e.highChild);
			if(low instanceof INode) {
				lowMin = ((INode) low)._min;
				lowMax = ((INode) low)._max;
			}else {
				lowMin = lowMax = ((TNode) low).value;
			}
			if(high instanceof INode) {
				highMin = ((INode) high)._min;
				highMax = ((INode) high)._max;
			}else {
				highMin = highMax = ((TNode) high).value;
			}
			e._max = Math.max(lowMax, highMax);
			e._min = Math.min(lowMin, highMin);
		}
	}
	
	public HashMap<String,Integer> buildMaxMapInt(int id) {
		Node n = _nodeMap.get(id);
		if(n instanceof INode) {
			INode e = (INode) n;
			int low = e.lowChild;
			int high = e.highChild;
			if(e._max == _nodeMap.get(low)._max) {
				_hmMax.put(e.getVar(), 0);
				buildMaxMapInt(low);
			}else if(e._max == _nodeMap.get(high)._max) {
				_hmMax.put(e.getVar(), 1);
				buildMaxMapInt(high);
			}
		}else {
		}
		return _hmMax;
	}
	
	public void buildMaxMap(int id) {
		_hmMax.clear();
		buildMaxMapInt(id);
	}
	
	public HashMap<String,Integer> buildMinMapInt(int id) {
		Node n = _nodeMap.get(id);
		if(n instanceof INode) {
			INode e = (INode) n;
			int low = e.lowChild;
			int high = e.highChild;
			if(e._min == _nodeMap.get(low)._min) {
				_hmMin.put(e.getVar(), 0);
				buildMinMapInt(low);
			}else if(e._min == _nodeMap.get(high)._min) {
				_hmMin.put(e.getVar(), 1);
				buildMinMapInt(high);
			}
		}else {
		}
		return _hmMin;
	}
	
	public void buildMinMap(int id) {
		_hmMin.clear();
		buildMaxMapInt(id);
		System.out.println(_hmMin);
	}
	
	public int computeResult(Node node1, Node node2, String operation) {

	
		int result = -1;
		
		switch (operation) {
		
			case "Plus":{
				if(node1 instanceof TNode && node2 instanceof TNode) {
					double val = ((TNode) node1).getValue() + ((TNode) node2).getValue();
					result = getTNode(val);
				}
				break;
			}
			case "Minus":{
				if(node1 instanceof TNode && node2 instanceof TNode) {
					double val = ((TNode) node1).getValue() - ((TNode) node2).getValue();
					result = getTNode(val);
				}
				break;
			}
			case "Mult":{
				if(node1 instanceof TNode && node2 instanceof TNode) {
					double val = ((TNode) node1).getValue() * ((TNode) node2).getValue();
					result = getTNode(val);
				}
				break;
			}
			case "Divid":{
				if(node1 instanceof TNode && node2 instanceof TNode) {
					double val = ((TNode) node1).getValue() / ((TNode) node2).getValue();
					result = getTNode(val);
				}
				break;
			}
			case "Max":{
				if(node1._max < node2._min) {
					result = node2.getId();	
				}else if(node2._max < node1._min){
					result = node1.getId();
				}
				break;
			}
		
//			case "Min":{
//				if(maxMap.get(node1.getId()) < minMap.get(node2.getId())) {
//					result = node1.getId();	
//				}else if(maxMap.get(node2.getId()) < minMap.get(node1.getId())){
//					result = node2.getId();
//				}
//				break;
//			}
//			
//			case "Greater ":{
//				if()
//				
//			}
		
//			case "
//		}
		}
		return result;
	}
	
	public int apply(int id1, int id2, String op) {
		Opair identifier = new Opair(id1, id2, op);
		
		Node n1 = _nodeMap.get(id1);
		Node n2 = _nodeMap.get(id2);
		int result = computeResult(n1,n2,op);
//		System.out.println(result);
		if (result >= 0) {
			return result;
		}
		
		if (_applyCache.containsKey(identifier)) {
			return _applyCache.get(identifier);
		} else {
			int n1low, n1high, n2low, n2high;
			String var;
			if(n1 instanceof INode) { 
				if(n2 instanceof INode) {
					if(_varOrder.get(((INode) _nodeMap.get(id1)).getVar()) > _varOrder.get(((INode) _nodeMap.get(id2)).getVar())) {
						var = ((INode) n1).getVar();
					}else {
						var = ((INode) n2).getVar();
					}
				}else {
					var = ((INode) n1).getVar();
				}
			}else {
				var = ((INode) n2).getVar();
			}
			
			if(n1 instanceof INode && var == ((INode) n1).getVar()) {
				n1low = ((INode) n1).getLowChild();
				n1high = ((INode) n1).getHighChild();
			} else {
				n1low = n1high = n1.getId();
			}
			
			if(n2 instanceof INode && var == ((INode) n2).getVar()) {
				n2low = ((INode) n2).getLowChild();
				n2high = ((INode) n2).getHighChild();
			} else {
				n2low = n2high = n2.getId();
			}
			int low = apply(n1low,n2low,op);
			int high = apply(n1high,n2high,op);
			result = getINode(var, low, high);
			_applyCache.put(new Opair(id1,id2,op), result);
			return result;
		}
		
	}
		
	public Graph getGraph(int id) {
		Graph<Node, RelationshipEdge> DirectedGraph =
				new DefaultDirectedGraph<Node, RelationshipEdge>(RelationshipEdge.class);
		convert2Graph(id, DirectedGraph);
		return DirectedGraph;
	}
	
	public void convert2Graph(int id, Graph g) {
		Node n = (Node) _nodeMap.get(id);
		if(n instanceof TNode) {
			
		} else {
			INode i = (INode) n;
			g.addVertex(i);
			g.addVertex(_nodeMap.get(i.getLowChild()));
			g.addVertex(_nodeMap.get(i.getHighChild()));
			g.addEdge(i,_nodeMap.get(i.getLowChild()),new RelationshipEdge("low"));
			g.addEdge(i,_nodeMap.get(i.getHighChild()),new RelationshipEdge("high"));
			convert2Graph(i.getLowChild(),g);
			convert2Graph(i.getHighChild(),g);
		}
		
	}
	
	
	public void toFile(Graph g, String directory ,String filename) throws ExportException, IOException {
		
		ComponentNameProvider<Node> vertexIdProvider = new ComponentNameProvider<Node>() {
			public String getName(Node node) {
				
				return "id"+ node.toString().replace(":", "_").replace(".", "_").replace("-", "m").replace("(","_").replace(")", "_").replace(",", "_");
					
			}
		};
		ComponentNameProvider<Node> labelProvider = new ComponentNameProvider<Node>() {
			public String getName(Node node) {
				return node.toString();
			}
		};
		
		ComponentNameProvider<RelationshipEdge> edgeProvider = new ComponentNameProvider<RelationshipEdge>() {

			@Override
			public String getName(RelationshipEdge edge) {
				// TODO Auto-generated method stub
				return edge.getLabel();
			}
		};
		GraphExporter<Node, RelationshipEdge> exporter = new DOTExporter<>(vertexIdProvider, labelProvider, edgeProvider);
		exporter.exportGraph(g,new FileWriter(directory + filename));
	}
	
	
	public static void main(String[] args) throws ExportException, IOException {
			
			ADD algDD = new ADD();
			//test 1
//			algDD.getTNode(1.0);//1
//			algDD.getTNode(7.0);//2
//			algDD.getTNode(3.5);//3
//			algDD.getTNode(-1.5);//4
//			algDD.createINode("A", 2, 3);//5
//			algDD.createINode("B", 4, 0);//6
//			algDD.createINode("D",1,1);//7
//			algDD.createINode("C", 6, 5);//8
//			algDD.createINode("E", 7, 7);
			
			//test 2
//			algDD.getTNode(1.0);
//			algDD.getTNode(2.0);
//			algDD.getTNode(3.0);
//			algDD.getTNode(4.0);
//			algDD.getTNode(5.0);
//			algDD.createINode("A", 0, 1);
//			algDD.createINode("A", 2, 3);
//			algDD.createINode("B", 3, 4);
//			algDD.createINode("C", 5, 6);
//			algDD.createINode("C", 6, 7);
//			algDD.createINode("D", 8, 9);
//			algDD.createINode("E", 10, 9);
			
			//test 3
//			algDD.getTNode(1.0);
//			algDD.getTNode(2.0);
//			algDD.createINode("A", 0, 1);
//			algDD.createINode("A", 0, 1);
//			algDD.createINode("B", 2, 3);
//			algDD.reduce(4);
			
			
			//test 4
//			algDD.getTNode(0);
//			algDD.getTNode(1);
//			algDD.getTNode(2);
//			algDD.createINode("C", 0, 1);
//			algDD.createINode("C", 0, 2);
//			algDD.createINode("B", 0, 3);
//			algDD.createINode("A", 5, 3);
//			algDD.createINode("A", 4, 0);
			
			//test 5
//			int zero = algDD.getTNode(0);
//			int one  = algDD.getTNode(1);
//			int cur = one;
//			for (int i = 0; i < 10; i++)
//				cur = algDD.apply(cur, algDD.createINode("X"+i, zero, one), "Plus");
//			algDD.reduce(cur);
//			int geez = algDD.apply(cur, algDD.getTNode(5.0), "Plus");
//			int lol = algDD.reduceThreshold(cur, ">", 0);
//			System.out.println(algDD._reduceCacheThreshold);
//			algDD.reduce(lol);
			
			
			
			//System.out.println(algDD._hmMax);
			//System.out.println(algDD._nodeMap.get(lol));
//			if(algDD._nodeMap.get(lol) instanceof INode) {
////				INode e = (INode)algDD._nodeMap.get();
//				System.out.println(e.toString() + e.highChild + " and " + e.lowChild);
//			}
			//HashMap<String, Integer> max = algDD.buildMaxMap(cur, algDD._hmMax);
			String targetDirectory = "testresults/graph/";
			new File(targetDirectory).mkdirs();
//			Graph g = algDD.getGraph(algDD.reduce(geez));
//			algDD.toFile(g, targetDirectory, "HAHA.dot");
//			Graph g2 = algDD.getGraph(algDD.reduce(lol));
//			algDD.toFile(g2, targetDirectory, "HAHA2.dot");
			//System.out.println(writer1.toString());
			//System.out.println(algDD._terminal.keySet());
//			System.out.println(algDD._nodeMap);
//			System.out.println(algDD._internal.keySet());
			
//			System.out.println(algDD.apply(3, 4, "Plus"));
//			System.out.println(algDD._applyCache);
//			Graph g3 = algDD.getGraph(5);
//			algDD.toFile(g3, targetDirectory, "HAHA3.dot");
//			algDD.setMax(5);
//			algDD.setMin(5);
//			System.out.println(algDD.maxMap);
//			System.out.println(algDD.minMap);
//			algDD.reduce(8);
//			System.out.println(algDD._nodeMap);
//			Graph g2 = algDD.getGraph(9);
//			algDD.toFile(g2, targetDirectory, "HAHAafter.dot");
			
			
			int result = BMParser.BMParser("files/metro1.txt", algDD);
			Graph g = algDD.getGraph(algDD.reduce(result));
			algDD.toFile(g, targetDirectory, "Trial1.dot");
			algDD.buildMinMap(result);

		}
}
