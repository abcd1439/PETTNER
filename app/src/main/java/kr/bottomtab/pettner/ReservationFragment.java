package kr.bottomtab.pettner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;


public class ReservationFragment extends Fragment{
    private static final String TITLE = "title";
    private static final String RESERVATION_KEY = "reservationKey";

    private Context context;
    private String title;

    protected RecyclerView recyclerView;
    public MyAdapter adapter;

    public ReservationFragment() {
    }

    public static ReservationFragment newInstance(String title, ArrayList<ReservationObject> r) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putSerializable(RESERVATION_KEY, r);
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<ReservationObject> reserv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            reserv = (ArrayList<ReservationObject>) getArguments().getSerializable(RESERVATION_KEY);
        }
        getArguments().remove(RESERVATION_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservation, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseApplication.getContext()));


        adapter = new MyAdapter(context, reserv);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        Context context;
        ArrayList<ReservationObject> reservationObjectArrayList = new ArrayList<>();
        View view;

        public MyAdapter(Context context, ArrayList<ReservationObject> reservation) {
            this.context = context;
            DateDay a = new DateDay();
            this.reservationObjectArrayList = reservation;

        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView dd, day, name, time, status;

            public ViewHolder(View v) {
                super(v);
                dd = (TextView) v.findViewById(R.id.dd);
                day = (TextView) v.findViewById(R.id.day);
                name = (TextView) v.findViewById(R.id.name);
                time = (TextView) v.findViewById(R.id.time);
                status = (TextView) v.findViewById(R.id.status);



            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_reservation, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ReservationObject reservationObject = reservationObjectArrayList.get(position);
            holder.dd.setText(reservationObject.resDatetime.substring(5, 10));
            DateDay a=new DateDay();
            try {
                holder.day.setText("("+a.getDateDay(reservationObject.resDatetime.substring(0,10).replaceAll("-", ""), "yyyymmdd")+")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.name.setText(reservationObject.name);
            holder.time.setText(reservationObject.resDatetime.substring(11, 16));
            if (reservationObject.status == 1) {
                holder.status.setText("대기중");
            } else if (reservationObject.status == 2) {
                holder.status.setText("예약완료");
            } else if (reservationObject.status == 3) {
                holder.status.setText("진료완료");
            } else if (reservationObject.status == 4) {
                holder.status.setText("예약취소");
                holder.status.setTextColor(Color.parseColor("#ff4081"));
            }

            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.status.getText().equals("대기중") || holder.status.getText().equals("예약완료")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("취소하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AsyncTask<String, Integer,String> task = new AsyncTask<String, Integer,String>(){
                                            @Override
                                            protected String doInBackground(String... params) {

                                                boolean flag;
                                                Response response = null;
                                                OkHttpClient toServer;
                                                try{

                                                    //요청 Form세팅
                                                    Log.e("tt","before setting");
                                                    Log.e("tt",params[0]);
                                                    Log.e("tt", String.valueOf(position));
                                                    toServer = OkHttpInitSingtonManager.getOkHttpClient();
                                                    //요청 Form세팅
                                                    RequestBody postBody = new FormBody.Builder()
                                                            .add("status", Integer.toString(4))

                                                            .build();

                                                    Log.e("tt","aaaaaaaaaaaa");
                                                    Request request = new Request.Builder()
                                                            .url(NetworkDefineConstant.SERVER_URL_HOSPITAL_RESERVATION_PUT+(position+1))
                                                            .post(postBody)
                                                            .build();
                                                    //동기 방식
                                                    response = toServer.newCall(request).execute();

                                                    flag = response.isSuccessful();
                                                    String returedJSON;
                                                    if( flag ){ //성공했다면
                                                        returedJSON = response.body().string();
                                                        Log.e("tt", returedJSON);

                                                    }else{
                                                        //요청에러 발생시(http 에러)
                                                    }

                                                }catch (UnknownHostException une) {
                                                    e("aaa", une.toString());
                                                } catch (UnsupportedEncodingException uee) {
                                                    e("bbb", uee.toString());
                                                } catch (Exception e) {
                                                    e("ccc", e.toString());
                                                } finally{
                                                    if(response != null) {
                                                        response.close(); //3.* 이상에서는 반드시 닫아 준다.
                                                    }
                                                }


                                                return null;
                                            }

                                            @Override
                                            protected void onPostExecute(String result){
                                                Toast.makeText(BaseApplication.getContext(), "예약을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                                                holder.status.setText("예약취소");
                                                holder.status.setTextColor(Color.parseColor("#ff4081"));
                                            }
                                        };
                                        Log.e("tt", reservationObject.id);
                                        task.execute(reservationObject.id);
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        dialog.create();
                        dialog.show();
                    }

                }
            });
        }



        @Override
        public int getItemCount() {
            return reservationObjectArrayList.size();
        }
    }

}
