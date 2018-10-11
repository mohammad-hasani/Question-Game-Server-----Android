package hajagha.dibagames.ir.hajagha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Interfaces.OnDataReceive;

/**
 * Created by Pars on 5/14/2016.
 */
public class MainQuestion extends Activity {
    private ArrayList<MainQuestionData> info;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private int index = 0;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = new ArrayList<MainQuestionData>();
        setContentView(R.layout.mainquestion);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewquestions);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == info.size() - 1) {
//                    index += 10;
//                    load(index);
//                }
//            }
//        });
        adapter = new RecyclerViewAdapter(info);
        recyclerView.setAdapter(adapter);
        load(index);
    }

    private void createItems(String data) {
        if (data == null || data.startsWith("false")) {
            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
            return;
        }
        String[] splitedData1 = data.split(App.spliter2);
        for (int i = 0; i < splitedData1.length; i++) {
            String[] splitedData2 = splitedData1[i].split(App.spliter1);
            MainQuestionData mainQuestionData = new MainQuestionData();
            mainQuestionData.id = Integer.valueOf(splitedData2[0]);
            mainQuestionData.txtTitle = splitedData2[1].toString().replaceAll(App.DOTE_CHARACTER, App.READ_DOTE_CHARACTER);
            mainQuestionData.txtBody = splitedData2[2].toString().replaceAll(App.DOTE_CHARACTER, App.READ_DOTE_CHARACTER);
            info.add(mainQuestionData);
        }
        adapter.notifyItemInserted(info.size() - 1);
    }

    private void load(int index) {
        String[] values = {String.valueOf(index)};
        new RestWebService(this, new OnDataReceive() {
            @Override
            public void Received(String data) {
                if (data != "null" && data != "false" && data != "true") {
                    createItems(data);
                }
            }
        }, App.keysGetQuestions, values, App.urlquestions);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        private Context _context;
        private ArrayList<MainQuestionData> _data;

        public RecyclerViewAdapter(ArrayList<MainQuestionData> data) {
            this._context = App.getContext();
            _data = data;
        }

        private View view;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(_context);
            view = layoutInflater.inflate(R.layout.mainquestionitem, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final MainQuestionData m = _data.get(position);
            holder.txtTitle.setText(m.txtTitle);
            holder.txtBody.setText(m.txtBody);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainQuestion.this, Question.class);
                    i.putExtra("id", m.id);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return _data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitle;
            TextView txtBody;

            public MyViewHolder(View itemView) {
                super(itemView);
                txtTitle = (TextView) itemView.findViewById(R.id.txttitle);
                txtBody = (TextView) itemView.findViewById(R.id.txtbody);
            }
        }
    }

    class MainQuestionData {
        public int id;
        public String txtTitle;
        public String txtBody;
    }
}
