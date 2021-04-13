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
package org.bridj;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.nio.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bridj.PointerIO;
import org.bridj.util.Utils;

/**
 * Helper class that knows how to read/write data from/to a {@link Pointer}.<br>
 * End users don't need to use this class directly as ({@link Pointer} lets you work with {@link java.lang.reflect.Type} and {@link Class}).
 * @author Olivier
 */
public abstract class PointerIO<T> {
	final Type targetType;
	final Class<?> typedPointerClass;
	final int targetSize, targetAlignment = -1;
	
	public PointerIO(Type targetType, int targetSize, Class<?> typedPointerClass) {
		this.targetType = targetType;
		this.targetSize = targetSize;
		this.typedPointerClass = typedPointerClass;
	}
	abstract T get(Pointer<T> pointer, long index);
	abstract void set(Pointer<T> pointer, long index, T value);
	public Object getArray(Pointer<T> pointer, long byteOffset, int length) {
		return pointer.offset(byteOffset).toArray(length);
	}
	public <B extends Buffer> B getBuffer(Pointer<T> pointer, long byteOffset, int length) {
		throw new UnsupportedOperationException("Cannot create a Buffer instance of elements of type " + getTargetType());
	}
	public void setArray(Pointer<T> pointer, long byteOffset, Object array) {
		Object[] a = (Object[])array;
		for (int i = 0, n = a.length; i < n; i++)
			set(pointer, i, (T)a[i]);
	}
	
	public T castTarget(long peer) {
		throw new UnsupportedOperationException("Cannot cast pointer to " + targetType);
	}
	
	PointerIO<Pointer<T>> getReferenceIO() {
		return new CommonPointerIOs.PointerPointerIO<T>(this);
	}
	public long getTargetSize() {
		return targetSize;
	}
	public long getTargetAlignment() { 
		return targetAlignment < 0 ? getTargetSize() : targetAlignment;
	}
	public boolean isTypedPointer() {
		return typedPointerClass != null;
	}
	public Class<?> getTypedPointerClass() {
		return typedPointerClass;
	}
	public Type getTargetType() {
		return targetType;
	}
	
	static Class<?> getClass(Type type) {
		if (type instanceof Class<?>)
			return (Class<?>)type;
		if (type instanceof ParameterizedType)
			return getClass(((ParameterizedType)type).getRawType());
		return null;
	}
	
	private static final PointerIO<?> PointerIO = getPointerInstance((PointerIO<?>)null);

	public static <T> PointerIO<Pointer<T>> getPointerInstance(Type target) {
		return getPointerInstance((PointerIO<T>)getInstance(target));
	}
	public static <T> PointerIO<Pointer<T>> getPointerInstance(PointerIO<T> targetIO) {
		return new CommonPointerIOs.PointerPointerIO<T>(targetIO);
	}
	public static <T> PointerIO<Pointer<T>> getArrayInstance(PointerIO<T> targetIO, long[] dimensions, int iDimension) {
		return new CommonPointerIOs.PointerArrayIO<T>(targetIO, dimensions, iDimension);
	}
	
	static <T> PointerIO<T> getArrayIO(Object array) {
        		if (array instanceof int[])
			return (PointerIO)CommonPointerIOs.intIO;
				if (array instanceof long[])
			return (PointerIO)CommonPointerIOs.longIO;
				if (array instanceof short[])
			return (PointerIO)CommonPointerIOs.shortIO;
				if (array instanceof byte[])
			return (PointerIO)CommonPointerIOs.byteIO;
				if (array instanceof char[])
			return (PointerIO)CommonPointerIOs.charIO;
				if (array instanceof float[])
			return (PointerIO)CommonPointerIOs.floatIO;
				if (array instanceof double[])
			return (PointerIO)CommonPointerIOs.doubleIO;
				if (array instanceof boolean[])
			return (PointerIO)CommonPointerIOs.booleanIO;
				return PointerIO.getInstance(array.getClass().getComponentType());
	}   
	
