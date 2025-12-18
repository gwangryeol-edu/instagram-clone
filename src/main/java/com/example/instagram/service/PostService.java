package com.example.instagram.service;

import com.example.instagram.dto.request.PostCreateRequest;
import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse create(PostCreateRequest postCreateRequest, MultipartFile image, Long userId);
    Post findById(Long postId);
    PostResponse getPost(Long postId, Long userId);
    List<PostResponse> getAllPosts(Long userId);
    List<PostResponse> getPostsByUsername(String username, Long userId);
    long countByUserId(Long userId);
    List<PostResponse> getAllPostsWithStats(Long userId);

    // 피드 조회
    Slice<PostResponse> getFeedPosts(Long userId, Pageable pageable);

    // 전체게시물 조회
    Slice<PostResponse> getAllPostsPaging(Pageable pageable, Long userId);

    Slice<PostResponse> searchPosts(String keyword, Pageable pageable, Long userId);
}
