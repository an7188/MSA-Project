package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // NULL값 데이터는 제외 시킴
public class ResponseUser {
    private String email;
    private String name;
    private String userId;

    // 사용자가 주문했던 전체 주문 내역
    private List<ResponseOrder> orders;
}