	private static final ConcurrentHashMap<StructIO, PointerIO<?>> structIOs = new ConcurrentHashMap<StructIO, PointerIO<?>>();
	public static <S extends StructObject> PointerIO<S> getInstance(StructIO s) {
        PointerIO io = structIOs.get(s);
        if (io == null) {
            io = new CommonPointerIOs.StructPointerIO(s);
            PointerIO previousIO = structIOs.putIfAbsent(s, io);
            if (previousIO != null)
                io = previousIO;
        }
        return io;
    }
  private static final ConcurrentHashMap<Type, PointerIO<?>> ios = new ConcurrentHashMap<Type, PointerIO<?>>();
  static {
		ios.put(Pointer.class, PointerIO);
		ios.put(SizeT.class, CommonPointerIOs.SizeTIO);
		ios.put(TimeT.class, CommonPointerIOs.TimeTIO);
		ios.put(CLong.class, CommonPointerIOs.CLongIO);

        {
    	PointerIO io = CommonPointerIOs.intIO;
    	ios.put(Integer.TYPE, io);
    	ios.put(Integer.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.longIO;
    	ios.put(Long.TYPE, io);
    	ios.put(Long.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.shortIO;
    	ios.put(Short.TYPE, io);
    	ios.put(Short.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.byteIO;
    	ios.put(Byte.TYPE, io);
    	ios.put(Byte.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.charIO;
    	ios.put(Character.TYPE, io);
    	ios.put(Character.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.floatIO;
    	ios.put(Float.TYPE, io);
    	ios.put(Float.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.doubleIO;
    	ios.put(Double.TYPE, io);
    	ios.put(Double.class, io);
    }
        {
    	PointerIO io = CommonPointerIOs.booleanIO;
    	ios.put(Boolean.TYPE, io);
    	ios.put(Boolean.class, io);
    }
      }
	public static <P> PointerIO<P> getInstance(Type type) {
		if (type == null)
			return null;

		PointerIO io = ios.get(type);
		if (io == null) {
			final Class<?> cl = Utils.getClass(type);
			if (cl != null) {
				if (cl == Pointer.class)
					io = getPointerInstance(((ParameterizedType)type).getActualTypeArguments()[0]);
				else if (StructObject.class.isAssignableFrom(cl))
					io = getInstance(StructIO.getInstance((Class)cl, type));
				else if (Callback.class.isAssignableFrom(cl))
					io = new CommonPointerIOs.CallbackPointerIO(cl);
				else if (NativeObject.class.isAssignableFrom(cl))
					io = new CommonPointerIOs.NativeObjectPointerIO(type);
				else if (IntValuedEnum.class.isAssignableFrom(cl)) {
					if (type instanceof ParameterizedType) {
						Type enumType = ((ParameterizedType)type).getActualTypeArguments()[0];
						if (enumType instanceof Class)
							io = new CommonPointerIOs.IntValuedEnumPointerIO((Class)enumType);
					}
				}
				else if (TypedPointer.class.isAssignableFrom(cl))
					io = new CommonPointerIOs.TypedPointerPointerIO((Class<? extends TypedPointer>)cl);
			}
			if (io != null) {
				PointerIO previousIO = ios.putIfAbsent(type, io);
				if (previousIO != null)
          io = previousIO; // created io twice : not important in general (expecially not compared to cost of contention on non-concurrent map)
        }
      }
      return io;
    }


		 		public static PointerIO<Integer> getIntInstance() {
      return (PointerIO)CommonPointerIOs.intIO;
  	}
  	  		public static PointerIO<Long> getLongInstance() {
      return (PointerIO)CommonPointerIOs.longIO;
  	}
  	  		public static PointerIO<Short> getShortInstance() {
      return (PointerIO)CommonPointerIOs.shortIO;
  	}
  	  		public static PointerIO<Byte> getByteInstance() {
      return (PointerIO)CommonPointerIOs.byteIO;
  	}
  	  		public static PointerIO<Character> getCharInstance() {
      return (PointerIO)CommonPointerIOs.charIO;
  	}
  	  		public static PointerIO<Float> getFloatInstance() {
      return (PointerIO)CommonPointerIOs.floatIO;
  	}
  	  		public static PointerIO<Double> getDoubleInstance() {
      return (PointerIO)CommonPointerIOs.doubleIO;
  	}
  	  		public static PointerIO<Boolean> getBooleanInstance() {
      return (PointerIO)CommonPointerIOs.booleanIO;
  	}
  	  		public static PointerIO<CLong> getCLongInstance() {
      return (PointerIO)CommonPointerIOs.CLongIO;
  	}
  	  		public static PointerIO<SizeT> getSizeTInstance() {
      return (PointerIO)CommonPointerIOs.SizeTIO;
  	}
  	   
  	public static <T> PointerIO<T> getPointerInstance() {
      return (PointerIO<T>)PointerIO;
  	}

  	public static PointerIO<TimeT> getTimeTInstance() {
      return (PointerIO)CommonPointerIOs.TimeTIO;
  	}

    public static <P> PointerIO<P> getBufferPrimitiveInstance(Buffer buffer) {
        		if (buffer instanceof IntBuffer)
            return (PointerIO)CommonPointerIOs.intIO;
				if (buffer instanceof LongBuffer)
            return (PointerIO)CommonPointerIOs.longIO;
				if (buffer instanceof ShortBuffer)
            return (PointerIO)CommonPointerIOs.shortIO;
				if (buffer instanceof ByteBuffer)
            return (PointerIO)CommonPointerIOs.byteIO;
				if (buffer instanceof CharBuffer)
            return (PointerIO)CommonPointerIOs.charIO;
				if (buffer instanceof FloatBuffer)
            return (PointerIO)CommonPointerIOs.floatIO;
				if (buffer instanceof DoubleBuffer)
            return (PointerIO)CommonPointerIOs.doubleIO;
		        throw new UnsupportedOperationException();
    }

}
