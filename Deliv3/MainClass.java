/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 3rd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: MainClass.java
 * November 19, 2023
 */

import java.util.Scanner;

// MainClass Class
public class MainClass
{
    static ScannerSCL Scanner = new ScannerSCL();
    static Parser Parser = new Parser();
    static Interpreter Interpreter = new Interpreter();
    static Scanner sc = new Scanner(System.in);

    // Run Method 
    public static void Run(String filePath) throws Exception {
    	if (Character.compare(filePath.charAt(0), '\"') == 0) filePath = filePath.substring(1, filePath.length()-1);
    	Interpreter.Begin(Parser.Begin(Scanner.Scan(filePath)));
    }
    
    // Main Method
    public static void main(String[] args) throws Exception {
        // If a command line argument is specified, begin scan
        if (args.length != 0) Run(args[0]);
        // If not, prompt user to get a file path
        else
        {
        	while (true) {
        		try {
        			System.out.print("Filepath: ");
        			String filePath = sc.nextLine();
        			if (filePath.isBlank()) throw new Exception("No file path specified");
            		else 
            		{
            			Run(filePath);
            			break;
            		}
        		}
        		catch (Exception e) 
        		{ 
        			System.out.println("\nFile path specified was invalid. Please try again."); 
        			e.printStackTrace();
        		}
        	}
        }
    }
}