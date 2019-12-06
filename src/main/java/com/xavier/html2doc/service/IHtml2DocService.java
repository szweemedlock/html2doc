package com.xavier.html2doc.service;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface IHtml2DocService {

    void html2Doc(String content, OutputStream outputStream);

    void html2Docx(String content, OutputStream outputStream);

    void html2DocByFtl(Map<String, Object> map, OutputStream outputStream) throws IOException, TemplateException;

    void Html2DocxByFtl(Map<String, Object> map, OutputStream outputStream);
}
