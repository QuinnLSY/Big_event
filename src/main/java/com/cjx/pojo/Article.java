package com.cjx.pojo;

import com.cjx.anno.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
@Data
public class Article {
    private Integer id;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,30}$")
    private  String title;
    @NotEmpty
    private  String content;
    @NotEmpty
    @URL
    private String coverImg;
    @State
    private String state;
    @NotNull
    private Integer categoryId;
    private Integer createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
