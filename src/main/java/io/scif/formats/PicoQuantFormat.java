
package io.scif.formats;

import io.scif.AbstractFormat;
import io.scif.AbstractMetadata;
import io.scif.Format;

import org.scijava.plugin.Plugin;

/**
 * Handler plugin for the PicoQuant file format.
 * 
 * @author Jack Yuan
 */
@Plugin(type = Format.class, name = "PicoQuant")
public class PicoQuantFormat extends AbstractFormat {

	// -- AbstractFormat Methods --

	@Override
	protected String[] makeSuffixArray() {
		return new String[] { "ptu", "phu", "ht3", "pt3", "t3r" };
	}

	// -- Nested Classes --

	/**
	 * SCIFIO Metadata object for PicoQuant images.
	 */
	public static class Metadata extends AbstractMetadata {

		@Override
		public void populateImageMetadata() {
			// TODO Auto-generated method stub

		}

	}

}
