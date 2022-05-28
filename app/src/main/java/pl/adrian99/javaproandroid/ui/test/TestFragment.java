package pl.adrian99.javaproandroid.ui.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.databinding.FragmentTestBinding;

public class TestFragment extends Fragment implements View.OnClickListener {

    private FragmentTestBinding binding;
    private Integer testId = null;
    private Integer questionId = null;
    private int questionsCount = 0;
    private int correctAnswersCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testId = getArguments().getInt("testId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);

        binding.send.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        getNextQuestion();
    }

    private void getNextQuestion() {
        ArrayList<Integer> currentAnswers = new ArrayList<>();
        if (questionId != null) {
            if (binding.answer1.isChecked()) {
                currentAnswers.add(0);
            }
            if (binding.answer2.isChecked()) {
                currentAnswers.add(1);
            }
            if (binding.answer3.isChecked()) {
                currentAnswers.add(2);
            }
            if (binding.answer4.isChecked()) {
                currentAnswers.add(3);
            }
        }

        // get question here

        if (true) { // if correct answer
            correctAnswersCount++;
        }

        if (false) { // is there next question
            questionId = 0;
            questionsCount++;
            binding.question.setText("New question");
            binding.answer1.setChecked(false);
            binding.answer2.setChecked(false);
            binding.answer3.setChecked(false);
            binding.answer4.setChecked(false);
            binding.answer1.setText("Answer 1");
            binding.answer2.setText("Answer 2");
            binding.answer3.setText("Answer 3");
            binding.answer4.setText("Answer 4");
        } else {
            questionId = null;
            binding.question.setText(getString(R.string.test_results, correctAnswersCount, questionsCount));
            binding.answersGroup.setVisibility(View.INVISIBLE);
            binding.send.setVisibility(View.INVISIBLE);
        }
    }
}