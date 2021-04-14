package org.potassco.api;

import static org.junit.Assert.*;

public class Test {

	@org.junit.Test
	public void test() {
		Control ctrl = new Control();
//		ctrl.add("base", null, "p(@id(10)). q(@seq(1,2)).");
		ctrl.add("base", null, "a. b.");
		ctrl.ground(null, ctrl);
		ctrl.solve(null, false, false, null);
	}

}
