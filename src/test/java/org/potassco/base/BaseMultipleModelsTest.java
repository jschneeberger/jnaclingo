package org.potassco.base;

import static org.junit.Assert.*;

import org.junit.Test;
import org.potassco.enums.SolveMode;
import org.potassco.jna.Part;
import org.potassco.jna.Size;

import com.sun.jna.Pointer;

public class BaseMultipleModelsTest {

	@Test
	public void test() {
		String name = "base";
		String program = "1 {p(1..3)} 2.";
		String[] arguments = { "0" }; // enumerate all models
		Pointer control = BaseClingo.control(arguments, null, null, 0);
		BaseClingo.controlAdd(control, name, null, program);
		Part[] parts = new Part[1];
		parts[0] = new Part(name, null, new Size(0));
		BaseClingo.controlGround(control, parts, new Size(1), null, null);
		Pointer handle = BaseClingo.controlSolve(control, SolveMode.YIELD, null, 0, null, null);
		boolean modelExits = true;
		int i = 0;
		while (modelExits) {
			Pointer model = BaseClingo.solveHandleModel(handle);
			if (model != null) {
				long mn = BaseClingo.modelNumber(model);
				BaseClingo.solveHandleResume(handle);
				i++;
			} else {
				modelExits = false;
			}
		}
		assertEquals(6, i);
	}

}