package com.cellular.automata.cellularautomata.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.objects.AutomataModel;
import com.cellular.automata.cellularautomata.utils.ImageHelper;

import java.util.ArrayList;

public class LoadScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ImageHelper imageHelper;
    RecyclerListener listener;

    public interface RecyclerListener {

        void modelSelected(AutomataModel model);
        void contextMenuCalled(AutomataModel model);
        Context getContext();

    }

    public void setListener(RecyclerListener listener){
        this.listener = listener;
    }

    private ArrayList<AutomataModel> models;

    public LoadScreenAdapter(ArrayList<AutomataModel> models){
        this.models = models;
    }

    public void setModels(ArrayList<AutomataModel> models){
        this.models = models;
    }

    public ArrayList<AutomataModel> getModels(){return models;}

    private class StudioItemViewHolder extends RecyclerView.ViewHolder{

        public TextView leftModelName_txt;
        public TextView rightModelName_txt;

        public ImageView leftModel_img;
        public ImageView rightModel_img;

        public StudioItemViewHolder(View itemView) {
            super(itemView);

            leftModelName_txt = itemView.findViewById(R.id.txt_model_name_left);
            rightModelName_txt = itemView.findViewById(R.id.txt_model_name_right);

            leftModel_img = itemView.findViewById(R.id.img_model_left);
            rightModel_img = itemView.findViewById(R.id.img_model_right);


            leftModel_img.setVisibility(View.INVISIBLE);
            leftModelName_txt.setVisibility(View.INVISIBLE);

            rightModel_img.setVisibility(View.INVISIBLE);
            rightModelName_txt.setVisibility(View.INVISIBLE);

        }

        public void showRightFigure(){

            rightModel_img.setVisibility(View.VISIBLE);
            rightModelName_txt.setVisibility(View.VISIBLE);

        }

        public void hideRightFigure(){
            rightModel_img.setVisibility(View.INVISIBLE);
            rightModelName_txt.setVisibility(View.INVISIBLE);
        }

        public void showLeftFigure(){

            leftModel_img.setVisibility(View.VISIBLE);
            leftModelName_txt.setVisibility(View.VISIBLE);


        }

        public void hideLeftFigure(){
            leftModel_img.setVisibility(View.INVISIBLE);
            leftModelName_txt.setVisibility(View.INVISIBLE);
        }



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false));

        imageHelper = new ImageHelper();

        return new StudioItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final AutomataModel modelLeft = position*2 > models.size()-1? null: models.get(position*2);
        final AutomataModel modelRight = position*2+1 > models.size()-1? null: models.get(position*2+1);

        final int pos = position;

        StudioItemViewHolder itemHolder = (StudioItemViewHolder)holder;

        if (modelLeft != null){
            itemHolder.leftModelName_txt.setText(modelLeft.getName());
            itemHolder.leftModel_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //CubeDataHolder.getInstance().modelToLoad = pos*2;
                    if (listener != null){
                        listener.modelSelected(modelLeft);
                    }

                }
            });

            itemHolder.leftModel_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //CubeDataHolder.getInstance().modelToLoad = pos*2;

                    if(listener != null){
                        listener.contextMenuCalled(modelLeft);
                    }
                    return true;
                }
            });

            String imageName = modelLeft.getScreenshotName() == null || modelLeft.getScreenshotName().equals("")? modelLeft.getName() : modelLeft.getScreenshotName();

            imageHelper.loadImageIntoImageView(listener.getContext(), imageName, itemHolder.leftModel_img);

            itemHolder.showLeftFigure();


        }else{
            itemHolder.hideLeftFigure();
        }

        if(modelRight!=null){
            itemHolder.rightModelName_txt.setText(modelRight.getName());
            itemHolder.rightModel_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //CubeDataHolder.getInstance().modelToLoad = pos*2+1;
                    if (listener != null){
                        listener.modelSelected(modelRight);
                    }

                }
            });

            itemHolder.rightModel_img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //CubeDataHolder.getInstance().modelToLoad = pos*2+1;

                    if(listener !=  null) {
                        listener.contextMenuCalled(modelRight);
                    }

                    return true;
                }
            });

            String imageName = modelRight.getScreenshotName() == null || modelRight.getScreenshotName().equals("")? modelRight.getName() : modelRight.getScreenshotName();

            imageHelper.loadImageIntoImageView(listener.getContext(), imageName, itemHolder.rightModel_img);

            itemHolder.showRightFigure();

        }else{
            itemHolder.hideRightFigure();
        }



    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {

        if(models == null) return 0;

        int count = models.size() % 2>0? models.size() /2+1 : models.size() /2;

        return count ;
    }
}
