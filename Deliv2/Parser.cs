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
        ParseTreeNode startNode = new ParseTreeNode("start");
        ParseTreeNode functionNode = ParseFunction();
        startNode.AddChild(functionNode);
        return startNode;
    }


    /// <summary>
    /// Parses the function non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseFunction()
    {
        ParseTreeNode FunctionNode = new ParseTreeNode("function");

        //Parse RHS type IDENTIFIER parameter ENDLINE expression
        if (MatchTokenType("type"))
        {
            FunctionNode.AddChild(ParseType()); // Parse type
            ParseTreeNode Ident = new ParseTreeNode("identifier");
            Ident.AddChild(ConsumeToken()); // Parse identifier
            FunctionNode.AddChild(Ident);
            FunctionNode.AddChild(ParseParameter()); // Parse parameter
            FunctionNode.AddChild(new ParseTreeNode(new Token("end_of_statement", "EOS", 1000))); // Parse ENDLINE
            FunctionNode.AddChild(ParseExpression()); // Parse expression
        }
        // Parse RHS ENDLINE
        else if (MatchTokenType("end_of_statement"))
        {
            FunctionNode.AddChild(new ParseTreeNode(new Token("end_of_statement", "EOS", 1000)));
        }
        //Error
        else
        {
            Console.WriteLine("Error: No valid Right-Hand-Side detected.");
        }

        return FunctionNode;
    }


    /// <summary>
    /// Parses the parameter non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseParameter()
    {
        ParseTreeNode parameterNode = new ParseTreeNode("parameter");

        if (MatchTokenType("type") && MatchTokenType("identifier", 1) && MatchTokenType("type", 2) && MatchTokenType("identifier", 3))
        {
            parameterNode.AddChild(ParseParameter());
            parameterNode.AddChild(ConsumeToken());
        }
        // Parse 'type' non-terminal
        else if (MatchTokenType("type"))
        {
            parameterNode.AddChild(ParseType()); // Parse the type
            parameterNode.AddChild(ConsumeToken()); //Parse the identifier
        }
        else if (MatchTokenType("end_of_statement"))
        {
            ConsumeToken();
        }

        return parameterNode;
    }


    /// <summary>
    /// Parses the type non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseType()
    {
        ParseTreeNode TypeNode = new ParseTreeNode("type");
        // Expect a 'type' token
        if (MatchTokenType("type"))
        {
            TypeNode.AddChild(ConsumeToken());
        }
        else
        {
            Console.WriteLine("Error: Expected a type.\nPrevious Token: " + Tokens[CurrentTokenIndex - 1] + "\n Current Token: " + Tokens[CurrentTokenIndex] + "\n  Next Token: " + Tokens[CurrentTokenIndex + 1]);
            return new ParseTreeNode("Invalid_Parse_in_Type_Method");
        }
        return TypeNode;
    }


    /// <summary>
    /// Parses the expression non-terminal
    /// </summary>
    /// <returns></returns>
    public ParseTreeNode ParseExpression()
    {
        ParseTreeNode ExpressionNode = new ParseTreeNode("expression");

        // Parse RHS type IDENTIFIER ENDLINE expression
        if (MatchTokenType("type") && MatchTokenType("identifier", 1))
        {
            //Console.WriteLine("Expression -> type called");
            ExpressionNode.AddChild(ParseType()); // Parse the type
            ParseTreeNode Ident = new ParseTreeNode("identifier");
            Ident.AddChild(ConsumeToken()); // Parse the identifier
            ExpressionNode.AddChild(Ident);
            ExpressionNode.AddChild(ConsumeToken()); // Parse the ENDLINE
            ExpressionNode.AddChild(ParseExpression()); //Parse the expression
        }
        // Parse RHS IDENTIFIER = expression
        else if (MatchTokenType("identifier") && MatchTokenType("operator", 1))
        {
            ParseTreeNode Ident = new ParseTreeNode("identifier");
            Ident.AddChild(ConsumeToken()); // Parse the identifier
            ExpressionNode.AddChild(Ident);
            ParseTreeNode Oper = new ParseTreeNode("operator");
            Oper.AddChild(ConsumeToken()); // Parse the operator
            ExpressionNode.AddChild(Oper);
            ExpressionNode.AddChild(ParseExpression()); // Parse the expression
        }
        // Parse RHS IDENTIFIER expression
        else if (MatchTokenType("identifier"))
        {
            ParseTreeNode Ident = new ParseTreeNode("identifier");
            Ident.AddChild(ConsumeToken()); // Parse the identifier
            ExpressionNode.AddChild(Ident);
            ExpressionNode.AddChild(ParseExpression()); // Parse the expression
        }
        // Parse RHS CONSTANT expression
        else if (MatchTokenType("constant"))
        {
            ParseTreeNode Const = new ParseTreeNode("constant");
            Const.AddChild(ConsumeToken()); // Parse the constant
            ExpressionNode.AddChild(Const);
            ExpressionNode.AddChild(ParseExpression()); // Parse the expression
        }
        // Parse RHSs...
        else if (MatchTokenType("keyword"))
        {
            // PRINT returnable expression
            if (Tokens[CurrentTokenIndex].GetValue() == "print")
            {
                ExpressionNode.AddChild(ConsumeToken());
                ExpressionNode.AddChild(ParseReturnable());
                ExpressionNode.AddChild(ParseExpression());
            }
            // RETURN returnable expression
            else if (Tokens[CurrentTokenIndex].GetValue() == "return")
            {
                ExpressionNode.AddChild(ConsumeToken());
                ExpressionNode.AddChild(ParseReturnable());
                ExpressionNode.AddChild(new ParseTreeNode(new Token("end_of_statement", "EOS", 1000)));
                ExpressionNode.AddChild(ParseExpression());
            }
        }
        // Parse RHS ENDLINE expression
        else if (MatchTokenType("end_of_statement"))
        {
            ExpressionNode.AddChild(ConsumeToken());
            ExpressionNode.AddChild(ParseExpression());
        }
        // Parse RHS (left blank)
        else
        {
            //If expression becomes nothing
        }
        return ExpressionNode;
    }


    /// <summary>
    /// Parses the returnable non-terminal
    /// </summary>
    /// <returns></returns>
    private ParseTreeNode ParseReturnable()
    {
        ParseTreeNode ReturnableNode = new ParseTreeNode("returnable");
        // Parse RHS identifier
        if (MatchTokenType("identifier"))
        {
            ParseTreeNode Ident = new ParseTreeNode("identifier");
            Ident.AddChild(ConsumeToken()); // Parse the identifier
            ReturnableNode.AddChild(Ident);
        }
        // Parse RHS constant
        else if (MatchTokenType("constant"))
        {
            ParseTreeNode Const = new ParseTreeNode("constant");
            Const.AddChild(ConsumeToken()); // Parse the constant
            ReturnableNode.AddChild(Const);
        }
        // Parse RHS (left blank)
        else if (MatchTokenType("end_of_statement"))
        {
            ConsumeToken();
            //if returnable becomes nothing
        }
        // No valid RHS detected
        else
        {
            Console.WriteLine("Error: expected a returnable token.");
        }
        return ReturnableNode;
    }
    

    /// <summary>
    /// Begins parser, displays preorder traversal
    /// </summary>
    public void Begin()
    {
        ParseTreeNode ParseTree = Start(); // Generate parse tree
        Display(ParseTree); // Display the parse tree
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
    private ParseTreeNode ConsumeToken()
    {
        ParseTreeNode CurrentNode = new ParseTreeNode(Tokens[CurrentTokenIndex]);
        CurrentTokenIndex++;
        return CurrentNode;
    }
}