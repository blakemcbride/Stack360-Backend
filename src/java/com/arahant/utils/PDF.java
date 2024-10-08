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


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Author: Blake McBride
 * Date: 3/5/16
 */
public class PDF {
    private PDDocument doc;
    private PDPageContentStream contentStream = null;
    private float posx=0, posy=0;
    private float fontSize = 11f;
    private PDFont font = PDType1Font.COURIER;
    private PDPage page;
    private boolean landscape = false;
    private float pageHeight, pageWidth;
    private String outputFilename;
    private PDRectangle pageSize = PDRectangle.LETTER;
    private boolean inText = false;

    /**
     * Begin a new PDF file
     *
     * @param fname the file name to be saved to (include the .pdf)
     */
    public PDF(String fname) {
        outputFilename = fname;
        doc = new PDDocument();
    }

    /**
     * Begin a new PDF file with an existing PDF file as its template.
     *
     * @param infile the name of the input PDF template file
     * @param outfile the file name to be saved to (include the .pdf)
     * @throws IOException
     */
    public PDF(String infile, String outfile) throws IOException {
        outputFilename = outfile;
        doc = PDDocument.load(new File(infile));
    }

    /**
     * Set font style and size
     *
     * @param fnt font style
     * @param fs font size in points
     */
    public void setFont(PDFont fnt, float fs) {
        font = fnt;
        fontSize = fs;
        if (contentStream != null) {
            try {
                contentStream.setFont(font, fontSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void landscape() {
        landscape = true;
    }

    public void portrait() {
        landscape = false;
    }

    public PDRectangle setPageSize(PDRectangle ps) {
        PDRectangle old = pageSize;
        pageSize = ps;
        return old;
    }

    /**
     * Output txt at line y, column x
     * Lines and column numbers take font into account so, for example, typically letter paper would
     * give 66 lines and 80 columns.
     *
     * @param y absolute line position top to bottom, line 1 to line ...
     * @param x absolute column position left to right, column 1 to ...
     * @param txt the text to be written
     */
    public void textOut(int y, int x, String txt) {
        float absx, absy, relx, rely;
        if (fontSize > 11.9f  &&  fontSize < 12.1f)
            absx = (x+1) * 7.2f;  //  12pt Courier font
        else  if (fontSize > 10.9f  &&  fontSize < 11.1f)
            absx = (x+3) * 6.6f;  //  11pt Courier font
        else
            absx = (x+6) * 6f;  //  10pt Courier font
        absy = pageHeight - (y * 12f) + 2f;
        try {
            if (!inText) {
                contentStream.beginText();
                contentStream.setNonStrokingColor(0);
                inText = true;
                posy = posx = 0;
            }
            relx = absx - posx;
            rely = absy - posy;
            contentStream.newLineAtOffset(relx, rely);
            contentStream.showText(txt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        posx = absx;
        posy = absy;
    }

    /**
     * Output txt at dot position y, dot position x
     *
     * @param absy absolute dot position, top to bottom
     * @param absx absolute dot position, left to right
     * @param txt the text to be written
     */
    public void textOutpx(float absy, float absx, String txt) {
        float relx, rely;
        absy = pageHeight - absy;
        try {
            if (!inText) {
                contentStream.beginText();
                contentStream.setNonStrokingColor(0);
                inText = true;
                posy = posx = 0;
            }
            relx = absx - posx;
            rely = absy - posy;
            contentStream.newLineAtOffset(relx, rely);
            contentStream.showText(txt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        posx = absx;
        posy = absy;
    }

    /**
     * End current page and start a new page
     */
    public void newPage() {
        try {
            if (contentStream != null) {
                if (inText)
                    contentStream.endText();
                contentStream.close();
            }
            page = new PDPage(pageSize);
            contentStream = new PDPageContentStream(doc, page, AppendMode.OVERWRITE, false);
            contentStream.setFont(font, fontSize);
            doc.addPage(page);
            posx = 0;
            posy = 0;
            contentStream.beginText();
            contentStream.setNonStrokingColor(0);
            inText = true;
            if (landscape) {
                page.setRotation(90);
                PDRectangle pageSize = page.getMediaBox();
                pageHeight = pageSize.getWidth();  //  height <- width is correct!
                pageWidth  = pageSize.getHeight(); //  width <- height is correct!
                try {
                    contentStream.transform(new Matrix(0, 1, -1, 0, pageHeight, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                PDRectangle pageSize = page.getMediaBox();
                pageHeight = pageSize.getHeight();
                pageWidth = pageSize.getWidth();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get an existing page.
     *
     * @param n the page number to get (starting at 0)
     */
    public void getPage(int n) {
        try {
            if (contentStream != null) {
                if (inText)
                    contentStream.endText();
                contentStream.close();
            }
            page = doc.getPage(n);
            contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
            contentStream.setFont(font, fontSize);
            posx = 0;
            posy = 0;
            contentStream.beginText();
            contentStream.setNonStrokingColor(0);
            inText = true;
            if (landscape) {
                page.setRotation(90);
                PDRectangle pageSize = page.getMediaBox();
                pageHeight = pageSize.getWidth();  //  height <- width is correct!
                pageWidth  = pageSize.getHeight(); //  width <- height is correct!
                try {
                    contentStream.transform(new Matrix(0, 1, -1, 0, pageHeight, 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                PDRectangle pageSize = page.getMediaBox();
                pageHeight = pageSize.getHeight();
                pageWidth = pageSize.getWidth();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw a line
     *
     * @param ya upper left  y point
     * @param xa upper left x point
     * @param yb lower right y point
     * @param xb lower right x point
     * @param thickness line thickness (-1 == no outside line)
     */
    public void drawLine(float ya, float xa, float yb, float xb, float thickness) {
        try {
            if (inText) {
                contentStream.endText();
                inText = false;
            }
            contentStream.setLineWidth(thickness);
            contentStream.moveTo(xa, pageHeight-ya);
            contentStream.lineTo(xb, pageHeight-yb);
            contentStream.stroke();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw a rectangle
     *
     * @param ya upper left y point
     * @param xa upper left x point
     * @param yb lower right y point
     * @param xb lower right x point
     * @param thickness line thickness (-1 == no outside line)
     * @param fill fill percent, -1=no fill, otherwise 0-255 where 0 is black and 255 is white
     */
    public void drawRect(float ya, float xa, float yb, float xb, float thickness, int fill) {
        try {
            if (inText) {
                contentStream.endText();
                inText = false;
            }

            contentStream.addRect(xa, pageHeight-ya, xb-xa, ya-yb);

            if (fill > -1) {
                if (thickness > .01) {
                    contentStream.setLineWidth(thickness);
                    contentStream.setNonStrokingColor(fill);
                    contentStream.fillAndStroke();
                } else {
                    contentStream.setNonStrokingColor(fill);
                    contentStream.fill();
                }
            } if (thickness > .01) {
                contentStream.setLineWidth(thickness);
                contentStream.stroke();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Output image file to PDF
     *  (currently only tested with png files)
     *
     * @param ypos place lower left corner of image at y position
     * @param xpos place lower left corner of image at x position
     * @param scale scale image (1.0f means no scaling)
     * @param filename name of file holding image
     */
    public void imageOut(float ypos, float xpos, float scale, String filename) {
        try {
            if (inText) {
                contentStream.endText();
                inText = false;
            }
            PDImageXObject pdImage = PDImageXObject.createFromFile(filename, doc);
            contentStream.drawImage(pdImage, xpos, pageHeight-ypos, pdImage.getWidth()*scale, pdImage.getHeight()*scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Output scaled image to PDF
     *
     * @param ypos place lower left corner of image at y position
     * @param xpos place lower left corner of image at x position
     * @param scale scale image (1.0f means no scaling)
     * @param image the image
     */
    public void imageOut(float ypos, float xpos, float scale, byte [] image) {
        try {
            if (inText) {
                contentStream.endText();
                inText = false;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(image);
            BufferedImage bim = ImageIO.read(bais);
            bais.close();
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bim);
            contentStream.drawImage(pdImage, xpos, pageHeight-ypos, pdImage.getWidth()*scale, pdImage.getHeight()*scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Output boxed image to PDF
     *
     * @param ypos place lower left corner of image at y position
     * @param xpos place lower left corner of image at x position
     * @param ypos2 place upper right corner of image at y position
     * @param xpos2 place upper right corner of image at x position
     * @param image the image
     */
    public void imageOut(float ypos, float xpos, float ypos2, float xpos2, byte [] image) {
        try {
            if (inText) {
                contentStream.endText();
                inText = false;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(image);
            BufferedImage bim = ImageIO.read(bais);
            bais.close();
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bim);

            // Figure out how to scale the image to fit in the box while keeping the aspect ratio
            int width = pdImage.getWidth();
            int height = pdImage.getHeight();
            float maxHeight = ypos - ypos2;
            float maxWidth = xpos2 - xpos;
            float yscale = maxHeight / height;
            float xscale = maxWidth / width;
            float scale = Math.min(yscale, xscale);

            contentStream.drawImage(pdImage, xpos, pageHeight-ypos, width*scale, height*scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endText() {
        try {
            if (inText) {
                contentStream.endText();
                inText = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * End current page and end document
     */
    public void endDocument() {
        try {
            if (contentStream != null) {
                if (inText)
                    contentStream.endText();
                contentStream.close();
            }
            doc.save(outputFilename);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (doc != null) {
                    doc.close();
                    doc = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public PDDocument getDoc() {
        return doc;
    }

    public PDPage getPage() {
        return page;
    }

    public PDPageContentStream getContentStream() {
        return contentStream;
    }

    public void grid() {
        float y, x;
        float yo = 16, xo = 16;  // page offsets

        setFont(PDType1Font.COURIER, 7);
        // left side marks
        for (y=0 ; y <= pageHeight ; y += 10)
            if (0 == y%100) {
                if (y != 0)
                    textOutpx(y-1, xo+3, "" + (int) y);
                drawLine(y, xo, y, xo+20, .5f);
            } else if (0 == y%50) {
                drawLine(y, xo, y, xo+15, .5f);
            } else
                drawLine(y, xo, y, xo+10, .5f);
        // right side marks
        for (y=0 ; y <= pageHeight ; y += 10)
            if (0 == y%100) {
                if (y != 0)
                    textOutpx(y-1, pageWidth-xo-18, "" + (int) y);
                drawLine(y, pageWidth-xo-20, y, pageWidth-xo, .5f);
            } else if (0 == y%50) {
                drawLine(y, pageWidth-xo-15, y, pageWidth-xo, .5f);
            } else
                drawLine(y, pageWidth-xo-10, y, pageWidth-xo, .5f);
        //  top marks
        for (x=0 ; x <= pageWidth ; x += 10)
            if (0 == x%100) {
                if (x != 0)
                    textOutpx(yo+16, x+2, "" + (int) x);
                drawLine(yo, x, yo+20, x, .5f);
            } else if (0 == x%50) {
                drawLine(yo, x, yo+15, x, .5f);
            } else
                drawLine(yo, x, yo+10, x, .5f);
        //  bottom marks
        for (x=0 ; x <= pageWidth ; x += 10)
            if (0 == x%100) {
                if (x != 0)
                    textOutpx(pageHeight-yo-16, x+2, "" + (int) x);
                drawLine(pageHeight-yo-20, x, pageHeight-yo, x, .5f);
            } else if (0 == x%50) {
                drawLine(pageHeight-yo-15, x, pageHeight-yo, x, .5f);
            } else
                drawLine(pageHeight-yo-10, x, pageHeight-yo, x, .5f);
    }

    public static void main(String[] args) throws IOException {
        test4();
    }

    private static void test1() {
        PDF pdf = new PDF("mypdf.pdf");

//        pdf.landscape();

        pdf.newPage();

        pdf.textOutpx(100, 100, "100x100");
        pdf.textOutpx(300, 100, "300x100");
        pdf.textOutpx(200, 100, "200x100");
        pdf.textOutpx(300, 300, "300x300");

        pdf.imageOut(100, 200, 1, "WayToGo.png");

        pdf.newPage();
        pdf.textOutpx(400, 200, "400x200");

        pdf.endDocument();
    }

    private static void test2() {
        PDF pdf = new PDF("mypdf.pdf");
        pdf.newPage();

        for (int line=1 ; line <= 66 ; line++)
            pdf.textOut(line, 20, line + "x20");
        pdf.textOut(25, 40, "25x40x");

        pdf.drawLine(100, 100, 200, 200, 2);
        pdf.drawRect(300, 300, 400, 400, 5, 200);
        pdf.drawLine(250, 200, 250, 250, 2);

        pdf.textOut(1, 1, "1234567890123456789");

        pdf.endDocument();
    }

    private static void test3() {
        PDF pdf = new PDF("mypdf.pdf");
//        pdf.landscape();
        pdf.newPage();
        pdf.grid();

        pdf.imageOut(300, 100, .2f, "WayToGo.png");

        pdf.endDocument();
    }

    private static void test4() throws IOException {
        PDF pdf = new PDF("/home/blake/Desktop/20200320 Vendor Letter KNH.pdf", "/home/blake/Desktop/res.pdf");
        pdf.getPage(0);
        //       pdf.grid();
        pdf.setFont(PDType1Font.COURIER, 12);
        pdf.textOutpx(230, 250, "Blake McBride");
        pdf.endDocument();
    }

}
