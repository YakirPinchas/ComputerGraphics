//Inbar Demuth 204885370
//Yakir Pinchas 203200530
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * this class draw an image in 2d given list of vertices
 * screen limit in 2d define to be: {top left {-0.5f, 0.5f}, top right{-0.5f, -10f},
 * 									bottom right {10f, -10f}, bottom left{10f, 0.5f}}
 * @author Inbar Demuth
 *
 */
public class Image implements HUD {
	
	private String fileName;
	private float[][] vertices;
	private Texture texture;
	
	public Image (String imageFileName, float[][] ImageVertices) {
		fileName = imageFileName;
		vertices = ImageVertices;
	}
	
	public void loadImage() {
		try {
    		texture = TextureIO.newTexture(new File(fileName),true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
	}

	@Override
	public void draw2D(GL2 gl) {
		
	    //Change the colouring for this particular object material
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{60.0f}, 0);
	  
	    //set the texture to the image
	    gl.glTexParameteri ( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP );
	    gl.glTexParameteri( GL.GL_TEXTURE_2D,GL.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP );
	    gl.glActiveTexture(GL2.GL_TEXTURE0);
	    texture.enable(gl);
	    texture.bind(gl);
	          
	    //Draw a quad and set the texture
	    gl.glBegin(GL2GL3.GL_QUADS);
	    
	    gl.glTexCoord2f(0f, 1f); 
	    gl.glVertex2f(vertices[0][0], vertices[0][1]);
	    gl.glTexCoord2f(0f, 0f); 
	    gl.glVertex2f(vertices[1][0], vertices[1][1]);
	    gl.glTexCoord2f(1f, 0f); 
	    gl.glVertex2f(vertices[2][0], vertices[2][1]);
	    gl.glTexCoord2f(1f, 1f); 
	    gl.glVertex2f(vertices[3][0], vertices[3][1]);
	    
	    gl.glEnd();
	    
	    texture.disable(gl);
	  
	    //Put the material properties back to default
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
	    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);
	    
	  		
	}

}
