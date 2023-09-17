using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
public class MainClass
{
    public static void Main(string[] args)
    {
        //Get grammar dictionary
        //Make it return the dictionary or call later
        GetGrammar();
    }//END MAIN
    private static void GetGrammar()
    {
        //Get file
        String filePath = "C:\\Users\\carde\\Documents\\Fall 2023\\CS 4308 Concepts of Prog Langs\\Deliv 1 Repo\\SCL_Interpreter\\res\\Modules_3_5_7_Project Resources (Copy)\\scl_grammar.txt";

        //Key: keyword, Values: list of legal arguments
        Dictionary<String, List<String>> grammarDict = new Dictionary<String, List<String>>();

        try
        {
            //Open file with scanner
            //File gFile = new File(filePath);
            StreamReader gFile = new StreamReader(filePath);
            //Scanner gScanner = new Scanner(gFile);

            //Loop through each line
            while (!gFile.EndOfStream)
            {
                string line = gFile.ReadLine();
                line.Trim();

                //Skip empty space in original file
                if (!string.IsNullOrWhiteSpace(line))
                {
                    //Skip comment block
                    if (line.Contains("/*"))
                    {
                        while (!line.Contains("*/")) line = gFile.ReadLine();
                        line = gFile.ReadLine();
                    }

                    //Should be at key, process start
                    if (line.Contains(":"))
                    {
                        List<string> allValues = new List<string>();
                        string[] tokensLine = line.Split(":");
                        string key = tokensLine[0];

                        if (tokensLine.Length == 1) allValues.Add(null);     //Can change null to ("") for no argument
                        else allValues.Add(tokensLine[1]);

                        //Get alternative values
                        line = gFile.ReadLine();
                        while (line.Contains("|"))
                        {
                            tokensLine = line.Split("|");
                            /*for (int i = 0; i <  tokensLine.Length; i++)
                            {
                                tokensLine[i].Trim();
                                tokensLine[i] = tokensLine[i].Substring(1, tokensLine[i].Length - 2);
                                Console.WriteLine(tokensLine[i]);
                            }*/
                            allValues.Add(tokensLine[1]);
                            line = gFile.ReadLine();
                        } //Exits when line is ";"
                        //Add key and value pair
                        grammarDict[key] = allValues;
                    }
                }
            } //END while(gScanner.hasNext())
            gFile.Close();
        }
        catch (FileNotFoundException e) 
        { 
            Console.WriteLine("File not found at " + filePath); 
        }

        //   Uncomment this to test getGrammar()
        foreach (var Entry in grammarDict)
        {
            Console.WriteLine("Key: " + Entry.Key);
            foreach (string value in Entry.Value)
            {
                Console.WriteLine("\t" + value);
            }
        }

    }//END getGrammar()
}//END CLASS
