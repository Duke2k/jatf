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
import java.util.List;

public class Vertex<T> {

  private List<Edge<T>> incomingEdges;
  private List<Edge<T>> outgoingEdges;
  private String name;
  private boolean mark;
  private int markState;
  private T data;

  public Vertex() {
    this(null, null);
  }

  public Vertex(String n) {
    this(n, null);
  }

  public Vertex(String n, T data) {
    incomingEdges = new ArrayList<>();
    outgoingEdges = new ArrayList<>();
    name = n;
    mark = false;
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public T getData() {
    return this.data;
  }

  void addEdge(Edge<T> e) {
    if (e.getFrom() == this)
      outgoingEdges.add(e);
    else if (e.getTo() == this)
      incomingEdges.add(e);
  }

  public boolean remove(Edge<T> e) {
    if (e.getFrom() == this)
      incomingEdges.remove(e);
    else if (e.getTo() == this)
      outgoingEdges.remove(e);
    else
      return false;
    return true;
  }

  public Edge<T> getIncomingEdge(int i) {
    return incomingEdges.get(i);
  }

  int getOutgoingEdgeCount() {
    return outgoingEdges.size();
  }

  Edge<T> getOutgoingEdge(int i) {
    return outgoingEdges.get(i);
  }

  Edge<T> findEdge(Vertex<T> dest) {
    for (Edge<T> e : outgoingEdges) {
      if (e.getTo() == dest)
        return e;
    }
    return null;
  }

  public int cost(Vertex<T> dest) {
    if (dest == this)
      return 0;

    Edge<T> e = findEdge(dest);
    int cost = Integer.MAX_VALUE;
    if (e != null)
      cost = e.getCost();
    return cost;
  }

  boolean notVisited() {
    return !mark;
  }

  private void mark() {
    mark = true;
  }

  int getMarkState() {
    return markState;
  }

  void setMarkState(int state) {
    markState = state;
  }

  public void visit() {
    mark();
  }

  public String toString() {
    StringBuilder tmp = new StringBuilder("Vertex(");
    tmp.append(name);
    tmp.append(", data=");
    tmp.append(data);
    tmp.append("), in:[");
    for (int i = 0; i < incomingEdges.size(); i++) {
      Edge<T> e = incomingEdges.get(i);
      if (i > 0) {
        tmp.append(',');
      }
      tmp.append('{');
      tmp.append(e.getFrom().name);
      tmp.append(',');
      tmp.append(e.getCost());
      tmp.append('}');
    }
    tmp.append("], out:[");
    for (int i = 0; i < outgoingEdges.size(); i++) {
      Edge<T> e = outgoingEdges.get(i);
      if (i > 0)
        tmp.append(',');
      tmp.append('{');
      tmp.append(e.getTo().name);
      tmp.append(',');
      tmp.append(e.getCost());
      tmp.append('}');
    }
    tmp.append(']');
    return tmp.toString();
  }
}
