/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 2nd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: MainClass.cs
 * October 22, 2023
*/

using System;
using System.Collections.Generic;

// MainClass Class
public class MainClass
{
    static Scanner Scanner = new Scanner();
    static Parser Parser = new Parser();
    // TestWithFilepath Method (used for testing in IDE without command line compilation)
    public static void TestWithFilePath()
    {
        // Prompt user, record file path, begin scan
        Console.Write("Filepath: ");
        string Filepath = Console.ReadLine();
        if (string.IsNullOrEmpty(Filepath) || string.IsNullOrWhiteSpace(Filepath))
        {
            Console.WriteLine("Error: No file path specified");
            return;
        }
        Parser.InitializeParser(Scanner.Scan(Filepath));
        Parser.Begin();
    }

    // Main Method
    public static void Main(string[] args)
    {
        // If a command line argument is specified, begin scan
        if (args.Length != 0)
        {
            string Path = args[0];
            Parser.InitializeParser(Scanner.Scan(Path));
            Parser.Begin();
        } 
        else Console.WriteLine("Error: No file path specified.");
        // Used in testing in IDE
        // TestWithFilePath();
    }
}
