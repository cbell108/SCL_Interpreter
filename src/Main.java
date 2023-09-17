import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main{
    public static void main(String[] args){
        //Get grammar dictionary
        //Make it return the dictionary or call later
        getGrammar();
    }//END MAIN
    private static void getGrammar(){
        //Get file
        String filePath = "res/Modules_3_5_7_Project Resources (Copy)/scl_grammar.txt";

        //Key: keyword, Values: list of legal arguments
        Map<String, ArrayList<String>> grammarDict = new HashMap<>();

        try{
            //Open file with scanner
            File gFile = new File(filePath);
            Scanner gScanner = new Scanner(gFile);

            //Loop through each line
            while(gScanner.hasNextLine()){
                String line = gScanner.nextLine();
                line = line.trim();

                //Skip empty space in original file
                if(!line.isBlank()){
                    //Skip comment block
                    if(line.contains("/*")){
                        while(!line.contains("*/")) line = gScanner.nextLine();
                        line = gScanner.nextLine();
                    }

                    //Should be at key, process start
                    if(line.contains(":")){
                        ArrayList<String> allValues = new ArrayList<>();
                        String[] tokensLine = line.split(":");
                        String key = tokensLine[0];

                        if(tokensLine.length == 1) allValues.add(null);     //Can change null to ("") for no argument
                        else allValues.add(tokensLine[1]);

                        //Get alternative values
                        line = gScanner.nextLine();
                        while(line.contains("|")){
                            tokensLine = line.split("\\|");
                            allValues.add(tokensLine[1]);
                            line = gScanner.nextLine();
                        } //Exits when line is ";"
                        //Add key and value pair
                        grammarDict.put(key, allValues);
                    }
                }
            } //END while(gScanner.hasNext())
            gScanner.close();
        } catch(FileNotFoundException e){ System.out.println("File not found at " + filePath); }
        /*     Uncomment this to test getGrammar()
        for(Map.Entry<String, ArrayList<String>> entry : grammarDict.entrySet()) {
            System.out.println("Key: " + entry.getKey());
            for(String value : entry.getValue()) {
               System.out.println("\t" + value);
            }
        }
        */
    }//END getGrammar()
}//END CLASS