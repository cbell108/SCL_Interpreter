/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 3rd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: Parser.java
 * November 19, 2023
*/
import java.util.List;

// Parser class
class Parser
{
    /** <summary>
    * Indexes: 0: keyword, 1 is identifier, 2: operator, 3: special_symbol, 4: type, 5: literal, 6: end_of_statement, 7: constant
    * </summary> */
    private List<Token> tokens;


    /** <summary>
    * Holds the current token index of the current token being evaluated in the list
    * </summary>*/
    private int currentTokenIndex = 0;


    /** <summary>
    * Default constructor
    * </summary> */
    public Parser() { }


    /** <summary>
    * Diplays the preorder traversal of the current parse tree
    * </summary>
    * <param name="Node"></param> */
    public void preOrderTraversal(ParseTreeNode node)
    {
        if (node == null) return;

        System.out.print(node.RawValue + " ");

        for (int i = 0; i < node.Children.size(); i++)
        {
            preOrderTraversal(node.Children.get(i));
        }
    }


    /** <summary>
    * Displays only the Tokens of the preorder traversal of the current parse tree
    * </summary>
    * <param name="Node"></param> */
    public void preOrderTraversalOfTokens(ParseTreeNode node)
    {
        if (node == null) return;

        if (node.HasToken())
        {
            if (node.ParsedToken.getValue() == "EOS")
            {
                System.out.println();
            }
            else System.out.print(node.RawValue + " ");
        }

        for (int i = 0; i < node.Children.size(); i++)
        {
            preOrderTraversalOfTokens(node.Children.get(i));
        }
    }


    /** <summary>
    * Parses the start non-terminal
    * </summary>
    * <returns></returns> */
    public ParseTreeNode Start() throws Exception {
        System.out.println("Entering <start>");

        ParseTreeNode startNode = new ParseTreeNode("start");
        startNode.AddChild(ParseFunction());

        System.out.println("Exiting <start>");

        return startNode;
    }


