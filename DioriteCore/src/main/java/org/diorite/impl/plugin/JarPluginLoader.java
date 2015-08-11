package org.diorite.impl.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import org.diorite.impl.CoreMain;
import org.diorite.impl.DioriteCore;
import org.diorite.plugin.BasePlugin;
import org.diorite.plugin.DioritePlugin;
import org.diorite.plugin.Plugin;
import org.diorite.plugin.PluginClassLoader;
import org.diorite.plugin.PluginDataBuilder;
import org.diorite.plugin.PluginException;
import org.diorite.plugin.PluginLoader;
import org.diorite.plugin.PluginNotFoundException;

public class JarPluginLoader implements PluginLoader
{
    @Override
    public DioritePlugin loadPlugin(final File file) throws PluginException
    {
        try
        {
            final PluginClassLoader classLoader = new PluginClassLoader(file);

            Class<?> mainClass = null;
            try
            {
                final String className = PluginManagerImpl.getCachedClass("jar|" + file.getName());
                if (className != null)
                {
                    mainClass = classLoader.findClass(className, false);
                    if (! DioritePlugin.class.isAssignableFrom(mainClass) || ! mainClass.isAnnotationPresent(Plugin.class))
                    {
                        mainClass = null;
                        CoreMain.debug("Cached main class for plugin: " + file.getPath() + " is invalid[2].");
                    }
                    else
                    {
                        CoreMain.debug("Cached main class for plugin: " + file.getPath() + " is " + mainClass.getName());
                    }
                }
                else
                {
                    CoreMain.debug("Can't find cached main class for plugin: " + file.getPath());
                }
            } catch (final ClassNotFoundException e)
            {
                CoreMain.debug("Cached main class for plugin: " + file.getPath() + " is invalid.");
            }

            if (mainClass == null)
            {
                final ConfigurationBuilder config = new ConfigurationBuilder();
                config.setClassLoaders(new PluginClassLoader[]{classLoader});
                config.setUrls(ClasspathHelper.forClassLoader(classLoader));

                final Reflections ref = new Reflections(config);
                final Set<Class<?>> annotated = ref.getTypesAnnotatedWith(Plugin.class);
                if (annotated.isEmpty())
                {
                    throw new PluginException("Plugin annotation wasn't found!");
                }
                if (annotated.size() > 1)
                {
                    throw new PluginException("Plugin has more than one main class!");
                }

                mainClass = annotated.iterator().next();

                if (! DioritePlugin.class.isAssignableFrom(mainClass))
                {
                    throw new PluginException("Main class must extend DioritePlugin!");
                }
            }
//            if (mainClass.isAnnotationPresent(CoreMod.class))
//            {
//                return DioriteCore.getInstance().getPluginManager().getPluginLoader(CoreJarPluginLoader.CORE_JAR_SUFFIX).loadPlugin(file);
//            }


            final DioritePlugin dioritePlugin = (DioritePlugin) mainClass.newInstance();
            final Plugin pluginDescription = mainClass.getAnnotation(Plugin.class);

            if (DioriteCore.getInstance().getPluginManager().getPlugin(pluginDescription.name()) != null)
            {
                throw new PluginException("Plugin " + pluginDescription.name() + " is arleady loaded!");
            }

            dioritePlugin.init(classLoader, this, new PluginDataBuilder(pluginDescription));
            dioritePlugin.getLogger().info("Loading " + pluginDescription.name() + " v" + pluginDescription.version() + " by " + pluginDescription.author() + " from file " + file.getName());
            dioritePlugin.onLoad();

            PluginManagerImpl.setCachedClass("jar|" + file.getName(), mainClass);
            return dioritePlugin;
        } catch (final InstantiationException | IllegalAccessException | MalformedURLException e)
        {
            throw new PluginException("Exception while loading plugin from file " + file.getName(), e);
        }
    }

    @Override
    public void enablePlugin(final BasePlugin plugin) throws PluginException
    {
        if (plugin == null)
        {
            throw new PluginNotFoundException();
        }
        plugin.setEnabled(true);
    }

    @Override
    public void disablePlugin(final BasePlugin plugin) throws PluginException
    {
        if (plugin == null)
        {
            throw new PluginNotFoundException();
        }
        plugin.setEnabled(false);
    }

    @Override
    public String getFileSuffix()
    {
        return ".jar";
    }
}
