import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Explorer {

    char[][] map;
    LinkedList<Node> keyLocations = new LinkedList<>();
    LinkedList<LinkedList<Node>> totalPaths = new LinkedList<>();


    public Explorer(){

    }

    public void scanWithUAV(char[][] map){
        this.map = map;

        for(int y = 0; y<map.length; y++){
            for(int x = 0; x <map[y].length; x++){
                char c = map[y][x];
                if(c != DungeonMap.OPEN && c != DungeonMap.WALL){
                    Node n = new Node(x, y, c);
                    keyLocations.add(n);
                }
            }
        }
        System.out.println(keyLocations);
    }

    int[][] moves = {{-1, 0}, {1,0}, {0,-1}, {0,1}};

    public void findPaths(){
        for(Node from: keyLocations){
            for(Node to: keyLocations){
                if(from.equals(to)) continue;
                LinkedList<Node> path = aStar(from, to);
                totalPaths.add(path);
                System.out.println(path);
            }
        }
        //System.out.println(totalPaths);
    }

    public LinkedList<Node> aStar(Node start, Node end){
        boolean[][] visited = new boolean[map.length][map[0].length];
        LinkedList<Node> result = new LinkedList<>();

        PriorityQueue<Node> test = new PriorityQueue<>();

        test.add(start);
        start.calcH(end);
        visited[start.y][start.x] = true;


        while(!test.isEmpty()){
            Node curr = test.poll();
            if(curr.equals(end)){
                while(curr != null){
                    result.addFirst(curr);
                    curr = curr.parent;
                }
                return result;
            }
            for(int i = 0; i <moves.length; i++){
                int nextX = curr.x + moves[i][0];
                int nextY = curr.y + moves[i][1];
                char nextC = map[nextY][nextX];
                if(!visited[nextY][nextX] && nextC != DungeonMap.WALL){
                    visited[nextY][nextX] = true;
                    Node next = new Node(nextX, nextY, nextC);
                    next.g = curr.g +1;
                    next.calcH(end);
                    next.parent = curr;
                    test.add(next);
                }
            }

        }

        return null;
    }

    class Edge implements Comparable<Edge>{
        LinkedList<Node> path;

        public Edge(LinkedList<Node> path){
            this.path = path;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.path.size(), o.path.size());
        }

        public String key(){
            assert path.peekFirst() != null;
            return path.peekFirst().item + "->" + path.peekLast().item;
        }

        @Override
        public String toString(){
            return key() + "[" + this.path.size() + "]";
        }
        
    }

    public void findMST(){
        Set<Character> visited = new HashSet<>();
        Map<String, Edge> mst = new HashMap<>();
        
        
        Node curr = keyLocations.peekFirst();
        assert curr != null;
        visited.add(curr.item);

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for(LinkedList<Node> edge : totalPaths){
            if(edge.peek().equals(curr)){
                pq.add(new Edge(edge));
            }
        }
        while(!pq.isEmpty()){
            Edge edge = pq.poll();
            assert edge.path.peekLast() != null;
            char item = edge.path.peekLast().item;
            if(visited.contains(item)) continue;
            visited.add(item);
            mst.put(edge.key(), edge);
            for(LinkedList<Node> nextEdges : totalPaths){
                if(nextEdges.peekFirst().item == item){
                    pq.add(new Edge(nextEdges));
                }
            }
        }
        System.out.println(mst.values());
    }
}