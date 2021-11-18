package io.scif.formats;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import io.scif.Format;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.Metadata;
import io.scif.Parser;
import io.scif.SCIFIO;

import java.io.IOException;

import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import org.scijava.io.location.LocationService;


public class EXRFormatTest extends AbstractFormatTest {

	private SCIFIO scifio;
	private LocationService locationService;
	
	@Before
	public void init() {
		scifio = new SCIFIO();
		locationService = scifio.context().getService(LocationService.class);
	}
	
	@Test
	public void testOpenSampleImg() throws FormatException, IOException {
		final String source = "/Users/jack/Projects/LOCI/scifio/src/test/resources/exr-example/MtTamWest.exr";
		final Location exrFileLoc = new FileLocation(source);
		final Format exrFormat = scifio.format().getFormat(exrFileLoc);
		final Parser exrParser = exrFormat.createParser();
		final Metadata meta = exrParser.parse(exrFileLoc);

		// TODO write tests here specific to exr metadata
		final ImageMetadata imgMeta = meta.get(0);
		// print axis length to check
		assertEquals(1214, imgMeta.getAxesLengths()[0]);
	}

}
