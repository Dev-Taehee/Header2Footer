package com.codestates.Header2Footer.question_board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
    @Query("select b from QuestionBoard b WHERE NOT b.questionStatus IN ('QUESTION_DELETE')")
    public Page<QuestionBoard> findWithoutDeleteBoard(PageRequest pageRequest);
}
