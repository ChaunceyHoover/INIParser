INIParser
=========

An initialization file parser with reading, writing, and adding elements and categories.

## Notes ##

Although the class `INIElement` has a set method for all it's properties (`File iniFile`,
`String category`, `String key`, `Object value`), it does _NOT_ actually update the values in the file. 
These methods should only be called when creating a new element for the `INIFileWriter`.

Also, all of the files are in JavaDoc format.

## How to use ##

*Content of Example.ini for all examples.*

	; Defining window properties
	[Window]
	Width=640
	Height=480
	
	; Defining render properties
	[Properties]
	vSync=true
	DoubleBuffer=true

### Reading all values from under a category ###

	// ...
	
	java.io.File iniFile = new java.io.File("Example.ini");
	INIReader reader = new INIReader(iniFile);
	
	for (INIElement element : reader.getElements("Window")) {
		String key = element.getKey();
		Object value = element.getValue();
		
		System.out.println(key + '=' + value.toString());
	}
	
	// ...
	
##### Output #####

	Width=640
	Height=480

### Getting the total number of categories in a file ###

	// ...
	
	java.io.File iniFile = new java.io.File("Example.ini");
	INIReader reader = new INIReader(iniFile);
	
	int totalCategories = reader.getCategoryCount();
	System.out.println("Total number of categories: " + reader);
	
	// ...
	
##### Output #####

	Total number of categories: 2

### Setting the value of a specific element ###

	// ...
	
	java.io.File iniFile = new java.io.File("Example.ini");
	INIReader reader = new INIReader(iniFile);
	
	// The first element within the category of "Window", which is "Width".
	INIElement element = reader.getElements("Window").get(0);
	INIElementWriter writer = new INIElementWriter(element);
	
	System.out.println("Before element is written to: " + element.getValue().toString());
	
	writer.setValue(480);
	
	System.out.println("After the element is written to: " + element.getValue().toString());
	
	// ...

##### Output #####

	Before element is written to: 640
	After element is written to: 480
	
##### Example.ini #####
	
	; Defining window properties
	[Window]
	Width=480
	Height=480
	
	; Defining render properties
	[Properties]
	vSync=true
	DoubleBuffer=true

### Adding a category and element to a file ###

	// ...
	
	java.awt.File iniFile = new java.awt.File("Example.ini");
	INIReader reader = new INIReader(iniFile);
	INIFileWriter writer = new INIFileWriter(iniFile);
	
	// Creates category "Example"
	writer.addCategory("Example");
	
	// Will not create category, because it already exists
	writer.addCategory("Window");
	
	// Adding a new element to the new category, and to a previous category.
	INIElement element = new INIElement(iniFile, "Example", "One", 1);
	writer.addElement("Example", "Two", 2);
	writer.addElement(element);
	
	// Printing out the new values
	for (INIElement e : reader.getElements("Example") {
		System.out.println(e.getKey() + '=' + e.getValue().toString());
	}
	
	// ...
	
##### Output #####

	One=1
	Two=2

##### Example.ini #####

	; Defining window properties
	[Window]
	Width=640
	Height=480
	
	; Defining render properties
	[Properties]
	vSync=true
	DoubleBuffer=true
	[Example]
	One=1
	Two=2

## Have more questions? ##

Send me a tweet (`@PlatinumCoding`) or just email me @ `DealerNextDoor@yahoo.com`.