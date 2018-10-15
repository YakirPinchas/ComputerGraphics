//Inbar Demuth 204885370
//Yakir Pinchas 203200530
import com.jogamp.opengl.util.texture.Texture;
import java.util.Vector;
import javax.media.opengl.GL2;

/**
 * Cell, class representing a cell in the maze
 */
public class Cell {

    //Define the sets of coordinates that will make up the walls, floors and ceiling
    private float[][] wall1 = {{-0.5f, 0.5f, 0.5f},
            {-0.5f, -0.5f, 0.5f},
            {-0.5f, -0.5f, -0.5f},
            {-0.5f, 0.5f, -0.5f}};

    private float[][] wall2 = {{0.5f, 0.5f, 0.5f},
            {0.5f, 0.5f, -0.5f},
            {0.5f, -0.5f, -0.5f},
            {0.5f, -0.5f, 0.5f}};

    private float[][] wall3 = {{0.5f, 0.5f, -0.5f},
            {-0.5f, 0.5f, -0.5f},
            {-0.5f, -0.5f, -0.5f},
            {0.5f, -0.5f, -0.5f}};

    private float[][] wall4 = {{0.5f, 0.5f, 0.5f},
            {0.5f, -0.5f, 0.5f},
            {-0.5f, -0.5f, 0.5f},
            {-0.5f, 0.5f, 0.5f}};

    private float[][] floor = {{0.5f, -0.5f, 0.5f},
            {0.5f, -0.5f, -0.5f},
            {-0.5f, -0.5f, -0.5f},
            {-0.5f, -0.5f, 0.5f}};

    private float[][] ceiling = {{0.5f, 0.5f, 0.5f},
            {-0.5f, 0.5f, 0.5f},
            {-0.5f, 0.5f, -0.5f},
            {0.5f, 0.5f, -0.5f}};


    //The list of booleans that determine the existence of the walls
    boolean[] walls;
    //The X and Y index of the maze cell
    private float i;
    private float j;
    //The maze object containing the start/end properties of the maze
    private MazeCellObj obj;
    private boolean haveObj = false;
    //The drawlist variable
    private int rectList = -1;
    
    ///normal for collision
    Vector<Float> axisXNormal = new Vector<Float>(3);
    Vector<Float> axisZNormal = new Vector<Float>(3);
	
    public Cell(boolean[] walls, float i, float j) {
        // This constructor gets passed three parameters
        // The first is a list of booleans listing whether
        // the cell has walls. The order of the walls is
        // +x, -x, +z, -z . So, if your cell is 1x1x1,
        // and the middle of your cell is at
        // (0.5, 0.5, 0.5) the +x wall is the wall
        // whose centre is at (1.0, 0.5, 0.5) and the 
        // -x wall is the wall whose centre is at
        // (0.0, 0.5, 0.5)
        // The i and j values are the cell index in the maze
        this.walls = walls;
        this.i = i;
        this.j = j;
        // Take the rectangle render list for drawing walls etc
        this.rectList = ViewRenderer.rectList;
        
        //set the normals for the collision
        axisXNormal.add(1f);
    	axisXNormal.add(0f);
    	axisXNormal.add(0f);
    	axisZNormal.add(0f);
    	axisZNormal.add(0f);
    	axisZNormal.add(1f);
    }

    
    /**
     * add obj to the cell
     * @param obj
     */
    public void addItem(MazeCellObj obj) {
        //Add the maze object to the list of variables
        this.obj = obj;
        haveObj = true;

        //If it's the start cell, set the initial player position
        if (this.obj.isStart()) ViewRenderer.setPos(i, j - 0.5f);
    }

    public void draw(Texture[] textures, GL2 gl) {

        gl.glPushMatrix();


        //Move the current cell of the maze according to its relative position
        gl.glTranslatef(i, 0.0f, j);

        //Set up the lighting point position
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[]{-0.25f, 0.4f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, new float[]{0.25f, 0.4f, 0.0f, 1.0f}, 0);
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, new float [] { 0.0f, -0.5f, 1.0f, 0.0f}, 0);

        //Enable and bind the textures to be used for mapping onto the faces
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        textures[0].enable(gl);
        textures[0].bind(gl);

