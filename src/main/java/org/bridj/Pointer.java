package org.bridj;
/*
 * BridJ - Dynamic and blazing-fast native interop for Java.
 * http://bridj.googlecode.com/
 *
 * Copyright (c) 2010-2015, Olivier Chafik (http://ochafik.com/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Olivier Chafik nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY OLIVIER CHAFIK AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.*;
import java.lang.annotation.Annotation;
import java.util.*;

import org.bridj.util.DefaultParameterizedType;
import org.bridj.util.Utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import static org.bridj.SizeT.safeIntCast;

/**
 * Pointer to a native memory location.<br>
 * Pointer is the entry point of any pointer-related operation in BridJ.
 * <p>
 * <u><b>Manipulating memory</b></u>
 * <p>
 * <ul>
 *	<li>Wrapping a memory address as a pointer : {@link Pointer#pointerToAddress(long)}
 *  </li>
 *	<li>Reading / writing a primitive from / to the pointed memory location :<br>
 *		{@link Pointer#getInt()} / {@link Pointer#setInt(int)} <br>
 *		{@link Pointer#getLong()} / {@link Pointer#setLong(long)} <br>
 *		{@link Pointer#getShort()} / {@link Pointer#setShort(short)} <br>
 *		{@link Pointer#getByte()} / {@link Pointer#setByte(byte)} <br>
 *		{@link Pointer#getChar()} / {@link Pointer#setChar(char)} <br>
 *		{@link Pointer#getFloat()} / {@link Pointer#setFloat(float)} <br>
 *		{@link Pointer#getDouble()} / {@link Pointer#setDouble(double)} <br>
 *		{@link Pointer#getBoolean()} / {@link Pointer#setBoolean(boolean)} <br>
 *		{@link Pointer#getSizeT()} / {@link Pointer#setSizeT(long)} <br>
 *		{@link Pointer#getCLong()} / {@link Pointer#setCLong(long)} <br>
 *</li>
 *	<li>Reading / writing the nth contiguous primitive value from / to the pointed memory location :<br>
 *		{@link Pointer#getIntAtIndex(long)} / {@link Pointer#setIntAtIndex(long, int)} <br>
 *		{@link Pointer#getLongAtIndex(long)} / {@link Pointer#setLongAtIndex(long, long)} <br>
 *		{@link Pointer#getShortAtIndex(long)} / {@link Pointer#setShortAtIndex(long, short)} <br>
 *		{@link Pointer#getByteAtIndex(long)} / {@link Pointer#setByteAtIndex(long, byte)} <br>
 *		{@link Pointer#getCharAtIndex(long)} / {@link Pointer#setCharAtIndex(long, char)} <br>
 *		{@link Pointer#getFloatAtIndex(long)} / {@link Pointer#setFloatAtIndex(long, float)} <br>
 *		{@link Pointer#getDoubleAtIndex(long)} / {@link Pointer#setDoubleAtIndex(long, double)} <br>
 *		{@link Pointer#getBooleanAtIndex(long)} / {@link Pointer#setBooleanAtIndex(long, boolean)} <br>
 *	  {@link Pointer#getSizeTAtIndex(long)} / {@link Pointer#setSizeTAtIndex(long, long)} <br>
 *	  {@link Pointer#getCLongAtIndex(long)} / {@link Pointer#setCLongAtIndex(long, long)} <br>
 *</li>
 *	<li>Reading / writing a primitive from / to the pointed memory location with a byte offset:<br>
 *		{@link Pointer#getIntAtOffset(long)} / {@link Pointer#setIntAtOffset(long, int)} <br>
 *		{@link Pointer#getLongAtOffset(long)} / {@link Pointer#setLongAtOffset(long, long)} <br>
 *		{@link Pointer#getShortAtOffset(long)} / {@link Pointer#setShortAtOffset(long, short)} <br>
 *		{@link Pointer#getByteAtOffset(long)} / {@link Pointer#setByteAtOffset(long, byte)} <br>
 *		{@link Pointer#getCharAtOffset(long)} / {@link Pointer#setCharAtOffset(long, char)} <br>
 *		{@link Pointer#getFloatAtOffset(long)} / {@link Pointer#setFloatAtOffset(long, float)} <br>
 *		{@link Pointer#getDoubleAtOffset(long)} / {@link Pointer#setDoubleAtOffset(long, double)} <br>
 *		{@link Pointer#getBooleanAtOffset(long)} / {@link Pointer#setBooleanAtOffset(long, boolean)} <br>
 *		{@link Pointer#getSizeTAtOffset(long)} / {@link Pointer#setSizeTAtOffset(long, long)} <br>
 *		{@link Pointer#getCLongAtOffset(long)} / {@link Pointer#setCLongAtOffset(long, long)} <br>
 *</li>
 *	<li>Reading / writing an array of primitives from / to the pointed memory location :<br>
 *		{@link Pointer#getInts(int)} / {@link Pointer#setInts(int[])} ; With an offset : {@link Pointer#getIntsAtOffset(long, int)} / {@link Pointer#setIntsAtOffset(long, int[])}<br>
 *		{@link Pointer#getLongs(int)} / {@link Pointer#setLongs(long[])} ; With an offset : {@link Pointer#getLongsAtOffset(long, int)} / {@link Pointer#setLongsAtOffset(long, long[])}<br>
 *		{@link Pointer#getShorts(int)} / {@link Pointer#setShorts(short[])} ; With an offset : {@link Pointer#getShortsAtOffset(long, int)} / {@link Pointer#setShortsAtOffset(long, short[])}<br>
 *		{@link Pointer#getBytes(int)} / {@link Pointer#setBytes(byte[])} ; With an offset : {@link Pointer#getBytesAtOffset(long, int)} / {@link Pointer#setBytesAtOffset(long, byte[])}<br>
 *		{@link Pointer#getChars(int)} / {@link Pointer#setChars(char[])} ; With an offset : {@link Pointer#getCharsAtOffset(long, int)} / {@link Pointer#setCharsAtOffset(long, char[])}<br>
 *		{@link Pointer#getFloats(int)} / {@link Pointer#setFloats(float[])} ; With an offset : {@link Pointer#getFloatsAtOffset(long, int)} / {@link Pointer#setFloatsAtOffset(long, float[])}<br>
 *		{@link Pointer#getDoubles(int)} / {@link Pointer#setDoubles(double[])} ; With an offset : {@link Pointer#getDoublesAtOffset(long, int)} / {@link Pointer#setDoublesAtOffset(long, double[])}<br>
 *		{@link Pointer#getBooleans(int)} / {@link Pointer#setBooleans(boolean[])} ; With an offset : {@link Pointer#getBooleansAtOffset(long, int)} / {@link Pointer#setBooleansAtOffset(long, boolean[])}<br>
 *		{@link Pointer#getSizeTs(int)} / {@link Pointer#setSizeTs(long[])} ; With an offset : {@link Pointer#getSizeTsAtOffset(long, int)} / {@link Pointer#setSizeTsAtOffset(long, long[])}<br>
 *		{@link Pointer#getCLongs(int)} / {@link Pointer#setCLongs(long[])} ; With an offset : {@link Pointer#getCLongsAtOffset(long, int)} / {@link Pointer#setCLongsAtOffset(long, long[])}<br>
 *  </li>
 *	<li>Reading / writing an NIO buffer of primitives from / to the pointed memory location :<br>
*		{@link Pointer#getIntBuffer(long)} (can be used for writing as well) / {@link Pointer#setInts(IntBuffer)}<br>
*		{@link Pointer#getLongBuffer(long)} (can be used for writing as well) / {@link Pointer#setLongs(LongBuffer)}<br>
*		{@link Pointer#getShortBuffer(long)} (can be used for writing as well) / {@link Pointer#setShorts(ShortBuffer)}<br>
*		{@link Pointer#getByteBuffer(long)} (can be used for writing as well) / {@link Pointer#setBytes(ByteBuffer)}<br>
*		{@link Pointer#getFloatBuffer(long)} (can be used for writing as well) / {@link Pointer#setFloats(FloatBuffer)}<br>
*		{@link Pointer#getDoubleBuffer(long)} (can be used for writing as well) / {@link Pointer#setDoubles(DoubleBuffer)}<br>
 *  </li>
 *  <li>Reading / writing a String from / to the pointed memory location using the default charset :<br>
*		{@link Pointer#getCString()} / {@link Pointer#setCString(String)} ; With an offset : {@link Pointer#getCStringAtOffset(long)} / {@link Pointer#setCStringAtOffset(long, String)}<br>
*		{@link Pointer#getWideCString()} / {@link Pointer#setWideCString(String)} ; With an offset : {@link Pointer#getWideCStringAtOffset(long)} / {@link Pointer#setWideCStringAtOffset(long, String)}<br>
 *  </li>
 *  <li>Reading / writing a String with control on the charset :<br>
 *		{@link Pointer#getStringAtOffset(long, StringType, Charset)} / {@link Pointer#setStringAtOffset(long, String, StringType, Charset)}<br>
 * </ul>
 * <p>
 * <u><b>Allocating memory</b></u>
 * <p>
 * <ul>
 *	<li>Getting the pointer to a struct / a C++ class / a COM object :
 *		{@link Pointer#getPointer(NativeObject)}
 *  </li>
 *  <li>Allocating a dynamic callback (without a static {@link Callback} definition, which would be the preferred way) :<br>
 *      {@link Pointer#allocateDynamicCallback(DynamicCallback, org.bridj.ann.Convention.Style, Type, Type[])}
 *  </li>
 *	<li>Allocating a primitive with / without an initial value (zero-initialized) :<br>
 *		{@link Pointer#pointerToInt(int)} / {@link Pointer#allocateInt()}<br>
 *		{@link Pointer#pointerToLong(long)} / {@link Pointer#allocateLong()}<br>
 *		{@link Pointer#pointerToShort(short)} / {@link Pointer#allocateShort()}<br>
 *		{@link Pointer#pointerToByte(byte)} / {@link Pointer#allocateByte()}<br>
 *		{@link Pointer#pointerToChar(char)} / {@link Pointer#allocateChar()}<br>
 *		{@link Pointer#pointerToFloat(float)} / {@link Pointer#allocateFloat()}<br>
 *		{@link Pointer#pointerToDouble(double)} / {@link Pointer#allocateDouble()}<br>
 *		{@link Pointer#pointerToBoolean(boolean)} / {@link Pointer#allocateBoolean()}<br>
 *		{@link Pointer#pointerToSizeT(long)} / {@link Pointer#allocateSizeT()}<br>
 *		{@link Pointer#pointerToCLong(long)} / {@link Pointer#allocateCLong()}<br>
 *  </li>
 *	<li>Allocating an array of primitives with / without initial values (zero-initialized) :<br>
 *		{@link Pointer#pointerToInts(int[])} or {@link Pointer#pointerToInts(IntBuffer)} / {@link Pointer#allocateInts(long)}<br>
 *		{@link Pointer#pointerToLongs(long[])} or {@link Pointer#pointerToLongs(LongBuffer)} / {@link Pointer#allocateLongs(long)}<br>
 *		{@link Pointer#pointerToShorts(short[])} or {@link Pointer#pointerToShorts(ShortBuffer)} / {@link Pointer#allocateShorts(long)}<br>
 *		{@link Pointer#pointerToBytes(byte[])} or {@link Pointer#pointerToBytes(ByteBuffer)} / {@link Pointer#allocateBytes(long)}<br>
 *		{@link Pointer#pointerToChars(char[])} or {@link Pointer#pointerToChars(CharBuffer)} / {@link Pointer#allocateChars(long)}<br>
 *		{@link Pointer#pointerToFloats(float[])} or {@link Pointer#pointerToFloats(FloatBuffer)} / {@link Pointer#allocateFloats(long)}<br>
 *		{@link Pointer#pointerToDoubles(double[])} or {@link Pointer#pointerToDoubles(DoubleBuffer)} / {@link Pointer#allocateDoubles(long)}<br>
 *		{@link Pointer#pointerToSizeTs(long[])} / {@link Pointer#allocateSizeTs(long)}<br>
 *		{@link Pointer#pointerToCLongs(long[])} / {@link Pointer#allocateCLongs(long)}<br>
 *		{@link Pointer#pointerToBuffer(Buffer)} / n/a<br>
 *  </li>
 *  <li>Allocating a native String :<br>
*		{@link Pointer#pointerToCString(String) } (default charset)<br>
*		{@link Pointer#pointerToWideCString(String) } (default charset)<br>
 *		{@link Pointer#pointerToString(String, StringType, Charset) }<br>
 *  </li>
 *  <li>Allocating a {@link ListType#Dynamic} Java {@link java.util.List} that uses native memory storage  (think of getting back the pointer with {@link NativeList#getPointer()} when you're done mutating the list):<br>
 *		{@link Pointer#allocateList(Class, long) }
 *  </li>
 *  <li>Transforming a pointer to a Java {@link java.util.List} that uses the pointer as storage (think of getting back the pointer with {@link NativeList#getPointer()} when you're done mutating the list, if it's {@link ListType#Dynamic}) :<br>
 *		{@link Pointer#asList(ListType) }<br>
 *		{@link Pointer#asList() }<br>
 *  </li>
 * </ul>
 * <p>
 * <u><b>Casting pointers</b></u>
 * <p>
 * <ul>
 *	<li>Cast a pointer to a {@link DynamicFunction} :<br>
 *		{@link Pointer#asDynamicFunction(org.bridj.ann.Convention.Style, java.lang.reflect.Type, java.lang.reflect.Type[]) }
 *  </li>
 *	<li>Cast a pointer to a {@link StructObject} or a {@link Callback} (as the ones generated by <a href="http://code.google.com/p/jnaerator/">JNAerator</a>) <br>:
 *		{@link Pointer#as(Class) }
 *  </li>
 *	<li>Cast a pointer to a complex type pointer (use {@link org.bridj.cpp.CPPType#getCPPType(Object[])} to create a C++ template type, for instance) :<br>
 *		{@link Pointer#as(Type) }
 *  </li>
 *	<li>Get an untyped pointer :<br>
 *		{@link Pointer#asUntyped() }
 *  </li>
 * </ul>
 * <p>
 * <u><b>Dealing with pointer bounds</b></u>
 * <p>
 * <ul>
 *	<li>Pointers to memory allocated through Pointer.pointerTo*, Pointer.allocate* have validity bounds that help prevent buffer overflows, at least when the Pointer API is used
 *  </li>
 *	<li>{@link Pointer#offset(long)}, {@link Pointer#next(long)} and other similar methods retain pointer bounds
 *  </li>
 *	<li>{@link Pointer#getValidBytes()} and {@link Pointer#getValidElements()} return the amount of valid memory readable from the pointer 
 *  </li>
 *	<li>Bounds can be declared manually with {@link Pointer#validBytes(long)} (useful for memory allocated by native code) 
 *  </li>
 * </ul>
 */
public abstract class Pointer<T> implements Comparable<Pointer<?>>, Iterable<T>
{


	
	
	/** The NULL pointer is <b>always</b> Java's null value */
    public static final Pointer<?> NULL = null;
	
    /** 
     * Size of a pointer in bytes. <br>
     * This is 4 bytes in a 32 bits environment and 8 bytes in a 64 bits environment.<br>
     * Note that some 64 bits environments allow for 32 bits JVM execution (using the -d32 command line argument for Sun's JVM, for instance). In that case, Java programs will believe they're executed in a 32 bits environment. 
     */
    public static final int SIZE = Platform.POINTER_SIZE;
    
	static {
        Platform.initLibrary();
    }
    
    
	protected static final long UNKNOWN_VALIDITY = -1;
	protected static final long NO_PARENT = 0/*-1*/;
  private static final long POINTER_MASK = Platform.is64Bits() ? -1 : 0xFFFFFFFFL;
	
	/**
	 * Default alignment used to allocate memory from the static factory methods in Pointer class (any value lower or equal to 1 means no alignment)
	 */
	public static final int defaultAlignment = Integer.parseInt(Platform.getenvOrProperty("BRIDJ_DEFAULT_ALIGNMENT", "bridj.defaultAlignment", "-1"));
	
	protected final PointerIO<T> io;
	private final long peer_;
     protected final long offsetInParent;
	protected final Pointer<?> parent;
	protected volatile Object sibling;
	protected final long validStart;
     protected final long validEnd;

	/**
	 * Object responsible for reclamation of some pointed memory when it's not used anymore.
	 */
	public interface Releaser {
		void release(Pointer<?> p);
	}
	
	Pointer(PointerIO<T> io, long peer, long validStart, long validEnd, Pointer<?> parent, long offsetInParent, Object sibling) {
		this.io = io;
		this.peer_ = peer;
		this.validStart = validStart;
		this.validEnd = validEnd;
		this.parent = parent;
		this.offsetInParent = offsetInParent;
		this.sibling = sibling;
		if (peer == 0)
			throw new IllegalArgumentException("Pointer instance cannot have NULL peer ! (use null Pointer instead)");
		if (BridJ.debugPointers) {
			creationTrace = new RuntimeException().fillInStackTrace();
          }
	}
	Throwable creationTrace;
     Throwable deletionTrace;
     Throwable releaseTrace;
     

	static class OrderedPointer<T> extends Pointer<T> {
		OrderedPointer(PointerIO<T> io, long peer, long validStart, long validEnd, Pointer<?> parent, long offsetInParent, Object sibling) {
			super(io, peer, validStart, validEnd, parent, offsetInParent, sibling);
		}
	
		@Override
		public boolean isOrdered() {
			return true;
		}
		
 
		@Override
		public Pointer<T> setInt(int value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_int(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setIntAtOffset(long byteOffset, int value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_int(checkedPeer, value);
						return this;
		}
	
		@Override
		public int getInt() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_int(checkedPeer);
					}
    
		@Override
		public int getIntAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_int(checkedPeer);
					}
 
		@Override
		public Pointer<T> setLong(long value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_long(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setLongAtOffset(long byteOffset, long value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_long(checkedPeer, value);
						return this;
		}
	
		@Override
		public long getLong() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_long(checkedPeer);
					}
    
		@Override
		public long getLongAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_long(checkedPeer);
					}
 
		@Override
		public Pointer<T> setShort(short value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						JNI.set_short(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setShortAtOffset(long byteOffset, short value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						JNI.set_short(checkedPeer, value);
						return this;
		}
	
		@Override
		public short getShort() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						return JNI.get_short(checkedPeer);
					}
    
		@Override
		public short getShortAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						return JNI.get_short(checkedPeer);
					}
 
		@Override
		public Pointer<T> setByte(byte value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_byte(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setByteAtOffset(long byteOffset, byte value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_byte(checkedPeer, value);
						return this;
		}
	
		@Override
		public byte getByte() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_byte(checkedPeer);
					}
    
		@Override
		public byte getByteAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_byte(checkedPeer);
					}
 
		@Override
		public Pointer<T> setChar(char value) {
						if (Platform.WCHAR_T_SIZE == 4)
				return setInt((int)value);
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						JNI.set_char(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setCharAtOffset(long byteOffset, char value) {
						if (Platform.WCHAR_T_SIZE == 4)
				return setIntAtOffset(byteOffset, (int)value);
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						JNI.set_char(checkedPeer, value);
						return this;
		}
	
		@Override
		public char getChar() {
						if (Platform.WCHAR_T_SIZE == 4)
				return (char)getInt();
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						return JNI.get_char(checkedPeer);
					}
    
		@Override
		public char getCharAtOffset(long byteOffset) {
						if (Platform.WCHAR_T_SIZE == 4)
				return (char)getIntAtOffset(byteOffset);
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						return JNI.get_char(checkedPeer);
					}
 
		@Override
		public Pointer<T> setFloat(float value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_float(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setFloatAtOffset(long byteOffset, float value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_float(checkedPeer, value);
						return this;
		}
	
		@Override
		public float getFloat() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_float(checkedPeer);
					}
    
		@Override
		public float getFloatAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_float(checkedPeer);
					}
 
		@Override
		public Pointer<T> setDouble(double value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_double(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setDoubleAtOffset(long byteOffset, double value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_double(checkedPeer, value);
						return this;
		}
	
		@Override
		public double getDouble() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_double(checkedPeer);
					}
    
		@Override
		public double getDoubleAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_double(checkedPeer);
					}
 
		@Override
		public Pointer<T> setBoolean(boolean value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_boolean(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setBooleanAtOffset(long byteOffset, boolean value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_boolean(checkedPeer, value);
						return this;
		}
	
		@Override
		public boolean getBoolean() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_boolean(checkedPeer);
					}
    
		@Override
		public boolean getBooleanAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_boolean(checkedPeer);
					}



	@Override
    public Pointer<T> setSizeTsAtOffset(long byteOffset, long[] values, int valuesOffset, int length) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		if (SizeT.SIZE == 8) {
			setLongsAtOffset(byteOffset, values, valuesOffset, length);
		} else {
			int n = length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 4);
	}
            
			long peer = checkedPeer;
			int valuesIndex = valuesOffset;
			for (int i = 0; i < n; i++) {
				int value = (int)values[valuesIndex];
						JNI.set_int(peer, value);
					peer += 4;
				valuesIndex++;
			}
		}
		return this;
	}
	/**
     * Write an array of SizeT values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeTs(int[])} over this method. 
	 */
	public Pointer<T> setSizeTsAtOffset(long byteOffset, int[] values) {
		if (SizeT.SIZE == 4) {
			setIntsAtOffset(byteOffset, values);
		} else {
			int n = values.length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 8);
	}
            
