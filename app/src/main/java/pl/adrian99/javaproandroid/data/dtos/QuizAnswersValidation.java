package pl.adrian99.javaproandroid.data.dtos;

import java.util.List;

public class QuizAnswersValidation {
    private List<Long> checkedAnswerIds;

    public List<Long> getCheckedAnswerIds() {
        return checkedAnswerIds;
    }

    public void setCheckedAnswerIds(List<Long> checkedAnswerIds) {
        this.checkedAnswerIds = checkedAnswerIds;
    }
}
