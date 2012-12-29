package net.netii.platinumcoding.ini;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Dealer Next Door
 */
public class INIReader {
	/**
	 * The INI file that is being read from.
	 */
	private File iniFile;
	
	/**
	 * Creates an INIReader that reads from the specified file
	 * 
	 * @param ini The INI file that is being read from.
	 */
	public INIReader(File ini) {
		this.iniFile = ini;
	}
	
	/**
	 * Returns the total amount of headers within the INI file.
	 * EX:
	 *	<i>; Within the INI file (C:/example.ini)...</i>
	 *	[Person]
	 *		Name=Chauncey
	 *		Age=16
	 *	[Computer]
	 *		Name=DealerNextDoor
	 *		Age=1
	 * 
	 *	<i>// Java source code...</i>
	 *	INIReader reader = new INIReader(new java.io.File("C:/example.ini"));
	 *	System.out.println(reader.getSectionCount());
	 *	> Outputs 2
	 * 
	 *	@return	The total number of sections within the INI file
	 */
	public int getCategoryCount() {
		if (this.iniFile == null) return 0;
		int sections = 0;
		
		try(BufferedReader reader = new BufferedReader(new FileReader(this.iniFile))) {
			String string;
			
			while ( (string = reader.readLine()) != null) {
				// Ignoring comments and blank lines
				if (string.equals("") || string.charAt(0) == ';' || string.charAt(0) == '#')
					continue;
				
				// Category found
				if (string.charAt(0) == '[') {
					sections++;
				}
			}
			
			reader.close();
		} catch (IOException ex) {}
		
		return sections;
	}
	
	/**
	 * Returns an array list with all of the elements in a specific category.
	 * 
	 * @param category The specified category to look under
	 * @return An ArrayList of INIElements
	 */
	public ArrayList<INIElement> getElements(String category) {
		// All of the elements within a category will be here
		ArrayList<INIElement> elements = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(this.iniFile))) {
			boolean hasFoundCategory = false;
			String string;
			
			while ((string = reader.readLine()) != null) {
				if (!hasFoundCategory) {
					// Things to happen if the reader has yet to find the category
					// of the element
					
					// Comments and blank lines
					if (string.equals("") || string.charAt(0) == ';' || string.charAt(0) == '#')
						continue;
					// (Purposely broke apart two if statements to make it
					// look nicer)
					
					// Line is not an element, so ignoring
					if (string.charAt(0) != '[')
						continue;

					// The specific category on this line without the braces
					String thisCategory = string.substring(1, string.length() - 1);
					if (thisCategory.equals(category)) {
						hasFoundCategory = true;
					}
				} else {
					// Initializing the key and value to make sure no NPEs are thrown
					String key = "NULL";
					Object value = null;
					
					// A new category has been reached, therefore the reading should
					// be stopped
					if (string.charAt(0) == '[')
						break;
					
					// Breaking apart the line from the '=' character
					for (int i = 0; i < string.length(); i++) {
						if (string.charAt(i) == '=') {
							key = string.substring(0, i);
							value = string.substring(i + 1);
							break;
						}
					}
					
					// Creating the element
					INIElement element = new INIElement(this.iniFile, category, key, value);
					elements.add(element);
				}
					
			}
		} catch(IOException e) {
			
		}
		
		return elements;
	}
	
	/**
	 * Returns the INI file that is being read from.
	 * 
	 * @return The INI file that is being read
	 */
	public File getINIFile() {
		return this.iniFile;
	}
}
