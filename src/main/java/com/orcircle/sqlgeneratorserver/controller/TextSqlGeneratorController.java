package com.orcircle.sqlgeneratorserver.controller;

import com.orcircle.sqlgeneratorserver.bean.TextGenerateSQLDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/text")
public class TextSqlGeneratorController {
    @PostMapping("/generate-sql")
    public ResponseEntity<byte[]> textGenerateSQL(TextGenerateSQLDTO textGenerateSQLDTO) {
        try {
            byte[] bytes = textGenerateSQLDTO.getFile().getBytes();
            String s = new String(bytes);
            String[] splits = s.split(textGenerateSQLDTO.getLineSeparator());
            StringBuilder responseDataString = new StringBuilder();
            for (String split : splits) {
                String[] value = split.split(textGenerateSQLDTO.getSeparator());
                String[] clone = textGenerateSQLDTO.getTemplate().clone();
                textGenerateSQLDTO.getTemplateMap().forEach((k, v) -> {
                    clone[k] = value[v];
                });
                responseDataString.append(Arrays.toString(clone));
                responseDataString.append("\n");
            }
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-disposition", "attachment; filename=aa.txt")
                    .body(responseDataString.toString().getBytes());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(null);
        }
    }
}