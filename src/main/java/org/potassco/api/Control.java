package org.potassco.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.potassco.jclingo.NativeClingo;
import org.potassco.jclingo.types.PartT;
import org.potassco.jclingo.types.SizeT;
import org.potassco.jclingo.types.SizeTByReference;
import org.potassco.jclingo.types.SolveEventCallbackT;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * @author jschneeberger
 *
 * class Control(arguments:List[str]=[], logger:Callback[[MessageCode,str],None]=None, message_limit:int=20)

    Control object for the grounding/solving process.
    Parameters

    arguments : List[str]
        Arguments to the grounder and solver.
    logger : Callback[[MessageCode,str],None]=None
        Function to intercept messages normally printed to standard error.
    message_limit : int
        The maximum number of messages passed to the logger.

    Notes

    Note that only gringo options (without --text) and clasp's search options are supported. Furthermore, a Control object is blocked while a search call is active; you must not call any member function during search.
    


 */
public class Control {
	/**
	 * Configuration object to change the configuration.
	 */
	private Configuration configuration;
	/**
	 * Whether the internal program representation is conflicting.

        If this (read-only) property is true, solve calls return immediately with an unsatisfiable solve result.
        Notes

        Conflicts first have to be detected, e.g., initial unit propagation results in an empty clause, or later if an empty clause is resolved during solving. Hence, the property might be false even if the problem is unsatisfiable.

	 */
	private Boolean isConflicting;
	/**
	 * A Map containing solve statistics of the last solve call.
        Notes

        The statistics correspond to the --stats output of clingo. The detail of the statistics depends on what level is requested on the command line.

        This property is only available in clingo.
        Examples

        The following example shows how to dump the solving statistics in json format:

        >>> import json
        >>> import clingo
        >>> ctl = clingo.Control()
        >>> ctl.add("base", [], "{a}.")
        >>> ctl.ground([("base", [])])
        >>> ctl.solve()
        SAT
        >>> print(json.dumps(ctl.statistics['solving'], sort_keys=True, indent=4,
        ... separators=(',', ': ')))
        {
            "solvers": {
                "choices": 1.0,
                "conflicts": 0.0,
                "conflicts_analyzed": 0.0,
                "restarts": 0.0,
                "restarts_last": 0.0
            }
        }
	 */
	private Map<String, Object> statistics;
	private Iterable<SymbolicAtom> symbolicAtoms;
	private Iterable<TheoryAtom> theoryAtoms;
	/**
	 * <p>Whether do discard or keep learnt information from enumeration modes.</p>
	 * 
	 * <p>If the enumeration assumption is enabled, then all information learnt from clasp's various enumeration modes is removed after a solve call. This includes enumeration of cautious or brave consequences, enumeration of answer sets with or without projection, or finding optimal models; as well as clauses added with SolveControl.add_clause().</p>
     * 
     * Notes
	 * <ul>
	 * <li>Initially the enumeration assumption is enabled.
	 * <li>In general, the enumeration assumption should be enabled whenever there are multiple calls to solve. Otherwise, the behavior of the solver will be unpredictable because there are no guarantees which information exactly is kept. There might be small speed benefits when disabling the enumeration assumption for single shot solving.
	 * </ul>
	 */
	private Boolean useEnumerationAssumption;
	private PointerByReference ctl;

	/*
	 * Methods
	 * =======
	 */

    public Control() {
		super();
		this.ctl = new PointerByReference();
		NativeClingo.INSTANCE.clingo_control_new(null, new SizeT(0), null, null, Application.MESSAGE_LIMIT, ctl);
	}

	/**
     * Extend the logic program with the given non-ground logic program in string form.
     * @param name The name of program block to add.
     * @param parameters The parameters of the program block to add.
     * @param program The non-ground program in string form.
     * @see {@link #ground(List, Object)}
     */
    public void add(String name, String[] parameters, String program) {
        if (name == null || name.isBlank()) {
			name = Application.PROGRAM_NAME;
		}
        NativeClingo.INSTANCE.clingo_control_add(ctl.getValue(), name, parameters, new SizeT(0), program);
    }


