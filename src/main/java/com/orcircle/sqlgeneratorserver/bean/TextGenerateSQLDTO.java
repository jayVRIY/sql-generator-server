package com.orcircle.sqlgeneratorserver.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class TextGenerateSQLDTO {
    private MultipartFile file;
    private String lineSeparator;
    private String separator;
    private String[] template;
    private Map<Integer, Integer> templateMap;

}
