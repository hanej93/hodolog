package com.hodolog.api.controller;

import com.hodolog.api.request.PostCreate;
import com.hodolog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate postCreate) {
        // case1. 저장한 데이터 Entity -> response로 응답하기
        // case2. 저장한 데이터의 primary id -> response로 응답하기
        //          -> client 에서는 수신한 id를 글 조회 API를 통해서 글 데이터를 수신받음
        // case3. 응답 필요 없음 -> client에서 모든 post(글) 데이터 context를 잘 관리함
        // Bad Case: 서버에서 -> 반드시 이렇게 할겁니다! fix
        //              -> 서버에서 차라리 유연하게 대응하는게 좋음.
        //              -> 한 번에 일괄적으로 잘 처리되는 케이스는 없음 -> 잘 관리하는 형태가 중요
        postService.write(postCreate);
    }
}
