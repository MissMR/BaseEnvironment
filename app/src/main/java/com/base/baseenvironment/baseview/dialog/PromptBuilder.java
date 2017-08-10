package com.base.baseenvironment.baseview.dialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2017/8/9.
 */

public class PromptBuilder {
    String title = "";
    String message = "";
    String canceText = "";
    String okText = "";
    DialogInterface.OnClickListener canceClickListener,onClickListener;
    Context context;
    PromptDialog promptDialog;

    public PromptBuilder(Context context) {
        this.context = context;
    }

    public PromptBuilder setTitle(String title){
       this.title = title;
        return  this;
    }

    public PromptBuilder setMessage(String message){
      this.message = message;
        return this;
    }

    public PromptBuilder setCancelListener(String canceText,DialogInterface.OnClickListener onClickListener){
         this.canceText = canceText;
        canceClickListener=onClickListener;
        return  this;
    }

    public PromptBuilder setOkListener(String okText,DialogInterface.OnClickListener onClickListener){
        this.okText = okText;
        this.onClickListener=onClickListener;
        return this;
    }


    public void create (){
         promptDialog = new PromptDialog(context);
        promptDialog.show();
        if (!title.isEmpty()){
            promptDialog.setTitle(title);
            title = "";
        }

        if (!message.isEmpty()){
            promptDialog.setMessage(message);
            message = "";
        }

        if (canceClickListener != null){
            promptDialog.setCancelListener(canceText,canceClickListener);
            canceClickListener = null;
        }

        if (onClickListener != null){
            promptDialog.setOkListener(okText,onClickListener);
            onClickListener = null;
        }

    }





}
