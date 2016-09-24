package nebulous.main;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import nebulous.graphics.Mesh;
import nebulous.graphics.Shader;
import nebulous.utils.BufferUtilities;

public class GameMain extends Game{

	private static final int width = 1366;
	private static final int height = 768;
	private static final String title = "Nebulous2D Game Engine";

	public static GameMain game = new GameMain();
	
	public Shader shader;
	public Mesh mesh;
	
	public static void main(String[] args) {
		game.getWindow().vSync(false);
		game.start();
	}
	
	public GameMain() {
		super(width, height, title);
	}

	@Override
	public void init() {
		shader = new Shader("/shaders/shader.vs", "/shaders/shader.fs");
		
		float[] vertices = new float[]{
		        -0.5f,  0.5f, 0.0f,
		        -0.5f, -0.5f, 0.0f,
		         0.5f, -0.5f, 0.0f,
		         0.5f,  0.5f, 0.0f,
		    };
		
		mesh = new Mesh(vertices);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render() {
	    shader.bind();
	    
	    mesh.render();
	    
	    shader.unbind();
	}

}