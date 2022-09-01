package com.mealfit.post.application;

import com.mealfit.common.storageService.StorageService;
import com.mealfit.post.application.dto.request.PostCreateRequestDto;
import com.mealfit.post.application.dto.request.PostUpdateRequestDto;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.domain.PostRepository;
import com.mealfit.post.presentation.dto.response.PostCUDResponse;
import com.mealfit.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;

    public PostCUDResponse createPost(PostCreateRequestDto requestDto) {
        validateContent(requestDto.getContent());

        //post 저장
        Post postEntity = requestDto.toEntity();

        //이미지 URL 저장하기
        List<MultipartFile> uploadImages = requestDto.getPostImageList();

        if (uploadImages != null) {
            List<String> saveImageUrls = saveImagesToS3(uploadImages);

            List<PostImage> postImages = new ArrayList<>();

            for (String saveImageUrl : saveImageUrls) {
                postImages.add(new PostImage(saveImageUrl));
            }

            log.info("postInfo -> {}", postImages);

            postEntity.addPostImages(postImages);

            Post savedPost = postRepository.save(postEntity);

            return new PostCUDResponse(savedPost);
        }

        Post savedPost = postRepository.save(postEntity);

        return new PostCUDResponse(savedPost);
    }

    private List<String> saveImagesToS3(List<MultipartFile> uploadImages) {
        if (uploadImages != null && !uploadImages.isEmpty()) {
            return storageService.uploadMultipartFile(uploadImages, "post/");
        }
        return null;
    }

    private void validateContent(String content) {
        if (content.isBlank()) {
            throw new IllegalArgumentException("내용을 넣어주세요.");
        }
    }

    // 게시글 수정
    public void updatePost(PostUpdateRequestDto requestDto) {
        //item
        Post post = postRepository.findById(requestDto.getPostId())
              .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        validateUser(requestDto.getUserId(), post.getUserId());
        validateContent(requestDto.getContent());

        // 사진 갈아끼우기
        List<MultipartFile> uploadImages = requestDto.getPostImageList();

        List<String> saveImageUrls = saveImagesToS3(uploadImages);
        List<PostImage> postImages = saveImageUrls.stream()
              .map(PostImage::new)
              .collect(Collectors.toList());

        post.replacePostImages(postImages);

        post.updateContent(requestDto.getContent());
    }

    private void validateUser(Long userId, Long postId) {
        if (!userId.equals(postId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    //게시글 삭제
    public Long deletePost(Long postId, User user) {
        //유효성 검사
        Post post = postRepository.findById(postId)
              .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        //작성자 검사
        validateUser(user.getId(), postId);

        postRepository.deleteById(postId);

        return postId;
    }
}

