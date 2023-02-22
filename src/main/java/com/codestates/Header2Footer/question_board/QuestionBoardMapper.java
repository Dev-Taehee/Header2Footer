package com.codestates.Header2Footer.question_board;

import com.codestates.Header2Footer.answer_board.AnswerBoard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionBoardMapper {

    List<QuestionBoardDto.Response> questionBoardsToQuestionBoardResponse(List<QuestionBoard> questionBoards);
    QuestionBoardDto.Response questionBoardToQuestionBoardResponse(QuestionBoard questionBoard);

    default QuestionBoard questionBoardPostDtoToQuestionBoard(QuestionBoardDto.Post questionBoardPostDto){
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setTitle(questionBoardPostDto.getTitle());
        questionBoard.setContent(questionBoardPostDto.getContent());

        if(questionBoardPostDto.getIsSecret()){
            questionBoard.setSecretStatus(QuestionBoard.SecretStatus.SECRET);
        }else{
            questionBoard.setSecretStatus(QuestionBoard.SecretStatus.PUBLIC);
        }

        return questionBoard;
    }

    default QuestionBoard questionBoardPatchDtoToQuestionBoard(QuestionBoardDto.Patch questionBoardPatchDto){
        QuestionBoard questionBoard = new QuestionBoard();
        questionBoard.setQuestionBoardId(questionBoardPatchDto.getQuestionBoardId());
        questionBoard.setTitle(questionBoardPatchDto.getTitle());
        questionBoard.setContent(questionBoardPatchDto.getContent());

        if(questionBoardPatchDto.getIsSecret() != null){ // 비밀글 업데이트 값이 존재할 경우
            if(questionBoardPatchDto.getIsSecret()){
                questionBoard.setSecretStatus(QuestionBoard.SecretStatus.SECRET);
            }else{
                questionBoard.setSecretStatus(QuestionBoard.SecretStatus.PUBLIC);
            }
        }

        return questionBoard;
    }
}
