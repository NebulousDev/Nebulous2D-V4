package nebulous.graphics.shaders;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import nebulous.utils.Console;

public abstract class Shader{
	
	private int programID;
	private int vertexID;
	private int fragmentID;
	private final Map<String, Integer> uniforms;
	
	public Shader(String vertexFile, String fragmentFile){
		this.programID = glCreateProgram();					//TODO: add error support
		this.vertexID = attachVertex(vertexFile);
		this.fragmentID = attachFragment(fragmentFile);
		this.uniforms = new HashMap<String, Integer>();
		linkAndCompile();
		//addUniform("projectionMatrix");
	}
	
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
	
	public abstract void addUniforms();
	public abstract void updateUniforms();
	
	public void addUniform(String name){
		int uniform = glGetUniformLocation(programID, name);	//TODO: add error support
		if (uniform < 0) new Exception ("Could not find uniform: " + name).printStackTrace();;
		uniforms.put(name, uniform);
	}
	
	public void setUniform1f(int location, float data){
		glUniform1f(location, data);
	}
	
	public void setUniform3f(int location, Vector3f data){
		glUniform3f(location, data.x, data.y, data.x);
	}
	
	public void setUniform1b(int location, boolean value){
		glUniform1i(location, value ? 1 : 0);
	}
	
	public void setUniformMat4f(int location, Matrix4f matrix4f){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer = matrix4f.get(buffer);
		glUniformMatrix4fv(location, true, buffer);
	}
	
	public int getUniform(String name){
		return glGetUniformLocation(programID, name);
	}
	
	public int attachVertex(String vertex){
		String vertexData = loadShader(vertex);
		int shaderID = glCreateShader(GL_VERTEX_SHADER);	//TODO: add error support
		glShaderSource(shaderID, vertexData);				//TODO: add error support
		glCompileShader(shaderID);							//TODO: add error support
		glAttachShader(programID, shaderID);				//TODO: add error support
		return shaderID;
	}
	
	public int attachFragment(String fragment){
		String fragmentData = loadShader(fragment);
		int shaderID = glCreateShader(GL_FRAGMENT_SHADER);	//TODO: add error support
		glShaderSource(shaderID, fragmentData);				//TODO: add error support
		glCompileShader(shaderID);							//TODO: add error support
		glAttachShader(programID, shaderID);				//TODO: add error support
		return shaderID;
	}
	
	public void linkAndCompile(){
		glLinkProgram(programID);							//TODO: add error support
		glValidateProgram(programID);						//TODO: add error support
	}	
	
	public void bind(){
		glUseProgram(programID);							//TODO: add error support
	}

	public void unbind(){
		glUseProgram(0);									//TODO: add error support
	}
	
	public void remove(){
		unbind();
		glDetachShader(programID, vertexID);				//TODO: add error support
		glDetachShader(programID, fragmentID);				//TODO: add error support
		glDeleteProgram(programID);							//TODO: add error support
	}

}
