package nebulous.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import nebulous.graphics.Window;
import nebulous.utils.Console;
import nebulous.utils.Time;

public abstract class Game{
	
	private String title = "Nebulous2D Game Engine";
	private int width = 1366;
	private int height = 768;
	private Window window = null;
	
	private double updateSpeed = 1.0 / 60;
	
	public Game(int width, int height, String title) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.window = createWindow();
	}
	
	public void start(){
		tick();
	}
	
	public void stop(){
		glfwTerminate();
		Console.println("Game exited with game.stop()");
		System.exit(0);
	}
	
	private void tick(){
		init();
		
		int frames = 0;
        double frameCounter = 0;
		
		double lastTime = Time.getTime();
		double unprocessedTime = 0;

		while(!glfwWindowShouldClose(window.getWindowID())){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			double startTime = Time.getTime();
            double pastTime = startTime - lastTime;
			lastTime = startTime;
			unprocessedTime += pastTime;
			frameCounter += pastTime;
			
			while(unprocessedTime > updateSpeed){
				unprocessedTime -= updateSpeed;
				
				update();
				
				if(frameCounter >= 1.0){
					Console.println("FPS: " + frames);
					frameCounter = 0;
					frames = 0;
				}
			}
			
			render();
			glfwSwapBuffers(window.getWindowID());
			glfwPollEvents();
			frames++;
			
		}
	}

	public abstract void init();
	
	public abstract void update();
	
	public abstract void render();
	
	private Window createWindow(){
		Window window = new Window().createWindow(width, height, title);
		window.setVisable(true);
		return window;
	}
	
	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Window getWindow() {
		return window;
	}

	public double getFramecap() {
		return updateSpeed;
	}

}