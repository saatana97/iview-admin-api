package cn.saatana.module.system.user.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RepwdVo {
    @NotEmpty
    private String id;
    @NotEmpty
    private String pass;
    @NotEmpty
    private String newPwd;
}
