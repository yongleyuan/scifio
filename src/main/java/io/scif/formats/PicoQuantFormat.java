
package io.scif.formats;

import io.scif.AbstractChecker;
import io.scif.AbstractFormat;
import io.scif.AbstractMetadata;
import io.scif.AbstractParser;
import io.scif.AbstractTranslator;
import io.scif.AbstractWriter;
import io.scif.ByteArrayPlane;
import io.scif.ByteArrayReader;
import io.scif.Format;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.Plane;
import io.scif.config.SCIFIOConfig;
import io.scif.formats.ICSFormat.Metadata;

import java.io.IOException;
import java.util.List;

import net.imglib2.Interval;

import org.scijava.io.handle.DataHandle;
import org.scijava.io.location.Location;
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

	/**
	 * SCIFIO file format Parser for PicoQuant images.
	 */
	public static class Parser extends AbstractParser<Metadata> {

		@Override
		protected void typedParse(DataHandle<Location> handle, Metadata meta,
			SCIFIOConfig config) throws IOException, FormatException
		{
			// TODO Auto-generated method stub

		}

	}

	/**
	 * SCIFIO file format Checker for PicoQuant images.
	 */
	public static class Checker extends AbstractChecker {

	}

	/**
	 * SCIFIO file format Reader for PicoQuant images
	 */
	public static class Reader extends ByteArrayReader<Metadata> {

		@Override
		public ByteArrayPlane openPlane(int imageIndex, long planeIndex,
			ByteArrayPlane plane, Interval bounds, SCIFIOConfig config)
			throws FormatException, IOException
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected String[] createDomainArray() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * SCIFIO file format Writer for PicoQuant images.
	 */
	public static class Writer extends AbstractWriter<Metadata> {

		@Override
		protected String[] makeCompressionTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void writePlane(int imageIndex, long planeIndex, Plane plane,
			Interval bounds) throws FormatException, IOException
		{
			// TODO Auto-generated method stub

		}

	}

	/**
	 * SCIFIO file format Translator for PicoQuant images.
	 */
	public static class Translator extends
		AbstractTranslator<io.scif.Metadata, Metadata>
	{

		@Override
		public Class<? extends io.scif.Metadata> source() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<? extends io.scif.Metadata> dest() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void translateImageMetadata(List<ImageMetadata> source,
			Metadata dest)
		{
			// TODO Auto-generated method stub
			
		}

	}

}
