package com.bear.studyboard.comment.repository;

import com.bear.studyboard.comment.dto.CommentListItemResponseDTO;
import com.bear.studyboard.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            select new com.bear.studyboard.comment.dto.CommentListItemResponseDTO(
                author.nickname,
                comment.body,
                comment.createdAt
            )
            from Comment comment
            join comment.author author
            where comment.studyPost.id = :id
            """, countQuery = "select count(comment) from Comment comment where comment.studyPost.id = :id")
    Page<CommentListItemResponseDTO> findPostCommentpage(Pageable pageRequest, Long id);
}
