package pl.adrian99.javaproandroid.util.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pl.adrian99.javaproandroid.R;

public class QuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER_VIEW = 1;
    public static final int FOOTER_VIEW = 2;

    private final ArrayList<String> answers;
    private final Set<Integer> checkedAnswers = new HashSet<>();
    private QuizHeaderViewHolder quizHeaderViewHolder;
    private QuizFooterViewHolder quizFooterViewHolder;

    public QuizAdapter(ArrayList<String> answers) {
        this.answers = answers;
    }

    public Set<Integer> getCheckedAnswers() {
        return checkedAnswers;
    }

    public void clearCheckedAnswers() {
        checkedAnswers.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            default:
                view = layoutInflater.inflate(R.layout.quiz_answer_layout, parent, false);
                return new QuizAnswerViewHolder(view);

            case HEADER_VIEW:
                view = layoutInflater.inflate(R.layout.quiz_header_layout, parent, false);
                quizHeaderViewHolder = new QuizHeaderViewHolder(view);
                return quizHeaderViewHolder;

            case FOOTER_VIEW:
                view = layoutInflater.inflate(R.layout.quiz_footer_layout, parent, false);
                quizFooterViewHolder = new QuizFooterViewHolder(view);
                return quizFooterViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuizAnswerViewHolder) {
            var answer = answers.get(position - 1);

            var quizAnswerViewHolder = (QuizAnswerViewHolder) holder;
            quizAnswerViewHolder.cbAnswer.setText(answer);
            quizAnswerViewHolder.cbAnswer.setChecked(checkedAnswers.contains(position));
            quizAnswerViewHolder.cbAnswer.setOnClickListener(view -> {
                if (((CheckBox) view).isChecked()) {
                    checkedAnswers.add(position);
                } else {
                    checkedAnswers.remove(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return answers.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW;
        } else if (position == answers.size() + 1) {
            return FOOTER_VIEW;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void setQuestion(String question) {
        quizHeaderViewHolder.tvQuestion.setText(question);
    }

    public Button getSendButton() {
        return quizFooterViewHolder.bSend;
    }
}
