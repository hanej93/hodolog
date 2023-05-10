package com.hodolog.request;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class PostCreate {

    private String title;
    private String content;
}
