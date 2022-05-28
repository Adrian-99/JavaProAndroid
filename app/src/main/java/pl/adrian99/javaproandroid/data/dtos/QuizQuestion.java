package pl.adrian99.javaproandroid.data.dtos;

import java.util.List;

public class QuizQuestion {
    private Long id;
    private String question;
    private List<QuizAnswer> answers;

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<QuizAnswer> getAnswers() {
        return answers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }
}
