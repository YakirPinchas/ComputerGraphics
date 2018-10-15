//Inbar Demuth 204885370
//Yakir Pinchas 203200530
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;
import java.io.File;
import java.util.ArrayList;


/**
 *         A View rendering class that handles all the OpenGL code.
 *         This class creates a world with the following conventions:
 *         y is up
 *         the default color is white
 *         the default matrix is the modelview matrix
 */
public class ViewRenderer implements GLEventListener {

	// The current time for global animation
    private int time = 0;
    // The amount to update time
    private int deltaT = 1;
    // The maximum number of frames to skip
    private int maxFrameSkip = 10;
    // The delay between time updates
    private long delay;
    // The time last time we checked
    private long lastTime;
    
    //Camera X position
    private static float initXPos;
    //Camera Z position
    private static float initZPos;
    //the min point of the player stand
    static float playerFloor = -0.2f;
    //the movement args
    float steps[] = new float[3];
    //the camera turn args
    float cameraTurn[] = new float[3];
    //the player axis 
    static PlayerAxis playerPos;
    //jumping state
    public enum PlayerState {
    	JUMPING_UP, JUMPING_DOWN, JUMPING_TOP, WALKING
    }
    PlayerState jumpState = PlayerState.WALKING;
    //the jump high limit
    float jumpLimit = 0.1f;
    
  	//end game key
  	private int reachEndKey = 0;
    //player keys
    private Image key1;
    private Image key2;
    //player health
    private HealthBar healthBar;
    //hp image
    private Image hpImg;
    //game details image
    private Image details;
    //game over screen
    private Image gameOver;
    //win screen
    private Image win;
    //level two title
    private Image levelTwoTitle;
    //HUD list for draw in 2D
    private ArrayList<HUD> objDraw2D;
    //the level we want to draw
    private int levelDraw = 1;
    //play level
    private int currentLevel = 1;
    //finish game flag
    private boolean finishGame = false;
    
    //The wall/ceiling/etc drawlist
    static int rectList = -1;
    // The list of the maze cells
    private ArrayList<Cell> mazeCells;
    //Initialize the texture variable array
    private Texture[] textures = new Texture[3];

    GLU glu = new GLUgl2();
    GLUT glut = new GLUT();

