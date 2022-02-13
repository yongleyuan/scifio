
package io.scif.formats;

import io.scif.AbstractChecker;
import io.scif.AbstractFormat;
import io.scif.AbstractMetadata;
import io.scif.AbstractParser;
import io.scif.Format;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.config.SCIFIOConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.scijava.io.handle.DataHandle;
import org.scijava.io.location.Location;
import org.scijava.plugin.Plugin;

/**
 * TODO Write class description here
 * 
 * @author jack
 */
@Plugin(type = Format.class)
public class EXRFormat extends AbstractFormat {

	// -- AbstractFormat Methods --

	// -- Constants --

	/**
	 * Magic number is fixed. It allows file reader to distinguish OpenEXR files
	 * from other files. Magic number if always the first 4 bytes of an OpenEXR
	 * file. Store in the first byte (first 8 bits).
	 * <p>
	 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#magic-number
	 */
	private static final int MAGIC_NUMBER = 20000630;

	@Override
	public String getFormatName() {
		return "OpenEXR v2.0"; // TODO Might need to accommodate v1
	}

	@Override
	protected String[] makeSuffixArray() {
		return new String[] { "exr" };
	}

	// -- Nested Classes --

	/**
	 * TODO Write class description here
	 */
	public static class Metadata extends AbstractMetadata {



		@Override
		public void populateImageMetadata() {
			// TODO Auto-generated method stub

			// axis + pixels

			// axis length
			// axis type
			// pixel type (count bytes) + set pixel type (if there's case that doesn't
			// directly matches one of the format tools
		}
	}

	public static class Parser extends AbstractParser<Metadata> {

		@Override
		protected void typedParse(DataHandle<Location> handle, Metadata meta,
			SCIFIOConfig config) throws IOException, FormatException
		{
			meta.createImageMetadata(1);
			final ImageMetadata m = meta.get(0);
			meta.
		}
	}

	public static class Checker extends AbstractChecker {

		@Override
		public boolean suffixSufficient() {
			return false;
		}

		@Override
		public boolean isFormat(final DataHandle<Location> stream)
			throws IOException
		{
			stream.setLittleEndian(true);
			stream.seek(0); // start at the beginning of the file
			if (stream.readInt() != MAGIC_NUMBER) {
				return false;
			}

			return true;
		}
	}

	public static class Reader {

	}
}
