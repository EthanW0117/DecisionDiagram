package algDecDia;

import org.jgrapht.graph.DefaultEdge;

class RelationshipEdge extends DefaultEdge
{
private String label;

/**
 * Constructs a relationship edge
 *
 * @param label the label of the new edge.
 * 
 */
public RelationshipEdge(String label)
{
    this.label = label;
}

/**
 * Gets the label associated with this edge.
 *
 * @return edge label
 */
public String getLabel()
{
    return label;
}
@Override 
public boolean equals(Object o) {
	if (o instanceof RelationshipEdge) {
		RelationshipEdge e = (RelationshipEdge) o;
		return this.label.equals(e.getLabel());
	}
	return false;
}

@Override
public String toString()
{
    return "(" + getSource() + " : " + getTarget() + " : " + label + ")";
}
}