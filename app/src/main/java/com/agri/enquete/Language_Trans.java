package com.agri.enquete;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Language_Trans extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    Context context;
    Resources resources;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_trans, container, false);

        Button button1 = v.findViewById(R.id.button1);
        Button button2 = v.findViewById(R.id.button2);
//        Context con=getApplicationContext();
        TextView langdefine=v.findViewById(R.id.langdefine);

        String  t=LocaleHelper.getLanguage(context);
        context = LocaleHelper.setLocale(context, t);
        resources = context.getResources();

        langdefine.setText(resources.getString(R.string.langdefine));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("English");
                context = LocaleHelper.setLocale(context, "en");
                resources = context.getResources();
//                lang.setText(resources.getString(R.string.text_translation));
                Intent intent = new Intent(context, HomePage.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Hindi");
                context = LocaleHelper.setLocale(context, "hi");
                resources = context.getResources();
//                lang.setText(resources.getString(R.string.text_translation));
                Intent intent = new Intent(context, HomePage.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}