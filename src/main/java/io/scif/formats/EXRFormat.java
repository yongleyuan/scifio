
package io.scif.formats;

import io.scif.AbstractChecker;
import io.scif.AbstractFormat;
import io.scif.AbstractMetadata;
import io.scif.AbstractParser;
import io.scif.Format;
import io.scif.FormatException;
import io.scif.config.SCIFIOConfig;

import java.io.IOException;

import org.scijava.io.handle.DataHandle;
import org.scijava.io.location.Location;
import org.scijava.plugin.Plugin;

@Plugin(type = Format.class)
public class EXRFormat extends AbstractFormat {

	// -- AbstractFormat Methods --

	@Override
	public String getFormatName() {
		return "OpenEXR";
	}

	@Override
	protected String[] makeSuffixArray() {
		return new String[] { "exr" };
	}
	
	// -- Nested Classes --
	
	public static class Metadata extends AbstractMetadata {

		@Override
		public void populateImageMetadata() {
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class Parser extends AbstractParser<Metadata> {

		@Override
		protected void typedParse(DataHandle<Location> handle, Metadata meta,
			SCIFIOConfig config) throws IOException, FormatException
		{
			// TODO Auto-generated method stub
			
		}
	}
	
	public static class Checker extends AbstractChecker {
		
	}
}
