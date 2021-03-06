package me.mrCookieSlime.QuestWorld;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.mrCookieSlime.QuestWorld.api.QuestExtension;
import me.mrCookieSlime.QuestWorld.util.Log;

public class ExtensionLoader {
	private ClassLoader loader;
	private File folder;
	
	public ExtensionLoader(ClassLoader loader, File folder) {
		this.loader = loader;
		this.folder = folder;
	}
	
	public void loadLocal() {
		// This is as much as bukkit checks, good enough for me!
		File[] extensions = folder.listFiles((file, name) -> name.endsWith(".jar"));
		
		// Not a directory or unable to list files for some reason
		if(extensions != null)
			for(File f : extensions)
				load(f);
	}
	
	public void load(File extensionFile) {
		Log.fine("Loader - Reading file: " + extensionFile.getName());
		URL[] jarURLs = {null};
		try { jarURLs[0] = extensionFile.toURI().toURL(); }
		catch (Exception e) { e.printStackTrace(); return; }
		
		URLClassLoader newLoader = URLClassLoader.newInstance(jarURLs, loader);
		
		JarFile jar;
		try { jar = new JarFile(extensionFile); }
		catch (Exception e) {
			Log.severe("Failed to load \""+extensionFile+"\": is it a valid jar file?");
			e.printStackTrace();
			return;
		}
		
		Enumeration<JarEntry> entries = jar.entries();
		ArrayList<Class<?>> extensionClasses = new ArrayList<>();
		
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if(entry.isDirectory() || !entry.getName().endsWith(".class"))
				continue;
			
			String className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
			Log.finer("Loader - Loading class: " + className);
			Class<?> clazz;
			try { clazz = newLoader.loadClass(className); }
			catch (ClassNotFoundException e) {
				Log.severe("Could not load class \""+className+"\"!");
				e.printStackTrace();
				continue;
			}

			if(QuestExtension.class.isAssignableFrom(clazz)) {
				Log.finer("Loader - Found extension class: " + className);
				extensionClasses.add(clazz);
			}
		}
		
		for(Class<?> extensionClass : extensionClasses) {
			Log.fine("Loader - Constructing: " + extensionClass.getName());
			try { extensionClass.getConstructor().newInstance(); }
			catch (Throwable e) {
				Log.severe("Exception while constructing extension class \""+extensionClass+"\"!");
				Log.severe("Is it missing a default constructor?");
				e.printStackTrace();
			}
		}
		
		try { jar.close(); }
		catch (Exception e) { e.printStackTrace(); }
	}
}
