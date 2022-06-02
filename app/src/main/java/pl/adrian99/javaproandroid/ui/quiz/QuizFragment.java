package pl.adrian99.javaproandroid.ui.quiz;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.AsyncHttpClient;
import pl.adrian99.javaproandroid.data.dtos.QuizAnswer;
import pl.adrian99.javaproandroid.data.dtos.QuizAnswersValidation;
import pl.adrian99.javaproandroid.data.dtos.QuizAnswersValidationResult;
import pl.adrian99.javaproandroid.data.dtos.QuizQuestion;
import pl.adrian99.javaproandroid.databinding.FragmentQuizBinding;
import pl.adrian99.javaproandroid.util.quiz.QuizAdapter;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private Activity activity;
    private Long testId = null;
    private List<QuizQuestion> questions;
    private int currentQuestion = 0;
    private int correctAnswersCount = 0;
    private ArrayList<String> answers = new ArrayList<>();
    private QuizAdapter quizAnswerAdapter;

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

        quizAnswerAdapter = new QuizAdapter(answers);
        binding.answersList.setAdapter(quizAnswerAdapter);

//        quizAnswerAdapter.getSendButton().setVisibility(View.INVISIBLE);
//        quizAnswerAdapter.getSendButton().setOnClickListener(view -> {
//            quizAnswerAdapter.getSendButton().setClickable(false);
//            checkAnswers();
//        });

        AsyncHttpClient.get("quiz/questions/" + testId,
                QuizQuestion[].class,
                response -> {
                    questions = Arrays.asList(response);
                    Collections.shuffle(questions);
                    activity.runOnUiThread(() -> {
                        showQuestion();
                        quizAnswerAdapter.getSendButton().setVisibility(View.VISIBLE);
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

    private void showQuestion() {
        if (currentQuestion < questions.size()) {
            var question = questions.get(currentQuestion);
            Collections.shuffle(question.getAnswers());
            quizAnswerAdapter.setQuestion(question.getQuestion());
            quizAnswerAdapter.clearCheckedAnswers();
            answers.clear();
            answers.addAll(question.getAnswers().stream()
                    .map(QuizAnswer::getAnswer)
                    .collect(Collectors.toList())
            );
            quizAnswerAdapter.getSendButton().setClickable(true);
        } else {
            quizAnswerAdapter.setQuestion(getString(R.string.test_results, correctAnswersCount, questions.size()));
            answers.clear();
            quizAnswerAdapter.getSendButton().setVisibility(View.INVISIBLE);
        }
    }

    private void checkAnswers() {
        var question = questions.get(currentQuestion);
        var answers = question.getAnswers();

        List<Long> checkedAnswerIds = quizAnswerAdapter.getCheckedAnswers().stream()
                .map(position -> answers.get(position).getId())
                .collect(Collectors.toList());

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