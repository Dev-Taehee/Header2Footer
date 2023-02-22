package com.codestates.Header2Footer.question_board;

import com.codestates.Header2Footer.answer_board.AnswerBoard;
import com.codestates.Header2Footer.audit.Auditable;
import com.codestates.Header2Footer.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class QuestionBoard extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionBoardId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTRATION;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SecretStatus secretStatus;

    @Column(nullable = false)
    private Integer views = 0;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(mappedBy = "questionBoard")
    private AnswerBoard answerBoard;

    public enum QuestionStatus{
        QUESTION_REGISTRATION("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETE("질문 삭제 상태");

        @Getter
        private String status;

        QuestionStatus(String status){this.status = status;}
    }

    public enum SecretStatus{
        PUBLIC("공개글"),
        SECRET("비밀글");

        @Getter
        private String status;

        SecretStatus(String status){this.status = status;}
    }
}
