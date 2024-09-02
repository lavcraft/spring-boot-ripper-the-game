package com.sbr.web;

import com.sbr.api.SpiderStrategy;
import com.sbr.api.Thing;
import com.sbr.loading.CCL;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static java.lang.Class.forName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Import({CCL.class})
class SpiderFactoryRegistratorControllerTest {
    final MockMvc mockMvc;
    final ApplicationContext applicationContext;

    @Test
    void registerNewSpiderFactory() throws Exception {
        mockMvc.perform(post("/spiders/register").contentType(MediaType.APPLICATION_JSON).content(
                //language=json
                """
                        {
                        "pathToClass": "test"
                        }
                        """)).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.resultedClassName").value("test")).andReturn();

    }

    @Test
    void should_load_class_from_file_and_register_as_bean() throws Exception {
        assertThrows(ClassNotFoundException.class, () -> forName("strategy0.BlazinglyFastSpider"));

        byte[] bytes = Files.readAllBytes(Paths.get("/Users/tolkv/git/jugrugroup/jira/Spring Boot Ripper 2 Demo/strategy-0/target/classes/strategy0/BlazinglyFastSpider.class"));
        String classAsString = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);

        System.out.println(classAsString);


        mockMvc.perform(post("/spiders/register").contentType(MediaType.APPLICATION_JSON).content(
                //language=json
                String.format("""
                        {
                          "classBytesBase64":"%s"
                        }
                        """, classAsString))).andExpect(status().is2xxSuccessful()).andReturn();

        SpiderStrategy bean = applicationContext.getBean("1234", SpiderStrategy.class);
        assertThat(bean).isNotNull();
        assertThat(bean.gamble()).isEqualTo(Thing.PAPER);
    }
}