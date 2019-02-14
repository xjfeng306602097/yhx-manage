package com.yhx.user.controller;

import com.yhx.common.annotation.ApiVersion;
import com.yhx.common.dto.BaseResponse;
import com.yhx.common.exception.BaseException;
import com.yhx.user.service.UserPidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/back/{version}/pid")
@ApiVersion("1")
@Slf4j
public class UserPidController {

    @Autowired
    private UserPidService userPidService;

    @PostMapping("/create")
    public BaseResponse<?> createPid(@RequestParam(name = "namePrefix") String namePrefix, @RequestParam(name = "start") int start, @RequestParam(name = "end") int end) {
        try {
            userPidService.createUserPid(namePrefix, start, end);
            return BaseResponse.success();
        } catch (BaseException e) {
            return BaseResponse.fail(e.getCode(), e.getMessage());
        } catch (Exception e1) {
            return BaseResponse.fail(500, e1.getMessage());
        }
    }

    @GetMapping("/sync")
    public BaseResponse<?> syncPid() {
        try {
            userPidService.syncJDPid();
            return BaseResponse.success();
        } catch (Exception e) {
            log.error("UserPidController>syncPid error:" + e.getMessage());
            return BaseResponse.fail(500, e.getMessage());
        }
    }

}
