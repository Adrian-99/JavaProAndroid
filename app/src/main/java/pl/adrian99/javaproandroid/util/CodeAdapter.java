package pl.adrian99.javaproandroid.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import pl.adrian99.javaproandroid.R;
import pl.adrian99.javaproandroid.data.dtos.Code;

public class CodeAdapter extends ArrayAdapter<Code> {

    public CodeAdapter(Context context, ArrayList<Code> codes) {
        super(context, 0, codes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        var code = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.code_layout, parent, false);
        }

        var tvName = (TextView) convertView.findViewById(R.id.codeName);
        var tvCode = (TextView) convertView.findViewById(R.id.code);

        tvName.setText(code.getName());
        tvCode.setText(code.getCode());

        return convertView;
    }
}
