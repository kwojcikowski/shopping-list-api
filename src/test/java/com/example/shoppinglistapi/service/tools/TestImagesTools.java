package com.example.shoppinglistapi.service.tools;


import com.example.shoppinglistapi.dto.product.ImageReadDto;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@Import({ImagesTools.class})
@TestPropertySource(properties = {"app.resource-dir=src/test/resources/img"})
public class TestImagesTools {

    @Value("${app.resource-dir}")
    private String testResourceDir;

    @Test
    public void testForExistenceOfTestImage(){
        Path path = FileSystems.getDefault().getPath(testResourceDir, ImagesTools.NO_IMAGE_FILENAME);
        File imageFile = new File(path.toAbsolutePath().toString());
        assertThat(imageFile.exists()).isTrue();
    }

    @Test
    public void testForExistenceOfNoImage(){
        Path path = FileSystems.getDefault().getPath("src", "main", "resources", "img",
                ImagesTools.NO_IMAGE_FILENAME);
        File imageFile = new File(path.toAbsolutePath().toString());
        assertThat(imageFile.exists()).isTrue();
    }

    @Test
    public void testSaveImageFromURLReturnImageCorrectImage(){
        Path path = FileSystems.getDefault().getPath(testResourceDir);
        String srcPath = path.toAbsolutePath() + File.separator + ImagesTools.NO_IMAGE_FILENAME;
        String destPath =  "test.png";
        File savedFile = null;
        try {
            savedFile = ImagesTools.saveImageFromURL("file:///" + srcPath, destPath);
        } catch (IOException e) {
            fail("Exception should not had been thrown.");
        }
        assertThat(savedFile).isNotNull();
        try {
            assertThat(FileUtils.contentEquals(savedFile, new File(srcPath))).isTrue();
        } catch (IOException e) {
            fail("Exception should not had been thrown.");
        }
        if(!savedFile.delete()){
            fail("Could not delete testing image.");
        }
    }

    @Test
    public void testGenerateImageThumbnailCheckIfDimensionsAreCorrect(){
        Path path = FileSystems.getDefault().getPath(testResourceDir);
        String srcPath = path.toAbsolutePath() + File.separator + ImagesTools.NO_IMAGE_FILENAME;
        File initialFile = new File(srcPath);
        File thumbnailFile = null;
        try {
            thumbnailFile = ImagesTools.generateImageThumbnail(initialFile);
            BufferedImage image = ImageIO.read(thumbnailFile);
            assertAll(() -> assertThat(image.getWidth()).isEqualTo(ImagesTools.THUMBNAIL_WIDTH),
                    () -> assertThat(image.getHeight()).isEqualTo(ImagesTools.THUMBNAIL_HEIGHT));
        } catch (IOException e) {
            fail("Exception should not had been thrown");
        }
        assertThat(thumbnailFile).isNotNull();
        if(!thumbnailFile.delete()){
            fail("Could not delete testing image.");
        }
    }

    @Test
    public void testGetImageFromLocalResourcesReturnCorrectImage(){
        Path path = FileSystems.getDefault().getPath(testResourceDir);
        String srcPath = path.toAbsolutePath() + File.separator + ImagesTools.NO_IMAGE_FILENAME;
        String fileType = ImagesTools.NO_IMAGE_FILENAME.split("\\.")[1];
        try {
            BufferedImage initialImage = ImageIO.read(new File(srcPath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(initialImage, fileType, bos);

            ImageReadDto resultImage = ImagesTools.getImageFromLocalResources(ImagesTools.NO_IMAGE_FILENAME);
            assertThat(resultImage.getImage()).isEqualTo(bos.toByteArray());
        } catch (IOException e) {
            fail("Exception should not had been thrown.");
        }
    }

    @Test
    public void testGetImageFromLocalResourcesReturnNoImageForNonExistingName(){
        Path path = FileSystems.getDefault().getPath(testResourceDir);
        String srcPath = path.toAbsolutePath() + File.separator + ImagesTools.NO_IMAGE_FILENAME;
        String fileType = ImagesTools.NO_IMAGE_FILENAME.split("\\.")[1];
        try {
            BufferedImage initialImage = ImageIO.read(new File(srcPath));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(initialImage, fileType, bos);

            ImageReadDto resultImage = ImagesTools.getImageFromLocalResources("s0m3r4nd0m1m4g3.png");
            assertThat(resultImage.getImage()).isEqualTo(bos.toByteArray());
        } catch (IOException e) {
            fail("Exception should not had been thrown.");
        }
    }
}
