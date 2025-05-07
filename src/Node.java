public class Node implements Comparable<Node>{

    public int x;
    public int y;
    public char item;
    public boolean isVisited;
    public int g; 
    public int h;
    public Node parent;
    

    public Node(int x, int y, char item){
        this.x = x;
        this.y = y;
        this.item = item;
        this.isVisited = false;
    }

    public void calcH(Node goal){
        h = Math.abs((this.x - goal.x) + (this.y - goal.y));
    }

    public int f(){
        return g + h;
    }

    public String toString(){
        return "" + item + "(" + x + "," + y + ")"; 
    }

    @Override
    public int compareTo(Node o) {
       return Integer.compare(this.f(), o.f());
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node)) return false;
        Node other = (Node) o;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode(){
        return x * y + h + g;
    }
}