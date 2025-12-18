package com.example.instagram.controller;


import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.dto.response.UserResponse;
import com.example.instagram.entity.User;
import com.example.instagram.service.PostService;
import com.example.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchApiController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/users")
    public List<UserResponse> searchUsers(
            @RequestParam String q
    ) {
        return userService.searchUsers(q.trim());
    }

    @GetMapping("/posts")
    public Slice<PostResponse> searchPosts(
            @RequestParam String q,
            @AuthenticationPrincipal org.springframework.security.core.annotation.AuthenticationPrincipal com.example.instagram.security.CustomUserDetails userDetails,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        Long userId = (userDetails != null) ? userDetails.getId() : null;
        return postService.searchPosts(q.trim(), pageable, userId);
    }
}
