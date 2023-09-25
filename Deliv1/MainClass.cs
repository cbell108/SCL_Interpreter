/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 1st Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: MainClass.cs
 * September 24, 2023
*/

using System;
using System.Collections.Generic;

// MainClass Class
public class MainClass
{
    static SCLScanner Scanner = new SCLScanner();
    // TestWithFilepath Method (used for testing in IDE without command line compilation)
    public static void TestWithFilePath()
    {
        // Prompt user, record file path, begin scan
        Console.Write("Filepath: ");
        string Filepath = Console.ReadLine();
        if (Filepath == null)
        {
            Console.WriteLine("Error: No file path specified");
            return;
        }
        Scanner.Scan(Filepath);
    }

    // Main Method
    public static void Main(string[] args)
    {
        // If a command line argument is specified, begin scan
        if (args.Length != 0)
        {
            string Path = args[0];
            Scanner.Scan(Path);
        } 
        else Console.WriteLine("Error: No file path specified.");
        //TestWithFilePath();
    }
}