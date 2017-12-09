package control.pathfinding;

import com.jme3.math.Vector3f;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2D pathfinding A*
 * with scaleable node network
 * Note: y is the up vector , it has bin swaped with z !
 * @author novo
 */


public class Pathfinding {

    private final Vector3f minPoint;
    private final Vector3f maxPoint;
    private final Boarder border;

	public class Node implements Comparable<Node> {

		public Vector3f position;
		// should be int !!!
		int fScore = 1;
		int gScore = 0;
		// for unpassable Nodes
		public boolean isBlocked = false;
		// for Nodes which are temporal unpassable
		boolean isTempBlocked = false;
		boolean isVisited = false;

		List<Node> neighbor;
		Node NW, N, NE, W, E, SW, S, SE = null;
		Node pre;

		public Node(Vector3f position, int fScore) {
			this.position = position;
			this.neighbor = new ArrayList<>();

		}

		public List<Node> getNeighbor() {

			if (NW != null) {
				this.neighbor.add(NW);
			}
			if (N != null) {
				this.neighbor.add(N);
			}
			if (NE != null) {
				this.neighbor.add(NE);
			}
			if (W != null) {
				this.neighbor.add(W);
			}
			if (E != null) {
				this.neighbor.add(E);
			}
			if (SW != null) {
				this.neighbor.add(SW);
			}
			if (S != null) {
				this.neighbor.add(S);
			}
			if (SE != null) {
				this.neighbor.add(SE);
			}

			return this.neighbor;
		}

		@Override
		public int compareTo(Node o) {
			if (this.fScore < o.fScore) {
				return -1;
			} else if (this.fScore > o.fScore) {
				return 1;
			}
			return 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((position == null) ? 0 : position.hashCode());
			result = (int) (prime * result + fScore);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (position == null) {
				if (other.position != null)
					return false;
			} else if (!position.equals(other.position))
				return false;
			return fScore == other.fScore;
		}

		private Pathfinding getOuterType() {
			return Pathfinding.this;
		}

	}

	List<Node> closedList;
	List<Node> openList;

	private int tentativeGscore;
	private Node start;
	private Node goal;
	
	private int WIDTH;
	private int HEIGHT;
	private Node[] nodeMap;
	private double i ;

        /**
         * 
         * @param minPoint of the map (left bottom)
         * @param maxPoint of the map (right up)
         * @param i scale factor to encrease node distance
         */
	public Pathfinding(Vector3f minPoint, Vector3f maxPoint, int i) {
		this.i = i;
		this.minPoint = minPoint;
                this.maxPoint = maxPoint;
                this.border = new Boarder(this.minPoint, this.maxPoint);
                
		this.border.width /= i;
		this.border.height/= i;
		this.WIDTH = (int) border.getWidth();
		this.HEIGHT = (int) border.getHeight();
		this.nodeMap = new Node[this.WIDTH * this.HEIGHT];

	}
        
        private class Boarder{
            float width;
            float height;
            Vector3f minPoint;
            Vector3f maxPoint;
            
            public Boarder(Vector3f minPoint, Vector3f maxPoint)
            {
                this.minPoint = minPoint;
                this.maxPoint = maxPoint;
                this.width =Math.abs( minPoint.x - maxPoint.x);
                this.height = Math.abs(minPoint.z - maxPoint.z);
            }
            
            public float getWidth()
            {
                return this.width;
            }
            
            public float getHeight(){
               return this.height;
            }

        private int getMaxZ() {
           return (int) maxPoint.z;
        }

        private int getMaxX() {
            return (int) maxPoint.x;
        }
        
        private int getMinX()
        {
            return (int) minPoint.x;
        }
        
        private int getMinZ()
        {
            return (int) minPoint.z;
        }
        }

