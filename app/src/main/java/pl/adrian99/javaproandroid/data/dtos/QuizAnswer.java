package pl.adrian99.javaproandroid.data.dtos;

public class QuizAnswer {
    private Long id;
    private String answer;

    public Long getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
