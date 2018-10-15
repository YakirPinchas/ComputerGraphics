//Inbar Demuth 204885370
//Yakir Pinchas 203200530
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;


/**
 * MazeObject, class representing an end point in the maze
 *
 */
public class MazeCellObj extends MazeObject {
	private float yrot = 30;
	private boolean move = false;
	private float xtran = 0.0f;
	
	/**
	 * the type that we can have for an cell object
	 * @author Inbar Demuth
	 *
	 */
    public enum ObjType {
        START,
        HEAL,
        DAMAGE,
        ENDKEY,
        END,
        NONE
    }
    private Texture obj_texture;
    private WavefrontObjectLoader obj;
    
    //if to draw the obj or not
    boolean draw = true;

    //The variable that decides the type of the object
    private ObjType type;

    public MazeCellObj(ObjType type) {
        this.type = type;
        setObjects(type);
    }
    
    /**
     * set the object type
     * @return
     */
    public ObjType getObjType() {
    	return type;
    }
    
    //The function to check if it's the start position
    public boolean isStart() {
        if (type == ObjType.START) return true;
        return false;
    }
    
    /**
     * if we hit the obj and we dont want to see him again
     */
    public void stopDraw() {
    	draw = false;
    }

    public void draw(GL2 gl) {
    	
    	if (!draw) {
    		return;
    	}

        GLUT glut = new GLUT();

        //Transform the to-be-placed geometric shapes
        gl.glPushMatrix();
        gl.glRotatef(90, -1.0f, 0.0f, 0.0f);
        gl.glTranslatef(0.0f, 0.0f, -0.35f);

        //Check which object this and draw accordingly
        switch(type) {
        case START:
        	//Place a glowing yellow cone
            //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{1.0f, 0.9f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{1.0f, 0.8f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 0.8f, 0.2f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{40.0f}, 0);
            
            gl.glTranslatef(0.0f, 0.0f, 0.7f);
            gl.glRotatef(-180.0f, 0.0f, 1.0f, 0.0f);
            
            glut.glutSolidCone(0.18f, 0.29f, 15, 15);
        	break;
        	
        case END:
        	//Place a shiny green torus
            //Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.6f, 0.6f, 0.8f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.3f, 0.0f, 0.5f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);

            //gl.glColor3f(0.0f, 1.0f, 0.0f);
            
            gl.glRotatef(-80.0f, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(35.0f, 1.0f, 0.0f, 0.0f);
            gl.glTranslatef(0.3f, 0.0f, 0.0f);
            gl.glTranslatef(xtran, 0.0f, 0.0f);
            
            if (xtran > -0.0009f) move = false;
            if (xtran < -0.02) move = true;
            if (move) xtran += 0.001f;
            else xtran -= 0.001f;
            
            glut.glutSolidTorus(0.05f, 0.1f, 15, 15);
        	break;
        	
        case HEAL:
            //Change the colouring for this particular object material
        	gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.0f, 0.2f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.0f, 0.8f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.1f, 0.3f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 0.6f, 0.6f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);

            gl.glColor3f(0.0f, 1.0f, 1.0f);
            
            gl.glRotatef(yrot, 0.0f, 0.0f, 1.0f);
            gl.glTranslatef(0.0f, 0.0f, xtran);
            
            if (xtran > -0.0009f) move = false;
            if (xtran < -0.05) move = true;
            if (move) xtran += 0.001f;
            else xtran -= 0.001f;
            
            yrot += 2.0f;
            if (yrot > 390) {
            	yrot = 30;
            }
            
            glut.glutSolidCube(0.15f);
        	break;
        	
        case DAMAGE:
        	//Change the colouring for this particular object material
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{1.0f, 0.2f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1.0f, 0.2f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{1.0f, 0.2f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 0.1f, 0.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{60.0f}, 0);

            gl.glRotatef(yrot, 0.0f, 0.0f, 1.0f);
            gl.glTranslatef(0.0f, 0.0f, -0.19f);
            /*gl.glTranslatef(xtran, 0.0f, 0.0f);
            
            if (xtran > 0.4) move = false;
            if (xtran < -0.1) move = true;
            if (move) xtran += 0.02f;
            else xtran -= 0.02f;*/
             
            yrot += 3.5f;
            if (yrot > 390) {
            	yrot = 30;
            }
            
            //set the texture for the object
            //gl.glActiveTexture(GL2.GL_TEXTURE0);
    	    //obj_texture.enable(gl);
            //obj_texture.bind(gl);
            
            //gl.glScalef(50, 50, 50);
            //obj.drawModel(gl);
            //obj_texture.disable(gl);
            
            gl.glColor3f(1.0f, 0.7f, 0.0f);
            glut.glutSolidSphere(0.28f, 20, 5);
        	
        	break;
        	
        case ENDKEY:
        	//Change the colouring for this particular object material
        	gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
            gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{20.0f}, 0);

            gl.glColor3f(1.0f, 1.0f, 1.0f);
            //set the texture for the object
            gl.glActiveTexture(GL2.GL_TEXTURE0);
    	    obj_texture.enable(gl);
            obj_texture.bind(gl);
            
            yrot += 2.5f;
            if (yrot > 390) {
            	yrot = 30;
            }
            gl.glRotatef(-70.0f, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(yrot, 1.0f, 0.0f, 0.0f);
            gl.glScalef(0.02f, 0.02f, 0.02f);
            obj.drawModel(gl);
            obj_texture.disable(gl);
            
        	break;
		default:
			break;
        }

        //Put the material properties back to default
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);

        gl.glPopMatrix();
    }
    
    
    private void setObjects(ObjType type) {
    	
    	switch(type) {
    	case DAMAGE:/*
    		try {
        		obj_texture=TextureIO.newTexture(new File( "textures/axe.jpg" ),true);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
    		
            obj = new WavefrontObjectLoader("resources/axe_v1.obj");*/
    		break;
    		
    	case ENDKEY:
    		try {
        		obj_texture=TextureIO.newTexture(new File( "textures/keyB_tx.bmp" ),true);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            obj = new WavefrontObjectLoader("resources/Key_B_02.obj");
            
    		break;
		default:
			break;
    	}
        
    }
}
