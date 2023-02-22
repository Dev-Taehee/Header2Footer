package com.codestates.Header2Footer.question_board;

import com.codestates.Header2Footer.dto.MultiResponseDto;
import com.codestates.Header2Footer.dto.SingleResponseDto;
import com.codestates.Header2Footer.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/questionBoards")
@Validated
@Slf4j
public class QuestionBoardController {
    private final static String QUESTION_BOARD_DEFAULT_URL = "/questionBoards";
    private final QuestionBoardService questionBoardService;
    private final QuestionBoardMapper questionBoardMapper;

    public QuestionBoardController(QuestionBoardService questionBoardService, QuestionBoardMapper questionBoardMapper) {
        this.questionBoardService = questionBoardService;
        this.questionBoardMapper = questionBoardMapper;
    }

    @PostMapping
    public ResponseEntity postQuestionBoard(
            @Positive @RequestParam Long memberId,
            @Valid @RequestBody QuestionBoardDto.Post requestBody){
        QuestionBoard questionBoard = questionBoardMapper.questionBoardPostDtoToQuestionBoard(requestBody);

        QuestionBoard createdQuestionBoard = questionBoardService.createQuestionBoard(questionBoard, memberId);
        URI location = UriCreator.createUri(QUESTION_BOARD_DEFAULT_URL, createdQuestionBoard.getQuestionBoardId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-board-id}")
    public ResponseEntity patchQuestionBoard(
            @PathVariable("question-board-id") @Positive Long questionBoardId,
            @Positive @RequestParam Long memberId,
            @Valid @RequestBody QuestionBoardDto.Patch requestBody){
        requestBody.setQuestionBoardId(questionBoardId);

        QuestionBoard questionBoard = questionBoardMapper.questionBoardPatchDtoToQuestionBoard(requestBody);

        QuestionBoard updatedQuestionBoard = questionBoardService.updateQuestionBoard(questionBoard, memberId);

        return new ResponseEntity<>(new SingleResponseDto<>(questionBoardMapper.questionBoardToQuestionBoardResponse(updatedQuestionBoard)), HttpStatus.OK);
    }

    @GetMapping("/{question-board-id}")
    public ResponseEntity getQuestionBoard(
            @PathVariable("question-board-id") @Positive Long questionBoardId,
            @Positive @RequestParam Long memberId){
        QuestionBoard questionBoard = questionBoardService.findQuestionBoard(questionBoardId, memberId);

        return new ResponseEntity<>(new SingleResponseDto<>(questionBoardMapper.questionBoardToQuestionBoardResponse(questionBoard)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestionBoards(@Positive @RequestParam Long memberId,
                                            @Positive @RequestParam int page,
                                            @Positive @RequestParam int size,
                                            @RequestParam String orderCondition){
        Page<QuestionBoard> pageQuestionBoards = questionBoardService.findQuestionBoards(memberId, page-1, size, orderCondition);
        List<QuestionBoard> questionBoards = pageQuestionBoards.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(questionBoardMapper.questionBoardsToQuestionBoardResponse(questionBoards),
                pageQuestionBoards), HttpStatus.OK);
    }

    @DeleteMapping("/{question-board-id}")
    public ResponseEntity deleteQuestionBoard(
            @PathVariable("question-board-id") @Positive Long questionBoardId,
            @Positive @RequestParam Long memberId){
        questionBoardService.deleteQuestionBoard(questionBoardId, memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
