package org.potassco.jna;

import java.util.Arrays;
import java.util.List;

import org.potassco.cpp.clingo_h;

import com.sun.jna.Structure;

/**
 * Represents a source code location marking its beginnig and end.
 * <p>
 * @note Not all locations refer to physical files.
 * By convention, such locations use a name put in angular brackets as filename.
 * The string members of a location object are internalized and valid for the duration of the process.
 * <p>
 * clingo_location_t
 * char const *begin_file; //!< the file where the location begins
 * char const *end_file;   //!< the file where the location ends
 * size_t begin_line;      //!< the line where the location begins
 * size_t end_line;        //!< the line where the location ends
 * size_t begin_column;    //!< the column where the location begins
 * size_t end_column;      //!< the column where the location ends#
 * @author Josef Schneeberger
 * {@link clingo_h#clingo_location_t}
 */
public class LocationSt extends Structure {
	public String begin_file;
	public String end_file;
	public SizeT begin_line;
	public SizeT end_line;
	public SizeT begin_column;
	public SizeT end_column;
	
	public LocationSt(String begin_file, String end_file, SizeT begin_line, SizeT end_line, SizeT begin_column, SizeT end_column) {
		super();
		this.begin_file = begin_file;
		this.end_file = end_file;
		this.begin_line = begin_line;
		this.end_line = end_line;
		this.begin_column = begin_column;
		this.end_column = end_column;
	}

	public LocationSt(String begin_file, String end_file, int begin_line, int end_line, int begin_column, int end_column) {
		this.begin_file = begin_file;
		this.end_file = end_file;
		this.begin_line = new SizeT(begin_line);
		this.end_line = new SizeT(end_line);
		this.begin_column = new SizeT(begin_column);
		this.end_column = new SizeT(end_column);
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("begin_file", "end_file", "begin_line", "end_line", "begin_column", "end_column");
	}

}