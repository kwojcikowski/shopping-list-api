package com.example.shoppinglistapi.service.tools;

import com.example.shoppinglistapi.dto.ImageDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ImagesTools {

    public static final int THUMBNAIL_HEIGHT = 50;
    public static final int THUMBNAIL_WIDTH = 50;
    public static final String NO_IMAGE_FILENAME = "no_image.png";

    public static File saveImageFromURL(String url, String destinationPath) throws IOException {
        try {
            URL imageUrl = new URL(url);
            ReadableByteChannel readableByteChannel = Channels.newChannel(imageUrl.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel()
                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            readableByteChannel.close();
            fileOutputStream.close();
            fileChannel.close();
            return new File(destinationPath);
        }catch (IOException e){
            throw new IOException("Error saving image from URL", e.getCause());
        }
    }

    public static File generateImageThumbnail(File source) throws IOException {
        String imagePath = source.getAbsolutePath();
        String fileType = imagePath.substring(imagePath.lastIndexOf("."));
        String thumbnailPath = imagePath.substring(0, imagePath.lastIndexOf("."))
                + "_thumbnail" + fileType;

        try {
            BufferedImage img = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
            img.createGraphics().drawImage(ImageIO.read(source)
                    .getScaledInstance(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
            File outputFile = new File(thumbnailPath);
            ImageIO.write(img, fileType.replaceAll("\\.", ""), outputFile);
            return outputFile;
        }catch(IOException e) {
            throw new IOException("Error creating thumbnail image");
        }
    }


    public static ImageDTO getImageFromLocalResources(String fileName) throws IOException {
        Path imagePath = FileSystems.getDefault().getPath("src","main", "resources", "img", fileName);
        File f = new File(imagePath.toString());
        String fileType;
        BufferedImage image;
        try {
            //If throws exception then incorrect name given.
            fileType = f.getName().split("\\.")[1];
            //If throws exception then non readable file given.
            image = ImageIO.read(f);
        }catch (IOException | ArrayIndexOutOfBoundsException e){
            return getImageFromLocalResources(NO_IMAGE_FILENAME);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, fileType, bos);
        return ImageDTO.builder().width(image.getWidth()).height(image.getHeight()).image(bos.toByteArray()).build();
    }
}
