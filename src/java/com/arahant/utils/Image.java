/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

package com.arahant.utils;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.kissweb.FileUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Author: Blake McBride
 * Date: 2/23/19
 */
public class Image {

    /**
     * Compress an image byte array.
     *
     * @param type files type "jpg", "png", etc.
     * @param inbytea the uncompress image
     * @param quality  a number between 0 and 1.  Smaller means more compression.
     * @return the compressed image
     * @throws IOException when error
     */
    public static byte [] compressImage(String type, byte [] inbytea, float quality) throws IOException {
        byte [] res;

        if (type == null)
            return inbytea;
        if ("pdf".equalsIgnoreCase(type))
            return shrinkPDF(quality, inbytea);

        // get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(type);

        if (!writers.hasNext())  // can't compress
            return inbytea;

        try (InputStream is = new ByteArrayInputStream(inbytea);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            // create a BufferedImage as the result of decoding the supplied InputStream
            BufferedImage image = ImageIO.read(is);

            ImageWriter writer = null;
            ImageOutputStream ios = null;
            try {
                writer = writers.next();
                ios = ImageIO.createImageOutputStream(os);
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();

                // compress to a given quality
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);

                // appends a complete image stream containing a single image and
                //associated stream and image metadata and thumbnails to the output
                writer.write(null, new IIOImage(image, null, null), param);
                res = os.toByteArray();
            } finally {
                if (ios != null)
                    ios.close();
                if (writer != null)
                    writer.dispose();
            }
        }
        return res;
    }

    private static byte [] shrinkPDF(float compQual, byte [] input) throws IOException {
        final PDDocument doc = PDDocument.load(input);
        try {
            final PDPageTree pages = doc.getDocumentCatalog().getPages();
            for (Object p : pages) {
                if (!(p instanceof PDPage))
                    continue;
                PDPage page = (PDPage) p;
                scanResources(page.getResources(), doc, compQual);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            doc.save(os);
            return os.toByteArray();
        } finally {
            if (doc != null)
                doc.close();
        }
    }

    private static void scanResources(final PDResources rList, final PDDocument doc, float compQual) throws IOException {
        if (rList == null)
            return;
        final Iterable<COSName> names = rList.getXObjectNames();
        for (COSName name : names) {
            final PDXObject xObj = rList.getXObject(name);
            if (xObj instanceof PDFormXObject)
                scanResources(((PDFormXObject) xObj).getResources(), doc, compQual);
            if (!(xObj instanceof PDImageXObject))
                continue;
            PDImageXObject img = (PDImageXObject) xObj;
            final Iterator<ImageWriter> jpgWriters = ImageIO.getImageWritersByFormatName("jpeg");
            final ImageWriter jpgWriter = jpgWriters.next();
            final ImageWriteParam iwp = jpgWriter.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(compQual);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jpgWriter.setOutput(ImageIO.createImageOutputStream(baos));
            jpgWriter.write(null, new IIOImage(img.getImage(), null, null), iwp);
            PDImageXObject im = PDImageXObject.createFromByteArray(doc, baos.toByteArray(), name.getName());
            rList.put(name, im);
        }
    }

    public static void writefile(String fname, byte [] out) throws IOException {
        Files.write(Paths.get(fname), out);
    }

    public static byte [] readfile(String fname) throws IOException {
        return Files.readAllBytes(Paths.get(fname));
    }

    // convert BufferedImage to byte[]
    private static byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    // convert byte[] to BufferedImage
    private static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;
    }

    /**
     * Reduce the size of JPEG, BMP, PNG,and GIF files.
     *
     * <code>size</code> is the resulting size of the larger axis (height or width)
     *
     * The aspect ratio is retained.
     *
     * If the type is not handled, the image passed is simply returned.
     *
     * @param in
     * @param type file type; JPG, JPEG, BMP, PNG, or GIF (case doesn't matter)
     * @param size new height or width size
     * @return
     */
    public static byte [] resizeImage(byte [] in, String type, int size) {
        if (type == null)
            return in;
        type = type.toLowerCase();
        if (in == null || !type.equals("jpg") &&
                          !type.equals("jpeg") &&
                          !type.equals("bmp") &&
                          !type.equals("png") &&
                          !type.equals("gif"))
            return in;
        try {
            BufferedImage bi = toBufferedImage(in);
            int h = bi.getHeight();
            int w = bi.getWidth();
            int h2, w2;
            int s2 = (int) ((double) size * 1.1);  // allow some flex
            if (h < s2 && w < s2)
                return in;
            if (h > w) {
                h2 = size;
                w2 = (w * h2) / h;
            } else {
                w2 = size;
                h2 = (h * w2) / w;
            }
            java.awt.Image im = bi.getScaledInstance(w2, h2, java.awt.Image.SCALE_DEFAULT);
            BufferedImage bi2 = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_RGB);
            bi2.getGraphics().drawImage(im, 0, 0, null);
            return toByteArray(bi2, type);
        } catch (IOException e) {
            e.printStackTrace();
            return in;
        }
    }

    public static void rotateLeft(String fileName) {
		try {
            final String ext = ExternalFile.fileExtension(fileName);

			byte[] image = FileUtils.readFileBytes(fileName);
			ByteArrayInputStream bis = new ByteArrayInputStream(image);
			BufferedImage bImage = ImageIO.read(bis);

			int w = bImage.getWidth();
			int h = bImage.getHeight();
			BufferedImage dest = new BufferedImage(h, w, bImage.getType());
			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++)
					dest.setRGB(y, w - x - 1, bImage.getRGB(x, y));

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final String ext2 = ext.length() > 1 ? ext.substring(1) : ext; // remove .
			ImageIO.write(dest, ext2, bos);
			image = bos.toByteArray();

            FileUtils.write(fileName, image);
		} catch (Exception ignored) {

		}
	}

    public static void rotateRight(String fileName) {
		try {
            final String ext = ExternalFile.fileExtension(fileName);

			byte[] image = FileUtils.readFileBytes(fileName);
			ByteArrayInputStream bis = new ByteArrayInputStream(image);
			BufferedImage bImage = ImageIO.read(bis);

			BufferedImage dest = rotateImage(bImage, 90);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final String ext2 = ext.length() > 1 ? ext.substring(1) : ext; // remove .
			ImageIO.write(dest, ext2, bos);
			image = bos.toByteArray();

			FileUtils.write(fileName, image);
		} catch (Exception ignored) {

		}
	}

    public static BufferedImage rotateImage(BufferedImage src, int rotationAngle) {
		double theta = (Math.PI * 2) / 360 * rotationAngle;
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dest;
		if (rotationAngle == 90 || rotationAngle == 270) {
			dest = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
		} else {
			dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		}

		Graphics2D graphics2D = dest.createGraphics();

		if (rotationAngle == 90) {
			graphics2D.translate((height - width) / 2, (height - width) / 2);
			graphics2D.rotate(theta, height / 2, width / 2);
		} else if (rotationAngle == 270) {
			graphics2D.translate((width - height) / 2, (width - height) / 2);
			graphics2D.rotate(theta, height / 2, width / 2);
		} else {
			graphics2D.translate(0, 0);
			graphics2D.rotate(theta, width / 2, height / 2);
		}
		graphics2D.drawRenderedImage(src, null);
		return dest;
	}

}
