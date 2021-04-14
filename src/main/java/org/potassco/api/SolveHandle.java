package org.potassco.api;

public class SolveHandle {
/*
 class SolveHandle

    Handle for solve calls.

    SolveHandle objects cannot be created from Python. Instead they are returned by Control.solve(). They can be used to control solving, like, retrieving models or cancelling a search.
    See Also

    Control.solve()
    Notes

    A SolveHandle is a context manager and must be used with Python's with statement.

    Blocking functions in this object release the GIL. They are not thread-safe though.
    Methods

    def cancel(self) -> None

        Cancel the running search.
        Returns

        None

        See Also

        Control.interrupt()
    def get(self) -> SolveResult

        Get the result of a solve call.

        If the search is not completed yet, the function blocks until the result is ready.
        Returns

        SolveResult

    def resume(self) -> None

        Discards the last model and starts searching for the next one.
        Notes

        If the search has been started asynchronously, this function also starts the search in the background. A model that was not yet retrieved by calling __next__ is not discared.
    def wait(self, timeout:Optional[float]=None) -> Optional[bool]

        Wait for solve call to finish with an optional timeout.
        Parameters

        timeout : Optional[float]=None
            If a timeout is given, the function blocks for at most timeout seconds.

        Returns

        Optional[bool]
            If a timout is given, returns a Boolean indicating whether the search has finished. Otherwise, the function blocks until the search is finished and returns nothing.


 */
}
