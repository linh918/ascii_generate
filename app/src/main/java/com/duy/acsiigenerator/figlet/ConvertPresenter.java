package com.duy.acsiigenerator.figlet;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duy on 06-May-17.
 */

public class ConvertPresenter implements ConvertContract.Presenter {
    /**
     * this maps wil be store {@link ConvertModel} created
     */
    private HashMap<String, ConvertModel> caches = new HashMap<>();

    private AssetManager assetManager;
    private ConvertContract.View mView;
    private ConvertModel mConvertModel;
    private long updateTime;
    private Handler handler = new Handler();
    private TaskGenerateData mTaskGenerateData;
    private ProcessData process = new ProcessData();

    public ConvertPresenter(AssetManager assetManager, @NonNull ConvertContract.View view) {
        this.assetManager = assetManager;
        this.mView = view;
        mView.setPresenter(this);
    }

    public String convert(String fontName, String data) {
        if (caches.get(fontName) != null) {
            return caches.get(fontName).convert(data);
        } else {
            try {
                caches.put(fontName, new ConvertModel(assetManager.open("fonts/" + fontName)));
                return caches.get(fontName).convert(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    @Override
    public void onTextChanged(@NonNull String text) {
        handler.removeCallbacks(process);
        process.setInput(text);
        handler.postDelayed(process, 300);
    }

    public class ProcessData implements Runnable {
        private String input = "";

        public void setInput(String input) {
            this.input = input;
        }

        @Override
        public void run() {
            new TaskGenerateData().execute(input);
        }
    }

    private class TaskGenerateData extends AsyncTask<String, String, Void> {
        private float maxProgress = 100;
        private AtomicInteger count = new AtomicInteger(0);
        private AtomicInteger current = new AtomicInteger(0);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            maxProgress = mView.getMaxProgress();
            mView.setProgress(0);
            mView.clearResult();
        }

        @Override
        protected Void doInBackground(String... params) {
            String input = params[0];
            try {
                if (input.isEmpty()) return null;
                AssetManager assets = assetManager;
                String[] files = assets.list("fonts");
                this.count.set(files.length);
                for (String fontName : files) {
                    if (isCancelled()) return null;
                    try {
                        String s1 = convert(fontName, input);
                        publishProgress(s1);
                    } catch (Exception ignored) {
                    }
                }
            } catch (IOException ignored) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            mView.addResult(values[0]);
            mView.setProgress((int) (maxProgress / count.get() * current.incrementAndGet()));
        }
    }

}
