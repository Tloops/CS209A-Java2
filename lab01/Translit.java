import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Translit {

	// what is hashmap? it is like a dictionary, stores the mapping (key->value)
    private HashMap<Character, String> bigHashMap = new HashMap<>();
    private HashMap<Character, String> smallHashMap = new HashMap<>();

    public Translit() {
		// This part (until "end") is the same as the method readfile in Lab1.java
		// The purpose is to read the file to a string (here: "content")
		
        String content = "";
        try (FileInputStream fis = new FileInputStream(new File("translit_table.txt"));
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader bReader = new BufferedReader(isr);){


            char[] cbuf = new char[65535];
            int file_len = bReader.read(cbuf);

            char[] target = new char[file_len];
            System.arraycopy(cbuf, 0, target, 0, file_len);
            content = new String(target);

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
		
		// end
		
		// The code below is aim to process the file contents
		// How we process it actually depends on how the file content looks like
		
		// split(): split using the input argument, returns a String array separated by the input argument
		// '\n' is huanhang, so each element in the "lines" array stores a line of the file
        String[] lines = content.split("\n");
        for (String line: lines){
			// the element in every line is divided by ',', so we split(",") here
            String[] segment = line.split(",");
            for (int i = 0; i < segment.length; i++){
				// trim(): delete empty characters at the beginning and the end of a String and returns it (The original string won't change except for an assignment(fuzhi))
				// replace(): replace argument 0 with argument 1
				// here I just want to delete " ' and empty characters
                segment[i] = segment[i].trim().replace("\"", "").replace("'", "");
			}
			// put(key, value) will put a mapping into the dictionary
			// get(key) will get the corresponding value of the input argument as the key
			// bigHashMap stores the upper case character dictionary (e.g.: M -> m)
			// smallHashMap stores the lower case character dictionary (e.g.: m -> m)
            bigHashMap.put(segment[0].charAt(0), segment[2]);
            smallHashMap.put(segment[1].charAt(0), segment[2]);
        }
    }

    public String convert(String russian_text) {
		// StringBuilder is a faster version of String, actually you can use String here, too
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < russian_text.length(); i++){
			// read the i-th character of the russian_text
            char c = russian_text.charAt(i);
			
			
            String translit = "";
            if(bigHashMap.containsKey(c)) // a upper case letter?
                translit = bigHashMap.get(c);
            else if(smallHashMap.containsKey(c)) // a lower case letter?
                translit = smallHashMap.get(c);
            else // neither, keep the original value
                translit = String.valueOf(c);
			
			// append the string to the end of the stringBuilder
            stringBuilder.append(translit);
        }
        return stringBuilder.toString();
    }

}
