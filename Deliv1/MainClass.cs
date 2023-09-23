using System;
using System.Collections.Generic;

public class MainClass
{
    //TestWithFilepath Method (used for testing without Command Line compilation)
    public static void TestWithFilePath()
    {
        Console.Write("Filepath: ");
        string Filepath = Console.ReadLine();
        if (Filepath == null)
        {
            Console.WriteLine("Error: No filepath specified");
            return;
        }
        SCLScanner Scanner = new SCLScanner();
        Scanner.Scan(Filepath);
    }
    //Main Method
    public static void Main(string[] args)
    {
        if (args.Length != 0)
        {
            string Path = args[0];
            SCLScanner Scanner = new SCLScanner();
            Scanner.Scan(Path);
        }
        else { Console.WriteLine("Error: No filepath found."); }
    }
}
