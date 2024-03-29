package com.hodolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.api.domain.Post;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.respository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 요청시 요청시 빈 값")
    void test() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String request = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다.")
                .build();
        String request = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String request = objectMapper.writeValueAsString(postCreate);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test5() throws Exception {
        // given
        List<Post> requestposts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("호돌맨 제목 " + i)
                                .content("반포자이 " + i)
                                .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(requestposts);

        // expected
        mockMvc.perform(get("/posts")
                        .param("page", "2")
                        .param("size", "10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()", is(10)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 20"))
                .andExpect(jsonPath("$[0].content").value("반포자이 20"))
                .andExpect(jsonPath("$[4].title").value("호돌맨 제목 16"))
                .andExpect(jsonPath("$[4].content").value("반포자이 16"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        List<Post> requestposts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("호돌맨 제목 " + i)
                                .content("반포자이 " + i)
                                .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(requestposts);

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(10L))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 30"))
                .andExpect(jsonPath("$[0].content").value("반포자이 30"))
                .andExpect(jsonPath("$[4].title").value("호돌맨 제목 26"))
                .andExpect(jsonPath("$[4].content").value("반포자이 26"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        String request = objectMapper.writeValueAsString(postEdit);

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();
        String request = objectMapper.writeValueAsString(postEdit);

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성 시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("반포자이")
                .build();
        String request = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}