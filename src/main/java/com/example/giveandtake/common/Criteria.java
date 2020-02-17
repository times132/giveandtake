package com.example.giveandtake.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

@Setter
@Getter
public class Criteria {

    private Integer page;

    private String type;
    private String keyword;

    public Criteria(){
        this(1);
    }

    public Criteria(Integer page){
        this.page = page;
    }

    public String[] getTypeArr(){
        return type == null ? new String[] {} : type.split("");
    }

    public String getKeyword(){
        if (keyword == null){
            return "";
        }else{
            return this.keyword;
        }
    }

    public String getUrlLink(){
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("page", getPage())
                .queryParam("type", getType())
                .queryParam("keyword", getKeyword());

        return builder.toUriString();
    }
}
