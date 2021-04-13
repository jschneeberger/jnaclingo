package org.potassco.clingo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.potassco.jclingo.NativeClingo;

import com.sun.jna.ptr.IntByReference;

public class ClingoJNATest {

	@Test
	public void test() {
        IntByReference major = new IntByReference();
        IntByReference minor = new IntByReference();
        IntByReference patch = new IntByReference();
		NativeClingo nativeLib = NativeClingo.INSTANCE;
		nativeLib.clingo_version(major, minor, patch);
		assertEquals(5, major.getValue());
		assertEquals(5, minor.getValue());
		assertEquals(0, patch.getValue());
	}

}
