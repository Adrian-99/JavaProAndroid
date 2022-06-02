package pl.adrian99.javaproandroid.data.dtos;

import java.util.List;

public class QuizQuestion {
    private Long id;
    private String question;
    private boolean hasImage;
    private List<QuizAnswer> answers;

    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public boolean hasImage() {
        return hasImage;
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

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public void setAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }
}
