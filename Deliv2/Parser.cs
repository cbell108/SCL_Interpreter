/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 2nd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: Parser.cs
 * October 22, 2023
*/
using System;
using System.Collections.Generic;
using System.Xml.Linq;

// Parser class
class Parser
{

    /// <summary>
    /// Indexes: 0: keyword, 1 is identifier, 2: operator, 3: special_symbol, 4: type, 5: literal, 6: end_of_statement, 7: constant
    /// </summary>
    private List<Token> Tokens;


    /// <summary>
    /// Holds the current token index of the current token being evaluated in the list
    /// </summary>
    private int CurrentTokenIndex = 0;


    /// <summary>
    /// Default constructor
    /// </summary>
    public Parser() { }


    /// <summary>
    /// Initializes parser with valid variables
    /// </summary>
    /// <param name="tokenList"></param>
    public void InitializeParser(List<Token> tokenList)
    {
        Tokens = tokenList;
        CurrentTokenIndex = 0;
    }


    /// <summary>
    /// Diplays the preorder traversal of the current parse tree
    /// </summary>
    /// <param name="Node"></param>
    public void PreOrderTraversal(ParseTreeNode Node)
    {
        if (Node == null)
            return;

        Console.Write(Node.RawValue + " ");

        for (int i = 0; i < Node.Children.Count; i++)
        {
            PreOrderTraversal(Node.Children[i]);
        }
    }


    /// <summary>
    /// Displays only the Tokens of the preorder traversal of the current parse tree
    /// </summary>
    /// <param name="Node"></param>
    public void PreOrderTraversalOfTokens(ParseTreeNode Node)
    {
        if (Node == null) return;

        if (Node.HasToken())
        {
            if (Node.ParsedToken.GetValue() == "EOS")
            {
                Console.WriteLine();
            }
            else Console.Write(Node.RawValue + " ");
        }

        for (int i = 0; i < Node.Children.Count; i++)
        {
            PreOrderTraversalOfTokens(Node.Children[i]);
        }
    }


    /// <summary>
    /// Parses the start non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode Start()
    {
        Console.WriteLine("Entering <start>");

        ParseTreeNode StartNode = new ParseTreeNode("start");
        StartNode.AddChild(ParseFunction());

        Console.WriteLine("Exiting <start>");

        return StartNode;
    }


    /// <summary>
    /// Parses the function non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseFunction()
    {
        Console.WriteLine("Entering <function>");

        ParseTreeNode FunctionNode = new ParseTreeNode("function");

        //Parse RHS type identifier parameter ENDLINE expression
        if (MatchTokenType("type"))
        {
            FunctionNode.AddChild(ParseType()); // Parse type
            FunctionNode.AddChild(ParseIdentifier()); // Parse identifier
            FunctionNode.AddChild(ParseParameter()); // Parse parameter
            FunctionNode.AddChild(ParseEndline()); // Parse ENDLINE
            FunctionNode.AddChild(ParseExpression()); // Parse expression
        }
        // Parse RHS (left blank)
        else if (MatchTokenType("end_of_statement"))
        {
            //if function becomes (left blank)
        }
        else
        {
            //if function becomes nothing, without next token being EOS
        }

        Console.WriteLine("Exiting <function>");
        return FunctionNode;
    }


    /// <summary>
    /// Parses the parameter non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseParameter()
    {
        Console.WriteLine("Entering <parameter>");

        ParseTreeNode parameterNode = new ParseTreeNode("parameter");

        //Parse RHS parameter parameter
        if (MatchTokenType("type") && MatchTokenType("identifier", 1) && MatchTokenType("type", 2) && MatchTokenType("identifier", 3))
        {
            parameterNode.AddChild(ParseType()); // Parse the type
            parameterNode.AddChild(ParseIdentifier()); // Parse the identifier
            parameterNode.AddChild(ParseParameter()); // parse the remaining parameter
        }
        // Parse RHS type IDENTIFIER
        else if (MatchTokenType("type"))
        {
            parameterNode.AddChild(ParseType()); // Parse the type
            parameterNode.AddChild(ParseIdentifier()); //Parse the identifier
        }
        // Parse RHS (left blank)
        else if (MatchTokenType("end_of_statement"))
        {
            // Parse the (left blank)
        }

        Console.WriteLine("Exiting <parameter>");

        return parameterNode;
    }


