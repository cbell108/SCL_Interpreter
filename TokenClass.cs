using System;
using System.Collections.Generic;
using System.IO;

public class Token
{
    string Type, Value;
    int ID;
    static int IDCounter = 100;

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

    public override string ToString()
    {
        return "['" + Type + "', " + ID + ", '" + Value.Trim() + "']";
    }
}