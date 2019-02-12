package com.yhx.manage.user.controller;

import com.yhx.manage.common.annotation.ApiVersion;
import com.yhx.manage.common.dto.BaseResponse;
import com.yhx.manage.common.exception.BaseException;
import com.yhx.manage.user.service.UserPidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/back/{version}/pid")
@ApiVersion("1")
@Slf4j
public class UserPidController {

    @Autowired
    private UserPidService userPidService;

    @PostMapping("/create/nameprefix/{nameprefix}/start/{start}/end/{end}")
    public BaseResponse<?> createPid(@PathVariable(value = "nameprefix") String namePrefix, @PathVariable(value = "start") int start, @PathVariable(value = "end") int end) {
        try {
            userPidService.createUserPid(namePrefix, start, end);
            return BaseResponse.success();
        } catch (BaseException e) {
            return BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e1) {
            return BaseResponse.fail(500, e1.getMessage());
        }
    }

}
