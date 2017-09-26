package kr.bottomtab.pettner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ExamineFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String PRESCRIPT_KEY = "prescriptKey";


    private String title;


    protected RecyclerView recyclerView;
    public MyAdapter adapter;

    public ExamineFragment() {
    }

    public static ExamineFragment newInstance(String title, ArrayList<PrescriptObject> p) {
        ExamineFragment fragment = new ExamineFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(PRESCRIPT_KEY,p);
        fragment.setArguments(args);
        return fragment;
    }


    ArrayList<PrescriptObject> pres;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            pres=(ArrayList<PrescriptObject>) getArguments().getSerializable(PRESCRIPT_KEY);
        }
        getArguments().remove(PRESCRIPT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_examine, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.getContext()));

        adapter = new MyAdapter(pres);
        recyclerView.setAdapter(adapter);

        return rootView;
    }



    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<String> problem=new ArrayList<>();
        ArrayList<String> answer=new ArrayList<>();
        ArrayList<String> predate=new ArrayList<>();
        ArrayList<String> pretime=new ArrayList<>();
        View view;

        public MyAdapter(ArrayList<PrescriptObject> p) {
            for(int i=0;i<p.size();i++){
                this.problem.add(p.get(i).problem);
                this.answer.add(p.get(i).answer);
                this.predate.add(p.get(i).prescriptDate);
                this.pretime.add(p.get(i).prescriptTime);
            }


        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView problem,answer,predate,pretime;
            public ViewHolder(View v) {
                super(v);
                problem=(TextView) v.findViewById(R.id.textup);
                answer=(TextView) v.findViewById(R.id.textdown);
                predate=(TextView) v.findViewById(R.id.predate);
                pretime=(TextView) v.findViewById(R.id.pretime);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_examine, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,int position) {
            holder.problem.setText("증상: "+problem.get(position));
            holder.answer.setText("방안: "+answer.get(position));
            holder.predate.setText(predate.get(position)+" 병원진료 기록");
            holder.pretime.setText(pretime.get(position));
        }
        @Override
        public int getItemCount() {
            return problem.size();
        }
    }

}
