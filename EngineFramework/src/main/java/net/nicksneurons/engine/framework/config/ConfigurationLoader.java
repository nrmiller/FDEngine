package net.nicksneurons.engine.framework.config;

import net.nicksneurons.tools.IOUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * TODO Document
 * Used checked exception when the client can handle the exception.
 * Use unchecked exception when the client cannot handle the exception because it is a programming error.
 */
public class ConfigurationLoader extends ClassLoader {

    private String jarFile;

    public ConfigurationLoader(String jarFile) {
        super("ConfigurationPluginClassLoader", ConfigurationLoader.class.getClassLoader());

        if (jarFile == null) throw new IllegalArgumentException("The argument \'jarFile\' cannot be null.");
        this.jarFile = jarFile;
    }

    public String getJarFile() {
        return jarFile;
    }

    @Override
    protected Class<?> findClass(String binaryName) throws ClassNotFoundException {
        //This class loader is primarily used to find the plugin class, and it will be able
        //to locate classes referenced by it. Since all referenced classes will be found earlier
        //up the call chain, this function is responsible for loading the root plugin class.
        try {
            //For example: Engine --> com/fractaldungeon/engine/Engine.class
            String entryPath = binaryName.replace('.', '/').concat(".class");
            byte[] classData = getJarData(jarFile, entryPath);
            if (classData != null) {
                Class<?> clazz = defineClass(binaryName, classData, 0, classData.length);
                return clazz;
            } else {
                //Passing null directly to defineClass generates a null-pointer exception inside native code,
                //so we must recreate it here as the cause.
                NullPointerException cause = new NullPointerException(String.format("The class \'%s\' does not exist in the jar.", binaryName));
                throw new ClassNotFoundException(binaryName, cause);
            }
        } catch(IOException e) {
            throw new ClassNotFoundException(String.format("Exception while reading the class \'%s\' from the plugin jar.", binaryName), e);
        }
    }

    /**
     * TODO Document.
     * @return
     * @throws PluginLoadException
     */
    public ConfigurationPlugin loadConfiguration() throws PluginLoadException {
        String binaryName = null;
        try {
            //Get the binary name. This is specified by the plugin's configuration.
            binaryName = getBinaryName();

            Class<?> clazz = loadClass(binaryName);
            ConfigurationPlugin plugin = (ConfigurationPlugin)clazz.getDeclaredConstructor().newInstance();
            return plugin;
        } catch(IOException e) {
            throw new PluginLoadException(String.format("Cannot obtain plugin class from the plugin \'%s\'.", jarFile), e);
        } catch(ClassNotFoundException e) {
            throw new PluginLoadException(String.format("Cannot find the plugin class \'%s\' inside plugin \'%s\'.", binaryName, jarFile), e);
        } catch(NoSuchMethodException|IllegalAccessException e) {
            throw new PluginLoadException(String.format("The plugin class \'%s\' inside plugin \'%s\' does not have a public, parameterless constructor.", binaryName, jarFile), e);
        } catch(InstantiationException e) {
            throw new PluginLoadException(String.format("The plugin class \'%s\' inside plugin \'%s\' cannot be instantiated because it is an abstract class.", binaryName, jarFile), e);
        } catch(InvocationTargetException e) {
            throw new PluginLoadException(String.format("The plugin class \'%s\' inside plugin \'%s\' cannot be instantiated because the constructor threw an exception.", binaryName, jarFile), e);
        }
    }

    /**
     * TODO Document
     * @return
     * @throws IOException
     */
    private String getBinaryName() throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            ZipEntry entry = jar.getEntry("Config.xml");
            if (entry != null) {
                //Open up Jar file entry for reading.
                try (InputStream instream = jar.getInputStream(entry)) {

                    JAXBContext context = null;
                    try {
                        context = JAXBContext.newInstance(PluginInfo.class);
                        Unmarshaller unmarshaller = context.createUnmarshaller();

                        PluginInfo info = (PluginInfo)unmarshaller.unmarshal(instream);
                        return info.getBinaryName();

                    } catch (JAXBException e) {
                        throw new IOException("Cannot deserialize Config.xml.", e);
                    }
                }
            } else {
                throw new IOException(String.format("Cannot find the entry 'Config.xml' in the jar \'%s\'.", jarFile));
            }
        }
    }

    /**
     * This class loads a Jar file entry into a byte array.
     * The resulting array has exactly the same size of the Jar file entry.
     * If the Jar file cannot be opened or if the entry cannot be read due
     * to the resource being unavailable, then an exception is thrown.
     * The entry must follow the format required by {@link JarFile#getEntry(String)}.
     *
     * @apiNote This function may be later moved to separate utilities class.
     *
     * @param jarPath The path of the Jar file. This path can be relative to
     *                the current working directory.
     * @param entryName The Jar file entry name.
     * @return A byte[] containing the contents of the file, or null if the entry
     * does not exist.
     * @throws IOException If the Jar file or its entry cannot be loaded.
     */
    private byte[] getJarData(String jarPath, String entryName) throws IOException {
        try (JarFile jar = new JarFile(jarPath)) {
            ZipEntry entry = jar.getEntry(entryName);
            if (entry != null) {
                //Open up Jar file entry for reading.
                try (InputStream instream = jar.getInputStream(entry)) {

                    byte[] entryData = IOUtil.readStream(instream);
                    return entryData;
                }
            } else {
                return null; //Entry does not exist.
            }
        }
    }
}