    /** <summary>
    * Parses the function non-terminal
    * </summary>
    * <returns></returns> */
    public ParseTreeNode ParseFunction() throws Exception {
        System.out.println("Entering <function>");

        ParseTreeNode functionNode = new ParseTreeNode("function");

        //Parse RHS type identifier parameter ENDLINE expression
        if (MatchTokenType("type"))
        {
            functionNode.AddChild(ParseType()); // Parse type
            functionNode.AddChild(ParseIdentifier()); // Parse identifier
            functionNode.AddChild(ParseParameter()); // Parse parameter
            functionNode.AddChild(ParseEndline()); // Parse ENDLINE
            functionNode.AddChild(ParseExpression()); // Parse expression
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

        System.out.println("Exiting <function>");
        return functionNode;
    }


    /** <summary>
    * Parses the parameter non-terminal
    * </summary>
    * <returns></returns> */
    public ParseTreeNode ParseParameter() throws Exception {
    	System.out.println("Entering <parameter>");

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

        System.out.println("Exiting <parameter>");

        return parameterNode;
    }


    /** <summary>
    * Parses the type non-terminal
    * </summary>
    * <returns></returns> */
    public ParseTreeNode ParseType() throws Exception
    {
    	System.out.println("Entering <type>");

        ParseTreeNode typeNode = new ParseTreeNode("type");

        if (!MatchTokenType("type"))
        {
            throw new Exception("invalid type at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }
        typeNode.AddChild(GetNextToken());

        System.out.println("Exiting <type>");

        return typeNode;
    }


    /** <summary>
    * Parses the expression non-terminal
    * </summary>
    * <returns></returns> */
    public ParseTreeNode ParseExpression() throws Exception {
    	System.out.println("Entering <expression>");

        ParseTreeNode expressionNode = new ParseTreeNode("expression");

        // Parse RHS type identifier ENDLINE expression
        if (MatchTokenType("type") && MatchTokenType("identifier", 1))
        {
            expressionNode.AddChild(ParseType()); // Parse the type
            expressionNode.AddChild(ParseIdentifier()); // Parse the identifier
            expressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
            expressionNode.AddChild(ParseExpression()); //Parse the expression
        }
        // Parse RHS returnable operator expression
        else if (MatchTokenType("identifier") || MatchTokenType("constant") || MatchTokenType("literal"))
        {
        	if (MatchTokenType("operator", 1))
        	{
        		expressionNode.AddChild(ParseReturnable()); // Parse the returnable
        		expressionNode.AddChild(ParseOperator()); // Parse the operator
        		expressionNode.AddChild(ParseExpression()); // Parse the expression
        	}
        	// Parse RHS returnable ENDLINE expression
        	else if (MatchTokenType("end_of_statement", 1)) {
        		expressionNode.AddChild(ParseReturnable()); // Parse the returnable
        		expressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
        		expressionNode.AddChild(ParseExpression()); // Parse the expression
        	}
        	else {
        		System.out.println(tokens.get(currentTokenIndex + 1));
                if (tokens.get(currentTokenIndex) != null)
                {
                    throw new Exception("expr ret invalid syntax at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
                }
                //If expression becomes nothing
        	}
        }
        // Parse RHSs...
        else if (MatchTokenType("keyword"))
        {
            // PRINT returnable expression
            if (tokens.get(currentTokenIndex).getValue().equals("print"))
            {
                expressionNode.AddChild(GetNextToken()); // Parse the print
                expressionNode.AddChild(ParseReturnable()); // Parse the returnable
                expressionNode.AddChild(ParseExpression()); // Parse the expression
            }
            // RETURN returnable ENDLINE expression
            else if (tokens.get(currentTokenIndex).getValue().equals("return"))
            {
                expressionNode.AddChild(GetNextToken()); // Parse the return
                expressionNode.AddChild(ParseReturnable()); // Parse the returnable
                expressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
                expressionNode.AddChild(ParseFunction()); // Parse the function
            }
        }
        // Parse RHS ENDLINE expression
        else if (MatchTokenType("end_of_statement"))
        {
            expressionNode.AddChild(ParseEndline()); // Parse the ENDLINE
            expressionNode.AddChild(ParseExpression()); // Parse the expression
        }
        // Parse RHS (left blank)
        else
        {
            if (tokens.get(currentTokenIndex) != null)
            {
                throw new Exception("expr invalid syntax at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
            }
            //If expression becomes nothing
        }

        System.out.println("Exiting <expression>");

        return expressionNode;
    }


    /** <summary>
    * Parse the identifier non-terminal
    * </summary>
    * <returns></returns> */
    private ParseTreeNode ParseIdentifier() throws Exception {
    	System.out.println("Entering <identifier>");

        ParseTreeNode identNode = new ParseTreeNode("identifier");

        if (!MatchTokenType("identifier"))
        {
            throw new Exception("invalid identifier at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }
        identNode.AddChild(GetNextToken()); // Parse the identifier

        System.out.println("Exiting <identifier>");
        return identNode;
    }


    /** <summary>
    * Parse the constant non-terminal
    * </summary>
    * <returns></returns> */
    private ParseTreeNode ParseConstant() throws Exception {
    	System.out.println("Entering <constant>");

        ParseTreeNode ConstNode = new ParseTreeNode("constant");

        if (!MatchTokenType("constant"))
        {
            throw new Exception("invalid constant at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }
        ConstNode.AddChild(GetNextToken()); // Parse the constant

        System.out.println("Exiting <constant>");

        return ConstNode;
    }


    /** <summary>
    * Parse the constant non-terminal
    * </summary>
    * <returns></returns> */
    private ParseTreeNode ParseOperator() throws Exception {
    	System.out.println("Entering <operator>");

        ParseTreeNode operNode = new ParseTreeNode("operator");

        if (!MatchTokenType("operator"))
        {
            throw new Exception("invalid operator at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }
        operNode.AddChild(GetNextToken()); // Parse the operator

        System.out.println("Exiting <operator>");

        return operNode;
    }


    /** <summary>
     *  Parse the endline terminal
     *  </summary>
     *  <returns></returns> */
    private ParseTreeNode ParseEndline() throws Exception {
    	System.out.println("Entering <endline>");

        if (!MatchTokenType("end_of_statement"))
        {
            throw new Exception("invalid syntax at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }

        System.out.println("Exiting <endline>");

        return GetNextToken(); // Parse the ENDLINE
    }

    
    /** <summary>
     *  Parse the literal terminal
     *  </summary>
     *  <returns></returns> */
    private ParseTreeNode ParseLiteral() throws Exception {
    	System.out.println("Entering <literal>");

    	ParseTreeNode litNode = new ParseTreeNode("literal");
    	if (!MatchTokenType("literal"))
        {
            throw new Exception("invalid literal at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }
        litNode.AddChild(GetNextToken()); // Parse the literal
        
        System.out.println("Exiting <literal>");
        return litNode; // Parse the ENDLINE
    }

    /** <summary>
    * Parses the returnable non-terminal
    * </summary>
    * <returns></returns> */
    private ParseTreeNode ParseReturnable() throws Exception {
    	System.out.println("Entering <returnable>");

        ParseTreeNode returnableNode = new ParseTreeNode("returnable");
        /*// Parse RHS (left blank) version 1
        if (MatchTokenType("identifier") && MatchTokenType("operator", 1))
        {
            //if returnable becomes nothing, following is expression
        }
        // Parse RHS identifier*/
        if (MatchTokenType("identifier"))
        {
            returnableNode.AddChild(ParseIdentifier());
        }
        // Parse RHS constant
        else if (MatchTokenType("constant"))
        {
            returnableNode.AddChild(ParseConstant());
        }
        // Parse RHS literal
        else if (MatchTokenType("literal"))
        {
        	returnableNode.AddChild(ParseLiteral());
        }
        // Parse RHS (left blank) version 2
        else if (MatchTokenType("end_of_statement"))
        {
            //if returnable becomes nothing
        }
        // No valid RHS detected
        else
        {
            throw new Exception("expected a returnable token at line " + tokens.get(currentTokenIndex).getLineNum() + ".");
        }

        System.out.println("Exiting <returnable>");

        return returnableNode;
    }


    /** <summary>
    * Begins parser, displays preorder traversal
    * </summary> */
    public ParseTreeNode Begin(List<Token> tokenList) throws Exception
    {
        tokens = tokenList;
        currentTokenIndex = 0;
        System.out.println("Beginning parse of specified file");
        ParseTreeNode ParseTree = Start(); // Generate parse tree
        System.out.println();
        //Display(ParseTree); // Display the parse tree
        return ParseTree;
    }


    /** <summary>
    * Displays the complete leftmost derivation traversal and token only leftmost derivation
    * </summary>
    * <param name="ParseTreeRoot"></param> */
    public void Display(ParseTreeNode ParseTreeRoot)
    {
    	System.out.print("Complete Leftmost Derivation Traversal:\n");
        preOrderTraversal(ParseTreeRoot); //Display full parse tree
        System.out.print("\n\nToken Only Leftmost Derivation Traversal:\n");
        preOrderTraversalOfTokens(ParseTreeRoot); //Display parse tree of Tokens
    }


    /** <summary>
    * Checks if the current token matches the expected type at the specified index offset
    * </summary>
    * <param name="ExpectedType"></param>
    * <param name="IndexOffset"></param>
    * <returns></returns> */
    private boolean MatchTokenType(String expectedType, int indexOffset)
    {
        if (currentTokenIndex < tokens.size())
        {
            switch (expectedType)
            {
                case "keyword":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "identifier":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "operator":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "special_symbol":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "type":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "literal":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "end_of_statement":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                case "constant":
                    if (tokens.get(currentTokenIndex + indexOffset).getTypeName().equals(expectedType)) return true;
                    break;
                default:
                    return false;
            }
        }
        return false;
    }


    /** <summary>
    * Checks if the current token matches the expected type
    * </summary>
    * <param name="ExpectedType"></param>
    * <returns></returns> */
    private boolean MatchTokenType(String expectedType)
    {
        return MatchTokenType(expectedType, 0);
    }


    /** <summary>
    * Consumes and returns the current token as ParseTreeNode object
    * </summary>
    * <returns></returns> */
    private ParseTreeNode GetNextToken()
    {
    	System.out.println("Next token is: " + currentTokenIndex + " Next lexeme is " + tokens.get(currentTokenIndex).getValue() + "\n");
        ParseTreeNode CurrentNode = new ParseTreeNode(tokens.get(currentTokenIndex));
        currentTokenIndex++;
        return CurrentNode;
    }
}