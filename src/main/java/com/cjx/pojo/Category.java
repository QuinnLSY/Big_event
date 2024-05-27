package com.cjx.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Category {
    @NotNull(groups = {Update.class})
    private  Integer id;
    @NotEmpty
    private  String categoryName;
    @NotEmpty
    private  String categoryAlias;
    private Integer createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public interface Add extends Default {

    }

    public interface Update extends Default{

    }
}
