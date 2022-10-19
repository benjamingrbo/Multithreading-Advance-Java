import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler extends Thread{

    private String fileToHandle;

    public FileHandler(String fileToHandle){
        this.fileToHandle = fileToHandle;
    }
    //method which reads first line aka target word

    private String getFirstLine() throws IOException {
        //System.out.println(firstLine);
        return Files.readAllLines(Paths.get(fileToHandle)).get(0);
    }
    /*
    Method which finds all characters in line/string and save them in hashmap
    for example: BALLOON
    Key:value
    A:1
    B:1
    L:2
    O:2
    N:1
     */
    private static HashMap<Character, Integer> allCharactersInLine(String line){
        HashMap<Character, Integer> numberOfRequiredCharsPerChar = new HashMap<>();
        for(int i=0; i<line.length(); i++){
            char character = line.charAt(i);
            if(!numberOfRequiredCharsPerChar.containsKey(character)){
                //nema ga u hashmapi, moramo ga dodat
                numberOfRequiredCharsPerChar.put(character, 0);
            }
            int currentCounter = numberOfRequiredCharsPerChar.get(character);
            numberOfRequiredCharsPerChar.put(character, currentCounter+1);
        }
        return numberOfRequiredCharsPerChar;
    }
    /*
    Method that compares relevant keys/chars from target word and every line
     It uses temp HashMap to store number or repeating of each relevant char, if it isn't relevant value will be 0, and if you need for example
     2 'L' chars for one word you will store 1 in this hashmap.
     */
    private static void numberOfWordsInLine(String firstLine, String line, HashMap<Character, Integer> numberOfCharsInFirstLine, HashMap<Character,Integer> numberOfCharsInLine){
        HashMap<Character, Integer> tempHashForLine = new HashMap<>();//Temp/help HashMap
        for(Character key : numberOfCharsInFirstLine.keySet()){
            if(numberOfCharsInLine.containsKey(key)){
                tempHashForLine.put(key, numberOfCharsInLine.get(key)/numberOfCharsInFirstLine.get(key));//Relevant char
            }else{
                tempHashForLine.put(key, 0);//Irrelevant char
            }
        }

        int numberOfWords = Collections.min(tempHashForLine.values());
        //System.out.println(tempHashForLine);
        //System.out.println(firstLine + " in " + line + " " + numberOfWords + " time. - Thread: " + currentThread().getId());
        try {
            writingInOutputFile(firstLine + " in " + line + " " + numberOfWords + " time. - Thread: " + currentThread().getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized void writingInOutputFile(String message) throws IOException{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./Output/output.txt", true));
            synchronized (bufferedWriter){
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.close();
        }
    }

    @Override
    public void run() {
        try {
            String firstLine = getFirstLine();//Target word
            HashMap<Character, Integer> requiredChars = allCharactersInLine(getFirstLine());//Chars in target word
            //System.out.println(requiredChars);//za provjeru - obrisati
            //System.out.println("--------------");//za provjeru - obrisati

            //Prolazimo kroz redove startajuci od drugog reda
            BufferedReader reader = new BufferedReader(new FileReader(fileToHandle));
            reader.readLine();//Skip first line
            String row;
            while((row = reader.readLine()) != null){
                HashMap<Character, Integer> charsInLine = allCharactersInLine(row);//Chars in every line/string
                //System.out.println(charsInLine);//za provjeru - obrisati
                numberOfWordsInLine(firstLine, row, requiredChars, charsInLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