	public List<Node> getBlockedNodes() {
		List<Node> list = new ArrayList<>();
		int index = 0;
		for (int z = this.border.getMinZ(); z < this.border.getMaxZ(); z++) {
			for (int x = this.border.getMinX(); x < this.border.getMaxX(); x++) {
				index = (x + z * (int) this.border.getMaxX());

				if (this.nodeMap[index].isBlocked)
					list.add(this.nodeMap[index]);
			}
		}
		return list;
	}

	/**
	 * going from start Node position(0,0) left upper corner like
	 * Manhattan-method going east to match x coordinate then going south to
	 * match y coordinate
	 * 
	 * @param nodePos
	 * @return
	 */
	public Node getNode(Vector3f nodePos) {

		if (nodePos != null) {
			int x = (int) nodePos.x;
			int z = (int) nodePos.z;
			int index = x + z * this.WIDTH;

			if (x >= 0 && z >= 0 && x < this.WIDTH && z < this.HEIGHT) {
				if (index > 0 && index < (this.WIDTH * this.HEIGHT)) {

					return nodeMap[index];
				}
			}
		}
		
		// should be found a node near goal which is not blocked
		
		return null;
		

	}

	public void setBlockedNodes(Rectangle rec) {

		rec.x /= i;
		rec.y /= i;
		rec.width /= i;
		rec.height /= i;
		
		for (int z = (int) rec.getMinY(); z < rec.getMaxY(); z++) {
			Node tmp;
			for (int x = (int) rec.getMinX(); x < rec.getMaxX(); x++) {
				tmp = getNode(new Vector3f(x, 0,z));

				if (tmp != null) {
					tmp.isBlocked = true;
				}
			}
		}
	}
	public void setUnBlockedNodes(Rectangle rec) {

		for (int z = (int) rec.getMinY(); z < rec.getMaxY(); z++) {
			Node tmp;
			for (int x = (int) rec.getMinX(); x < rec.getMaxX(); x++) {
				tmp = getNode(new Vector3f(x, 0,z));

				if (tmp != null) {
					tmp.isBlocked = true;
				}
			}
		}
	}

	private void reset() {

		// setting up nodes
		for (Node n : this.nodeMap) {
			n.fScore = 1;
			n.gScore = 0;
		}

	}

	public void setMap() {

		int index = 0;
		int mainIndex = 0;
		// setting up nodes
		for (int z = this.border.getMinZ(); z < this.border.getMaxZ(); z++) {
			for (int x = this.border.getMinX(); x < this.border.getMaxX(); x++) {
				index = (x + z * (int) this.border.getMaxX());

				this.nodeMap[index] = new Node(new Vector3f((float)(x*this.i),0,(float)( z*this.i)), 1);

			}
		}
		// add some Border Nodes !!!

		// knot the net
		for (int z = this.border.getMinZ(); z < this.border.getMaxZ(); z++) {
			for (int x = this.border.getMinX(); x < this.border.getMaxX(); x++) {

				mainIndex = (int) ((x) + (z) * this.border.getMaxX());

				index = (int) ((x - 1) + (z - 1) * this.border.getMaxX());
				if (x - 1 > 0 && x - 1 < this.WIDTH && z - 1 > 0
						&& z - 1 < this.HEIGHT) {

					this.nodeMap[mainIndex].NW = this.nodeMap[index];
				}

				index = (int) ((x) + (z - 1) * this.border.getMaxX());
				if (x > 0 && x < this.WIDTH && z - 1 > 0 && z - 1 < this.HEIGHT) {

					this.nodeMap[mainIndex].N = this.nodeMap[index];
				}

				index = (int) ((x + 1) + (z - 1) * this.border.getMaxX());
				if (x + 1 > 0 && x + 1 < this.WIDTH && z - 1 > 0
						&& z - 1 < this.HEIGHT) {

					this.nodeMap[mainIndex].NE = this.nodeMap[index];
				}

				index = (int) ((x + 1) + (z) * this.border.getMaxX());
				if (x + 1 > 0 && x + 1 < this.WIDTH && z > 0 && z < this.HEIGHT) {

					this.nodeMap[mainIndex].E = this.nodeMap[index];
				}

				index = (int) ((x + 1) + (z + 1) * this.border.getMaxX());
				if (x + 1 > 0 && x + 1 < this.WIDTH && z + 1 > 0
						&& z + 1 < this.HEIGHT) {
					this.nodeMap[mainIndex].SE = this.nodeMap[index];
				}

				index = (int) ((x) + (z + 1) * this.border.getMaxX());
				if (x > 0 && x < this.WIDTH && z + 1 > 0 && z + 1 < this.HEIGHT) {
					this.nodeMap[mainIndex].S = this.nodeMap[index];
				}

				index = (int) ((x - 1) + (z + 1) * this.border.getMaxX());
				if (x - 1 > 0 && x - 1 < this.WIDTH && z + 1 > 0
						&& z + 1 < this.HEIGHT) {
					this.nodeMap[mainIndex].SW = this.nodeMap[index];
				}

				index = (int) ((x - 1) + (z) * this.border.getMaxX());
				if (x + -1 > 0 && x - 1 < this.WIDTH && z > 0
						&& z < this.HEIGHT) {
					this.nodeMap[mainIndex].W = this.nodeMap[index];
				}

			}
		}

	}
	private Node findUnblocked(Node node){
		
		if(!node.isBlocked){
			return node;
		}else{
			this.findUnblocked(node.E);
			this.findUnblocked(node.N);
			this.findUnblocked(node.NE);
			this.findUnblocked(node.NW);
			this.findUnblocked(node.W);
			this.findUnblocked(node.SW);
			this.findUnblocked(node.S);
			this.findUnblocked(node.SE);
		}
		
		return null;
	}

