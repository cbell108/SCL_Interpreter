/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 2nd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: Scanner.cs
 * October 22, 2023
*/

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

// Scanner Class
public class Scanner
{
    /// <summary>
    /// Indexes: 0: keyword, 1 is identifier, 2: operator, 3: special_symbol, 4: type, 5: literal
    /// </summary>
    List<List<Token>> TokenDictionary;


    /// <summary>
    /// List of Token objects containing the final order of the tokens taken from the input file.
    /// </summary>
    List<Token> FinalTokenList;


    /// <summary>
    /// Keeps track of which line is being scanned
    /// </summary>
    int LineNumber = 0;


    /// <summary>
    /// Creates an SCLScanner object, getting initial tokens and creating an empty final list for tokenized output
    /// </summary>
    public Scanner()
    {
        TokenDictionary = FillInitialTokens();
        FinalTokenList = new List<Token>();
    }


    /// <summary>
    /// Calls FilterScan, checks if it generated a list, creates a JsonWriterClass object that writes the JSON file.
    /// </summary>
    /// <param name="Filepath"></param>
    public List<Token> Scan(string Filepath)
    {
        if (FilterScan(Filepath) == null) throw new Exception("FilterScan() returned an empty list");
        return FinalTokenList;
    }


    /// <summary>
    /// Returns false if string Value is not added to FinalTokenList as a token
    /// </summary>
    /// <param name="Value"></param>
    public bool CheckNormalToken(string Value)
    {
        //Identifies if current token is in any token category
        if (ContainsValue(Value))
        {
            if (ContainsValue("keyword", Value))
            {
                FinalTokenList.Add(new Token(GetToken("keyword", Value), LineNumber));
            }
            else if (ContainsValue("identifier", Value))
            {
                FinalTokenList.Add(new Token(GetToken("identifier", Value), LineNumber));
            }
            else if (ContainsValue("operator", Value))
            {
                FinalTokenList.Add(new Token(GetToken("operator", Value), LineNumber));
            }
            else if (ContainsValue("special_symbol", Value))
            {
                FinalTokenList.Add(new Token(GetToken("special_symbol", Value), LineNumber));
            }
            else if (ContainsValue("type", Value))
            {
                FinalTokenList.Add(new Token(GetToken("type", Value), LineNumber));
            }
            else if (ContainsValue("literal", Value))
            {
                FinalTokenList.Add(new Token(GetToken("literal", Value), LineNumber));
            }
            return true;
        }
        else if (IsInt(Value) || IsFloat(Value)) //a constant is a constant, not a general custom token
        {
            FinalTokenList.Add(new Token(new Token("constant", Value), LineNumber));
            return true;
        }
        else return false;
    }


    /// <summary>
    /// Checks SCL operator and returns what it is
    /// </summary>
    /// <param name="Operator"></param>
    /// <returns></returns>
    public string CheckWhichOperator(string Operator)
    {
        if (Operator.Contains("<=")) return "<=";
        if (Operator.Contains(">=")) return ">=";
        if (Operator.Contains("^")) return "^";
        if (Operator.Contains("<")) return "<";
        if (Operator.Contains(">")) return ">";
        if (Operator.Contains("*")) return "*";
        if (Operator.Contains("/")) return "/";
        if (Operator.Contains("+")) return "+";
        if (Operator.Contains("-")) return "-";
        if (Operator.Contains("=")) return "=";
        if (Operator.Contains("add")) return "add";
        return null;
    }


    /// <summary>
    /// Adds the EndOfStatement token to the FinalTokenList
    /// </summary>
    public void AddEndOfStatementToken()
    {
        FinalTokenList.Add(new Token(GetToken("end_of_statement", "EOS"), LineNumber));
    }


