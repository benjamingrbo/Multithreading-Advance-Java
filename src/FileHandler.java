import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler extends Thread{

    private String fileToHandle;

    public FileHandler(String fileToHandle){
        this.fileToHandle = fileToHandle;
    }

    private String getFirstLine() throws IOException {
        //System.out.println(firstLine);
        return Files.readAllLines(Paths.get(fileToHandle)).get(0);
    }

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

    private static void numberOfWordsInLine(String firstLine, String line, HashMap<Character, Integer> numberOfCharsInFirstLine, HashMap<Character,Integer> numberOfCharsInLine){
        HashMap<Character, Integer> tempHashForLine = new HashMap<>();
        for(Character key : numberOfCharsInFirstLine.keySet()){
            if(numberOfCharsInLine.containsKey(key)){
                tempHashForLine.put(key, numberOfCharsInLine.get(key)/numberOfCharsInFirstLine.get(key));
            }else{
                tempHashForLine.put(key, 0);
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
            String firstLine = getFirstLine();
            HashMap<Character, Integer> requiredChars = allCharactersInLine(getFirstLine());//broj slova u nultoj rijeci
            //System.out.println(requiredChars);//za provjeru - obrisati
            //System.out.println("--------------");//za provjeru - obrisati
            //Prolazimo kroz redove startajuci od drugog reda
            BufferedReader reader = new BufferedReader(new FileReader(fileToHandle));
            reader.readLine();
            String row;
            while((row = reader.readLine()) != null){
                HashMap<Character, Integer> charsInLine = allCharactersInLine(row);
                //System.out.println(charsInLine);//za provjeru - obrisati
                numberOfWordsInLine(firstLine, row, requiredChars, charsInLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
