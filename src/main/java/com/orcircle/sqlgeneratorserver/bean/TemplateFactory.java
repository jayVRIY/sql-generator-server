package com.orcircle.sqlgeneratorserver.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TemplateFactory {
    private static final char PREFIX1 = '$';
    private static final char PREFIX2 = '{';
    private static final char SUFFIX = '}';
    private final String[] templateSplits;
    private final Map<String, Integer> keyWordMapper = new HashMap<>();

    public TemplateFactory(String template) throws Exception {
        char[] chars = template.toCharArray();
        var matchStartIndex = 0;
        var endIdex = 0;
        var isMatchStart = false;
        ArrayList<String> strings = new ArrayList<>();
        loop:for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case PREFIX1 -> {
                    if (chars[i + 1] != PREFIX2) {
                        throw new Exception("模版识别错误:'$'符后缺省'{'");
                    } else if (isMatchStart) {
                        throw new Exception("模版识别错误:'${'符后缺省'}'");
                    } else {
                        matchStartIndex = i;
                        strings.add(new String(Arrays.copyOfRange(chars, endIdex, matchStartIndex)));
                        isMatchStart = true;
                    }
                }
                case SUFFIX -> {
                    isMatchStart = false;
                    endIdex = i + 1;
                    String templateKey = new String(Arrays.copyOfRange(chars, matchStartIndex, endIdex));
                    strings.add(templateKey);
                    keyWordMapper.put(templateKey, strings.size() - 1);
                    var hasLeft = false;
                    for (int i1 = i; i1 < chars.length; i1++) {
                        if (i1 == PREFIX1) hasLeft = true;
                    }
                    if (!hasLeft) {
                        strings.add(new String(Arrays.copyOfRange(chars, endIdex, chars.length)));
                        break loop;
                    }
                }
            }
        }
        templateSplits = strings.toArray(new String[]{});
    }

    public String assembleTemplate(Map<String, Integer> templateMapper, String[] values) {
        String[] clone = templateSplits.clone();
        //遍历已识别模版，将模版对应的模版字符替换成上送的实际值
        keyWordMapper.forEach((k, v) -> {
            Integer matchedIndex = templateMapper.get(k);
            if (matchedIndex != null) {
                clone[v] = values[matchedIndex];
            }
        });
        StringBuilder result = new StringBuilder();
        for (String partString : clone) {
            result.append(partString);
        }
        return result.toString();
    }
}
