package net.netne.platinumcoding.ini;

import java.io.*;

/**
 * This class is used for changing the value of an {@link INIElement} and saving
 * it to a file.
 * <br /><br />
 * If you want to modify the values of an individual element, then you would just
 * call the method {@link #setValue(Object)} after you set the element to be written
 * to by either the constructor ({@link INIElementWriter(INIElement)}) or the
 * set method ({@link #setElement(INIElement)}).
 * <br /><br />
 * To change the value of an element and have it saved to the file, you just call
 * the set method and it is changed.
 * <br /><br />
 * The following text is content of the file "Example.ini", which is used in
 * all of the examples:
 * <br /><br />
 * <pre>
 * {@code 
 * ; Window properties
 * [Window]
 * Width=640
 * Height=480
 * 
 * ; Render properties
 * [Properties]
 * vSync=true
 * DoubleBuffer=true
 * }
 * </pre>
 * <pre>
 * {@code
 * // ...
 * 
 * // Creates a reader to get the INIElements
 * INIReader reader = new INIReader(new java.io.File("Example.ini"));
 * 
 * // Gets the elements for modifying
 * INIElement width = reader.get(0);
 * INIElement height = reader.get(1);
 * 
 * // Creates an element writer
 * INIElementWriter writer = new INIElementWriter(element);
 * 
 * // Proof of the value with a before-and-after
 * System.out.println(element.getKey() + " = " + element.getValue());
 * 
 * // Changing the value
 * writer.setValue(480);
 * 
 * // The after
 * System.out.println(element.getKey() + " = " + element.getValue());
 * 
 * // Instead of having to instance a new INIElementWriter, which would also work,
 * // you can use the .setElement(INIElement) method to change which element is
 * // being written to.
 * writer.setElement(height);
 * 
 * // More before-and-after
 * System.out.println(height.getKey() + " = " + height.getValue());
 * writer.setValue(640);
 * System.out.println(height.getKey() + " = " + height.getValue());
 * 
 * // ...
 * }
 * </pre>
 * 
 * Which would output the following:
 * 
 * <pre>
 * {@code 
 * Width = 640
 * Width = 480
 * Height = 480
 * Height = 640
 * }
 * </pre>
 * 
 * @author Dealer Next Door
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
				
				if (string.equals("") || string.charAt(0) == ';' || string.charAt(0) == '#')
					continue;
				
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
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the element that the writer is modifying.
	 * 
	 * @param e 
	 */
	public void setElement(INIElement e) {
		this.element = e;
	}
	
	/**
	 * Returns the current element that is being written to.
	 * 
	 * @return The element that is being written to the file
	 */
	public INIElement getElement() {
		return this.element;
	}
}