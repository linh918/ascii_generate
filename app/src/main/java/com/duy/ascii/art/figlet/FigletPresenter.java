/*
 *     Copyright (C) 2018 Tran Le Duy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.ascii.art.figlet;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Duy on 06-May-17.
 */

public class FigletPresenter implements FigletContract.Presenter {
    /**
     * this maps wil be store {@link FigletModel} created
     */
    private HashMap<String, FigletModel> caches = new HashMap<>();

    private AssetManager assetManager;
    private FigletContract.View mView;
    private FigletModel mFigletModel;
    private long updateTime;
    private Handler handler = new Handler();
    private TaskGenerateData mTaskGenerateData;
    private ProcessData process = new ProcessData();

    public FigletPresenter(AssetManager assetManager, @NonNull FigletContract.View view) {
        this.assetManager = assetManager;
        this.mView = view;
    }

    public String convert(String fontName, String data) {
        if (caches.get(fontName) != null) {
            return caches.get(fontName).convert(data);
        } else {
            try {
                caches.put(fontName, new FigletModel(assetManager.open("fonts/" + fontName)));
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

    @Override
    public void cancel() {
        handler.removeCallbacks(process);
        process.cancel();
    }

    @Nullable
    public FigletContract.View getView() {
        return mView;
    }


    public class ProcessData implements Runnable {
        @Nullable
        TaskGenerateData taskGenerateData;
        private String input = "";

        public void setInput(String input) {
            this.input = input;
        }

        @Override
        public void run() {
            taskGenerateData = new TaskGenerateData();
            taskGenerateData.execute(input);
        }

        public void cancel() {
            if (taskGenerateData != null) {
                taskGenerateData.cancel(true);
            }
        }
    }

    private class TaskGenerateData extends AsyncTask<String, String, Void> {
        private float maxProgress = 100;
        private AtomicInteger count = new AtomicInteger(0);
        private AtomicInteger current = new AtomicInteger(0);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.showProgress();
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
            if (isCancelled()) return;
            mView.addResult(values[0]);
            mView.setProgress((int) (maxProgress / count.get() * current.incrementAndGet()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mView.hideProgress();
        }
    }

}