        gl.glColor3f(1.0f, 1.0f, 1.0f);

        //If a wall of the current maze cell exists, draw it. Repeat for all 4 walls.
        //right wall
        if (walls[0]) {
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);

        }
        //left wall
        if (walls[1]) {
            gl.glPushMatrix();
            gl.glRotatef(180, 0.0f, 1.0f, 0.0f);
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);
            gl.glPopMatrix();
        }
        //back wall
        if (walls[2]) {
            gl.glPushMatrix();
            gl.glRotatef(270, 0.0f, 1.0f, 0.0f);
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);
            gl.glPopMatrix();
        }
        //front wall
        if (walls[3]) {
            gl.glPushMatrix();
            gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
            gl.glNormal3f(1.0f, 0.0f, 0.0f);
            gl.glCallList(rectList);
            gl.glPopMatrix();
        }

        //Change the textures to be used for next mapping
        textures[0].disable(gl);
        textures[1].enable(gl);
        textures[1].bind(gl);

        //The floor
        gl.glBegin(GL2.GL_POLYGON);
        //setNormal(floor[0], floor[1], floor[2], gl);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 0, 0);
        gl.glVertex3fv(floor[0], 0);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 0, 1);
        gl.glVertex3fv(floor[1], 0);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 1, 1);
        gl.glVertex3fv(floor[2], 0);
        gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, 1, 0);
        gl.glVertex3fv(floor[3], 0);
        gl.glEnd();

        //Change the textures to be used for next mapping
        textures[1].disable(gl);
        textures[2].enable(gl);
        textures[2].bind(gl);

        //The ceiling
        gl.glPushMatrix();
        gl.glRotatef(90, 0.0f, 0.0f, -1.0f);
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glCallList(rectList);
        gl.glPopMatrix();

        textures[2].disable(gl);

        //If the maze's end-point object exists, invoke its draw method
        if (haveObj) obj.draw(gl);
        
        gl.glPopMatrix();
    }

    /**
     * Calculates a normalized vector of a surface, and sends the
     * normalization command to the openGL context
     *
     * @param a  First point of the triangle which surface normal to calculate
     * @param b  Second point of the triangle which surface normal to calculate
     * @param c  Third point of the triangle which surface normal to calculate
     * @param gl The openGL context
     */
    public void setNormal(float[] a, float[] b, float[] c, GL2 gl) {

        //Create the vectors to apply the cross product on
        float v1[] = new float[3];
        float v2[] = new float[3];
        float vC[] = new float[3];
        float vN[] = new float[3];

        //Calculate the vectors by substracting one set of coordinates from another
        v1[0] = b[0] - a[0];
        v1[1] = b[1] - a[1];
        v1[2] = b[2] - a[2];

        v2[0] = c[0] - a[0];
        v2[1] = c[1] - a[1];
        v2[2] = c[2] - a[2];

        //Calculate the cross product between the 2 vectors
        vC[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
        vC[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
        vC[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);

        //Turn the resulting vector into a unit-vector
        float magnitude = (float) Math.sqrt((vC[0] * vC[0]) + (vC[1] * vC[1]) + (vC[2] * vC[2]));
        vN[0] = vC[0] / magnitude;
        vN[1] = vC[1] / magnitude;
        vN[2] = vC[2] / magnitude;
        //Send the normalization command with the vector to the GL context
        gl.glNormal3fv(vN, 0);
    }
    
    /**
     * get the next step of the player and the radius of the player. 
     * first if the next step is not close to this cell - then return false, no collision.
     * else, use the Bounding Sphere method when the center is the player position. 
     * the normal are always the z axis and the x axis,
     * so to make it shorter - just calculate the x or the z distance.
     * @param nextX next x
     * @param nextY next y
     * @param nextZ next z
     * @return true if there is collision, else false
     */
    public boolean isHit(float nextX, float nextY, float nextZ, float playerRadius) {
    	
    	//if the next step not close to this cell, then no collision
    	if (Math.abs(Math.abs(nextX) - i) > 0.518 || Math.abs(Math.abs(nextZ) - j) > 0.518) {
    		return false;
    	}
    	
    	//if looking on the floor
    	if (nextY < -0.45f) {
    		return true;
    	}
    	
    	//distance from wall1 to the player
    	/*Vector<Float> playerToWall1 = new Vector<Float>(3);
    	playerToWall1.add(nextX - (i + wall1[0][0]));
    	playerToWall1.add(nextY - wall1[0][1]);
    	playerToWall1.add(nextZ - (j + wall1[0][2]));
    	float distanceWall1 = MathCalcu.dotProduct(playerToWall1, axisXNormal);*/
    	float distanceWall1 = nextX - (i + wall1[0][0]);
    	
    	//distance from wall2 to the player
    	/*Vector<Float> playerToWall2 = new Vector<Float>(3);
    	playerToWall2.add(nextX - (i + wall2[0][0]));
    	playerToWall2.add(nextY - wall2[0][1]);
    	playerToWall2.add(nextZ - (j + wall2[0][2]));
    	float distanceWall2 = MathCalcu.dotProduct(playerToWall2, axisXNormal);*/
    	float distanceWall2 = nextX - (i + wall2[0][0]);
    	
    	//distance from wall3 to the player
    	/*Vector<Float> playerToWall3 = new Vector<Float>(3);
    	playerToWall3.add(nextX - (i + wall3[0][0]));
    	playerToWall3.add(nextY - wall3[0][1]);
    	playerToWall3.add(nextZ - (j + wall3[0][2]));
    	float distanceWall3 = MathCalcu.dotProduct(playerToWall3, axisZNormal);*/
    	float distanceWall3 = nextZ - (j + wall3[0][2]);
    	
    	//distance from wall4 to the player
    	/*Vector<Float> playerToWall4 = new Vector<Float>(3);
    	playerToWall4.add(nextX - (i + wall4[0][0]));
    	playerToWall4.add(nextY - wall4[0][1]);
    	playerToWall4.add(nextZ - (j + wall4[0][2]));
    	float distanceWall4 = MathCalcu.dotProduct(playerToWall4, axisZNormal);*/
    	float distanceWall4 = nextZ - (j + wall4[0][2]);
    	
    	//check if there is collision
    	if (walls[0] && Math.abs(distanceWall1) < playerRadius) {
    		//System.out.println("hit! wall 1");
    		return true;
    	}
    	if (walls[1] && Math.abs(distanceWall2) < playerRadius) {
    		//System.out.println("hit! wall 2");
    		return true;
    	}
    	if (walls[2] && Math.abs(distanceWall3) < playerRadius) {
    		//System.out.println("hit! wall 3");
    		return true;
    	}
    	if (walls[3] && Math.abs(distanceWall4) < playerRadius) {
    		//System.out.println("hit! wall 4");
    		return true;
    	}
    	
    	return false;
    	
    }
    
    /**
     * check if the next step will hit the cell object
     * @param nextX
     * @param nextY
     * @param nextZ
     * @param objRadius
     * @return
     */
    public MazeCellObj.ObjType isHitObj(float nextX, float nextY, float nextZ, float objRadius) {
    	
    	//if there is an object in the cell
    	if (haveObj) {
    		
    		//calculate the distance from the object
    		float xDistance = Math.abs((i - nextX));
    		float zDistance = Math.abs((j - nextZ));
    		float yDistance;
    		if (obj.getObjType() == MazeCellObj.ObjType.END) {
    			yDistance = Math.abs((-0.2f - nextY));
    		} else {
    			yDistance = Math.abs((-0.5f - nextY));
    		}
    		
    		//if it's a dmg obj change the radius
    		if (obj.getObjType() == MazeCellObj.ObjType.DAMAGE) {
    			objRadius += 0.1;
    		}
    		
    		//check if hit the obj
    		if (xDistance < objRadius && zDistance < objRadius && yDistance < 0.35f &&
    				xDistance + 0.5 > objRadius && zDistance + 0.5 > objRadius) {
    			
    			//if hit, return the type of the object
    			return obj.getObjType();
    		}
    	}
    	
    	//if we didnt hit an object, return none
    	return MazeCellObj.ObjType.NONE;
    }
    
    public void deleteCellObj() {
    	obj.stopDraw();
    	haveObj = false;
    }
}
