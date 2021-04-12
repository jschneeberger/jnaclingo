package org.potassco.clingo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jna.ptr.IntByReference;

public class ClingoTest {

	@Test
	public void test() {
		IntByReference major = null;
		IntByReference minor = null;
		IntByReference patch = null;
		CLibrary nativeLib = CLibrary.INSTANCE;
		nativeLib.clingo_version(major, minor, patch);
		assertEquals(0, major);
		assertEquals(0, minor);
		assertEquals(0, patch);
	}

}
