package com.orcircle.sqlgeneratorserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orcircle.sqlgeneratorserver.bean.TemplateFactory;
import com.orcircle.sqlgeneratorserver.bean.TextGenerateSQLDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/text")
@Slf4j
public class TextSqlGeneratorController {
    @PostMapping("/generate-sql")
    public ResponseEntity<byte[]> textGenerateSQL(@ModelAttribute TextGenerateSQLDTO textGenerateSQLDTO) {
        try {
            log.info("进入上传-------{}", textGenerateSQLDTO.toString());
            //读取文件
            byte[] bytes = textGenerateSQLDTO.getFile().getBytes();
            String s = new String(bytes);
            String[] splits = s.split(textGenerateSQLDTO.getLineSeparator());
            //读取模版-值对应表
            String templateMapString = textGenerateSQLDTO.getTemplateMapString();
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object> hashMap = objectMapper.readValue(templateMapString, HashMap.class);
            textGenerateSQLDTO.setTemplateMap(hashMap);
            //创建模版工厂
            TemplateFactory templateFactory = new TemplateFactory(textGenerateSQLDTO.getTemplate());
            //循环填充返回参数
            StringBuilder responseDataString = new StringBuilder();
            for (String split : splits) {
                String[] values = split.split(textGenerateSQLDTO.getSeparator());
                responseDataString.append(templateFactory.assembleTemplate(textGenerateSQLDTO.getTemplateMap(), values));
                responseDataString.append("\n");
            }
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-disposition", "attachment; filename=toRun.sql")
                    .body(responseDataString.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("文件上传失败".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("业务异常".getBytes());
        }
    }
}