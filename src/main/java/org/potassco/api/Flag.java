package org.potassco.api;

/**
 * @author jschneeberger
 * 
 *  class Flag(value:bool=False)

    Helper object to parse command-line flags.
    Parameters

    value : bool=False
        The initial value of the flag.

    Instance variables

    var value : bool

        The value of the flag.


 *
 */
public class Flag {
	private boolean value = false;

	public Flag(boolean value) {
		super();
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
}
