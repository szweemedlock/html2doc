package com.xavier.html2doc.controller;

import com.xavier.html2doc.service.IHtml2DocService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "/")
@Data
public class IndexController {

    private Map<String, Object> data = new HashMap<>();

    private final IHtml2DocService html2DocService;

    public IndexController(IHtml2DocService html2DocService) {
        this.html2DocService = html2DocService;
    }

    @GetMapping(path = "/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @PostMapping(path = "/up")
    @ResponseBody
    public void getData(
            @RequestParam(name = "title", required = false)String title
            , @RequestParam(name = "content", required = false)String content) {
        data.put("title", title);
        data.put("content", content);
        System.out.println(123);
        this.setData(data);
    }

    @GetMapping(path = "/to")
    public void html2doc(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((UUID.randomUUID() + ".docx").getBytes("GBK"), StandardCharsets.ISO_8859_1) + "");
            response.setContentType("application/msword");
            OutputStream stream = response.getOutputStream();
            html2DocService.html2DocByFtl(this.getData(), stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
