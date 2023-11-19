import java.util.*;

public class Graph {
    public Map<Photo, List<Edge>> adjVertices = new HashMap<>();
    private final Set<Photo> usedPhotos = new HashSet<>();

    public Photo findNextPhoto(Photo currentPhoto, boolean requireVertical) {
        if (requireVertical && currentPhoto.isHorizontal()) {
            return null; // If the current photo is not vertical but vertical is required, return null
        }

        List<Edge> edges = getAdjVertices(currentPhoto);
        Photo bestPhoto = null;
        double maxWeight = -1;

        for (Edge edge : edges) {
            Photo candidatePhoto = edge.getDestination();
            if (requireVertical) {
                if (!usedPhotos.contains(candidatePhoto) && !candidatePhoto.isHorizontal()) {
                    if (edge.getWeight() > maxWeight) {
                        maxWeight = edge.getWeight();
                        bestPhoto = candidatePhoto;
                    }
                }
            } else {
                if (!usedPhotos.contains(candidatePhoto)) {
                    maxWeight = edge.getWeight();
                    bestPhoto = candidatePhoto;
                }
            }
        }

        if (bestPhoto != null) {
            usedPhotos.add(bestPhoto);
        }

        return bestPhoto;
    }

    public void usePhoto(Photo photo) {
        usedPhotos.add(photo);
    }

    void clearGraph() {
        usedPhotos.clear();
    }

    int size() {
        return this.usedPhotos.size();
    }

    void addVertex(Photo photo) {
        adjVertices.putIfAbsent(photo, new ArrayList<>());
    }

    public void updateEdge(Photo source, Photo destination, int weight) {
        List<Edge> sourceEdges = adjVertices.computeIfAbsent(source, k -> new ArrayList<>());
        List<Edge> destinationEdges = adjVertices.computeIfAbsent(destination, k -> new ArrayList<>());

        // Increment weights if the edge exists in either direction
        boolean sourceToDestEdgeExists = false;
        boolean destToSourceEdgeExists = false;

        for (Edge edge : sourceEdges) {
            if (edge.getDestination().equals(destination)) {
                edge.incrementWeight(weight);
                sourceToDestEdgeExists = true;
                break;
            }
        }

        for (Edge edge : destinationEdges) {
            if (edge.getDestination().equals(source)) {
                edge.incrementWeight(weight);
                destToSourceEdgeExists = true;
                break;
            }
        }

        // If the edge does not exist in any direction, create new edges
        if (!sourceToDestEdgeExists) {
            sourceEdges.add(new Edge(destination, weight));
        }
        if (!destToSourceEdgeExists) {
            destinationEdges.add(new Edge(source, weight));
        }
    }



    boolean hasEdge(Photo v1, Photo v2) {
        return adjVertices.getOrDefault(v1, Collections.emptyList()).contains(v2) ||
                adjVertices.getOrDefault(v2, Collections.emptyList()).contains(v1);
    }

    List<Edge> getAdjVertices(Photo label) {
        return adjVertices.get(label);
    }
}