    /**Assign a truth value to an external atom.
        Parameters

        external : Union[Symbol,int]
            A symbol or program literal representing the external atom.
        truth : Optional[bool]
            A Boolean fixes the external to the respective truth value; and None leaves its truth value open.

        Returns

        None

        See Also

        Control.release_external(), SolveControl.symbolic_atoms, SymbolicAtom.is_external
        Notes

        The truth value of an external atom can be changed before each solve call. An atom is treated as external if it has been declared using an #external directive, and has not been released by calling release_external() or defined in a logic program with some rule. If the given atom is not external, then the function has no effect.

        For convenience, the truth assigned to atoms over negative program literals is inverted.

     * @param external
     * @param truth
     */
    public void assignExternal(Union<Symbol, Integer> external, Optional<Boolean> truth) {
    	
    }

/*            def backend(self) -> Backend

        Returns a Backend object providing a low level interface to extend a logic program.
        Returns

        Backend */

    public Backend backend() {
		return null;
    }

/*

    def builder(self) -> ProgramBuilder

        Return a builder to construct a non-ground logic programs.
        Returns

        ProgramBuilder

        See Also

        ProgramBuilder */

    public ProgramBuilder builder() {
    	return null;
    }

/*
    def cleanup(self) -> None

        Cleanup the domain used for grounding by incorporating information from the solver.

        This function cleans up the domain used for grounding. This is done by first simplifying the current program representation (falsifying released external atoms). Afterwards, the top-level implications are used to either remove atoms from the domain or mark them as facts.
        Returns

        None

        Notes

        Any atoms falsified are completely removed from the logic program. Hence, a definition for such an atom in a successive step introduces a fresh atom.
*/

    public Optional<Symbol> getConst() {
    	return null;
    }

/*
    def get_const(self, name:str) -> Optional[Symbol]

        Return the symbol for a constant definition of form: #const name = symbol.
        Parameters

        name : str
            The name of the constant to retrieve.

        Returns

        Optional[Symbol]
            The function returns None if no matching constant definition exists.
*/

    public Optional<Symbol> getConst(String name) {
    	return null;
    }

/*
    def ground(self, parts:List[Tuple[str,List[Symbol]]], context:Any=None) -> None

        Ground the given list of program parts specified by tuples of names and arguments.
        Parameters

        parts : List[Tuple[str,List[Symbol]]]
            List of tuples of program names and program arguments to ground.
        context : Any=None
            A context object whose methods are called during grounding using the @-syntax (if omitted methods, from the main module are used).

        Notes

        Note that parts of a logic program without an explicit #program specification are by default put into a program called base without arguments.
        Examples

        >>> import clingo
        >>> ctl = clingo.Control()
        >>> ctl.add("p", ["t"], "q(t).")
        >>> parts = []
        >>> parts.append(("p", [1]))
        >>> parts.append(("p", [2]))
        >>> ctl.ground(parts)
        >>> ctl.solve(on_model=lambda m: print("Answer: {}".format(m)))
        Answer: q(1) q(2)
        SAT
*/

    public void ground(List<Tuple<String, List<Symbol>>> parts1, Object context) {
        PartT[] parts = new PartT [1];
        parts[0] = new PartT();
        parts[0].name = "base";
        parts[0].params = null;
        parts[0].size = new SizeT(0);
        NativeClingo.INSTANCE.clingo_control_ground(ctl.getValue(), parts, new SizeT(1), null, null);

    }

/*
    def interrupt(self) -> None

        Interrupt the active solve call.
        Returns

        None

        Notes

        This function is thread-safe and can be called from a signal handler. If no search is active, the subsequent call to Control.solve() is interrupted. The result of the Control.solve() method can be used to query if the search was interrupted.
*/

