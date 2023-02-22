package com.codestates.Header2Footer.answer_board;

import com.codestates.Header2Footer.dto.SingleResponseDto;
import com.codestates.Header2Footer.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;
import java.net.URI;

@RestController
@RequestMapping("/answers")
@Validated
@Slf4j
public class AnswerBoardController {
    private final static String ANSWER_BOARD_DEFAULT_URL = "/answers";
    private final AnswerBoardService answerBoardService;
    private final AnswerBoardMapper answerBoardMapper;

    public AnswerBoardController(AnswerBoardService answerBoardService, AnswerBoardMapper answerBoardMapper) {
        this.answerBoardService = answerBoardService;
        this.answerBoardMapper = answerBoardMapper;
    }

    @PostMapping
    public ResponseEntity postAnswerBoard(@Positive @RequestParam Long questionBoardId,
                                     @Positive @RequestParam Long memberId,
                                     @Valid @RequestBody AnswerBoardDto.Post requestBody){
        AnswerBoard answerBoard = answerBoardMapper.answerBoardPostToAnswerBoard(requestBody);

        AnswerBoard createdAnswerBoard = answerBoardService.createAnswerBoard(answerBoard, questionBoardId, memberId);
        URI location = UriCreator.createUri(ANSWER_BOARD_DEFAULT_URL, createdAnswerBoard.getAnswerBoardId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{answer-board-id}")
    public ResponseEntity patchAnswerBoard(@Positive @RequestParam Long memberId,
                                           @Positive @PathVariable("answer-board-id") Long answerBoardId,
                                           @Valid @RequestBody AnswerBoardDto.Patch requestBody) {
        AnswerBoard answerBoard = answerBoardMapper.answerBoardPatchToAnswerBoard(requestBody);
        answerBoard.setAnswerBoardId(answerBoardId);

        AnswerBoard updatedAnswerBoard = answerBoardService.updateAnswerBoard(answerBoard, memberId);

        return new ResponseEntity<>(new SingleResponseDto<>(answerBoardMapper.answerBoardToAnswerBoardResponse(updatedAnswerBoard)), HttpStatus.OK);
    }

    @DeleteMapping("/{answer-board-id}")
    public ResponseEntity deleteAnswerBoard(@Positive @RequestParam Long memberId,
                                            @Positive @PathVariable("answer-board-id") Long answerBoardId){
        answerBoardService.deleteAnswerBoard(answerBoardId, memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