    /// <summary>
    /// Parses the type non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseType()
    {
        Console.WriteLine("Entering <type>");

        ParseTreeNode TypeNode = new ParseTreeNode("type");

        if (!MatchTokenType("type"))
        {
            throw new Exception("invalid type at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
        }
        TypeNode.AddChild(GetNextToken());

        Console.WriteLine("Exiting <type>");

        return TypeNode;
    }


    /// <summary>
    /// Parses the expression non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseExpression()
    {
        Console.WriteLine("Entering <expression>");

        ParseTreeNode ExpressionNode = new ParseTreeNode("expression");

        // Parse RHS type identifier ENDLINE expression
        if (MatchTokenType("type") && MatchTokenType("identifier", 1))
        {
            ExpressionNode.AddChild(ParseType()); // Parse the type
            ExpressionNode.AddChild(ParseIdentifier()); // Parse the identifier
            ExpressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
            ExpressionNode.AddChild(ParseExpression()); //Parse the expression
        }
        // Parse RHS IDENTIFIER operator returnable expression
        else if (MatchTokenType("identifier") && MatchTokenType("operator", 1))
        {
            ExpressionNode.AddChild(ParseIdentifier()); // Parse the identifier
            ExpressionNode.AddChild(ParseOperator()); // Parse the operator
            ExpressionNode.AddChild(ParseReturnable()); // Parse the returnable
            ExpressionNode.AddChild(ParseExpression()); // Parse the expression
        }
        // Parse RHSs...
        else if (MatchTokenType("keyword"))
        {
            // PRINT returnable expression
            if (Tokens[CurrentTokenIndex].GetValue() == "print")
            {
                ExpressionNode.AddChild(GetNextToken()); // Parse the print
                ExpressionNode.AddChild(ParseReturnable()); // Parse the returnable
                ExpressionNode.AddChild(ParseExpression()); // Parse the expression
            }
            // RETURN returnable ENDLINE expression
            else if (Tokens[CurrentTokenIndex].GetValue() == "return")
            {
                ExpressionNode.AddChild(GetNextToken()); // Parse the return
                ExpressionNode.AddChild(ParseReturnable()); // Parse the returnable
                ExpressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
                ExpressionNode.AddChild(ParseFunction()); // Parse the function
            }
        }
        // Parse RHS ENDLINE expression
        else if (MatchTokenType("end_of_statement"))
        {
            ExpressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
            ExpressionNode.AddChild(ParseExpression()); // Parse the expression
        }
        // Parse RHS (left blank)
        else
        {
            if (Tokens[CurrentTokenIndex] != null)
            {
                throw new Exception("invalid syntax at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
            }
            //If expression becomes nothing
        }

        Console.WriteLine("Exiting <expression>");

        return ExpressionNode;
    }


    /// <summary>
    /// Parse the identifier non-terminal
    /// </summary>
    /// <returns></returns>
    private ParseTreeNode ParseIdentifier()
    {
        Console.WriteLine("Entering <identifier>");

        ParseTreeNode IdentNode = new ParseTreeNode("identifier");

        if (!MatchTokenType("identifier"))
        {
            throw new Exception("invalid identifier at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
        }
        IdentNode.AddChild(GetNextToken()); // Parse the identifier

        Console.WriteLine("Exiting <identifier>");
        return IdentNode;
    }


    /// <summary>
    /// Parse the constant non-terminal
    /// </summary>
    /// <returns></returns>
    private ParseTreeNode ParseConstant()
    {
        Console.WriteLine("Entering <constant>");

        ParseTreeNode ConstNode = new ParseTreeNode("constant");

        if (!MatchTokenType("constant"))
        {
            throw new Exception("invalid constant at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
        }
        ConstNode.AddChild(GetNextToken()); // Parse the constant

        Console.WriteLine("Exiting <constant>");

        return ConstNode;
    }


    /// <summary>
    /// Parse the constant non-terminal
    /// </summary>
    /// <returns></returns>
    private ParseTreeNode ParseOperator()
    {
        Console.WriteLine("Entering <operator>");

        ParseTreeNode OperNode = new ParseTreeNode("operator");

        if (!MatchTokenType("operator"))
        {
            throw new Exception("invalid operator at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
        }
        OperNode.AddChild(GetNextToken()); // Parse the operator

        Console.WriteLine("Exiting <operator>");

        return OperNode;
    }


    // Parse the endline terminal
    private ParseTreeNode ParseEndline()
    {
        Console.WriteLine("Entering <endline>");

        if (!MatchTokenType("end_of_statement"))
        {
            throw new Exception("invalid syntax at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
        }

        Console.WriteLine("Exiting <endline>");

        return GetNextToken(); // Parse the ENDLINE
    }


    /// <summary>
    /// Parses the returnable non-terminal
    /// </summary>
    /// <returns></returns>
    private ParseTreeNode ParseReturnable()
    {
        Console.WriteLine("Entering <returnable>");

        ParseTreeNode ReturnableNode = new ParseTreeNode("returnable");
        // Parse RHS (left blank) version 1
        if (MatchTokenType("identifier") && MatchTokenType("operator", 1))
        {
            //if returnable becomes nothing, following is expression
        }
        // Parse RHS identifier
        else if (MatchTokenType("identifier"))
        {
            ReturnableNode.AddChild(ParseIdentifier());
        }
        // Parse RHS constant
        else if (MatchTokenType("constant"))
        {
            ReturnableNode.AddChild(ParseConstant());
        }
        // Parse RHS (left blank) version 2
        else if (MatchTokenType("end_of_statement"))
        {
            //if returnable becomes nothing
        }
        // No valid RHS detected
        else
        {
            throw new Exception("expected a returnable token at line " + Tokens[CurrentTokenIndex].GetLineNum() + ".");
        }

        Console.WriteLine("Exiting <returnable>");

        return ReturnableNode;
    }


    /// <summary>
    /// Begins parser, displays preorder traversal
    /// </summary>
    public void Begin()
    {
        try
        {
            Console.WriteLine("Beginning parse of specified file");
            ParseTreeNode ParseTree = Start(); // Generate parse tree
            Console.WriteLine();
            Display(ParseTree); // Display the parse tree
        }
        catch (Exception E)
        {
            Console.WriteLine("Error: " + E.Message);
        }
    }


    /// <summary>
    /// Displays the complete leftmost derivation traversal and token only leftmost derivation
    /// </summary>
    /// <param name="ParseTreeRoot"></param>
    public void Display(ParseTreeNode ParseTreeRoot)
    {
        Console.Write("Complete Leftmost Derivation Traversal:\n");
        PreOrderTraversal(ParseTreeRoot); //Display full parse tree
        Console.Write("\n\nToken Only Leftmost Derivation Traversal:\n");
        PreOrderTraversalOfTokens(ParseTreeRoot); //Display parse tree of Tokens
    }


    /// <summary>
    /// Checks if the current token matches the expected type at the specified index offset
    /// </summary>
    /// <param name="ExpectedType"></param>
    /// <param name="IndexOffset"></param>
    /// <returns></returns>
    private bool MatchTokenType(string ExpectedType, int IndexOffset)
    {
        if (CurrentTokenIndex < Tokens.Count)
        {
            switch (ExpectedType)
            {
                case "keyword":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "identifier":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "operator":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "special_symbol":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "type":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "literal":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "end_of_statement":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                case "constant":
                    if (Tokens[CurrentTokenIndex + IndexOffset].GetTypeName() == ExpectedType) return true;
                    break;
                default:
                    return false;
            }
        }
        return false;
    }


    /// <summary>
    /// Checks if the current token matches the expected type
    /// </summary>
    /// <param name="ExpectedType"></param>
    /// <returns></returns>
    private bool MatchTokenType(string ExpectedType)
    {
        return MatchTokenType(ExpectedType, 0);
    }


    /// <summary>
    /// Consumes and returns the current token as ParseTreeNode object
    /// </summary>
    /// <returns></returns>
    private ParseTreeNode GetNextToken()
    {
        Console.WriteLine("Next token is: " + CurrentTokenIndex + " Next lexeme is " + Tokens[CurrentTokenIndex].GetValue() + "\n");
        ParseTreeNode CurrentNode = new ParseTreeNode(Tokens[CurrentTokenIndex]);
        CurrentTokenIndex++;
        return CurrentNode;
    }
}