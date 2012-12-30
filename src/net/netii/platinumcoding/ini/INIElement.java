package net.netii.platinumcoding.ini;

import java.io.*;

/**
 * This class is used to represent an element within an INI file.
 * <br /><br />
 * An important thing to remember when using the INIElement set methods is this:
 * it does <b>NOT</b> write the changes to the file! The only time the set methods
 * of this class should be called is when it is being prepared for an {@link INIElementWriter}
 * to be written to a file.
 * 
 * @author Dealer Next Door
 */
public class INIElement {
	/**
	 * The INI file that this element belongs to
	 */
	File iniFile;
	
	/**
	 * The value of this element
	 */
	Object value;
	
	/**
	 * The category that this element is under and the identify (key)
	 */
	String category, key;
	
	/**
	 * Initializing the element of an INI file
	 * 
	 * @param f The INI file that the element is located in
	 * @param c The category that the element is in
	 * @param k The key that the element stores its value at
	 * @param v The value of the key
	 */
	public INIElement(File f, String c, String k, Object v) {
		this.iniFile = f;
		this.category = c;
		this.key = k;
		this.value = v;
	}
	
	/**
	 * <b>DOES NOT WRITE TO FILE</b>
	 * Sets the INI file that this element belongs to. This method should only
	 * be called by the INIWriter.
	 * 
	 * @param file The INI file
	 */
	public void setINIFile(File file) {
		this.iniFile = file;
	}
	
	/**
	 * <b>DOES NOT WRITE TO FILE</b>
	 * Sets the category that the element is under. This method should only be
	 * called by the INIWriter.
	 * 
	 * @param category The category that the INIElement belongs to
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * <b>DOES NOT WRITE TO FILE</b>
	 * Sets the key of the element. This method should only
	 * be called by the INIWriter.
	 * 
	 * @param key The key of the element within the INI file
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * <b>DOES NOT WRITE TO FILE</b>
	 * Sets the value of the object. This method should only be called by
	 * the INIWriter.
	 * 
	 * @param obj The value of the element
	 */
	public void setValue(Object obj) {
		this.value = obj;
	}
	
	/**
	 * Gets the INI file that the element belongs to.
	 * @return The INI file that the element is held in
	 */
	public File getINIFile() {
		return this.iniFile;
	}
	
	/**
	 * Gets the category that the element is under.
	 * @return The category of the element
	 */
	public String getCategory() {
		return this.category;
	}
	
	/**
	 * Gets the key that the element is recorded as.
	 * @return The key of the element.
	 */
	public String getKey() {
		return this.key;
	}
	
	/**
	 * Gets the value of the element.
	 * @return The value of the element.
	 */
	public Object getValue() {
		return this.value;
	}
}
