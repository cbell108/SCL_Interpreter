/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 2nd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: ParseTreeNode.cs
 * October 22, 2023
*/
using System;
using System.Collections.Generic;
using System.Xml.Linq;

// ParseTreeNode class
class ParseTreeNode
{
    /// <summary>
    /// The raw string value of the node
    /// </summary>
    public string RawValue;


    /// <summary>
    /// The parsed token object of the node
    /// </summary>
    public Token? ParsedToken;


    /// <summary>
    /// The child nodes of the node
    /// </summary>
    public List<ParseTreeNode> Children;


    /// <summary>
    /// Constructor for nonterminals
    /// </summary>
    /// <param name="RawValue"></param>
    public ParseTreeNode(string RawValue)
    {
        this.RawValue = RawValue;
        this.Children = new List<ParseTreeNode>();
    }


    /// <summary>
    /// Constructor for terminals (tokens)
    /// </summary>
    /// <param name="ParsedToken"></param>
    public ParseTreeNode(Token ParsedToken)
    {
        this.ParsedToken = ParsedToken;
        this.RawValue = ParsedToken.GetValue();
        this.Children = new List<ParseTreeNode>();
    }


    /// <summary>
    /// Adds the specified node as a child node of this node
    /// </summary>
    /// <param name="child"></param>
    public void AddChild(ParseTreeNode child)
    {
        Children.Add(child);
    }


    /// <summary>
    /// Detects if the node has a token object in ParsedToken or not
    /// </summary>
    /// <returns></returns>
    public bool HasToken()
    {
        if (ParsedToken != null) return true;
        return false;
    }
}