
package io.scif.formats;

import io.scif.AbstractFormat;
import io.scif.AbstractMetadata;
import io.scif.Format;

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
}
