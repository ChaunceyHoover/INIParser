package net.netne.platinumcoding.ini;

import java.io.*;
/**
 * This class is used for creating new categories and elements within a specific
 * INI file. If you want to modify existing elements, see {@link INIElementWriter}.
 * <br /><br />
 * When you want to create an element or a category within an INI file, you use
 * this class to do so. An example has been provided for every method of this class.
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
 * 
 * If you wanted to add another category to the file, or add an element to a
 * category, you would use the {@link #addCategory(String)} method to create a
 * new category, and {@link #addElement(String, String, Object)} to create an
 * element.
 * <br /><br />
 * <pre>
 * {@code 
 * // ...
 * 
 * // Initializes a writer
 * INIFileWriter writer = new INIFileWriter(new java.io.File("Example.ini"));
 * 
 * // Used for adding a new element to the file
 * INIElement element;	
 * 
 * // Successfully adds the new category "Color"
 * writer.addCategory("Color");
 * 
 * // Does not add the category "Window" because it already exists
 * writer.addCategory("Window");
 * 
 * // Creates a new element under Color (Text=Black)
 * String category = "Color";
 * String key = "Text";
 * Object value = "Black";
 * writer.addElement(category, key, value);
 * 
 * // Creates a new element that will be under Color, as Background=White
 * String key2 = "Background";
 * Object value2 = "White";
 * element = new INIElement(writer.getINIFile(), category, key2, value2);
 * 
 * // ...
 * }
 * </pre>
 * And the content of the Example.ini file is now:
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
 * 
 * [Color]
 * Text=Black
 * }
 * </pre>
 * But why is the Background=White in the file? Because the INIFileWriter never
 * actually wrote it to the file. Just because you created an INIElement and set
 * the file that it belongs to, does not mean that it will be in the file. For it
 * to be successfully added to the file, it must be added via INIFileWriter.
 * <br /><br />
 * So, to fix the problem, just add the following line after the element is
 * initialized:
 * <br /><br />
 * <pre>
 * {@code 
 * writer.addElement(element);
 * }
 * </pre>
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
	 * Adds a category to the current INI file. If the category already exists,
	 * then the category will not be created.
	 * 
	 * @param category The category to be added to the file
	 */
	public void addCategory(String category) {
		// Checking to see if the file already contains the desired category
		boolean containsCategory = false;
		String fileContent = "";
		String string;
		
		try (BufferedReader in = new BufferedReader(new FileReader(iniFile))) {
			while ( (string = in.readLine()) != null) {
				fileContent += string;
				fileContent += System.getProperty("line.separator");
				
				if (!string.equals("")) {
					// Line is a comment, ignoring
					if (string.charAt(0) == ';' || string.charAt(0) == '#')
						continue;

					// Line is a category, checking if it is the category that is
					// about to be created
					if (string.charAt(0) == '[') {
						String thisCategory = string.substring(1, string.length() - 1);
						if (thisCategory.equals(category))
							containsCategory = true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// The file does not contain the category, so appeanding it to the end
		// of the file.
		if (!containsCategory) {
			try (BufferedWriter out = new BufferedWriter(new FileWriter(iniFile))) {
				// Overwritting the old file content with the correct content,
				// which has a proper newline at the end of the file.
				out.write(fileContent);
				
				if (fileContent.length() != 0)
					out.newLine();
				
				out.write('[' + category + ']');
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds an element to the desired category. If the element already exists,
	 * the element is NOT overwritten. Instead, nothing is done. If you want to
	 * override a value, see {@link INIElementWriter#setValue(Object)}.
	 * 
	 * @param category	The desired category to add the element to
	 * @param key		The key that will represent the element
	 * @param value		The value of the element
	 */
	public void addElement(String category, String key, Object value) {
		this.addElement(new INIElement(this.iniFile, category, key, value));
	}
	
	/**
	 * Adds an element to the desired category. If the element already exists,
	 * it will not override the value. If you wish to change the value of an
	 * element, see {@link INIElementWriter#setValue(Object)}
	 * <i>Note: The element's INI file will be overwritten to the INIFileWriter's
	 * INI file</i>
	 * 
	 * @param element An element containing a category, key, and a value. 
	 */
	public void addElement(INIElement element) {
		element.setINIFile(this.iniFile);
		
		try(BufferedReader in = new BufferedReader(new FileReader(iniFile))) {
			File temp = new File(".temp.ini");
			BufferedWriter out = new BufferedWriter(new FileWriter(temp, true));
			String string;
			
			boolean isInSameCategory = false;
			boolean hasFoundCategory = false;
			boolean keyExists = false;
			
			while ((string = in.readLine()) != null) {
				String newline = string;
				
				if (!hasFoundCategory) {
					// Line is a category, checking if it is the specified one...
					if (!string.equals("") && string.charAt(0) == '[') {
						String thisCategory = string.substring(1, string.length() - 1);
						if (thisCategory.equals(element.getCategory())) {
							hasFoundCategory = true;
							isInSameCategory = true;
						}
					}
				} else {
					// A blank line has been reached under this category, adding
					// the element to the list.
					if (string.equals("")) {
						if (!keyExists) {
							keyExists = true;
							out.write(element.getKey() + '=' + element.getValue());
							out.newLine();
						}
					} else
						// A new category has started
						if (string.charAt(0) == '[')
							isInSameCategory = false;
					
					// Checking if line is an element
					if (string.contains("=")) {
						int pos = string.indexOf("=");
						String key = string.substring(0, pos);
						
						if (key.equals(element.getKey()) && isInSameCategory)
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
				
				if (!keyExists ) {
					out.write(element.getKey() + '=' + element.getValue());
					out.newLine();
				}
			
			in.close();
			out.close();
			
			element.getINIFile().delete();
			temp.renameTo(element.getINIFile());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a comment to the line directly above the specified category.
	 *
	 * NOTE: It does not append new lines to the comment, so comment wisely.
	 * 
	 * @param category 	The category to append the comment above
	 * @param comment 	The comment to be added above the specified category
	 */
	public void addComment(String category, Object comment) {
		File temp = new File(".temp.ini");
		
		boolean hasCategory = false;
		String currentLine;
		
		try (BufferedReader in = new BufferedReader(new FileReader(iniFile))) {
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			
			while ((currentLine = in.readLine()) != null) {
				// Instead of appending current line to fileContent before knowing the line is not a category,
				// check to see if the line is the desired category, append comment, then add the currentLine.
				
				if (!currentLine.equals("")) {
					// Line is not a comment
					if (currentLine.charAt(0) == '[') {
						String thisCategory = currentLine.substring(1, currentLine.length() - 1);
						if (thisCategory.equals(category)) {
							String cmt = comment.toString();
							out.write("# " + cmt);
							out.write(System.getProperty("line.separator"));
						}
					}
				}
				
				System.out.println(currentLine);
				out.write(currentLine);
				out.write(System.getProperty("line.separator"));
			}
			
			out.flush();
			out.close();
			in.close();
			
			this.getINIFile().delete();
			temp.renameTo(this.iniFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current INI file that is being written to.
	 * 
	 * @return The INI file being written to
	 */
	public File getINIFile() {
		return this.iniFile;
	}
}