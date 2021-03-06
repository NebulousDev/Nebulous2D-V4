#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;

out vec3 vColor;

uniform mat4 transform;
uniform mat4 perspective;

void main()
{
    gl_Position = perspective * transform * vec4(position, 1.0);
	vColor = color;
}