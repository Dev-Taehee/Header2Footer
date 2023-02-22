package com.codestates.Header2Footer.answer_board;

import com.codestates.Header2Footer.exception.BusinessLogicException;
import com.codestates.Header2Footer.exception.ExceptionCode;
import com.codestates.Header2Footer.member.Member;
import com.codestates.Header2Footer.member.MemberRepository;
import com.codestates.Header2Footer.question_board.QuestionBoard;
import com.codestates.Header2Footer.question_board.QuestionBoardRepository;
import com.codestates.Header2Footer.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class AnswerBoardService {
    private final AnswerBoardRepository answerBoardRepository;
    private final MemberRepository memberRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final CustomBeanUtils<AnswerBoard> beanUtils;

    public AnswerBoardService(AnswerBoardRepository answerBoardRepository, MemberRepository memberRepository, QuestionBoardRepository questionBoardRepository, CustomBeanUtils<AnswerBoard> beanUtils) {
        this.answerBoardRepository = answerBoardRepository;
        this.memberRepository = memberRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.beanUtils = beanUtils;
    }

    public AnswerBoard createAnswerBoard(AnswerBoard answerBoard, Long questionBoardId, Long memberId){
        verifyMemberIsAdmin(memberId);
        QuestionBoard questionBoard = questionBoardRepository.getReferenceById(questionBoardId);

        answerBoard.setTitle("Re: " + questionBoard.getTitle()); // title 설정
        answerBoard.setQuestionBoard(questionBoard); // questionBoard 설정
        // content는 Dto 때 저장되었음.
        if(questionBoard.getSecretStatus() == QuestionBoard.SecretStatus.SECRET){ // 질문글이 비밀글이라면 답변글도 비밀글이어야 한다.
            answerBoard.setSecretStatus(AnswerBoard.SecretStatus.SECRET);
        }else{ // 질문글이 공개글이라면 답변글도 공개글이어야한다.
            answerBoard.setSecretStatus(AnswerBoard.SecretStatus.PUBLIC);
        }

        AnswerBoard savedAnswerBoard = answerBoardRepository.save(answerBoard);

        // 답변이 등록되면 질문의 상태값은 QUESTION_ANSWERED로 변경되어야 한다.
        questionBoard.setQuestionStatus(QuestionBoard.QuestionStatus.QUESTION_ANSWERED);
        questionBoardRepository.save(questionBoard);

        return savedAnswerBoard;
    }

    public AnswerBoard updateAnswerBoard(AnswerBoard answerBoard, Long memberId){
        verifyMemberIsAdmin(memberId);
        AnswerBoard findAnswerBoard = findVerifiedAnswerBoard(answerBoard.getAnswerBoardId());

        AnswerBoard updatedAnswerBoard = beanUtils.copyNonNullProperties(answerBoard, findAnswerBoard);

        return answerBoardRepository.save(updatedAnswerBoard);
    }

    public void deleteAnswerBoard(Long answerBoardId, Long memberId){
        verifyMemberIsAdmin(memberId);
        answerBoardRepository.delete(answerBoardRepository.getReferenceById(answerBoardId));
    }

    @Transactional(readOnly = true)
    public AnswerBoard findVerifiedAnswerBoard(Long answerBoardId){
        Optional<AnswerBoard> optionalAnswerBoard = answerBoardRepository.findById(answerBoardId);
        AnswerBoard findAnswerBoard = optionalAnswerBoard.orElseThrow(()->new BusinessLogicException(ExceptionCode.ANSWER_BOARD_NOT_FOUND));
        return findAnswerBoard;
    }

    private void verifyMemberIsAdmin(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        if(!member.isPresent()){ // memberId가 존재하지 않을 경우
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }else{ // memberId가 존재한다면
            Member existMember = memberRepository.getReferenceById(memberId);
            if(existMember.getEmail().equals("admin@gmail.com")){ // member가 관리자일 경우

            }else{ // member가 관리자가 아닐 경우
                throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER_STATUS);
            }
        }

    }
}
