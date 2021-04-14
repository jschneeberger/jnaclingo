package org.potassco.api;

import java.util.List;

public class Clingo {
	public static SymbolType infimum;
	public static SymbolType supremum;
	public static final String VERSION = "5.5.0";
	
/*

Functions

def Function(name:str, arguments:List[Symbol]=[], positive:bool=True) -> Symbol

    Construct a function symbol.

    This includes constants and tuples. Constants have an empty argument list and tuples have an empty name. Functions can represent classically negated atoms. Argument positive has to be set to false to represent such atoms.
    Parameters

    name : str
        The name of the function (empty for tuples).
    arguments : List[Symbol] = []
        The arguments in form of a list of symbols.
    positive : bool = True
        The sign of the function (tuples must not have signs).

    Returns

    Symbol
*/
	public static Symbol function(String name, List<Symbol> arguments, Boolean positive) {
		if (positive == null) {
			positive = true;
		}
		return null;
	}
	/*
def Number(number:int) -> Symbol

    Construct a numeric symbol given a number.
    Parameters

    number : int
        The given number.

    Returns

    Symbol
*/
	public static Symbol number(Integer number) {
		return null;
	}
	/*
def String(string:str) -> Symbol

    Construct a string symbol given a string.
    Parameters

    string : str
        The given string.

    Returns

    Symbol
*/
	public static Symbol string(String str) {
		return null;
	}
	/*
def Tuple(arguments:List[Symbol]) -> Symbol

    A shortcut for Function("", arguments).
    Parameters

    arguments : List[Symbol]
        The arguments in form of a list of symbols.

    Returns

    Symbol

    See Also

    Function()
    */
	public static Symbol tuple(List<Symbol> arguments) {
		return null;
	}
	/*
def clingo_main(application:Application, files:List[str]=[]) -> int

    Runs the given application using clingo's default output and signal handling.

    The application can overwrite clingo's default behaviour by registering additional options and overriding its default main function.
    Parameters

    application : Application
        The Application object (see notes).
    files : List[str]
        The files to pass to the main function of the application.

    Returns

    int
        The exit code of the application.

    Notes

    The application object must implement a main function and additionally can override the other functions.
    */
	public static Integer clingoMain(Application application, List<String> files) {
		return null;
	}

	/*
def parse_program(program:str, callback:Callable[[AST],None]) -> None

    Parse the given program and return an abstract syntax tree for each statement via a callback.
    Parameters

    program : str
        String representation of the program.
    callback : Callable[[AST], None]
        Callback taking an ast as argument.

    Returns

    None

    See Also

    ProgramBuilder
    */
	public static void parseProgram(String program, AstEventHandler handler) {
		
	}

	/*
	def parse_term(string:str, logger:Callback[[MessageCode,str],None]=None, message_limit:int=20) -> Symbol

    Parse the given string using gringo's term parser for ground terms.

    The function also evaluates arithmetic functions.
    Parameters

    string : str
        The string to be parsed.
    logger : Callback[[MessageCode,str],None] = None
        Function to intercept messages normally printed to standard error.
    message_limit : int = 20
        Maximum number of messages passed to the logger.

    Returns

    Symbol

    Examples

    >>> import clingo
    >>> clingo.parse_term('p(1+2)')
    p(3)


 */
	public static Symbol parseTerm(String str, Integer messageLimit) {
		return null;
		
	}
}
