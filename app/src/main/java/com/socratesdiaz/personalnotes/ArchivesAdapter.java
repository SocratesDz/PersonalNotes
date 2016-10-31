package com.socratesdiaz.personalnotes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by socratesdiaz on 10/25/16.
 */
public class ArchivesAdapter extends RecyclerView.Adapter<ArchivesAdapter.NoteHolder> {
    private LayoutInflater mInflater;
    private List<Archive> mData = Collections.emptyList();
    private Context mContext;

    public ArchivesAdapter(Context context, List<Archive> data) {
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public ArchivesAdapter.NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_trash_archive_adapter_layout, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(ArchivesAdapter.NoteHolder holder, int position) {
        Archive selectedNote = mData.get(position);

        holder._id.setText(String.valueOf(selectedNote.getId()));
        holder.title.setText(selectedNote.getTitle());
        if(selectedNote.getDateTime().contains(AppConstant.NO_TIME)) {
            NoteCustomList noteCustomList = new NoteCustomList(mContext);
            noteCustomList.setUpForHomeAdapter(selectedNote.getDescription());
            holder.listLayout.removeAllViews();
            holder.listLayout.addView(noteCustomList);
            holder.listLayout.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.GONE);
        } else if(selectedNote.getType().equals(AppConstant.LIST)) {
            NoteCustomList noteCustomList = new NoteCustomList(mContext);
            noteCustomList.setUpForListNotification(selectedNote.getDescription());
            holder.listLayout.removeAllViews();
            holder.listLayout.addView(noteCustomList);
            holder.listLayout.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.GONE);
        } else {
            holder.listLayout.setVisibility(View.GONE);
            holder.description.setText(selectedNote.getDescription());
        }
        holder.date.setText(String.format("%s from %s", selectedNote.getDateTime(), selectedNote.getCategory()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Archive> data) {
        this.mData = data;
    }

    public void delete(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        TextView title, description, date, _id;
        LinearLayout listLayout;

        public NoteHolder(View itemView) {
            super(itemView);
            _id = (TextView) itemView.findViewById(R.id.id_note_custom_home);
            title = (TextView) itemView.findViewById(R.id.title_note_custom_home);
            description = (TextView) itemView.findViewById(R.id.description_note_custom_home);
            date = (TextView) itemView.findViewById(R.id.date_time_note_custom_home);
            listLayout = (LinearLayout) itemView.findViewById(R.id.home_list);
        }
    }
}
