package org.potassco.api;

public class Backend {
/*
 class Backend

    Backend object providing a low level interface to extend a logic program.

    This class allows for adding statements in ASPIF format.
    See Also

    Control.backend()
    Notes

    The Backend is a context manager and must be used with Python's with statement.

    Statements added with the backend are added directly to the solver. For example, the grounding component will not be aware if facts were added to a program via the backend. The only exception are atoms added with Backend.add_atom(), which will subsequently be used to instantiate rules. Furthermore, the Control.cleanup() method can be used to transfer information about facts back to the grounder.
    Examples

    The following example shows how to add a fact to a program and the effect of the Control.cleanup() function:

    >>> import clingo
    >>> ctl = clingo.Control()
    >>> sym_a = clingo.Function("a")
    >>> with ctl.backend() as backend:
    ...     atm_a = backend.add_atom(sym_a)
    ...     backend.add_rule([atm_a])
    ...
    >>> ctl.symbolic_atoms[sym_a].is_fact
    False
    >>> ctl.cleanup()
    >>> ctl.symbolic_atoms[sym_a].is_fact
    True
    >>> ctl.solve(on_model=lambda m: print("Answer: {}".format(m)))
    Answer: a
    SAT

    Methods

    def add_acyc_edge(self, node_u:int, node_v:int, condition:List[int]) -> None

        Add an edge directive to the program.
        Parameters

        node_u : int
            The start node represented as an unsigned integer.
        node_v : int
            The end node represented as an unsigned integer.
        condition : List[int]
            List of program literals.

        Returns

        None

    def add_assume(self, literals:List[int]) -> None

        Add assumptions to the program.
        Parameters

        literals : List[int]
            The list of literals to assume true.

        Returns

        None

    def add_atom(self, symbol:Optional[Symbol]=None) -> int

        Return a fresh program atom or the atom associated with the given symbol.

        If the given symbol does not exist in the atom base, it is added first. Such atoms will be used in subequents calls to ground for instantiation.
        Parameters

        symbol : Optional[Symbol]=None
            The symbol associated with the atom.

        Returns

        int
            The program atom representing the atom.

    def add_external(self, atom:int, value:TruthValue=TruthValue.False_) -> None

        Mark a program atom as external optionally fixing its truth value.
        Parameters

        atom : int
            The program atom to mark as external.
        value : TruthValue=TruthValue.False_
            Optional truth value.

        Returns

        None

        Notes

        Can also be used to release an external atom using TruthValue.Release.
    def add_heuristic(self, atom:int, type:HeuristicType, bias:int, priority:int, condition:List[int]) -> None

        Add a heuristic directive to the program.
        Parameters

        atom : int
            Program atom to heuristically modify.
        type : HeuristicType
            The type of modification.
        bias : int
            A signed integer.
        priority : int
            An unsigned integer.
        condition : List[int]
            List of program literals.

        Returns

        None

    def add_minimize(self, priority:int, literals:List[Tuple[int,int]]) -> None

        Add a minimize constraint to the program.
        Parameters

        priority : int
            Integer for the priority.
        literals : List[Tuple[int,int]]
            List of pairs of program literals and weights.

        Returns

        None

    def add_project(self, atoms:List[int]) -> None

        Add a project statement to the program.
        Parameters

        atoms : List[int]
            List of program atoms to project on.

        Returns

        None

    def add_rule(self, head:List[int], body:List[int]=[], choice:bool=False) -> None

        Add a disjuntive or choice rule to the program.
        Parameters

        head : List[int]
            The program atoms forming the rule head.
        body : List[int]=[]
            The program literals forming the rule body.
        choice : bool=False
            Whether to add a disjunctive or choice rule.

        Returns

        None

        Notes

        Integrity constraints and normal rules can be added by using an empty or singleton head list, respectively.
    def add_weight_rule(self, head:List[int], lower:int, body:List[Tuple[int,int]], choice:bool=False) -> None

        Add a disjuntive or choice rule with one weight constraint with a lower bound in the body to the program.
        Parameters

        head : List[int]
            The program atoms forming the rule head.
        lower : int
            The lower bound.
        body : List[Tuple[int,int]]
            The pairs of program literals and weights forming the elements of the weight constraint.
        choice : bool=False
            Whether to add a disjunctive or choice rule.

        Returns

        None


 */
}
