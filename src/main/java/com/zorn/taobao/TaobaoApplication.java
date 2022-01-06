package com.zorn.taobao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zorn.taobao.mapper")
@SpringBootApplication
public class TaobaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaobaoApplication.class, args);
    }

}
