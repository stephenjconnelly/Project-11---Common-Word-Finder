/**
 * Class that finds the most common words in the bible using hashmap, bst, and avl.
 * @author Stpehen Connelly Sjc2235
 * @version 1.0.1 December 19, 2022
 * Works Cited:
 * //citing: https://www.geeksforgeeks.org/read-file-into-an-array-in-java/
 * //#1705 discussion on EdStem
 * //https://stackoverflow.com/questions/50415200/sort-an-array-of-arrays-in-javascript
 * //https://stackoverflow.com/questions/2635076/convert-integer-to-equivalent-number-of-blank-spaces
 */

import java.util.Iterator;
import java.io.*;
import java.util.Arrays;


public class CommonWordFinder{
    /**
     * Checks for valid user input and depending on value inputted for args[1], either
     * calls hashTime, avlTime, or bstTime. Reads file and converts it into a parsed array
     * of strings that have been separated by spaces and new lines. All special characters except
     * hyphens and apostrophes are removed.
     * @param args an array of use input: [<filename>, <bst|avl|hash>, [limit]]
     */
    public static void main(String[] args){
       int limit = 0;
       //generally checks if commandline arguments are valid for the program
       try{
           String file = args[0];
       }catch(ArrayIndexOutOfBoundsException e){
           System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
           System.exit(1);
       }
        //if no third, sets limit = 10. Checks if there are at least 2 arguments.
       if (args.length == 2) {
           limit = 10;
       }else if (args.length<2 || args.length>3){
           System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
           System.exit(1);
       }else{//if there is a third argument, checks if it is non-negative and numerical
           try {
               limit = Integer.parseInt(args[2]);
               if (Integer.parseInt(args[2]) < 0){
                   System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
                   System.exit(1);
               }
           }catch(NumberFormatException e){
               System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
               System.exit(1);
           }
       }
       int i = 0;
       String[] bible = new String[900000];
       //reads file using FileReader and reads character by character
       try{
           FileReader file = new FileReader(args[0]);
           String str = new String();
           while(file.ready() && i < bible.length-1) {
               char chr = (char) file.read();
                   if (Character.isLetter(chr) || chr == '\'') {
                       str += Character.toLowerCase(chr);
                   } else if (chr == '\n' || chr == ' ') { //if char is a space or new line character
                       if(!str.isBlank()) {
                           bible[i] = str;
                           i++;
                       }
                       str = new String(); //'resets' string
                   }else if (chr == '-' && str.length() != 0 ){ //if char is a hyphen and not at the start of a word
                        str += Character.toLowerCase(chr);
                   } else {
                        //do nothing
                   }
           }
       } catch(FileNotFoundException e){
           System.err.println("Error: Cannot open file '" + args[0] + "' for input." );
       } catch(IOException e) {
           System.err.println("Error: An I/O error occurred reading '" + args[0] + "'." );
       }

       //checks second argument to see if it is a valid structure and then calls appropriate method if valid
       if(args[1].compareTo("avl") == 0){
           avlTime(limit, bible, i);
       }else if(args[1].compareTo("bst") == 0){
           bstTime(limit,  bible, i);
       }else if(args[1].compareTo("hash") == 0){
           hashTime(limit, bible, i);
       }else{
           System.err.println("Error: Invalid data structure '" + args[1] + "' received.");
       }
   }
    /**
     * Implements a hashMap from myMap interface to map words from parsed file array "bible" into hashmap and increments
     * value of MapEntry by 1 if entry is already there. If not, puts entry and sets entry value 1. User iterator to
     * iterate through hashMap and puts values into a nested array "String[][] arrays". Then sorts arrays alphabetically
     * and then numerically in descending order. Then finds length of the longest word and Calls printHelper.
     * @param limit amount to which list of numbers should be printed in output.
     * @param size amount of elements in bible array
     * @param bible array of parsed strings from inputted file.
     */
    private static void hashTime(int limit, String[] bible, int size){
        int count = 0;
        MyMap<String, Integer> map = new MyHashMap<>();
        Integer intgr;
        //iterates through and maps elements
        for(int i = 0; i<size; i++){
            if(bible[i] != null) {
                intgr = map.get(bible[i]);
                if (intgr == null) { //if there is not an entry already at this key, put element and set value = 1
                    map.put(bible[i], 1);
                    count++;
                } else { //else, put element in at key and increment by 1 for value
                    map.put(bible[i], map.get(bible[i]) + 1);
                }
            }
        }
        Iterator<Entry<String, Integer>> iter = map.iterator();
        String[][] arrays = new String[map.size()][2];
        int n = 0;
        int maxLengthWord = 0;
        while (iter.hasNext()){ //iterates through map and puts value and key into nested array
            Entry<String, Integer>it = iter.next();
            if(it != null){
                int j = it.value;
                arrays[n][0] = Integer.toString(j);
                String s = it.key;
                arrays[n][1] = s;
            }
            n++;
        }
        //sorts by alphabetical order
        Arrays.sort(arrays, (a, b) -> (a[1]).compareTo((b[1])));
        //sorts by descending order
        Arrays.sort(arrays, (b, a) -> Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0])));
        if (limit > arrays.length){ //makes length of the sorted map array the limit if limit is greater
            limit = arrays.length;
        }
        //finds length of the longest word in map less than the limit
        for(int x = 0; x <limit-1; x++){
            if(maxLengthWord < arrays[x][1].length()) {
                maxLengthWord = arrays[x][1].length();
            }
        }
        System.out.print("Total unique words: " +  count);
        printHelper(n, maxLengthWord, arrays, limit);
    }
    /**
     * Implements a BSTMap from myMap interface to map words from parsed file array "bible" into bst and increments
     * value of node by 1 if node is already in map. If not, puts node in map and sets node value to 1. User iterator to
     * iterate through BST and puts values into a nested array "String[][] arrays". Then sorts arrays numerically
     * in descending order. Then finds length of the longest word and Calls printHelper.
     * @param limit amount to which list of numbers should be printed in output.
     * @param size amount of elements in bible array
     * @param bible array of parsed strings from inputted file.
     */
    private static void bstTime(int limit, String[] bible, int size){
        int count = 0;
        MyMap<String, Integer> map = new BSTMap<>();
        Integer intgr = map.get(bible[0]);
        //iterates through and maps elements
        for(int i = 0; i<size; i++){
            if(bible[i] != null) {
                intgr = map.get(bible[i]);
                if (intgr == null) {//if there is not an entry already at this key, put element and set value = 1
                    map.put(bible[i], 1);
                    count++;
                } else { //else, put element in at key and increment by 1 for value
                    map.put(bible[i], map.get(bible[i]) + 1);
                }
            }
        }
        System.out.print("Total unique words: " +  count);
        Iterator<Entry<String, Integer>> iter = map.iterator();
        String[][] arrays = new String[map.size()][2];
        int n = 0;
        int maxLengthWord = 0;
        while (iter.hasNext()){ //iterates through map and puts value and key into nested array
            Entry<String, Integer>it = iter.next();
            if(it != null){
                int j = it.value;
                arrays[n][0] = Integer.toString(j);
                String s = it.key;
                arrays[n][1] = s;
            }
            n++;
        }
        //sorts by descending order
        Arrays.sort(arrays, (b, a) -> Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0])));
        if (limit > arrays.length){ //makes length of the sorted map array the limit if limit is greater
            limit = arrays.length;
        }
        //finds the length of the longest word less than the limit.
        for(int x = 0; x <limit-1; x++){
            if(maxLengthWord < arrays[x][1].length()) {
                maxLengthWord = arrays[x][1].length();
            }
        }
        printHelper(n, maxLengthWord, arrays, limit);
    }

    /**
     * Implements a AVLTreeMap from myMap interface to map words from parsed file array "bible" into AVL tree and
     * increments value of pair by 1 if pair is already in map. If not, puts pair in map and sets pair value to 1.
     * User iterator to iterate through AVL and puts values into a nested array "String[][] arrays". Then sorts
     * arrays numerically in descending order. Then finds length of the longest word and Calls printHelper.
     * @param limit amount to which list of numbers should be printed in output.
     * @param size amount of elements in bible array
     * @param bible array of parsed strings from inputted file.
     */
    private static void avlTime(int limit, String[] bible, int size){
        int count = 0;
        MyMap<String, Integer> map = new AVLTreeMap<>();
        Integer intgr = map.get(bible[0]);
        //iterates through and maps elements
        for(int i = 0; i<size; i++){
            if(bible[i] != null) {
                intgr = map.get(bible[i]);
                if (intgr == null) {//if there is not an entry already at this key, put element and set value = 1
                    map.put(bible[i], 1);
                    count++;
                } else {//else, put element in at key and increment by 1 for value
                    map.put(bible[i], map.get(bible[i]) + 1);
                }
            }
        }
        System.out.print("Total unique words: " +  count);
        Iterator<Entry<String, Integer>> iter = map.iterator();
        String[][] arrays = new String[map.size()][2];
        int n = 0;
        int maxLengthWord = 0;
        String s = "";
        int j = 0;
        while (iter.hasNext()){ //iterates through map and puts value and key into nested array
            Entry<String, Integer>it = iter.next();
            if(it != null){
                j = it.value;
                arrays[n][0] = Integer.toString(j);
                s = it.key;
                arrays[n][1] = s;
            }
            n++;
        }
        //sorts by descending order
        Arrays.sort(arrays, (b, a) -> Integer.compare(Integer.parseInt(a[0]), Integer.parseInt(b[0])));
        if (limit > arrays.length){ //makes length of the sorted map array the limit if limit is greater
            limit = arrays.length;
        }
        //finds the length of the longest word less than the limit..
        for(int x = 0; x <limit-1; x++){
            if(maxLengthWord < arrays[x][1].length()) {
                maxLengthWord = arrays[x][1].length();
            }
        }
        printHelper(n, maxLengthWord, arrays, limit);
    }
    /**
     * Iterates through nested array 'arrays' and formats and prints both the word and the frequency of the word.
     * @param limit amount to which list of numbers should be printed in output.
     * @param n amount of elements in 'String[][] arrays'
     * @param maxLengthWord max length of all strings in 'String[][] arrays'.
     * @param arrays array of parsed strings from inputted file.
     */
    static void printHelper(int n, int maxLengthWord, String[][] arrays, int limit){
        //iterates through 'arrays' and prints elements with nested array
        for(int i = 0; i < limit; i++){
            String frequency = arrays[i][0];
            String word = arrays[i][1];
            int c = maxLengthWord+1-(word.length());
            String spaces = String.format("%"+c+"s","");
            String spaces1 = String.format("%"+(Integer.toString(limit).length()-String.valueOf(i+1).length()+1)+"s","");
            //adds space before the "1. ... 2. ... 3"
            System.out.format(System.lineSeparator() + spaces1.substring(1,spaces1.length()));
            System.out.print(i+1); //prints "1. ... 2. ..."
            System.out.print(". "+word); //prints word
            System.out.format(spaces); //adds spacing between word and frequency
            System.out.print(frequency); //prints frequency
        }
        System.out.format(System.lineSeparator());
    }
}
