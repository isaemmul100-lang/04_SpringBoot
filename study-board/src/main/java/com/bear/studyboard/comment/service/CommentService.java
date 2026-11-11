package com.bear.studyboard.comment.service;

import com.bear.studyboard.comment.dto.CommentListItemResponseDTO;
import com.bear.studyboard.comment.dto.CommentResponseDTO;
import com.bear.studyboard.comment.dto.CreateCommentRequestDTO;
import com.bear.studyboard.comment.entity.Comment;
import com.bear.studyboard.comment.repository.CommentRepository;
import com.bear.studyboard.common.dto.PageResponseDTO;
import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.repository.MemberRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final StudyPostRepository studyPostRepository;
    private final MemberRepository memberRepository;

    // 댓글 등록
    @Transactional
    public CommentResponseDTO createComment(CreateCommentRequestDTO request, Long postId) {

        if (request.getBody() == null || request.getAuthorId() == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "값을 입력해주세요");
        }

        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "게시글을 찾을 수 없습니다."));

        Member member = memberRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "회원을 찾을 수 없습니다."));

        Comment comment = new Comment(
                post,
                member,
                request.getBody(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Comment saveComment = commentRepository.saveAndFlush(comment);

        return CommentResponseDTO.from(saveComment);
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public PageResponseDTO<CommentListItemResponseDTO> findPostComments(Pageable pageable, Long postId) {

        StudyPost post = studyPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND, "게시글을 찾을 수 없습니다."));

        Pageable pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt", "id")
        );

        Page<CommentListItemResponseDTO> postComments = commentRepository.findPostCommentpage(pageRequest, post.getId());

        return PageResponseDTO.from(postComments);
    }

    // 댓글 수정
//    @Transactional
//    public CommentResponseDTO updateComment(String body, Long commentId) {
//
//        if (body == null) {
//            throw new BusinessException(ErrorCode.INVALID_INPUT, "값을 입력해주세요");
//        }
//
//        Comment comment = errorCommentNotFound(commentId);
//
//        comment.updateProfile(
//                body,
//                LocalDateTime.now());
//
//        return CommentResponseDTO.from(comment);
//    }

    @Transactional
    public CommentResponseDTO updateComment(String body, Long commentId) {

        if (body == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "값을 입력해주세요");
        }

        Comment comment = errorCommentNotFound(commentId);

        int updateCount = commentRepository.updateComment(body, comment.getId());

        if (updateCount == 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "댓글이 수정되지 않았습니다.");
        }

        Comment updateComment = errorCommentNotFound(commentId);

        return CommentResponseDTO.from(updateComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = errorCommentNotFound(commentId);

        commentRepository.deleteById(comment.getId());
    }

    // 댓글 아이디로 찾기
    public Comment errorCommentNotFound(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));
    }
}
