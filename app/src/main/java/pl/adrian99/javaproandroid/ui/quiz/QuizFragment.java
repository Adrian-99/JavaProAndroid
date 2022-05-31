package pl.adrian99.javaproandroid.ui.quiz;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.QuizAnswersValidation;
import pl.adrian99.javaproandroid.data.dtos.QuizAnswersValidationResult;
import pl.adrian99.javaproandroid.data.dtos.QuizQuestion;
import pl.adrian99.javaproandroid.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private FragmentQuizBinding binding;
    private Activity activity;
    private Long testId = null;
    private List<QuizQuestion> questions;
    private int currentQuestion = 0;
    private int correctAnswersCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            testId = getArguments().getLong("quizCategoryId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
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
                        showQuestion();
                        binding.answersGroup.setVisibility(View.VISIBLE);
                        binding.send.setVisibility(View.VISIBLE);
                    });
                },
                exception -> activity.runOnUiThread(() ->
                        Toast.makeText(activity, getString(R.string.server_connection_error), Toast.LENGTH_LONG).show()
                )
        );

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

    private void showQuestion() {
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

        var answersValidation = new QuizAnswersValidation();
        answersValidation.setCheckedAnswerIds(checkedAnswerIds);

        AsyncHttpClient.post("quiz/answers/" + question.getId() + "/validate",
                answersValidation,
                QuizAnswersValidationResult.class,
                response -> {
                    if (response.areCorrect()) {
                        correctAnswersCount++;
                    }
                    currentQuestion++;
                    activity.runOnUiThread(this::showQuestion);
                },
                exception -> activity.runOnUiThread(() ->
                        Toast.makeText(activity, getString(R.string.server_connection_error), Toast.LENGTH_LONG).show()
                )
        );
    }
}