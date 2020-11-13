package com.example.newcycle.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class CustomTask extends AsyncTask<Void, Void, Void> {

    public interface TaskListener{
        public void onExecute();
        public void onDone();
    }

    private final TaskListener taskListener;

    public CustomTask(TaskListener taskListener){
        this.taskListener = taskListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(this.taskListener != null){
            this.taskListener.onExecute();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(this.taskListener != null){
            this.taskListener.onDone();
        }
    }
}
