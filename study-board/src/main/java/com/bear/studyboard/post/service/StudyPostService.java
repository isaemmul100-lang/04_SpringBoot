package com.bear.studyboard.post.service;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.category.repository.CategoryRepository;
import com.bear.studyboard.common.dto.PageResponseDTO;
import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.repository.MemberRepository;
import com.bear.studyboard.post.dto.CreatePostRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDto;
import com.bear.studyboard.post.dto.PostListItemResponseDto;
import com.bear.studyboard.post.dto.PostUpdateRequestDTO;
import com.bear.studyboard.post.entity.StudyPost;
import com.bear.studyboard.post.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    // 자료 등록
    @Transactional
    public PostDetailResponseDto createPost(CreatePostRequestDTO request) {

        if (request.getAuthor() == null ||
                request.getCategory() == null ||
                request.getTitle() == null ||
                request.getBody() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "값을 입력해주세요.");
        }

        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Member member = memberRepository.findById(request.getAuthor())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        StudyPost studyPost = new StudyPost(
                member,
                category,
                request.getTitle(),
                request.getBody(),
                request.getReferenceUrl(),
                LocalDateTime.now(),
                LocalDateTime.now());

        StudyPost saveStudyPost = studyPostRepository.saveAndFlush(studyPost);

        return PostDetailResponseDto.from(saveStudyPost);
    }

    public PageResponseDTO<PostListItemResponseDto> findPosts(Pageable pageable) {

        /*
        * PageRequest : pageable 인터페이스의 대표 구현체
        * PageRequest.of(페이지 번호, 페이지 크기)
        * PageRequest.of(페이지 번호, 페이지 크기, 정렬)
        * */
        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        // Page로 래핑된 DTO Projection
        Page<PostListItemResponseDto> posts = studyPostRepository.findPostpage(pageRequest);

        return PageResponseDTO.from(posts); // Page -> PageResponseDTO 변환 (클라이언트에서 필요한 값만 전달)
    }

    @Transactional(readOnly = true)
    public PostDetailResponseDto findPostById(Long postId) {

        StudyPost studyPost = studyPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "요청한 아이디의 게시글을 찾을 수 없습니다."));

        return studyPostRepository.findPostDetailById(studyPost.getId());
    }

    @Transactional
    public PostDetailResponseDto updatePost(PostUpdateRequestDTO request, Long postId) {

        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "요청한 아이디의 게시글을 찾을 수 없습니다."));

        StudyPost updatePost = new StudyPost(
                post.getAuthor(),
                post.getCategory(),
                request.getTitle(),
                request.getBody(),
                request.getUrl(),
                post.getCreatedAt(),
                LocalDateTime.now()
        );

        post.updateProfile(updatePost.getTitle(), updatePost.getBody(), updatePost.getReferenceUrl(), LocalDateTime.now());

        return PostDetailResponseDto.from(updatePost);
    }
}
