package com.example.wordtest;

import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.LoadOptions;
import com.aspose.words.SaveFormat;
import com.google.common.collect.Lists;
import com.itextpdf.text.DocumentException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * word解析图片工具类
 * created by: FoAng
 * create time: 12/5/2023 10:35 上午
 */
@Slf4j
public class WordUtil {

    public static void merge(OutputStream ops, InputStream first, InputStream second) {
        try {
            XWPFDocument mergedDoc = new XWPFDocument();
            Stream.of(first, second).forEach(it -> {
                try {
                    XWPFDocument doc = new XWPFDocument(it);
                    // 遍历要合并的文档中的每个段落
                    for (XWPFParagraph paragraph : doc.getParagraphs()) {
                        // 创建一个新的段落
                        XWPFParagraph newParagraph = mergedDoc.createParagraph();
                        // 复制原始段落的样式
                        newParagraph.getCTP().setPPr(paragraph.getCTP().getPPr());
                        // 遍历原始段落中的每个运行元素
                        for (XWPFRun run : paragraph.getRuns()) {
                            // 创建一个新的运行元素
                            XWPFRun newRun = newParagraph.createRun();
                            // 复制原始运行元素的样式
                            newRun.getCTR().setRPr(run.getCTR().getRPr());
                            // 设置新运行元素的文本内容
                            newRun.setText(run.getText(0));
                        }
                    }
                } catch (IOException e) {
                    log.error("merge word file item error:{}", e.getMessage());
                }
            });
            mergedDoc.write(ops);
            IOUtils.close(first);
            IOUtils.close(second);
        } catch (Exception e) {
            log.error("merge word file error:{}", e.getMessage());
        }
    }

    private static boolean configWordLicense() {
        boolean result = false;
        try {
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(s.getBytes());
            com.aspose.words.License license = new com.aspose.words.License();
            license.setLicense(inputStream);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @SneakyThrows
    public static DocImageVo parseWordImageByPage(InputStream inputStream, int position, int quality) {
        if (!configWordLicense()) {
           throw new Exception("解析图片失败");
        }
        try {
            DocImageVo docImageVo = new DocImageVo();
            Document doc = new Document(inputStream);
            ImageSaveOptions options = new ImageSaveOptions(SaveFormat.PNG);
            options.setPrettyFormat(true);
            options.setUseAntiAliasing(true);
            options.setUseHighQualityRendering(true);
            options.setResolution(quality);
            int pageCount = doc.getPageCount();
            docImageVo.setTotalCount(pageCount);
            docImageVo.setCurrent(position);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            options.setPageIndex(position);
            doc.save(output, options);
            byte[] imageBytes = output.toByteArray();
            docImageVo.setContent(Base64.getEncoder().encodeToString(imageBytes));
            return docImageVo;
        } catch (Exception e) {
            log.error("word convert stream error: {}", e.getMessage());
           throw new Exception(e.getMessage());
        }
    }

    public static List<OutputStream> wordConvertStream(InputStream inputStream, int quality){
        if (!configWordLicense()) {
            return Lists.newArrayList();
        }
        try {
            Document doc = new Document(inputStream);
            ImageSaveOptions options = new ImageSaveOptions(SaveFormat.PNG);
            options.setPrettyFormat(true);
            options.setUseAntiAliasing(true);
            options.setUseHighQualityRendering(true);
            options.setResolution(quality);
            int pageCount = doc.getPageCount();

            List<OutputStream> streamList = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                options.setPageIndex(i);
                doc.save(output, options);
                streamList.add(output);
            }
            return streamList;
        } catch (Exception e) {
            log.error("word convert stream error: {}", e.getMessage());
            return Lists.newArrayList();
        }
    }

    public static byte[] wordConvertPdf(InputStream inputStream) {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        try {
            Document doc = new Document(inputStream);
            // 保存转换的pdf文件
            doc.save(ops, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ops.toByteArray();
    }

    public static List<BufferedImage> wordConvertImage(InputStream inputStream, int quality) throws Exception {
        List<OutputStream> streamList = wordConvertStream(inputStream, quality);
        List<BufferedImage> imageList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(streamList)) {
            streamList.forEach(it -> {
                try {
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(parse(it));
                    imageList.add(ImageIO.read(imageInputStream));
                } catch (Exception e) {
                    log.error("save bufferImage error:{}", e.getMessage());
                }
            });
        }
        return imageList;
    }

    private static ByteArrayInputStream parse(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
