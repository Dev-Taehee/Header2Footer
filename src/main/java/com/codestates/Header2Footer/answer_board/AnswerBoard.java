package com.codestates.Header2Footer.answer_board;

import com.codestates.Header2Footer.audit.Auditable;
import com.codestates.Header2Footer.question_board.QuestionBoard;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class AnswerBoard extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerBoardId;

    @Column(length = 104)
    private String title;

    @Column(nullable = false, length = 4000)
    private String content;

    @OneToOne
    @JoinColumn(name = "QUESTION_BOARD_ID")
    private QuestionBoard questionBoard;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SecretStatus secretStatus;

    public enum SecretStatus{
        PUBLIC("공개글"),
        SECRET("비밀글");

        @Getter
        private String status;

        SecretStatus(String status){this.status = status;}
    }

    public Long getQuestionBoard(){return questionBoard.getQuestionBoardId();}
}
