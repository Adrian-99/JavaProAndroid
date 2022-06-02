package pl.adrian99.javaproandroid.util.quiz;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.adrian99.javaproandroid.R;

public class QuizFooterViewHolder extends RecyclerView.ViewHolder {

    public Button bSend;

    public QuizFooterViewHolder(@NonNull View itemView) {
        super(itemView);
        bSend = itemView.findViewById(R.id.send);
    }
}
