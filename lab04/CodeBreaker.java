package lab4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class is used to decode a secret message.
 * 
 * 
 */
public class CodeBreaker {
	static String fileContent = new String("");

	/**
	 * This method reads the contents of a file into a String.
	 * <P>
	 * It specifies that the characters in the file are encoded with the UTF-8
	 * encoding scheme (this is the standard on the Web and on Linux machines;
	 * Windows machines use a different default encoding scheme) We will see
	 * files in detail later in the course.
	 * 
	 * 
	 * @param fileName
	 *            the name of input file
	 * @throws FileNotFoundException
	 *             throws when the input file can not be found
	 * @throws UnsupportedEncodingException
	 *             throws when the input file is not UTF-8
	 * @throws IOException
	 *             throws when I/O operations are failed or interrupted
	 */
	private static void readFile(String fileName)
			throws FileNotFoundException, UnsupportedEncodingException, IOException {
		// Reads and loads in memory (into fileContent)
		char[] cbuf = new char[1000];
		int charsRead;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		while ((charsRead = isr.read(cbuf, 0, 1000)) != -1) {
			fileContent += new String(java.util.Arrays.copyOfRange(cbuf, 0, charsRead));
		}
		isr.close();
	}

	/**
	 * This method is the main process.
	 * <p>
	 * <b>Work Flow</b>
	 * <ol>
	 * <li>Read file</li>
	 * <li>Decode the file content</li>
	 * <li>Out put the decoded message</li>
	 * </ol>
	 * 
	 * @param args
	 *            only one argument is allowed, and it should be a correct file
	 *            name, otherwise program will exit with a message
	 */
	public static void main(String[] args) {
		// The program takes the name of the file from the command line.
		// When it runs, it finds command line parameters into the args array.
		if (args.length > 0) {
			try {
				// Load the content of the file in memory
				readFile(args[0]);
				Parse parse = new Parse();
				String decoded = parse.decode(fileContent);
				System.out.print(fileContent);
				// Convert and display. It will all be in lowercase.
				System.out.println(decoded);
			} catch (Exception e) {
				// If anything goes wrong
				System.err.println(e.getMessage());
			}
		} else {
			System.err.println("Usage: java CodeBreaker <message filename>");
		}
	}
}

/**
 * The class actually shows how to decode a secret message.
 * <p>
 * It has only one public method {@code decode}
 * 
 */
class Parse {

	/**
	 * This private method finishes the first step task.
	 * <p>
	 * It finds the most frequent sequence of three symbols in a message
	 * 
	 * 
	 * @param fileContent
	 *            a secret message
	 * @return a string contains the most frequent of three symbols
	 */
	private String parseThe(String fileContent) {
		HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();

		String strLikeThe = "";
		int maxFreq = 0;
		for (int i = 0; i < fileContent.length() - 3; i++) {
			String word = fileContent.substring(i, i + 3);

			if (wordFreq.containsKey(word)) {
				int curFreq = wordFreq.get(word) + 1;

				wordFreq.replace(word, curFreq);
				if (curFreq > maxFreq) {
					maxFreq = curFreq;
					strLikeThe = word;
				}
			} else {
				wordFreq.put(word, 1);
			}
		}
		return strLikeThe;
	}

	/**
	 * This method is the only public method, try to convert the secret message
	 * to plain text.
	 * <p>
	 * <b>work flow</b>
	 * <ol>
	 * <li>First step: {@code parseThe}</li>
	 * <li>Second step: {@code secondParse}</li>
	 * <li>Third step: {@code your code?}</li>
	 * </ol>
	 * 
	 * @param fileContent
	 *            secret message
	 * @return decoded message
	 */
	public String decode(String fileContent) {
		String output = "";
		// First step: parse "the"
		String strLikeThe = parseThe(fileContent);
		System.out.println(strLikeThe);

		HashMap<Character, Character> firstMap = new HashMap<Character, Character>();
		firstMap.put(strLikeThe.charAt(0), 't');
		firstMap.put(strLikeThe.charAt(1), 'h');
		firstMap.put(strLikeThe.charAt(2), 'e');

		String[] likely = { "seventeen", "starboard", "thirteen", "fourteen", "eighteen", "nineteen", "fifteen",
				"sixteen", "through","degree", "fathom","branch", "eleven", "twelve", "twenty", "thirty", "forty", "fifty", "seven",
				"eight", "three", "north", "south", "right","left","from", "shop", "yard", "foot", "feet", "inch", "mile", "east", 
				"west", "port", "four", "five", "nine", "one", "two", "six", "ten" };
		HashMap<Character, Character> secondMap = new HashMap<Character, Character>();

		secondMap = secondParse(fileContent, firstMap, likely);
		output = CodeUtil.decode(fileContent, secondMap).toString();

		return output;
	}

	/**
	 * This private method finishes the second step task.
	 * <p>
	 * It tries to set up the conversion HashMap and populate it as much as we
	 * can.
	 * 
	 * 
	 * @param fileContent
	 *            a secret message
	 * @param firstMap
	 *            the conversion HashMap produced by first step
	 * @param likely
	 *            a array contains all likely words
	 * @return the conversion HashMap produced by second step
	 */
	private HashMap<Character, Character> secondParse(String fileContent, HashMap<Character, Character> firstMap,
			String[] likely) {
		HashMap<Character, Character> secondMap = new HashMap<>(firstMap);
		// TODO:add your code here
		secondMap.put('(', 'r');
		secondMap.put('6', 'i');
		secondMap.put('*', 'n');
		secondMap.put('1', 'f');
		secondMap.put('$', 'o');
		secondMap.put('+', 'd');
		secondMap.put('3', 'g');
		secondMap.put('?', 'u');
		secondMap.put('9', 'm');
		secondMap.put(')', 's');
		secondMap.put('5', 'a');
		secondMap.put(']', 'w');
		secondMap.put(':', 'y');
		secondMap.put('0', 'l');
		secondMap.put('%', 'v');
		secondMap.put('2', 'b');
		secondMap.put('-', 'c');
		secondMap.put('.', 'p');
		return secondMap;
	}

}