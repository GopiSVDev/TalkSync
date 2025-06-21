package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
