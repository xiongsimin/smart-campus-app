package com.example.smartcampus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smartcampus.R;
import com.example.smartcampus.entity.Campus;
import com.example.smartcampus.entity.Term;

import java.util.List;

public class TermAdapter extends ArrayAdapter {
    private final int resourceId;
    Term term;

    public TermAdapter(@NonNull Context context, int resource, List<Term> termList, Term term) {
        super(context, resource, termList);
        resourceId = resource;
        this.term = term;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Term term = (Term) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_term_item = (TextView) view.findViewById(R.id.tv_term_item);
        tv_term_item.setText(term.getYear() + "-" + (term.getYear() + 1) + "学年" + " 第" + term.getWhichTerm() + "学期");
        if (this.term != null) {
            if (this.term.getWhichTerm() == term.getWhichTerm() && this.term.getYear() == term.getYear()) {
                tv_term_item.setBackground(tv_term_item.getResources().getDrawable(R.drawable.ic_bottom_line_checked));
            }
        }
        return view;
    }
}
