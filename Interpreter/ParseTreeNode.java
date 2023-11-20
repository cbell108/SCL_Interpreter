/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 3rd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: ParseTreeNode.java
 * November 19, 2023
 */

import java.util.ArrayList;
import java.util.List;

// ParseTreeNode class
class ParseTreeNode
{
    /// <summary>
    /// The raw string value of the node
    /// </summary>
    public String RawValue;


    /// <summary>
    /// The parsed token object of the node
    /// </summary>
    public Token ParsedToken;


    /// <summary>
    /// The child nodes of the node
    /// </summary>
    public List<ParseTreeNode> Children;


    /// <summary>
    /// Constructor for nonterminals
    /// </summary>
    /// <param name="RawValue"></param>
    public ParseTreeNode(String RawValue)
    {
        this.RawValue = RawValue;
        this.Children = new ArrayList<ParseTreeNode>();
    }


    /// <summary>
    /// Constructor for terminals (tokens)
    /// </summary>
    /// <param name="ParsedToken"></param>
    public ParseTreeNode(Token ParsedToken)
    {
        this.ParsedToken = ParsedToken;
        this.RawValue = ParsedToken.getValue();
        this.Children = new ArrayList<ParseTreeNode>();
    }


    /// <summary>
    /// Adds the specified node as a child node of this node
    /// </summary>
    /// <param name="child"></param>
    public void AddChild(ParseTreeNode child)
    {
        Children.add(child);
    }


    /// <summary>
    /// Detects if the node has a token object in ParsedToken or not
    /// </summary>
    /// <returns></returns>
    public boolean HasToken()
    {
        if (ParsedToken != null) return true;
        return false;
    }
}