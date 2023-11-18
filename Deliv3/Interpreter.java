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
			ArrayList<ArrayList<ParseTreeNode>> statements = getAllStatements(); // get all statements
			interpretStatements(statements); // begin interpretation
		}
		catch (Exception e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * returns a list of SCL statements
	 * @return
	 * @throws Exception
	 */
	private ArrayList<ArrayList<ParseTreeNode>> getAllStatements() throws Exception{
		//System.out.println("Getting all statements");
		ArrayList<ArrayList<ParseTreeNode>> statements = new ArrayList<ArrayList<ParseTreeNode>>();
		for (ArrayList<ParseTreeNode> statement = getNextStatement(); statement != null; statement = getNextStatement()) 
		{
			statements.add(statement);
		}
		return statements;
	}
	
	
	/**
	 * prints the value specified to the console
	 * @param type
	 * @param value
	 */
	private void printOnScreen(String type, String value) {
		if (type.equals("char") || type.equals("literal")) {
			String output = "";
			for (int i = 1; i < value.length() - 1; i++) 
			{
				if (Character.compare(value.charAt(i), '\\') == 0) 
				{
					System.out.print(output);
					output = "";
					switch (value.charAt(i+1)) 
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
				output += "" + value.charAt(i);
			}
			System.out.print(output);
		}
	}
	
	
	/**
	 * interprets the individual statements of the scl file
	 * @param statements
	 * @throws Exception
	 */
	private void interpretStatements(ArrayList<ArrayList<ParseTreeNode>> statements) throws Exception
	{
		//System.out.println("Beginning Interpretation on next line:");
		for (ArrayList<ParseTreeNode> statement : statements) 
		{
			switch (statement.get(0).ParsedToken.getTypeName()) {
			
			// declaring variable of type
			case "type":
				ParseTreeNode type = statement.get(0); // get the type
				ParseTreeNode identifier = statement.get(1); // get the variable name
				declareVariable(type, identifier); // declare the variable
				break;
				
			// assigning value of a variable
			case "identifier":
				ParseTreeNode leftVar = statement.get(0); // leftVar is always a variable
				if (statement.size() == 3) { // if there is only (variable = [one other token])
					if (sameType(leftVar, statement.get(2))) { // if variables are the same type
						if (typeMap.get(leftVar.RawValue).equals("int")) { // if variables are integers
							varMap.put(leftVar.RawValue, Integer.parseInt(statement.get(2).RawValue)); // save the value
						}
						else if (typeMap.get(leftVar.RawValue).equals("float")) {
							varMap.put(leftVar.RawValue, Float.parseFloat(statement.get(2).RawValue));
						}
						else varMap.put(leftVar.RawValue, statement.get(2).RawValue);
					}
				}
				else {
					if (typeMap.get(statement.get(0).RawValue).equals("int")) {
						interpretMathInt(statement);
					}
					else if (typeMap.get(statement.get(0).RawValue).equals("float")) {
						interpretMathFloat(statement);
					}
					else throw new Exception("invalid syntax at line " + statement.get(0).ParsedToken.getLineNum() + ".");
				}
				break;
				
			// either printing or returning a value/variable
			case "keyword":
				
				// print expected
				if (statement.get(0).ParsedToken.getValue().equals("print")) { // if 'print' is the keyword
					String temp = "" + varMap.get(statement.get(1).RawValue); // get the value of the variable
					if (statement.get(1).ParsedToken.getTypeName().equals("literal"))
					{
						printOnScreen("literal", statement.get(1).ParsedToken.getValue());
					}
					else if ("char".equals(typeMap.get(statement.get(1).RawValue)))
					{
						printOnScreen("char", (String)varMap.get(statement.get(1).RawValue));
					}
					else if ("constant".equals(statement.get(1).ParsedToken.getTypeName())) System.out.print(statement.get(1).RawValue);
					else if (typeMap.get(statement.get(1).RawValue) == null) throw new Exception("the variable \'" + statement.get(1).RawValue + "\' is used at line " + statement.get(1).ParsedToken.getLineNum() + " but has not been declared.");
					else System.out.print(temp);
					break;
				}
				
				// return expected
				else if (statement.get(0).ParsedToken.getValue().equals("return")) {
					return;
				}
				throw new Exception("invalid syntax at line " + statement.get(0).ParsedToken.getLineNum() + ".");
			}
		}
	}
	
	
	/**
	 * returns true if the variables are of the same type
	 * @param var1
	 * @param var2
	 * @return
	 */
	private boolean sameType(ParseTreeNode var1, ParseTreeNode var2) throws Exception {
		//check if var2 is constant (matches float or int) or literal (matches char)
		if (!isDeclared(var1.RawValue)) throw new Exception("the variable \'" + var1.RawValue + "\' is not declared");
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
		// if we move over to a variable class, this needs to change to if the varMap is null or not
		
		if (typeMap.get(name) != null) return true;
		return false;
	}
	
	
	/**
	 * declares the variable in the varMap and typeMap
	 * @param type
	 * @param_name
	 */
	private void declareVariable(ParseTreeNode type, ParseTreeNode name) {
		typeMap.put(name.RawValue, type.RawValue); // set type of variable
		varMap.put(name.RawValue, null); // set value of variable
	}
	
	/**
	 * returns the next valid statement of tokens
	 * @return
	 * @throws Exception
	 */
	private ArrayList<ParseTreeNode> getNextStatement() throws Exception
	{
		ArrayList<ParseTreeNode> nextStatement = new ArrayList<ParseTreeNode>(); // create ArrayList to hold next statement
		
		// switch statement to begin using the parse tree
		switch (current.RawValue){
		
		// start expected
		case "start":
			//System.out.println("  start is child 0 of current.");
			current = current.Children.get(0); // interpret start
			return getNextStatement();
			
		// function expected
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
			
		// parameter expected
		case "parameter":
			for (int i = 0; i < current.Children.size(); i++) {
				nextStatement.add(current.Children.get(i));
			}
			return nextStatement;
		
		// expression expected
		case "expression":
			working = current; // set working pointer to the current pointer
			switch (current.Children.get(0).RawValue) { // find which RHS to put make into a statement
			
			// type identifier ENDLINE expression
			case "type": 
				nextStatement.add(working.Children.get(0).Children.get(0));
				nextStatement.add(working.Children.get(1).Children.get(0));
				current = working.Children.get(3); // interpret ENDLINE, set to correct ParseTreeNode
				return nextStatement; // continue search for next statement

			// returnable ...
			case "returnable":
				// returnable operator
				if (current.Children.get(1).RawValue.equals("operator")) {
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
					nextStatement.add(working.Children.get(0).Children.get(0).Children.get(0)); // get the constant or identifier
					current = working.Children.get(2); // continue from the expression past the ENDLINE
					return nextStatement;
				}
				throw new Exception("invalid syntax at line " + current.Children.get(1).ParsedToken.getLineNum() + ".");
			
			//ENDLINE expression
			case "EOS":
				current = current.Children.get(1);
				return getNextStatement();
				
			// print returnable expression
			case "print":
				working = current;
				nextStatement.add(working.Children.get(0));
				if (working.Children.get(1).Children.size() == 0) throw new Exception("invalid syntax at line " + working.Children.get(0).ParsedToken.getLineNum() + ".");
				nextStatement.add(working.Children.get(1).Children.get(0).Children.get(0));
				current = working.Children.get(2);
				return nextStatement;

			// return returnable ENDLINE function
			case "return":
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
	
	
	/**
	 * returns the integer precedence of the operator specified
	 * @param operator
	 * @return
	 * @throws Exception
	 */
	private int getPrecedence(ParseTreeNode operator) throws Exception {
		if (operator.RawValue.equals("+") || operator.RawValue.equals("-")) return (int) 1;
		if (operator.RawValue.equals("*") || operator.RawValue.equals("/")) return (int) 2;
		throw new Exception("invalid operator at line " + operator.ParsedToken.getLineNum() + ".");
	}
	
	
	/**
	 * returns a list of indexes of operators
	 * @param statement
	 * @return
	 */
	private ArrayList<Integer> findOperators(ArrayList<ParseTreeNode> statement){
		ArrayList<Integer> indexes = new ArrayList<Integer>(); // contains indexes of all operators
		for (int i = 2; i < statement.size(); i++) {
			if (statement.get(i).RawValue.equals("+") || statement.get(i).RawValue.equals("-") || statement.get(i).RawValue.equals("*") || statement.get(i).RawValue.equals("/")) {
				indexes.add(i - 2);
			}
		}
		return indexes;
	}
	
	
	/**
	 * interprets the mathematical expression, returning the integer result
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	private void interpretMathInt(ArrayList<ParseTreeNode> statement) throws Exception{
		ArrayList<String> expression = new ArrayList<String>(); // contains expression
		ArrayList<Integer> opIndexes = findOperators(statement); // contains operator indexes
		ArrayList<Integer> opPrecedence = new ArrayList<Integer>(); // contains operator precedences
		
		// loop to get the expression and operator precedences
		for (int i = 2; i < statement.size(); i++) {
			if (isDeclared(statement.get(i).RawValue)) expression.add("" + varMap.get(statement.get(i).RawValue));
			else if (statement.get(i).ParsedToken.getTypeName().equals("operator")) {
				opPrecedence.add(getPrecedence(statement.get(i)));
				expression.add(statement.get(i).RawValue);
			}
			else expression.add(statement.get(i).RawValue);
		}
		
		// loop to calculate the result of the expression
		for (int count = 0; count < opIndexes.size(); count++) {
			int highest = 0; // stores the highest precedence
			int indexHigh = 0; // stores the index of the highest precedence operator
			int precIndex = 0; // stores the opPrecedence index of highest precedence operator
			
			// loop to find highest precedence
			for (int i = 0; i < opPrecedence.size(); i++) {
				if (opPrecedence.get(i) > highest) {
					highest = opPrecedence.get(i);
					indexHigh = opIndexes.get(i);
					precIndex = i;
				}
			}
			
			// calcculate the value
			int value = interpretOperationInt(Integer.parseInt(expression.get(indexHigh-1)), Integer.parseInt(expression.get(indexHigh+1)), expression.get(indexHigh));
			expression.remove(indexHigh+1); // remove the value used in the operation
			expression.remove(indexHigh); // remove the operator used in the operation
			expression.remove(indexHigh-1); // remove the value used in the operation
			expression.add(indexHigh-1, "" + value); // add the correct value back into the expression
			opPrecedence.remove(precIndex); // remove the operator's precedence used in the operation
		}
		varMap.put(statement.get(0).RawValue, expression.get(0));
	}
	
	
	/**
	 * interprets the singular mathematical operation, returning the integer result
	 * @param value1
	 * @param value2
	 * @param op
	 * @return
	 * @throws Exception
	 */
	private int interpretOperationInt(int value1, int value2, String op) throws Exception {
		switch (op) {
		case "+":
			return value1 + value2;
		case "-":
			return value1 - value2;
		case "*":
			return value1 * value2;
		case "/":
			return value1 / value2;
		}
		throw new Exception("invalid operator"); //+ op.ParsedToken.getLineNum() + ".");
	}
	

	/**
	 * interprets the input floating point math expression and saves the result in the variable
	 * @param statement
	 * @throws Exception
	 */
	private void interpretMathFloat(ArrayList<ParseTreeNode> statement) throws Exception{
		ArrayList<String> expression = new ArrayList<String>(); // contains expression
		ArrayList<Integer> opIndexes = findOperators(statement); // contains operator indexes
		ArrayList<Integer> opPrecedence = new ArrayList<Integer>(); // contains operator precedences
		
		// loop to get the expression and operator precedences
		for (int i = 2; i < statement.size(); i++) {
			if (isDeclared(statement.get(i).RawValue)) expression.add("" + varMap.get(statement.get(i).RawValue));
			else if (statement.get(i).ParsedToken.getTypeName().equals("operator")) {
				opPrecedence.add(getPrecedence(statement.get(i)));
				expression.add(statement.get(i).RawValue);
			}
			else expression.add(statement.get(i).RawValue);
		}
		
		// loop to calculate the result of the expression
		for (int count = 0; count < opIndexes.size(); count++) {
			int highest = 0; // stores the highest precedence
			int indexHigh = 0; // stores the index of the highest precedence operator
			int precIndex = 0; // stores the opPrecedence index of highest precedence operator
			
			// loop to find highest precedence
			for (int i = 0; i < opPrecedence.size(); i++) {
				if (opPrecedence.get(i) > highest) {
					highest = opPrecedence.get(i);
					indexHigh = opIndexes.get(i);
					precIndex = i;
				}
			}
			
			// calcculate the value
			float value = interpretOperationFloat(Float.parseFloat(expression.get(indexHigh-1)), Float.parseFloat(expression.get(indexHigh+1)), expression.get(indexHigh));
			expression.remove(indexHigh+1); // remove the value used in the operation
			expression.remove(indexHigh); // remove the operator used in the operation
			expression.remove(indexHigh-1); // remove the value used in the operation
			expression.add(indexHigh-1, "" + value); // add the correct value back into the expression
			opPrecedence.remove(precIndex); // remove the operator's precedence used in the operation
		}
		varMap.put(statement.get(0).RawValue, expression.get(0));
	}
	
	
	/**
	 * interprets the singular mathematical operation, returning the floating point result
	 * @param value1
	 * @param value2
	 * @param op
	 * @return
	 * @throws Exception
	 */
	private float interpretOperationFloat(float value1, float value2, String op) throws Exception {
		switch (op) {
		case "+":
			return value1 + value2;
		case "-":
			return value1 - value2;
		case "*":
			return value1 * value2;
		case "/":
			return value1 / value2;
		}
		throw new Exception("invalid operator");
	}
	
}