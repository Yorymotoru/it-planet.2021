package task4;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    private final List<Node> neighbors = new ArrayList<>();
    private final int h;
    private final int w;

    private double f = Double.MAX_VALUE;

    public Node(int h, int w) {
        this.h = h;
        this.w = w;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    public void setF(double f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "n: " + neighbors.size();
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.f, o.f);
    }
}
