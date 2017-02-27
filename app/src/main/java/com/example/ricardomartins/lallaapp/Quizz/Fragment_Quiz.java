package com.example.ricardomartins.lallaapp.Quizz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ricardomartins.lallaapp.R;


public class Fragment_Quiz extends Fragment {

    private int Numeration=1;
    private String[] Questions, Answers;
    private int Current_Answer=0;
    private Boolean PlayingQuiz=true; //Indicates to Button if Quiz is finished

    private View view;
    private TextView Quiz_QuestionNb, Quiz_QuestionText;
    private Button Button_Next;
    private RadioGroup radioGroup;

    private OnFragmentInteractionListener mListener;

    public Fragment_Quiz() {
        // Required empty public constructor
    }

    public static Fragment_Quiz newInstance(String param1, String param2) {
        Fragment_Quiz fragment = new Fragment_Quiz();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_quiz, container, false);

        String Question_Number = String.format(getResources().getString(R.string.Quiz_Number), Numeration);
        Quiz_QuestionNb = (TextView) view.findViewById(R.id.Quiz_QuestionNb);
        Quiz_QuestionNb.setText(Question_Number);

        Questions = getResources().getStringArray(R.array.Quiz_Questions);
        Answers = getResources().getStringArray(R.array.Quiz_Answers);

        Quiz_QuestionText = (TextView) view.findViewById(R.id.Quiz_QuestionText);
        Quiz_QuestionText.setText(Questions[Numeration-1]);

        setAnswerText();

        Button_Next = (Button) view.findViewById(R.id.Quiz_NextButton);
        Button_Next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(!PlayingQuiz){
                    Log.i("Quiz", "Finished get me out of here");
                    mListener.onAnswerSelected(-1,0); // id=-1 indicates test is over

                }else{
                    if(Current_Answer!=0){
                        Log.i("Quiz", "Got Answer = " + Answers[Current_Answer-1]);
                        mListener.onAnswerSelected(Numeration, Current_Answer );
                        DisplayNextQuestion();
                    }else{
                        Log.i("Quiz", "No answer yet");
                    }
                }
            }
        });

        radioGroup = (RadioGroup) view.findViewById(R.id.Quiz_Radio_Group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.Quiz_Answer1:
                        Current_Answer=1;
                        break;
                    case R.id.Quiz_Answer2:
                        Current_Answer=2;
                        break;
                    case R.id.Quiz_Answer3:
                        Current_Answer=3;
                        break;
                    case R.id.Quiz_Answer4:
                        Current_Answer=4;
                        break;
                    case R.id.Quiz_Answer5:
                        Current_Answer=5;
                        break;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setAnswerText(){
        ((RadioButton) view.findViewById(R.id.Quiz_Answer1)).setText(Answers[0]);
        ((RadioButton) view.findViewById(R.id.Quiz_Answer2)).setText(Answers[1]);
        ((RadioButton) view.findViewById(R.id.Quiz_Answer3)).setText(Answers[2]);
        ((RadioButton) view.findViewById(R.id.Quiz_Answer4)).setText(Answers[3]);
        ((RadioButton) view.findViewById(R.id.Quiz_Answer5)).setText(Answers[4]);
    }

    private void DisplayNextQuestion(){
        if(Numeration >= Questions.length){
            Log.i("Quiz", "No more Questions Thanks");
            PlayingQuiz=false;
            DisplayFinalMessage();
        }else {
            Numeration++;
            String Question_Number = String.format(getResources().getString(R.string.Quiz_Number), Numeration);
            Quiz_QuestionNb.setText(Question_Number);
            Quiz_QuestionText.setText(Questions[Numeration - 1]);
            radioGroup.clearCheck();
            Current_Answer=0;
        }
    }

    private void DisplayFinalMessage(){
        radioGroup.setVisibility(View.INVISIBLE);
        Quiz_QuestionNb.setVisibility(View.INVISIBLE);
        Quiz_QuestionText.setText( getResources().getText(R.string.Quiz_Final_Text));
    }

    public interface OnFragmentInteractionListener {
        void onAnswerSelected(int id, int answer);
    }
}

