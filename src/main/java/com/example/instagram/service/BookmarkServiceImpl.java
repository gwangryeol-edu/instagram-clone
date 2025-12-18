package com.example.instagram.service;

import com.example.instagram.dto.response.PostResponse;
import com.example.instagram.entity.Bookmark;
import com.example.instagram.entity.Post;
import com.example.instagram.entity.User;
import com.example.instagram.repository.BookmarkRepository;
import com.example.instagram.repository.CommentRepository;
import com.example.instagram.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostService postService;
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void toggleBookmark(Long postId, Long userId) {
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId);

        if (existingBookmark.isPresent()) {
            bookmarkRepository.delete(existingBookmark.get());
        } else {
            Post post = postService.findById(postId);
            User user = userService.findById(userId);

            Bookmark bookmark = Bookmark.builder()
                    .post(post)
                    .user(user)
                    .build();
            bookmarkRepository.save(bookmark);
        }
    }

    @Override
    public boolean isBookmarked(Long postId, Long userId) {
        return bookmarkRepository.existsByUserIdAndPostId(userId, postId);
    }

    @Override
    public long getBookmarkCount(Long postId) {
        return 0; // Bookmark count is usually not displayed per post
    }

    @Override
    public List<PostResponse> getBookmarkedPosts(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);
        return bookmarks.stream()
                .map(Bookmark::getPost)
                .map(post -> {
                    long likeCount = likeRepository.countByPostId(post.getId());
                    long commentCount = commentRepository.countByPostId(post.getId());
                    boolean isLiked = likeRepository.existsByPostIdAndUserId(post.getId(), userId);
                    boolean isBookmarked = true; // since we are fetching bookmarked posts
                    return PostResponse.from(post, commentCount, likeCount, isLiked, isBookmarked);
                })
                .collect(Collectors.toList());
    }
}
