package com.example.instagram.service;

import com.example.instagram.dto.response.PostResponse;
import java.util.List;

public interface BookmarkService {
    void toggleBookmark(Long postId, Long userId);
    boolean isBookmarked(Long postId, Long userId);
    long getBookmarkCount(Long postId);
    List<PostResponse> getBookmarkedPosts(Long userId);
}
