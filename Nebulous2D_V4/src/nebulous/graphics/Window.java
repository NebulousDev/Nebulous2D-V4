package nebulous.graphics;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.Version;
import static org.lwjgl.opengl.GL11.*;

import nebulous.graphics.enums.VideoMode;
import nebulous.utils.Console;

public class Window {
	
	private String TITLE;
	private int WIDTH, HEIGHT;
	private VideoMode videoMode;
	private long windowID = 0;
	private boolean resized = false;
	private boolean fullscreen = false;
	private boolean vSync = false;
	private static Window context;

	public Window() {
		context = this;
	}
	
	public Window createWindow(int width, int height, String title){
		this.TITLE = title;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.videoMode = VideoMode.NORMAL;
		return this;
	}
	
	public static Window getContext(){
		return context;
	}
	
	public void init(){
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (glfwInit()){
			Console.println("GLFWInit() completed successfully...");
		} else {
			Console.printErr("GLFWInit() failed to complete successfully!");
			throw new IllegalStateException("ERROR: Unable to initialize GLFW!");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        if(videoMode == VideoMode.FULLSCREEN){
        	windowID = glfwCreateWindow(WIDTH, HEIGHT, TITLE, glfwGetPrimaryMonitor(), 0);
        	Console.println("Window Set Fullscreen: True");
        } else windowID = glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
        
		if(windowID == 0){ throw new RuntimeException("ERROR: Failed to create the GLFW window!");}
		
		GLFWWindowSizeCallback windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				Window.this.WIDTH = width;
				Window.this.HEIGHT = height;
				Window.this.resized = true;
			}
		}; 
		
		glfwSetWindowSizeCallback(windowID, windowSizeCallback);
		
		if(!fullscreen){
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	        glfwSetWindowPos(windowID,(vidmode.width() - WIDTH) / 2,(vidmode.height() - HEIGHT) / 2);
		}
		
		glfwMakeContextCurrent(windowID);
		
		if(vSync)glfwSwapInterval(1);
		else glfwSwapInterval(0);
		
		GL.createCapabilities();
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL_DEPTH_TEST);
		GL11.glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		printGLStats();
	}
	
	public boolean isResized(){
		return resized;
	}

	public void update(){
		if (resized) {
            glViewport(0, 0, WIDTH, HEIGHT);
            resized = false;
            Console.println("Window Resized: " + HEIGHT + ", " + WIDTH);
        }
	}
	
	public void render(){
		glfwSwapBuffers(windowID);
		glfwPollEvents();
	}
	
	public void vSync(boolean enable){
		if(enable) glfwSwapInterval(1);
		else glfwSwapInterval(0);
	}
	
	public void setVisable(boolean visable){
		if(visable) glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		else glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
	}
	
	public void setResizable(boolean resizable){
		if(resizable) glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		else glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
	}
	
	public void setVideoMode(VideoMode mode){
		this.videoMode = mode;
	}
	
	public void setVideoMode(VideoMode mode, int width, int height){
		this.WIDTH = width;
		this.HEIGHT = height;
		this.videoMode = mode;
	}
	
	public static void printGLStats(){
        Console.printMOTD(
        " OPENGL: " + glGetString(GL_VERSION) + "\n" +
        " LWJGL: " + Version.getVersion() + "\n" + 
        " GRAPHICS: " + glGetString(GL_RENDERER) + "\n" +
        " VENDORS: " + glGetString(GL_VENDOR) + "\n" +
        " OPERATING SYSTEM: " + System.getProperty("os.name") + "\n" +
        " JAVA VERSION: " + System.getProperty("java.version") + "\n" +
        " CURRENT DIRECTORY: \n" +
        " " + System.getProperty("user.dir")
        );
    }

	public String getTitle() {
		return TITLE;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public long getWindowID() {
		return windowID;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public boolean isVSync() {
		return vSync;
	}

	public void setVSync(boolean vSync) {
		this.vSync = vSync;
	}

}
