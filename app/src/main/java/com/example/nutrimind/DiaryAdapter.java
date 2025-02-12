package com.example.nutrimind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends BaseAdapter {

    // List to hold the diary entries (DiaryModel objects)
    private List<DiaryModel> diaryList;

    // Inflater to convert layout resource files to Views (turn the layout files into usable UI components in memory)
    private LayoutInflater inflater;

    // Constructor for initializing the DiaryAdapter with context and the diary list
    public DiaryAdapter(Context context, List<DiaryModel> diaryList) {
        this.diaryList = diaryList;
        this.inflater = LayoutInflater.from(context); // Set up inflater from context
    }

    @Override
    public int getCount() {
        // Return the number of diary entries in the list
        return diaryList.size();
    }

    @Override
    public Object getItem(int position) {
        // Return the DiaryModel object at the specified position in the list
        return diaryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Return the position of the item (used as the item's ID)
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the convertView is null (this means the view has not been reused before)
        if (convertView == null) {
            // Inflate a new view for each item in the list
            convertView = inflater.inflate(R.layout.activity_diary_item, parent, false);
        }

        // Get the diary entry at the specified position
        DiaryModel entry = diaryList.get(position);

        // Find the TextViews in the item layout (activity_diary_item)
        TextView feelingText = convertView.findViewById(R.id.textFeeling);
        TextView dateText = convertView.findViewById(R.id.textDate);

        // Set the feeling text from the DiaryModel object
        feelingText.setText(entry.getFeeling());

        // If the timestamp exists (greater than 0), format the timestamp as a readable date
        if (entry.getTimestamp() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            // Convert the timestamp to a formatted date string
            String formattedDate = dateFormat.format(entry.getTimestamp());
            dateText.setText(formattedDate); // Display the formatted date
        } else {
            // If timestamp is not available, display "Unknown Date"
            dateText.setText("Unknown Date");
        }

        // Return the inflated and populated view for the current diary entry
        return convertView;
    }
}