			long peer = checkedPeer;
			for (int i = 0; i < n; i++) {
				int value = values[i];
						JNI.set_long(peer, value);
					peer += 8;
			}
		}
		return this;
	}


	@Override
    public Pointer<T> setCLongsAtOffset(long byteOffset, long[] values, int valuesOffset, int length) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		if (CLong.SIZE == 8) {
			setLongsAtOffset(byteOffset, values, valuesOffset, length);
		} else {
			int n = length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 4);
	}
            
			long peer = checkedPeer;
			int valuesIndex = valuesOffset;
			for (int i = 0; i < n; i++) {
				int value = (int)values[valuesIndex];
						JNI.set_int(peer, value);
					peer += 4;
				valuesIndex++;
			}
		}
		return this;
	}
	/**
     * Write an array of CLong values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLongs(int[])} over this method. 
	 */
	public Pointer<T> setCLongsAtOffset(long byteOffset, int[] values) {
		if (CLong.SIZE == 4) {
			setIntsAtOffset(byteOffset, values);
		} else {
			int n = values.length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 8);
	}
            
			long peer = checkedPeer;
			for (int i = 0; i < n; i++) {
				int value = values[i];
						JNI.set_long(peer, value);
					peer += 8;
			}
		}
		return this;
	}
	}

	static class DisorderedPointer<T> extends Pointer<T> {
		DisorderedPointer(PointerIO<T> io, long peer, long validStart, long validEnd, Pointer<?> parent, long offsetInParent, Object sibling) {
			super(io, peer, validStart, validEnd, parent, offsetInParent, sibling);
		}
	
		@Override
		public boolean isOrdered() {
			return false;
		}
		
 
		@Override
		public Pointer<T> setInt(int value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_int_disordered(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setIntAtOffset(long byteOffset, int value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_int_disordered(checkedPeer, value);
						return this;
		}
	
		@Override
		public int getInt() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_int_disordered(checkedPeer);
					}
    
		@Override
		public int getIntAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_int_disordered(checkedPeer);
					}
 
		@Override
		public Pointer<T> setLong(long value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_long_disordered(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setLongAtOffset(long byteOffset, long value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_long_disordered(checkedPeer, value);
						return this;
		}
	
		@Override
		public long getLong() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_long_disordered(checkedPeer);
					}
    
		@Override
		public long getLongAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_long_disordered(checkedPeer);
					}
 
		@Override
		public Pointer<T> setShort(short value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						JNI.set_short_disordered(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setShortAtOffset(long byteOffset, short value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						JNI.set_short_disordered(checkedPeer, value);
						return this;
		}
	
		@Override
		public short getShort() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						return JNI.get_short_disordered(checkedPeer);
					}
    
		@Override
		public short getShortAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2);
	}
						return JNI.get_short_disordered(checkedPeer);
					}
 
		@Override
		public Pointer<T> setByte(byte value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_byte(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setByteAtOffset(long byteOffset, byte value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_byte(checkedPeer, value);
						return this;
		}
	
		@Override
		public byte getByte() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_byte(checkedPeer);
					}
    
		@Override
		public byte getByteAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_byte(checkedPeer);
					}
 
		@Override
		public Pointer<T> setChar(char value) {
						if (Platform.WCHAR_T_SIZE == 4)
				return setInt((int)value);
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						JNI.set_char_disordered(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setCharAtOffset(long byteOffset, char value) {
						if (Platform.WCHAR_T_SIZE == 4)
				return setIntAtOffset(byteOffset, (int)value);
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						JNI.set_char_disordered(checkedPeer, value);
						return this;
		}
	
		@Override
		public char getChar() {
						if (Platform.WCHAR_T_SIZE == 4)
				return (char)getInt();
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						return JNI.get_char_disordered(checkedPeer);
					}
    
		@Override
		public char getCharAtOffset(long byteOffset) {
						if (Platform.WCHAR_T_SIZE == 4)
				return (char)getIntAtOffset(byteOffset);
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
						return JNI.get_char_disordered(checkedPeer);
					}
 
		@Override
		public Pointer<T> setFloat(float value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_float_disordered(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setFloatAtOffset(long byteOffset, float value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						JNI.set_float_disordered(checkedPeer, value);
						return this;
		}
	
		@Override
		public float getFloat() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_float_disordered(checkedPeer);
					}
    
		@Override
		public float getFloatAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4);
	}
						return JNI.get_float_disordered(checkedPeer);
					}
 
		@Override
		public Pointer<T> setDouble(double value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_double_disordered(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setDoubleAtOffset(long byteOffset, double value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						JNI.set_double_disordered(checkedPeer, value);
						return this;
		}
	
		@Override
		public double getDouble() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_double_disordered(checkedPeer);
					}
    
		@Override
		public double getDoubleAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8);
	}
						return JNI.get_double_disordered(checkedPeer);
					}
 
		@Override
		public Pointer<T> setBoolean(boolean value) {
						
					long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_boolean(checkedPeer, value);
						return this;
		}
		
		@Override
		public Pointer<T> setBooleanAtOffset(long byteOffset, boolean value) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						JNI.set_boolean(checkedPeer, value);
						return this;
		}
	
		@Override
		public boolean getBoolean() {
								long checkedPeer = getPeer() + 0;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_boolean(checkedPeer);
					}
    
		@Override
		public boolean getBooleanAtOffset(long byteOffset) {
							long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
						return JNI.get_boolean(checkedPeer);
					}



	@Override
    public Pointer<T> setSizeTsAtOffset(long byteOffset, long[] values, int valuesOffset, int length) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		if (SizeT.SIZE == 8) {
			setLongsAtOffset(byteOffset, values, valuesOffset, length);
		} else {
			int n = length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 4);
	}
            
			long peer = checkedPeer;
			int valuesIndex = valuesOffset;
			for (int i = 0; i < n; i++) {
				int value = (int)values[valuesIndex];
						JNI.set_int_disordered(peer, value);
					peer += 4;
				valuesIndex++;
			}
		}
		return this;
	}
	/**
     * Write an array of SizeT values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeTs(int[])} over this method. 
	 */
	public Pointer<T> setSizeTsAtOffset(long byteOffset, int[] values) {
		if (SizeT.SIZE == 4) {
			setIntsAtOffset(byteOffset, values);
		} else {
			int n = values.length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 8);
	}
            
			long peer = checkedPeer;
			for (int i = 0; i < n; i++) {
				int value = values[i];
						JNI.set_long_disordered(peer, value);
					peer += 8;
			}
		}
		return this;
	}


	@Override
    public Pointer<T> setCLongsAtOffset(long byteOffset, long[] values, int valuesOffset, int length) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		if (CLong.SIZE == 8) {
			setLongsAtOffset(byteOffset, values, valuesOffset, length);
		} else {
			int n = length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 4) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 4);
	}
            
			long peer = checkedPeer;
			int valuesIndex = valuesOffset;
			for (int i = 0; i < n; i++) {
				int value = (int)values[valuesIndex];
						JNI.set_int_disordered(peer, value);
					peer += 4;
				valuesIndex++;
			}
		}
		return this;
	}
	/**
     * Write an array of CLong values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLongs(int[])} over this method. 
	 */
	public Pointer<T> setCLongsAtOffset(long byteOffset, int[] values) {
		if (CLong.SIZE == 4) {
			setIntsAtOffset(byteOffset, values);
		} else {
			int n = values.length;
				long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + n * 8) > validEnd
	   )) {
		invalidPeer(checkedPeer, n * 8);
	}
            
			long peer = checkedPeer;
			for (int i = 0; i < n; i++) {
				int value = values[i];
						JNI.set_long_disordered(peer, value);
					peer += 8;
			}
		}
		return this;
	}
	}

	/**
	 * Create a {@code Pointer<T>} type. <br>
	 * For Instance, {@code Pointer.pointerType(Integer.class) } returns a type that represents {@code Pointer<Integer> }  
	 */
	public static Type pointerType(Type targetType) {
		return org.bridj.util.DefaultParameterizedType.paramType(Pointer.class, targetType);	
	}
	/**
	 * Create a {@code IntValuedEnum<T>} type. <br>
	 * For Instance, {@code Pointer.intEnumType(SomeEnum.class) } returns a type that represents {@code IntValuedEnum<SomeEnum> }  
	 */
	public static <E extends Enum<E>> Type intEnumType(Class<? extends IntValuedEnum<E>> targetType) {
		return org.bridj.util.DefaultParameterizedType.paramType(IntValuedEnum.class, targetType);	
	}
	
	/**
	 * Manually release the memory pointed by this pointer if it was allocated on the Java side.<br>
	 * If the pointer is an offset version of another pointer (using {@link Pointer#offset(long)} or {@link Pointer#next(long)}, for instance), this method tries to release the original pointer.<br>
	 * If the memory was not allocated from the Java side, this method does nothing either.<br>
	 * If the memory was already successfully released, this throws a RuntimeException.
	 * @throws RuntimeException if the pointer was already released
	 */
	public synchronized void release() {
		Object sibling = this.sibling;
		this.sibling = null;
		if (sibling instanceof Pointer) {
			((Pointer)sibling).release();
          }
          //this.peer_ = 0;
          if (BridJ.debugPointerReleases) {
               releaseTrace = new RuntimeException().fillInStackTrace();
          }
	}

	/**
	 * Compare to another pointer based on pointed addresses.
	 * @param p other pointer
	 * @return 1 if this pointer's address is greater than p's (or if p is null), -1 if the opposite is true, 0 if this and p point to the same memory location.
	 */
	//@Override
    public int compareTo(Pointer<?> p) {
		if (p == null)
			return 1;
		
		long p1 = getPeer(), p2 = p.getPeer();
		return p1 == p2 ? 0 : p1 < p2 ? -1 : 1;
	}
	
	/**
	* Compare the byteCount bytes at the memory location pointed by this pointer to the byteCount bytes at the memory location pointer by other using the C <a href="http://www.cplusplus.com/reference/clibrary/cstring/memcmp/">memcmp</a> function.<br>
	 * @return 0 if the two memory blocks are equal, -1 if this pointer's memory is "less" than the other and 1 otherwise.
	 */
	public int compareBytes(Pointer<?> other, long byteCount) {
		return compareBytesAtOffset(0, other, 0, byteCount);	
	}
	
	/**
	 * Compare the byteCount bytes at the memory location pointed by this pointer shifted by byteOffset to the byteCount bytes at the memory location pointer by other shifted by otherByteOffset using the C <a href="http://www.cplusplus.com/reference/clibrary/cstring/memcmp/">memcmp</a> function.<br>
	 * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues)
	 * @return 0 if the two memory blocks are equal, -1 if this pointer's memory is "less" than the other and 1 otherwise.
	 */
	public int compareBytesAtOffset(long byteOffset, Pointer<?> other, long otherByteOffset, long byteCount) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + byteCount) > validEnd
	   )) {
		invalidPeer(checkedPeer, byteCount);
	}
		return JNI.memcmp(checkedPeer, other.getCheckedPeer(otherByteOffset, byteCount), byteCount);	
	}
	
    /**
	 * Compute a hash code based on pointed address.
	 */
	@Override
    public int hashCode() {
		int hc = new Long(getPeer()).hashCode();
		return hc;
    }
    
    @Override 
    public String toString() {
		return "Pointer(peer = 0x" + Long.toHexString(getPeer()) +
        ", targetType = " + Utils.toString(getTargetType()) +
        ", order = " + order() +
        ", valid bytes = " + getValidBytes() +
        ")";
    }
    
    protected final void invalidPeer(long peer, long validityCheckLength) {
		throw new IndexOutOfBoundsException("Cannot access to memory data of length " + validityCheckLength + " at offset " + (peer - getPeer()) + " : valid memory start is " + validStart + ", valid memory size is " + (validEnd - validStart));
	}
	
    private final long getCheckedPeer(long byteOffset, long validityCheckLength) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + validityCheckLength) > validEnd
	   )) {
		invalidPeer(checkedPeer, validityCheckLength);
	}
		return checkedPeer;
    }
    
    /**
	 * Returns a pointer which address value was obtained by this pointer's by adding a byte offset.<br>
	 * The returned pointer will prevent the memory associated to this pointer from being automatically reclaimed as long as it lives, unless Pointer.release() is called on the originally-allocated pointer.
	 * @param byteOffset offset in bytes of the new pointer vs. this pointer. The expression {@code p.offset(byteOffset).getPeer() - p.getPeer() == byteOffset} is always true.
	 */
    public Pointer<T> offset(long byteOffset) {
    	return offset(byteOffset, getIO());
    }

    <U> Pointer<U> offset(long byteOffset, PointerIO<U> pio) {
		if (byteOffset == 0)
			return pio == this.io ? (Pointer<U>)this : as(pio);
		
		long newPeer = getPeer() + byteOffset;
		
		Object newSibling = getSibling() != null ? getSibling() : this;
		if (validStart == UNKNOWN_VALIDITY)
			return newPointer(pio, newPeer, isOrdered(), UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, null, NO_PARENT, null, newSibling);	
		if (newPeer > validEnd || newPeer < validStart)
			throw new IndexOutOfBoundsException("Invalid pointer offset : " + byteOffset + " (validBytes = " + getValidBytes() + ") !");
		
		return newPointer(pio, newPeer, isOrdered(), validStart, validEnd, null, NO_PARENT, null, newSibling);	
	}
	
	/**
	 * Creates a pointer that has the given number of valid bytes ahead.<br>
	 * If the pointer was already bound, the valid bytes must be lower or equal to the current getValidBytes() value.
	 */
	public Pointer<T> validBytes(long byteCount) {
		long peer = getPeer();
		long newValidEnd = peer + byteCount;
		if (validStart == peer && validEnd == newValidEnd)
			return this;
		
		if (validEnd != UNKNOWN_VALIDITY && newValidEnd > validEnd)
			throw new IndexOutOfBoundsException("Cannot extend validity of pointed memory from " + validEnd + " to " + newValidEnd);
		
		Object newSibling = getSibling() != null ? getSibling() : this;
		return newPointer(getIO(), peer, isOrdered(), validStart, newValidEnd, parent, offsetInParent, null, newSibling);    	
	}
	
	/**
	 * Creates a pointer that forgot any memory validity information.<br>
	 * Such pointers are typically faster than validity-aware pointers, since they perform less checks at each operation, but they're more prone to crashes if misused.
	 * @deprecated Pointers obtained via this method are faster but unsafe and are likely to cause crashes hard to debug if your logic is wrong.
     */
	@Deprecated
	public Pointer<T> withoutValidityInformation() {
          long peer = getPeer();
		if (validStart == UNKNOWN_VALIDITY)
			return this;
		
		Object newSibling = getSibling() != null ? getSibling() : this;
		return newPointer(getIO(), peer, isOrdered(), UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, parent, offsetInParent, null, newSibling);    	
	}
	
	/**
	* Creates a copy of the pointed memory location (allocates a new area of memory) and returns a pointer to it.<br>
	* The pointer's bounds must be known (see {@link Pointer#getValidBytes()}, {@link Pointer#validBytes(long)} or {@link Pointer#validElements(long)}).
	 */
	public Pointer<T> clone() {
		long length = getValidElements();
		if (length < 0)
			throw new UnsupportedOperationException("Number of bytes unknown, unable to clone memory (use validBytes(long))");
		
		Pointer<T> c = allocateArray(getIO(), length);
		copyTo(c);
		return c;    	
	}
	
	/**
	 * Creates a pointer that has the given number of valid elements ahead.<br>
	 * If the pointer was already bound, elementCount must be lower or equal to the current getValidElements() value.
	 */
	public Pointer<T> validElements(long elementCount) {
		return validBytes(elementCount * getIO("Cannot define elements validity").getTargetSize());
    }   
	
	/**
	 * Returns a pointer to this pointer.<br>
	 * It will only succeed if this pointer was dereferenced from another pointer.<br>
	 * Let's take the following C++ code :
	 * <pre>{@code
	int** pp = ...;
	int* p = pp[10];
	int** ref = &p;
	ASSERT(pp == ref);
	 }</pre>
	 * Here is its equivalent Java code :
	 * <pre>{@code
	Pointer<Pointer<Integer>> pp = ...;
	Pointer<Integer> p = pp.get(10);
	Pointer<Pointer<Integer>> ref = p.getReference();
	assert pp.equals(ref);
	 }</pre>
	 */
    public Pointer<Pointer<T>> getReference() {
		if (parent == null)
			throw new UnsupportedOperationException("Cannot get reference to this pointer, it wasn't created from Pointer.getPointer(offset) or from a similar method.");
		
		PointerIO io = getIO();
		return parent.offset(offsetInParent).as(io == null ? null : io.getReferenceIO());
	}
	
	/**
	 * Get the address of the memory pointed to by this pointer ("cast this pointer to long", in C jargon).<br>
	 * This is equivalent to the C code {@code (size_t)&pointer}
	 * @return Address of the memory pointed to by this pointer
	 */
	public final long getPeer() {
          if (BridJ.debugPointerReleases) {
               if (releaseTrace != null) {
                    throw new RuntimeException("Pointer was released here:\n\t" + Utils.toString(releaseTrace).replaceAll("\n", "\n\t"));
               }
          }
		return peer_;
	}
    
	/**
	 * Create a native callback which signature corresponds to the provided calling convention, return type and parameter types, and which redirects calls to the provided Java {@link org.bridj.DynamicCallback} handler.<br>
	 * For instance, a callback of C signature <code>double (*)(float, int)</code> that adds its two arguments can be created with :<br>
     * <code>{@code 
     * Pointer callback = Pointer.allocateDynamicCallback(
	 *	  new DynamicCallback<Integer>() {
	 *	      public Double apply(Object... args) {
	 *	          float a = (Float)args[0];
	 *	          int b = (Integer)args[1];
	 *	          return (double)(a + b);
	 *	      }
	 *	  }, 
	 *    null, // Use the platform's default calling convention
	 *    int.class, // return type
	 *    float.class, double.class // parameter types
	 * );
     * }</code><br>
     * For the <code>void</code> return type, you can use {@link java.lang.Void} :<br>
     * <code>{@code 
     * Pointer callback = Pointer.allocateDynamicCallback(
	 *	  new DynamicCallback<Void>() {
	 *	      public Void apply(Object... args) {
	 *	          ...
	 *	          return null; // Void cannot be instantiated anyway ;-)
	 *	      }
	 *	  }, 
	 *    null, // Use the platform's default calling convention
	 *    int.class, // return type
	 *    float.class, double.class // parameter types
	 * );
     * }</code><br>
	 * @return Pointer to a native callback that redirects calls to the provided Java callback instance, and that will be destroyed whenever the pointer is released (make sure you keep a reference to it !)
	 */
	public static <R> Pointer<DynamicFunction<R>> allocateDynamicCallback(DynamicCallback<R> callback, org.bridj.ann.Convention.Style callingConvention, Type returnType, Type... parameterTypes) {
		if (callback == null)
			throw new IllegalArgumentException("Java callback handler cannot be null !");
		if (returnType == null)
			throw new IllegalArgumentException("Callback return type cannot be null !");
		if (parameterTypes == null)
			throw new IllegalArgumentException("Invalid (null) list of parameter types !");
		try {
			MethodCallInfo mci = new MethodCallInfo(returnType, parameterTypes, false);
			Method method = DynamicCallback.class.getMethod("apply", Object[].class);
			mci.setMethod(method);
			mci.setJavaSignature("([Ljava/lang/Object;)Ljava/lang/Object;");
			mci.setCallingConvention(callingConvention);
			mci.setGenericCallback(true);
			mci.setJavaCallback(callback);
			
			//System.out.println("Java sig
			
			return CRuntime.createCToJavaCallback(mci, DynamicCallback.class);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to allocate dynamic callback for convention " + callingConvention + ", return type " + Utils.toString(returnType) + " and parameter types " + Arrays.asList(parameterTypes) + " : " + ex, ex);
		}
	}
    
    /**
     * Cast this pointer to another pointer type
     * @param newIO
     */
    public <U> Pointer<U> as(PointerIO<U> newIO) {
    	return viewAs(isOrdered(), newIO);
    }
    /**
     * Create a view of this pointer that has the byte order provided in argument, or return this if this pointer already uses the requested byte order.
     * @param order byte order (endianness) of the returned pointer
     */
    public Pointer<T> order(ByteOrder order) {
		if (order.equals(ByteOrder.nativeOrder()) == isOrdered())
			return this;
		
		return viewAs(!isOrdered(), getIO());
	}
    
	/**
     * Get the byte order (endianness) of this pointer.
     */
    public ByteOrder order() {
    		ByteOrder order = isOrdered() ? ByteOrder.nativeOrder() : ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
		return order;
    }

    <U> Pointer<U> viewAs(boolean ordered, PointerIO<U> newIO) {
    	if (newIO == io && ordered == isOrdered())
    		return (Pointer<U>)this;
    	else
    		return newPointer(newIO, getPeer(), ordered, getValidStart(), getValidEnd(), getParent(), getOffsetInParent(), null, getSibling() != null ? getSibling() : this);
    }

    /**
     * Get the PointerIO instance used by this pointer to get and set pointed values.
     */
    public final PointerIO<T> getIO() {
		return io;
	}
    
	/**
     * Whether this pointer reads data in the system's native byte order or not.
     * See {@link Pointer#order()}, {@link Pointer#order(ByteOrder)}
     */
    public abstract boolean isOrdered();
    
    final long getOffsetInParent() {
		return offsetInParent;
	}
    final Pointer<?> getParent() {
		return parent;
	}
    final Object getSibling() {
		return sibling;
	}
    
    final long getValidEnd() {
		return validEnd;
	}
    final long getValidStart() {
		return validStart;
	}

    /**
     * Cast this pointer to another pointer type<br>
     * Synonym of {@link Pointer#as(Class)}<br>
     * The following C code :<br>
     * <code>{@code 
     * T* pointerT = ...;
     * U* pointerU = (U*)pointerT;
     * }</code><br>
     * Can be translated to the following Java code :<br>
     * <code>{@code 
     * Pointer<T> pointerT = ...;
     * Pointer<U> pointerU = pointerT.as(U.class);
     * }</code><br>
     * @param <U> type of the elements pointed by the returned pointer
     * @param type type of the elements pointed by the returned pointer
     * @return pointer to type U elements at the same address as this pointer
     */
    public <U> Pointer<U> as(Type type) {
    	PointerIO<U> pio = PointerIO.getInstance(type);
    	return as(pio);
    }

    /**
     * Cast this pointer to another pointer type.<br>
     * Synonym of {@link Pointer#as(Type)}<br>
     * The following C code :<br>
     * <code>{@code 
     * T* pointerT = ...;
     * U* pointerU = (U*)pointerT;
     * }</code><br>
     * Can be translated to the following Java code :<br>
     * <code>{@code 
     * Pointer<T> pointerT = ...;
     * Pointer<U> pointerU = pointerT.as(U.class); // or pointerT.as(U.class);
     * }</code><br>
     * @param <U> type of the elements pointed by the returned pointer
     * @param type type of the elements pointed by the returned pointer
     * @return pointer to type U elements at the same address as this pointer
     */
    public <U> Pointer<U> as(Class<U> type) {
    	return as((Type)type);
    }
    
    /**
     * Cast this pointer as a function pointer to a function that returns the specified return type and takes the specified parameter types.<br>
     * See for instance the following C code that uses a function pointer :
     * <pre>{@code
     *	  double (*ptr)(int, const char*) = someAddress;
     *    double result = ptr(10, "hello");
     * }</pre>
     * Its Java equivalent with BridJ is the following :
     * <pre>{@code
     *	  DynamicFunction ptr = someAddress.asDynamicFunction(null, double.class, int.class, Pointer.class);
     *    double result = (Double)ptr.apply(10, pointerToCString("hello"));
     * }</pre>
     * Also see {@link CRuntime#getDynamicFunctionFactory(org.bridj.NativeLibrary, org.bridj.ann.Convention.Style, java.lang.reflect.Type, java.lang.reflect.Type[])  } for more options.
     * @param callingConvention calling convention used by the function (if null, default is typically {@link org.bridj.ann.Convention.Style#CDecl})
     * @param returnType return type of the function
     * @param parameterTypes parameter types of the function
     */
    public <R> DynamicFunction<R> asDynamicFunction(org.bridj.ann.Convention.Style callingConvention, Type returnType, Type... parameterTypes) {
    		return CRuntime.getInstance().getDynamicFunctionFactory(null, callingConvention, returnType, parameterTypes).newInstance((Pointer<? extends NativeObject>)this);
    }
    
    /**
     * Cast this pointer to an untyped pointer.<br>
     * Synonym of {@code ptr.as((Class<?>)null)}.<br>
     * See {@link Pointer#as(Class)}<br>
     * The following C code :<br>
     * <code>{@code 
     * T* pointerT = ...;
     * void* pointer = (void*)pointerT;
     * }</code><br>
     * Can be translated to the following Java code :<br>
     * <code>{@code 
     * Pointer<T> pointerT = ...;
     * Pointer<?> pointer = pointerT.asUntyped(); // or pointerT.as((Class<?>)null);
     * }</code><br>
     * @return untyped pointer pointing to the same address as this pointer
     */
    public Pointer<?> asUntyped() {
    	return as((Class<?>)null);
    }

    /**
     * Get the amount of memory known to be valid from this pointer, or -1 if it is unknown.<br>
     * Memory validity information is available when the pointer was allocated by BridJ (with {@link Pointer#allocateBytes(long)}, for instance), created out of another pointer which memory validity information is available (with {@link Pointer#offset(long)}, {@link Pointer#next()}, {@link Pointer#next(long)}) or created from a direct NIO buffer ({@link Pointer#pointerToBuffer(Buffer)}, {@link Pointer#pointerToInts(IntBuffer)}...)
     * @return amount of bytes that can be safely read or written from this pointer, or -1 if this amount is unknown
     */
    public long getValidBytes() {
    	long ve = getValidEnd();
    	return ve == UNKNOWN_VALIDITY ? -1 : ve - getPeer();
    }
    
    /**
    * Get the amount of memory known to be valid from this pointer (expressed in elements of the target type, see {@link Pointer#getTargetType()}) or -1 if it is unknown.<br>
     * Memory validity information is available when the pointer was allocated by BridJ (with {@link Pointer#allocateBytes(long)}, for instance), created out of another pointer which memory validity information is available (with {@link Pointer#offset(long)}, {@link Pointer#next()}, {@link Pointer#next(long)}) or created from a direct NIO buffer ({@link Pointer#pointerToBuffer(Buffer)}, {@link Pointer#pointerToInts(IntBuffer)}...)
     * @return amount of elements that can be safely read or written from this pointer, or -1 if this amount is unknown
     */
    public long getValidElements() {
    	long bytes = getValidBytes();
    	long elementSize = getTargetSize();
    	if (bytes < 0 || elementSize <= 0)
    		return -1;
    	return bytes / elementSize;
    }
    
    /**
     * Returns an iterator over the elements pointed by this pointer.<br>
     * If this pointer was allocated from Java with the allocateXXX, pointerToXXX methods (or is a view or a clone of such a pointer), the iteration is safely bounded.<br>
     * If this iterator is just a wrapper for a native-allocated pointer (or a view / clone of such a pointer), iteration will go forever (until illegal areas of memory are reached and cause a JVM crash).
     */
    public ListIterator<T> iterator() {
    	return new ListIterator<T>() {
    		Pointer<T> next = Pointer.this.getValidElements() != 0 ? Pointer.this : null;
    		Pointer<T> previous;
    		//@Override
			public T next() {
				if (next == null)
					throw new NoSuchElementException();
                T value = next.get();
                previous = next;
                long valid = next.getValidElements();
				next = valid < 0 || valid > 1 ? next.next(1) : null;
				return value;
			}
			//@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			//@Override
			public boolean hasNext() {
				long rem;
				return next != null && ((rem = next.getValidBytes()) < 0 || rem > 0);
			}
			//@Override
			public void add(T o) {
				throw new UnsupportedOperationException();
			}
			//@Override
			public boolean hasPrevious() {
				return previous != null;
			}
			//@Override
			public int nextIndex() {
				throw new UnsupportedOperationException();
			}
			//@Override
			public T previous() {
				//TODO return previous;
				throw new UnsupportedOperationException();
			}
			//@Override
			public int previousIndex() {
				throw new UnsupportedOperationException();
			}
			//@Override
			public void set(T o) {
				if (previous == null)
					throw new NoSuchElementException("You haven't called next() prior to calling ListIterator.set(E)");
				previous.set(o);
			} 
    	};
    }
    
    
    /**
     * Get a pointer to an enum. 
     */
    public static <E extends Enum<E>> Pointer<IntValuedEnum<E>> pointerToEnum(IntValuedEnum<E> instance) {
    	Class<E> enumClass;
    	if (instance instanceof FlagSet) {
    		enumClass = ((FlagSet)instance).getEnumClass();
        } else if (instance instanceof Enum) {
        	enumClass = (Class)instance.getClass();
        } else 
        	throw new RuntimeException("Expected a FlagSet or an Enum, got " + instance);

    	PointerIO<IntValuedEnum<E>> io = (PointerIO)PointerIO.getInstance(DefaultParameterizedType.paramType(IntValuedEnum.class, enumClass));
    	Pointer<IntValuedEnum<E>> p = allocate(io);
    	p.setInt((int)instance.value());
    	return p;
    }

    /**
      * @deprecated Will be removed in a future version, please use {@link Pointer#getPointer(NativeObject)} instead.
      */
    @Deprecated
    public static <N extends NativeObject> Pointer<N> pointerTo(N instance) {
         return getPointer(instance);
    }
    
    /**
     * Get a pointer to a native object (C++ or ObjectiveC class, struct, union, callback...) 
     */
    public static <N extends NativeObject> Pointer<N> getPointer(N instance) {
    		return getPointer(instance, null);
    }
    /**
     * Get a pointer to a native object (C++ or ObjectiveC class, struct, union, callback...) 
     */
    public static <N extends NativeObjectInterface> Pointer<N> getPointer(N instance) {
    		return (Pointer)getPointer((NativeObject)instance);
    }
    
    /**
     * Get a pointer to a native object, specifying the type of the pointer's target.<br>
     * In C++, the address of the pointer to an object as its canonical class is not always the same as the address of the pointer to the same object cast to one of its parent classes. 
     */
    public static <R extends NativeObject> Pointer<R> getPointer(NativeObject instance, Type targetType) {
		return instance == null ? null : (Pointer<R>)instance.peer;
    }
    /**
    * Get the address of a native object, specifying the type of the pointer's target (same as {@code getPointer(instance, targetType).getPeer()}, see {@link Pointer#getPointer(NativeObject, Type)}).<br>
     * In C++, the address of the pointer to an object as its canonical class is not always the same as the address of the pointer to the same object cast to one of its parent classes. 
     */
    public static long getAddress(NativeObject instance, Class targetType) {
		return getPeer(getPointer(instance, targetType));
    }
    
	/**
     * Read a native object value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getNativeObject(Type)} over this method. 
	 */
	public <O extends NativeObject> O getNativeObjectAtOffset(long byteOffset, Type type) {
		return (O)BridJ.createNativeObjectFromPointer((Pointer<O>)(byteOffset == 0 ? this : offset(byteOffset)), type);
	}
	/**
     * Write a native object value to the pointed memory location
     */
	public <O extends NativeObject> Pointer<T> setNativeObject(O value, Type type) {
		BridJ.copyNativeObjectToAddress(value, type, (Pointer)this);
		return this;
	}
	/**
     * Read a native object value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getNativeObject(Class)} over this method. 
	 */
	 public <O extends NativeObject> O getNativeObjectAtOffset(long byteOffset, Class<O> type) {
		return (O)getNativeObjectAtOffset(byteOffset, (Type)type);
	}
	/**
     * Read a native object value from the pointed memory location
     */
    public <O extends NativeObject> O getNativeObject(Class<O> type) {
		return (O)getNativeObject((Type)type);
	}
	/**
     * Read a native object value from the pointed memory location
     */
    public <O extends NativeObject> O getNativeObject(Type type) {
		O o = (O)getNativeObjectAtOffset(0, type);
		return o;
	}
	
	/**
	 * Check that the pointer's peer is aligned to the target type alignment.
	 * @throws RuntimeException If the target type of this pointer is unknown
	 * @return getPeer() % alignment == 0
	 */
	public boolean isAligned() {
        return isAligned(getIO("Cannot check alignment").getTargetAlignment());
	}
	
	/**
	 * Check that the pointer's peer is aligned to the given alignment.
	 * If the pointer has no peer, this method returns true.
	 * @return getPeer() % alignment == 0
	 */
	public boolean isAligned(long alignment) {
		return isAligned(getPeer(), alignment);
	}
	
	/**
	 * Check that the provided address is aligned to the given alignment.
	 * @return address % alignment == 0
	 */
	protected static boolean isAligned(long address, long alignment) {
		return computeRemainder(address, alignment) == 0;
	}
	
	protected static int computeRemainder(long address, long alignment) {
		switch ((int)alignment) {
		case -1:
		case 0:
		case 1:
			return 0;
		case 2:
			return (int)(address & 1);
		case 4:
			return (int)(address & 3);
		case 8:
			return (int)(address & 7);
		case 16:
			return (int)(address & 15);
		case 32:
			return (int)(address & 31);
		case 64:
			return (int)(address & 63);
		default:
			if (alignment < 0)
				return 0;
			return (int)(address % alignment);
		}
	}
	
	/**
	 * Dereference this pointer (*ptr).<br>
     Take the following C++ code fragment :
     <pre>{@code
     int* array = new int[10];
     for (int index = 0; index < 10; index++, array++) 
     	printf("%i\n", *array);
     }</pre>
     Here is its equivalent in Java :
     <pre>{@code
     import static org.bridj.Pointer.*;
     ...
     Pointer<Integer> array = allocateInts(10);
     for (int index = 0; index < 10; index++) { 
     	System.out.println("%i\n".format(array.get()));
     	array = array.next();
	 }
     }</pre>
     Here is a simpler equivalent in Java :
     <pre>{@code
     import static org.bridj.Pointer.*;
     ...
     Pointer<Integer> array = allocateInts(10);
     for (int value : array) // array knows its size, so we can iterate on it
     	System.out.println("%i\n".format(value));
     }</pre>
     @throws RuntimeException if called on an untyped {@code Pointer<?>} instance (see {@link  Pointer#getTargetType()}) 
	 */
    public T get() {
        return get(0);
    }
    
    /**
     * Returns null if pointer is null, otherwise dereferences the pointer (calls pointer.get()).
     */
    public static <T> T get(Pointer<T> pointer) {
    		return pointer == null ? null : pointer.get();
    }
    
    /**
     Gets the n-th element from this pointer.<br>
     This is equivalent to the C/C++ square bracket syntax.<br>
     Take the following C++ code fragment :
     <pre>{@code
	int* array = new int[10];
	int index = 5;
	int value = array[index];
     }</pre>
     Here is its equivalent in Java :
     <pre>{@code
	import static org.bridj.Pointer.*;
	...
	Pointer<Integer> array = allocateInts(10);
	int index = 5;
	int value = array.get(index);
     }</pre>
     @param index offset in pointed elements at which the value should be copied. Can be negative if the pointer was offset and the memory before it is valid.
     @throws RuntimeException if called on an untyped {@code Pointer<?>} instance ({@link  Pointer#getTargetType()}) 
	 */
	public T get(long index) {
        return getIO("Cannot get pointed value").get(this, index);
    }
    
    /**
	 Assign a value to the pointed memory location, and return it (different behaviour from {@link List#set(int, Object)} which returns the old value of that element !!!).<br>
     Take the following C++ code fragment :
     <pre>{@code
	int* array = new int[10];
	for (int index = 0; index < 10; index++, array++) { 
		int value = index;
		*array = value;
	}
     }</pre>
     Here is its equivalent in Java :
     <pre>{@code
	import static org.bridj.Pointer.*;
	...
	Pointer<Integer> array = allocateInts(10);
	for (int index = 0; index < 10; index++) {
		int value = index;
		array.set(value);
		array = array.next();
	}
     }</pre>
     @throws RuntimeException if called on a raw and untyped {@code Pointer} instance (see {@link Pointer#asUntyped()} and {@link  Pointer#getTargetType()}) 
	 @return The value that was given (not the old value as in {@link List#set(int, Object)} !!!)
	 */
    public T set(T value) {
        return set(0, value);
    }
    
    private static long getTargetSizeToAllocateArrayOrThrow(PointerIO<?> io) {
    		long targetSize = -1;
    		if (io == null || (targetSize = io.getTargetSize()) < 0)
			throwBecauseUntyped("Cannot allocate array ");
		return targetSize;
	}
    	
    private static void throwBecauseUntyped(String message) {
    	throw new RuntimeException("Pointer is not typed (call Pointer.as(Type) to create a typed pointer) : " + message);
    }
    static void throwUnexpected(Throwable ex) {
    	throw new RuntimeException("Unexpected error", ex);
    }
	/**
     Sets the n-th element from this pointer, and return it (different behaviour from {@link List#set(int, Object)} which returns the old value of that element !!!).<br>
     This is equivalent to the C/C++ square bracket assignment syntax.<br>
     Take the following C++ code fragment :
     <pre>{@code
     float* array = new float[10];
     int index = 5;
     float value = 12;
     array[index] = value;
     }</pre>
     Here is its equivalent in Java :
     <pre>{@code
     import static org.bridj.Pointer.*;
     ...
     Pointer<Float> array = allocateFloats(10);
     int index = 5;
     float value = 12;
     array.set(index, value);
     }</pre>
     @param index offset in pointed elements at which the value should be copied. Can be negative if the pointer was offset and the memory before it is valid.
     @param value value to set at pointed memory location
     @throws RuntimeException if called on a raw and untyped {@code Pointer} instance (see {@link Pointer#asUntyped()} and {@link  Pointer#getTargetType()})
     @return The value that was given (not the old value as in {@link List#set(int, Object)} !!!)
	 */
	public T set(long index, T value) {
        getIO("Cannot set pointed value").set(this, index, value);
        return value;
    }
	
    /**
     * Get a pointer's peer (see {@link Pointer#getPeer}), or zero if the pointer is null.
     */
	public static long getPeer(Pointer<?> pointer) {
        return pointer == null ? 0 : pointer.getPeer();
    }
	
    /**
     * Get the unitary size of the pointed elements in bytes.
     * @throws RuntimeException if the target type is unknown (see {@link Pointer#getTargetType()})
     */
	public long getTargetSize() {
        return getIO("Cannot compute target size").getTargetSize();
	}
	
	/**
	 * Returns a pointer to the next target.
	 * Same as incrementing a C pointer of delta elements, but creates a new pointer instance.
	 * @return next(1)
	 */
	public Pointer<T> next() {
		return next(1);
	}
	
	/**
	 * Returns a pointer to the n-th next (or previous) target.
	 * Same as incrementing a C pointer of delta elements, but creates a new pointer instance.
	 * @return offset(getTargetSize() * delta)
	 */
	public Pointer<T> next(long delta) {
        return offset(getIO("Cannot get pointers to next or previous targets").getTargetSize() * delta);
	}
	
	/**
     * Release pointers, if they're not null (see {@link Pointer#release}).
     */
	public static void release(Pointer... pointers) {
    		for (Pointer pointer : pointers)
    			if (pointer != null)
    				pointer.release();
	}

    /**
	 * Test equality of the pointer using the address.<br>
	 * @return true if and only if obj is a Pointer instance and {@code obj.getPeer() == this.getPeer() }
	 */
	@Override
    public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pointer))
			return false;
		
		Pointer p = (Pointer)obj;
		return getPeer() == p.getPeer();
	}
  
	/**
     * Create a pointer out of a native memory address
     * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == address }
     */
    @Deprecated
    public static Pointer<?> pointerToAddress(long peer) {
    	return pointerToAddress(peer, (PointerIO) null);
    }

    /**
     * Create a pointer out of a native memory address
     * @param size number of bytes known to be readable at the pointed address 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    @Deprecated
    public static Pointer<?> pointerToAddress(long peer, long size) {
        return newPointer(null, peer, true, peer, peer + size, null, NO_PARENT, null, null);
    }
    
    /**
     * Create a pointer out of a native memory address
     * @param targetClass type of the elements pointed by the resulting pointer 
	 * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    public static <P> Pointer<P> pointerToAddress(long peer, Class<P> targetClass, final Releaser releaser) {
        return pointerToAddress(peer, (Type)targetClass, releaser);
    }
    /**
     * Create a pointer out of a native memory address
     * @param targetType type of the elements pointed by the resulting pointer 
	 * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    public static <P> Pointer<P> pointerToAddress(long peer, Type targetType, final Releaser releaser) {
    		PointerIO<P> pio = PointerIO.getInstance(targetType);
        return newPointer(pio, peer, true, UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, null, -1, releaser, null);
    }
    /**
     * Create a pointer out of a native memory address
     * @param io PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    public static <P> Pointer<P> pointerToAddress(long peer, PointerIO<P> io) {
    	if (BridJ.cachePointers)
    		return (Pointer<P>)localCachedPointers.get().get(peer, io);
    	else
    		return pointerToAddress_(peer, io);
	}

	private static <P> Pointer<P> pointerToAddress_(long peer, PointerIO<P> io) {
    	return newPointer(io, peer, true, UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, null, NO_PARENT, null, null);
	}

	private static final int LRU_POINTER_CACHE_SIZE = 8;
  private static final int LRU_POINTER_CACHE_TOLERANCE = 1;
  private static final ThreadLocal<PointerLRUCache> localCachedPointers = new ThreadLocal<PointerLRUCache>() {
      @Override
      protected PointerLRUCache initialValue() {
          return new PointerLRUCache(LRU_POINTER_CACHE_SIZE, LRU_POINTER_CACHE_TOLERANCE) {
          	@Override
          	protected <P> Pointer<P> pointerToAddress(long peer, PointerIO<P> io) {
          		return pointerToAddress_(peer, io);
          	}
          };
      }
  };

	/**
     * Create a pointer out of a native memory address
     * @param io PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    static <P> Pointer<P> pointerToAddress(long peer, PointerIO<P> io, Releaser releaser) {
    	return newPointer(io, peer, true, UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, null, NO_PARENT, releaser, null);
	}
	
	/**
     * Create a pointer out of a native memory address
     * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    @Deprecated
    public static Pointer<?> pointerToAddress(long peer, Releaser releaser) {
		return newPointer(null, peer, true, UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, null, NO_PARENT, releaser, null);
	}
    
	/**
     * Create a pointer out of a native memory address
     * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param size number of bytes known to be readable at the pointed address 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    public static Pointer<?> pointerToAddress(long peer, long size, Releaser releaser) {
        return newPointer(null, peer, true, peer, peer + size, null, NO_PARENT, releaser, null);
    }
    
	/**
     * Create a pointer out of a native memory address
     * @param io PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param size number of bytes known to be readable at the pointed address 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    public static <P> Pointer<P> pointerToAddress(long peer, long size, PointerIO<P> io, Releaser releaser) {
        return newPointer(io, peer, true, peer, peer + size, null, NO_PARENT, releaser, null);
    }
	
	/**
     * Create a pointer out of a native memory address
     * @param targetClass type of the elements pointed by the resulting pointer 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    @Deprecated
    public static <P> Pointer<P> pointerToAddress(long peer, Class<P> targetClass) {
    		return pointerToAddress(peer, (Type)targetClass);
    }
    
	/**
     * Create a pointer out of a native memory address
     * @param targetType type of the elements pointed by the resulting pointer 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    @Deprecated
    public static <P> Pointer<P> pointerToAddress(long peer, Type targetType) {
    	return newPointer((PointerIO<P>)PointerIO.getInstance(targetType), peer, true, UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, null, -1, null, null);
    }
    
	/**
     * Create a pointer out of a native memory address
     * @param size number of bytes known to be readable at the pointed address 
	 * @param io PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    static <U> Pointer<U> pointerToAddress(long peer, long size, PointerIO<U> io) {
    	return newPointer(io, peer, true, peer, peer + size, null, NO_PARENT, null, null);
	}
	
	/**
     * Create a pointer out of a native memory address
     * @param releaser object responsible for reclaiming the native memory once whenever the returned pointer is garbage-collected 
	 * @param peer native memory address that is to be converted to a pointer
	 * @return a pointer with the provided address : {@code pointer.getPeer() == peer }
     */
    static <U> Pointer<U> newPointer(
		PointerIO<U> io, 
		long peer, 
		boolean ordered, 
		long validStart, 
		long validEnd, 
		Pointer<?> parent, 
		long offsetInParent, 
		final Releaser releaser,
		Object sibling)
	{
    peer = peer & POINTER_MASK;
		if (peer == 0)
			return null;
		
		if (validEnd != UNKNOWN_VALIDITY && validEnd <= validStart)
			return null;
		
		if (releaser == null) {
			if (ordered) {
				return new OrderedPointer<U>(io, peer, validStart, validEnd, parent, offsetInParent, sibling);
			} else {
				return new DisorderedPointer<U>(io, peer, validStart, validEnd, parent, offsetInParent, sibling);
			}
		} else {
			assert sibling == null;
			if (ordered) {
				return new OrderedPointer<U>(io, peer, validStart, validEnd, parent, offsetInParent, sibling) {
									private volatile Releaser rel = releaser;
				//@Override
				public synchronized void release() {
					if (rel != null) {
						Releaser rel = this.rel;
						this.rel = null;
						rel.release(this);
					}
                         //this.peer_ = 0;
                         if (BridJ.debugPointerReleases)
                              releaseTrace = new RuntimeException().fillInStackTrace();
				}
				protected void finalize() {
					release();
				}
				
				@Deprecated
				public synchronized Pointer<U> withReleaser(final Releaser beforeDeallocation) {
					final Releaser thisReleaser = rel;
					rel = null;
					return newPointer(getIO(), getPeer(), isOrdered(), getValidStart(), getValidEnd(), null, NO_PARENT, beforeDeallocation == null ? thisReleaser : new Releaser() {
						//@Override
						public void release(Pointer<?> p) {
							beforeDeallocation.release(p);
							if (thisReleaser != null)
								thisReleaser.release(p);
						}
					}, null);
				}
				};
			} else {
				return new DisorderedPointer<U>(io, peer, validStart, validEnd, parent, offsetInParent, sibling) {
									private volatile Releaser rel = releaser;
				//@Override
				public synchronized void release() {
					if (rel != null) {
						Releaser rel = this.rel;
						this.rel = null;
						rel.release(this);
					}
                         //this.peer_ = 0;
                         if (BridJ.debugPointerReleases)
                              releaseTrace = new RuntimeException().fillInStackTrace();
				}
				protected void finalize() {
					release();
				}
				
				@Deprecated
				public synchronized Pointer<U> withReleaser(final Releaser beforeDeallocation) {
					final Releaser thisReleaser = rel;
					rel = null;
					return newPointer(getIO(), getPeer(), isOrdered(), getValidStart(), getValidEnd(), null, NO_PARENT, beforeDeallocation == null ? thisReleaser : new Releaser() {
						//@Override
						public void release(Pointer<?> p) {
							beforeDeallocation.release(p);
							if (thisReleaser != null)
								thisReleaser.release(p);
						}
					}, null);
				}
				};
			}
		}
    }
	
	/**
     * Allocate enough memory for a typed pointer value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized typed pointer value
     */
    public static <P extends TypedPointer> Pointer<P> allocateTypedPointer(Class<P> type) {
    	return (Pointer<P>)(Pointer)allocate(PointerIO.getInstance(type));
    }
	/**
     * Allocate enough memory for arrayLength typed pointer values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<P extends TypedPointer>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized typed pointer consecutive values
     */
    public static <P extends TypedPointer> Pointer<P> allocateTypedPointers(Class<P> type, long arrayLength) {
    	return (Pointer<P>)(Pointer)allocateArray(PointerIO.getInstance(type), arrayLength);
    }
    /**
     * Create a memory area large enough to hold a pointer.
     * @param targetType target type of the pointer values to be stored in the allocated memory 
     * @return a pointer to a new memory area large enough to hold a single typed pointer
     */
    public static <P> Pointer<Pointer<P>> allocatePointer(Class<P> targetType) {
    	return allocatePointer((Type)targetType); 
    }
    /**
     * Create a memory area large enough to hold a pointer.
     * @param targetType target type of the pointer values to be stored in the allocated memory 
     * @return a pointer to a new memory area large enough to hold a single typed pointer
     */
    public static <P> Pointer<Pointer<P>> allocatePointer(Type targetType) {
    	return (Pointer<Pointer<P>>)(Pointer)allocate(PointerIO.getPointerInstance(targetType)); 
    }
    /**
     * Create a memory area large enough to hold a pointer to a pointer
     * @param targetType target type of the values pointed by the pointer values to be stored in the allocated memory 
     * @return a pointer to a new memory area large enough to hold a single typed pointer
     */
    public static <P> Pointer<Pointer<Pointer<P>>> allocatePointerPointer(Type targetType) {
    	return allocatePointer(pointerType(targetType)); 
    }/**
     * Create a memory area large enough to hold a pointer to a pointer
     * @param targetType target type of the values pointed by the pointer values to be stored in the allocated memory 
     * @return a pointer to a new memory area large enough to hold a single typed pointer
     */
    public static <P> Pointer<Pointer<Pointer<P>>> allocatePointerPointer(Class<P> targetType) {
    	return allocatePointerPointer((Type)targetType); 
    }
	/**
     * Allocate enough memory for a untyped pointer value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized untyped pointer value
     */
    /**
     * Create a memory area large enough to hold an untyped pointer.
     * @return a pointer to a new memory area large enough to hold a single untyped pointer
     */
    public static <V> Pointer<Pointer<?>> allocatePointer() {
    	return (Pointer)allocate(PointerIO.getPointerInstance());
    }
	/**
     * Allocate enough memory for arrayLength untyped pointer values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Pointer<?>>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized untyped pointer consecutive values
     */
    public static Pointer<Pointer<?>> allocatePointers(int arrayLength) {
		return (Pointer<Pointer<?>>)(Pointer)allocateArray(PointerIO.getPointerInstance(), arrayLength); 
	}
	
    /**
     * Create a memory area large enough to hold an array of arrayLength typed pointers.
     * @param targetType target type of element pointers in the resulting pointer array. 
     * @param arrayLength size of the allocated array, in elements
     * @return a pointer to a new memory area large enough to hold an array of arrayLength typed pointers
     */
    public static <P> Pointer<Pointer<P>> allocatePointers(Class<P> targetType, int arrayLength) {
		return allocatePointers((Type)targetType, arrayLength);
	}
	
    /**
     * Create a memory area large enough to hold an array of arrayLength typed pointers.
     * @param targetType target type of element pointers in the resulting pointer array. 
     * @param arrayLength size of the allocated array, in elements
     * @return a pointer to a new memory area large enough to hold an array of arrayLength typed pointers
     */
    public static <P> Pointer<Pointer<P>> allocatePointers(Type targetType, int arrayLength) {
		return (Pointer<Pointer<P>>)(Pointer)allocateArray(PointerIO.getPointerInstance(targetType), arrayLength); // TODO 
	}
	
    
    /**
     * Create a memory area large enough to a single items of type elementClass.
     * @param elementClass type of the array elements
     * @return a pointer to a new memory area large enough to hold a single item of type elementClass.
     */
    public static <V> Pointer<V> allocate(Class<V> elementClass) {
        return allocate((Type)elementClass);
    }

    /**
     * Create a memory area large enough to a single items of type elementClass.
     * @param elementClass type of the array elements
     * @return a pointer to a new memory area large enough to hold a single item of type elementClass.
     */
    public static <V> Pointer<V> allocate(Type elementClass) {
        return allocateArray(elementClass, 1);
    }

    /**
     * Create a memory area large enough to hold one item of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     * @param io PointerIO instance able to store and retrieve the element
     * @return a pointer to a new memory area large enough to hold one item of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     */
    public static <V> Pointer<V> allocate(PointerIO<V> io) {
    		return allocateBytes(io, getTargetSizeToAllocateArrayOrThrow(io), null);
    }
    /**
     * Create a memory area large enough to hold arrayLength items of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     * @param io PointerIO instance able to store and retrieve elements of the array
     * @param arrayLength length of the array in elements
     * @return a pointer to a new memory area large enough to hold arrayLength items of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     */
    public static <V> Pointer<V> allocateArray(PointerIO<V> io, long arrayLength) {
		return allocateBytes(io, getTargetSizeToAllocateArrayOrThrow(io) * arrayLength, null);
    }
    /**
     * Create a memory area large enough to hold arrayLength items of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     * @param io PointerIO instance able to store and retrieve elements of the array
     * @param arrayLength length of the array in elements
     * @param beforeDeallocation fake releaser that should be run just before the memory is actually released, for instance in order to call some object destructor
     * @return a pointer to a new memory area large enough to hold arrayLength items of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     */
    public static <V> Pointer<V> allocateArray(PointerIO<V> io, long arrayLength, final Releaser beforeDeallocation) {
		return allocateBytes(io, getTargetSizeToAllocateArrayOrThrow(io) * arrayLength, beforeDeallocation);
    }
    /**
     * Create a memory area large enough to hold byteSize consecutive bytes and return a pointer to elements of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()})
     * @param io PointerIO instance able to store and retrieve elements of the array
     * @param byteSize length of the array in bytes
     * @param beforeDeallocation fake releaser that should be run just before the memory is actually released, for instance in order to call some object destructor
     * @return a pointer to a new memory area large enough to hold byteSize consecutive bytes
     */
    public static <V> Pointer<V> allocateBytes(PointerIO<V> io, long byteSize, final Releaser beforeDeallocation) {
    		return allocateAlignedBytes(io, byteSize, defaultAlignment, beforeDeallocation);
    }
    	
    /**
     * Create a memory area large enough to hold byteSize consecutive bytes and return a pointer to elements of the type associated to the provided PointerIO instance (see {@link PointerIO#getTargetType()}), ensuring the pointer to the memory is aligned to the provided boundary.
     * @param io PointerIO instance able to store and retrieve elements of the array
     * @param byteSize length of the array in bytes
     * @param alignment boundary to which the returned pointer should be aligned
     * @param beforeDeallocation fake releaser that should be run just before the memory is actually released, for instance in order to call some object destructor
     * @return a pointer to a new memory area large enough to hold byteSize consecutive bytes
     */
    public static <V> Pointer<V> allocateAlignedBytes(PointerIO<V> io, long byteSize, int alignment, final Releaser beforeDeallocation) {
        if (byteSize == 0)
        	return null;
        if (byteSize < 0)
        	throw new IllegalArgumentException("Cannot allocate a negative amount of memory !");
        
        long address, offset = 0;
        if (alignment <= 1)
        		address = JNI.mallocNulled(byteSize);
        	else {
        		//address = JNI.mallocNulledAligned(byteSize, alignment);
        		//if (address == 0) 
        		{
        			// invalid alignment (< sizeof(void*) or not a power of 2
        			address = JNI.mallocNulled(byteSize + alignment - 1);
				long remainder = address % alignment;
				if (remainder > 0)
					offset = alignment - remainder;
        		}
        	}
        	
        if (address == 0)
        	throw new RuntimeException("Failed to allocate " + byteSize);

		Pointer<V> ptr = newPointer(io, address, true, address, address + byteSize + offset, null, NO_PARENT, beforeDeallocation == null ? freeReleaser : new Releaser() {
        	//@Override
        	public void release(Pointer<?> p) {
        		beforeDeallocation.release(p);
        		freeReleaser.release(p);
        	}
        }, null);
        
        if (offset > 0)
        		ptr = ptr.offset(offset);
        
        return ptr;
    }
    
    /**
     * Create a pointer that depends on this pointer and will call a releaser prior to release this pointer, when it is GC'd.<br>
     * This pointer MUST NOT be used anymore.
     * @deprecated This method can easily be misused and is reserved to advanced users.
     * @param beforeDeallocation releaser that should be run before this pointer's releaser (if any).
     * @return a new pointer to the same memory location as this pointer
     */
    @Deprecated
    public synchronized Pointer<T> withReleaser(final Releaser beforeDeallocation) {
    		return newPointer(getIO(), getPeer(), isOrdered(), getValidStart(), getValidEnd(), null, NO_PARENT, beforeDeallocation, null);
    }
    static Releaser freeReleaser = new FreeReleaser();
    static class FreeReleaser implements Releaser {
    	//@Override
		public void release(Pointer<?> p) {
			assert p.getSibling() == null;
			assert p.validStart == p.getPeer();
			
               if (BridJ.debugPointers) {
                    p.deletionTrace = new RuntimeException().fillInStackTrace();
               	BridJ.info("Freeing pointer " + p +
                         " (peer = " + p.getPeer() +
                         ", validStart = " + p.validStart +
                         ", validEnd = " + p.validEnd + 
                         ", validBytes = " + p.getValidBytes() + 
                         ").\nCreation trace:\n\t" + Utils.toString(p.creationTrace).replaceAll("\n", "\n\t") +
                         "\nDeletion trace:\n\t" + Utils.toString(p.deletionTrace).replaceAll("\n", "\n\t"));
               }
          	if (!BridJ.debugNeverFree)
          		JNI.free(p.getPeer());
          }
    }
    
    /**
     * Create a memory area large enough to hold arrayLength items of type elementClass.
     * @param elementClass type of the array elements
     * @param arrayLength length of the array in elements
     * @return a pointer to a new memory area large enough to hold arrayLength items of type elementClass.  
     */
    public static <V> Pointer<V> allocateArray(Class<V> elementClass, long arrayLength) {
        return allocateArray((Type)elementClass, arrayLength);
    }
    /**
     * Create a memory area large enough to hold arrayLength items of type elementClass.
     * @param elementClass type of the array elements
     * @param arrayLength length of the array in elements
     * @return a pointer to a new memory area large enough to hold arrayLength items of type elementClass.
     */
    public static <V> Pointer<V> allocateArray(Type elementClass, long arrayLength) {
		if (arrayLength == 0)
			return null;
		
		PointerIO pio = PointerIO.getInstance(elementClass);
		if (pio == null)
			throw new UnsupportedOperationException("Cannot allocate memory for type " + (elementClass instanceof Class ? ((Class)elementClass).getName() : elementClass.toString()));
		return (Pointer<V>)allocateArray(pio, arrayLength);
    }
    
    
    /**
     * Create a memory area large enough to hold arrayLength items of type elementClass, ensuring the pointer to the memory is aligned to the provided boundary.
     * @param elementClass type of the array elements
     * @param arrayLength length of the array in elements
     * @param alignment boundary to which the returned pointer should be aligned
     * @return a pointer to a new memory area large enough to hold arrayLength items of type elementClass.  
     */
    public static <V> Pointer<V> allocateAlignedArray(Class<V> elementClass, long arrayLength, int alignment) {
        return allocateAlignedArray((Type)elementClass, arrayLength, alignment);
    }
    
    /**
     * Create a memory area large enough to hold arrayLength items of type elementClass, ensuring the pointer to the memory is aligned to the provided boundary.
     * @param elementClass type of the array elements
     * @param arrayLength length of the array in elements
     * @param alignment boundary to which the returned pointer should be aligned
     * @return a pointer to a new memory area large enough to hold arrayLength items of type elementClass.
     */
    public static <V> Pointer<V> allocateAlignedArray(Type elementClass, long arrayLength, int alignment) {
		PointerIO io = PointerIO.getInstance(elementClass);
		if (io == null)
			throw new UnsupportedOperationException("Cannot allocate memory for type " + (elementClass instanceof Class ? ((Class)elementClass).getName() : elementClass.toString()));
		return allocateAlignedBytes(io, getTargetSizeToAllocateArrayOrThrow(io) * arrayLength, alignment, null);
    }

    /**
     * Create a pointer to the memory location used by a direct NIO buffer.<br>
     * If the NIO buffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToArray(Object)}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.
     */
    public static Pointer<?> pointerToBuffer(Buffer buffer) {
        if (buffer == null)
			return null;
		
				if (buffer instanceof IntBuffer)
			return (Pointer)pointerToInts((IntBuffer)buffer);
				if (buffer instanceof LongBuffer)
			return (Pointer)pointerToLongs((LongBuffer)buffer);
				if (buffer instanceof ShortBuffer)
			return (Pointer)pointerToShorts((ShortBuffer)buffer);
				if (buffer instanceof ByteBuffer)
			return (Pointer)pointerToBytes((ByteBuffer)buffer);
				if (buffer instanceof CharBuffer)
			return (Pointer)pointerToChars((CharBuffer)buffer);
				if (buffer instanceof FloatBuffer)
			return (Pointer)pointerToFloats((FloatBuffer)buffer);
				if (buffer instanceof DoubleBuffer)
			return (Pointer)pointerToDoubles((DoubleBuffer)buffer);
		        throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
	}
	
	/**
	 * When a pointer was created with {@link Pointer#pointerToBuffer(Buffer)} on a non-direct buffer, a native copy of the buffer data was made.
	 * This method updates the original buffer with the native memory, and does nothing if the buffer is direct <b>and</b> points to the same memory location as this pointer.<br>
	 * @throws IllegalArgumentException if buffer is direct and does not point to the exact same location as this Pointer instance
     */
    public void updateBuffer(Buffer buffer) {
        if (buffer == null)
			throw new IllegalArgumentException("Cannot update a null Buffer !");
		
		if (Utils.isDirect(buffer)) {
			long address = JNI.getDirectBufferAddress(buffer);
			if (address != getPeer()) {
				throw new IllegalArgumentException("Direct buffer does not point to the same location as this Pointer instance, updating it makes no sense !");
			}
		} else {
									if (buffer instanceof IntBuffer) {
				((IntBuffer)buffer).duplicate().put(getIntBuffer());
				return;
			}
												if (buffer instanceof LongBuffer) {
				((LongBuffer)buffer).duplicate().put(getLongBuffer());
				return;
			}
												if (buffer instanceof ShortBuffer) {
				((ShortBuffer)buffer).duplicate().put(getShortBuffer());
				return;
			}
												if (buffer instanceof ByteBuffer) {
				((ByteBuffer)buffer).duplicate().put(getByteBuffer());
				return;
			}
																		if (buffer instanceof FloatBuffer) {
				((FloatBuffer)buffer).duplicate().put(getFloatBuffer());
				return;
			}
												if (buffer instanceof DoubleBuffer) {
				((DoubleBuffer)buffer).duplicate().put(getDoubleBuffer());
				return;
			}
									throw new UnsupportedOperationException("Unhandled buffer type : " + buffer.getClass().getName());
		}
	}

 
