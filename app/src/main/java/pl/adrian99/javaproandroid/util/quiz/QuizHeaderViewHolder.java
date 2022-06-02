package pl.adrian99.javaproandroid.util.quiz;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.adrian99.javaproandroid.R;

public class QuizHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView tvQuestion;

    public QuizHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        tvQuestion = itemView.findViewById(R.id.question);
    }
}
