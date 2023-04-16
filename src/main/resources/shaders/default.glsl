#type vertex
#version 330 core

layout (location = 0) in vec3 attributePosition;
layout (location = 1) in vec4 attributeColor;
layout (location = 2) in vec2 attributeTextureCoordinates;

uniform mat4 uniformProjection;
uniform mat4 uniformView;

out vec4 fragmentColor;
out vec2 fragmentTextureCoordinates;

void main(void) {
    fragmentColor = attributeColor;
    fragmentTextureCoordinates = attributeTextureCoordinates;
    gl_Position = uniformProjection * uniformView * vec4(attributePosition, 1.0);
}

#type fragment
#version 330 core

uniform float uniformTime;
uniform sampler2D TEXTURE_SAMPLER;

in vec4 fragmentColor;
in vec2 fragmentTextureCoordinates;

out vec4 color;

void main(void) {
    color = texture(TEXTURE_SAMPLER, fragmentTextureCoordinates);
}
