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
import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter{
	HashMap<String, Object> varMap = new HashMap<String, Object>(); 
	HashMap<String, String> typeMap = new HashMap<String, String>();
	ParseTreeNode root, current, working;
	
	
	/**
	 * begins the interpretation process
	 * @param root
	 * @throws Exception
	 */
	public void Begin(ParseTreeNode root) throws Exception
	{
		try {
			//Avoids starting from anywhere but the start of the list
			if (!root.RawValue.equals("start")) throw new Exception("root was not start nonterminal.");
			this.root = root; // get root node
			current = root; // set current to the current node, root
			working = null; // set working to null
			System.out.println("Getting all statements");
			ArrayList<ArrayList<ParseTreeNode>> statements = new ArrayList<ArrayList<ParseTreeNode>>();
			
			// Get statement, add to list of statements
			for (ArrayList<ParseTreeNode> statement = getNextStatement(); statement != null; statement = getNextStatement()) 
			{
				statements.add(statement);
			}
			
			// Print the statements;
			for (ArrayList<ParseTreeNode> list : statements)
			{
				for (ParseTreeNode node : list) 
				{
					System.out.print(node.RawValue + " ");
				}
				System.out.println();
			}
			
			//begin interpretation
			System.out.println("\nBeginning Interpretation\n");
			interpretStatements(statements);
		}
		catch (Exception e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * interprets the individual statements of the scl file
	 * @param statements
	 * @throws Exception
	 */
	private void interpretStatements(ArrayList<ArrayList<ParseTreeNode>> statements) throws Exception
	{
		for (ArrayList<ParseTreeNode> statement : statements) 
		{
			switch (statement.get(0).ParsedToken.getTypeName()) {
			
			// declaring variable of type
			case "type":
				ParseTreeNode type = statement.get(0);
				ParseTreeNode identifier = statement.get(1);
				declareVariable(identifier.RawValue, type.RawValue);
				break;
				
			// assigning value of a variable
			case "identifier":
				ParseTreeNode leftVar = statement.get(0); // leftVar is always a variable
				ParseTreeNode assignOp = statement.get(1); // assignOp is always =
				if (statement.size() == 3) // if there is only (variable = ...)
				{
					if (sameType(leftVar, statement.get(2))) // if variables are the same type
					{
						varMap.put(leftVar.RawValue, statement.get(2).RawValue); // save the value
					}
				}
				//interpret assignment statement
				//check if math, interpret math
				//if interpreting math, howManyOperands(statement), get indexes of operators, then
				// get precedence of operators, then complete operations one by one
				break;
				
			// either printing or returning a value/variable
			case "keyword":
				if (statement.get(0).ParsedToken.getValue().equals("print")) {
					String temp = (String) varMap.get(statement.get(1).RawValue);
					String output = "";
					if (statement.get(1).ParsedToken.getTypeName().equals("literal"))
					{
						for (int i = 1; i < statement.get(1).ParsedToken.getValue().length() - 1; i++) 
						{
							if (Character.compare(statement.get(1).ParsedToken.getValue().charAt(i), '\\') == 0) 
							{
								System.out.print(output);
								output = "";
								switch (statement.get(1).ParsedToken.getValue().charAt(i+1)) 
								{
								case 'n':
									System.out.print("\n");
									i = i + 1;
									continue;
								case 't':
									System.out.print("\t");
									i = i + 1;
									continue;
								case 'b':
									System.out.print("\b");
									i = i + 1;
									continue;
								case 'r':
									System.out.print("\r");
									i = i + 1;
									continue;
								case 'f':
									System.out.print("\f");
									i = i + 1;
									continue;
								}
							}
							output += "" + (Character)statement.get(1).ParsedToken.getValue().charAt(i);
						}
						System.out.print(output);
					}
					else if (typeMap.get(statement.get(1).RawValue).equals("char"))
					{
						for (int i = 1; i < temp.length() - 1; i++) {
							output += temp.charAt(i) + "";
						}
						System.out.print(output);
					}
					else System.out.print(temp);
					break;
				}
				else if (statement.get(0).ParsedToken.getValue().equals("return")) {
					return;
				}
				throw new Exception("invalid syntax at line " + statement.get(0).ParsedToken.getLineNum() + ".");
			}
		}
	}
	
	
	/**
	 * returns the number of operands in the statement
	 * @param statement
	 * @return
	 */
	private int howManyOperands(ArrayList<ParseTreeNode> statement)
	{
		int counter = 0;
		for (int i = 0; i < statement.size(); i++)
		{
			String typeName = statement.get(i).ParsedToken.getTypeName();
			if (typeName.equals("identifier") || typeName.equals("constant") || typeName.equals("literal"))
			{
				counter++;
			}
		}
		return counter;
	}
	
	
	/**
	 * returns true if the variables are of the same type
	 * @param var1
	 * @param var2
	 * @return
	 */
	private boolean sameType(ParseTreeNode var1, ParseTreeNode var2) {
		//check if var2 is constant (matches float or int) or literal (matches char)
		String var1Type = typeMap.get(var1.RawValue);
		String var2Type = var2.ParsedToken.getTypeName();
		if (var2Type.equals("constant") && (var1Type.equals("float") || var1Type.equals("int"))) return true;
		if (var2Type.equals("literal") && var1Type.equals("char")) return true;
		if (var1Type.equals(typeMap.get(var2.RawValue))) return true;
		return false;
	}
	
	
	/**
	 * returns true if the variable has been declared
	 * @param name
	 * @return
	 */
	private boolean isDeclared(String name) {
		if (typeMap.get(name) != null) return true;
		return false;
	}
	
	
	/**
	 * declares the variable in the varMap and typeMap
	 * @param name
	 * @param type
	 */
	private void declareVariable(String name, String type) {
		typeMap.put(name, type); // set type of variable
		varMap.put(name, null); // set value of variable
	}
	
	/**
	 * returns the next valid statement of tokens
	 * @return
	 * @throws Exception
	 */
	private ArrayList<ParseTreeNode> getNextStatement() throws Exception
	{
		ArrayList<ParseTreeNode> nextStatement = new ArrayList<ParseTreeNode>(); // create ArrayList to hold next statement
		ParseTreeNode parent, type, identifier, parameter, expression, constant, operator;
		//System.out.println(current.RawValue + " is current.");
		switch (current.RawValue){
		case "start":
			//System.out.println("  start is child 0 of current.");
			current = current.Children.get(0); // interpret start
			return getNextStatement();
		case "function":
			//System.out.println("  function is child 0 of current.");
			if (current.Children.size() <= 0) return null; // if at the end of the document
			working = current;
			nextStatement.add(working.Children.get(0).Children.get(0)); // get type
			nextStatement.add(working.Children.get(1).Children.get(0)); // get identifier
			current = working.Children.get(2); // set current to the parameter parent
			nextStatement.addAll(getNextStatement()); // get parameter(s)
			current = working.Children.get(4); // hold onto expression
			return nextStatement; // continue search for next statement
		case "parameter":
			for (int i = 0; i < current.Children.size(); i++) {
				nextStatement.add(current.Children.get(i));
			}
			return nextStatement;
		case "expression":
			working = current; // set working pointer to the current pointer
			switch (current.Children.get(0).RawValue) { // find which RHS to put make into a statement
			
			case "type": // type identifier ENDLINE expression
				//System.out.println("type  is child 0 of current.");
				type = working.Children.get(0).Children.get(0); // get type
				identifier = working.Children.get(1).Children.get(0); // get identifier
				//typeMap.put(identifier.RawValue, type.RawValue); // set type of variable
				//varMap.put(identifier.RawValue, null); // set value of variable
				nextStatement.add(type);
				nextStatement.add(identifier);
				current = working.Children.get(3); // interpret ENDLINE, set to correct ParseTreeNode
				return nextStatement; // continue search for next statement

			case "returnable": // returnable ...
				//System.out.println("  returnable is child 0 of current.");
				// returnable operator
				if (current.Children.get(1).RawValue.equals("operator")) {
					//System.out.println("    operator is child 1 of current.");
					working = current;
					// add values until endline to nextStatement
					nextStatement.add(working.Children.get(0).Children.get(0).Children.get(0)); // get the constant or identifier
					nextStatement.add(working.Children.get(1).Children.get(0)); // get the operator
					current = working.Children.get(2); // continue from the expression past the ENDLINE
		            nextStatement.addAll(getNextStatement());
					return nextStatement;
				}
				else if (current.Children.get(1).RawValue.equals("EOS")) {
					working = current;
					//System.out.println("    ENDLINE is chlld 1 of current.");
					nextStatement.add(working.Children.get(0).Children.get(0).Children.get(0)); // get the constant or identifier
					current = working.Children.get(2); // continue from the expression past the ENDLINE
					return nextStatement;
				}
				throw new Exception("invalid syntax at line " + current.Children.get(1).ParsedToken.getLineNum() + ".");
			
			//ENDLINE expression
			case "EOS":
				//System.out.println("  ENDLINE is child 0 of current.");
				//expect ENDLINE expression
				current = current.Children.get(1);
				return getNextStatement();
			// print returnable expression
			case "print":
				//System.out.println("  print is child 0 of current.");
				working = current;
				nextStatement.add(working.Children.get(0));
				nextStatement.add(working.Children.get(1).Children.get(0).Children.get(0));
				current = working.Children.get(2);
				
				//expect print returnable expression->ENDLINE expression
				return nextStatement;

			// return returnable ENDLINE function
			case "return":
				//System.out.println("  return is child 0 of current.");
				working = current;
				nextStatement.add(working.Children.get(0));
				if (working.Children.get(1).Children.size() > 0) nextStatement.add(working.Children.get(1).Children.get(0).Children.get(0));
				current = working.Children.get(3);
				return nextStatement;
				
			default:
				throw new Exception("unexpected ParseTreeNode." + current.RawValue);
				
			}
		}
		return nextStatement;
	}
	
	
	
	
	//SCL mixed operations?     float / int?
	//Solution: convert to required type, do operation, convert to and return a string

	private Object interpretMathExpression(ArrayList<ParseTreeNode> statement) {
		ArrayList<Integer> opIndexes = new ArrayList<Integer>();
		ArrayList<Integer> opPrecedence = new ArrayList<Integer>();
		
		for (ParseTreeNode node : statement){
		    //Check if operator or value or variable
			//int op_ = node.ParsedToken.getTypeName().equals("operator") ? getPrecedence() : -1 ;
			
		    //Function that returns precedence if true, 0 if false?
		}
		//List<> queue = new List<>();

		// Method 1:
		// Given 4 + 2 * 5
		// Find all operators and assign precedence
		// + 0 * 1
		// 2 * 5
		// Make pointers for each variable node 4, node 2, node 5
		// Make operation for queue (node 2, node *, node 5) node 2 is assigned value 10
		// Make operation for queue (node 4, node +, node 2) node 4 is assigned value 14
		// We havent updated operation 4 + 2 * 5
		// (4 + 2), we see that 2 is in another operation
		// 2 = result of other operation

		// Method 2:
		// Given x = 4 + 2 * 5 
		// Find all operators and assign precedence
		// + 0 * 1
		// 2 * 5 = 10
		// 4 + 2 * 5 find previous parameters 2 * 5
		// 4 + 10 remove old expression, add result
		// 4 + 10 = 14
		// x = 4 + 10 remove old expression, add result
		// x = 14

		
		// analyze
		//order by precedence
		//call interpret
		//add up
		int x = 2;
		return x;
	}
	
	private Object interpretOperation(int value1, int value2, String op){
		return new Object();
	}
}