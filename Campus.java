import java.util.*;
//profa me equivoque muchisimas veces a la hora de subirlo, segun lo hice por partes pero resulto que lo hice todo mal haahahaha, pero al final si pude subirlo :c
class CampusMap {
    private Map<String, Map<String, Integer>> graph;

    public CampusMap() {
        graph = new HashMap<>();
    }

    public void addLocation(String location) {
        graph.putIfAbsent(location, new HashMap<>());
    }

    public void addPath(String from, String to, int distance) {
        addLocation(from);
        addLocation(to);
        graph.get(from).put(to, distance);
        graph.get(to).put(from, distance); 
    }

    public List<String> bfs(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<String> order = new ArrayList<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            order.add(current);

            for (String neighbor : graph.get(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return order;
    }

    public List<String> dfs(String start) {
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        dfsHelper(start, visited, order);
        return order;
    }

    private void dfsHelper(String current, Set<String> visited, List<String> order) {
        visited.add(current);
        order.add(current);

        for (String neighbor : graph.get(current).keySet()) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited, order);
            }
        }
    }

    public List<String> dijkstra(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (String location : graph.keySet()) {
            distances.put(location, Integer.MAX_VALUE);
            previous.put(location, null);
        }

        distances.put(start, 0);
        pq.add(new AbstractMap.SimpleEntry<>(start, 0));

        while (!pq.isEmpty()) {
            String current = pq.poll().getKey();

            if (current.equals(end)) {
                break;
            }

            for (Map.Entry<String, Integer> neighborEntry : graph.get(current).entrySet()) {
                String neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                int newDist = distances.get(current) + weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    pq.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
}

class AVLNode {
    int key;
    String value;
    int height;
    AVLNode left, right;

    public AVLNode(int key, String value) {
        this.key = key;
        this.value = value;
        this.height = 1;
    }
}

class AVLTree {
    private AVLNode root;

    public void insert(int key, String value) {
        root = insertRec(root, key, value);
    }

    private AVLNode insertRec(AVLNode node, int key, String value) {
        if (node == null) {
            return new AVLNode(key, value);
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key, value);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, value);
        } else {
            return node;
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }
}

class Incident {
    int id;
    String title;
    int priority;
    long timestamp;

    public Incident(int id, String title, int priority, long timestamp) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.timestamp = timestamp;
    }
}

class IncidentQueue {
    private PriorityQueue<Incident> heap;

    public IncidentQueue() {
        heap = new PriorityQueue<>(Comparator.comparingInt(i -> i.priority));
    }

    public void push(Incident incident) {
        heap.add(incident);
    }

    public Incident pop() {
        return heap.poll();
    }

    public Incident peek() {
        return heap.peek();
    }

    public void changePriority(Incident incident, int newPriority) {
        heap.remove(incident);
