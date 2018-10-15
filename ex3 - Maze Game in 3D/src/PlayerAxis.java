//Inbar Demuth 204885370
//Yakir Pinchas 203200530
import MathCalculation.MathCalcu;

public class PlayerAxis {
	
	//player coordinate
	float x[] = new float[3];
	float y[] = new float[3];
	float z[] = new float[3];
	
	//player position
	float position[] = new float[3];
	
	//look at point
	float lookAt[] = new float[3];
	
	//move step in every press(rate move)
	float step;
	
	//angleStep for rotation rate
	float angleStep;
	
	//translate matrix to player axis
	float coordiTrans[][] = new float[3][3];
			
	public PlayerAxis(float move_step, float angle_move) {
		x[0] = 1;
		y[1] = 1;
		z[2] = -1;
		step = move_step;
		angleStep = angle_move;
		setTransMatrix();
	}
	
	/**
	 * reset the player axis to the basis axis
	 */
	public void resetAxis() {
		x[0] = 1;
		x[1] = 0;
		x[2] = 0;
		
		y[0] = 0;
		y[1] = 1;
		y[2] = 0;
		
		z[0] = 0;
		z[1] = 0;
		z[2] = -1;
		setTransMatrix();
	}
	
	/**
	 * rotate the player axis to get camera movement
	 * and then normalize the player axis
	 * @param angle_step
	 * @param axis
	 */
	public void cameraMoving(float moveParam, String axis) {
		
		float[] new_x = x;
		float[] new_y = y;
		float[] new_z = z;
		float alfa = moveParam * angleStep;
		
		switch(axis) {
		case "x":
			new_z = MathCalcu.addVectors(
					MathCalcu.multVecScalar(z, MathCalcu.COS(alfa)), 
					MathCalcu.multVecScalar(y, MathCalcu.SIN(alfa)));
			new_y = MathCalcu.subVectors(
					MathCalcu.multVecScalar(y, MathCalcu.COS(alfa)), 
					MathCalcu.multVecScalar(z, MathCalcu.SIN(alfa)));
			break;
		case "y":
			new_x = MathCalcu.addVectors(
					MathCalcu.multVecScalar(x, MathCalcu.COS(alfa)), 
					MathCalcu.multVecScalar(z, MathCalcu.SIN(alfa)));
			new_z = MathCalcu.subVectors(
					MathCalcu.multVecScalar(z, MathCalcu.COS(alfa)), 
					MathCalcu.multVecScalar(x, MathCalcu.SIN(alfa)));
			break;
		case "z":
			new_x = MathCalcu.addVectors(
					MathCalcu.multVecScalar(x, MathCalcu.COS(alfa)), 
					MathCalcu.multVecScalar(y, MathCalcu.SIN(alfa)));
			new_y = MathCalcu.subVectors(
					MathCalcu.multVecScalar(y, MathCalcu.COS(alfa)), 
					MathCalcu.multVecScalar(x, MathCalcu.SIN(alfa)));
		}
		
		x = new_x;
		y = new_y;
		z = new_z;
		normalization();
	}
	
	/**
	 * move the player in the space, get the direction move in basis axis,
	 * translate it to the player coordinate and then move in rate of "step"
	 * @param x_move
	 * @param y_move
	 * @param z_move
	 */
	public void playerMoving(float x_move, float y_move, float z_move) {
		float[] move = new float[3];
		move[0] = x_move;
		move[1] = y_move;
		move[2] = z_move;
		
		//set the matrix with the current player coordinate
		setTransMatrix();
		float[] trans_move = transVector(move);
		position[0] = position[0] + step*trans_move[0];
		position[1] = position[1] + step*trans_move[1];
		position[2] = position[2] + step*trans_move[2];
	}
	
	/**
	 * given current position, calculate the next position 
	 * and return it without save it
	 * @param x_move
	 * @param y_move
	 * @param z_move
	 * @return
	 */
	public float[] nextPos(float x_move, float y_move, float z_move) {
		float[] move = new float[3];
		move[0] = x_move;
		move[1] = y_move;
		move[2] = z_move;
		
		//set the matrix with the current player coordinate
		setTransMatrix();
		float[] trans_move = transVector(move);
		move[0] = position[0] + step*trans_move[0];
		move[1] = position[1] + step*trans_move[1];
		move[2] = position[2] + step*trans_move[2];
		return move;
	}
	
	/**
	 * set player position
	 * @param pos
	 */
	public void setPos(float[] pos) {
		position[0] = pos[0];
		position[1] = pos[1];
		position[2] = pos[2];
	}
	
	/**
	 * create the look at point for the "gluLookAt" func
	 */
	public void setLookAtPoint() {
		lookAt[0] = position[0] + z[0];
		lookAt[1] = position[1] + z[1];
		lookAt[2] = position[2] + z[2];
	}
	
	/**
	 * set the Coordinate Changing matrix with the player current Coordinate
	 */
	public void setTransMatrix() {
		for (int i = 0; i < 3; i++) {
			coordiTrans[i][0] = x[i];
			coordiTrans[i][1] = y[i];
			coordiTrans[i][2] = z[i];
		}
	}
	
	/**
	 * get vector in the player coordinate and change it to 
	 * world coordinate
	 * @param v
	 * @return
	 */
	public float[] transVector(float[] v) {
		return MathCalcu.multiplyMatrixInVector(coordiTrans, v);
	}
	
	/**
	 * normalize the player coordinate
	 */
	public void normalization() {
		float x_len = MathCalcu.vectorLen(x);
		float y_len = MathCalcu.vectorLen(y);
		float z_len = MathCalcu.vectorLen(z);
		
		for (int i = 0; i < 3; i++) {
			x[i] = x[i] / x_len;
			y[i] = y[i] / y_len;
			z[i] = z[i] / z_len;
		}
	}
}
