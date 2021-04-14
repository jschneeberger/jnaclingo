package org.potassco.api;

public class Configuration {
/*
 class Configuration

    Allows for changing the configuration of the underlying solver.

    Options are organized hierarchically. To change and inspect an option use:

    config.group.subgroup.option = "value"
    value = config.group.subgroup.option

    There are also arrays of option groups that can be accessed using integer indices:

    config.group.subgroup[0].option = "value1"
    config.group.subgroup[1].option = "value2"

    To list the subgroups of an option group, use the Configuration.keys member. Array option groups, like solver, have a non-negative length and can be iterated. Furthermore, there are meta options having key configuration. Assigning a meta option sets a number of related options. To get further information about an option or option group <opt>, use property __desc_<opt> to retrieve a description.
    Notes

    When integers are assigned to options, they are automatically converted to strings. The value of an option is always a string.
    Examples

    The following example shows how to modify the configuration to enumerate all models:

    >>> import clingo
    >>> prg = clingo.Control()
    >>> prg.configuration.solve.__desc_models
    'Compute at most %A models (0 for all)\n'
    >>> prg.configuration.solve.models = 0
    >>> prg.add("base", [], "{a;b}.")
    >>> prg.ground([("base", [])])
    >>> prg.solve(on_model=lambda m: print("Answer: {}".format(m)))
    Answer:
    Answer: a
    Answer: b
    Answer: a b
    SAT

    Instance variables

    var keys : Optional[List[str]]

        The list of names of sub-option groups or options.

        The list is None if the current object is not an option group.


*/
}
