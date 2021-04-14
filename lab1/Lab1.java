//
//   Lab1.java
//
//   You should use this program for testing your Translit class.
//   To run it in a console:
//      $ java Lab1 <name of the file to convert>
//
//   To run it from Eclipse you need first to go to
//       Run/Run Configurations...
//   then click on the tab "(x)= Arguments" and enter the full access
//   path to the file in the "Program arguments:" entry field.
//

import java.io.*;
import java.nio.charset.StandardCharsets;

// Define class Translit here.
// You can also define it as a public class in a separate file named
// Translit.java

public class Lab1 {
    static String fileContent = new String("");

    // This method reads the contents of a file into a String.
    // It specifies that the characters in the file are encoded
    // with the UTF-8 encoding scheme (this is the standard on the Web
    // and on Linux machines; Windows machines use a different default
    // encoding scheme)
    // We will see files in detail later in the course.
    private static void readFile(String fileName) throws IOException {
		// The code in this method is mainly obtained from the lab sample code of BufferedReader
		
		// try block with (), the source declared in the bracket () will be closed autoly 
		// after the execution of try block
        try (FileInputStream fis = new FileInputStream(new File(fileName));
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader bReader = new BufferedReader(isr);){


            char[] cbuf = new char[65535];
			// the read() method below take a char array as an input
			// after execution of the method below, the cbuf will be the file content read from the source file
			// the method returns the length which was read from the source file
            int file_len = bReader.read(cbuf);

            char[] target = new char[file_len];
			// This method copies "file_len" elements start from "0" in "cbuf" to "target" start from "0"
			// In this way, the redundant empty characters will be thrown away which means only required text are kept
            System.arraycopy(cbuf, 0, target, 0, file_len);
			// create a string using the "target" char array and give it to "fileContent" which is a static "String" variable in the class
            fileContent = new String(target);

        } catch (FileNotFoundException e) {
            System.out.println("The pathname does not exist.");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("The Character Encoding is not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed or interrupted when doing the I/O operations");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // The program takes the name of the file from the command line.
        // Wen it runs, it finds command line parameters into the args array.

        if (args.length > 0) {
            try {
                // Load the content of the file in memory
                readFile(args[0]);
                // Display what has been read for control.
                System.out.println("Input:");
                System.out.println(fileContent);
                // Create a Translit object
                Translit tr = new Translit();
                // Convert and display. It will all be in lowercase.
                System.out.println("Output:");
                System.out.println(tr.convert(fileContent));
            } catch (Exception e) {
                System.out.println("Something wrong!");
            }
        }
        else
            System.out.println("There's no argument!");
    }

}