    public void interrupt() {
    	
    }

/*
    def load(self, path:str) -> None

        Extend the logic program with a (non-ground) logic program in a file.
        Parameters

        path : str
            The path of the file to load.

        Returns

        None
*/

    public void load(String path) {
    	
    }

/*
    def register_observer(self, observer:Observer, replace:bool=False) -> None

        Registers the given observer to inspect the produced grounding.
        Parameters

        observer : Observer
            The observer to register. See below for a description of the requirede interface.
        replace : bool=False
            If set to true, the output is just passed to the observer and nolonger to the underlying solver (or any previously registered observers).

        Returns

        None

        Notes

        An observer should be a class of the form below. Not all functions have to be implemented and can be omitted if not needed.
*/

    public void registerObserver(Observer observer, Boolean replace) {
    	
    }

/*
    def register_propagator(self, propagator:Propagator) -> None

        Registers the given propagator with all solvers.
        Parameters

        propagator : Propagator
            The propagator to register.

        Returns

        None

        Notes

        Each symbolic or theory atom is uniquely associated with a positive program atom in form of a positive integer. Program literals additionally have a sign to represent default negation. Furthermore, there are non-zero integer solver literals. There is a surjective mapping from program atoms to solver literals.

        All methods called during propagation use solver literals whereas SymbolicAtom.literal and TheoryAtom.literal return program literals. The function PropagateInit.solver_literal() can be used to map program literals or condition ids to solver literals.

        A propagator should be a class of the form below. Not all functions have to be implemented and can be omitted if not needed.
*/

    public void registerPropagator(Propagator propagator) {
    	
    }

    /*
    def release_external(self, symbol:Union[Symbol,int]) -> None

        Release an external atom represented by the given symbol or program literal.

        This function causes the corresponding atom to become permanently false if there is no definition for the atom in the program. Otherwise, the function has no effect.
        Parameters

        symbol : Union[Symbol,int]
            The symbolic atom or program atom to release.

        Returns

        None

        Notes

        If the program literal is negative, the corresponding atom is released.
        Examples

        The following example shows the effect of assigning and releasing and external atom.

        >>> import clingo
        >>> ctl = clingo.Control()
        >>> ctl.add("base", [], "a. #external b.")
        >>> ctl.ground([("base", [])])
        >>> ctl.assign_external(clingo.Function("b"), True)
        >>> ctl.solve(on_model=lambda m: print("Answer: {}".format(m)))
        Answer: b a
        SAT
        >>> ctl.release_external(clingo.Function("b"))
        >>> ctl.solve(on_model=lambda m: print("Answer: {}".format(m)))
        Answer: a
        SAT
*/

