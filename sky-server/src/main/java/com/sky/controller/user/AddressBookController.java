package com.sky.controller.user;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AddressBookController")
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "地址簿相关接口")
public class AddressBookController {
}
