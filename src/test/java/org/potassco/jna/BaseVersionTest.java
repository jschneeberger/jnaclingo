package org.potassco.jna;

import static org.junit.Assert.*;

import org.junit.Test;
import org.potassco.jna.BaseClingo;
import org.potassco.jna.ClingoLibrary;

import com.sun.jna.ptr.IntByReference;

public class BaseVersionTest {

	@Test
	public void version1() {
		IntByReference major = new IntByReference();
		IntByReference minor = new IntByReference();
		IntByReference patch = new IntByReference();
		ClingoLibrary.INSTANCE.clingo_version(major, minor, patch);
		assertEquals(5, major.getValue());
		assertEquals(5, minor.getValue());
		assertEquals(0, patch.getValue());
	}

	@Test
	public void version2() {
		assertEquals("5.5.0", BaseClingo.version());
	}

}