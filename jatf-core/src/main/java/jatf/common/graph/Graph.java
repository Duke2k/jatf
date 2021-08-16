/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Graph<T> {

  private static final int VISIT_COLOR_WHITE = 1;
  private static final int VISIT_COLOR_GREY = 2;
  private static final int VISIT_COLOR_BLACK = 3;

  private final List<Vertex<T>> verticies;
  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final List<Edge<T>> edges;

  public Graph() {
    verticies = new ArrayList<>();
    edges = new ArrayList<>();
  }

  public void addVertex(Vertex<T> v) {
    if (!verticies.contains(v)) {
      verticies.add(v);
    }
  }

  public int size() {
    return verticies.size();
  }

  public void setRootVertex(Vertex<T> root) {
    if (!verticies.contains(root))
      this.addVertex(root);
  }

  private Vertex<T> getVertex(int n) {
    return verticies.get(n);
  }

  public void addEdge(Vertex<T> from, Vertex<T> to, int cost) throws IllegalArgumentException {
    if (!verticies.contains(from)) {
      throw new IllegalArgumentException("from is not in graph");
    }
    if (!verticies.contains(to)) {
      throw new IllegalArgumentException("to is not in graph");
    }
    Edge<T> e = new Edge<>(from, to, cost);
    if (from.findEdge(to) == null) {
      from.addEdge(e);
      to.addEdge(e);
      edges.add(e);
    }
  }

  @SuppressWarnings("unused")
  public void depthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
    VisitorEX<T, RuntimeException> wrapper = (g, v1) -> {
      if (visitor != null)
        visitor.visit(g, v1);
    };
    this.depthFirstSearch(v, wrapper);
  }

  private <E extends Exception> void depthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E {
    if (visitor != null)
      visitor.visit(this, v);
    v.visit();
    for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
      Edge<T> e = v.getOutgoingEdge(i);
      if (e.getTo().notVisited()) {
        depthFirstSearch(e.getTo(), visitor);
      }
    }
  }

  @SuppressWarnings("unused")
  public void breadthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
    VisitorEX<T, RuntimeException> wrapper = (g, v1) -> {
      if (visitor != null)
        visitor.visit(g, v1);
    };
    this.breadthFirstSearch(v, wrapper);
  }

  private <E extends Exception> void breadthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor)
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
        if (to.notVisited()) {
          q.add(to);
          if (visitor != null)
            visitor.visit(this, to);
          to.visit();
        }
      }
    }
  }

  @SuppressWarnings("unused")
  void dfsSpanningTree(Vertex<T> v, DFSVisitor<T> visitor) {
    v.visit();
    if (visitor != null)
      visitor.visit(this, v);

    for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
      Edge<T> e = v.getOutgoingEdge(i);
      if (e.getTo().notVisited()) {
        if (visitor != null)
          visitor.visit(this, v, e);
        e.mark();
        dfsSpanningTree(e.getTo(), visitor);
      }
    }
  }

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
