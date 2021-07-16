package com.skrb7f16.trashtocash.utilities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.skrb7f16.trashtocash.R;

public class AddOtherTypesFragment extends Dialog{
    String type="";

    Context context;
    Button yes,no;
    Dialog dialog;
    EditText e;
    OnMyDialogResult mDialogResult;
    public AddOtherTypesFragment(Context context) {
        super(context);

        this.context=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_other_types);
        setTitle("Enter the Zip Code ");
         yes = (Button) findViewById(R.id.yes);
         no = (Button) findViewById(R.id.no);
        yes.setOnClickListener(new OKListener());
        e = (EditText) findViewById(R.id.newType);
        no.setOnClickListener(new NoListerner());
    }
    private class OKListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            if( mDialogResult != null ){
                mDialogResult.finish(String.valueOf(e.getText()));
            }
            AddOtherTypesFragment.this.dismiss();
        }
    }


    private class NoListerner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            AddOtherTypesFragment.this.dismiss();
        }
    }
    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String result);
    }
    public EditText getE() {
        return e;
    }

    public void setE(EditText e) {
        this.e = e;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
