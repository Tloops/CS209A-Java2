package lab3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CodeBreaker_1 {

    public static void main(String[] args) {
        if(args.length != 1)
            return;
        String fileName = args[0];
        try (FileInputStream fis = new FileInputStream(new File(fileName));
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader bReader = new BufferedReader(isr);){

            char[] cbuf = new char[65535];
            int file_len = bReader.read(cbuf);

            char[] target = new char[file_len];
            System.arraycopy(cbuf, 0, target, 0, file_len);
            String secret = new String(target);

            HashMap<String, Integer> map = new HashMap<>();

            for (int i = 0; i <= secret.length()-3; i++) {
                String sequence = secret.substring(i, i+3);
                if(map.containsKey(sequence))
                    map.put(sequence, map.get(sequence)+1);
                else
                    map.put(sequence, 1);
            }

            int maxFrequence = 0;
            String targetSequence = "";
            for(Map.Entry<String, Integer> entry : map.entrySet()){
                if(entry.getValue() > maxFrequence) {
                    maxFrequence = entry.getValue();
                    targetSequence = entry.getKey();
                }
            }
            System.out.println(targetSequence);

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

}
