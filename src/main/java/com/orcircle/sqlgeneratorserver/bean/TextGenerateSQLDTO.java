package com.orcircle.sqlgeneratorserver.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Data
public class TextGenerateSQLDTO {
    private MultipartFile file;
    private String lineSeparator;
    private String separator;
    private String template;
    private HashMap<String, Object> templateMap;
    private String templateMapString;

}
