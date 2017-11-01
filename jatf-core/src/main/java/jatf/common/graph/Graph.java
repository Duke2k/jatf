package jatf.common.graph;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A directed graph data structure.
 *
 * @param <T>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class Graph<T> {
	/**
	 * Color used to mark unvisited nodes
	 */
	public static final int VISIT_COLOR_WHITE = 1;

	/**
	 * Color used to mark nodes as they are first visited in DFS order
	 */
	public static final int VISIT_COLOR_GREY = 2;

	/**
	 * Color used to mark nodes after descendants are completely visited
	 */
	public static final int VISIT_COLOR_BLACK = 3;

	/**
	 * Vector<Vertex> of graph verticies
	 */
	private List<Vertex<T>> verticies;

	/**
	 * Vector<Edge> of edges in the graph
	 */
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private List<Edge<T>> edges;

	/**
	 * Construct a new graph without any vertices or edges
	 */
	public Graph() {
		verticies = new ArrayList<>();
		edges = new ArrayList<>();
	}

	/**
	 * Add a vertex to the graph
	 *
	 * @param v the Vertex to add
	 * @return true if the vertex was added, false if it was already in the graph.
	 */
	public boolean addVertex(Vertex<T> v) {
		boolean added;
		if (verticies.contains(v)) return false;
		added = verticies.add(v);
		return added;
	}

	/**
	 * Get the vertex count.
	 *
	 * @return the number of verticies in the graph.
	 */
	public int size() {
		return verticies.size();
	}

	/**
	 * Set a root vertex. If root does no exist in the graph it is added.
	 *
	 * @param root -
	 *             the vertex to set as the root and optionally add if it does not
	 *             exist in the graph.
	 */
	public void setRootVertex(Vertex<T> root) {
				/*
			The vertex identified as the root of the graph
     */
		Vertex<T> rootVertex = root;
		if (!verticies.contains(root))
			this.addVertex(root);
	}

	/**
	 * Get the given Vertex.
	 *
	 * @param n the index [0, size()-1] of the Vertex to access
	 * @return the nth Vertex
	 */
	public Vertex<T> getVertex(int n) {
		return verticies.get(n);
	}

	/**
	 * Insert a directed, weighted Edge<T> into the graph.
	 *
	 * @param from -
	 *             the Edge<T> starting vertex
	 * @param to   -
	 *             the Edge<T> ending vertex
	 * @param cost -
	 *             the Edge<T> weight/cost
	 * @return true if the Edge<T> was added, false if from already has this Edge<T>
	 * @throws IllegalArgumentException if from/to are not verticies in the graph
	 */
	public boolean addEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException {
		if (!verticies.contains(from))
			throw new IllegalArgumentException("from is not in graph");
		if (!verticies.contains(to))
			throw new IllegalArgumentException("to is not in graph");

		Edge<T> e = new Edge<>(from, to, cost);
		if (from.findEdge(to) != null)
			return false;
		else {
			from.addEdge(e);
			to.addEdge(e);
			edges.add(e);
			return true;
		}
	}

	/**
	 * Perform a depth first serach using recursion.
	 *
	 * @param v       -
	 *                the Vertex to start the search from
	 * @param visitor -
	 *                the vistor to inform prior to
	 * @see Visitor#visit(Graph, Vertex)
	 */
	public void depthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
		VisitorEX<T, RuntimeException> wrapper = (g, v1) -> {
			if (visitor != null)
				visitor.visit(g, v1);
		};
		this.depthFirstSearch(v, wrapper);
	}

	/**
	 * Perform a depth first serach using recursion. The search may be cut short
	 * if the visitor throws an exception.
	 *
	 * @param v       -
	 *                the Vertex to start the search from
	 * @param visitor -
	 *                the vistor to inform prior to
	 * @throws E if visitor.visit throws an exception
	 * @see Visitor#visit(Graph, Vertex)
	 */
	public <E extends Exception> void depthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E {
		if (visitor != null)
			visitor.visit(this, v);
		v.visit();
		for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
			Edge<T> e = v.getOutgoingEdge(i);
			if (!e.getTo().visited()) {
				depthFirstSearch(e.getTo(), visitor);
			}
		}
	}

	/**
	 * Perform a breadth first search of this graph, starting at v.
	 *
	 * @param v       -
	 *                the search starting point
	 * @param visitor -
	 *                the vistor whose vist method is called prior to visting a vertex.
	 */
	public void breadthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
		VisitorEX<T, RuntimeException> wrapper = (g, v1) -> {
			if (visitor != null)
				visitor.visit(g, v1);
		};
		this.breadthFirstSearch(v, wrapper);
	}

	/**
	 * Perform a breadth first search of this graph, starting at v. The vist may
	 * be cut short if visitor throws an exception during a vist callback.
	 *
	 * @param v       -
	 *                the search starting point
	 * @param visitor -
	 *                the vistor whose vist method is called prior to visting a vertex.
	 * @throws E if vistor.visit throws an exception
	 */
	public <E extends Exception> void breadthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor)
			throws E {
		LinkedList<Vertex<T>> q = new LinkedList<>();

		q.add(v);
		if (visitor != null)
			visitor.visit(this, v);
		v.visit();
		while (!q.isEmpty()) {
			v = q.removeFirst();
			for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
				Edge<T> e = v.getOutgoingEdge(i);
				Vertex<T> to = e.getTo();
				if (!to.visited()) {
					q.add(to);
					if (visitor != null)
						visitor.visit(this, to);
					to.visit();
				}
			}
		}
	}

	/**
	 * Find the spanning tree using a DFS starting from v.
	 *
	 * @param v       -
	 *                the vertex to start the search from
	 * @param visitor -
	 *                visitor invoked after each vertex is visited and an edge is added
	 *                to the tree.
	 */
	public void dfsSpanningTree(Vertex<T> v, DFSVisitor<T> visitor) {
		v.visit();
		if (visitor != null)
			visitor.visit(this, v);

		for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
			Edge<T> e = v.getOutgoingEdge(i);
			if (!e.getTo().visited()) {
				if (visitor != null)
					visitor.visit(this, v, e);
				e.mark();
				dfsSpanningTree(e.getTo(), visitor);
			}
		}
	}

	/**
	 * Search the verticies for one with name.
	 *
	 * @param name -
	 *             the vertex name
	 * @return the first vertex with a matching name, null if no matches are found
	 */
	public Vertex<T> findVertexByName(String name) {
		Vertex<T> match = null;
		for (Vertex<T> v : verticies) {
			if (name.equals(v.getName())) {
				match = v;
				break;
			}
		}
		return match;
	}

	/**
	 * Search the graph for cycles. In order to detect cycles, we use a modified
	 * depth first search called a colored DFS. All nodes are initially marked
	 * white. When a node is encountered, it is marked grey, and when its
	 * descendants are completely visited, it is marked black. If a grey node is
	 * ever encountered, then there is a cycle.
	 *
	 * @return the edges that form cycles in the graph. The array will be empty if
	 * there are no cycles.
	 */
	public Edge<T>[] findCycles() {
		ArrayList<Edge<T>> cycleEdges = new ArrayList<>();
		// Mark all verticies as white
		for (int n = 0; n < verticies.size(); n++) {
			Vertex<T> v = getVertex(n);
			v.setMarkState(VISIT_COLOR_WHITE);
		}
		for (int n = 0; n < verticies.size(); n++) {
			Vertex<T> v = getVertex(n);
			visit(v, cycleEdges);
		}

		Edge<T>[] cycles = new Edge[cycleEdges.size()];
		cycleEdges.toArray(cycles);
		return cycles;
	}

	private void visit(Vertex<T> v, ArrayList<Edge<T>> cycleEdges) {
		v.setMarkState(VISIT_COLOR_GREY);
		int count = v.getOutgoingEdgeCount();
		for (int n = 0; n < count; n++) {
			Edge<T> e = v.getOutgoingEdge(n);
			Vertex<T> u = e.getTo();
			if (u.getMarkState() == VISIT_COLOR_GREY) {
				// A cycle Edge<T>
				cycleEdges.add(e);
			} else if (u.getMarkState() == VISIT_COLOR_WHITE) {
				visit(u, cycleEdges);
			}
		}
		v.setMarkState(VISIT_COLOR_BLACK);
	}

	public String toString() {
		StringBuilder tmp = new StringBuilder("Graph[");
		verticies.forEach(tmp::append);
		tmp.append(']');
		return tmp.toString();
	}
}
