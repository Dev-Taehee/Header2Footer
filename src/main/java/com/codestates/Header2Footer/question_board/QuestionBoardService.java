package com.codestates.Header2Footer.question_board;

import com.codestates.Header2Footer.exception.BusinessLogicException;
import com.codestates.Header2Footer.exception.ExceptionCode;
import com.codestates.Header2Footer.member.Member;
import com.codestates.Header2Footer.member.MemberRepository;
import com.codestates.Header2Footer.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class QuestionBoardService {
    private final QuestionBoardRepository questionBoardRepository;
    private final MemberRepository memberRepository;
    private final CustomBeanUtils<QuestionBoard> beanUtils;

    public QuestionBoardService(QuestionBoardRepository questionBoardRepository, MemberRepository memberRepository, CustomBeanUtils<QuestionBoard> beanUtils) {
        this.questionBoardRepository = questionBoardRepository;
        this.memberRepository = memberRepository;
        this.beanUtils = beanUtils;
    }

    public QuestionBoard createQuestionBoard(QuestionBoard questionBoard, Long memberId){
        verifyExistsMember(memberId);

        Member member = memberRepository.getReferenceById(memberId);
        questionBoard.setMember(member);

        QuestionBoard savedQuestionBoard = questionBoardRepository.save(questionBoard);

        return savedQuestionBoard;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public QuestionBoard updateQuestionBoard(QuestionBoard questionBoard, Long memberId){
        QuestionBoard findQuestionBoard = findVerifiedQuestionBoard(questionBoard.getQuestionBoardId());

        QuestionBoard updatedQuestionBoard;

        if(findQuestionBoard.getMember().getMemberId() != memberId){ // 수정을 요청한 멤버가 작성자가 아니라면
            throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER_STATUS);
        }else{ // 수정을 요청한 멤버가 작성자라면
            updatedQuestionBoard = beanUtils.copyNonNullProperties(questionBoard, findQuestionBoard);
        }

        return questionBoardRepository.save(updatedQuestionBoard);
    }

    @Transactional(readOnly = true)
    public QuestionBoard findQuestionBoard(Long questionBoardId, Long memberId){
        verifyExistsMember(memberId);
        QuestionBoard questionBoard = findVerifiedQuestionBoard(questionBoardId);
        QuestionBoard findedQuestionBoard;

        if(questionBoard.getQuestionStatus() == QuestionBoard.QuestionStatus.QUESTION_DELETE){ // 이미 삭제 상태인 질문은 조회할 수 없다.
            throw new BusinessLogicException(ExceptionCode.QUESTION_BOARD_REMOVED);
        }else{
            if(questionBoard.getSecretStatus() == QuestionBoard.SecretStatus.SECRET){ // 비밀글인 경우
                if(questionBoard.getMember().getMemberId() == memberId || questionBoard.getMember().getEmail().equals("admin@gmail.com")){ // 비밀글을 쓴 당사진 경우이거나 관리자인 경우
                    findedQuestionBoard = findVerifiedQuestionBoard(questionBoardId);
                    addViews(findedQuestionBoard);
                    return findedQuestionBoard;
                }else{
                    throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER_STATUS);
                }
            }else{ // 공개글인 경우
                findedQuestionBoard = findVerifiedQuestionBoard(questionBoardId);
                addViews(findedQuestionBoard);
                return findedQuestionBoard;
            }
        }
    }

    public Page<QuestionBoard> findQuestionBoards(Long memberId, int page, int size, String orderCondition){
        verifyExistsMember(memberId);
        if(orderCondition.equals("newDate")){ // 최신글 순으로 정렬
            return questionBoardRepository.findWithoutDeleteBoard(PageRequest.of(page, size,
                    Sort.by("createdAt").descending()));
        } else if (orderCondition.equals("oldDate")) { // 오래된 글 순으로 정렬
            return questionBoardRepository.findWithoutDeleteBoard(PageRequest.of(page, size,
                    Sort.by("createdAt").ascending()));
        }

        // default로 최신글 정렬
        return questionBoardRepository.findWithoutDeleteBoard(PageRequest.of(page, size,
                Sort.by("createdAt").descending()));
    }

    public void deleteQuestionBoard(Long questionBoardId, Long memberId){
        QuestionBoard questionBoard = findVerifiedQuestionBoard(questionBoardId);

        if(questionBoard.getMember().getMemberId() != memberId){ // 삭제를 요청하는 멤버가 작성자가 아니라면
            throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER_STATUS);
        }else{ // 삭제를 요청하는 멤버가 작성자라면
            if(questionBoard.getQuestionStatus() == QuestionBoard.QuestionStatus.QUESTION_DELETE){ // 이미 삭제된 상태라면
                throw new BusinessLogicException(ExceptionCode.QUESTION_BOARD_REMOVED);
            }else{
                questionBoard.setQuestionStatus(QuestionBoard.QuestionStatus.QUESTION_DELETE);
            }
        }

        questionBoardRepository.save(questionBoard);
    }

    @Transactional(readOnly = true)
    public QuestionBoard findVerifiedQuestionBoard(Long questionBoardId){
        Optional<QuestionBoard> optionalQuestionBoard = questionBoardRepository.findById(questionBoardId);
        QuestionBoard findQuestionBoard =
                optionalQuestionBoard.orElseThrow(()->new BusinessLogicException(ExceptionCode.QUESTION_BOARD_NOT_FOUND));

        return findQuestionBoard;
    }

    private void verifyExistsMember(Long memberId){
        Optional<Member> member = memberRepository.findById(memberId);
        if(!member.isPresent()){
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

    private void addViews(QuestionBoard questionBoard){ // 질문글을 조회하면 조회수 1 증가
        questionBoard.setViews(questionBoard.getViews() + 1);
//        questionBoardRepository.save(questionBoard);
        questionBoardRepository.saveAndFlush(questionBoard);
    }
}
