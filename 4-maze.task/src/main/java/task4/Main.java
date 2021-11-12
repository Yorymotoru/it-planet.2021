package task4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;

public class Main {
    private static final Color WALL = new Color(0, 0, 0);
    private static final Color FLOOR = new Color(255, 255, 255);
    private static final Color START = new Color(0, 255, 0);
    private static final Color FINISH = new Color(0, 0, 255);
    private static final Color PATH = new Color(255, 0, 0);

    private static void drawPath(String input, String output) {
        try {
            File inputIMG = new File(input);
            BufferedImage image = ImageIO.read(inputIMG);

            int height = image.getHeight();
            int width = image.getWidth();

            Node[][] nodes = new Node[height][width];
            Node startNode = null;
            Node finishNode = null;

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    if (image.getRGB(w, h) != WALL.getRGB()) {
                        nodes[h][w] = new Node(h, w);

                        if (image.getRGB(w, h) == START.getRGB()) {
                            startNode = nodes[h][w];
                        } else if (image.getRGB(w, h) == FINISH.getRGB()) {
                            finishNode = nodes[h][w];
                        }
                    }
                }
            }

            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    if (image.getRGB(w, h) != WALL.getRGB()) {
                        Node node = nodes[h][w];
                        if (h > 0 && image.getRGB(w, h - 1) != WALL.getRGB()) {
                            node.addNeighbor(nodes[h - 1][w]);
                        }
                        if (h < height - 1 && image.getRGB(w, h + 1) != WALL.getRGB()) {
                            node.addNeighbor(nodes[h + 1][w]);
                        }
                        if (w > 0 && image.getRGB(w - 1, h) != WALL.getRGB()) {
                            node.addNeighbor(nodes[h][w - 1]);
                        }
                        if (w < width - 1 && image.getRGB(w + 1, h) != WALL.getRGB()) {
                            node.addNeighbor(nodes[h][w + 1]);
                        }
                    }
                }
            }

            assert startNode != null;
            Map<Node, Node> cameFrom = aStar(startNode, finishNode);

            Node n = cameFrom.get(finishNode);
            while (n != startNode) {
                image.setRGB(n.getW(), n.getH(), PATH.getRGB());
                n = cameFrom.get(n);
            }

            File outputFile = new File(output);
            ImageIO.write(image, "png", outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Node, Node> aStar(Node startNode, Node finishNode) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Integer> costSoFar = new HashMap<>();

        startNode.setF(0);
        frontier.add(startNode);
        cameFrom.put(startNode, null);
        costSoFar.put(startNode, 0);

        while (frontier.size() > 0) {
            Node current = frontier.poll();

            if (current == finishNode) {
                break;
            }

            for (Node next : current.getNeighbors()) {
                Integer newCost = costSoFar.get(current) + 1;
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    double priority = newCost + heuristic(next, finishNode);
                    next.setF(priority);
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }
        return cameFrom;
    }

    private static Integer heuristic(Node u, Node v) {
        return abs(u.getH() - v.getH()) + abs(u.getW() - v.getW());
    }

    public static void main(String[] args) {
        // Input wasn't specified explicitly
        if (args.length == 0) {         // for input filename from stdin; output automatically
            Scanner in = new Scanner(System.in);
            System.out.print("Enter filename:\n> ");
            String filename = in.nextLine();
            String[] path = filename.split("/");
            drawPath(filename, "result_" + path[path.length - 1]);
        } else if (args.length == 1) {  // for input filename console argument; output automatically
            String filename = args[0];
            String[] path = filename.split("/");
            drawPath(filename, "result_" + path[path.length - 1]);
        } else if (args.length == 2) {  // for input filename console arguments for input and output
            String filename = args[0];
            String outFilename = args[1];
        }
    }

}
