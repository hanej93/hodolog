package com.hodolog.api.respository;

import com.hodolog.api.domain.Post;
import com.hodolog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
