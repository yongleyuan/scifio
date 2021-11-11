
package io.scif.formats;

import io.scif.AbstractChecker;
import io.scif.AbstractFormat;
import io.scif.AbstractMetadata;
import io.scif.AbstractParser;
import io.scif.Format;
import io.scif.FormatException;
import io.scif.ImageMetadata;
import io.scif.config.SCIFIOConfig;

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

	@Override
	public String getFormatName() {
		return "OpenEXR v2.0"; // TODO Might need to accommodate v1
	}

	@Override
	protected String[] makeSuffixArray() {
		return new String[] { "exr" };
	}

	// -- Nested Classes --

	// -- OpenEXR Predefined Attribute Types --

	/**
	 * Enumeration of the 5 file types OpenEXR currently support. The file type is
	 * defined by the combination of bit 9, 11, and 12, which are commented after
	 * each type.
	 */
	private static enum FileType {
			SINGLE_SCAN_LINE, // 0 0 0
			SINGLE_TILE, // 1 0 0
			MULTI_PART, // 0 0 1
			SINGLE_PART_DEEP, // 0 1 0
			MULTI_PART_DEEP // 0 1 1
	}

	/** Used to define the size of a window. Contains four int's. */
	private static class Box2i {

		int xMin;
		int yMin;
		int xMax;
		int yMax;

		Box2i(int xMin, int yMin, int xMax, int yMax) {
			this.xMin = xMin;
			this.yMin = yMin;
			this.xMax = xMax;
			this.yMax = yMax;
		}
	}

	/** Used to define the size of a window. Contains four float's. */
	private static class Box2f {

		float xMin;
		float yMin;
		float xMax;
		float yMax;

		Box2f(float xMin, float yMin, float xMax, float yMax) {
			this.xMin = xMin;
			this.yMin = yMin;
			this.xMax = xMax;
			this.yMax = yMax;
		}
	}

	/** UNIT = 0, HALF = 1, FLOAT = 2 */
	private static enum PixelType {
			UNIT, HALF, FLOAT
	}

	/** A sequence of channels followed by a null byte. */
	private static class Chlist {

		PixelType pixelType;
		String name;
		char pLinear; // 0 or 1
		// TODO Is 'reserved' needed
		int xSampling;
		int ySampling;

		Chlist(int pixelType, String name, char pLinear, int xSampling,
			int ySampling)
		{
			this.pixelType = pixelType == 0 ? PixelType.UNIT : pixelType == 1
				? PixelType.HALF : PixelType.FLOAT;
			switch (pixelType) {
				case 0:
					this.pixelType = PixelType.UNIT;
					break;
				case 1:
					this.pixelType = PixelType.HALF;
					break;
				case 2:
					this.pixelType = PixelType.FLOAT;
					break;
				default:
					throw new IllegalArgumentException("Pixel type not recognized.");
			}
			this.name = name;
			this.pLinear = pLinear;
			this.xSampling = xSampling;
			this.ySampling = ySampling;
		}
	}

	/** Used to define chromaticity. Contains 8 float's. */
	private static class Chromaticities {

		float redX;
		float redY;
		float greenX;
		float greenY;
		float blueX;
		float blueY;
		float whiteX;
		float whiteY;

		Chromaticities(float redX, float redY, float greenX, float greenY,
			float blueX, float blueY, float whiteX, float whiteY)
		{
			this.redX = redX;
			this.redY = redY;
			this.greenX = greenX;
			this.greenY = greenY;
			this.blueX = blueX;
			this.blueY = blueY;
			this.whiteX = whiteX;
			this.whiteY = whiteY;
		}
	}

	/** Compression type. */
	private static enum Compression {
			NO_COMPRESSION, RLE_COMPRESSION, ZIPS_COMPRESSION, ZIP_COMPRESSION,
			PIZ_COMPRESSION, PXR24_COMPRESSION, B44_COMPRESSION, B44A_COMPRESSION
	}

	/** Contains 7 int's. */
	private static class Keycode {

		int filmMfcCode;
		int filmeType;
		int prefix;
		int count;
		int perfOffset;
		int perfsPerFrame;
		int perfsPerCount;

		Keycode(int filmMfcCode, int filmeType, int prefix, int count,
			int perfOffset, int perfsPerFrame, int perfsPerCount)
		{
			this.filmMfcCode = filmMfcCode;
			this.filmeType = filmeType;
			this.prefix = prefix;
			this.count = count;
			this.perfOffset = perfOffset;
			this.perfsPerFrame = perfsPerFrame;
			this.perfsPerCount = perfsPerCount;
		}
	}

	/** Order of lines. */
	private static enum LineOrder {
			INCRESING_Y, DECREASING_Y, RAMDOM_Y
	}

	/**
	 * Contains width and height, followed by 4*width*height of pixel data. Scan
	 * lines are stored top to bottom; within a scan line pixels are stored from
	 * left to right. A pixel consists of R, G, B, A.
	 */
	private static class Preview {

		int width;
		int height;

		// TODO Assign pixel data
	}

	private static enum LevelMode {
			ONE_LEVEL, MIPMAP_LEVELS, RIPMAP_LEVELS
	}

	private static enum RoundingMode {
			ROUND_DOWN, ROUND_UP
	}

	/**
	 * Contains xSize and ySize, and mode which is calculated from levelMode and
	 * roundingMode.
	 */
	private static class TileDesc {

		LevelMode levelMode;
		RoundingMode roundingMode;
		int xSize;
		int ySize;
		int mode;

		TileDesc(int levelMode, int roundingMode, int xSize, int ySize) {
			switch (levelMode) {
				case 0:
					this.levelMode = LevelMode.ONE_LEVEL;
					break;
				case 1:
					this.levelMode = LevelMode.MIPMAP_LEVELS;
					break;
				case 2:
					this.levelMode = LevelMode.RIPMAP_LEVELS;
					break;
				default:
					throw new IllegalArgumentException("Level mode not recognized.");
			}
			switch (roundingMode) {
				case 0:
					this.roundingMode = RoundingMode.ROUND_DOWN;
					break;
				case 1:
					this.roundingMode = RoundingMode.ROUND_UP;
					break;
				default:
					throw new IllegalArgumentException("Level mode not recognized.");
			}
			this.xSize = xSize;
			this.ySize = ySize;
			this.mode = levelMode + roundingMode * 16;
		}
	}

	/** Contains timeAndFlags and userData. */
	private static class Timecode {

		int timeAndFlags;
		int userData;

		Timecode(int timeAndFlags, int userData) {
			this.timeAndFlags = timeAndFlags;
			this.userData = userData;
		}
	}

	// -- Other Atributes --

	/**
	 * Component 3 - Header
	 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#component-three-header
	 * TODO Write class description here
	 */
	private static class Header {

		// -- Header Attributes (All Files) --
		Chlist channels;
		Compression compression;
		Box2i dataWindow;
		Box2i displayWindow;
		LineOrder lineOrder;
		float pixelAspectRatio;
		int[] screenWindowCenter = new int[2];
		float screenWindowWidth;

		// -- Tile Header Attribute --
		TileDesc tiles;

		// -- Multi-View Header Attribute --
		String view;

		// -- Multi-Part and Deep Data Header Attributes --
		String name;
		String type;
		Integer version; // can be null
		Integer chunkCount;
		// TileDesc tiles: duplicated

		// -- DeepData Header Attribute --
		// TileDesc tiles: duplicated
		Integer maxSamplesPerPixel;
		// int version: duplicated
		// String type: duplicated

		/**
		 * Standard constructor that assigns attributes for all kinds of headers.
		 */
		Header(Chlist channels, int compression, Box2i dataWindow,
			Box2i displayWindow, LineOrder lineOrder, float pixelAspectRatio,
			int centerX, int centerY, float screenWindowWidth)
		{
			this.channels = channels;
			switch (compression) {
				case 0:
					this.compression = Compression.NO_COMPRESSION;
					break;
				case 1:
					this.compression = Compression.RLE_COMPRESSION;
					break;
				case 2:
					this.compression = Compression.ZIPS_COMPRESSION;
					break;
				case 3:
					this.compression = Compression.ZIP_COMPRESSION;
					break;
				case 4:
					this.compression = Compression.PIZ_COMPRESSION;
					break;
				case 5:
					this.compression = Compression.PXR24_COMPRESSION;
					break;
				case 6:
					this.compression = Compression.B44_COMPRESSION;
					break;
				case 7:
					this.compression = Compression.B44A_COMPRESSION;
					break;
				default:
					throw new IllegalArgumentException("Compression not recognized.");
			}
			this.dataWindow = dataWindow;
			this.displayWindow = displayWindow;
			this.lineOrder = lineOrder;
			this.pixelAspectRatio = pixelAspectRatio;
			this.screenWindowCenter[0] = centerX;
			this.screenWindowCenter[1] = centerY;
			this.screenWindowWidth = screenWindowWidth;
		}

		/** Overload constructor for tile headers. */
		Header(Chlist channels, int compression, Box2i dataWindow,
			Box2i displayWindow, LineOrder lineOrder, float pixelAspectRatio,
			int centerX, int centerY, float screenWindowWidth, TileDesc tiles)
		{
			this(channels, compression, dataWindow, displayWindow, lineOrder,
				pixelAspectRatio, centerX, centerY, screenWindowWidth);
			this.tiles = tiles;
		}

		/** Overload constructor for multi-view tile headers. */
		Header(Chlist channels, int compression, Box2i dataWindow,
			Box2i displayWindow, LineOrder lineOrder, float pixelAspectRatio,
			int centerX, int centerY, float screenWindowWidth, String view)
		{
			this(channels, compression, dataWindow, displayWindow, lineOrder,
				pixelAspectRatio, centerX, centerY, screenWindowWidth);
			this.view = view;
		}

		/** Overload constructor for multi-part and deep data headers. */
		Header(Chlist channels, int compression, Box2i dataWindow,
			Box2i displayWindow, LineOrder lineOrder, float pixelAspectRatio,
			int centerX, int centerY, float screenWindowWidth, String name,
			String type, int version, int chunkCount, TileDesc tiles)
		{
			this(channels, compression, dataWindow, displayWindow, lineOrder,
				pixelAspectRatio, centerX, centerY, screenWindowWidth);
			this.name = name;
			this.type = type;
			this.version = version;
			this.chunkCount = chunkCount;
			this.tiles = tiles;
		}

		/** Overload constructor for deep data headers. */
		Header(Chlist channels, int compression, Box2i dataWindow,
			Box2i displayWindow, LineOrder lineOrder, float pixelAspectRatio,
			int centerX, int centerY, float screenWindowWidth, TileDesc tiles,
			int maxSamplesPerPixel, int version, String type)
		{
			this(channels, compression, dataWindow, displayWindow, lineOrder,
				pixelAspectRatio, centerX, centerY, screenWindowWidth);
			this.tiles = tiles;
			this.maxSamplesPerPixel = maxSamplesPerPixel;
			this.version = version;
			this.type = type;
		}
	}

	/**
	 * Component 4 - Offset Table
	 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#component-four-offset-tables
	 * TODO Write class description here
	 */
	private static class OffsetTable {

		int numEntries;
		int[] table; // TODO accommodate multi-part type

		OffsetTable(int numEntries, int[] table) {
			this.numEntries = numEntries;
			this.table = table;
		}

	}

	/**
	 * Component 5 - Pixel Data
	 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#component-five-pixel-data
	 * TODO Write class description here
	 */
	private static class PixelData {

		int partNum; // if multi-part bit is set
		ScanLine scanLine;
		ImageTile tile;
		DeepDataScanLine deepScanLine;
		DeepDataTile deepTile;
	}

	private static class ScanLineBlock {

		int yCoordinate;
		int pixelDataSize;
		int[] pixelData;

		ScanLineBlock(int yCoordinate, int pixelDataSize, int[] pixelData) {
			this.yCoordinate = yCoordinate;
			this.pixelDataSize = pixelDataSize;
			this.pixelData = pixelData;
		}
	}

	private static class ScanLine {

		int pixelDataSize; // number of bytes occupied by the pixel data
		ScanLineBlock[] blocks;

		ScanLine(int pixelDataSize, ScanLineBlock[] blocks) {
			this.pixelDataSize = pixelDataSize;
			this.blocks = blocks;
		}
	}

	private static class TileCoordinates {

		int tileX;
		int tileY;
		int levelX;
		int levelY;

		TileCoordinates(int tileX, int tileY, int levelX, int levelY) {
			this.tileX = tileX;
			this.tileY = tileY;
			this.levelX = levelX;
			this.levelY = levelY;
		}
	}

	private static class ImageTile {

		TileCoordinates tileCoordinates;
		int pixelDataSize; // number of bytes occupied by the pixel data
		int[] pixelData;

		ImageTile(TileCoordinates tileCoordinates, int pixelDataSize,
			int[] pixelData)
		{
			this.tileCoordinates = tileCoordinates;
			this.pixelDataSize = pixelDataSize;
			this.pixelData = pixelData;
		}
	}

	private static class DeepDataScanLine {

		int yCoordinate;
		long packedSizeOffsetTable;
		long packedSizeSampleData;
		long unpackedSizeSampleData;
		int[] compressedOffsetTable;
		int[] compressesSampleData;

		DeepDataScanLine(int yCoordinate, long packedSizeOffsetTable,
			long packedSizeSampleData, long unpackedSizeSampleData,
			int[] compressedOffsetTable, int[] compressesSampleData)
		{
			this.yCoordinate = yCoordinate;
			this.packedSizeOffsetTable = packedSizeOffsetTable;
			this.packedSizeSampleData = packedSizeSampleData;
			this.unpackedSizeSampleData = unpackedSizeSampleData;
			this.compressedOffsetTable = compressedOffsetTable;
			this.compressesSampleData = compressesSampleData;
		}
	}

	private static class DeepDataTile {

		TileCoordinates tileCoordinates;
		long packedSizeOffsetTable;
		long packedSizeSampleData;
		long unpackedSizeSampleData;
		int[] compressedOffsetTable;
		int[] compressedSampleData;

		DeepDataTile(TileCoordinates tileCoordinates, long packedSizeOffsetTable,
			long packedSizeSampleData, long unpackedSizeSampleData,
			int[] compressedOffsetTable, int[] compressedSampleData)
		{
			this.tileCoordinates = tileCoordinates;
			this.packedSizeOffsetTable = packedSizeOffsetTable;
			this.packedSizeSampleData = packedSizeSampleData;
			this.unpackedSizeSampleData = unpackedSizeSampleData;
			this.compressedOffsetTable = compressedOffsetTable;
			this.compressedSampleData = compressedSampleData;
		}
	}

	/**
	 * TODO Write class description here
	 */
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

		/** Version of OpenEXR file. Stored from bit 0 to 7 */
		private int version;

		/**
		 * True if the file contains attribute names, attribute type names, and
		 * channel names longer than 31 bytes. Stored at bit 10.
		 */
		private boolean longNames;

		private FileType fileType;

		/** Header */
		private Header header;

		/**
		 * Component 4 - Offset table
		 * <p>
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#component-four-offset-tables
		 */
		private OffsetTable offsetTable;

		/**
		 * Component 5 - Pixel data
		 * <p>
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#component-five-pixel-data
		 */
		private PixelData pixelData;

		// -- Fields --

		/**
		 * Component 1 & 2 - Magic number, Version, and Flags
		 * <p>
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#version-field
		 * https://openexr.readthedocs.io/en/latest/OpenEXRFileLayout.html#version-field-valid-values
		 */

		public FileType getFileType() {
			return fileType;
		}

		public void setFileType(FileType fileType) {
			this.fileType = fileType;
		}
		
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
			// check magic number
			log().info("Verifying EXR magic number");
			// TODO
			
			switch (meta.getFileType()) {
				case SINGLE_SCAN_LINE:
					meta.createImageMetadata(1);
					
					final ImageMetadata m = meta.get(0);
					// TODO
					break;
				case SINGLE_TILE:
					// TODO
					break;
				case MULTI_PART:
					// TODO
					break;
				case SINGLE_PART_DEEP:
					// TODO
					break;
				case MULTI_PART_DEEP:
					// TODO
					break;
				default:
					throw new IllegalArgumentException(
						"Metadata's file type is not recognized.");
			}
		}
	}

	public static class Checker extends AbstractChecker {

	}

	public static class Reader {

	}
}