//-- primitive: int --
	
    	/**
     * Allocate enough memory for a single int value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the int value given in argument
     */
    public static Pointer<Integer> pointerToInt(int value) {
        Pointer<Integer> mem = allocate(PointerIO.getIntInstance());
        mem.setInt(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length int values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Integer>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the int consecutive values provided in argument
     */
    public static Pointer<Integer> pointerToInts(int... values) {
        if (values == null)
			return null;
		Pointer<Integer> mem = allocateArray(PointerIO.getIntInstance(), values.length);
        mem.setIntsAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D int array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the int values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Integer>> pointerToInts(int[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Integer>> mem = allocateInts(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setIntsAtOffset(i1 * dim2 * 4, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D int array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the int values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Integer>>> pointerToInts(int[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Integer>>> mem = allocateInts(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setIntsAtOffset(offset2 * 4, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a int value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized int value
     */
    public static Pointer<Integer> allocateInt() {
        return allocate(PointerIO.getIntInstance());
    }
    	/**
     * Allocate enough memory for arrayLength int values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Integer>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized int consecutive values
     */
    public static Pointer<Integer> allocateInts(long arrayLength) {
        return allocateArray(PointerIO.getIntInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 int values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized int consecutive values
     */
    public static Pointer<Pointer<Integer>> allocateInts(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getIntInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 int values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized int consecutive values
     */
    public static Pointer<Pointer<Pointer<Integer>>> allocateInts(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getIntInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getIntInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: long --
	
    	/**
     * Allocate enough memory for a single long value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the long value given in argument
     */
    public static Pointer<Long> pointerToLong(long value) {
        Pointer<Long> mem = allocate(PointerIO.getLongInstance());
        mem.setLong(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length long values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Long>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the long consecutive values provided in argument
     */
    public static Pointer<Long> pointerToLongs(long... values) {
        if (values == null)
			return null;
		Pointer<Long> mem = allocateArray(PointerIO.getLongInstance(), values.length);
        mem.setLongsAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D long array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the long values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Long>> pointerToLongs(long[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Long>> mem = allocateLongs(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setLongsAtOffset(i1 * dim2 * 8, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D long array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the long values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Long>>> pointerToLongs(long[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Long>>> mem = allocateLongs(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setLongsAtOffset(offset2 * 8, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a long value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized long value
     */
    public static Pointer<Long> allocateLong() {
        return allocate(PointerIO.getLongInstance());
    }
    	/**
     * Allocate enough memory for arrayLength long values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Long>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized long consecutive values
     */
    public static Pointer<Long> allocateLongs(long arrayLength) {
        return allocateArray(PointerIO.getLongInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 long values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized long consecutive values
     */
    public static Pointer<Pointer<Long>> allocateLongs(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getLongInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 long values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized long consecutive values
     */
    public static Pointer<Pointer<Pointer<Long>>> allocateLongs(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getLongInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getLongInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: short --
	
    	/**
     * Allocate enough memory for a single short value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the short value given in argument
     */
    public static Pointer<Short> pointerToShort(short value) {
        Pointer<Short> mem = allocate(PointerIO.getShortInstance());
        mem.setShort(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length short values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Short>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the short consecutive values provided in argument
     */
    public static Pointer<Short> pointerToShorts(short... values) {
        if (values == null)
			return null;
		Pointer<Short> mem = allocateArray(PointerIO.getShortInstance(), values.length);
        mem.setShortsAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D short array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the short values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Short>> pointerToShorts(short[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Short>> mem = allocateShorts(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setShortsAtOffset(i1 * dim2 * 2, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D short array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the short values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Short>>> pointerToShorts(short[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Short>>> mem = allocateShorts(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setShortsAtOffset(offset2 * 2, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a short value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized short value
     */
    public static Pointer<Short> allocateShort() {
        return allocate(PointerIO.getShortInstance());
    }
    	/**
     * Allocate enough memory for arrayLength short values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Short>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized short consecutive values
     */
    public static Pointer<Short> allocateShorts(long arrayLength) {
        return allocateArray(PointerIO.getShortInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 short values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized short consecutive values
     */
    public static Pointer<Pointer<Short>> allocateShorts(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getShortInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 short values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized short consecutive values
     */
    public static Pointer<Pointer<Pointer<Short>>> allocateShorts(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getShortInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getShortInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: byte --
	
    	/**
     * Allocate enough memory for a single byte value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the byte value given in argument
     */
    public static Pointer<Byte> pointerToByte(byte value) {
        Pointer<Byte> mem = allocate(PointerIO.getByteInstance());
        mem.setByte(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length byte values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Byte>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the byte consecutive values provided in argument
     */
    public static Pointer<Byte> pointerToBytes(byte... values) {
        if (values == null)
			return null;
		Pointer<Byte> mem = allocateArray(PointerIO.getByteInstance(), values.length);
        mem.setBytesAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D byte array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the byte values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Byte>> pointerToBytes(byte[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Byte>> mem = allocateBytes(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setBytesAtOffset(i1 * dim2 * 1, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D byte array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the byte values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Byte>>> pointerToBytes(byte[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Byte>>> mem = allocateBytes(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setBytesAtOffset(offset2 * 1, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a byte value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized byte value
     */
    public static Pointer<Byte> allocateByte() {
        return allocate(PointerIO.getByteInstance());
    }
    	/**
     * Allocate enough memory for arrayLength byte values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Byte>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized byte consecutive values
     */
    public static Pointer<Byte> allocateBytes(long arrayLength) {
        return allocateArray(PointerIO.getByteInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 byte values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized byte consecutive values
     */
    public static Pointer<Pointer<Byte>> allocateBytes(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getByteInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 byte values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized byte consecutive values
     */
    public static Pointer<Pointer<Pointer<Byte>>> allocateBytes(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getByteInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getByteInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: char --
	
    	/**
     * Allocate enough memory for a single char value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the char value given in argument
     */
    public static Pointer<Character> pointerToChar(char value) {
        Pointer<Character> mem = allocate(PointerIO.getCharInstance());
        mem.setChar(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length char values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Character>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the char consecutive values provided in argument
     */
    public static Pointer<Character> pointerToChars(char... values) {
        if (values == null)
			return null;
		Pointer<Character> mem = allocateArray(PointerIO.getCharInstance(), values.length);
        mem.setCharsAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D char array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the char values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Character>> pointerToChars(char[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Character>> mem = allocateChars(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setCharsAtOffset(i1 * dim2 * Platform.WCHAR_T_SIZE, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D char array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the char values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Character>>> pointerToChars(char[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Character>>> mem = allocateChars(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setCharsAtOffset(offset2 * Platform.WCHAR_T_SIZE, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a char value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized char value
     */
    public static Pointer<Character> allocateChar() {
        return allocate(PointerIO.getCharInstance());
    }
    	/**
     * Allocate enough memory for arrayLength char values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Character>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized char consecutive values
     */
    public static Pointer<Character> allocateChars(long arrayLength) {
        return allocateArray(PointerIO.getCharInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 char values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized char consecutive values
     */
    public static Pointer<Pointer<Character>> allocateChars(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getCharInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 char values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized char consecutive values
     */
    public static Pointer<Pointer<Pointer<Character>>> allocateChars(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getCharInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getCharInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: float --
	
    	/**
     * Allocate enough memory for a single float value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the float value given in argument
     */
    public static Pointer<Float> pointerToFloat(float value) {
        Pointer<Float> mem = allocate(PointerIO.getFloatInstance());
        mem.setFloat(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length float values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Float>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the float consecutive values provided in argument
     */
    public static Pointer<Float> pointerToFloats(float... values) {
        if (values == null)
			return null;
		Pointer<Float> mem = allocateArray(PointerIO.getFloatInstance(), values.length);
        mem.setFloatsAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D float array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the float values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Float>> pointerToFloats(float[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Float>> mem = allocateFloats(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setFloatsAtOffset(i1 * dim2 * 4, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D float array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the float values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Float>>> pointerToFloats(float[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Float>>> mem = allocateFloats(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setFloatsAtOffset(offset2 * 4, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a float value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized float value
     */
    public static Pointer<Float> allocateFloat() {
        return allocate(PointerIO.getFloatInstance());
    }
    	/**
     * Allocate enough memory for arrayLength float values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Float>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized float consecutive values
     */
    public static Pointer<Float> allocateFloats(long arrayLength) {
        return allocateArray(PointerIO.getFloatInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 float values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized float consecutive values
     */
    public static Pointer<Pointer<Float>> allocateFloats(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getFloatInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 float values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized float consecutive values
     */
    public static Pointer<Pointer<Pointer<Float>>> allocateFloats(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getFloatInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getFloatInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: double --
	
    	/**
     * Allocate enough memory for a single double value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the double value given in argument
     */
    public static Pointer<Double> pointerToDouble(double value) {
        Pointer<Double> mem = allocate(PointerIO.getDoubleInstance());
        mem.setDouble(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length double values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Double>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the double consecutive values provided in argument
     */
    public static Pointer<Double> pointerToDoubles(double... values) {
        if (values == null)
			return null;
		Pointer<Double> mem = allocateArray(PointerIO.getDoubleInstance(), values.length);
        mem.setDoublesAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D double array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the double values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Double>> pointerToDoubles(double[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Double>> mem = allocateDoubles(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setDoublesAtOffset(i1 * dim2 * 8, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D double array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the double values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Double>>> pointerToDoubles(double[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Double>>> mem = allocateDoubles(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setDoublesAtOffset(offset2 * 8, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a double value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized double value
     */
    public static Pointer<Double> allocateDouble() {
        return allocate(PointerIO.getDoubleInstance());
    }
    	/**
     * Allocate enough memory for arrayLength double values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Double>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized double consecutive values
     */
    public static Pointer<Double> allocateDoubles(long arrayLength) {
        return allocateArray(PointerIO.getDoubleInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 double values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized double consecutive values
     */
    public static Pointer<Pointer<Double>> allocateDoubles(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getDoubleInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 double values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized double consecutive values
     */
    public static Pointer<Pointer<Pointer<Double>>> allocateDoubles(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getDoubleInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getDoubleInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 
//-- primitive: boolean --
	
    	/**
     * Allocate enough memory for a single boolean value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the boolean value given in argument
     */
    public static Pointer<Boolean> pointerToBoolean(boolean value) {
        Pointer<Boolean> mem = allocate(PointerIO.getBooleanInstance());
        mem.setBoolean(value);
        return mem;
    }
	
	/**
     * Allocate enough memory for values.length boolean values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Boolean>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the boolean consecutive values provided in argument
     */
    public static Pointer<Boolean> pointerToBooleans(boolean... values) {
        if (values == null)
			return null;
		Pointer<Boolean> mem = allocateArray(PointerIO.getBooleanInstance(), values.length);
        mem.setBooleansAtOffset(0, values, 0, values.length);
        return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 2D boolean array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the boolean values provided in argument packed as a 2D C array would be
     */
    public static Pointer<Pointer<Boolean>> pointerToBooleans(boolean[][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length;
		Pointer<Pointer<Boolean>> mem = allocateBooleans(dim1, dim2);
		for (int i1 = 0; i1 < dim1; i1++)
        	mem.setBooleansAtOffset(i1 * dim2 * 1, values[i1], 0, dim2);
		return mem;
    }
    
        /**
     * Allocate enough memory for all the values in the 3D boolean array, copy the values provided as argument into it as packed multi-dimensional C array and return a pointer to that memory.<br>
     * Assumes that all of the subarrays of the provided array are non null and have the same size.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the boolean values provided in argument packed as a 3D C array would be
     */
    public static Pointer<Pointer<Pointer<Boolean>>> pointerToBooleans(boolean[][][] values) {
        if (values == null)
			return null;
		int dim1 = values.length, dim2 = values[0].length, dim3 = values[0][0].length;
		Pointer<Pointer<Pointer<Boolean>>> mem = allocateBooleans(dim1, dim2, dim3);
		for (int i1 = 0; i1 < dim1; i1++) {
        	int offset1 = i1 * dim2;
        	for (int i2 = 0; i2 < dim2; i2++) {
        		int offset2 = (offset1 + i2) * dim3;
				mem.setBooleansAtOffset(offset2 * 1, values[i1][i2], 0, dim3);
			}
		}
		return mem;
    }
	
    	/**
     * Allocate enough memory for a boolean value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized boolean value
     */
    public static Pointer<Boolean> allocateBoolean() {
        return allocate(PointerIO.getBooleanInstance());
    }
    	/**
     * Allocate enough memory for arrayLength boolean values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Boolean>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized boolean consecutive values
     */
    public static Pointer<Boolean> allocateBooleans(long arrayLength) {
        return allocateArray(PointerIO.getBooleanInstance(), arrayLength);
    }
    
    	/**
     * Allocate enough memory for dim1 * dim2 boolean values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 zero-initialized boolean consecutive values
     */
    public static Pointer<Pointer<Boolean>> allocateBooleans(long dim1, long dim2) {
        return allocateArray(PointerIO.getArrayInstance(PointerIO.getBooleanInstance(), new long[] { dim1, dim2 }, 0), dim1);
        
    }
    	/**
     * Allocate enough memory for dim1 * dim2 * dim3 boolean values in a packed multi-dimensional C array and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @return pointer to dim1 * dim2 * dim3 zero-initialized boolean consecutive values
     */
    public static Pointer<Pointer<Pointer<Boolean>>> allocateBooleans(long dim1, long dim2, long dim3) {
        long[] dims = new long[] { dim1, dim2, dim3 };
		return
			allocateArray(
				PointerIO.getArrayInstance(
					//PointerIO.getBooleanInstance(),
					PointerIO.getArrayInstance(
						PointerIO.getBooleanInstance(), 
						dims,
						1
					),
					dims,
					0
				),
				dim1
			)
		;
    }

 //-- primitive (no bool): int --

	/**
     * Create a pointer to the memory location used by a direct NIO IntBuffer.<br>
     * If the NIO IntBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToInts(int[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Integer> pointerToInts(IntBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      int[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Integer> ptr = allocateInts(length);
      ptr.setIntsAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect IntBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= 4;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Integer> io = CommonPointerIOs.intIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
 //-- primitive (no bool): long --

	/**
     * Create a pointer to the memory location used by a direct NIO LongBuffer.<br>
     * If the NIO LongBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToLongs(long[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Long> pointerToLongs(LongBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      long[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Long> ptr = allocateLongs(length);
      ptr.setLongsAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect LongBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= 8;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Long> io = CommonPointerIOs.longIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
 //-- primitive (no bool): short --

	/**
     * Create a pointer to the memory location used by a direct NIO ShortBuffer.<br>
     * If the NIO ShortBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToShorts(short[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Short> pointerToShorts(ShortBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      short[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Short> ptr = allocateShorts(length);
      ptr.setShortsAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect ShortBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= 2;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Short> io = CommonPointerIOs.shortIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
 //-- primitive (no bool): byte --

	/**
     * Create a pointer to the memory location used by a direct NIO ByteBuffer.<br>
     * If the NIO ByteBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToBytes(byte[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Byte> pointerToBytes(ByteBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      byte[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Byte> ptr = allocateBytes(length);
      ptr.setBytesAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect ByteBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= 1;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Byte> io = CommonPointerIOs.byteIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
 //-- primitive (no bool): char --

	/**
     * Create a pointer to the memory location used by a direct NIO CharBuffer.<br>
     * If the NIO CharBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToChars(char[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Character> pointerToChars(CharBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      char[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Character> ptr = allocateChars(length);
      ptr.setCharsAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect CharBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= Platform.WCHAR_T_SIZE;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Character> io = CommonPointerIOs.charIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
 //-- primitive (no bool): float --

	/**
     * Create a pointer to the memory location used by a direct NIO FloatBuffer.<br>
     * If the NIO FloatBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToFloats(float[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Float> pointerToFloats(FloatBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      float[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Float> ptr = allocateFloats(length);
      ptr.setFloatsAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect FloatBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= 4;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Float> io = CommonPointerIOs.floatIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
 //-- primitive (no bool): double --

	/**
     * Create a pointer to the memory location used by a direct NIO DoubleBuffer.<br>
     * If the NIO DoubleBuffer is not direct, then its backing Java array is copied to some native memory and will never be updated by changes to the native memory (calls {@link Pointer#pointerToDoubles(double[])}), unless a call to {@link Pointer#updateBuffer(Buffer)} is made manually.<br>
     * The returned pointer (and its subsequent views returned by {@link Pointer#offset(long)} or {@link Pointer#next(long)}) can be used safely : it retains a reference to the original NIO buffer, so that this latter cannot be garbage collected before the pointer.<br>
     */
    public static Pointer<Double> pointerToDoubles(DoubleBuffer buffer) {
        if (buffer == null)
			return null;
		
		if (!buffer.isDirect()) {
      double[] array = buffer.array();
      int offset = buffer.arrayOffset();
      int length = array.length - offset;
      Pointer<Double> ptr = allocateDoubles(length);
      ptr.setDoublesAtOffset(0, array, offset, length);
			return ptr;
			//throw new UnsupportedOperationException("Cannot create pointers to indirect DoubleBuffer buffers");
		}
		
		long address = JNI.getDirectBufferAddress(buffer);
		long size = JNI.getDirectBufferCapacity(buffer);
		
		// HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
		size *= 8;
		//System.out.println("Buffer capacity = " + size);
		
		if (address == 0 || size == 0)
			return null;
		
		PointerIO<Double> io = CommonPointerIOs.doubleIO;
		boolean ordered = buffer.order().equals(ByteOrder.nativeOrder());
		return newPointer(io, address, ordered, address, address + size, null, NO_PARENT, null, buffer);
    }
	
    
    /**
     * Get the type of pointed elements.
     */
	public Type getTargetType() {
        PointerIO<T> io = getIO();
        return io == null ? null : io.getTargetType();
    }
    
    /**
	 * Read an untyped pointer value from the pointed memory location
	 * @deprecated Avoid using untyped pointers, if possible.
	 */
	@Deprecated
    public Pointer<?> getPointer() {
    	return getPointerAtOffset(0, (PointerIO)null);	
    }
    
    /**
	 * Read a pointer value from the pointed memory location shifted by a byte offset
	 */
	public Pointer<?> getPointerAtOffset(long byteOffset) {
        return getPointerAtOffset(byteOffset, (PointerIO)null);
    }
    
	/**
     * Read the nth contiguous pointer value from the pointed memory location.<br>
	   * Equivalent to <code>getPointerAtOffset(valueIndex * Pointer.SIZE)</code>.
     * @param valueIndex index of the value to read
	 */
	public Pointer<?> getPointerAtIndex(long valueIndex) {
	    return getPointerAtOffset(valueIndex * Pointer.SIZE);
	}
    
    /**
	 * Read a pointer value from the pointed memory location.<br>
	 * @param c class of the elements pointed by the resulting pointer 
	 */
    public <U> Pointer<U> getPointer(Class<U> c) {
    	return getPointerAtOffset(0, (PointerIO<U>)PointerIO.getInstance(c));	
    }
    
    /**
	 * Read a pointer value from the pointed memory location
	 * @param pio PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 */
    public <U> Pointer<U> getPointer(PointerIO<U> pio) {
    	return getPointerAtOffset(0, pio);	
    }
    
    /**
	 * Read a pointer value from the pointed memory location shifted by a byte offset
	 * @param c class of the elements pointed by the resulting pointer 
	 */
	public <U> Pointer<U> getPointerAtOffset(long byteOffset, Class<U> c) {
    	return getPointerAtOffset(byteOffset, (Type)c);	
    }
    
    /**
	 * Read a pointer value from the pointed memory location shifted by a byte offset
	 * @param t type of the elements pointed by the resulting pointer 
	 */
	public <U> Pointer<U> getPointerAtOffset(long byteOffset, Type t) {
        return getPointerAtOffset(byteOffset, t == null ? null : (PointerIO<U>)PointerIO.getInstance(t));
    }
    
    /**
	 * Read a pointer value from the pointed memory location shifted by a byte offset
	 * @param pio PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 */
	public <U> Pointer<U> getPointerAtOffset(long byteOffset, PointerIO<U> pio) {
    	long value = getSizeTAtOffset(byteOffset);
    	if (value == 0)
    		return null;
    	return newPointer(pio, value, isOrdered(), UNKNOWN_VALIDITY, UNKNOWN_VALIDITY, this, byteOffset, null, null);
    }

    /**
     * Write a pointer value to the pointed memory location
     */
    public Pointer<T> setPointer(Pointer<?> value) {
    	return setPointerAtOffset(0, value);
    }
    
    /**
     * Write a pointer value to the pointed memory location shifted by a byte offset
     */
	public Pointer<T> setPointerAtOffset(long byteOffset, Pointer<?> value) {
        setSizeTAtOffset(byteOffset, value == null ? 0 : value.getPeer());
        return this;
    }

	/**
     * Write the nth contiguous pointer value to the pointed memory location.<br>
	   * Equivalent to <code>setPointerAtOffset(valueIndex * Pointer.SIZE, value)</code>.
     * @param valueIndex index of the value to write
     * @param value pointer value to write
	 */
    public Pointer<T> setPointerAtIndex(long valueIndex, Pointer<?> value) {
        setPointerAtOffset(valueIndex * Pointer.SIZE, value);
        return this;
    }
    
    /**
	 * Read an array of untyped pointer values from the pointed memory location shifted by a byte offset
	 * @deprecated Use a typed version instead : {@link Pointer#getPointersAtOffset(long, int, Type)}, {@link Pointer#getPointersAtOffset(long, int, Class)} or {@link Pointer#getPointersAtOffset(long, int, PointerIO)}
	 */
	public Pointer<?>[] getPointersAtOffset(long byteOffset, int arrayLength) {
        return getPointersAtOffset(byteOffset, arrayLength, (PointerIO)null);
    }
    /**
	 * Read the array of remaining untyped pointer values from the pointed memory location
	 * @deprecated Use a typed version instead : {@link Pointer#getPointersAtOffset(long, int, Type)}, {@link Pointer#getPointersAtOffset(long, int, Class)} or {@link Pointer#getPointersAtOffset(long, int, PointerIO)}
	 */
    @Deprecated
	public Pointer<?>[] getPointers() {
        long rem = getValidElements("Cannot create array if remaining length is not known. Please use getPointers(int length) instead.");
		return getPointersAtOffset(0L, (int)rem);
    }
    /**
	 * Read an array of untyped pointer values from the pointed memory location
	 * @deprecated Use a typed version instead : {@link Pointer#getPointersAtOffset(long, int, Type)}, {@link Pointer#getPointersAtOffset(long, int, Class)} or {@link Pointer#getPointersAtOffset(long, int, PointerIO)}
	 */
    @Deprecated                     
	public Pointer<?>[] getPointers(int arrayLength) {
        return getPointersAtOffset(0, arrayLength);
    }
    /**
	 * Read an array of pointer values from the pointed memory location shifted by a byte offset
	 * @param t type of the elements pointed by the resulting pointer 
	 */
	public <U> Pointer<U>[] getPointersAtOffset(long byteOffset, int arrayLength, Type t) {
        return getPointersAtOffset(byteOffset, arrayLength, t == null ? null : (PointerIO<U>)PointerIO.getInstance(t));
    }
    /**
	 * Read an array of pointer values from the pointed memory location shifted by a byte offset
	 * @param t class of the elements pointed by the resulting pointer 
	 */
	public <U> Pointer<U>[] getPointersAtOffset(long byteOffset, int arrayLength, Class<U> t) {
        return getPointersAtOffset(byteOffset, arrayLength, (Type)t);
    }
    
    /**
	 * Read an array of pointer values from the pointed memory location shifted by a byte offset
	 * @param pio PointerIO instance that knows how to read the elements pointed by the resulting pointer 
	 */
	public <U> Pointer<U>[] getPointersAtOffset(long byteOffset, int arrayLength, PointerIO pio) {
    	Pointer<U>[] values = (Pointer<U>[])new Pointer[arrayLength];
		int s = Platform.POINTER_SIZE;
		for (int i = 0; i < arrayLength; i++)
			values[i] = getPointerAtOffset(byteOffset + i * s, pio);
		return values;
	}
	/**
	 * Write an array of pointer values to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setPointersAtOffset(long byteOffset, Pointer<?>[] values) {
    		return setPointersAtOffset(byteOffset, values, 0, values.length);
	}
	
	/**
	 * Write length pointer values from the given array (starting at the given value offset) to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setPointersAtOffset(long byteOffset, Pointer<?>[] values, int valuesOffset, int length) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		int n = length, s = Platform.POINTER_SIZE;
		for (int i = 0; i < n; i++)
			setPointerAtOffset(byteOffset + i * s, values[valuesOffset + i]);
		return this;
	}
	
	/**
	 * Write an array of pointer values to the pointed memory location
	 */
    public Pointer<T> setPointers(Pointer<?>[] values) {
    		return setPointersAtOffset(0, values);
	}
	
	/**
	 * Read an array of elements from the pointed memory location shifted by a byte offset.<br>
	 * For pointers to primitive types (e.g. {@code Pointer<Integer> }), this method returns primitive arrays (e.g. {@code int[] }), unlike {@link Pointer#toArray } (which returns arrays of objects so primitives end up being boxed, e.g. {@code Integer[] })
	 * @return an array of values of the requested length. The array is an array of primitives if the pointer's target type is a primitive or a boxed primitive type
	 */
	public Object getArrayAtOffset(long byteOffset, int length) {
        return getIO("Cannot create sublist").getArray(this, byteOffset, length);	
	}
	
	/**
	 * Read an array of elements from the pointed memory location.<br>
	 * For pointers to primitive types (e.g. {@code Pointer<Integer> }), this method returns primitive arrays (e.g. {@code int[] }), unlike {@link Pointer#toArray } (which returns arrays of objects so primitives end up being boxed, e.g. {@code Integer[] })
	 * @return an array of values of the requested length. The array is an array of primitives if the pointer's target type is a primitive or a boxed primitive type
	 */
	public Object getArray(int length) {
		return getArrayAtOffset(0L, length);	
	}
	
	/**
	 * Read the array of remaining elements from the pointed memory location.<br>
	 * For pointers to primitive types (e.g. {@code Pointer<Integer> }), this method returns primitive arrays (e.g. {@code int[] }), unlike {@link Pointer#toArray } (which returns arrays of objects so primitives end up being boxed, e.g. {@code Integer[] })
	 * @return an array of values of the requested length. The array is an array of primitives if the pointer's target type is a primitive or a boxed primitive type
	 */
	public Object getArray() {
		return getArray((int)getValidElements());	
	}
	
	/**
	 * Read an NIO {@link Buffer} of elements from the pointed memory location shifted by a byte offset.<br>
	 * @return an NIO {@link Buffer} of values of the requested length.
	 * @throws UnsupportedOperationException if this pointer's target type is not a Java primitive type with a corresponding NIO {@link Buffer} class.
	 */
	public <B extends Buffer> B getBufferAtOffset(long byteOffset, int length) {
        return (B)getIO("Cannot create Buffer").getBuffer(this, byteOffset, length);	
	}
	
	/**
	 * Read an NIO {@link Buffer} of elements from the pointed memory location.<br>
	 * @return an NIO {@link Buffer} of values of the requested length.
	 * @throws UnsupportedOperationException if this pointer's target type is not a Java primitive type with a corresponding NIO {@link Buffer} class.
	 */
	public <B extends Buffer> B getBuffer(int length) {
		return (B)getBufferAtOffset(0L, length);	
	}
	
	/**
	 * Read the NIO {@link Buffer} of remaining elements from the pointed memory location.<br>
	 * @return an array of values of the requested length.
	 * @throws UnsupportedOperationException if this pointer's target type is not a Java primitive type with a corresponding NIO {@link Buffer} class.
	 */
	public <B extends Buffer> B getBuffer() {
		return (B)getBuffer((int)getValidElements());	
	}
	
	/**
	 * Write an array of elements to the pointed memory location shifted by a byte offset.<br>
	 * For pointers to primitive types (e.g. {@code Pointer<Integer> }), this method accepts primitive arrays (e.g. {@code int[] }) instead of arrays of boxed primitives (e.g. {@code Integer[] })
	 */
	public Pointer<T> setArrayAtOffset(long byteOffset, Object array) {
        getIO("Cannot create sublist").setArray(this, byteOffset, array);
        return this;
	}
	
	/**
     * Allocate enough memory for array.length values, copy the values of the array provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * For pointers to primitive types (e.g. {@code Pointer<Integer> }), this method accepts primitive arrays (e.g. {@code int[] }) instead of arrays of boxed primitives (e.g. {@code Integer[] })
	 * @param array primitive array containing the initial values for the created memory area
     * @return pointer to a new memory location that initially contains the consecutive values provided in argument
     */
	public static <T> Pointer<T> pointerToArray(Object array) {
		if (array == null)
			return null;
		
		PointerIO<T> io = PointerIO.getArrayIO(array);
		if (io == null)
            throwBecauseUntyped("Cannot create pointer to array");
        
        Pointer<T> ptr = allocateArray(io, java.lang.reflect.Array.getLength(array));
        io.setArray(ptr, 0, array);
        return ptr;
	}
	
	/**
	 * Write an array of elements to the pointed memory location.<br>
	 * For pointers to primitive types (e.g. {@code Pointer<Integer> }), this method accepts primitive arrays (e.g. {@code int[] }) instead of arrays of boxed primitives (e.g. {@code Integer[] })
	 */
	public Pointer<T> setArray(Object array) {
		return setArrayAtOffset(0L, array);
	}
	
	//-- size primitive: SizeT --

	/**
     * Allocate enough memory for a single SizeT value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the SizeT value given in argument
     */
    public static Pointer<SizeT> pointerToSizeT(long value) {
		Pointer<SizeT> p = allocate(PointerIO.getSizeTInstance());
		p.setSizeT(value);
		return p;
	}
	/**
     * Allocate enough memory for a single SizeT value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the SizeT value given in argument
     */
    public static Pointer<SizeT> pointerToSizeT(SizeT value) {
		Pointer<SizeT> p = allocate(PointerIO.getSizeTInstance());
		p.setSizeT(value);
		return p;
	}
	/**
     * Allocate enough memory for values.length SizeT values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<SizeT>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the SizeT consecutive values provided in argument
     */
    public static Pointer<SizeT> pointerToSizeTs(long... values) {
		if (values == null)
			return null;
		return allocateArray(PointerIO.getSizeTInstance(), values.length).setSizeTsAtOffset(0, values);
	}
	/**
     * Allocate enough memory for values.length SizeT values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<SizeT>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the SizeT consecutive values provided in argument
     */
    public static Pointer<SizeT> pointerToSizeTs(SizeT... values) {
		if (values == null)
			return null;
		return allocateArray(PointerIO.getSizeTInstance(), values.length).setSizeTsAtOffset(0, values);
	}
	
	/**
     * Allocate enough memory for values.length SizeT values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<SizeT>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the SizeT consecutive values provided in argument
     */
    public static Pointer<SizeT> pointerToSizeTs(int[] values) {
		if (values == null)
			return null;
		return allocateArray(PointerIO.getSizeTInstance(), values.length).setSizeTsAtOffset(0, values);
	}
	
	/**
     * Allocate enough memory for arrayLength SizeT values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<SizeT>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized SizeT consecutive values
     */
    public static Pointer<SizeT> allocateSizeTs(long arrayLength) {
		return allocateArray(PointerIO.getSizeTInstance(), arrayLength);
	}
	/**
     * Allocate enough memory for a SizeT value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized SizeT value
     */
    public static Pointer<SizeT> allocateSizeT() {
		return allocate(PointerIO.getSizeTInstance());
	}
	
	/**
     * Read a SizeT value from the pointed memory location
     */
    public long getSizeT() {
		return SizeT.SIZE == 8 ? 
			getLong() : 
			getInt();// & 0xFFFFFFFFL;
	}
	/**
     * Read a SizeT value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getSizeT()} over this method. 
	 */
    public long getSizeTAtOffset(long byteOffset) {
		return SizeT.SIZE == 8 ? 
			getLongAtOffset(byteOffset) : 
			getIntAtOffset(byteOffset);// & 0xFFFFFFFFL;
	}
	/**
     * Read the nth contiguous SizeT value from the pointed memory location.<br>
	   * Equivalent to <code>getSizeTAtOffset(valueIndex * SizeT.SIZE</code>.
     * @param valueIndex index of the value to read
	 */
	public long getSizeTAtIndex(long valueIndex) {
	  return getSizeTAtOffset(valueIndex * SizeT.SIZE);
	}
	/**
     * Read the array of remaining SizeT values from the pointed memory location
     */
    public long[] getSizeTs() {
		long rem = getValidElements("Cannot create array if remaining length is not known. Please use getSizeTs(int length) instead.");
		if (SizeT.SIZE == 8)
    		return getLongs((int)rem);
		return getSizeTs((int)rem);
	}
	/**
     * Read an array of SizeT values of the specified length from the pointed memory location
     */
    public long[] getSizeTs(int arrayLength) {
    	if (SizeT.SIZE == 8)
    		return getLongs(arrayLength);
		return getSizeTsAtOffset(0, arrayLength);
	}
	/**
     * Read an array of SizeT values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getSizeTs(int)} over this method. 
	 */
	public long[] getSizeTsAtOffset(long byteOffset, int arrayLength) {
		if (SizeT.SIZE == 8)  
			return getLongsAtOffset(byteOffset, arrayLength);
		
		int[] values = getIntsAtOffset(byteOffset, arrayLength);
		long[] ret = new long[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			ret[i] = //0xffffffffL & 
				values[i];
		}
		return ret;
	}
	
	/**
     * Write a SizeT value to the pointed memory location
     */
    public Pointer<T> setSizeT(long value) {
    	if (SizeT.SIZE == 8)
			setLong(value);
		else {
			setInt(SizeT.safeIntCast(value));
		}
		return this;
	}
	/**
     * Write a SizeT value to the pointed memory location
     */
    public Pointer<T> setSizeT(SizeT value) {
		return setSizeT(value.longValue());
	}
    /**
     * Write a SizeT value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeT(long)} over this method. 
	 */
	public Pointer<T> setSizeTAtOffset(long byteOffset, long value) {
		if (SizeT.SIZE == 8)
			setLongAtOffset(byteOffset, value);
		else {
			setIntAtOffset(byteOffset, SizeT.safeIntCast(value));
		}
		return this;
	}
	/**
     * Write the nth contiguous SizeT value to the pointed memory location.<br>
	   * Equivalent to <code>setSizeTAtOffset(valueIndex * SizeT.SIZE, value)</code>.
     * @param valueIndex index of the value to write
     * @param value SizeT value to write
	 */
  public Pointer<T> setSizeTAtIndex(long valueIndex, long value) {
	  return setSizeTAtOffset(valueIndex * SizeT.SIZE, value);
	}
	
    /**
     * Write a SizeT value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeT(SizeT)} over this method. 
	 */
	public Pointer<T> setSizeTAtOffset(long byteOffset, SizeT value) {
		return setSizeTAtOffset(byteOffset, value.longValue());
	}
	/**
     * Write an array of SizeT values to the pointed memory location
     */
    public Pointer<T> setSizeTs(long[] values) {
		if (SizeT.SIZE == 8)
    		return setLongs(values);
		return setSizeTsAtOffset(0, values);
	}
	/**
     * Write an array of SizeT values to the pointed memory location
     */
    public Pointer<T> setSizeTs(int[] values) {
    	if (SizeT.SIZE == 4)
    		return setInts(values);
		return setSizeTsAtOffset(0, values);
	}
	/**
     * Write an array of SizeT values to the pointed memory location
     */
    public Pointer<T> setSizeTs(SizeT[] values) {
		return setSizeTsAtOffset(0, values);
	}
	/**
     * Write an array of SizeT values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeTs(long[])} over this method. 
	 */
	public Pointer<T> setSizeTsAtOffset(long byteOffset, long[] values) {
    		return setSizeTsAtOffset(byteOffset, values, 0, values.length);
	}
	/**
     * Write an array of SizeT values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeTs(long[])} over this method. 
	 */
    public abstract Pointer<T> setSizeTsAtOffset(long byteOffset, long[] values, int valuesOffset, int length);
	/**
     * Write an array of SizeT values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeTs(SizeT...)} over this method. 
	 */
	public Pointer<T> setSizeTsAtOffset(long byteOffset, SizeT... values) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		int n = values.length, s = SizeT.SIZE;
		for (int i = 0; i < n; i++)
			setSizeTAtOffset(byteOffset + i * s, values[i].longValue());
		return this;
	}
	/**
     * Write an array of SizeT values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setSizeTs(int[])} over this method. 
	 */
	public abstract Pointer<T> setSizeTsAtOffset(long byteOffset, int[] values);
	
	//-- size primitive: CLong --

	/**
     * Allocate enough memory for a single CLong value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the CLong value given in argument
     */
    public static Pointer<CLong> pointerToCLong(long value) {
		Pointer<CLong> p = allocate(PointerIO.getCLongInstance());
		p.setCLong(value);
		return p;
	}
	/**
     * Allocate enough memory for a single CLong value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the CLong value given in argument
     */
    public static Pointer<CLong> pointerToCLong(CLong value) {
		Pointer<CLong> p = allocate(PointerIO.getCLongInstance());
		p.setCLong(value);
		return p;
	}
	/**
     * Allocate enough memory for values.length CLong values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<CLong>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the CLong consecutive values provided in argument
     */
    public static Pointer<CLong> pointerToCLongs(long... values) {
		if (values == null)
			return null;
		return allocateArray(PointerIO.getCLongInstance(), values.length).setCLongsAtOffset(0, values);
	}
	/**
     * Allocate enough memory for values.length CLong values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<CLong>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the CLong consecutive values provided in argument
     */
    public static Pointer<CLong> pointerToCLongs(CLong... values) {
		if (values == null)
			return null;
		return allocateArray(PointerIO.getCLongInstance(), values.length).setCLongsAtOffset(0, values);
	}
	
	/**
     * Allocate enough memory for values.length CLong values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<CLong>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the CLong consecutive values provided in argument
     */
    public static Pointer<CLong> pointerToCLongs(int[] values) {
		if (values == null)
			return null;
		return allocateArray(PointerIO.getCLongInstance(), values.length).setCLongsAtOffset(0, values);
	}
	
	/**
     * Allocate enough memory for arrayLength CLong values and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<CLong>} instance that can be safely iterated upon.
     * @return pointer to arrayLength zero-initialized CLong consecutive values
     */
    public static Pointer<CLong> allocateCLongs(long arrayLength) {
		return allocateArray(PointerIO.getCLongInstance(), arrayLength);
	}
	/**
     * Allocate enough memory for a CLong value and return a pointer to it.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * @return pointer to a single zero-initialized CLong value
     */
    public static Pointer<CLong> allocateCLong() {
		return allocate(PointerIO.getCLongInstance());
	}
	
	/**
     * Read a CLong value from the pointed memory location
     */
    public long getCLong() {
		return CLong.SIZE == 8 ? 
			getLong() : 
			getInt();// & 0xFFFFFFFFL;
	}
	/**
     * Read a CLong value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getCLong()} over this method. 
	 */
    public long getCLongAtOffset(long byteOffset) {
		return CLong.SIZE == 8 ? 
			getLongAtOffset(byteOffset) : 
			getIntAtOffset(byteOffset);// & 0xFFFFFFFFL;
	}
	/**
     * Read the nth contiguous CLong value from the pointed memory location.<br>
	   * Equivalent to <code>getCLongAtOffset(valueIndex * CLong.SIZE</code>.
     * @param valueIndex index of the value to read
	 */
	public long getCLongAtIndex(long valueIndex) {
	  return getCLongAtOffset(valueIndex * CLong.SIZE);
	}
	/**
     * Read the array of remaining CLong values from the pointed memory location
     */
    public long[] getCLongs() {
		long rem = getValidElements("Cannot create array if remaining length is not known. Please use getCLongs(int length) instead.");
		if (CLong.SIZE == 8)
    		return getLongs((int)rem);
		return getCLongs((int)rem);
	}
	/**
     * Read an array of CLong values of the specified length from the pointed memory location
     */
    public long[] getCLongs(int arrayLength) {
    	if (CLong.SIZE == 8)
    		return getLongs(arrayLength);
		return getCLongsAtOffset(0, arrayLength);
	}
	/**
     * Read an array of CLong values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getCLongs(int)} over this method. 
	 */
	public long[] getCLongsAtOffset(long byteOffset, int arrayLength) {
		if (CLong.SIZE == 8)  
			return getLongsAtOffset(byteOffset, arrayLength);
		
		int[] values = getIntsAtOffset(byteOffset, arrayLength);
		long[] ret = new long[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			ret[i] = //0xffffffffL & 
				values[i];
		}
		return ret;
	}
	
	/**
     * Write a CLong value to the pointed memory location
     */
    public Pointer<T> setCLong(long value) {
    	if (CLong.SIZE == 8)
			setLong(value);
		else {
			setInt(SizeT.safeIntCast(value));
		}
		return this;
	}
	/**
     * Write a CLong value to the pointed memory location
     */
    public Pointer<T> setCLong(CLong value) {
		return setCLong(value.longValue());
	}
    /**
     * Write a CLong value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLong(long)} over this method. 
	 */
	public Pointer<T> setCLongAtOffset(long byteOffset, long value) {
		if (CLong.SIZE == 8)
			setLongAtOffset(byteOffset, value);
		else {
			setIntAtOffset(byteOffset, SizeT.safeIntCast(value));
		}
		return this;
	}
	/**
     * Write the nth contiguous CLong value to the pointed memory location.<br>
	   * Equivalent to <code>setCLongAtOffset(valueIndex * CLong.SIZE, value)</code>.
     * @param valueIndex index of the value to write
     * @param value CLong value to write
	 */
  public Pointer<T> setCLongAtIndex(long valueIndex, long value) {
	  return setCLongAtOffset(valueIndex * CLong.SIZE, value);
	}
	
    /**
     * Write a CLong value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLong(CLong)} over this method. 
	 */
	public Pointer<T> setCLongAtOffset(long byteOffset, CLong value) {
		return setCLongAtOffset(byteOffset, value.longValue());
	}
	/**
     * Write an array of CLong values to the pointed memory location
     */
    public Pointer<T> setCLongs(long[] values) {
		if (CLong.SIZE == 8)
    		return setLongs(values);
		return setCLongsAtOffset(0, values);
	}
	/**
     * Write an array of CLong values to the pointed memory location
     */
    public Pointer<T> setCLongs(int[] values) {
    	if (CLong.SIZE == 4)
    		return setInts(values);
		return setCLongsAtOffset(0, values);
	}
	/**
     * Write an array of CLong values to the pointed memory location
     */
    public Pointer<T> setCLongs(CLong[] values) {
		return setCLongsAtOffset(0, values);
	}
	/**
     * Write an array of CLong values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLongs(long[])} over this method. 
	 */
	public Pointer<T> setCLongsAtOffset(long byteOffset, long[] values) {
    		return setCLongsAtOffset(byteOffset, values, 0, values.length);
	}
	/**
     * Write an array of CLong values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLongs(long[])} over this method. 
	 */
    public abstract Pointer<T> setCLongsAtOffset(long byteOffset, long[] values, int valuesOffset, int length);
	/**
     * Write an array of CLong values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLongs(CLong...)} over this method. 
	 */
	public Pointer<T> setCLongsAtOffset(long byteOffset, CLong... values) {
		if (values == null)
			throw new IllegalArgumentException("Null values");
		int n = values.length, s = CLong.SIZE;
		for (int i = 0; i < n; i++)
			setCLongAtOffset(byteOffset + i * s, values[i].longValue());
		return this;
	}
	/**
     * Write an array of CLong values to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setCLongs(int[])} over this method. 
	 */
	public abstract Pointer<T> setCLongsAtOffset(long byteOffset, int[] values);
	
		
	void setSignedIntegralAtOffset(long byteOffset, long value, long sizeOfIntegral) {
		switch ((int)sizeOfIntegral) {
		case 1:
			if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE)
				throw new RuntimeException("Value out of byte bounds : " + value);
			setByteAtOffset(byteOffset, (byte)value);
			break;
		case 2:
			if (value > Short.MAX_VALUE || value < Short.MIN_VALUE)
				throw new RuntimeException("Value out of short bounds : " + value);
			setShortAtOffset(byteOffset, (short)value);
			break;
		case 4:
			if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
				throw new RuntimeException("Value out of int bounds : " + value);
			setIntAtOffset(byteOffset, (int)value);
			break;
		case 8:
			setLongAtOffset(byteOffset, value);
			break;
		default:
			throw new IllegalArgumentException("Cannot write integral type of size " + sizeOfIntegral + " (value = " + value + ")");
		}
	}
	long getSignedIntegralAtOffset(long byteOffset, long sizeOfIntegral) {
		switch ((int)sizeOfIntegral) {
		case 1:
			return getByteAtOffset(byteOffset);
		case 2:
			return getShortAtOffset(byteOffset);
		case 4:
			return getIntAtOffset(byteOffset);
		case 8:
			return getLongAtOffset(byteOffset);
		default:
			throw new IllegalArgumentException("Cannot read integral type of size " + sizeOfIntegral);
		}
	}
	
	/**
     * Allocate enough memory for a single pointer value, copy the value provided in argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * @param value initial value for the created memory location
     * @return pointer to a new memory location that initially contains the pointer value given in argument
     */
    public static <T> Pointer<Pointer<T>> pointerToPointer(Pointer<T> value) {
		Pointer<Pointer<T>> p = (Pointer<Pointer<T>>)(Pointer)allocate(PointerIO.getPointerInstance());
		p.setPointerAtOffset(0, value);
		return p;
	}
	
	/**
     * Allocate enough memory for values.length pointer values, copy the values provided as argument into it and return a pointer to that memory.<br>
     * The memory will be automatically be freed when the pointer is garbage-collected or upon manual calls to {@link Pointer#release()}.<br>
     * The pointer won't be garbage-collected until all its views are garbage-collected themselves ({@link Pointer#offset(long)}, {@link Pointer#next(long)}, {@link Pointer#next()}).<br>
     * The returned pointer is also an {@code Iterable<Pointer>} instance that can be safely iterated upon :
     <pre>{@code
     for (float f : pointerToFloats(1f, 2f, 3.3f))
     	System.out.println(f); }</pre>
     * @param values initial values for the created memory location
     * @return pointer to a new memory location that initially contains the pointer consecutive values provided in argument
     */
	public static <T> Pointer<Pointer<T>> pointerToPointers(Pointer<T>... values) {
		if (values == null)
			return null;
		int n = values.length, s = Pointer.SIZE;
		PointerIO<?> pio = PointerIO.getPointerInstance(); // TODO get actual pointer instances PointerIO !!!
		Pointer<Pointer<T>> p = (Pointer<Pointer<T>>)(Pointer)allocateArray(pio, n);
		for (int i = 0; i < n; i++) {
			p.setPointerAtOffset(i * s, values[i]);
		}
		return p;
	}
	
    /**
     * Copy all values from an NIO buffer to the pointed memory location shifted by a byte offset
     */
	public Pointer<T> setValuesAtOffset(long byteOffset, Buffer values) {
                if (values instanceof IntBuffer) {
            setIntsAtOffset(byteOffset, (IntBuffer)values);
            return this;
        }
                if (values instanceof LongBuffer) {
            setLongsAtOffset(byteOffset, (LongBuffer)values);
            return this;
        }
                if (values instanceof ShortBuffer) {
            setShortsAtOffset(byteOffset, (ShortBuffer)values);
            return this;
        }
                if (values instanceof ByteBuffer) {
            setBytesAtOffset(byteOffset, (ByteBuffer)values);
            return this;
        }
                if (values instanceof CharBuffer) {
            setCharsAtOffset(byteOffset, (CharBuffer)values);
            return this;
        }
                if (values instanceof FloatBuffer) {
            setFloatsAtOffset(byteOffset, (FloatBuffer)values);
            return this;
        }
                if (values instanceof DoubleBuffer) {
            setDoublesAtOffset(byteOffset, (DoubleBuffer)values);
            return this;
        }
                throw new UnsupportedOperationException("Unhandled buffer type : " + values.getClass().getName());
    }
    
    /**
     * Copy length values from an NIO buffer (beginning at element at valuesOffset index) to the pointed memory location shifted by a byte offset
     */
	public Pointer<T> setValuesAtOffset(long byteOffset, Buffer values, int valuesOffset, int length) {
                if (values instanceof IntBuffer) {
            setIntsAtOffset(byteOffset, (IntBuffer)values, valuesOffset, length);
            return this;
        }
                if (values instanceof LongBuffer) {
            setLongsAtOffset(byteOffset, (LongBuffer)values, valuesOffset, length);
            return this;
        }
                if (values instanceof ShortBuffer) {
            setShortsAtOffset(byteOffset, (ShortBuffer)values, valuesOffset, length);
            return this;
        }
                if (values instanceof ByteBuffer) {
            setBytesAtOffset(byteOffset, (ByteBuffer)values, valuesOffset, length);
            return this;
        }
                if (values instanceof CharBuffer) {
            setCharsAtOffset(byteOffset, (CharBuffer)values, valuesOffset, length);
            return this;
        }
                if (values instanceof FloatBuffer) {
            setFloatsAtOffset(byteOffset, (FloatBuffer)values, valuesOffset, length);
            return this;
        }
                if (values instanceof DoubleBuffer) {
            setDoublesAtOffset(byteOffset, (DoubleBuffer)values, valuesOffset, length);
            return this;
        }
                throw new UnsupportedOperationException("Unhandled buffer type : " + values.getClass().getName());
    }
    
    /**
     * Copy values from an NIO buffer to the pointed memory location
     */
    public Pointer<T> setValues(Buffer values) {
    	        if (values instanceof IntBuffer) {
            setInts((IntBuffer)values);
            return this;
        }
                if (values instanceof LongBuffer) {
            setLongs((LongBuffer)values);
            return this;
        }
                if (values instanceof ShortBuffer) {
            setShorts((ShortBuffer)values);
            return this;
        }
                if (values instanceof ByteBuffer) {
            setBytes((ByteBuffer)values);
            return this;
        }
                if (values instanceof CharBuffer) {
            setChars((CharBuffer)values);
            return this;
        }
                if (values instanceof FloatBuffer) {
            setFloats((FloatBuffer)values);
            return this;
        }
                if (values instanceof DoubleBuffer) {
            setDoubles((DoubleBuffer)values);
            return this;
        }
                throw new UnsupportedOperationException("Unhandled buffer type : " + values.getClass().getName());
    }

    /**
     * Copy bytes from the memory location indicated by this pointer to that of another pointer (with byte offsets for both the source and the destination), using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memcpy/">memcpy</a> C function.<br>
     * If the destination and source memory locations are likely to overlap, {@link Pointer#moveBytesAtOffsetTo(long, Pointer, long, long)} must be used instead.
     */
    @Deprecated
	public Pointer<T> copyBytesAtOffsetTo(long byteOffset, Pointer<?> destination, long byteOffsetInDestination, long byteCount) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + byteCount) > validEnd
	   )) {
		invalidPeer(checkedPeer, byteCount);
	}
		JNI.memcpy(destination.getCheckedPeer(byteOffsetInDestination, byteCount), checkedPeer, byteCount);
		return this;
    }
    
    /**
     * Copy bytes from the memory location indicated by this pointer to that of another pointer using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memcpy/">memcpy</a> C function.<br>
     * If the destination and source memory locations are likely to overlap, {@link Pointer#moveBytesAtOffsetTo(long, Pointer, long, long)} must be used instead.<br>
     * See {@link Pointer#copyBytesAtOffsetTo(long, Pointer, long, long)} for more options.
     */
    @Deprecated
	public Pointer<T> copyBytesTo(Pointer<?> destination, long byteCount) {
    		return copyBytesAtOffsetTo(0, destination, 0, byteCount);
    }
    
    /**
     * Copy bytes from the memory location indicated by this pointer to that of another pointer (with byte offsets for both the source and the destination), using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memmove/">memmove</a> C function.<br>
     * Works even if the destination and source memory locations are overlapping.
     */
    @Deprecated
	public Pointer<T> moveBytesAtOffsetTo(long byteOffset, Pointer<?> destination, long byteOffsetInDestination, long byteCount) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + byteCount) > validEnd
	   )) {
		invalidPeer(checkedPeer, byteCount);
	}
		JNI.memmove(destination.getCheckedPeer(byteOffsetInDestination, byteCount), checkedPeer, byteCount);
    		return this;
    }
    
    /**
     * Copy bytes from the memory location indicated by this pointer to that of another pointer, using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memmove/">memmove</a> C function.<br>
     * Works even if the destination and source memory locations are overlapping.
     */
	public Pointer<T> moveBytesTo(Pointer<?> destination, long byteCount) {
    		return moveBytesAtOffsetTo(0, destination, 0, byteCount);
    }
    
    /**
     * Copy all valid bytes from the memory location indicated by this pointer to that of another pointer, using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memmove/">memmove</a> C function.<br>
     * Works even if the destination and source memory locations are overlapping.
     */
	public Pointer<T> moveBytesTo(Pointer<?> destination) {
    		return moveBytesTo(destination, getValidBytes("Cannot move an unbounded memory location. Please use validBytes(long)."));
    }
    
    final long getValidBytes(String error) {
    		long rem = getValidBytes();
    		if (rem < 0)
    			throw new IndexOutOfBoundsException(error);

        return rem;
    }
    final long getValidElements(String error) {
    		long rem = getValidElements();
    		if (rem < 0)
    			throw new IndexOutOfBoundsException(error);

        return rem;
    }
    final PointerIO<T> getIO(String error) {
    		PointerIO<T> io = getIO();
        if (io == null)
            throwBecauseUntyped(error);
        return io;
    }
    
    /**
    * Copy remaining bytes from this pointer to a destination using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memcpy/">memcpy</a> C function (see {@link Pointer#copyBytesTo(Pointer, long)}, {@link Pointer#getValidBytes()})
     */
    public Pointer<T> copyTo(Pointer<?> destination) {
    		return copyTo(destination, getValidElements());
    }
    
    /**
    * Copy remaining elements from this pointer to a destination using the <a href="http://www.cplusplus.com/reference/clibrary/cstring/memcpy/">memcpy</a> C function (see {@link Pointer#copyBytesAtOffsetTo(long, Pointer, long, long)}, {@link Pointer#getValidBytes})
     */
    public Pointer<T> copyTo(Pointer<?> destination, long elementCount) {
    		PointerIO<T> io = getIO("Cannot copy untyped pointer without byte count information. Please use copyBytesAtOffsetTo(offset, destination, destinationOffset, byteCount) instead");
    		return copyBytesAtOffsetTo(0, destination, 0, elementCount * io.getTargetSize());
    }
    
    /**
     * Find the first appearance of the sequence of valid bytes pointed by needle in the memory area pointed to by this bounded pointer (behaviour equivalent to <a href="http://linux.die.net/man/3/memmem">memmem</a>, which is used underneath on platforms where it is available)
     */
    public Pointer<T> find(Pointer<?> needle) {
    		if (needle == null)
    			return null;
    		long firstOccurrence = JNI.memmem(
			getPeer(), 
			getValidBytes("Cannot search an unbounded memory area. Please set bounds with validBytes(long)."), 
			needle.getPeer(), 
			needle.getValidBytes("Cannot search for an unbounded content. Please set bounds with validBytes(long).")
		);
		return pointerToAddress(firstOccurrence, io);
    }
    
    /**
    * Find the last appearance of the sequence of valid bytes pointed by needle in the memory area pointed to by this bounded pointer (also see {@link Pointer#find(Pointer)}).
     */
    public Pointer<T> findLast(Pointer<?> needle) {
    		if (needle == null)
    			return null;
    		long lastOccurrence = JNI.memmem_last(
			getPeer(), 
			getValidBytes("Cannot search an unbounded memory area. Please set bounds with validBytes(long)."), 
			needle.getPeer(), 
			needle.getValidBytes("Cannot search for an unbounded content. Please set bounds with validBytes(long).")
		);
		return pointerToAddress(lastOccurrence, io);
    }


 //-- primitive: int --
	
	/**
     * Write a int value to the pointed memory location
     */
	public abstract Pointer<T> setInt(int value);
	
    /**
     * Write a int value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setInt(int)} over this method. 
	 */
	public abstract Pointer<T> setIntAtOffset(long byteOffset, int value);
	
	/**
     * Write the nth contiguous int value to the pointed memory location.<br>
	   * Equivalent to <code>setIntAtOffset(valueIndex * 4, value)</code>.
     * @param valueIndex index of the value to write
     * @param value int value to write
	 */
	public Pointer<T> setIntAtIndex(long valueIndex, int value) {
		return setIntAtOffset(valueIndex * 4, value);
	}
		
	/**
	 * Write an array of int values of the specified length to the pointed memory location
	 */
    public Pointer<T> setInts(int[] values) {
		return setIntsAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of int values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setIntsAtOffset(long byteOffset, int[] values) {
        return setIntsAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of int values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setIntsAtOffset(long byteOffset, int[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4 * length);
	}
            	if (!isOrdered()) {
        	JNI.set_int_array_disordered(checkedPeer, values, valuesOffset, length);
        	return this;
    	}
        		JNI.set_int_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a int value from the pointed memory location
     */
    public abstract int getInt();
    
	/**
     * Read a int value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getInt()} over this method. 
	 */
	public abstract int getIntAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous int value from the pointed memory location.<br>
	   * Equivalent to <code>getIntAtOffset(valueIndex * 4)</code>.
     * @param valueIndex index of the value to read
	 */
	public int getIntAtIndex(long valueIndex) {
		return getIntAtOffset(valueIndex * 4);
	}
	
	/**
     * Read an array of int values of the specified length from the pointed memory location
     */
	public int[] getInts(int length) {
		return getIntsAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining int values from the pointed memory location
     */
    public int[] getInts() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getInts(int length) instead.");
		return getInts((int)(validBytes / 4));
    }

	/**
     * Read an array of int values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getInts(int)} over this method. 
	 */
	public int[] getIntsAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4 * length);
	}
            	if (!isOrdered())
        	return JNI.get_int_array_disordered(checkedPeer, length);
                return JNI.get_int_array(checkedPeer, length);
    }
    
 //-- primitive: long --
	
	/**
     * Write a long value to the pointed memory location
     */
	public abstract Pointer<T> setLong(long value);
	
    /**
     * Write a long value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setLong(long)} over this method. 
	 */
	public abstract Pointer<T> setLongAtOffset(long byteOffset, long value);
	
	/**
     * Write the nth contiguous long value to the pointed memory location.<br>
	   * Equivalent to <code>setLongAtOffset(valueIndex * 8, value)</code>.
     * @param valueIndex index of the value to write
     * @param value long value to write
	 */
	public Pointer<T> setLongAtIndex(long valueIndex, long value) {
		return setLongAtOffset(valueIndex * 8, value);
	}
		
	/**
	 * Write an array of long values of the specified length to the pointed memory location
	 */
    public Pointer<T> setLongs(long[] values) {
		return setLongsAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of long values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setLongsAtOffset(long byteOffset, long[] values) {
        return setLongsAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of long values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setLongsAtOffset(long byteOffset, long[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8 * length);
	}
            	if (!isOrdered()) {
        	JNI.set_long_array_disordered(checkedPeer, values, valuesOffset, length);
        	return this;
    	}
        		JNI.set_long_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a long value from the pointed memory location
     */
    public abstract long getLong();
    
	/**
     * Read a long value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getLong()} over this method. 
	 */
	public abstract long getLongAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous long value from the pointed memory location.<br>
	   * Equivalent to <code>getLongAtOffset(valueIndex * 8)</code>.
     * @param valueIndex index of the value to read
	 */
	public long getLongAtIndex(long valueIndex) {
		return getLongAtOffset(valueIndex * 8);
	}
	
	/**
     * Read an array of long values of the specified length from the pointed memory location
     */
	public long[] getLongs(int length) {
		return getLongsAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining long values from the pointed memory location
     */
    public long[] getLongs() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getLongs(int length) instead.");
		return getLongs((int)(validBytes / 8));
    }

	/**
     * Read an array of long values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getLongs(int)} over this method. 
	 */
	public long[] getLongsAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8 * length);
	}
            	if (!isOrdered())
        	return JNI.get_long_array_disordered(checkedPeer, length);
                return JNI.get_long_array(checkedPeer, length);
    }
    
 //-- primitive: short --
	
	/**
     * Write a short value to the pointed memory location
     */
	public abstract Pointer<T> setShort(short value);
	
    /**
     * Write a short value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setShort(short)} over this method. 
	 */
	public abstract Pointer<T> setShortAtOffset(long byteOffset, short value);
	
	/**
     * Write the nth contiguous short value to the pointed memory location.<br>
	   * Equivalent to <code>setShortAtOffset(valueIndex * 2, value)</code>.
     * @param valueIndex index of the value to write
     * @param value short value to write
	 */
	public Pointer<T> setShortAtIndex(long valueIndex, short value) {
		return setShortAtOffset(valueIndex * 2, value);
	}
		
	/**
	 * Write an array of short values of the specified length to the pointed memory location
	 */
    public Pointer<T> setShorts(short[] values) {
		return setShortsAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of short values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setShortsAtOffset(long byteOffset, short[] values) {
        return setShortsAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of short values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setShortsAtOffset(long byteOffset, short[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2 * length);
	}
            	if (!isOrdered()) {
        	JNI.set_short_array_disordered(checkedPeer, values, valuesOffset, length);
        	return this;
    	}
        		JNI.set_short_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a short value from the pointed memory location
     */
    public abstract short getShort();
    
	/**
     * Read a short value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getShort()} over this method. 
	 */
	public abstract short getShortAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous short value from the pointed memory location.<br>
	   * Equivalent to <code>getShortAtOffset(valueIndex * 2)</code>.
     * @param valueIndex index of the value to read
	 */
	public short getShortAtIndex(long valueIndex) {
		return getShortAtOffset(valueIndex * 2);
	}
	
	/**
     * Read an array of short values of the specified length from the pointed memory location
     */
	public short[] getShorts(int length) {
		return getShortsAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining short values from the pointed memory location
     */
    public short[] getShorts() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getShorts(int length) instead.");
		return getShorts((int)(validBytes / 2));
    }

	/**
     * Read an array of short values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getShorts(int)} over this method. 
	 */
	public short[] getShortsAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2 * length);
	}
            	if (!isOrdered())
        	return JNI.get_short_array_disordered(checkedPeer, length);
                return JNI.get_short_array(checkedPeer, length);
    }
    
 //-- primitive: byte --
	
	/**
     * Write a byte value to the pointed memory location
     */
	public abstract Pointer<T> setByte(byte value);
	
    /**
     * Write a byte value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setByte(byte)} over this method. 
	 */
	public abstract Pointer<T> setByteAtOffset(long byteOffset, byte value);
	
	/**
     * Write the nth contiguous byte value to the pointed memory location.<br>
	   * Equivalent to <code>setByteAtOffset(valueIndex * 1, value)</code>.
     * @param valueIndex index of the value to write
     * @param value byte value to write
	 */
	public Pointer<T> setByteAtIndex(long valueIndex, byte value) {
		return setByteAtOffset(valueIndex * 1, value);
	}
		
	/**
	 * Write an array of byte values of the specified length to the pointed memory location
	 */
    public Pointer<T> setBytes(byte[] values) {
		return setBytesAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of byte values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setBytesAtOffset(long byteOffset, byte[] values) {
        return setBytesAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of byte values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setBytesAtOffset(long byteOffset, byte[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1 * length);
	}
        		JNI.set_byte_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a byte value from the pointed memory location
     */
    public abstract byte getByte();
    
	/**
     * Read a byte value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getByte()} over this method. 
	 */
	public abstract byte getByteAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous byte value from the pointed memory location.<br>
	   * Equivalent to <code>getByteAtOffset(valueIndex * 1)</code>.
     * @param valueIndex index of the value to read
	 */
	public byte getByteAtIndex(long valueIndex) {
		return getByteAtOffset(valueIndex * 1);
	}
	
	/**
     * Read an array of byte values of the specified length from the pointed memory location
     */
	public byte[] getBytes(int length) {
		return getBytesAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining byte values from the pointed memory location
     */
    public byte[] getBytes() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getBytes(int length) instead.");
		return getBytes((int)(validBytes / 1));
    }

	/**
     * Read an array of byte values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getBytes(int)} over this method. 
	 */
	public byte[] getBytesAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1 * length);
	}
                return JNI.get_byte_array(checkedPeer, length);
    }
    
 //-- primitive: char --
	
	/**
     * Write a char value to the pointed memory location
     */
	public abstract Pointer<T> setChar(char value);
	
    /**
     * Write a char value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setChar(char)} over this method. 
	 */
	public abstract Pointer<T> setCharAtOffset(long byteOffset, char value);
	
	/**
     * Write the nth contiguous char value to the pointed memory location.<br>
	   * Equivalent to <code>setCharAtOffset(valueIndex * Platform.WCHAR_T_SIZE, value)</code>.
     * @param valueIndex index of the value to write
     * @param value char value to write
	 */
	public Pointer<T> setCharAtIndex(long valueIndex, char value) {
		return setCharAtOffset(valueIndex * Platform.WCHAR_T_SIZE, value);
	}
		
	/**
	 * Write an array of char values of the specified length to the pointed memory location
	 */
    public Pointer<T> setChars(char[] values) {
		return setCharsAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of char values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setCharsAtOffset(long byteOffset, char[] values) {
        return setCharsAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of char values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setCharsAtOffset(long byteOffset, char[] values, int valuesOffset, int length) {
        		if (Platform.WCHAR_T_SIZE == 4)
			return setIntsAtOffset(byteOffset, wcharsToInts(values, valuesOffset, length));
		    		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE * length);
	}
            	if (!isOrdered()) {
        	JNI.set_char_array_disordered(checkedPeer, values, valuesOffset, length);
        	return this;
    	}
        		JNI.set_char_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a char value from the pointed memory location
     */
    public abstract char getChar();
    
	/**
     * Read a char value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getChar()} over this method. 
	 */
	public abstract char getCharAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous char value from the pointed memory location.<br>
	   * Equivalent to <code>getCharAtOffset(valueIndex * Platform.WCHAR_T_SIZE)</code>.
     * @param valueIndex index of the value to read
	 */
	public char getCharAtIndex(long valueIndex) {
		return getCharAtOffset(valueIndex * Platform.WCHAR_T_SIZE);
	}
	
	/**
     * Read an array of char values of the specified length from the pointed memory location
     */
	public char[] getChars(int length) {
		return getCharsAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining char values from the pointed memory location
     */
    public char[] getChars() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getChars(int length) instead.");
		return getChars((int)(validBytes / Platform.WCHAR_T_SIZE));
    }

	/**
     * Read an array of char values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getChars(int)} over this method. 
	 */
	public char[] getCharsAtOffset(long byteOffset, int length) {
        		if (Platform.WCHAR_T_SIZE == 4)
			return intsToWChars(getIntsAtOffset(byteOffset, length));
		    		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE * length);
	}
            	if (!isOrdered())
        	return JNI.get_char_array_disordered(checkedPeer, length);
                return JNI.get_char_array(checkedPeer, length);
    }
    
 //-- primitive: float --
	
	/**
     * Write a float value to the pointed memory location
     */
	public abstract Pointer<T> setFloat(float value);
	
    /**
     * Write a float value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setFloat(float)} over this method. 
	 */
	public abstract Pointer<T> setFloatAtOffset(long byteOffset, float value);
	
	/**
     * Write the nth contiguous float value to the pointed memory location.<br>
	   * Equivalent to <code>setFloatAtOffset(valueIndex * 4, value)</code>.
     * @param valueIndex index of the value to write
     * @param value float value to write
	 */
	public Pointer<T> setFloatAtIndex(long valueIndex, float value) {
		return setFloatAtOffset(valueIndex * 4, value);
	}
		
	/**
	 * Write an array of float values of the specified length to the pointed memory location
	 */
    public Pointer<T> setFloats(float[] values) {
		return setFloatsAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of float values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setFloatsAtOffset(long byteOffset, float[] values) {
        return setFloatsAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of float values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setFloatsAtOffset(long byteOffset, float[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4 * length);
	}
            	if (!isOrdered()) {
        	JNI.set_float_array_disordered(checkedPeer, values, valuesOffset, length);
        	return this;
    	}
        		JNI.set_float_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a float value from the pointed memory location
     */
    public abstract float getFloat();
    
	/**
     * Read a float value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getFloat()} over this method. 
	 */
	public abstract float getFloatAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous float value from the pointed memory location.<br>
	   * Equivalent to <code>getFloatAtOffset(valueIndex * 4)</code>.
     * @param valueIndex index of the value to read
	 */
	public float getFloatAtIndex(long valueIndex) {
		return getFloatAtOffset(valueIndex * 4);
	}
	
	/**
     * Read an array of float values of the specified length from the pointed memory location
     */
	public float[] getFloats(int length) {
		return getFloatsAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining float values from the pointed memory location
     */
    public float[] getFloats() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getFloats(int length) instead.");
		return getFloats((int)(validBytes / 4));
    }

	/**
     * Read an array of float values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getFloats(int)} over this method. 
	 */
	public float[] getFloatsAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4 * length);
	}
            	if (!isOrdered())
        	return JNI.get_float_array_disordered(checkedPeer, length);
                return JNI.get_float_array(checkedPeer, length);
    }
    
 //-- primitive: double --
	
	/**
     * Write a double value to the pointed memory location
     */
	public abstract Pointer<T> setDouble(double value);
	
    /**
     * Write a double value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setDouble(double)} over this method. 
	 */
	public abstract Pointer<T> setDoubleAtOffset(long byteOffset, double value);
	
	/**
     * Write the nth contiguous double value to the pointed memory location.<br>
	   * Equivalent to <code>setDoubleAtOffset(valueIndex * 8, value)</code>.
     * @param valueIndex index of the value to write
     * @param value double value to write
	 */
	public Pointer<T> setDoubleAtIndex(long valueIndex, double value) {
		return setDoubleAtOffset(valueIndex * 8, value);
	}
		
	/**
	 * Write an array of double values of the specified length to the pointed memory location
	 */
    public Pointer<T> setDoubles(double[] values) {
		return setDoublesAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of double values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setDoublesAtOffset(long byteOffset, double[] values) {
        return setDoublesAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of double values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setDoublesAtOffset(long byteOffset, double[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8 * length);
	}
            	if (!isOrdered()) {
        	JNI.set_double_array_disordered(checkedPeer, values, valuesOffset, length);
        	return this;
    	}
        		JNI.set_double_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a double value from the pointed memory location
     */
    public abstract double getDouble();
    
	/**
     * Read a double value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getDouble()} over this method. 
	 */
	public abstract double getDoubleAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous double value from the pointed memory location.<br>
	   * Equivalent to <code>getDoubleAtOffset(valueIndex * 8)</code>.
     * @param valueIndex index of the value to read
	 */
	public double getDoubleAtIndex(long valueIndex) {
		return getDoubleAtOffset(valueIndex * 8);
	}
	
	/**
     * Read an array of double values of the specified length from the pointed memory location
     */
	public double[] getDoubles(int length) {
		return getDoublesAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining double values from the pointed memory location
     */
    public double[] getDoubles() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getDoubles(int length) instead.");
		return getDoubles((int)(validBytes / 8));
    }

	/**
     * Read an array of double values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getDoubles(int)} over this method. 
	 */
	public double[] getDoublesAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8 * length);
	}
            	if (!isOrdered())
        	return JNI.get_double_array_disordered(checkedPeer, length);
                return JNI.get_double_array(checkedPeer, length);
    }
    
 //-- primitive: boolean --
	
	/**
     * Write a boolean value to the pointed memory location
     */
	public abstract Pointer<T> setBoolean(boolean value);
	
    /**
     * Write a boolean value to the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#setBoolean(boolean)} over this method. 
	 */
	public abstract Pointer<T> setBooleanAtOffset(long byteOffset, boolean value);
	
	/**
     * Write the nth contiguous boolean value to the pointed memory location.<br>
	   * Equivalent to <code>setBooleanAtOffset(valueIndex * 1, value)</code>.
     * @param valueIndex index of the value to write
     * @param value boolean value to write
	 */
	public Pointer<T> setBooleanAtIndex(long valueIndex, boolean value) {
		return setBooleanAtOffset(valueIndex * 1, value);
	}
		
	/**
	 * Write an array of boolean values of the specified length to the pointed memory location
	 */
    public Pointer<T> setBooleans(boolean[] values) {
		return setBooleansAtOffset(0, values, 0, values.length);
	}	
	
	/**
	 * Write an array of boolean values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setBooleansAtOffset(long byteOffset, boolean[] values) {
        return setBooleansAtOffset(byteOffset, values, 0, values.length);
    }
    
    /**
	 * Write an array of boolean values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given array offset and for the given length from the provided array.
	 */
	public Pointer<T> setBooleansAtOffset(long byteOffset, boolean[] values, int valuesOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1 * length);
	}
        		JNI.set_boolean_array(checkedPeer, values, valuesOffset, length);
        return this;
	}
	
	/**
     * Read a boolean value from the pointed memory location
     */
    public abstract boolean getBoolean();
    
	/**
     * Read a boolean value from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getBoolean()} over this method. 
	 */
	public abstract boolean getBooleanAtOffset(long byteOffset);
    
	/**
     * Read the nth contiguous boolean value from the pointed memory location.<br>
	   * Equivalent to <code>getBooleanAtOffset(valueIndex * 1)</code>.
     * @param valueIndex index of the value to read
	 */
	public boolean getBooleanAtIndex(long valueIndex) {
		return getBooleanAtOffset(valueIndex * 1);
	}
	
	/**
     * Read an array of boolean values of the specified length from the pointed memory location
     */
	public boolean[] getBooleans(int length) {
		return getBooleansAtOffset(0, length);
    }
    
  
	/**
     * Read the array of remaining boolean values from the pointed memory location
     */
    public boolean[] getBooleans() {
		long validBytes = getValidBytes("Cannot create array if remaining length is not known. Please use getBooleans(int length) instead.");
		return getBooleans((int)(validBytes / 1));
    }

	/**
     * Read an array of boolean values of the specified length from the pointed memory location shifted by a byte offset
     * @deprecated Avoid using the byte offset methods variants unless you know what you're doing (may cause alignment issues). Please favour {@link Pointer#getBooleans(int)} over this method. 
	 */
	public boolean[] getBooleansAtOffset(long byteOffset, int length) {
            		long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1 * length);
	}
                return JNI.get_boolean_array(checkedPeer, length);
    }
    
 //-- primitive (no bool): int --

        /**
	 * Read int values into the specified destination array from the pointed memory location
	 */
	public void getInts(int[] dest) {
    		getIntBuffer().get(dest);
    }
    
    /**
	 * Read int values into the specified destination buffer from the pointed memory location
	 */
	public void getInts(IntBuffer dest) {
    		dest.duplicate().put(getIntBuffer());
    }
    
    /**
	 * Read length int values into the specified destination array from the pointed memory location shifted by a byte offset, storing values after the provided destination offset.
	 */
	public void getIntsAtOffset(long byteOffset, int[] dest, int destOffset, int length) {
    		getIntBufferAtOffset(byteOffset, length).get(dest, destOffset, length);
    }
        
	/**
	 * Write a buffer of int values of the specified length to the pointed memory location
	 */
    public Pointer<T> setInts(IntBuffer values) {
		return setIntsAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of int values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setIntsAtOffset(long byteOffset, IntBuffer values) {
		return setIntsAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of int values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setIntsAtOffset(long byteOffset, IntBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
		    	if (values.isDirect()) {
            long len = length * 4, off = valuesOffset * 4;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= 4;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4 * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else if (values.isReadOnly()) {
            getIntBufferAtOffset(byteOffset, length).put(values.duplicate());
        } 
                else {
            setIntsAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        /**
	 * Get a direct buffer of int values of the specified length that points to this pointer's target memory location
	 */
	public IntBuffer getIntBuffer(long length) {
		return getIntBufferAtOffset(0, length);
	}
	
	/**
	 * Get a direct buffer of int values that points to this pointer's target memory locations
	 */
	public IntBuffer getIntBuffer() {
		long validBytes = getValidBytes("Cannot create buffer if remaining length is not known. Please use getIntBuffer(long length) instead.");
		return getIntBufferAtOffset(0, validBytes / 4);
	}
	
	/**
	 * Get a direct buffer of int values of the specified length that points to this pointer's target memory location shifted by a byte offset
	 */
	public IntBuffer getIntBufferAtOffset(long byteOffset, long length) {
        long blen = 4 * length;
        	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + blen) > validEnd
	   )) {
		invalidPeer(checkedPeer, blen);
	}
        ByteBuffer buffer = JNI.newDirectByteBuffer(checkedPeer, blen);
        buffer.order(order()); // mutates buffer order
                return buffer.asIntBuffer();
            }
        
 //-- primitive (no bool): long --

        /**
	 * Read long values into the specified destination array from the pointed memory location
	 */
	public void getLongs(long[] dest) {
    		getLongBuffer().get(dest);
    }
    
    /**
	 * Read long values into the specified destination buffer from the pointed memory location
	 */
	public void getLongs(LongBuffer dest) {
    		dest.duplicate().put(getLongBuffer());
    }
    
    /**
	 * Read length long values into the specified destination array from the pointed memory location shifted by a byte offset, storing values after the provided destination offset.
	 */
	public void getLongsAtOffset(long byteOffset, long[] dest, int destOffset, int length) {
    		getLongBufferAtOffset(byteOffset, length).get(dest, destOffset, length);
    }
        
	/**
	 * Write a buffer of long values of the specified length to the pointed memory location
	 */
    public Pointer<T> setLongs(LongBuffer values) {
		return setLongsAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of long values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setLongsAtOffset(long byteOffset, LongBuffer values) {
		return setLongsAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of long values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setLongsAtOffset(long byteOffset, LongBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
		    	if (values.isDirect()) {
            long len = length * 8, off = valuesOffset * 8;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= 8;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8 * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else if (values.isReadOnly()) {
            getLongBufferAtOffset(byteOffset, length).put(values.duplicate());
        } 
                else {
            setLongsAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        /**
	 * Get a direct buffer of long values of the specified length that points to this pointer's target memory location
	 */
	public LongBuffer getLongBuffer(long length) {
		return getLongBufferAtOffset(0, length);
	}
	
	/**
	 * Get a direct buffer of long values that points to this pointer's target memory locations
	 */
	public LongBuffer getLongBuffer() {
		long validBytes = getValidBytes("Cannot create buffer if remaining length is not known. Please use getLongBuffer(long length) instead.");
		return getLongBufferAtOffset(0, validBytes / 8);
	}
	
	/**
	 * Get a direct buffer of long values of the specified length that points to this pointer's target memory location shifted by a byte offset
	 */
	public LongBuffer getLongBufferAtOffset(long byteOffset, long length) {
        long blen = 8 * length;
        	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + blen) > validEnd
	   )) {
		invalidPeer(checkedPeer, blen);
	}
        ByteBuffer buffer = JNI.newDirectByteBuffer(checkedPeer, blen);
        buffer.order(order()); // mutates buffer order
                return buffer.asLongBuffer();
            }
        
 //-- primitive (no bool): short --

        /**
	 * Read short values into the specified destination array from the pointed memory location
	 */
	public void getShorts(short[] dest) {
    		getShortBuffer().get(dest);
    }
    
    /**
	 * Read short values into the specified destination buffer from the pointed memory location
	 */
	public void getShorts(ShortBuffer dest) {
    		dest.duplicate().put(getShortBuffer());
    }
    
    /**
	 * Read length short values into the specified destination array from the pointed memory location shifted by a byte offset, storing values after the provided destination offset.
	 */
	public void getShortsAtOffset(long byteOffset, short[] dest, int destOffset, int length) {
    		getShortBufferAtOffset(byteOffset, length).get(dest, destOffset, length);
    }
        
	/**
	 * Write a buffer of short values of the specified length to the pointed memory location
	 */
    public Pointer<T> setShorts(ShortBuffer values) {
		return setShortsAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of short values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setShortsAtOffset(long byteOffset, ShortBuffer values) {
		return setShortsAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of short values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setShortsAtOffset(long byteOffset, ShortBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
		    	if (values.isDirect()) {
            long len = length * 2, off = valuesOffset * 2;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= 2;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 2 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 2 * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else if (values.isReadOnly()) {
            getShortBufferAtOffset(byteOffset, length).put(values.duplicate());
        } 
                else {
            setShortsAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        /**
	 * Get a direct buffer of short values of the specified length that points to this pointer's target memory location
	 */
	public ShortBuffer getShortBuffer(long length) {
		return getShortBufferAtOffset(0, length);
	}
	
	/**
	 * Get a direct buffer of short values that points to this pointer's target memory locations
	 */
	public ShortBuffer getShortBuffer() {
		long validBytes = getValidBytes("Cannot create buffer if remaining length is not known. Please use getShortBuffer(long length) instead.");
		return getShortBufferAtOffset(0, validBytes / 2);
	}
	
	/**
	 * Get a direct buffer of short values of the specified length that points to this pointer's target memory location shifted by a byte offset
	 */
	public ShortBuffer getShortBufferAtOffset(long byteOffset, long length) {
        long blen = 2 * length;
        	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + blen) > validEnd
	   )) {
		invalidPeer(checkedPeer, blen);
	}
        ByteBuffer buffer = JNI.newDirectByteBuffer(checkedPeer, blen);
        buffer.order(order()); // mutates buffer order
                return buffer.asShortBuffer();
            }
        
 //-- primitive (no bool): byte --

        /**
	 * Read byte values into the specified destination array from the pointed memory location
	 */
	public void getBytes(byte[] dest) {
    		getByteBuffer().get(dest);
    }
    
    /**
	 * Read byte values into the specified destination buffer from the pointed memory location
	 */
	public void getBytes(ByteBuffer dest) {
    		dest.duplicate().put(getByteBuffer());
    }
    
    /**
	 * Read length byte values into the specified destination array from the pointed memory location shifted by a byte offset, storing values after the provided destination offset.
	 */
	public void getBytesAtOffset(long byteOffset, byte[] dest, int destOffset, int length) {
    		getByteBufferAtOffset(byteOffset, length).get(dest, destOffset, length);
    }
        
	/**
	 * Write a buffer of byte values of the specified length to the pointed memory location
	 */
    public Pointer<T> setBytes(ByteBuffer values) {
		return setBytesAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of byte values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setBytesAtOffset(long byteOffset, ByteBuffer values) {
		return setBytesAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of byte values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setBytesAtOffset(long byteOffset, ByteBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
		    	if (values.isDirect()) {
            long len = length * 1, off = valuesOffset * 1;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= 1;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1 * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else if (values.isReadOnly()) {
            getByteBufferAtOffset(byteOffset, length).put(values.duplicate());
        } 
                else {
            setBytesAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        /**
	 * Get a direct buffer of byte values of the specified length that points to this pointer's target memory location
	 */
	public ByteBuffer getByteBuffer(long length) {
		return getByteBufferAtOffset(0, length);
	}
	
	/**
	 * Get a direct buffer of byte values that points to this pointer's target memory locations
	 */
	public ByteBuffer getByteBuffer() {
		long validBytes = getValidBytes("Cannot create buffer if remaining length is not known. Please use getByteBuffer(long length) instead.");
		return getByteBufferAtOffset(0, validBytes / 1);
	}
	
	/**
	 * Get a direct buffer of byte values of the specified length that points to this pointer's target memory location shifted by a byte offset
	 */
	public ByteBuffer getByteBufferAtOffset(long byteOffset, long length) {
        long blen = 1 * length;
        	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + blen) > validEnd
	   )) {
		invalidPeer(checkedPeer, blen);
	}
        ByteBuffer buffer = JNI.newDirectByteBuffer(checkedPeer, blen);
        buffer.order(order()); // mutates buffer order
                return buffer;
            }
        
 //-- primitive (no bool): char --

        
	/**
	 * Write a buffer of char values of the specified length to the pointed memory location
	 */
    public Pointer<T> setChars(CharBuffer values) {
		return setCharsAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of char values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setCharsAtOffset(long byteOffset, CharBuffer values) {
		return setCharsAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of char values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setCharsAtOffset(long byteOffset, CharBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
				if (Platform.WCHAR_T_SIZE == 4) {
			for (int i = 0; i < length; i++)
				setCharAtOffset(byteOffset + i, values.get((int)(valuesOffset + i)));
			return this;
		}
		    	if (values.isDirect()) {
            long len = length * Platform.WCHAR_T_SIZE, off = valuesOffset * Platform.WCHAR_T_SIZE;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= Platform.WCHAR_T_SIZE;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else {
            setCharsAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        
 //-- primitive (no bool): float --

        /**
	 * Read float values into the specified destination array from the pointed memory location
	 */
	public void getFloats(float[] dest) {
    		getFloatBuffer().get(dest);
    }
    
    /**
	 * Read float values into the specified destination buffer from the pointed memory location
	 */
	public void getFloats(FloatBuffer dest) {
    		dest.duplicate().put(getFloatBuffer());
    }
    
    /**
	 * Read length float values into the specified destination array from the pointed memory location shifted by a byte offset, storing values after the provided destination offset.
	 */
	public void getFloatsAtOffset(long byteOffset, float[] dest, int destOffset, int length) {
    		getFloatBufferAtOffset(byteOffset, length).get(dest, destOffset, length);
    }
        
	/**
	 * Write a buffer of float values of the specified length to the pointed memory location
	 */
    public Pointer<T> setFloats(FloatBuffer values) {
		return setFloatsAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of float values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setFloatsAtOffset(long byteOffset, FloatBuffer values) {
		return setFloatsAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of float values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setFloatsAtOffset(long byteOffset, FloatBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
		    	if (values.isDirect()) {
            long len = length * 4, off = valuesOffset * 4;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= 4;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 4 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 4 * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else if (values.isReadOnly()) {
            getFloatBufferAtOffset(byteOffset, length).put(values.duplicate());
        } 
                else {
            setFloatsAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        /**
	 * Get a direct buffer of float values of the specified length that points to this pointer's target memory location
	 */
	public FloatBuffer getFloatBuffer(long length) {
		return getFloatBufferAtOffset(0, length);
	}
	
	/**
	 * Get a direct buffer of float values that points to this pointer's target memory locations
	 */
	public FloatBuffer getFloatBuffer() {
		long validBytes = getValidBytes("Cannot create buffer if remaining length is not known. Please use getFloatBuffer(long length) instead.");
		return getFloatBufferAtOffset(0, validBytes / 4);
	}
	
	/**
	 * Get a direct buffer of float values of the specified length that points to this pointer's target memory location shifted by a byte offset
	 */
	public FloatBuffer getFloatBufferAtOffset(long byteOffset, long length) {
        long blen = 4 * length;
        	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + blen) > validEnd
	   )) {
		invalidPeer(checkedPeer, blen);
	}
        ByteBuffer buffer = JNI.newDirectByteBuffer(checkedPeer, blen);
        buffer.order(order()); // mutates buffer order
                return buffer.asFloatBuffer();
            }
        
 //-- primitive (no bool): double --

        /**
	 * Read double values into the specified destination array from the pointed memory location
	 */
	public void getDoubles(double[] dest) {
    		getDoubleBuffer().get(dest);
    }
    
    /**
	 * Read double values into the specified destination buffer from the pointed memory location
	 */
	public void getDoubles(DoubleBuffer dest) {
    		dest.duplicate().put(getDoubleBuffer());
    }
    
    /**
	 * Read length double values into the specified destination array from the pointed memory location shifted by a byte offset, storing values after the provided destination offset.
	 */
	public void getDoublesAtOffset(long byteOffset, double[] dest, int destOffset, int length) {
    		getDoubleBufferAtOffset(byteOffset, length).get(dest, destOffset, length);
    }
        
	/**
	 * Write a buffer of double values of the specified length to the pointed memory location
	 */
    public Pointer<T> setDoubles(DoubleBuffer values) {
		return setDoublesAtOffset(0, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of double values of the specified length to the pointed memory location shifted by a byte offset
	 */
	public Pointer<T> setDoublesAtOffset(long byteOffset, DoubleBuffer values) {
		return setDoublesAtOffset(byteOffset, values, 0, values.capacity());
	}

    /**
	 * Write a buffer of double values of the specified length to the pointed memory location shifted by a byte offset, reading values at the given buffer offset and for the given length from the provided buffer.
	 */
	public Pointer<T> setDoublesAtOffset(long byteOffset, DoubleBuffer values, long valuesOffset, long length) {
        if (values == null)
			throw new IllegalArgumentException("Null values");
		    	if (values.isDirect()) {
            long len = length * 8, off = valuesOffset * 8;
            long cap = JNI.getDirectBufferCapacity(values);
            // HACK (TODO?) the JNI spec says size is in bytes, but in practice on mac os x it's in elements !!!
            cap *= 8;
		
            if (cap < off + len)
                throw new IndexOutOfBoundsException("The provided buffer has a capacity (" + cap + " bytes) smaller than the requested write operation (" + len + " bytes starting at byte offset " + off + ")");
            
            
            	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 8 * length) > validEnd
	   )) {
		invalidPeer(checkedPeer, 8 * length);
	}
			JNI.memcpy(checkedPeer, JNI.getDirectBufferAddress(values) + off, len);
        }
                else if (values.isReadOnly()) {
            getDoubleBufferAtOffset(byteOffset, length).put(values.duplicate());
        } 
                else {
            setDoublesAtOffset(byteOffset, values.array(), (int)(values.arrayOffset() + valuesOffset), (int)length);
        }
        return this;
    }
    
        /**
	 * Get a direct buffer of double values of the specified length that points to this pointer's target memory location
	 */
	public DoubleBuffer getDoubleBuffer(long length) {
		return getDoubleBufferAtOffset(0, length);
	}
	
	/**
	 * Get a direct buffer of double values that points to this pointer's target memory locations
	 */
	public DoubleBuffer getDoubleBuffer() {
		long validBytes = getValidBytes("Cannot create buffer if remaining length is not known. Please use getDoubleBuffer(long length) instead.");
		return getDoubleBufferAtOffset(0, validBytes / 8);
	}
	
	/**
	 * Get a direct buffer of double values of the specified length that points to this pointer's target memory location shifted by a byte offset
	 */
	public DoubleBuffer getDoubleBufferAtOffset(long byteOffset, long length) {
        long blen = 8 * length;
        	long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + blen) > validEnd
	   )) {
		invalidPeer(checkedPeer, blen);
	}
        ByteBuffer buffer = JNI.newDirectByteBuffer(checkedPeer, blen);
        buffer.order(order()); // mutates buffer order
                return buffer.asDoubleBuffer();
            }
        

	/**
	 * Type of a native character string.<br>
	 * In the native world, there are several ways to represent a string.<br>
	 * See {@link Pointer#getStringAtOffset(long, StringType, Charset)} and {@link Pointer#setStringAtOffset(long, String, StringType, Charset)}
	 */
    public enum StringType {
        /**
		 * C strings (a.k.a "NULL-terminated strings") have no size limit and are the most used strings in the C world.
		 * They are stored with the bytes of the string (using either a single-byte encoding such as ASCII, ISO-8859 or windows-1252 or a C-string compatible multi-byte encoding, such as UTF-8), followed with a zero byte that indicates the end of the string.<br>
		 * Corresponding C types : {@code char* }, {@code const char* }, {@code LPCSTR }<br>
		 * Corresponding Pascal type : {@code PChar }<br>
		 * See {@link Pointer#pointerToCString(String)}, {@link Pointer#getCString()} and {@link Pointer#setCString(String)}
		 */
		C(false, true),
		/**
		 * Wide C strings are stored as C strings (see {@link StringType#C}) except they are composed of shorts instead of bytes (and are ended by one zero short value = two zero byte values). 
		 * This allows the use of two-bytes encodings, which is why this kind of strings is often found in modern Unicode-aware system APIs.<br>
		 * Corresponding C types : {@code wchar_t* }, {@code const wchar_t* }, {@code LPCWSTR }<br>
		 * See {@link Pointer#pointerToWideCString(String)}, {@link Pointer#getWideCString()} and {@link Pointer#setWideCString(String)}
		 */
        WideC(true, true),
    		/**
		 * Pascal strings can be up to 255 characters long.<br>
		 * They are stored with a first byte that indicates the length of the string, followed by the ascii or extended ascii chars of the string (no support for multibyte encoding).<br>
		 * They are often used in very old Mac OS programs and / or Pascal programs.<br>
		 * Usual corresponding C types : {@code unsigned char* } and {@code const unsigned char* }<br>
		 * Corresponding Pascal type : {@code ShortString } (see <a href="http://www.codexterity.com/delphistrings.htm">http://www.codexterity.com/delphistrings.htm</a>)<br>
		 * See {@link Pointer#pointerToString(String, StringType, Charset)}, {@link Pointer#getString(StringType)}, {@link Pointer#setString(String, StringType)}, 
		 */
        PascalShort(false, true),
		/**
		 * Wide Pascal strings are ref-counted unicode strings that look like WideC strings but are prepended with a ref count and length (both 32 bits ints).<br>
		 * They are the current default in Delphi (2010).<br>
		 * Corresponding Pascal type : {@code WideString } (see <a href="http://www.codexterity.com/delphistrings.htm">http://www.codexterity.com/delphistrings.htm</a>)<br>
		 * See {@link Pointer#pointerToString(String, StringType, Charset)}, {@link Pointer#getString(StringType)}, {@link Pointer#setString(String, StringType)}, 
		 */
        PascalWide(true, true),
        /**
		 * Pascal ANSI strings are ref-counted single-byte strings that look like C strings but are prepended with a ref count and length (both 32 bits ints).<br>
		 * Corresponding Pascal type : {@code AnsiString } (see <a href="http://www.codexterity.com/delphistrings.htm">http://www.codexterity.com/delphistrings.htm</a>)<br>
		 * See {@link Pointer#pointerToString(String, StringType, Charset)}, {@link Pointer#getString(StringType)}, {@link Pointer#setString(String, StringType)}, 
		 */
        PascalAnsi(false, true),
        /**
         * Microsoft's BSTR strings, used in COM, OLE, MS.NET Interop and MS.NET Automation functions.<br>
         * See <a href="http://msdn.microsoft.com/en-us/library/ms221069.aspx">http://msdn.microsoft.com/en-us/library/ms221069.aspx</a> for more details.<br>
         * See {@link Pointer#pointerToString(String, StringType, Charset)}, {@link Pointer#getString(StringType)}, {@link Pointer#setString(String, StringType)}, 
		 */
        BSTR(true, true),
        /**
         * STL strings have compiler- and STL library-specific implementations and memory layouts.<br>
         * BridJ support reading and writing to / from pointers to most implementation's STL strings, though.
         * See {@link Pointer#pointerToString(String, StringType, Charset)}, {@link Pointer#getString(StringType)}, {@link Pointer#setString(String, StringType)}, 
		 */
		STL(false, false),
        /**
         * STL wide strings have compiler- and STL library-specific implementations and memory layouts.<br>
         * BridJ supports reading and writing to / from pointers to most implementation's STL strings, though.
         * See {@link Pointer#pointerToString(String, StringType, Charset)}, {@link Pointer#getString(StringType)}, {@link Pointer#setString(String, StringType)}, 
		 */
		WideSTL(true, false);
        //MFCCString,
        //CComBSTR,
        //_bstr_t
        
        final boolean isWide, canCreate;
        StringType(boolean isWide, boolean canCreate) {
			this.isWide = isWide;
			this.canCreate = canCreate;
        }
        
    }
	
    private static void notAString(StringType type, String reason) {
    		throw new RuntimeException("There is no " + type + " String here ! (" + reason + ")");
    }
    
    protected void checkIntRefCount(StringType type, long byteOffset) {
    		int refCount = getIntAtOffset(byteOffset);
		if (refCount <= 0)
			notAString(type, "invalid refcount: " + refCount);
    }
    
	/**
	 * Read a native string from the pointed memory location using the default charset.<br>
	 * See {@link Pointer#getStringAtOffset(long, StringType, Charset)} for more options.
	 * @param type Type of the native String to read. See {@link StringType} for details on the supported types.
	 * @return string read from native memory
	 */
	public String getString(StringType type) {
		return getStringAtOffset(0, type, null);
	}
	
	/**
	 * Read a native string from the pointed memory location, using the provided charset or the system's default if not provided.
	 * See {@link Pointer#getStringAtOffset(long, StringType, Charset)} for more options.
	 * @param type Type of the native String to read. See {@link StringType} for details on the supported types.
	 * @param charset Character set used to convert bytes to String characters. If null, {@link Charset#defaultCharset()} will be used
	 * @return string read from native memory
	 */
	public String getString(StringType type, Charset charset) {
		return getStringAtOffset(0, type, charset);
	}
	 
	
	String getSTLStringAtOffset(long byteOffset, StringType type, Charset charset) {
		// Assume the following layout :
		// - fixed buff of 16 chars
		// - ptr to dynamic array if the string is bigger
		// - size of the string (size_t)
		// - max allowed size of the string without the need for reallocation
		boolean wide = type == StringType.WideSTL;
		
		int fixedBuffLength = 16;
		int fixedBuffSize = wide ? fixedBuffLength * Platform.WCHAR_T_SIZE : fixedBuffLength;
		long length = getSizeTAtOffset(byteOffset + fixedBuffSize + Pointer.SIZE);
		long pOff;
		Pointer<?> p;
		if (length < fixedBuffLength - 1) {
			pOff = byteOffset;
			p = this;
		} else {
			pOff = 0;
			p = getPointerAtOffset(byteOffset + fixedBuffSize + Pointer.SIZE);
		}
		int endChar = wide ? p.getCharAtOffset(pOff + length * Platform.WCHAR_T_SIZE) : p.getByteAtOffset(pOff + length);
		if (endChar != 0)
			notAString(type, "STL string format is not recognized : did not find a NULL char at the expected end of string of expected length " + length);
		return p.getStringAtOffset(pOff, wide ? StringType.WideC : StringType.C, charset);
	}
	
	static <U> Pointer<U> setSTLString(Pointer<U> pointer, long byteOffset, String s, StringType type, Charset charset) {
		boolean wide = type == StringType.WideSTL;
		
		int fixedBuffLength = 16;
		int fixedBuffSize = wide ? fixedBuffLength * Platform.WCHAR_T_SIZE : fixedBuffLength;
		long lengthOffset = byteOffset + fixedBuffSize + Pointer.SIZE;
		long capacityOffset = lengthOffset + Pointer.SIZE;
		
		long length = s.length();
		if (pointer == null)// { && length > fixedBuffLength - 1)
			throw new UnsupportedOperationException("Cannot create STL strings (yet)");
		
		long currentLength = pointer.getSizeTAtOffset(lengthOffset);
		long currentCapacity = pointer.getSizeTAtOffset(capacityOffset);
		
		if (currentLength < 0 || currentCapacity < 0 || currentLength > currentCapacity)
			notAString(type, "STL string format not recognized : currentLength = " + currentLength + ", currentCapacity = " + currentCapacity);
		
		if (length > currentCapacity)
			throw new RuntimeException("The target STL string is not large enough to write a string of length " + length + " (current capacity = " + currentCapacity + ")");
		
		pointer.setSizeTAtOffset(lengthOffset, length);
		
		long pOff;
		Pointer<?> p;
		if (length < fixedBuffLength - 1) {
			pOff = byteOffset;
			p = pointer;
		} else {
			pOff = 0;
			p = pointer.getPointerAtOffset(byteOffset + fixedBuffSize + SizeT.SIZE);
		}
		
		int endChar = wide ? p.getCharAtOffset(pOff + currentLength * Platform.WCHAR_T_SIZE) : p.getByteAtOffset(pOff + currentLength);
		if (endChar != 0)
			notAString(type, "STL string format is not recognized : did not find a NULL char at the expected end of string of expected length " + currentLength);
		
		p.setStringAtOffset(pOff, s, wide ? StringType.WideC : StringType.C, charset);
		return pointer;
	}
    
	
	/**
	 * Read a native string from the pointed memory location shifted by a byte offset, using the provided charset or the system's default if not provided.
	 * @param byteOffset
	 * @param charset Character set used to convert bytes to String characters. If null, {@link Charset#defaultCharset()} will be used
	 * @param type Type of the native String to read. See {@link StringType} for details on the supported types.
	 * @return string read from native memory
	 */
	public String getStringAtOffset(long byteOffset, StringType type, Charset charset) {
        try {
			long len;
			
			switch (type) {
			case PascalShort:
				len = getByteAtOffset(byteOffset) & 0xff;
				return new String(getBytesAtOffset(byteOffset + 1, safeIntCast(len)), charset(charset));
			case PascalWide:
				checkIntRefCount(type, byteOffset - 8);
			case BSTR:
				len = getIntAtOffset(byteOffset - 4);
				if (len < 0 || ((len & 1) == 1))
					notAString(type, "invalid byte length: " + len);
				//len = wcslen(byteOffset);
				if (getCharAtOffset(byteOffset + len) != 0)
					notAString(type, "no null short after the " + len + " declared bytes");
				return new String(getCharsAtOffset(byteOffset, safeIntCast(len / Platform.WCHAR_T_SIZE)));
			case PascalAnsi:
				checkIntRefCount(type, byteOffset - 8);
				len = getIntAtOffset(byteOffset - 4);
				if (len < 0)
					notAString(type, "invalid byte length: " + len);
				if (getByteAtOffset(byteOffset + len) != 0)
					notAString(type, "no null short after the " + len + " declared bytes");
				return new String(getBytesAtOffset(byteOffset, safeIntCast(len)), charset(charset));
			case C:
				len = strlen(byteOffset);
				return new String(getBytesAtOffset(byteOffset, safeIntCast(len)), charset(charset));
			case WideC:
				len = wcslen(byteOffset);
				return new String(getCharsAtOffset(byteOffset, safeIntCast(len)));
			case STL:
			case WideSTL:
				return getSTLStringAtOffset(byteOffset, type, charset);
			default:
				throw new RuntimeException("Unhandled string type : " + type);
			}
		} catch (UnsupportedEncodingException ex) {
            throwUnexpected(ex);
            return null;
        }
	}

	/**
	 * Write a native string to the pointed memory location using the default charset.<br>
	 * See {@link Pointer#setStringAtOffset(long, String, StringType, Charset)} for more options.
	 * @param s string to write
	 * @param type Type of the native String to write. See {@link StringType} for details on the supported types.
	 * @return this
	 */
	public Pointer<T> setString(String s, StringType type) {
		return setString(this, 0, s, type, null);
	}
	
	
    /**
	 * Write a native string to the pointed memory location shifted by a byte offset, using the provided charset or the system's default if not provided.
	 * @param byteOffset
	 * @param s string to write
	 * @param charset Character set used to convert String characters to bytes. If null, {@link Charset#defaultCharset()} will be used
	 * @param type Type of the native String to write. See {@link StringType} for details on the supported types.
	 * @return this
	 */
	public Pointer<T> setStringAtOffset(long byteOffset, String s, StringType type, Charset charset) {
		return setString(this, byteOffset, s, type, charset);
	}
	
	private static String charset(Charset charset) {
		return (charset == null ? Charset.defaultCharset() : charset).name();
	}
			
	static <U> Pointer<U> setString(Pointer<U> pointer, long byteOffset, String s, StringType type, Charset charset) {
        try {
			if (s == null)
				return null;
			
			byte[] bytes;
			char[] chars;
			int bytesCount, headerBytes;
			int headerShift;
			
			switch (type) {
			case PascalShort:
				bytes = s.getBytes(charset(charset));
				bytesCount = bytes.length;
				if (pointer == null)
					pointer = (Pointer<U>)allocateBytes(bytesCount + 1);
				if (bytesCount > 255)
					throw new IllegalArgumentException("Pascal strings cannot be more than 255 chars long (tried to write string of byte length " + bytesCount + ")");
				pointer.setByteAtOffset(byteOffset, (byte)bytesCount);
				pointer.setBytesAtOffset(byteOffset + 1, bytes, 0, bytesCount);
				break;
			case C:
				bytes = s.getBytes(charset(charset));
				bytesCount = bytes.length;
				if (pointer == null)
					pointer = (Pointer<U>)allocateBytes(bytesCount + 1);
				pointer.setBytesAtOffset(byteOffset, bytes, 0, bytesCount);
				pointer.setByteAtOffset(byteOffset + bytesCount, (byte)0);
				break;
			case WideC:
				chars = s.toCharArray();
				bytesCount = chars.length * Platform.WCHAR_T_SIZE;
				if (pointer == null)
					pointer = (Pointer<U>)allocateChars(bytesCount + 2);
				pointer.setCharsAtOffset(byteOffset, chars);
				pointer.setCharAtOffset(byteOffset + bytesCount, (char)0);
				break;
			case PascalWide:
				headerBytes = 8;
				chars = s.toCharArray();
				bytesCount = chars.length * Platform.WCHAR_T_SIZE;
				if (pointer == null) {
					pointer = (Pointer<U>)allocateChars(headerBytes + bytesCount + 2);
					byteOffset = headerShift = headerBytes;
				} else
					headerShift = 0;
				pointer.setIntAtOffset(byteOffset - 8, 1); // refcount
				pointer.setIntAtOffset(byteOffset - 4, bytesCount); // length header
				pointer.setCharsAtOffset(byteOffset, chars);
				pointer.setCharAtOffset(byteOffset + bytesCount, (char)0);
				// Return a pointer to the WideC string-compatible part of the Pascal WideString
				return (Pointer<U>)pointer.offset(headerShift);
			case PascalAnsi:
				headerBytes = 8;
				bytes = s.getBytes(charset(charset));
				bytesCount = bytes.length;
				if (pointer == null) {
					pointer = (Pointer<U>)allocateBytes(headerBytes + bytesCount + 1);
					byteOffset = headerShift = headerBytes;
				} else
					headerShift = 0;
				pointer.setIntAtOffset(byteOffset - 8, 1); // refcount
				pointer.setIntAtOffset(byteOffset - 4, bytesCount); // length header
				pointer.setBytesAtOffset(byteOffset, bytes);
				pointer.setByteAtOffset(byteOffset + bytesCount, (byte)0);
				// Return a pointer to the WideC string-compatible part of the Pascal WideString
				return (Pointer<U>)pointer.offset(headerShift);
			case BSTR:
				headerBytes = 4;
				chars = s.toCharArray();
				bytesCount = chars.length * Platform.WCHAR_T_SIZE;
				if (pointer == null) {
					pointer = (Pointer<U>)allocateChars(headerBytes + bytesCount + 2);
					byteOffset = headerShift = headerBytes;
				} else
					headerShift = 0;
				pointer.setIntAtOffset(byteOffset - 4, bytesCount); // length header IN BYTES
				pointer.setCharsAtOffset(byteOffset, chars);
				pointer.setCharAtOffset(byteOffset + bytesCount, (char)0);
				// Return a pointer to the WideC string-compatible part of the Pascal WideString
				return (Pointer<U>)pointer.offset(headerShift);
			case STL:
			case WideSTL:
				return setSTLString(pointer, byteOffset, s, type, charset);
			default:
				throw new RuntimeException("Unhandled string type : " + type);
			}
	
			return (Pointer<U>)pointer;
		} catch (UnsupportedEncodingException ex) {
            throwUnexpected(ex);
            return null;
        }
    }
	
    /**
     * Allocate memory and write a string to it, using the system's default charset to convert the string (See {@link StringType} for details on the supported types).<br>
	 * See {@link Pointer#setString(String, StringType)}, {@link Pointer#getString(StringType)}.
	 * @param charset Character set used to convert String characters to bytes. If null, {@link Charset#defaultCharset()} will be used
	 * @param type Type of the native String to create.
	 */
	public static Pointer<?> pointerToString(String string, StringType type, Charset charset) {
		return setString(null, 0, string, type, charset);
	}
	

    /**
     * Allocate memory and write a C string to it, using the system's default charset to convert the string.  (see {@link StringType#C}).<br>
	 * See {@link Pointer#setCString(String)}, {@link Pointer#getCString()}.<br>
	 * See {@link Pointer#pointerToString(String, StringType, Charset)} for choice of the String type or Charset.
	 */
	 public static Pointer<Byte> pointerToCString(String string) {
		return setString(null, 0, string, StringType.C, null);
	}
	
	/**
	 * Allocate an array of pointers to strings.
	 */
    public static Pointer<Pointer<Byte>> pointerToCStrings(final String... strings) {
    	if (strings == null)
    		return null;
        final int len = strings.length;
        final Pointer<Byte>[] pointers = (Pointer<Byte>[])new Pointer[len];
        Pointer<Pointer<Byte>> mem = allocateArray((PointerIO<Pointer<Byte>>)(PointerIO)PointerIO.getPointerInstance(Byte.class), len, new Releaser() {
        	//@Override
        	public void release(Pointer<?> p) {
        		Pointer<Pointer<Byte>> mem = (Pointer<Pointer<Byte>>)p;
        		for (int i = 0; i < len; i++) {
        			Pointer<Byte> pp = pointers[i];
        			if (pp != null)
        				pp.release();
        		}
        }});
        for (int i = 0; i < len; i++)
            mem.set(i, pointers[i] = pointerToCString(strings[i]));

		return mem;
    }
    
    /**
     * Allocate memory and write a WideC string to it, using the system's default charset to convert the string.  (see {@link StringType#WideC}).<br>
	 * See {@link Pointer#setWideCString(String)}, {@link Pointer#getWideCString()}.<br>
	 * See {@link Pointer#pointerToString(String, StringType, Charset)} for choice of the String type or Charset.
	 */
	 public static Pointer<Character> pointerToWideCString(String string) {
		return setString(null, 0, string, StringType.WideC, null);
	}
	
	/**
	 * Allocate an array of pointers to strings.
	 */
    public static Pointer<Pointer<Character>> pointerToWideCStrings(final String... strings) {
    	if (strings == null)
    		return null;
        final int len = strings.length;
        final Pointer<Character>[] pointers = (Pointer<Character>[])new Pointer[len];
        Pointer<Pointer<Character>> mem = allocateArray((PointerIO<Pointer<Character>>)(PointerIO)PointerIO.getPointerInstance(Character.class), len, new Releaser() {
        	//@Override
        	public void release(Pointer<?> p) {
        		Pointer<Pointer<Character>> mem = (Pointer<Pointer<Character>>)p;
        		for (int i = 0; i < len; i++) {
        			Pointer<Character> pp = pointers[i];
        			if (pp != null)
        				pp.release();
        		}
        }});
        for (int i = 0; i < len; i++)
            mem.set(i, pointers[i] = pointerToWideCString(strings[i]));

		return mem;
    }
    

	
//-- StringType: C --

	/**
	 * Read a C string using the default charset from the pointed memory location (see {@link StringType#C}).<br>
	 * See {@link Pointer#getCStringAtOffset(long)}, {@link Pointer#getString(StringType)} and {@link Pointer#getStringAtOffset(long, StringType, Charset)} for more options
	 */
	public String getCString() {
		return getCStringAtOffset(0);
	}
	
	/**
	 * Read a C string using the default charset from the pointed memory location shifted by a byte offset (see {@link StringType#C}).<br>
	 * See {@link Pointer#getStringAtOffset(long, StringType, Charset)} for more options
	 */
	public String getCStringAtOffset(long byteOffset) {
		return getStringAtOffset(byteOffset, StringType.C, null);
	}
	
	/**
	 * Write a C string using the default charset to the pointed memory location (see {@link StringType#C}).<br>
	 * See {@link Pointer#setCStringAtOffset(long, String)} and {@link Pointer#setStringAtOffset(long, String, StringType, Charset)} for more options
	 */
	public Pointer<T> setCString(String s) {
        return setCStringAtOffset(0, s);
    }
    /**
	 * Write a C string using the default charset to the pointed memory location shifted by a byte offset (see {@link StringType#C}).<br>
	 * See {@link Pointer#setStringAtOffset(long, String, StringType, Charset)} for more options
	 */
	public Pointer<T> setCStringAtOffset(long byteOffset, String s) {
        return setStringAtOffset(byteOffset, s, StringType.C, null);
    }
	
//-- StringType: WideC --

	/**
	 * Read a WideC string using the default charset from the pointed memory location (see {@link StringType#WideC}).<br>
	 * See {@link Pointer#getWideCStringAtOffset(long)}, {@link Pointer#getString(StringType)} and {@link Pointer#getStringAtOffset(long, StringType, Charset)} for more options
	 */
	public String getWideCString() {
		return getWideCStringAtOffset(0);
	}
	
	/**
	 * Read a WideC string using the default charset from the pointed memory location shifted by a byte offset (see {@link StringType#WideC}).<br>
	 * See {@link Pointer#getStringAtOffset(long, StringType, Charset)} for more options
	 */
	public String getWideCStringAtOffset(long byteOffset) {
		return getStringAtOffset(byteOffset, StringType.WideC, null);
	}
	
	/**
	 * Write a WideC string using the default charset to the pointed memory location (see {@link StringType#WideC}).<br>
	 * See {@link Pointer#setWideCStringAtOffset(long, String)} and {@link Pointer#setStringAtOffset(long, String, StringType, Charset)} for more options
	 */
	public Pointer<T> setWideCString(String s) {
        return setWideCStringAtOffset(0, s);
    }
    /**
	 * Write a WideC string using the default charset to the pointed memory location shifted by a byte offset (see {@link StringType#WideC}).<br>
	 * See {@link Pointer#setStringAtOffset(long, String, StringType, Charset)} for more options
	 */
	public Pointer<T> setWideCStringAtOffset(long byteOffset, String s) {
        return setStringAtOffset(byteOffset, s, StringType.WideC, null);
    }
	

	/**
	 * Get the length of the C string at the pointed memory location shifted by a byte offset (see {@link StringType#C}).
	 */
	protected long strlen(long byteOffset) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + 1) > validEnd
	   )) {
		invalidPeer(checkedPeer, 1);
	}
		return JNI.strlen(checkedPeer);
	}
	
	/**
	 * Get the length of the wide C string at the pointed memory location shifted by a byte offset (see {@link StringType#WideC}).
	 */
	protected long wcslen(long byteOffset) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + Platform.WCHAR_T_SIZE) > validEnd
	   )) {
		invalidPeer(checkedPeer, Platform.WCHAR_T_SIZE);
	}
		return JNI.wcslen(checkedPeer);
	}
	
	/**
	 * Write zero bytes to all of the valid bytes pointed by this pointer
	 */
	public void clearValidBytes() {
		long bytes = getValidBytes();
    		if (bytes < 0)
    			throw new UnsupportedOperationException("Number of valid bytes is unknown. Please use clearBytes(long) or validBytes(long).");
		clearBytes(bytes);	
	}
	
	/**
	 * Write zero bytes to the first length bytes pointed by this pointer
	 */
	public void clearBytes(long length) {
		clearBytesAtOffset(0, length, (byte)0);	
	}
	/**
	 * Write a byte {@code value} to each of the {@code length} bytes at the address pointed to by this pointer shifted by a {@code byteOffset}
	 */
	public void clearBytesAtOffset(long byteOffset, long length, byte value) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + length) > validEnd
	   )) {
		invalidPeer(checkedPeer, length);
	}
		JNI.memset(checkedPeer, value, length);
	}
	
	/**
	 * Find the first occurrence of a value in the memory block of length searchLength bytes pointed by this pointer shifted by a byteOffset 
	 */
	public Pointer<T> findByte(long byteOffset, byte value, long searchLength) {
			long checkedPeer = getPeer() + byteOffset;
	if (validStart != UNKNOWN_VALIDITY && (
			checkedPeer < validStart || 
			(checkedPeer + searchLength) > validEnd
	   )) {
		invalidPeer(checkedPeer, searchLength);
	}
		long found = JNI.memchr(checkedPeer, value, searchLength);	
		return found == 0 ? null : offset(found - checkedPeer);
	}
	
	/**
	 * Alias for {@link Pointer#get(long)} defined for more natural use from the Scala language.
	 */
    public final T apply(long index) {
		return get(index);
	}
	
    /**
	 * Alias for {@link Pointer#set(long, Object)} defined for more natural use from the Scala language.
	 */
	public final void update(long index, T element) {
		set(index, element);
	}
	
    /**
	 * Create an array with all the values in the bounded memory area.<br>
	 * Note that if you wish to get an array of primitives (if T is boolean, char or a numeric type), then you need to call {@link Pointer#getArray()}.
	 * @throws IndexOutOfBoundsException if this pointer's bounds are unknown
	 */
	public T[] toArray() {
		getIO("Cannot create array");
        return toArray((int)getValidElements("Length of pointed memory is unknown, cannot create array out of this pointer"));
	}
	
	T[] toArray(int length) {
        Class<?> c = Utils.getClass(getIO("Cannot create array").getTargetType());
		if (c == null)
			throw new RuntimeException("Unable to get the target type's class (target type = " + io.getTargetType() + ")");
        return (T[])toArray((Object[])Array.newInstance(c, length));
	}
	
    /**
	 * Create an array with all the values in the bounded memory area, reusing the provided array if its type is compatible and its size is big enough.<br>
	 * Note that if you wish to get an array of primitives (if T is boolean, char or a numeric type), then you need to call {@link Pointer#getArray()}.
	 * @throws IndexOutOfBoundsException if this pointer's bounds are unknown
	 */
	public <U> U[] toArray(U[] array) {
		int n = (int)getValidElements();
		if (n < 0)
            throwBecauseUntyped("Cannot create array");
        
        if (array.length != n)
        	return (U[])toArray();
        
        for (int i = 0; i < n; i++)
        	array[i] = (U)get(i);
        return array;
	}
	
	/**
	* Types of pointer-based list implementations that can be created through {@link Pointer#asList()} or {@link Pointer#asList(ListType)}.
	 */
	public enum ListType {
		/**
		 * Read-only list
		 */
        Unmodifiable,
        /**
		 * List is modifiable and can shrink, but capacity cannot be increased (some operations will hence throw UnsupportedOperationException when the capacity is unsufficient for the requested operation)
		 */
        FixedCapacity,
        /**
		 * List is modifiable and its underlying memory will be reallocated if it needs to grow beyond its current capacity.
		 */
        Dynamic
    }
    
	/**
	 * Create a {@link ListType#FixedCapacity} native list that uses this pointer as storage (and has this pointer's pointed valid elements as initial content).<br> 
	 * Same as {@link Pointer#asList(ListType)}({@link ListType#FixedCapacity}).
	 */
	public NativeList<T> asList() {
		return asList(ListType.FixedCapacity);
	}
	/**
	 * Create a native list that uses this pointer as <b>initial</b> storage (and has this pointer's pointed valid elements as initial content).<br>
	 * If the list is {@link ListType#Dynamic} and if its capacity is grown at some point, this pointer will probably no longer point to the native memory storage of the list, so you need to get back the pointer with {@link NativeList#getPointer()} when you're done mutating the list.
	 */
	public NativeList<T> asList(ListType type) {
		return new DefaultNativeList(this, type);
	}
	/**
     * Create a {@link ListType#Dynamic} list with the provided initial capacity (see {@link ListType#Dynamic}).
     * @param io Type of the elements of the list
     * @param capacity Initial capacity of the list
     */
    public static <E> NativeList<E> allocateList(PointerIO<E> io, long capacity) {
        NativeList<E> list = new DefaultNativeList(allocateArray(io, capacity), ListType.Dynamic);
        list.clear();
        return list;
    }
	/**
     * Create a {@link ListType#Dynamic} list with the provided initial capacity (see {@link ListType#Dynamic}).
     * @param type Type of the elements of the list
     * @param capacity Initial capacity of the list
     */
    public static <E> NativeList<E> allocateList(Class<E> type, long capacity) {
        return allocateList((Type)type, capacity);
    }
	/**
     * Create a {@link ListType#Dynamic} list with the provided initial capacity (see {@link ListType#Dynamic}).
     * @param type Type of the elements of the list
     * @param capacity Initial capacity of the list
     */
    public static <E> NativeList<E> allocateList(Type type, long capacity) {
        return (NativeList)allocateList(PointerIO.getInstance(type), capacity);
    }
    
    private static char[] intsToWChars(int[] in) {
    	int n = in.length;
    	char[] out = new char[n];
    	for (int i = 0; i < n; i++)
    		out[i] = (char)in[i];
    	return out;
    }
    private static int[] wcharsToInts(char[] in, int valuesOffset, int length) {
    	int[] out = new int[length];
    	for (int i = 0; i < length; i++)
    		out[i] = in[valuesOffset + i];
    	return out;
    }

	public Pointer<T> setIntegralAtOffset(long byteOffset, AbstractIntegral value) {
		switch (value.byteSize()) {
			case 8:
				setLongAtOffset(byteOffset, value.longValue());
				break;
			case 4:
				setIntAtOffset(byteOffset, SizeT.safeIntCast(value.longValue()));
				break;
			default:
				throw new UnsupportedOperationException("Unsupported integral size");
		}
		return this;
	}
  public long getIntegralAtOffset(long byteOffset, int integralSize) {
		switch (integralSize) {
			case 8:
				return getLongAtOffset(byteOffset);
			case 4:
				return getIntAtOffset(byteOffset);
			default:
				throw new UnsupportedOperationException("Unsupported integral size");
		}
	}
}
