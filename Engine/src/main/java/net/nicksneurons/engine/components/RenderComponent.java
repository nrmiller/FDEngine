package net.nicksneurons.engine.components;

import net.nicksneurons.engine.framework.Component;
import net.nicksneurons.engine.framework.data.Shader;

/**
 * TODO Document
 * This class contains render settings for an entity.
 * Sub-classes will further define what exactly is being rendered.
 *
 * TODO Unity uses a list of materials to support multi-pass rendering.
 * We should determine what is the best way to do this.
 */
public class RenderComponent extends Component {

    private Shader vertexShader;
    private Shader geometryShader;
    private Shader fragmentShader;
}