    /// <summary>
    /// Adds the specified operator token using the Operator string
    /// </summary>
    /// <param name="Operator"></param>
    public void AddOperator(string Operator)
    {
        switch (Operator)
        {
            case " +":
                FinalTokenList.Add(new Token(GetToken("operator", 400), LineNumber));
                break;
            case " -":
                FinalTokenList.Add(new Token(GetToken("operator", 401), LineNumber));
                break;
            case " *":
                FinalTokenList.Add(new Token(GetToken("operator", 402), LineNumber));
                break;
            case " /":
                FinalTokenList.Add(new Token(GetToken("operator", 403), LineNumber));
                break;
            case " ^":
                FinalTokenList.Add(new Token(GetToken("operator", 404), LineNumber));
                break;
            case " >":
                FinalTokenList.Add(new Token(GetToken("operator", 405), LineNumber));
                break;
            case " <":
                FinalTokenList.Add(new Token(GetToken("operator", 406), LineNumber));
                break;
            case " =":
                FinalTokenList.Add(new Token(GetToken("operator", 408), LineNumber));
                break;
            case "add":
                FinalTokenList.Add(new Token(GetToken("operator", 409), LineNumber));
                break;
            case "<=":
                FinalTokenList.Add(new Token(GetToken("operator", 410), LineNumber));
                break;
            case ">=":
                FinalTokenList.Add(new Token(GetToken("operator", 411), LineNumber));
                break;
        }
        Console.WriteLine(FinalTokenList[FinalTokenList.Count - 1]);
    }


    /// <summary>
    /// Filters File, returns a complete list of the list of the tokens
    /// </summary>
    /// <param name="Filename"></param>
    /// <returns></returns>
    public List<Token> FilterScan(string Filename)
    {
        try
        {
            StreamReader SCLFileReader = new StreamReader(Filename);
            bool Comment = false;
            //Loop until end of line
            while (!SCLFileReader.EndOfStream)
            {
                //Read next line, add to LineNumber counter
                string line = SCLFileReader.ReadLine();
                LineNumber++;

                //Filter out whitespace
                if (string.IsNullOrWhiteSpace(line) || string.IsNullOrEmpty(line))
                {
                    continue;
                }
                line = line.Trim();

                //Filter out comments
                if (line.Contains("/*")) Comment = true;
                if (line.Contains("*/"))
                {
                    Comment = false;
                    continue;
                }
                if (Comment) continue;
                if (line.Contains("//")) line = line.Substring(0, line.IndexOf("//"));

                //Break string into potential tokens
                string[] UnformattedSplitLine = line.Split(' ');
                List<string> TempList = new List<string>();
                for (int i = 0; i < UnformattedSplitLine.Count(); i++)
                {
                    if (!string.IsNullOrEmpty(UnformattedSplitLine[i])) TempList.Add(UnformattedSplitLine[i]);
                }
                string[] SplitLine = TempList.ToArray();

                //Validate potential tokens as tokens
                for (int i = 0; i < SplitLine.Length; i++)
                {
                    //If string of the SplitLine is a normal token
                    if (CheckNormalToken(SplitLine[i]))
                    {
                        continue;
                    }

                    //Filter out new literals
                    else if (SplitLine[i].Contains("\""))
                    {
                        string Literal = SplitLine[i];
                        if (SplitLine[i].Substring(0, 1) == "\"" && SplitLine[i].Substring(SplitLine[i].Length - 1, 1) == "\"")
                        {
                            Token NewLiteral = new Token("literal", SplitLine[i]);
                            TokenDictionary[5].Add(NewLiteral);
                            FinalTokenList.Add(new Token(NewLiteral, LineNumber));
                        }
                        for (int j = i + 1; j < SplitLine.Length; j++)
                        {
                            if (SplitLine[j].Contains("\""))
                            {
                                bool ContainsComma = false;
                                i = j;
                                if (SplitLine[j][SplitLine[j].Length - 1].Equals(','))
                                {
                                    Literal += " " + SplitLine[j].Substring(0, SplitLine[j].Length - 1);
                                    ContainsComma = true;
                                }
                                else Literal += " " + SplitLine[j];
                                Token NewLiteral = new Token("literal", Literal);
                                TokenDictionary[5].Add(NewLiteral);
                                FinalTokenList.Add(new Token(NewLiteral, LineNumber));
                                if (ContainsComma)
                                {
                                    FinalTokenList.Add(new Token(GetToken("special_symbol", ","), LineNumber));
                                    ContainsComma = false;
                                }
                                break;
                            }
                            else Literal += " " + SplitLine[j];
                        }
                    }

                    //Filter out operators
                    else if (SplitLine[i].Contains("^") || SplitLine[i].Contains("<") || SplitLine[i].Contains(">") || SplitLine[i].Contains("*") || SplitLine[i].Contains("/") || SplitLine[i].Contains("+") || SplitLine[i].Contains("-") || SplitLine[i].Contains("="))
                    {
                        if (SplitLine[i].Length > 1)
                        {
                            int OpIndex = SplitLine[i].IndexOf(CheckWhichOperator(SplitLine[i]));
                            string Before = SplitLine[i].Substring(0, OpIndex);
                            if (!CheckNormalToken(Before))
                            {
                                Token NewToken = new Token("identifier", Before);
                                TokenDictionary[1].Add(NewToken);
                                FinalTokenList.Add(new Token(NewToken, LineNumber));
                            }
                            if (CheckWhichOperator(SplitLine[i]).Length == 1) AddOperator(CheckWhichOperator(SplitLine[i]));//CheckNormalToken(" " + CheckWhichOperator(SplitLine[i]));
                            else CheckNormalToken(CheckWhichOperator(SplitLine[i]));
                            string After = SplitLine[i].Substring(OpIndex + 1);
                            if (!CheckNormalToken(After))
                            {
                                Token NewToken = new Token("identifier", After);
                                TokenDictionary[1].Add(NewToken);
                                FinalTokenList.Add(new Token(NewToken, LineNumber));
                            }
                        }
                        else
                        {
                            FinalTokenList.Add(new Token(GetToken("operator", SplitLine[i]), LineNumber));
                        }
                    }

                    //Add identifier if not previously made
                    else
                    {
                        Token NewToken = new Token("identifier", SplitLine[i]);
                        TokenDictionary[1].Add(NewToken);
                        FinalTokenList.Add(new Token(NewToken, LineNumber));
                    }
                }
                AddEndOfStatementToken();
            }

            //Prints to screen all newly created tokens form the SCL file
            //COMMENTS: part of original scanner implementation
            //COMMENTS: not needed in recent deliverables
            /*for (int i = 0; i < FinalTokenList.Count; i++)
            {
                Console.WriteLine("New Token Created: " + FinalTokenList[i]);
            }
            Console.WriteLine("End of New Tokens\n");*/
            return FinalTokenList;
        }

        catch (FileNotFoundException) //If file is not valid
        {
            Console.WriteLine("No file or directory: " + Filename);
            return null;
        }

        catch (Exception E) //Error reporting
        {
            Console.WriteLine("Error: " + E);
            return null;
        }
    }


