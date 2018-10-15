//Inbar Demuth 204885370
//Yakir Pinchas 203200530
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;

public class HealthBar implements HUD {
	private int health = 20;
	
	public HealthBar() {}
	
	
	/**
	 * this draw is in 2D!
	 * max hp 100 = 20 pieces.
	 * will be draw on the left top of the screen
	 * @param gl
	 */
	@Override
	public void draw2D(GL2 gl) {
		
		//the separating space
		float sep = 0.03f;
		
		//hp piece width
		int numDiv = 12;
		float barWidth = 1.0f/(float)numDiv;
		
		//set the counter to draw the hp and the x value
		int hpCounter = 0;
		float i = -0.08f;
		
		//Change the colouring for this particular object material
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{1.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.5f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{60.0f}, 0);

        gl.glColor3f(1.0f, 0.0f, 0.0f);
		
		gl.glBegin(GL2GL3.GL_QUADS);
		
		//draw all the hp pieces
		while (hpCounter < health) {
			
			gl.glColor3d(1,0,0);
			gl.glVertex2f(i, 0.1f);
			gl.glColor3d(1,1,0);
			gl.glVertex2f(i, -0.3f);
			gl.glColor3d(1,1,1);
			gl.glVertex2f(i + barWidth, -0.3f);
			gl.glColor3d(0,1,1);
		    gl.glVertex2f(i + barWidth, 0.1f);
		    
			i += (sep + barWidth);
			hpCounter++;
		}
		gl.glEnd();
		
		//Put the material properties back to default
		gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_EMISSION, new float[]{0.0f, 0.0f, 0.0f, 1.0f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SHININESS, new float[]{50.0f}, 0);
	}
	
	/**
	 * update the health point if got damage
	 * @param dmg
	 */
	public void decreaseHP(int dmg) {
		if (health > 0) {
			health -= dmg;
			if(health < 0) {
				health = 0;
			}
		}
	}
	public void increaseHP(int hp) {
		if (health < 20) {
			health += hp;
			if (health > 20) {
				health = 20;
			}
		}	
	}
	public void setHealth() {
		health = 20;
	}
	public int getHealth() {
		return health;
	}
}
