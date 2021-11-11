package io.scif.formats;

import org.junit.Before;
import org.junit.Test;

import io.scif.FormatException;
import io.scif.SCIFIO;

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
	public void testOpenSampleImg() throws FormatException {
		final String source = "/Users/jack/Projects/LOCI/scifio/src/test/resources/exr-example/MtTamWest.exr";
		Location fileLoc = new FileLocation(source);
		scifio.format().getFormat(fileLoc);
	}

}
