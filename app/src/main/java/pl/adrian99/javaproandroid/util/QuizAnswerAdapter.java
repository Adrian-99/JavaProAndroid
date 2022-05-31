package pl.adrian99.javaproandroid.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pl.adrian99.javaproandroid.R;

public class QuizAnswerAdapter extends ArrayAdapter<String> {

    private final Set<Integer> checkedAnswers = new HashSet<>();

    public QuizAnswerAdapter(Context context, ArrayList<String> answers) {
        super(context, 0, answers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        var answer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.quiz_answer_layout, parent, false);
        }

        var cbAnswer = (CheckBox) convertView.findViewById(R.id.answer);

        cbAnswer.setText(answer);
        cbAnswer.setChecked(checkedAnswers.contains(position));
        cbAnswer.setOnClickListener(view -> {
            if (((CheckBox) view).isChecked()) {
                checkedAnswers.add(position);
            } else {
                checkedAnswers.remove(position);
            }
        });

        return convertView;
    }

    public Set<Integer> getCheckedAnswers() {
        return checkedAnswers;
    }

    public void clearCheckedAnswers() {
        checkedAnswers.clear();
    }
}