    /// <summary>
    /// Returns true if string is a float.
    /// </summary>
    /// <param name="num"></param>
    /// <returns></returns>
    private static bool IsFloat(string num)
    {
        return float.TryParse(num, out _);
    }


    /// <summary>
    /// Returns true if string is an int.
    /// </summary>
    /// <param name="num"></param>
    /// <returns></returns>
    private static bool IsInt(string num)
    {
        return int.TryParse(num, out _);
    }


    /// <summary>
    /// Gets the subdictionary list of the specified string Type,
    /// Types: keyword, operator, identifier, special_symbol, type, literal
    /// </summary>
    /// <param name="Type"></param>
    /// <returns></returns>
    /// <exception cref="Exception"></exception>
    private List<Token> GetSubDict(string Type)
    {
        switch (Type)
        {
            case "keyword":
                return TokenDictionary[0];
            case "operator":
                return TokenDictionary[1];
            case "identifier":
                return TokenDictionary[2];
            case "special_symbol":
                return TokenDictionary[3];
            case "type":
                return TokenDictionary[4];
            case "literal":
                return TokenDictionary[5];
            case "end_of_statement":
                return TokenDictionary[6];
            case "constant":
                return TokenDictionary[7];
            default:
                throw new Exception("Looked for dictionary that does not exist");
        }
    }


    /// <summary>
    /// Looks for token in TokenDictionary, if found returns true
    /// </summary>
    /// <param name="Value"></param>
    /// <returns></returns>
    private bool ContainsValue(string Value)
    {
        //needs to do switch statement with subdictionary selected by that
        for (int i = 0; i < TokenDictionary.Count; i++)
        {
            for (int j = 0; j < TokenDictionary[i].Count; j++)
            {
                if (TokenDictionary[i][j].GetValue() == Value)
                {
                    return true;
                }
            }
        }
        return false;
    }


