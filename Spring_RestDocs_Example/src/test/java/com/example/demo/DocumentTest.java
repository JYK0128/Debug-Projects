package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class DocumentTest {
    private MockMvc mockMvc;

    // for other unit test package not "Junit", create Manual document
    //      private ManualRestDocumentation restDocumentation = new ManualRestDocumentation();


    // there is setting to server side test using MockMvc.
    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void PeopleApi() throws Exception {
        this.mockMvc.perform(get("/people")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("/people",
                        links(
                                linkWithRel("self").ignored(),
                                linkWithRel("profile").ignored(),
                                linkWithRel("search").description("the List of People API"))
                ));
    }

    //path Parameters의 경우, MockHttpServletRequestBuilder 대신 RestDocumentationRequestBuilders 사용.
    @Test
    public void PeopleDetail() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/people/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("/people/id",
                        pathParameters(
                                parameterWithName("id").description("User detail")
                        )
                ));
    }

    @Test
    public void PeopleApi_SearchList() throws Exception {
        this.mockMvc.perform(get("/people/search")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("/people/search", links(
                        linkWithRel("self").ignored(),
                        linkWithRel("findById").ignored(),
                        linkWithRel("findCustomById").description("insert parameter"),
                        linkWithRel("IgnoreCase").description("insert parameter")
                )));
    }

    @Test
    public void PeopleApi_FindCustomById() throws Exception {
        this.mockMvc.perform(get("/people/search/findCustomById").param("id","1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("/people/search/findCustomById",
                        requestParameters(
                                parameterWithName("id").description("아이디")
                )));
    }
}