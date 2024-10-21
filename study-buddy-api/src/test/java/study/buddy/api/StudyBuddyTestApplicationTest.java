package study.buddy.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Used for testing
@SpringBootTest
@AutoConfigureMockMvc
public class StudyBuddyTestApplicationTest {

    @Autowired
    private MockMvc mock;
//    @Test
//    public void shouldBeDefaultMessage() throws Exception {
//        this.mock.perform(get("/")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("This is a test")));
//    }
//
//    @Test
//    public void contextLoads() {
//    }
}
