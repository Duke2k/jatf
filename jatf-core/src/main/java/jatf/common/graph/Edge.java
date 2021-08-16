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

public class Edge<T> {

  private final Vertex<T> from;
  private final Vertex<T> to;
  private final int cost;
  private boolean mark;

  @SuppressWarnings("UnusedDeclaration")
  public Edge(Vertex<T> from, Vertex<T> to) {
    this(from, to, 0);
  }

  Edge(Vertex<T> from, Vertex<T> to, int cost) {
    this.from = from;
    this.to = to;
    this.cost = cost;
    mark = false;
  }

  public Vertex<T> getTo() {
    return to;
  }

  Vertex<T> getFrom() {
    return from;
  }

  int getCost() {
    return cost;
  }

  void mark() {
    mark = true;
  }

  @SuppressWarnings("UnusedDeclaration")
  public boolean isMarked() {
    return mark;
  }

  public String toString() {
    @SuppressWarnings("StringBufferReplaceableByString")
    StringBuilder tmp = new StringBuilder("Edge[from: ");
    tmp.append(from.getName());
    tmp.append(",to: ");
    tmp.append(to.getName());
    tmp.append(", cost: ");
    tmp.append(cost);
    tmp.append("]");
    return tmp.toString();
  }
}
