package net.netii.platinumcoding.ini;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Dealer Next Door
 */
public class INIFileWriter {
	/**
	 * The file to write the configuration to.
	 */
	private File iniFile;
	
	/**
	 * Creates an INIFileWriter that will write elements and categories to the
	 * specified file.
	 * 
	 * @param file The file to be written to.
	 */
	public INIFileWriter(File file) {
		this.iniFile = file;
	}
	
	/**
	 * Adds a category to the current INI file.
	 * 
	 * @param category The category to be added to the file
	 */
	public void addCategory(String category) {
		if (iniFile == null) return;
		
		// Checking to see if the file already contains the desired category
		boolean containsCategory = false;
		String fileContent = "";
		String lastLine = "";
		String string;
		
		try (BufferedReader in = new BufferedReader(new FileReader(iniFile))) {
			while ( (string = in.readLine()) != null) {
				lastLine = string;
				fileContent += string;
				fileContent += System.getProperty("line.separator");
				
				// Line is a comment or is empty, ignoring
				if (!string.equals(""))
					if (string.charAt(0) == ';' || string.charAt(0) == '#')
						continue;
				
				// Line is a category, checking if it is the category that is
				// about to be created
				if (!string.equals("") && string.charAt(0) == '[') {
					String thisCategory = string.substring(1, string.length() - 1);
					if (thisCategory.equals(category))
						containsCategory = true;
				}
			}
		} catch (IOException e) {}
		
		// The file does not contain the category, so appeanding it to the end
		// of the file.
		if (!containsCategory) {
			try (BufferedWriter out = new BufferedWriter(new FileWriter(iniFile))) {
				// Overwritting the old file content with the correct content,
				// which has a proper newline at the end of the file.
				out.write(fileContent);
				out.newLine();
				out.write('[' + category + ']');
				out.close();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Adds an element to the desired category.
	 * 
	 * @param category	The desired category to add the element to
	 * @param key		The key that will represent the element
	 * @param value		The value of the element
	 */
	public void addElement(String category, String key, Object value) {
		this.addElement(new INIElement(this.iniFile, category, key, value));
	}
	
	/**
	 * Adds an element to the desired category.
	 * <i>Note: The element's INI file will be overwritten to the INIFileWriter's
	 * INI file</i>
	 * 
	 * @param element An element containing a category, key, and a value. 
	 */
	public void addElement(INIElement element) {
		element.setINIFile(this.iniFile);
		
		try(BufferedReader in = new BufferedReader(new FileReader(iniFile))) {
			File temp = new File("temp.ini");
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			String string;
			
			boolean hasFoundCategory = false;
			boolean keyExists = false;
			
			while ((string = in.readLine()) != null) {
				String newline = string;
				
				if (!hasFoundCategory) {
					// Line is a category, checking if it is the specified one...
					if (!string.equals("") && string.charAt(0) == '[') {
						String thisCategory = string.substring(1, string.length() - 1);
						if (thisCategory.equals(element.getCategory()))
							hasFoundCategory = true;
					}
				} else {
					// New category has been reached, stopping loop.
					if (!string.equals("") && string.charAt(0) == '[')
						break;
					
					// Checking if line is an element
					if (!string.equals("") && string.contains("=")) {
						int pos = string.indexOf("=");
						String key = string.substring(0, pos);
						
						if (key.equals(element.getKey()))
							keyExists = true;
					}
				}
				
				out.write(newline);
				out.newLine();
			}
			
			if (!hasFoundCategory)
				System.out.println("WARNING: Category " + element.getCategory() + " is not in the INI file");
			else
				// This only happens when adding a category to the end of the file
				// and there is no elements within that category. (A fix is just
				// adding two new lines after a category at the end of the file,
				// but this is just to save us any issues.
				
				if (!keyExists) {
					out.write(element.getKey() + '=' + element.getValue());
					out.newLine();
				}
			
			in.close();
			out.close();
			
			element.getINIFile().delete();
			temp.renameTo(element.getINIFile());
		} catch(IOException e) {}
	}
}