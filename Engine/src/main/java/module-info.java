module Engine {
    requires Tools;
    requires EngineFramework;
    requires kotlin.stdlib;
    exports net.nicksneurons.engine;
    exports net.nicksneurons.engine.components;
    exports net.nicksneurons.engine.systems;
}