package jatf.common.graph;

/**
 * A graph visitor interface.
 *
 * @param <T>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
interface Visitor<T> {
	/**
	 * Called by the graph traversal methods when a vertex is first visited.
	 *
	 * @param g -
	 *          the graph
	 * @param v -
	 *          the vertex being visited.
	 */
	public void visit(Graph<T> g, Vertex<T> v);
}