    /**
     * Default constructor for the ViewRenderer class
     *
     * @param fps - the number of fps at which animation should occur.
     */
    public ViewRenderer(int fps) {
        // Delay between time updates in ms
        delay = 1000 / fps;
        // Look at the time now
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    /**
     * This function sets up the camera and then tells the scene objects to draw themselves
     */

    @Override
    public void display(GLAutoDrawable drawable) {
    	
        // Get the OpenGL Context
        GL2 gl = drawable.getGL().getGL2();
        
        switch(levelDraw) {
        case 1:
        	mazeCells = MazeLoader.MakeMaze("maze_layout_1");
            playerPos.resetAxis();
            levelDraw = 0;
            break;
        case 2:
        	mazeCells = MazeLoader.MakeMaze("maze_layout_2");
            playerPos.resetAxis();
            levelDraw = 0;
            break;
        }

        //Update the time variable for each frame
        time++;
        
        //TODO limit the rotation
        //Update the camera angle based on the cameraTurn if we didnt finish the game
        if (!finishGame) {
        	playerPos.cameraMoving(cameraTurn[0], "x");
            playerPos.cameraMoving(cameraTurn[1], "y");
            playerPos.cameraMoving(cameraTurn[2], "z");
        }
        
        //Update the player position according to the steps
        float newPos[] = playerPos.nextPos(steps[0], steps[1], steps[2]);
        
        //if the next step change the y and it's not a jump, dont do it!
        if(newPos[1] > playerFloor && jumpState == PlayerState.WALKING) {
        	newPos[1] = playerFloor;
        	
        // if it's a jump
        } else if(jumpState != PlayerState.WALKING) {
        	newPos[1] += jump(newPos[1]);
        }
        
        //check if there is collision
        boolean collisionFlag = false;
        for (Cell cell : mazeCells) {
        	if (cell.isHit(newPos[0], newPos[1], newPos[2], 0.1f)) {
        		collisionFlag = true;
        		//decrease the lives in 1
        		healthBar.decreaseHP(1);
        		//if not lives
        		if(healthBar.getHealth() == 0) {
        			//message that lives it's over
        			defete();
        			return;
        		}
        		break;
        	}
        	
        	switch(cell.isHitObj(newPos[0], newPos[1], newPos[2], 0.1f)) {
    		case START:
    			break;
    		case END:
    			//if player came to end point in maze and take the level key.
    			if(reachEndKey == 2) {
    				
            		//if we in stage 1 , we are finished and move to stage 2
            		if(currentLevel == 1) {
            			currentLevel = 2;
            			setLevelTwo();
            			return;
            		} else {
            			//finish the game
            			finishGame = true;
            			objDraw2D.add(win);
            		}
            	}
    			collisionFlag = true;
    			break;
    		case HEAL:
    			//if the player take the object that gain lives
    			healthBar.increaseHP(3);
    			cell.deleteCellObj();
    			collisionFlag = true;
    			break;
    		case DAMAGE:
    			//if the player take the object that decrease lives
    			//decrease his lives
        		healthBar.decreaseHP(2);
        		//check if live is over.
        		if(healthBar.getHealth() == 0) {
        			//send messeage that lives it's over
        			defete();
        			return;
        		}
        		collisionFlag = true;
    			break;
    		case ENDKEY:
    			//if player take the key that help him to win in this stage
    			reachEndKey += 1;
    			collisionFlag = true;
    			cell.deleteCellObj();
    			switch (reachEndKey) {
    			case 1:
    				objDraw2D.add(key1);
    				break;
    			case 2:
    				objDraw2D.add(key2);
    				break;
    			}
    			break;
			default:
				break;	
    		}
        }
        
        //if there is no collision, do the step
        if (!collisionFlag && !finishGame) {
            playerPos.setPos(newPos);
        }
        
        //update the look-at position based on the current cam position
        playerPos.setLookAtPoint();

        //Clear the colour and depth buffers
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //Load the identity matrix and set up the camera based on its coords
        gl.glLoadIdentity();
        glu.gluLookAt(playerPos.position[0], playerPos.position[1], playerPos.position[2],
        		playerPos.lookAt[0], playerPos.lookAt[1], playerPos.lookAt[2],
        		playerPos.y[0], playerPos.y[1], playerPos.y[2]);

        //Iterate through the draw methods of each cell in the arraylist
        for (Cell cell : mazeCells) {
            cell.draw(textures, gl);
        }
        
        //draw all the 2D objects
        drawHUD(gl, objDraw2D);
        
        // Flush the data.
        gl.glFlush();
        
    }
    
    /**
     * This function initialises the OpenGL environment
     */
    @Override
    public void init(GLAutoDrawable drawable) {

        drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        
        //player axis get the player speed and the camera speed
        playerPos = new PlayerAxis(0.08f, 4.9f);

        //Set up the default buffer clear parameters
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glClearDepth(1.0f);

        //Enable the depth testing
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

        //Enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHT1);
        
        //Set up the minimum ambient lighting level of the whole scene
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f}, 0);

        //Set up the light's material properties
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

