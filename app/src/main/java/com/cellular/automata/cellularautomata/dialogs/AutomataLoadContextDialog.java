package com.cellular.automata.cellularautomata.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.objects.AutomataModel;

public class AutomataLoadContextDialog extends Dialog implements
        View.OnClickListener{



        public Dialog d;
        private TextView txt_open, txt_delete;
        private AutomataModel model;

        private AutomataContextDialogListener listener;

        public interface AutomataContextDialogListener {

            void onOpen(AutomataModel model);
            void onDelete(AutomataModel model);

        }

        public void setListener(AutomataContextDialogListener listener){

            this.listener = listener;

        }


        public AutomataLoadContextDialog(Context c, AutomataModel model) {

            super(c);
            this.model = model;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_context_load_automata);

            ((TextView)findViewById(R.id.load_context_txt_name)).setText("name: " + model.getName());
            ((TextView)findViewById(R.id.load_context_txt_iterations)).setText(String.valueOf("iterations: " + model.getIteration()));
            ((TextView)findViewById(R.id.load_context_txt_cubes)).setText(String.valueOf("cubes: " + model.getAliveNumber()));
            ((TextView)findViewById(R.id.load_context_txt_rule)).setText("rule: " + model.getRule());

            txt_open = findViewById(R.id.load_context_open);
            txt_delete = findViewById(R.id.load_context_delete);

            txt_open.setOnClickListener(this);
            txt_delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(listener == null) {

                dismiss();
                return;

            }

            if(v.getId() == R.id.load_context_open){

                listener.onOpen(model);

            }else{

                listener.onDelete(model);

            }

        }

}
