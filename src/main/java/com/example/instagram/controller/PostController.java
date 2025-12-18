package com.example.instagram.controller;


import com.example.instagram.dto.request.CommentRequest;
import com.example.instagram.dto.request.PostCreateRequest;
import com.example.instagram.dto.response.CommentResponse;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.security.CustomUserDetails;
import com.example.instagram.service.CommentService;
import com.example.instagram.service.PostService;
import com.example.instagram.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;


    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("postCreateRequest", new PostCreateRequest());
        return "post/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute PostCreateRequest postCreateRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "image", required = false) MultipartFile image
            ) {
        if (bindingResult.hasErrors()) {
            return "post/form";
        }

        postService.create(postCreateRequest, image, userDetails.getId());

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = (userDetails != null) ? userDetails.getId() : null;
        PostResponse post = postService.getPost(id, userId);
        List<CommentResponse> comments = commentService.getComments(id);

        model.addAttribute("post", post);
        model.addAttribute("commentRequest", new CommentRequest());
        model.addAttribute("comments", comments);
        return "post/detail";
    }


    @PostMapping("/{postId}/comments")
    public String createComment(
            @PathVariable Long postId,
            @Valid @ModelAttribute CommentRequest commentRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            PostResponse post = postService.getPost(postId);
            List<CommentResponse> comments = commentService.getComments(postId);

            model.addAttribute("post", post);
            model.addAttribute("comments", comments);
//            model.addAttribute("commentRequest", commentCreateRequest);
            return "post/detail";
        }

        commentService.create(postId, commentRequest, userDetails.getId());


        return "redirect:/posts/" + postId;
    }

    @PostMapping("{id}/like")
    public String toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        likeService.toggleLike(id, userDetails.getId());
        return "redirect:/posts/" + id;
    }

    @PostMapping("{id}/bookmark")
    public String toggleBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        bookmarkService.toggleBookmark(id, userDetails.getId());
        return "redirect:/posts/" + id;
    }

}
