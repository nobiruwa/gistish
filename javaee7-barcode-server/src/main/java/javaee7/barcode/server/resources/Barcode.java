package javaee7.barcode.server.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pao.barcode.EAN128;
import pao.barcode.GraphicsUnit;

// http://localhost:8080/javaee7-barcode-server/resources/
@Path("barcode")
public class Barcode {
    private static final int WKIND_DRAW = 0;
    private static final int WKIND_DIRECT = 1;
    private static final int WKIND_DELICATE = 2;

    public GraphicsUnit getGraphicsUnit() {
        return GraphicsUnit.PIXEL;
    }

    @GET
    @Produces("image/png")
    public byte[] getGreeting() {
        float x = 25;
        float y = 45;
        float w = 250;
        float h = 50;
        int width = 800;
        int height = 600;
        int dot = 0; // 0, 1, -1を試す

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // pao.barcode.IBarCode barcode = null;
        int fontSize = 9;
        Font s_font = new Font(null, Font.PLAIN, fontSize);
        int dpi = 600;

        String barcodeString = "{FNC1}0114912345678904{FNC1}17990101{FNC1}301000{FNC1}1012345678";
        EAN128 conveniBarcode = new EAN128((Graphics2D) g);
        conveniBarcode.setTextWrite(true);
        conveniBarcode.setTextKintou(true);
        conveniBarcode.setTextFont(s_font);
        conveniBarcode.setDPI(dpi);
        conveniBarcode.setKuroBarChousei(dot);

        int drawType = WKIND_DRAW;

        if (drawType == WKIND_DRAW) {
            // Draw
            conveniBarcode.drawConvenience(barcodeString, x, y, w, h, getGraphicsUnit(), dpi, false);
        } else if (drawType == WKIND_DIRECT) {
            // DrawDirect
            conveniBarcode.drawConvenienceDirect(barcodeString, x, y, w, h, getGraphicsUnit(), dpi, false);
        } else if (drawType == WKIND_DELICATE) {
            // DrawDelicate
            conveniBarcode.drawConvenienceDelicate(barcodeString, x, y, w, h);
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
