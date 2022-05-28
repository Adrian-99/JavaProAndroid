package pl.adrian99.javaproandroid.ui.test;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.AnswersValidation;
import pl.adrian99.javaproandroid.data.dtos.AnswersValidationResult;
import pl.adrian99.javaproandroid.data.dtos.QuizQuestion;
import pl.adrian99.javaproandroid.databinding.FragmentTestBinding;

public class TestFragment extends Fragment implements View.OnClickListener {

    private FragmentTestBinding binding;
    private Activity activity;
    private Long testId = null;
    private List<QuizQuestion> questions;
    private int currentQuestion = 0;
    private int correctAnswersCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testId = getArguments().getLong("testId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTestBinding.inflate(inflater, container, false);
        activity = getActivity();

        binding.answersGroup.setVisibility(View.INVISIBLE);
        binding.send.setVisibility(View.INVISIBLE);
        binding.send.setOnClickListener(this);

        AsyncHttpClient.get("quiz/questions/" + testId,
                QuizQuestion[].class,
                response -> {
                    questions = Arrays.asList(response);
                    Collections.shuffle(questions);
                    activity.runOnUiThread(() -> {
                        showNextQuestion();
                        binding.answersGroup.setVisibility(View.VISIBLE);
                        binding.send.setVisibility(View.VISIBLE);
                    });
                });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        binding.send.setClickable(false);
        checkAnswers();
    }

    private void showNextQuestion() {
        currentQuestion++;
        if (currentQuestion < questions.size()) {
            var question = questions.get(currentQuestion);
            Collections.shuffle(question.getAnswers());
            binding.question.setText(question.getQuestion());
            binding.answer1.setChecked(false);
            binding.answer2.setChecked(false);
            binding.answer3.setChecked(false);
            binding.answer4.setChecked(false);
            binding.answer1.setText(question.getAnswers().get(0).getAnswer());
            binding.answer2.setText(question.getAnswers().get(1).getAnswer());
            binding.answer3.setText(question.getAnswers().get(2).getAnswer());
            binding.answer4.setText(question.getAnswers().get(3).getAnswer());
            binding.send.setClickable(true);
        } else {
            binding.question.setText(getString(R.string.test_results, correctAnswersCount, questions.size()));
            binding.answersGroup.setVisibility(View.INVISIBLE);
            binding.send.setVisibility(View.INVISIBLE);
        }
    }

    private void checkAnswers() {
        List<Long> checkedAnswerIds = new ArrayList<>();
        var question = questions.get(currentQuestion);
        var answers = question.getAnswers();

        if (binding.answer1.isChecked()) {
            checkedAnswerIds.add(answers.get(0).getId());
        }
        if (binding.answer2.isChecked()) {
            checkedAnswerIds.add(answers.get(1).getId());
        }
        if (binding.answer3.isChecked()) {
            checkedAnswerIds.add(answers.get(2).getId());
        }
        if (binding.answer4.isChecked()) {
            checkedAnswerIds.add(answers.get(3).getId());
        }

        var answersValidation = new AnswersValidation();
        answersValidation.setCheckedAnswerIds(checkedAnswerIds);

        AsyncHttpClient.post("quiz/answers/" + question.getId() + "/validate",
                answersValidation,
                AnswersValidationResult.class,
                response -> {
                    if (response.areCorrect()) {
                        correctAnswersCount++;
                    }
                    activity.runOnUiThread(this::showNextQuestion);
                });
    }
}