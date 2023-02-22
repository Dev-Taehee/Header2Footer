package com.codestates.Header2Footer.answer_board;

import com.codestates.Header2Footer.question_board.QuestionBoard;
import com.codestates.Header2Footer.question_board.QuestionBoardService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerBoardMapper {
    AnswerBoardDto.Response answerBoardToAnswerBoardResponse(AnswerBoard answerBoard);

    default AnswerBoard answerBoardPostToAnswerBoard(AnswerBoardDto.Post post){
        AnswerBoard answerBoard = new AnswerBoard();

        answerBoard.setContent(post.getContent());
        return answerBoard;
    }

    default AnswerBoard answerBoardPatchToAnswerBoard(AnswerBoardDto.Patch patch){
        AnswerBoard answerBoard = new AnswerBoard();

        answerBoard.setContent(patch.getContent());
        return answerBoard;
    }

    
}
