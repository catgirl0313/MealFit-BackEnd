package com.mealfit.post.application;


import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostReadRepository;
import com.mealfit.post.presentation.dto.response.PostResponse;
import com.mealfit.user.domain.User;
import com.mealfit.user.domain.UserRepository;
import java.util.List;
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

    private final UserRepository userRepository;
    private final PostReadRepository postReadRepository;

    //상세 게시글 조회
    public PostResponse getReadOne(Long postId) {
        Post post = postReadRepository.findById(postId)
              .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        User user = userRepository.findById(post.getUserId())
              .orElseThrow(() -> new IllegalArgumentException("없는 회원정보입니다."));

        return PostResponse.builder()
              .postId(post.getId())
              .content(post.getContent())
              .images(post.getImageUrls())
              .nickname(user.getUserProfile().getNickname())
              .profileImage(user.getUserProfile().getProfileImage())
//                .like(likeItRepository.likes(postId,user.getId()))
              .view(postReadRepository.updateView(postId))
              .view(post.getView())
              .createdAt(post.getCreatedAt())
              .build();
    }

    //전체 게시물 조회
    public Page<PostResponse> getReadAll(Pageable pageable, Long lastId) {

        log.info("pageable -> {} ", pageable);
        log.info("lastId -> {} ", lastId);

        Page<Post> postSlice = postReadRepository
              .findAllByIdLessThan(lastId, pageable);

        log.info("result=> {}", postSlice);
        log.info("result=> {}", postSlice.getContent());

        return postToPostsResponseDtos(postSlice);
    }
    //전체 게시물 조회

    private Page<PostResponse> postToPostsResponseDtos(Page<Post> postSlice) {
        List<Post> posts = postSlice.getContent();

        return postSlice.map(post ->
              PostResponse.builder()
                    .postId(post.getId())
                    .nickname(post.getNickname())
                    .profileImage(post.getProfileImage())
                    .content(post.getContent())
                    .images(post.getImageUrls())
                    .view(post.getView())
                    .like(post.getLikeIt())
                    .createdAt(post.getCreatedAt())
                    .build()
        );
    }
}
