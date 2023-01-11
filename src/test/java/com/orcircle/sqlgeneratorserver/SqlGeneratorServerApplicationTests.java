package com.orcircle.sqlgeneratorserver;

import com.orcircle.sqlgeneratorserver.bean.TemplateFactory;
import org.junit.jupiter.api.Test;


class SqlGeneratorServerApplicationTests {
    @Test
    void contextLoads() throws Exception {
        String s = "inset into ${sas dd ss} ${asdfd} ";
        new TemplateFactory(s);
    }

}
