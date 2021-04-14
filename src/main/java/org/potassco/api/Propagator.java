package org.potassco.api;

public interface Propagator {
	/*
    class Propagator:
        def init(self, init: PropagateInit) -> None:
            """
            This function is called once before each solving step.

            It is used to map relevant program literals to solver literals, add
            watches for solver literals, and initialize the data structures used
            during propagation.

            Parameters
            ----------
            init : PropagateInit
                Object to initialize the propagator.

            Returns
            -------
            None

            Notes
            -----
            This is the last point to access theory atoms.  Once the search has
            started, they are no longer accessible.
            """

        def propagate(self, control: PropagateControl, changes: List[int]) -> None:
            """
            Can be used to propagate solver literals given a partial assignment.

            Parameters
            ----------
            control : PropagateControl
                Object to control propagation.
            changes : List[int]
                List of watched solver literals assigned to true.

            Returns
            -------
            None

            Notes
            -----
            Called during propagation with a non-empty list of watched solver
            literals that have been assigned to true since the last call to either
            propagate, undo, (or the start of the search) - the change set. Only
            watched solver literals are contained in the change set. Each literal
            in the change set is true w.r.t. the current Assignment.
            `PropagateControl.add_clause` can be used to add clauses. If a clause
            is unit resulting, it can be propagated using
            `PropagateControl.propagate`. If either of the two methods returns
            False, the propagate function must return immediately.

                c = ...
                if not control.add_clause(c) or not control.propagate(c):
                    return

            Note that this function can be called from different solving threads.
            Each thread has its own assignment and id, which can be obtained using
            `PropagateControl.id`.
            """

        def undo(self, thread_id: int, assignment: Assignment,
                 changes: List[int]) -> None:
            """
            Called whenever a solver with the given id undos assignments to watched
            solver literals.

            Parameters
            ----------
            thread_id : int
                The solver thread id.
            assignment : Assignment
                Object for inspecting the partial assignment of the solver.
            changes : List[int]
                The list of watched solver literals whose assignment is undone.

            Returns
            -------
            None

            Notes
            -----
            This function is meant to update assignment dependent state in a
            propagator but not to modify the current state of the solver.
            """

        def check(self, control: PropagateControl) -> None:
            """
            This function is similar to propagate but is called without a change
            set on propagation fixpoints.

            When exactly this function is called, can be configured using the @ref
            PropagateInit.check_mode property.

            Parameters
            ----------
            control : PropagateControl
                Object to control propagation.

            Returns
            -------
            None

            Notes
            -----
            This function is called even if no watches have been added.
            """

        def decide(self, thread_id: int, assignment: Assignment, fallback: int) -> int:
            """
            This function allows a propagator to implement domain-specific
            heuristics.

            It is called whenever propagation reaches a fixed point.

            Parameters
            ----------
            thread_id : int
                The solver thread id.
            assignment : Assignment
                Object for inspecting the partial assignment of the solver.
            fallback : int
                The literal choosen by the solver's heuristic.

            Returns
            -------
            int
                Тhe next solver literal to make true.

            Notes
            -----
            This function should return a free solver literal that is to be
            assigned true. In case multiple propagators are registered, this
            function can return 0 to let a propagator registered later make a
            decision. If all propagators return 0, then the fallback literal is
            used.
            """
*/
}