    /// <summary>
    /// Looks for token in the token sublist, if found returns true;
    /// Types: keyword, operator, identifier, special_symbol, unknown_symbol, literal
    /// </summary>
    /// <param name="TokenSubDict"></param>
    /// <param name="Type"></param>
    /// <param name="Value"></param>
    /// <returns></returns>
    private bool ContainsValue(string Type, string Value)
    {
        //needs to do switch statement with subdictionary selected by that
        List<Token> SubDictionary = GetSubDict(Type);
        for (int i = 0; i < SubDictionary.Count; i++)
        {
            if (SubDictionary[i].GetValue() == Value)
            {
                return true;
            }
        }
        return false;
    }


    /// <summary>
    /// Looks for token in the token sublist, if not found returns null,
    /// Types: keyword, operator, identifier, special_symbol, unknown_symbol, literal, end_of_statement
    /// </summary>
    /// <param name="TokenSubDict"></param>
    /// <param name="Type"></param>
    /// <param name="Value"></param>
    /// <returns></returns>
    private Token GetToken(string Type, string Value)
    {
        //needs to do switch statement with subdictionary selected by that
        List<Token> SubDictionary = GetSubDict(Type);
        for (int i = 0; i < SubDictionary.Count; i++)
        {
            if (SubDictionary[i].GetValue() == Value)
            {
                return SubDictionary[i];
            }
        }
        return null;
    }


    /// <summary>
    /// Returns token with specified id, null if not found
    /// </summary>
    /// <param name="type"></param>
    /// <param name="ID"></param>
    /// <returns></returns>
    private Token GetToken(string Type, int ID)
    {
        //needs to do switch statement with subdictionary selected by that
        List<Token> SubDictionary = GetSubDict(Type);
        for (int i = 0; i < SubDictionary.Count; i++)
        {
            if (SubDictionary[i].GetID() == ID)
            {
                return SubDictionary[i];
            }
        }
        return null;
    }


    /// <summary>
    /// Returns a filled List dictionary of token objects.
    /// Indexes: 0: keyword, 1 is identifier, 2: operator, 3: special_symbol, 4: type, 5: literal, 6: end_of_statement, 7: constant
    /// </summary>
    /// <returns></returns>
    private List<List<Token>> FillInitialTokens()
    {
        List<List<Token>> TokenDict = new List<List<Token>>();
        List<Token> Keywords = new List<Token>();
        List<Token> Operators = new List<Token>();
        List<Token> SpecialSymbols = new List<Token>();
        List<Token> Identifiers = new List<Token>();
        List<Token> Types = new List<Token>();
        List<Token> Literals = new List<Token>();
        List<Token> EndOfStatements = new List<Token>();
        List<Token> Constants = new List<Token>();

        Keywords.Add(new Token("keyword", "return", 0));
        Keywords.Add(new Token("keyword", "print", 1));

        Operators.Add(new Token("operator", "+", 400));
        Operators.Add(new Token("operator", "-", 401));
        Operators.Add(new Token("operator", "*", 402));
        Operators.Add(new Token("operator", "/", 403));
        Operators.Add(new Token("operator", ">", 404));
        Operators.Add(new Token("operator", "<", 405));
        Operators.Add(new Token("operator", "=", 406));
        Operators.Add(new Token("operator", "<=", 407));
        Operators.Add(new Token("operator", ">=", 408));

        SpecialSymbols.Add(new Token("special_symbol", ",", 800));
        SpecialSymbols.Add(new Token("special_symbol", ".", 801));

        Identifiers.Add(new Token("identifier", "main", 100));

        Types.Add(new Token("type", "int", 900));
        Types.Add(new Token("type", "float", 901));
        Types.Add(new Token("type", "char", 902));
        Types.Add(new Token("type", "void", 903));
        Types.Add(new Token("type", "bool", 904));

        EndOfStatements.Add(new Token("end_of_statement", "EOS", 1000));

        TokenDict.Add(Keywords);
        TokenDict.Add(Identifiers);
        TokenDict.Add(Operators);
        TokenDict.Add(SpecialSymbols);
        TokenDict.Add(Types);
        TokenDict.Add(Literals);
        TokenDict.Add(EndOfStatements);
        TokenDict.Add(Constants);
        return TokenDict;
    }
}