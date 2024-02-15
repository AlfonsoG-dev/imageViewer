/**
 * Copyright (C) 2024  Alfonso Gomajoa Achicanoy
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package Mundo;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.io.File;

public class TreatImage {
    private String imagePath;
    private int imageWidth, imageHeight;
    private int frameWidth, frameHeight;

    public TreatImage(String imagePath, int frameWidth, int frameHeight) {
        this.imagePath = imagePath;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }
    public ImageIcon loadImage() {
        try {
            BufferedImage readImage = ImageIO.read(new File(imagePath));
            imageWidth = readImage.getWidth() > frameWidth ?
                (frameWidth - 100) : readImage.getWidth();

            imageHeight = readImage.getHeight() > frameHeight ?
                (frameHeight - 100) : readImage.getHeight();

            Image newSize = readImage.getScaledInstance(
                    imageWidth,
                    imageHeight,
                    Image.SCALE_SMOOTH
            );
            return new ImageIcon(newSize);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Image getOriginalImage() {
        return loadImage().getImage();
    }

    public BufferedImage getBufferedImage() {
        return new BufferedImage(
                imageWidth,
                imageHeight,
                BufferedImage.SCALE_SMOOTH
        );
    }
    public void drawShapeImage(Image origen, BufferedImage copied, Rectangle captureRectangle) {
        Graphics2D g = copied.createGraphics();
        g.drawImage(
                origen,
                0,
                0,
                null
        );
        g.setColor(Color.RED);
        if(captureRectangle == null) {
            return;
        }
        g.draw(captureRectangle);

        g.setColor(
                new Color(
                    25,
                    25,
                    23,
                    10
                )
        );
        g.fill(captureRectangle);
        g.dispose();
    }
    public void drawPaintBoard(BufferedImage base) {
        Graphics2D g = base.createGraphics();
        Rectangle r = new Rectangle(
                imageWidth,
                imageHeight
        );
        g.draw(r);

        g.setColor(Color.GRAY);
        g.fill(r);
        g.dispose();
    }
    public BufferedImage getCropImageToSelection(Rectangle captureRectangle, BufferedImage copied) {
        int targetWidth = captureRectangle.width;
        int targetHeight = captureRectangle.height;
        BufferedImage croppedImage = copied.getSubimage(
                captureRectangle.x,
                captureRectangle.y,
                targetWidth,
                targetHeight
        );
        return croppedImage;
    }
}
