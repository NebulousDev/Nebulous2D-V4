package nebulous.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import nebulous.utils.Console;

/**
 * Shader(String vertexFile, String fragmentFile)
 * <p>
 * An OpenGL shader used for graphics
 * <p>
 * @author NebulousDev
 *
 */
public class Shader{
	
	private int programID;
	
	private int vertexID;
	private int fragmentID;
	
	/**
	 * @param vertexFile - Vertex file location (vs / vsf).
	 * @param fragmentFile - Fragment file location (fs / fsf).
	 * <p>
	 * Constructor for the Shader class. Requires vertex and fragment
	 * files. 
	 * <p>
	 * @author NebulousDev
	 */
	
	public Shader(String vertexFile, String fragmentFile){
		this.programID = glCreateProgram();					//TODO: add error support
		this.vertexID = attachVertex(vertexFile);
		this.fragmentID = attachFragment(fragmentFile);
		linkAndCompile();
	}
	
	/**
	 * Loads a shader file then compiles it to a usable shader.
	 * <p>
	 * @param filename - File location of the shader.
	 * @return (String) - Returns a string usable for OpenGL.
	 */
	
	public static String loadShader(String filename){
		StringBuilder source = new StringBuilder();
		BufferedReader reader = null;
		final String INCLUDE_DIRECTIVE = "#include";
		
		try{
			InputStream input = Shader.class.getResourceAsStream(filename);
			reader = new BufferedReader(new InputStreamReader(input));
			String line;
			while((line = reader.readLine()) != null)
				if(line.startsWith(INCLUDE_DIRECTIVE)){
					source.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() -1)));
				} else {
					source.append(line).append("\n");
				}
			reader.close();
		} catch (Exception e){
			Console.printErr("ERROR LOADING SHADER : " + filename);
			e.printStackTrace();
		}
		
		return source.toString();
	}
	
	/**
	 * Creates a GL_VERTEX_SHADER and attaches to the program.
	 * <p>
	 * @param vertex - Vertex file location (vs / vsf).
	 * @return - (int) returns vertexID.
	 * <p>
	 * @author NebulousDev
	 */
	
	public int attachVertex(String vertex){
		String vertexData = loadShader(vertex);
		int shaderID = glCreateShader(GL_VERTEX_SHADER);	//TODO: add error support
		glShaderSource(shaderID, vertexData);				//TODO: add error support
		glCompileShader(shaderID);							//TODO: add error support
		glAttachShader(programID, shaderID);				//TODO: add error support
		return shaderID;
	}
	
	/**
	 * Creates a GL_FRAGMENT_SHADER and attaches to the program.
	 * <p>
	 * @param fragment - Fragment file location (fs / fsf).
	 * @return - (int) returns fragmentID.
	 * <p>
	 * @author NebulousDev
	 */
	
	public int attachFragment(String fragment){
		String fragmentData = loadShader(fragment);
		int shaderID = glCreateShader(GL_FRAGMENT_SHADER);	//TODO: add error support
		glShaderSource(shaderID, fragmentData);				//TODO: add error support
		glCompileShader(shaderID);							//TODO: add error support
		glAttachShader(programID, shaderID);				//TODO: add error support
		return shaderID;
	}
	
	/**
	 * Links and validates the shader prorgam.
	 * <p>
	 * @author NebulousDev
	 */
	
	public void linkAndCompile(){
		glLinkProgram(programID);							//TODO: add error support
		glValidateProgram(programID);						//TODO: add error support
	}	
	
	/**
	 * Binds thes shader to be rendered.
	 * <p>
	 * @author NebulousDev
	 */
	
	public void bind(){
		glUseProgram(programID);							//TODO: add error support
	}
	
	/**
	 * Unbinds the shader.
	 * <p>
	 * @author NebulousDev
	 */
	
	public void unbind(){
		glUseProgram(0);									//TODO: add error support
	}
	
	/**
	 * Unbinds and removes the shader
	 * <p>
	 * @author NebulousDev
	 */
	
	public void remove(){
		unbind();
		glDetachShader(programID, vertexID);				//TODO: add error support
		glDetachShader(programID, fragmentID);				//TODO: add error support
		glDeleteProgram(programID);							//TODO: add error support
	}

}
