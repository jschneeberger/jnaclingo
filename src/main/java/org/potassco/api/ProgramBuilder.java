package org.potassco.api;

public class ProgramBuilder {
/*
 class ProgramBuilder

    Object to build non-ground programs.
    See Also

    Control.builder(), parse_program()
    Notes

    A ProgramBuilder is a context manager and must be used with Python's with statement.
    Examples

    The following example parses a program from a string and passes the resulting AST to the builder:

    >>> import clingo
    >>> ctl = clingo.Control()
    >>> prg = "a."
    >>> with ctl.builder() as bld:
    ...    clingo.parse_program(prg, lambda stm: bld.add(stm))
    ...
    >>> ctl.ground([("base", [])])
    >>> ctl.solve(on_model=lambda m: print("Answer: {}".format(m)))
    Answer: a
    SAT

    Methods

    def add(self, statement:AST) -> None

        Adds a statement in form of an AST node to the program.
        Parameters

        statement : AST
            The statement to add.

        Returns

        None


 */
}
