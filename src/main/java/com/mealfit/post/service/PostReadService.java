package com.mealfit.post.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.post.dto.UserDto;
import com.mealfit.post.dto.PostResponseDto;
import com.mealfit.post.dto.PostsResponseDto;
import com.mealfit.post.repository.PostReadRepository;
import com.mealfit.user.domain.User;
import com.mealfit.user.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional //(readOnly = true)
public class PostReadService {


    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final PostReadRepository postReadRepository;

    //상세 게시글 조회
    public PostResponseDto getReadOne(Long postId) {
        Post post = postReadRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        User user = userRepository.findById(post.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원정보입니다."));

        return PostResponseDto.builder()
                .postId(post.getId())
                .content(post.getContent())
                .image(post.getImages().stream().map(PostImage::getUrl)
                        .collect(Collectors.toList()))
                .userDto(new UserDto(user.getNickname(), user.getProfileImage()))
                .like(post.getLikeIt())
                .view(postReadRepository.updateView(postId))
                .view(post.getView())
                .build();
    }

    //전체 게시물 조회
    public Page<PostsResponseDto> getReadAll( Pageable pageable, Long lastId) {

        log.info("pageable -> {} ", pageable);
        log.info("lastId -> {} ", lastId);

        Page<Post> postSlice = postReadRepository
                .findAllByIdLessThan(lastId, pageable);

        log.info("result=> {}", postSlice);
        log.info("result=> {}", postSlice.getContent());
        return postToPostsResponseDtos(postSlice);
    }
    //전체 게시물 조회

    private Page<PostsResponseDto> postToPostsResponseDtos(Page<Post> postSlice) {
        return postSlice.map(p ->
                PostsResponseDto.builder()
                        .postId(p.getId())
                        .content(p.getContent())
                        .image(p.getImages().stream()
                                .filter(PostImage::isRepresentative)
                                .map(PostImage::getUrl)
                                .findFirst().orElse(null))
                        .like(p.getLikeIt())
                        .view(p.getView())
                        .build()
        );
    }
}
