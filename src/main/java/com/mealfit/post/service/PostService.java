package com.mealfit.post.service;

import com.mealfit.common.storageService.StorageService;

import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.dto.PostCUDResponseDto;
import com.mealfit.post.dto.PostRequestDto;

import com.mealfit.post.repository.PostRepository;
import com.mealfit.user.domain.User;
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


    public PostCUDResponseDto createPost(PostRequestDto postRequestDto, User user) {
        validatePostDto(postRequestDto);

        //post 저장
        Post postEntity = postRequestDto.toEntity();
        postEntity.settingUserInfo(user.getId(), user.getProfileImage(), user.getNickname());

        //이미지 URL 저장하기
        List<MultipartFile> uploadImages = postRequestDto.getPostImageList();
        if (uploadImages != null) {
            List<String> saveImageUrls = saveImagesToS3(uploadImages);
            List<PostImage> postImages = saveImageUrls.stream()
                  .map(PostImage::new)
                  .collect(Collectors.toList());

            postEntity.addPostImages(postImages);
            postRepository.save(postEntity);
            return new PostCUDResponseDto(postEntity, user, saveImageUrls);
        }

        postRepository.save(postEntity);
        return new PostCUDResponseDto(postEntity, user);
    }

    private List<String> saveImagesToS3(List<MultipartFile> uploadImages) {
        if (uploadImages != null) {
            return storageService.uploadMultipartFile(uploadImages, "post/");
        }
        return null;
    }

    private void validatePostDto(PostRequestDto postRequestDto) {
        validateContent(postRequestDto);
    }

    private void validateContent(PostRequestDto dto) {
        if (dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 넣어주세요.");
        }
    }

    // 게시글 수정
    public void updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        //item
        Post post = postRepository.findById(postId)
              .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        validateUser(user, post);
        validatePostDto(postRequestDto);

        // 사진 갈아끼우기
        List<MultipartFile> uploadImages = postRequestDto.getPostImageList();

        if (uploadImages != null) {
            List<String> saveImageUrls = saveImagesToS3(uploadImages);
            List<PostImage> postImages = saveImageUrls.stream()
                  .map(PostImage::new)
                  .collect(Collectors.toList());

            post.replacePostImages(postImages);
        }

        post.updateContent(postRequestDto.getContent());
    }

    private static void validateUser(User user, Post post) {
        Long postUserId = post.getUserId();
        if (!user.getId().equals(postUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    //게시글 삭제
    public Long deletePost(Long postId, User user) {
        //유효성 검사
        Post post = postRepository.findById(postId)
              .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        //작성자 검사
        validateUser(user, post);

        postRepository.deleteById(postId);

        return postId;
    }



    }

