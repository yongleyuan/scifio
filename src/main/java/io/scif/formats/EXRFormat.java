
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
		return "OpenEXR v2.0"; // TODO Might need to accommodate v1
	}

	@Override
	protected String[] makeSuffixArray() {
		return new String[] { "exr" };
	}

	// -- Nested Classes --

	public static class Metadata extends AbstractMetadata {

		// -- Constants --

		/**
		 * Magic number is fixed. It allows file reader to distinguish OpenEXR files
		 * from other files. Magic number if always the first 4 bytes of an OpenEXR
		 * file. Store in the first byte (first 8 bits).
		 * <p>
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#magic-number
		 */
		private static final int MAGIC_NUMBER = 20000630;

		// -- Fields --

		/**
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#version-field
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#version-field-valid-values
		 */

		// TODO Might not need to specify all bits, instead just choose file format
		// according to those bits.

		/** Version of OpenEXR file */
		private int version;

		/** True if the file is a single-part tiled image. Stored at bit 9. */
		private boolean isSingleTile;

		/**
		 * True if the file contains attribute names, attribute type names, and
		 * channel names longer than 31 bytes. Stored at bit 10.
		 */
		private boolean containLongNames;

		/** True if the file contains deep data. Stored at bit 11. */
		private boolean containDeepData;

		/** True if the file is a multi-part file. Stored at bit 12. */
		private boolean isMultiPart;
		
		

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

	public static class Reader {

	}
}
