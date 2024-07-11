package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App {

    static Tesseract tesseract;
    public static void main( String[] args ) {
        initTesseract(App.class.getResource("/data").getPath());
        try(InputStream inputStream = App.class.getResourceAsStream("/order.jpg")) {
            String result = recognizeText(inputStream);
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initTesseract(String dataPath) {
        tesseract = new Tesseract();
        // 设置训练数据文件夹路径
        tesseract.setDatapath(dataPath);
        // 设置为中文简体
        tesseract.setLanguage("chi_sim");
    }

    public static String recognizeText(InputStream sbs) {
        // 转换
        try {
            BufferedImage bufferedImage = optimize(ImageIO.read(sbs));
            // 对图片进行文字识别
            return tesseract.doOCR(bufferedImage);
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 图片优化，提高识别率:
     * getScaledInstance 放大图片
     * getSubImage 截取图片
     * convertImageToBinary 转二进制
     * convertImageToGrayscale 将图像转换为灰度
     * invertImageColor 反转图像颜色
     * rotateImage 旋转影像
     */
    private static BufferedImage optimize(BufferedImage bufferedImage) throws IOException {
        // 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分
        bufferedImage = ImageHelper.convertImageToGrayscale(bufferedImage);
        // 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率
        bufferedImage = ImageHelper.convertImageToBinary(bufferedImage);
        // 图片放大5倍,增强识别率(很多图片本身无法识别,放大7倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大7倍)
        bufferedImage = ImageHelper.getScaledInstance(bufferedImage, bufferedImage.getWidth() * 7, bufferedImage.getHeight() * 7);
        return bufferedImage;
    }
}