    public void releaseExternal(Union<Symbol,Integer> symbol) {
    	
    }

/*
    def solve(self, assumptions:List[Union[Tuple[Symbol,bool],int]]=[], on_model:Callback[[Model],Optional[bool]]=None, on_statistics:Callback[[StatisticsMap,StatisticsMap],None]=None, on_finish:Callback[[SolveResult],None]=None, yield_:bool=False, async_:bool=False) -> Union[SolveHandle,SolveResult]

        Starts a search.
        Parameters

        assumptions : List[Union[Tuple[Symbol,bool],int]]=[]
            List of (atom, boolean) tuples or program literals that serve as assumptions for the solve call, e.g., solving under assumptions [(Function("a"), True)] only admits answer sets that contain atom a.
        on_model : Callback[[Model],Optional[bool]]=None
            Optional callback for intercepting models. A Model object is passed to the callback. The search can be interruped from the model callback by returning False.
        on_statistics : Callback[[StatisticsMap,StatisticsMap],None]=None
            Optional callback to update statistics. The step and accumulated statistics are passed as arguments.
        on_finish : Callback[[SolveResult],None]=None
            Optional callback called once search has finished. A SolveResult also indicating whether the solve call has been intrrupted is passed to the callback.
        yield_ : bool=False
            The resulting SolveHandle is iterable yielding Model objects.
        async_ : bool=False
            The solve call and the method SolveHandle.resume() of the returned handle are non-blocking.

        Returns

        Union[SolveHandle,SolveResult]
            The return value depends on the parameters. If either yield_ or async_ is true, then a handle is returned. Otherwise, a SolveResult is returned.

        Notes

        If neither yield_ nor async_ is set, the function returns a SolveResult right away.

        Note that in gringo or in clingo with lparse or text output enabled this function just grounds and returns a SolveResult where SolveResult.unknown is true.

        If this function is used in embedded Python code, you might want to start clingo using the --outf=3 option to disable all output from clingo.

        Note that asynchronous solving is only available in clingo with thread support enabled. Furthermore, the on_model and on_finish callbacks are called from another thread. To ensure that the methods can be called, make sure to not use any functions that block Python's GIL indefinitely.

        This function as well as blocking functions on the SolveHandle release the GIL but are not thread-safe.
        Examples

        The following example shows how to intercept models with a callback:

        >>> import clingo
        >>> ctl = clingo.Control("0")
        >>> ctl.add("p", [], "1 { a; b } 1.")
        >>> ctl.ground([("p", [])])
        >>> ctl.solve(on_model=lambda m: print("Answer: {}".format(m)))
        Answer: a
        Answer: b
        SAT

        The following example shows how to yield models:

        >>> import clingo
        >>> ctl = clingo.Control("0")
        >>> ctl.add("p", [], "1 { a; b } 1.")
        >>> ctl.ground([("p", [])])
        >>> with ctl.solve(yield_=True) as handle:
        ...     for m in handle: print("Answer: {}".format(m))
        ...     handle.get()
        ...
        Answer: a
        Answer: b
        SAT

        The following example shows how to solve asynchronously:

        >>> import clingo
        >>> ctl = clingo.Control("0")
        >>> ctl.add("p", [], "1 { a; b } 1.")
        >>> ctl.ground([("p", [])])
        >>> with ctl.solve(on_model=lambda m: print("Answer: {}".format(m)), async_=True) as handle:
        ...     while not handle.wait(0): pass
        ...     handle.get()
        ...
        Answer: a
        Answer: b
        SAT
*/

    public Union<SolveHandle,SolveResult> solve(List<Union<Tuple<Symbol,Boolean>,Integer>> assumptions,
    		Boolean yield,
    		Boolean async,
    		SolveEventHandler handler) {
    	// solve it
        SolveEventCallbackT cb = new SolveEventCallbackT() {
            public boolean call(int type, Pointer event, Pointer goon) {
                if (type == 0) {
                    SizeTByReference num = new SizeTByReference();
                    NativeClingo.INSTANCE.clingo_model_symbols_size(event, 2, num);
                    System.out.printf("model: %d\n", num.getValue());
                    long[] symbols = new long [(int)num.getValue()];
                    NativeClingo.INSTANCE.clingo_model_symbols(event, 2, symbols, new SizeT(num.getValue()));
                    System.out.print("ANSWER:");
                    for (int i = 0; i < num.getValue(); ++i) {
                        SizeTByReference len = new SizeTByReference();
                        NativeClingo.INSTANCE.clingo_symbol_to_string_size(symbols[i], len);
                        byte[] str = new byte[(int)len.getValue()];
                        NativeClingo.INSTANCE.clingo_symbol_to_string(symbols[i], str, new SizeT(len.getValue()));
                        System.out.format(" %s", new String(str));
                    }
                    System.out.println();

                }
                return true;
            }
        };
        PointerByReference hnd = new PointerByReference();
        NativeClingo.INSTANCE.clingo_control_solve(ctl.getValue(), 0, null, new SizeT(0), cb, null, hnd);
        IntByReference res = new IntByReference();
        NativeClingo.INSTANCE.clingo_solve_handle_get(hnd.getValue(), res);
        NativeClingo.INSTANCE.clingo_solve_handle_close(hnd.getValue());
        // clean up
        NativeClingo.INSTANCE.clingo_control_free(ctl.getValue());
    	return null;
    }

}
