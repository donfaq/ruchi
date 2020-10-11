package com.donfaq.ruchi.model.twitch.api;

import lombok.Data;

import java.util.List;


@Data
public class TwitchResponse<T> {

    private List<T> data;
    private Integer total;

}
