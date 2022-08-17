package com.mealfit.post.service;

import com.mealfit.common.s3.S3Service;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.dto.PostCUDResponseDto;
import com.mealfit.post.dto.PostRequestDto;
import com.mealfit.post.repository.PostRepository;
import com.mealfit.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;


    @Transactional
    public PostCUDResponseDto createPost(PostRequestDto postRequestDto, User user) {
        validatePostDto(postRequestDto);

        //post 저장
        Post postEntity = postRequestDto.toEntity();
        postEntity.settingUserInfo( user.getId(), user.getProfileImage(), user.getNickname());

        //이미지 URL 저장하기
        List<String> savedUrls = s3Service.uploadFileInS3(postRequestDto.getImageUrls(), "post/");
        List<PostImage> postImages = savedUrls
                .stream()
                .map(PostImage::new)
                .collect(Collectors.toList());

        log.info("insert into s3 complete! -> {}", postImages);

        postEntity.addPostImages(postImages);

        log.info("entity -> {}", postEntity);

        postRepository.save(postEntity);

        //return 값 생성
        return new PostCUDResponseDto(postEntity, user, savedUrls);}

    private void validatePostDto(PostRequestDto postRequestDto) {
        validateContent(postRequestDto);
    }

    private void validateContent(PostRequestDto dto) {
        if (dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 넣어주세요.");
        }
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        //item
        Post post= postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        validateUser(user, post);
        validatePostDto( postRequestDto);

        // 사진 갈아끼우기
        if ( postRequestDto.getImageUrls() != null) {
            List<String> deleteUrls = post.getImages().stream()
                    .map(PostImage::getUrl)
                    .collect(Collectors.toList());
            List<String> imagePaths = s3Service.update(deleteUrls,  postRequestDto.getImageUrls(), "post/");
            List<PostImage> postImages = imagePaths
                    .stream()
                    .map(PostImage::new)
                    .collect(Collectors.toList());

            Post postEntity = postRequestDto.toEntity();
            log.info("insert into s3 complete! -> {}", postImages);

            postEntity.addPostImages(postImages);

            log.info("entity -> {}", postEntity);

            postRepository.save(postEntity);
        }

        post.update(postRequestDto);
    }

    private static void validateUser(User user, Post post) {
        Long postUserId = post.getUserId();
        if (!user.getId().equals(postUserId)) {
            throw new IllegalArgumentException("작성자가 아니므로, 해당 게시글을 수정할 수 없습니다.");
        }
    }

    //게시글 삭제
    @Transactional
    public Long deletePost(Long postId, User user) {
        //유효성 검사
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 없습니다."));

        //작성자 검사
        validateUser(user, post);

        List<String> imageUrls = post.getImages().stream()
                .map(PostImage::getUrl)
                .collect(Collectors.toList());

        //S3 사진, ImageURl, item 삭제
        s3Service.deleteFileInS3(imageUrls);
        postRepository.deleteById(postId);
        //System.out.println("삭제확인");

        return postId;
    }
}
