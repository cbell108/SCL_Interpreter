# SCL_Interpreter
CPL Group Projects

Google Docs Report_1 Link: https://docs.google.com/document/d/1-XZuHQikIStp0eLV1MfMUs5bGoc7beH59H42F7bObk0/edit?usp=sharing

Deliverable 1 Requirements:

The task at hand is to develop an interpreter for a subset of the SCL language. SCL is an 
experimental system programming language that can be accessed at 
http://ksuweb.kennesaw.edu/~jgarrido/sysplm/. To accomplish this, you must implement a 
scanner that reads the source code and creates a list of tokens. The scanner implementation 
must include an array of the keywords used in the subset of SCL, an array (or list) of the 
identifiers (variables), and other tokens (such as operators, keywords, constants, and/or 
special characters). Additionally, you will need to define the grammar of the subset of SCL 
that you are using in BNF/EBNF.
Python is recommended as the programming language of choice for this project. The 
program should take command-line input for a file and generate a JSON file with a list of 
tokens while also printing those tokens to the console. For instance, the following command 
will execute the program on the given source SCL file: python scl_scanner.py 
hello_world.scl
You must submit a brief report detailing the work done, including the grammar of the subset 
of SCL, source code files for the scanner program, as well as input and output files. The 
report should demonstrate the execution of the scanner program using appropriate input 
files, and the program should display a list of the scanned tokens.
