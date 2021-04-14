package org.potassco.api;

/**
 * Enumeration of the different types of symbols.

    SymbolType objects have a readable string representation, implement Python's rich comparison operators, and can be used as dictionary keys.

    Furthermore, they cannot be constructed from Python. Instead the following preconstructed objects are available:
    Attributes
    
    Number : SymbolType
        A numeric symbol, e.g., 1.
    String : SymbolType
        A string symbol, e.g., "a".
    Function : SymbolType
        A function symbol, e.g., c, (1, "a"), or f(1,"a").
    Infimum : SymbolType
        The #inf symbol.
    Supremum : SymbolType
        The #sup symbol
        
 * @author jschneeberger
 *
 */
public enum SymbolType {
	NUMBER, STRING, FUNCTION, INFIMUM, SUPREMUM
}
