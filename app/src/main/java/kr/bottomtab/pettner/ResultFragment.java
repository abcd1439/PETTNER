package kr.bottomtab.pettner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String LIVER_DATA= "liverData";
    private static final String DIA_DATA = "diaData";
    private static final String KID_DATA = "kidData";

    private String title;

    protected RecyclerView recyclerView;
    public ResultAdapter adapter;

    public ResultFragment() {
    }

    public static ResultFragment newInstance(String title, ArrayList<HealthObject> a, ArrayList<HealthObject> b, ArrayList<HealthObject> c) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(LIVER_DATA,a);
        args.putSerializable(DIA_DATA,b);
        args.putSerializable(KID_DATA,c);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<HealthObject> a;
    ArrayList<HealthObject> b;
    ArrayList<HealthObject> c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            a=(ArrayList<HealthObject>) getArguments().getSerializable(LIVER_DATA);
            b=(ArrayList<HealthObject>) getArguments().getSerializable(DIA_DATA);
            c=(ArrayList<HealthObject>) getArguments().getSerializable(KID_DATA);
        }
        getArguments().remove(LIVER_DATA);
        getArguments().remove(DIA_DATA);
        getArguments().remove(KID_DATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.getContext()));


        adapter = new ResultAdapter(a,b,c);
        recyclerView.setAdapter(adapter);

        return rootView;
    }



    public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
        ArrayList<HealthObject> a=new ArrayList<>();
        ArrayList<HealthObject> b=new ArrayList<>();
        ArrayList<HealthObject> c=new ArrayList<>();
        View view;

        public ResultAdapter(ArrayList<HealthObject> a, ArrayList<HealthObject> b, ArrayList<HealthObject> c) {
            for(int i=0;i<a.size();i++){
                this.a.add(a.get(i));
                this.b.add(b.get(i));
                this.c.add(c.get(i));
            }


        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView color1,color2,color3;
            TextView text1,text2,text3,link1,link2,link3,date;
            public ViewHolder(View v) {
                super(v);
                color1=(ImageView) v.findViewById(R.id.color1);
                color2=(ImageView) v.findViewById(R.id.color2);
                color3=(ImageView) v.findViewById(R.id.color3);

                text1=(TextView) v.findViewById(R.id.text1);
                text2=(TextView) v.findViewById(R.id.text2);
                text3=(TextView) v.findViewById(R.id.text3);

                link1=(TextView) v.findViewById(R.id.link1);
                link2=(TextView) v.findViewById(R.id.link2);
                link3=(TextView) v.findViewById(R.id.link3);

                date=(TextView) v.findViewById(R.id.date);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_result, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if(a.get(position).recordFloat==0.1||a.get(position).recordFloat==0){
                holder.color1.setBackgroundColor(Color.rgb(254,202,152));
                holder.text1.setText("정상");
                holder.text1.setTextColor(Color.rgb(33,125,251));
            }else if(a.get(position).recordFloat==1){
                holder.color1.setBackgroundColor(Color.rgb(250,168,146));
                holder.text1.setText("비정상");
                holder.text1.setTextColor(Color.rgb(255,64,129));
            }else if(a.get(position).recordFloat==2){
                holder.color1.setBackgroundColor(Color.rgb(235,146,142));
                holder.text1.setText("비정상");
                holder.text1.setTextColor(Color.rgb(255,64,129));
            }else if(a.get(position).recordFloat==4){
                holder.color1.setBackgroundColor(Color.rgb(240,138,134));
                holder.text1.setText("비정상");
                holder.text1.setTextColor(Color.rgb(255,64,129));
            }else if(a.get(position).recordFloat==8){
                holder.color1.setBackgroundColor(Color.rgb(232,107,137));
                holder.text1.setText("비정상");
                holder.text1.setTextColor(Color.rgb(255,64,129));
            }


            if(b.get(position).recordFloat==0){
                holder.color2.setBackgroundColor(Color.rgb(144,208,182));
                holder.text2.setText("정상");
                holder.text2.setTextColor(Color.rgb(33,125,251));

            }else if(b.get(position).recordFloat==100){
                holder.color2.setBackgroundColor(Color.rgb(140,196,135));
                holder.text2.setText("비정상");
                holder.text2.setTextColor(Color.rgb(255,64,129));
            }else if(b.get(position).recordFloat==250){
                holder.color2.setBackgroundColor(Color.rgb(123,176,86));
                holder.text2.setText("비정상");
                holder.text2.setTextColor(Color.rgb(255,64,129));
            }else if(b.get(position).recordFloat==500){
                holder.color2.setBackgroundColor(Color.rgb(140,144,67));
                holder.text2.setText("비정상");
                holder.text2.setTextColor(Color.rgb(255,64,129));
            }else if(b.get(position).recordFloat==1000){
                holder.color2.setBackgroundColor(Color.rgb(129,118,54));
                holder.text2.setText("비정상");
                holder.text2.setTextColor(Color.rgb(255,64,129));
            }else if(b.get(position).recordFloat==2000){
                holder.color2.setBackgroundColor(Color.rgb(119,85,58));
                holder.text2.setText("비정상");
                holder.text2.setTextColor(Color.rgb(255,64,129));
            }

            if(c.get(position).recordFloat==0){
                holder.color3.setBackgroundColor(Color.rgb(220,233,119));
                holder.text3.setText("정상");
                holder.text3.setTextColor(Color.rgb(33,125,251));

            }else if(c.get(position).recordFloat==1){
                holder.color3.setBackgroundColor(Color.rgb(186,211,109));
                holder.text3.setText("비정상");
                holder.text3.setTextColor(Color.rgb(255,64,129));
            }else if(c.get(position).recordFloat==30){
                holder.color3.setBackgroundColor(Color.rgb(165,193,119));
                holder.text3.setText("비정상");
                holder.text3.setTextColor(Color.rgb(255,64,129));
            }else if(c.get(position).recordFloat==100){
                holder.color3.setBackgroundColor(Color.rgb(144,185,17));
                holder.text3.setText("비정상");
                holder.text3.setTextColor(Color.rgb(255,64,129));
            }else if(c.get(position).recordFloat==300){
                holder.color3.setBackgroundColor(Color.rgb(112,175,154));
                holder.text3.setText("비정상");
                holder.text3.setTextColor(Color.rgb(255,64,129));
            }else if(c.get(position).recordFloat==2000){
                holder.color3.setBackgroundColor(Color.rgb(89,156,138));
                holder.text3.setText("비정상");
                holder.text3.setTextColor(Color.rgb(255,64,129));
            }

            holder.date.setText( a.get(position).date+"키트 종합검사 결과");


            holder.link1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(BaseApplication.getContext(),KitResultActivity.class);
                    i.putExtra("value",a.get(position).date);
                    i.putExtra("value1",a.get(position).recordFloat);
                    i.putExtra("value2",a.get(position).prescriptIcon);
                    i.putExtra("value3",a.get(position).prescriptText);
                    i.putExtra("type",a.get(position).type);
                    i.putExtra("doctor",a.get(position).doctor);
                    startActivity(i);

                }
            });
            holder.link2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(BaseApplication.getContext(),KitResultActivity.class);
                    i.putExtra("value",b.get(position).date);
                    i.putExtra("value1",b.get(position).recordFloat);
                    i.putExtra("value2",b.get(position).prescriptIcon);
                    i.putExtra("value3",b.get(position).prescriptText);
                    i.putExtra("type",b.get(position).type);
                    i.putExtra("doctor",b.get(position).doctor);
                    startActivity(i);
                }
            });
            holder.link3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(BaseApplication.getContext(),KitResultActivity.class);
                    i.putExtra("value",c.get(position).date);
                    i.putExtra("value1",c.get(position).recordFloat);
                    i.putExtra("value2",c.get(position).prescriptIcon);
                    i.putExtra("value3",c.get(position).prescriptText);
                    i.putExtra("type",c.get(position).type);
                    i.putExtra("doctor",c.get(position).doctor);
                    startActivity(i);
                }
            });


        }
        @Override
        public int getItemCount() {
            return a.size();
        }
    }

}
