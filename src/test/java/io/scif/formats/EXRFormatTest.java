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
import io.scif.config.SCIFIOConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.scijava.io.location.FileLocation;
import org.scijava.io.location.Location;
import org.scijava.io.location.LocationService;


public class EXRFormatTest {

	private SCIFIO scifio;
	private LocationService locationService;
	
	@Before
	public void init() {
		scifio = new SCIFIO();
		locationService = scifio.context().getService(LocationService.class);
	}
	
	@Test
	public void testOpenSampleImg() throws FormatException, IOException, URISyntaxException {
		final URI path = this.getClass().getResource("/exr-example/MtTamWest.exr").toURI();
		final Location exrFileLoc = new FileLocation(path);
		final SCIFIOConfig config = new SCIFIOConfig(scifio.getContext());
		config.checkerSetOpen(true);
		final Format exrFormat1 = scifio.format().getFormat(exrFileLoc, config);
		config.checkerSetOpen(false);
		final Format exrFormat2 = scifio.format().getFormat(exrFileLoc, config);
		assertEquals(exrFormat1, exrFormat2);
		
		final Parser exrParser = exrFormat1.createParser();
		final Metadata meta = exrParser.parse(exrFileLoc);
		
		// TODO separate into different test methods
		// TODO write tests here specific to exr metadata
		final ImageMetadata imgMeta = meta.get(0);
		// print axis length to check
		assertEquals(1214, imgMeta.getAxesLengths()[0]);
	}

}