	public List<Node> aStar(Vector3f startPos, Vector3f goalPos) {
		
		this.closedList = new ArrayList<>();
		this.openList   = new ArrayList<>();

		this.start = this.getNode(startPos.multLocal((float) (1.0f/i),0,(float) (1.0f/i)));
		this.goal = this.getNode(goalPos.multLocal((float) (1.0f/i),0,(float) (1.0f/i)));
		
		
		
	
		if (this.start != null && this.goal != null) {
			
			if(this.goal.isBlocked){
				return new ArrayList<>();
			}

			this.start.gScore = 0;
			this.start.fScore = (estimatedCost(start, goal));

			this.openList.add(this.start);

			Node current;

			while (!this.openList.isEmpty()) {
		
				current = Collections.min(this.openList);

				if (current.equals(goal)) {
					return this.reconstructPath();
				}

				this.openList.remove(current);
				this.closedList.add(current);

				for (Node n : current.getNeighbor()) {

					if (n != null && !n.isBlocked) {

						if (this.closedList.contains(n)) {
							continue;
						}

						tentativeGscore = (n.gScore + distBetween(current, n));

						if (!this.openList.contains(n)
								|| tentativeGscore < n.gScore) {

							n.gScore = tentativeGscore;
							n.fScore = (n.gScore + this.estimatedCost(n, goal));
							n.pre = current;
							n.isVisited = true;

							if (!this.openList.contains(n)) {
								this.openList.add(n);
							}

						}
					}
				}
			}
		}
		return new ArrayList<>();
	}

	private int distBetween(Node current, Node n) {
		return (int) (n.position.subtract(current.position).length());
	}

	private List<Node> reconstructPath() {
		List<Node> list = new ArrayList<>();
		Node tmp = this.goal;
		while (!tmp.equals(this.start)) {
			list.add(tmp);
			tmp = tmp.pre;
		}

		Collections.reverse(list);
		this.reset();
		return list;
	}

	/**
	 * Manhattan heuristic
	 * 
	 * @param start
	 * @param goal
	 * @return
	 */

	private int estimatedCost(Node start, Node goal) {
		return (int) (10 * (Math.abs(start.position.z
				- goal.position.z) + Math.abs(start.position.x
				- goal.position.x)));
	}
}
