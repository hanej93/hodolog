package com.hodolog.api.service;

import com.hodolog.api.domain.Post;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import com.hodolog.api.respository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertThat(postRepository.count()).isEqualTo(1L);

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("1234567890123456")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("1234567890");
        assertThat(response.getContent()).isEqualTo("bar");
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                    Post.builder()
                            .title("호돌맨 제목 " + i)
                            .content("반포자이 " + i)
                            .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(2)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertThat(posts.size()).isEqualTo(10L);
        assertThat(posts.get(0).getTitle()).isEqualTo("호돌맨 제목 20");
        assertThat(posts.get(9).getTitle()).isEqualTo("호돌맨 제목 11");
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
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

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. io=" + post.getId()));
        assertThat(changedPost.getTitle()).isEqualTo("호돌걸");
        assertThat(changedPost.getContent()).isEqualTo("반포자이");
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. io=" + post.getId()));
        assertThat(changedPost.getTitle()).isEqualTo("호돌맨");
        assertThat(changedPost.getContent()).isEqualTo("초가집");
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("글 1개 조회 - 조회하지 않는 글")
    void test7() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> postService.get(post.getId() + 1))
//                .withMessage("존재하지 않는 글입니다.");

        assertThatThrownBy(() -> postService.get(post.getId() + 1))
                .isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("게시글 삭제- 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expect
        assertThatThrownBy(() -> postService.delete(post.getId() + 1))
                .isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        // expect
        assertThatThrownBy(() -> postService.edit(post.getId() + 1, postEdit))
                .isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("글 내용 수정")
    void test10() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. io=" + post.getId()));
        assertThat(changedPost.getTitle()).isEqualTo("호돌맨");
        assertThat(changedPost.getContent()).isEqualTo("초가집");
    }
}