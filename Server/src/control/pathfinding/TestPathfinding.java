package control.pathfinding;

import com.jme3.math.Vector3f;
import control.pathfinding.Pathfinding.Node;



public class TestPathfinding {

	   Vector3f startPos = new Vector3f (10,0,10);
	Vector3f  goalPos = new Vector3f (50, 0,50);
	

	

	public TestPathfinding() {
	System.out.println(goalPos.subtract(startPos).length());
		
		Pathfinding         path = new Pathfinding(new Vector3f(0,0,0),new Vector3f(500,0,500), 10);
               
	path.setMap();
		
		path.getNode(new Vector3f(3,0,0)).isBlocked = true;
		path.getNode(new Vector3f(3,0,1)).isBlocked = true;
		path.getNode(new Vector3f(3,0,2)).isBlocked = true;
		path.getNode(new Vector3f(3,0,3)).isBlocked = true;
		path.getNode(new Vector3f(3,0,4)).isBlocked = true;
		path.getNode(new Vector3f(3,0,5)).isBlocked = true;
		path.getNode(new Vector3f(3,0,6)).isBlocked = true;
		
		
		
		
		path.getNode(new Vector3f(3,7,0)).isBlocked = true;

		
		System.out.println(path.aStar(startPos, goalPos).size());
		for(Node n:path.aStar(startPos, goalPos)){
			System.out.println("test"+n.position);
		}
                
                	System.out.println(path.aStar(startPos, goalPos).size());
		for(Node n:path.aStar(startPos, goalPos)){
			System.out.println("test"+n.position);
		}
	}

	public static void main(String[] args) {
               TestPathfinding testPathfinding = new TestPathfinding();

	}

}
