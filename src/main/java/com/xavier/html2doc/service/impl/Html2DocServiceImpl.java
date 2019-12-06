package com.xavier.html2doc.service.impl;

import com.sun.imageio.plugins.gif.GIFImageMetadataFormat;
import com.sun.media.sound.AiffFileReader;
import com.sun.media.sound.AiffFileWriter;
import com.xavier.html2doc.service.IHtml2DocService;
import com.xavier.html2doc.util.FreeMarkUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static java.io.File.separator;

@Service
@Slf4j
public class Html2DocServiceImpl implements IHtml2DocService {



    @Override
    public void html2Doc(String content, OutputStream outputStream) {

    }

    @Override
    public void html2Docx(String content, OutputStream outputStream) {

    }

    @Override
    public void html2DocByFtl(Map<String, Object> dataMap, OutputStream outputStream) throws IOException, TemplateException {
        ZipOutputStream zipout = null;
        //图片配置文件模板
        ByteArrayInputStream documentXmlRelsInput = FreeMarkUtils.getFreemarkerContentInputStream(dataMap, "document.xml.rels");
        //内容模板
        ByteArrayInputStream documentInput = FreeMarkUtils.getFreemarkerContentInputStream(dataMap, "document.xml");
        //最初设计的模板
        File docxFile = new File(Html2DocServiceImpl.class.getClassLoader().getResource("templates").getPath() + "/document.docx");
        if (!docxFile.exists()) {
            docxFile.createNewFile();
        }
        ZipFile zipFile = new ZipFile(docxFile);
        Enumeration<? extends ZipEntry> zipEntrys = zipFile.entries();
            zipout = new ZipOutputStream(outputStream);
            //开始覆盖文档------------------
            int len = -1;
            byte[] buffer = new byte[1024];
            while (zipEntrys.hasMoreElements()) {
                ZipEntry next = zipEntrys.nextElement();
                InputStream is = zipFile.getInputStream(next);
                if (next.toString().indexOf("media") < 0) {
                    zipout.putNextEntry(new ZipEntry(next.getName()));
                    //如果是document.xml.rels由我们输入
                    if (next.getName().indexOf("document.xml.rels") > 0) {
                        if (documentXmlRelsInput != null) {
                            while ((len = documentXmlRelsInput.read(buffer)) != -1) {
                                zipout.write(buffer, 0, len);
                            }
                            documentXmlRelsInput.close();
                        }
                        //如果是word/document.xml由我们输入
                    } else if ("word/document.xml".equals(next.getName())) {
                        if (documentInput != null) {
                            while ((len = documentInput.read(buffer)) != -1) {
                                zipout.write(buffer, 0, len);
                            }
                            documentInput.close();
                        }
                    } else {
                        while ((len = is.read(buffer)) != -1) {
                            zipout.write(buffer, 0, len);
                        }
                        is.close();
                    }
                }
            }
            //写入图片
            List<Map<String, Object>> picList = (List<Map<String, Object>>) dataMap.get("picList");
            if (Objects.nonNull(picList)) {
                for (Map<String, Object> pic : picList) {
                    ZipEntry next = new ZipEntry("word" + separator + "media" + separator + pic.get("name"));
                    zipout.putNextEntry(new ZipEntry(next.toString()));
                    InputStream in = (ByteArrayInputStream)pic.get("code");
                    while ((len = in.read(buffer)) != -1) {
                        zipout.write(buffer, 0, len);
                    }
                    in.close();
                }
            }
            zipout.close();
            outputStream.close();
    }

    @Override
    public void Html2DocxByFtl(Map<String, Object> map, OutputStream outputStream) {

    }
}
