/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageHelper {

    public BufferedImage CreateImageFromTextString(String word, Font font, int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics2d = image.createGraphics();

        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2d.setFont(font);

        graphics2d.setColor(Color.white);
        graphics2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics2d.setColor(new Color(0, 0, 0));

        FontMetrics fontmetrics = graphics2d.getFontMetrics();

        graphics2d.drawString(word, 0, fontmetrics.getAscent());

        graphics2d.dispose();

        return image;
    }

    public void SaveImageAsPNGFile(BufferedImage image, String imageFile) throws IOException {
        ImageIO.write(image, "png", new File(imageFile));
    }

    public BufferedImage ResizeImage(BufferedImage image, int targetWidth, int targetHeight) {

        BufferedImage imageBuffer = new BufferedImage(targetWidth,
                targetHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics2d = imageBuffer.createGraphics();

        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics2d.drawImage(image, 0, 0, imageBuffer.getWidth(), imageBuffer.getHeight(), null);
        graphics2d.dispose();

        return imageBuffer;
    }

    public ImageSize ComputerMaxTextImageSizeAllFonts(String words_file, List<Font> allFontsList) throws IOException, FontFormatException {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(words_file), "UTF8"));

        String word;
        ImageSize imageSize = new ImageSize();

        int maxImageWidth = 0;
        int maxImageHeight = 0;

        while ((word = br.readLine()) != null) {
            for (Font font : allFontsList) {
                imageSize = ComputeTextImageSizeSingleFont(word, font);

                //get max width and height to be used for resizing if required
                if (imageSize.Width > maxImageWidth) {
                    maxImageWidth = imageSize.Width;
                }
                if (imageSize.Height > maxImageHeight) {
                    maxImageHeight = imageSize.Height;
                }
            }
        }

        imageSize.Width = maxImageWidth;
        imageSize.Height = maxImageHeight;

        return imageSize;
    }

    public ImageSize ComputeTextImageSizeSingleFont(String word, Font font) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics2d = image.createGraphics();

        graphics2d.setFont(font);

        FontMetrics fontmetrics = graphics2d.getFontMetrics();
        int width = fontmetrics.stringWidth(word);
        int height = fontmetrics.getHeight();

        ImageSize imageSize = new ImageSize();
        imageSize.Width = width;
        imageSize.Height = height;

        graphics2d.dispose();

        return imageSize;
    }

    public int getMaxFittingFontSize(String word, Font font, int width, int height) {
        int minSize = 0;
        int maxSize = 288;
        int curSize = font.getSize();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics2d = image.createGraphics();

        while (maxSize - minSize > 2) {
            FontMetrics fm = graphics2d.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
            int fontWidth = fm.stringWidth(word);
            int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();

            if ((fontWidth > width) || (fontHeight > height)) {
                maxSize = curSize;
                curSize = (maxSize + minSize) / 2;
            } else {
                minSize = curSize;
                curSize = (minSize + maxSize) / 2;
            }
        }//end while
        
        graphics2d.dispose();

        return curSize;
    }

}
