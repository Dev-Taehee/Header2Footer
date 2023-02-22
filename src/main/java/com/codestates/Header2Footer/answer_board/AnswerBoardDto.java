package com.codestates.Header2Footer.answer_board;

import com.codestates.Header2Footer.question_board.QuestionBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class AnswerBoardDto {
    @Getter
    @NoArgsConstructor
    public static class Post{
        @NotBlank(message = "답변은 공백이 아니어야 합니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class Patch{
        @NotBlank(message = "답변은 공백이 아니어야 합니다.")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private long answerBoardId;
        private String title;
        private String content;
        private Long questionBoardId;
        private AnswerBoard.SecretStatus secretStatus;
    }
}
