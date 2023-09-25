/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 1st Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: JsonWriteClass.cs
 * September 24, 2023
*/

using System;
using System.IO;
using System.Collections.Generic;

// JsonWriterClass Class
public class JsonWriterClass
{
    // Declared StreamWriter to write to a file
    StreamWriter JsonWriter;
    List<Token> TokenList;

    /// <summary>
    /// Create JsonWriterClass object, writing the JSON file from the FinalTokenList at the selected Filename
    /// </summary>
    /// <param name="Filename"></param>
    /// <param name="FinalTokenList"></param>
    public JsonWriterClass(string Filename, List<Token> FinalTokenList)
    {
        JsonWriter = new StreamWriter(Filename, true);
        this.TokenList = FinalTokenList;
        WriteJsonFile();
    }

    //WriteJsonFile Method
    private void WriteJsonFile()
    {
        try
        {
            if (TokenList.Count > 0) //Checks TokenList is empty
            {
                //Writes to file in JSON format
                JsonWriter.WriteLine("{");
                int TokenCounter = 0;
                foreach (Token T in TokenList) //Loop to write each token to the file
                {
                    JsonWriter.WriteLine("  \"Token_" + TokenCounter + "\": {");
                    JsonWriter.WriteLine("    \"Type\": \"" + T.GetTypeName() + "\",");
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

        catch (Exception e)
        { 
            Console.WriteLine("Error: " + e.Message); 
        }
        
        JsonWriter.Close(); //SAVE THOSE RESOURCES
    }
}