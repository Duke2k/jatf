package jatf.common.graph;

/**
 * A spanning tree visitor callback interface
 *
 * @param <T>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 * @see jatf.common.graph.Graph#dfsSpanningTree(jatf.common.graph.Vertex, jatf.common.graph.DFSVisitor)
 */
interface DFSVisitor<T> {
    /**
     * Called by the graph traversal methods when a vertex is first visited.
     *
     * @param g -
     *          the graph
     * @param v -
     *          the vertex being visited.
     */
    public void visit(Graph<T> g, Vertex<T> v);

    /**
     * Used dfsSpanningTree to notify the visitor of each outgoing edge to an
     * unvisited vertex.
     *
     * @param g -
     *          the graph
     * @param v -
     *          the vertex being visited
     * @param e -
     *          the outgoing edge from v
     */
    public void visit(Graph<T> g, Vertex<T> v, Edge<T> e);
}
