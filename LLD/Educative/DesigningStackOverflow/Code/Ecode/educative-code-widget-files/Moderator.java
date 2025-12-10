import java.util.*;

public class Moderator extends User {
    public void closeQuestion(Question question) {
        question.setStatus(QuestionStatus.CLOSED);
    }

    public void reopenQuestion(Question question) {
        question.setStatus(QuestionStatus.ACTIVE);
    }

    public void deleteQuestion(Question question) {
        question.setStatus(null); // or a DELETED status if defined
    }

    public void restoreQuestion(Question question) {
        question.setStatus(QuestionStatus.ACTIVE);
    }

    public void deleteAnswer(Answer answer) {
        System.out.println("Deleted answer ID: " + answer.getId());
    }
}
