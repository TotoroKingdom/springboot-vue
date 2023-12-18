package com.totoro.entity.vo.request;

import com.totoro.entity.BaseData;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: totoro
 * @createDate: 2023 12 18 22 33
 * @description:
 **/
@Data
@AllArgsConstructor
public class ConfirmResetVO implements BaseData {

    @Email
    String email;

    @Length(max = 6, min = 6)
    String code;

}
