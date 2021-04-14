package lab4;

import java.util.HashMap;

// This file also illustrates what is expected for Javadoc
//   - Class description
//   - Description of every public method (short and long description)
//
//   If there were an enum in the class, it should also
//   be commented
//

/**
  *   CodeUtil contains a handful of static methods
  *   to handle a simple character-to-character cipher.
  */
public class CodeUtil {

   // No constructor needed, this is to inhibit javadoc constructor
   // reference
   private CodeUtil() {}

   /**
    *   Returns the decoded secret message.
    *   <p> 
    *   The method replaces symbols with the corresponding letter
    *   found in HashMap trans. If a symbol isn't found, it's replaced
    *   with a dot.
    *
    *   @param secretMessage  The encrypted message
    *   @param trans          The conversion HashMap
    *   @return The decrypted (as far as trans allows) message
    */
   public static StringBuffer decode(String secretMessage,
                                     HashMap<Character,Character> trans) {
       Character       meaning;
       StringBuffer    sb = new StringBuffer();    

       for (int i = 0; i < secretMessage.length(); i++) { 
         meaning = trans.get(secretMessage.charAt(i));
         if (meaning != null) {
           sb.append(meaning);
         } else {
           sb.append('.');
         }
       }
       return sb;
   }

   /**
    *  Converts a searched word to a pattern.
    *  <p>
    *  The pattern matches what is known of the code. Letters for
    *  which the symbol in the code is known are left untouched,
    *  letters for which the symbol is unknown are replaced with a dot.
    *
    *  @param searched The word that we want to search
    *  @param trans          The conversion HashMap
    *  @return The pattern corresponding to the word in the current state of decryption. 
    */
   public static StringBuffer wordPattern(String searched,
                                          HashMap<Character,Character> trans) {
       StringBuffer needle = new StringBuffer();    
       for (int i = 0; i < searched.length(); i++) { 
         if (trans.containsValue(searched.charAt(i))) {
           // We know how this one is converted
           needle.append(searched.charAt(i));
         } else {
           needle.append('.');
         }
       }
       return needle;
   }

}
