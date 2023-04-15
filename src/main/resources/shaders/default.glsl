#type vertex
#version 330 core

layout (location = 0) in vec3 attributePosition;
layout (location = 1) in vec4 attributeColor;

uniform mat4 uniformProjection;
uniform mat4 uniformView;

out vec4 fragmentColor;

void main(void) {
    fragmentColor = attributeColor;
    gl_Position = uniformProjection * uniformView * vec4(attributePosition, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;

out vec4 color;

void main(void) {
    color = fragmentColor;
}
