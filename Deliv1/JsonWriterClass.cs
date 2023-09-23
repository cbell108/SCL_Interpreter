using System;
using System.IO;
using System.Collections.Generic;

//JsonWriterClass Class
public class JsonWriterClass
{
    //Declared StreamWriter to write to a file
    StreamWriter JsonWriter;
    List<Token> TokenList;

    //Constructor
    public JsonWriterClass(string Filename, List<Token> FinalTokenList)
    {
        JsonWriter = new StreamWriter(Filename, true);
        this.TokenList = FinalTokenList;
        WriteJsonFile();
    }

    //WriteJsonFile Method
    public void WriteJsonFile()
    {
        try //In case of oopsies
        {
            if (TokenList.Count > 0) //Checks TokenList is empty
            {
                //Writes to file in JSON format
                JsonWriter.WriteLine("{");
                int TokenCounter = 0;
                foreach (Token T in TokenList) //Loop to write each token to the file
                {
                    JsonWriter.WriteLine("  \"Token_" + TokenCounter + "\": {");
                    JsonWriter.WriteLine("    \"Type\": \"" + T.GetType() + "\",");
                    JsonWriter.WriteLine("    \"ID\": \"" + T.GetID() + "\",");
                    JsonWriter.WriteLine("    \"Value\": \"" + T.GetValue() + "\",");
                    if (TokenCounter == TokenList.Count - 1) JsonWriter.WriteLine("  }");
                    else JsonWriter.WriteLine("  },");
                    TokenCounter++;
                }
                JsonWriter.WriteLine("}");
            }
            else throw new Exception("TokenList is empty");
        }

        catch (Exception e) //Gotta catch those oopsies
        { 
            Console.WriteLine("Error: " + e.Message); 
        }
        
        JsonWriter.Close(); //SAVE THOSE RESOURCES
    }
}
