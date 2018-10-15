import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

/**
 * MazeObject, class representing an end point in the maze
 *
 */
public class MazeEndPoint extends MazeObject {
	private float yrot = 0;
    public enum EndPointType {
        START,
        MIDDLE,
        WRONG,
        BOOM,
        END
    }

    //The variable that decides if it's a start point or an end point
    private EndPointType type;

    public MazeEndPoint(EndPointType type) {
        this.type = type;
    }
    public EndPointType getPointType() {
    	return type;
    }
    //The function to check if it's the start position
    public boolean isStart() {
        if (type == EndPointType.START) return true;
        return false;
    }

    public void draw(GL2 gl) {

        GLUT glut = new GLUT();

        //Transform the to-be-placed geometric shapes
        gl.glPushMatrix();
        gl.glRotatef(90, -1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, -0.35f);

        //Check if it's a start point
        if (type == EndPointType.START) {

            //Place a glowing yellow cone

            //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.5f, 0.5f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{60.0f}, 0);

            gl.glColor3f(1.0f, 1.0f, 0.0f);
            glut.glutSolidCone(0.25f, 0.5f, 10, 10);
          //Check if it's a end point
        } else if(type == EndPointType.END) {

            //Place a shiny green torus

            //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.0f, 0.2f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.1f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{20.0f}, 0);

            gl.glColor3f(0.0f, 1.0f, 0.0f);
            glut.glutSolidTorus(0.125f, 0.25f, 10, 10);
          //Check if it's a MIDDLE point
        } else if (type == EndPointType.MIDDLE) {

            //Place a shiny green torus

            //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.0f, 0.2f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.1f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{20.0f}, 0);

            gl.glRotatef(yrot, 0.0f, 0.0f, 1.0f);
            yrot = yrot + 0.5f;
            
            gl.glColor3f(15.0f, 0.0f, 20.0f);
            glut.glutSolidCube(0.23f);
          //Check if it's a WRONG point
        } else if (type == EndPointType.WRONG)  {

            //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.0f, 0.2f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.1f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{20.0f}, 0);

            gl.glRotatef(yrot, 1.0f, 0.0f, 0.0f);
            yrot = yrot + 0.5f;
            
            gl.glColor3f(1.0f, 1.0f, 0.0f);
            glut.glutSolidSphere(0.125f, 5, 5);
        } else {
        	//BOOM POINT
        	 //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.0f, 0.2f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.1f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{20.0f}, 0);

            //gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
            yrot = yrot + 0.5f;
            
            gl.glColor3f(1.0f, 1.0f, 20.0f);
            glut.glutSolidCylinder(0.125f, 0.125f, 10, 10);;
        }

        //Put the material properties back to default
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);

        gl.glPopMatrix();
    }
}
