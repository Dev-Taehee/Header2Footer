package com.codestates.Header2Footer.question_board;

import com.codestates.Header2Footer.answer_board.AnswerBoard;
import com.codestates.Header2Footer.member.Member;
import com.codestates.Header2Footer.validator.NotSpace;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class QuestionBoardDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        @NotBlank(message = "질문 제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "질문 내용은 공백이 아니어야 합니다.")
        private String content;

        private Boolean isSecret;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch{
        private long questionBoardId;

        @NotSpace(message = "질문 제목은 공백이 아니어야 합니다.")
        private String title;

        @NotSpace(message = "질문 내용은 공백이 아니어야 합니다.")
        private String content;

        private Boolean isSecret;

        public void setQuestionBoardId(long questionBoardId){this.questionBoardId =questionBoardId;}
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private long questionBoardId;
        private String title;
        private String content;
        private Long views;
        private QuestionBoard.QuestionStatus questionStatus;
        private QuestionBoard.SecretStatus secretStatus;
        private Member member;
        private AnswerBoard answerBoard;

        public String getMember() {return member.getEmail();}
    }
}
