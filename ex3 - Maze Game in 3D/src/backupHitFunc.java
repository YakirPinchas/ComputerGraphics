/*import java.util.Vector;

import MathCalculation.MathCalcu;*/

public class backupHitFunc {
	/*boolean w1 = isHitOneWall(nextX, yPos, nextZ, wall1);
	boolean w2 = isHitOneWall(nextX, yPos, nextZ, wall2);
	boolean w3 = isHitOneWall(nextX, yPos, nextZ, wall3);
	boolean w4 = isHitOneWall(nextX, yPos, nextZ, wall4);
	
	if (walls[0] && w1) {
		System.out.println("hit! wall 1");
		return true;
	}
	
	if (walls[1] && w2) {
		System.out.println("hit! wall 2");
		return true;
	}
	
	if (walls[2] && w3) {
		System.out.println("hit! wall 3");
		return true;
	}
	
	if (walls[3] && w4) {
		System.out.println("hit! wall 4");
		return true;
	}*/
	
	/*if ((walls[0] && isHitOneWall(nextX, yPos, nextZ, wall1)) ||
			(walls[1] && isHitOneWall(nextX, yPos, nextZ, wall2)) ||
			(walls[2] && isHitOneWall(nextX, yPos, nextZ, wall3)) ||
			(walls[3] && isHitOneWall(nextX, yPos, nextZ, wall4))) {
		return true;
	}*/
	//return false;
	
	//System.out.println("xPos: " + xPos + " zPos: " + zPos);
	//System.out.println("i: " + i + " j: " + j);
	
	//if the current position is inside that cell
	/*if (zPos < 0.5f+j && zPos > -0.5f+j &&
			xPos < 0.5f+i && xPos > -0.5f+i) {
		
		//check if there is collision
		//wall1
    	if (walls[0] && nextX <= -0.47f+i) { 
    		System.out.println("hittt!! wall1");
    		return true;
    	}
    	
    	//wall2
    	else if (walls[1] && nextX >= 0.47f+i) { 
    		System.out.println("hittt!! wall2");
    		return true;
    	}
    	
    	//wall3
    	else if (walls[2] && nextZ <= -0.47f+j) { 
    		System.out.println("hittt!! wall3");
    		return true;
    	}
    	
    	//wall4
    	else if (walls[3] && nextZ >= 0.47f+j) { 
    		System.out.println("hittt!! wall4");
    		return true;
    	}
    	
    	//if it was outside the cell and then hit the wall of this cell
	} 
	/*else {
		if ((nextX  < 0.5f+i && nextX > -0.5f+i) ||
				(nextZ < 0.5f+j && nextZ > -0.5f+j)) {
			
			//check if there is collision
    		//wall1
        	if (walls[0] && (Math.abs((0.47f+i) - nextX) <= 0.2)) { 
        		System.out.println("outside!! wall1");
        		return true;
        	}
        	
        	//wall2
        	else if (walls[1] && (Math.abs((0.47f+i) - nextX) <= 0.2)) { 
        		System.out.println("outside!! wall2");
        		return true;
        	}
        	
        	//wall3
        	else if (walls[2] && (Math.abs((0.47f+j) - nextZ) <= 0.2)) { 
        		System.out.println("outside!! wall3");
        		return true;
        	}
        	
        	//wall4
        	else if (walls[3] && (Math.abs((0.47f+j) - nextZ) <= 0.2)) { 
        		System.out.println("outside!! wall4");
        		return true;
        	}
		}
	}*/
	//return false;
}

/*private boolean isHitOneWall(float xPos, float yPos, float zPos, float[][] wall) {
	Vector<Float> posToUpLeft = new Vector<Float>(3);
	Vector<Float> posToUpRight = new Vector<Float>(3);
	Vector<Float> posToDownLeft = new Vector<Float>(3);
	Vector<Float> posToDownRight = new Vector<Float>(3);
	
	posToUpLeft.add((xPos - wall[0][0] + i));
	posToUpLeft.add(yPos - wall[0][1]);
	posToUpLeft.add(zPos - (wall[0][2] + j));
	
	posToUpRight.add(xPos - (wall[1][0] + i));
	posToUpRight.add(yPos - wall[1][1]);
	posToUpRight.add(zPos - (wall[1][2] + j));
	
	posToDownLeft.add(xPos - (wall[2][0] + i));
	posToDownLeft.add(yPos - wall[2][1]);
	posToDownLeft.add(zPos - (wall[2][2] + j));
	
	posToDownRight.add(xPos - (wall[3][0] + i));
	posToDownRight.add(yPos - wall[3][1]);
	posToDownRight.add(zPos - (wall[3][2] + j));
	
	float angle = MathCalcu.angleBetweenVectors(posToUpLeft, posToUpRight) +
			MathCalcu.angleBetweenVectors(posToUpRight, posToDownLeft) +
			MathCalcu.angleBetweenVectors(posToDownLeft, posToDownRight) +
			MathCalcu.angleBetweenVectors(posToDownRight, posToUpLeft);
	
	//System.out.println(angle);
	
	if (Math.abs(360 - angle) < 2) {
		return true;
	}
	
	return false;
}*/
