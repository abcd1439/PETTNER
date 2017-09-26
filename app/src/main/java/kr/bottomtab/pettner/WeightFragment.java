package kr.bottomtab.pettner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class WeightFragment extends Fragment {
    public static String WEIGHT_KEY="weightKey";

    protected RecyclerView recyclerView;
    protected MyAdapter adapter;
    public WeightFragment() {    }

    public static WeightFragment newInstance(ArrayList<HealthObject> weight){
        WeightFragment f=new WeightFragment();
        Bundle b=new Bundle();
        b.putSerializable(WEIGHT_KEY,weight);
        f.setArguments(b);

        return f;
    }

    FloatingActionButton fab;

    ArrayList<HealthObject> weight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getArguments();
        if(b!=null){
            weight=(ArrayList<HealthObject>)b.getSerializable(WEIGHT_KEY);
        }
        getArguments().remove(WEIGHT_KEY);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_weight, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(super.getContext(), LinearLayoutManager.VERTICAL,false));

        fab=(FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WeightActivity.class);
                startActivity(intent);
            }
        });
        adapter = new MyAdapter(weight);
        recyclerView.setAdapter(adapter);

        return v;
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        ArrayList<Integer> weight=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();
        ArrayList<String> time=new ArrayList<>();
        int prev=0;


        public MyAdapter(ArrayList<HealthObject> weight) {
            for(int i=0;i<weight.size();i++){
                this.weight.add(weight.get(i).recordFloat);
                this.date.add(weight.get(i).date);
                this.time.add(weight.get(i).time);
            }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textdate,texttime,textweight,textminus;
            ImageView updown;
            public ViewHolder(View v) {
                super(v);
                textdate=(TextView) v.findViewById(R.id.textdate);
                texttime=(TextView) v.findViewById(R.id.texttime);
                textweight=(TextView) v.findViewById(R.id.textweight);
                textminus=(TextView) v.findViewById(R.id.textminus);
                updown=(ImageView) v.findViewById(R.id.updown);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_weight, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textdate.setText(date.get(position));
            holder.texttime.setText(time.get(position));
            holder.textweight.setText(Integer.toString(weight.get(position)));

            int a=weight.get(position)-prev;
            prev=weight.get(position);
            if(a<0){
                holder.textminus.setText(Integer.toString(a).substring(1));
                holder.updown.setImageResource(R.drawable.icon_down);
            }else if(a>0){
                holder.textminus.setText(Integer.toString(a));
                holder.updown.setImageResource(R.drawable.icon_up);
            }else{
                holder.textminus.setText(Integer.toString(a));
                holder.updown.setImageResource(R.drawable.icon_no);
            }



        }

        @Override
        public int getItemCount() {
            return weight.size();
        }
    }

}
