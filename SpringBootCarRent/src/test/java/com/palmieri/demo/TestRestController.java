package com.palmieri.demo;

import com.palmieri.demo.controller.WelcomeRestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(WelcomeRestController.class)

public class TestRestController {
     @Autowired
    private MockMvc mvc;

     @Test
    public void testGreetingsController() throws Exception
     {
         mvc.perform(get("/api/test")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$").value("Ciao sono il tuo primo web service"))
                 .andDo(print());
     }
     @Test
     public void testGetName() throws Exception
     {
         mvc.perform(get("/api/welcome/riccardo")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$").value("Ciao riccardo sono il tuo primo web service"))
                 .andDo(print());
     }


}
