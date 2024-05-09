package net.nicksneurons.engine.framework.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PluginInfo", namespace = "http://fractaldungeon.com")
@XmlRootElement(name = "PluginInfo", namespace = "http://fractaldungeon.com")
public class PluginInfo {

    private String author;
    private String pluginName;
    private String binaryName;

    @XmlAttribute
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @XmlAttribute
    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    @XmlAttribute(required=true)
    public String getBinaryName() {
        return binaryName;
    }

    public void setBinaryName(String pluginBinaryName) {
        this.binaryName = pluginBinaryName;
    }
}
