package net.netii.platinumcoding.ini;

import java.io.*;

/**
 *
 * @author DealerNextDoor
 */
public class INIElementWriter {
	/**
	 * The element that is being written to.
	 */
	private INIElement element;
	
	/**
	 * Creates an INIElementWriter to write to the specified element.
	 * 
	 * @param e The element to be written to.
	 */
	public INIElementWriter(INIElement e) {
		this.element = e;
	}
	
	/**
	 * Sets the value of this specific INIElement and writes the value to the file
	 * 
	 * @param obj The desired value of this element
	 */
	public void setValue(Object obj) {
		if (this.element == null || this.element.getINIFile() == null) return;
		
		// Used to detect if it should write to the file or not
		boolean hasFoundCategory = false;
		File iniFile = element.getINIFile();
		
		try(BufferedReader in = new BufferedReader(new FileReader(iniFile))) {
			// Temp file for writing the changes to
			File tempFile = new File("temp.ini");
			BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
			String string;
			while ((string = in.readLine()) != null) {
				String newline = string;
				
				// Checking if it has found the category that this element is in
				if (!hasFoundCategory) {
					if (string.charAt(0) == '[') {
						// Getting the category name without the braces
						String fileCategory = string.substring(1, string.length() - 1);
						if (fileCategory.equals(element.getCategory())) {
							hasFoundCategory = true;
							// Found the category, can now write to the file
						}
					}
				} else {
					// The element is confirmed to be under this category, checking to override
					// the current value
					if (string.startsWith(element.getKey())) {
						newline = element.getKey() + "=" + obj.toString();
						element.setValue(obj);
					}
				}
				
				// Writes the new line, making it easier to modify
				out.write(newline);
				out.newLine();
			}
			
			in.close();
			out.close();
			
			element.getINIFile().delete();
			tempFile.renameTo(element.getINIFile());
		} catch(IOException e) {}
	}
}