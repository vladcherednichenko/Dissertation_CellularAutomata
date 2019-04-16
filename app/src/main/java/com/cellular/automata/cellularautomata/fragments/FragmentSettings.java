package com.cellular.automata.cellularautomata.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.presenters.Presenter;

import java.util.Arrays;

public class FragmentSettings extends Fragment {

    private View view;
    private Presenter presenter;
    private TextView delayTextView, gridSizeTextView;
    private EditText surviveNumberInput, reviveNumberInput;
    private CheckBox colorInheritanceCheckBox, colorDarkeningCheckBox;

    public void setPresenter(Presenter presenter){

        this.presenter = presenter;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        view.findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.settingsFragmentReturnPressed();

            }
        });

        delayTextView = view.findViewById(R.id.delay_textview);
        gridSizeTextView = view.findViewById(R.id.grid_size_textview);
        surviveNumberInput = view.findViewById(R.id.survive_number_input);
        reviveNumberInput = view.findViewById(R.id.revive_number_input);
        colorInheritanceCheckBox = view.findViewById(R.id.color_inheritance_checkbox);
        colorInheritanceCheckBox.setChecked(Settings.enableColorInheritance);
        colorInheritanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Settings.enableColorInheritance = isChecked;
                colorInheritanceChanged();

                if(isChecked && Settings.enableColorDarkening){

                    Settings.enableColorDarkening = false;
                    colorDarkeningCheckBox.setChecked(false);

                }

            }

        });

        colorDarkeningCheckBox = view.findViewById(R.id.color_darkening_checkbox);
        colorDarkeningCheckBox.setChecked(Settings.enableColorDarkening);
        colorDarkeningCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Settings.enableColorDarkening = isChecked;
                colorDarkeningChanged();

                if(isChecked && Settings.enableColorInheritance){

                    Settings.enableColorInheritance = false;
                    colorInheritanceCheckBox.setChecked(false);

                }

            }

        });

        // Plus radius
        view.findViewById(R.id.grid_size_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newRadius = Settings.automataRadius + 5;
                if(newRadius <= Settings.maximumAutomataRadius){

                    Settings.automataRadius = newRadius;
                    automataRadiusChanged();

                }

            }
        });

        // Minus radius
        view.findViewById(R.id.grid_size_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newRadius = Settings.automataRadius - 5;
                if(newRadius >= Settings.minimumAutomataRadius){

                    Settings.automataRadius = newRadius;
                    automataRadiusChanged();

                }

            }
        });

        // Plus delay
        view.findViewById(R.id.delay_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newRadius = Settings.delay + 100;
                if(newRadius <= Settings.maxDelay){

                    Settings.delay = newRadius;
                    delayChanged();

                }

            }
        });

        // Minus delay
        view.findViewById(R.id.delay_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newRadius = Settings.delay - 100;
                if(newRadius >= Settings.minDelay){

                    Settings.delay = newRadius;
                    delayChanged();

                }

            }
        });

        // Apply
        view.findViewById(R.id.apply_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rule rule = createRule();
                if(rule != null){

                    presenter.ruleChanged(rule);
                    presenter.settingsFragmentReturnPressed();

                }

            }
        });

        updateGridSizeTextView();
        updateDelayTextView();
        updateRuleTextVies();

        return view;

    }

    private void colorInheritanceChanged(){


    }

    private void colorDarkeningChanged(){


    }

    private void delayChanged(){

        updateDelayTextView();

    }

    private void automataRadiusChanged(){

        updateGridSizeTextView();
        presenter.automataRadiusChanged();

    }


    private void updateDelayTextView(){

        delayTextView.setText(String.valueOf(Settings.delay));

    }

    private void updateGridSizeTextView(){

        gridSizeTextView.setText(String.valueOf(Settings.automataRadius));

    }

    private void updateRuleTextVies(){

        Rule rule = presenter.getCurrentRule();
        surviveNumberInput.setText(Arrays.toString(rule.keepAliveNeighboursNumber).replace("[", "").replace("]", ""));
        reviveNumberInput.setText(Arrays.toString(rule.reviveNeighboursNumber).replace("[", "").replace("]", ""));

    }

    private void makeToast(String text){

        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

    }

    private Rule createRule() {

        String rawSurviveNeighbours = surviveNumberInput.getText().toString().replace(" ", "");
        if(rawSurviveNeighbours .length() == 0){

            makeToast("survive numbers are not correct");
            return null;

        }

        String rawReviveNeighbours = reviveNumberInput.getText().toString().replace(" ", "");
        if(rawReviveNeighbours .length() == 0){

            makeToast("revive numbers are not correct");
            return null;

        }

        int[] surviveNeighboursNumbers;
        int[] reviveNeighboursNumbers;

        if(rawSurviveNeighbours.length() == 1){

            surviveNeighboursNumbers = new int[1];
            surviveNeighboursNumbers[0] = Integer.valueOf(rawSurviveNeighbours);

        }else{

            String stringNumbers[] = rawSurviveNeighbours.split(",");
            surviveNeighboursNumbers = new int[stringNumbers.length];
            for (int i=0; i< stringNumbers.length; i++){

                surviveNeighboursNumbers[i] = Integer.valueOf(stringNumbers[i]);

            }

        }

        if(rawReviveNeighbours.length() == 1){

            reviveNeighboursNumbers = new int[1];
            reviveNeighboursNumbers[0] = Integer.valueOf(rawReviveNeighbours);

        }else{

            String stringNumbers[] = rawReviveNeighbours.split(",");
            reviveNeighboursNumbers = new int[stringNumbers.length];
            for (int i=0; i< stringNumbers.length; i++){

                reviveNeighboursNumbers[i] = Integer.valueOf(stringNumbers[i]);

            }

        }

        return new Rule(surviveNeighboursNumbers, reviveNeighboursNumbers);


    }

}
