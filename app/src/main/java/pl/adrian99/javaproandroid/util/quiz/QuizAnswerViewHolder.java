package pl.adrian99.javaproandroid.util.quiz;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.adrian99.javaproandroid.R;

public class QuizAnswerViewHolder extends RecyclerView.ViewHolder {

    public CheckBox cbAnswer;

    public QuizAnswerViewHolder(@NonNull View itemView) {
        super(itemView);
        cbAnswer = itemView.findViewById(R.id.answer);
    }
}
