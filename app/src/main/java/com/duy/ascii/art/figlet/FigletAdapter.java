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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.ascii.art.ImageFactory;
import com.duy.ascii.art.clipboard.ClipboardManagerCompat;
import com.duy.ascii.art.clipboard.ClipboardManagerCompatFactory;
import com.duy.ascii.art.utils.FileUtil;
import com.duy.ascii.art.R;
import com.duy.common.media.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Duy on 06-May-17.
 */

class FigletAdapter extends RecyclerView.Adapter<FigletAdapter.ViewHolder> {
    private static final String TAG = "ResultAdapter";
    private final ArrayList<String> items = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private ClipboardManagerCompat clipboardManagerCompat;
    @Nullable
    private View emptyView;
    @Nullable
    private OnItemClickListener onItemClickListener;
    private int backgroundColor;
    private int textColor;

    FigletAdapter(@NonNull Context context, @Nullable View emptyView) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clipboardManagerCompat = ClipboardManagerCompatFactory.getManager(context);
        this.emptyView = emptyView;
        invalidateEmptyView();
        this.textColor = context.getResources().getColor(android.R.color.primary_text_light);
        this.backgroundColor = context.getResources().getColor(android.R.color.background_light);
        items.add(context.getString(R.string.figlet_msg));
    }

    private void invalidateEmptyView() {
        if (items.size() > 0) {
            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }
        } else {
            if (emptyView != null && emptyView.getVisibility() == View.GONE) {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_figlet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.txtContent.setTypeface(Typeface.MONOSPACE);
        holder.txtContent.setText(items.get(position));
        holder.txtContent.setTextColor(textColor);
        holder.txtContent.setBackgroundColor(backgroundColor);
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    try {
                        File file = getImage(holder.txtContent);
                        onItemClickListener.onShareImage(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //IO exception
                    }
                }
            }
        });
        holder.imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = getImage(holder.txtContent);
                    if (ImageUtils.addImageToGallery(file, context)) {
                        Toast.makeText(context, "Save in " + file.getPath(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //IO exception
                }
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File getImage(View view) throws IOException {
        Bitmap image = ImageFactory.createImageFromView(view, Color.WHITE);
        File file = new File(FileUtil.getImageDirectory(context), System.currentTimeMillis() + ".png");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        ImageFactory.writeToFile(image, file);
        image.recycle();
        return file;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void clear() {
        this.items.clear();
        items.add(context.getString(R.string.figlet_msg));
        notifyDataSetChanged();
        invalidateEmptyView();
    }

    public void add(String value) {
        this.items.add(value);
        notifyItemInserted(items.size() - 1);
        invalidateEmptyView();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        notifyDataSetChanged();
    }

    int getBackgroundColor() {
        return backgroundColor;
    }

    void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        notifyDataSetChanged();

    }

    public interface OnItemClickListener {
        void onShareImage(@NonNull File bitmap);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        View imgShare, imgSave;

        public ViewHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.content);
            imgShare = itemView.findViewById(R.id.img_share);
            imgSave = itemView.findViewById(R.id.img_save);
        }
    }
}

