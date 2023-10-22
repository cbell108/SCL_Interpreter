/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 2nd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: TokenClass.cs
 * October 22, 2023
*/

using System;
using System.Collections.Generic;
using System.IO;

/// <summary>
/// Stores the generated tokens in a string Type, string Value, int ID format
/// </summary>
public class Token
{
    //Declared variables to store Token object values
    string Type, Value;
    int ID;
    int? LineNum;
    static int IDCounter = 101;

    /// <summary>
    /// Builds new token of specified Type, Value, and ID; Types: keyword, operator, identifier, special_symbol, unknown_symbol, literal
    /// </summary>
    /// <param name="Type"></param>
    /// <param name="Value"></param>
    /// <param name="ID"></param>
    public Token(string Type, string Value, int ID)
    {
        this.Type = Type;
        this.ID = ID;
        this.Value = Value;
    }



    /// <summary>
    /// Builds new token of specified Type and Value
    /// </summary>
    /// <param name="Type"></param>
    /// <param name="Value"></param>
    public Token(string Type, string Value)
    {
        this.Type = Type;
        this.Value = Value;
        this.ID = IDCounter++;
    }



    /// <summary>
    /// Creates token object with line number
    /// </summary>
    /// <param name="OriginalToken"></param>
    /// <param name="LineNum"></param>
    public Token(int ID, string Value, string Type, int LineNum)
    {
        this.LineNum = LineNum;
        this.ID = ID;
        this.Value = Value;
        this.Type = Type;
    }



    /// <summary>
    /// Creates token object from a token objecct with line number
    /// </summary>
    /// <param name="OriginalToken"></param>
    /// <param name="LineNum"></param>
    public Token(Token OriginalToken, int LineNum)
    {
        this.LineNum = LineNum;
        this.ID = OriginalToken.GetID();
        this.Value = OriginalToken.GetValue();
        this.Type = OriginalToken.GetTypeName();
    }



    /// <summary>
    /// Returns integer ID of the Token object
    /// </summary>
    /// <returns></returns>
    public int GetID()
    {
        return ID;
    }



    /// <summary>
    /// Returns string Value of the Token object
    /// </summary>
    /// <returns></returns>
    public string GetValue()
    {
        return Value;
    }



    /// <summary>
    /// Returns string Type of the Token object
    /// </summary>
    /// <returns></returns>
    public string GetTypeName()
    {
        return Type;
    }



    //
    public int GetLineNum()
    {
        if (LineNum != null)
        {
            return (int)LineNum;
        }
        else throw new Exception("line num not initialized");
    }
    /// <summary>
    /// Returns a formatted string of the Token object
    /// </summary>
    /// <returns></returns>
    public override string ToString()
    {
        return "['" + Type + "', " + ID + ", '" + Value.Trim() + "']";
    }
}