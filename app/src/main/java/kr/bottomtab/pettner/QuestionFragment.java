package kr.bottomtab.pettner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class QuestionFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String QUESTION_KEY = "questionKey";


    private String title;
    protected RecyclerView recyclerView;
    public QuestionAdapter adapter;

    FloatingActionButton fab;

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(String title, ArrayList<QuestionObject> q) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(QUESTION_KEY,q);
        fragment.setArguments(args);
        return fragment;
    }


    ArrayList<QuestionObject> question;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            question=(ArrayList<QuestionObject>) getArguments().getSerializable(QUESTION_KEY);
        }
        getArguments().remove(QUESTION_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.qrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.getContext()));

        fab=(FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(QuestionFragment.super.getContext(),QuestionActivity.class);
                startActivity(intent);
            }
        });

        adapter = new QuestionAdapter(question);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
    public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
        ArrayList<String> name=new ArrayList<>();
        /*ArrayList<Integer> subNum=new ArrayList<>();*/
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> question=new ArrayList<>();
        ArrayList<Integer> id=new ArrayList<>();
        View view;

        public QuestionAdapter(ArrayList<QuestionObject> question) {
            for(int i=0;i<question.size();i++) {
                this.name.add(question.get(i).name);
                /*this.subNum.add(question.get(i).subNum);*/
                this.date.add(question.get(i).date);
                this.question.add(question.get(i).question);
                this.id.add(question.get(i).id);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CardView card;
            TextView hosname,hosdate,hossubnum,hostext;
            public ViewHolder(View v) {
                super(v);
                hosname=(TextView) v.findViewById(R.id.hosname);
                hosdate=(TextView) v.findViewById(R.id.hosdate);
                /*hossubnum=(TextView) v.findViewById(R.id.hossubnum);*/
                hostext=(TextView) v.findViewById(R.id.hostext);

                card=(CardView) v.findViewById(R.id.card);


            }
        }

        @Override
        public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_question, parent, false);

            return new QuestionAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(QuestionAdapter.ViewHolder holder, final int position) {
            holder.hosname.setText(name.get(position));
            holder.hosdate.setText(date.get(position));
            /*holder.hossubnum.setText("답변 "+Integer.toString(subNum.get(position))+"개");*/
            holder.hostext.setText(question.get(position));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(BaseApplication.getContext(),QuestionAnswerActivity.class);
                    i.putExtra("value",id.get(position));
                    startActivity(i);
                }
            });

        }
        @Override
        public int getItemCount() {
            return name.size();
        }
    }

}