        // using texture color as a base for material properties
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        //Set up a light source for light 0 and 1
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[]{0.4f, 0.4f, 0.4f, 1.0f}, 0);

        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, new float[]{0.4f, 0.4f, 0.4f, 1.0f}, 0);

        //Set the attenuation parameter of the lights
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, 0.5f);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, 0.5f);

        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_LINEAR_ATTENUATION, 0.5f);
        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_QUADRATIC_ATTENUATION, 0.5f);

        //Enable backface culling to save up more system resources
        gl.glCullFace(GL2.GL_BACK);
        gl.glEnable(GL2.GL_CULL_FACE);

        //Turn on texturing
        //gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        //gl.glActiveTexture(GL.GL_TEXTURE1);
        //gl.glEnable(GL2.GL_TEXTURE_2D);

        //Load the image files to be used as textures
        try {
            textures[0] = TextureIO.newTexture(new File("textures/wall.jpg"), true);
            textures[1] = TextureIO.newTexture(new File("textures/floor.jpg"), true);
            textures[2] = TextureIO.newTexture(new File("textures/ceiling.jpg"), true);
        } catch (Exception e) {
            System.out.println("Error: cannot load textures");
            e.printStackTrace();
        }
        
        //create health bar
        healthBar = new HealthBar();
        hpImg = new Image("textures/hp.png", new float [][]{{-0.48f, 0.2f},{-0.48f, -0.4f},{-0.1f, -0.4f},{-0.1f, 0.2f}});
        hpImg.loadImage();
        //create player key image when find one
        key1 = new Image("textures/key.jpg", new float [][]{{9.2f, -8.8f},{9.2f, -9.5f},{9.7f, -9.5f},{9.7f, -8.8f}});
        key1.loadImage();
        key2 = new Image("textures/key.jpg", new float [][]{{8.6f, -8.8f},{8.6f, -9.5f},{9.1f, -9.5f},{9.1f, -8.8f}});
        key2.loadImage();
        //create game details image
        details = new Image("textures/details.png", new float [][]{{1.5f, -0.8f},{1.5f, -8.7f},{8.0f, -8.7f},{8.0f, -0.8f}});
        details.loadImage();
        //create level two title
        levelTwoTitle = new Image("textures/levelTwo.png", new float [][]{{1.0f, -2.3f},{1.0f, -7.2f},{8.5f, -7.2f},{8.5f, -2.3f}});
        levelTwoTitle.loadImage();
        //game over / win screen
        gameOver = new Image("textures/gameover.png", new float [][]{{1.0f, -3.0f},{1.0f, -6.0f},{8.5f, -6.0f},{8.5f, -3.0f}});
        gameOver.loadImage();
        win = new Image("textures/win.png", new float [][]{{1.0f, -3.0f},{1.0f, -6.0f},{8.5f, -6.0f},{8.5f, -3.0f}});
        win.loadImage();
        
        //set the texture wrap
        textures[0].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        textures[0].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        textures[1].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        textures[1].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        textures[2].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        textures[2].setTexParameteri(gl, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);

        //Create a draw list of a generic wall/ceiling/floor rectangle
        //that consists of 100x100 smaller vertexes for smooth lignting
        rectList = gl.glGenLists(1);
        gl.glNewList(rectList, GL2.GL_COMPILE);
        gl.glPushMatrix();
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, -0.475f, -0.475f);
        //gl.glNormal3f(0,0,-1);
        
        //create generic wall vertices on the y-z axis
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                gl.glBegin(GL2.GL_POLYGON);
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) + (0.5f * 0.05f));
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) - (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glVertex3f(-0.5f, (i / 20.0f) + (0.5f * 0.05f), (j / 20.0f) - (0.5f * 0.05f));
                gl.glEnd();
            }
        }

        gl.glPopMatrix();
        gl.glEndList();
        
        //set the list of the objects that will be draw in 2D
        objDraw2D = new ArrayList<HUD>();
        objDraw2D.add(hpImg);
        objDraw2D.add(healthBar);
        
        //Load the maze from the text file
        mazeCells = MazeLoader.MakeMaze("maze_layout_1");
    }

    /**
     * Re-initialise the projection matrix and viewport after a reshape event
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height <= 0) height = 1;
        float aspectRatio = (float) x / (float) y;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(75, 1, 0.01, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    
    /**
     * Update the time variable used in the world.
     */
    private void updateTime() {
        // Update the time for as much time as has passed
    	long curTime = System.currentTimeMillis();
        long numFrames = ((curTime - lastTime) / delay);

        // Clamp numFrames to a minimum of 1 and a maximum of maxFrameSkip frames
        numFrames = Math.max(numFrames, 1l);
        numFrames = Math.min(numFrames, maxFrameSkip);

        // Increase time by the update amount. When deltaT is set
        // to zero this has no effect.
        time += deltaT * numFrames;

        // record the time so we can measure the time difference next frame
        // If we are paused or have skipped over maxFrameSkip frames, don't try to keep track of
        // partial frames
        if (delay == 0 || numFrames == maxFrameSkip) {
            lastTime = curTime;
        } else {
            lastTime += numFrames * delay;
        }
    }

    /**
     * Get the current time.
     *
     * @return The current time variable
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the initial camera position
     *
     * @param initXPos the X position of the player
     * @param initZPos the Z position of the player
     */
    public static void setPos(float inXPos, float inZPos) {
        initXPos = inXPos;
        initZPos = inZPos;
        playerPos.setPos(new float[]{inXPos, playerFloor, inZPos});
    }
    /**
     * Reset the camera position
     */
    public void reset() {
    	//update the start position
        playerPos.setPos(new float[]{initXPos, playerFloor, initZPos});
        playerPos.resetAxis();
    }

    /**
     * Move the player on the z and x axis(without moving the camera)
     *
     * @param move the boolean that decides to either move or stand still
     */
    public void moveForward(Boolean move) {
        if (move) steps[2] = 1f;
        else steps[2] = 0f;
    }
    public void moveBackward(Boolean move) {
        if (move) steps[2] = -1f;
        else steps[2] = 0f;
    }
    public void moveLeft(Boolean move) {
        if (move) steps[0] = -1f;
        else steps[0] = 0f;
    }
    public void moveRight(Boolean move) {
        if (move) steps[0] = 1f;
        else steps[0] = 0f;
    }


    /**
     * Turn the camera
     * @param move
     */
    public void turnLeft(Boolean move) {
        if (move) cameraTurn[1] = 1f;
        else cameraTurn[1] = 0f;
    }
    public void turnRight(Boolean move) {
        if (move) cameraTurn[1] = -1f;
        else cameraTurn[1] = 0f;
    }
    public void turnUp(Boolean move) {
        if (move) cameraTurn[0] = 1f;
        else cameraTurn[0] = 0f;
    }
    public void turnDown(Boolean move) {
        if (move) cameraTurn[0] = -1f;
        else cameraTurn[0] = 0f;
    }
    
    /**
     * ture camera on the z axis
     * @param move
     */
    public void turnZUp(Boolean move) {
        if (move) cameraTurn[2] = 1f;
        else cameraTurn[2] = 0f;
    }
    public void turnZDown(Boolean move) {
        if (move) cameraTurn[2] = -1f;
        else cameraTurn[2] = 0f;
    }
    
    /**
     * do jump
     * @param jump
     */
    public void doJump() {
    	if (jumpState == PlayerState.WALKING) {
    		jumpState = PlayerState.JUMPING_UP;
    	}
    }
    
    /**
     * do the jump according to the current eyey and the 
     * jump limit, when we reach the limit, stay on top and then go back down
     * @param eyey
     * @return
     */
    private float jump(float eyey) {
    	float jumpValue = 0f;
    	float jumpParam = 0.05f;
    	
    	//if we are jumping up
    	if (jumpState == PlayerState.JUMPING_UP) {
    		jumpValue = jumpParam;
    	
    		//if we reach the limit y of the jump
    		if(eyey + jumpValue > jumpLimit) 
    		{ jumpState = PlayerState.JUMPING_TOP; }

    	//if we reach the top, next go down
    	} else if(jumpState == PlayerState.JUMPING_TOP){
    		jumpState = PlayerState.JUMPING_DOWN; 
    		
    	// if we are jumping down	
    	} else if (jumpState == PlayerState.JUMPING_DOWN) {
    		jumpValue = -jumpParam;
    		
    		//if we are close to the floor
    		if(eyey + jumpValue + 0.05f < playerFloor) { jumpState = PlayerState.WALKING; }
    	}
    	return jumpValue;
    }
    
    /**
     * draw object in 2d
     * @param gl
     * @param obj
     */
    public void drawHUD(GL2 gl, ArrayList<HUD> objDraw2D2) {
    	
    	//set to ortho matrix to draw in 2d
    	gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    	gl.glPushMatrix();
    	gl.glLoadIdentity();
    	gl.glOrtho(-0.5f, 10f, -10f, 0.5f, -1f, 1f);
    	gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    	gl.glPushMatrix();
    	gl.glLoadIdentity();
    	
    	//draw the objects that we want to draw in 2d
    	for (HUD obj : objDraw2D2) {
    		obj.draw2D(gl);
    	}
    	
    	//return the PROJECTION matrix and then to vm
    	gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
    	gl.glPopMatrix();
    	gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    	gl.glPopMatrix();
    }
    
    /**
     * if we finish level one, load the next level maze
     */
    public void setLevelTwo() {    	
        //Load the maze from the text file
    	levelDraw = 2;
    	currentLevel = 2;
    	reachEndKey = 0;
        
    	if(objDraw2D.contains(key1)) {
    		objDraw2D.remove(key1);
    	}
    	if(objDraw2D.contains(key2)) {
    		objDraw2D.remove(key2);
    	}
    	if(objDraw2D.contains(details)) {
    		objDraw2D.remove(details);
    	}
    	
        objDraw2D.add(levelTwoTitle);
        
        //delete title after 1500 milisec
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                    	objDraw2D.remove(levelTwoTitle);
                    }
                }, 
                1500 
        );
    }
    
    /**
     * restart the game
     */
    public void restartGame() {
    	levelDraw = currentLevel;
    	reachEndKey = 0;
    	if(objDraw2D.contains(key1)) {
    		objDraw2D.remove(key1);
    	}
    	if(objDraw2D.contains(key2)) {
    		objDraw2D.remove(key2);
    	}
    	if(objDraw2D.contains(details)) {
    		objDraw2D.remove(details);
    	}
    	//if the player in stage 1 , he player the game again and he gain full lives
		healthBar.setHealth();
		finishGame = false;
    }
    
    private void defete() {
    	finishGame = true;
    	objDraw2D.add(gameOver);
    	
    	//delete title after 1000 milisec and start new game
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                    	objDraw2D.remove(gameOver);
                    	restartGame();
                    }
                }, 
                1000 
        );
    }
    
    /**
     * show the game details
     */
    public void showGameDetails () {
    	if (!objDraw2D.contains(details)) {
    		objDraw2D.add(details);
    	} else {
    		objDraw2D.remove(details);
    	}
    }
}
