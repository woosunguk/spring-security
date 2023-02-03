package com.example.springsecurity.controller;

import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class SecurityController {
  @GetMapping("/profile")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  @ResponseBody
  public String profile(Principal principal, Authentication authentication, Authorization authorization) {
    log.error("pricipal : {}, {}", principal, principal.getClass().getName());
    log.error("authentication : {}, {}", authentication, authentication.getName());
    log.error("authorization : {}", authorization);

    return principal.getName();
  }

  @GetMapping("/users")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Users";
  }
}
