package org.potassco.api;

public class Model {
/*
 class Model

    Provides access to a model during a solve call and provides a SolveContext object to provided limited support to influence the running search.
    Notes

    The string representation of a model object is similar to the output of models by clingo using the default output.

    Model objects cannot be constructed from Python. Instead they are obained during solving (see Control.solve()). Furthermore, the lifetime of a model object is limited to the scope of the callback it was passed to or until the search for the next model is started. They must not be stored for later use.
    Examples

    The following example shows how to store atoms in a model for usage after solving:

    >>> import clingo
    >>> ctl = clingo.Control()
    >>> ctl.add("base", [], "{a;b}.")
    >>> ctl.ground([("base", [])])
    >>> ctl.configuration.solve.models="0"
    >>> models = []
    >>> with ctl.solve(yield_=True) as handle:
    ...     for model in handle:
    ...         models.append(model.symbols(atoms=True))
    ...
    >>> sorted(models)
    [[], [a], [a, b], [b]]

    Instance variables

    var context : SolveControl

        Object that allows for controlling the running search.
    var cost : List[int]

        Return the list of integer cost values of the model.

        The return values correspond to clasp's cost output.
    var number : int

        The running number of the model.
    var optimality_proven : bool

        Whether the optimality of the model has been proven.
    var thread_id : int

        The id of the thread which found the model.
    var type : ModelType

        The type of the model.

    Methods

    def contains(self, atom:Symbol) -> bool

        Efficiently check if an atom is contained in the model.
        Parameters

        atom : Symbol
            The atom to lookup.

        Returns

        bool
            Whether the given atom is contained in the model.

        Notes

        The atom must be represented using a function symbol.
    def extend(self, symbols:List[Symbol]) -> None

        Extend a model with the given symbols.
        Parameters

        symbols : List[Symbol]
            The symbols to add to the model.

        Returns

        None

        Notes

        This only has an effect if there is an underlying clingo application, which will print the added symbols.
    def is_true(self, literal:int) -> bool

        Check if the given program literal is true.
        Parameters

        literal : int
            The given program literal.

        Returns

        bool
            Whether the given program literal is true.

    def symbols(self, atoms:bool=False, terms:bool=False, shown:bool=False, csp:bool=False, complement:bool=False) -> List[Symbol]

        Return the list of atoms, terms, or CSP assignments in the model.
        Parameters

        atoms : bool=False
            Select all atoms in the model (independent of #show statements).
        terms : bool=False
            Select all terms displayed with #show statements in the model.
        shown : bool
            Select all atoms and terms as outputted by clingo.
        csp : bool
            Select all csp assignments (independent of #show statements).
        complement : bool
            Return the complement of the answer set w.r.t. to the atoms known to the grounder. (Does not affect csp assignments.)

        Returns

        List[Symbol]
            The selected symbols.

        Notes

        Atoms are represented using functions (Symbol objects), and CSP assignments are represented using functions with name "$" where the first argument is the name of the CSP variable and the second its value.


 */
}
