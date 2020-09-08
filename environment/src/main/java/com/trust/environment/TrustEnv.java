package com.trust.environment;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.trust.environment.utils.Constants;

import static com.trust.environment.utils.Constants.ENVIRONMENT_KEY;
import static com.trust.environment.utils.Constants.ENVIRONMENT_PROD_VALUE;
import static com.trust.environment.utils.Constants.ENVIRONMENT_STG_VALUE;
import static com.trust.environment.utils.Constants.ENVIRONMENT_TST_VALUE;


/**
 *
 */
public class TrustEnv {

    private Context context;
    private String urlProd;
    private String urlDev;
    private String urlStg;
    private String pinCode;
    private String trustId;
    private int imgLogo;

    /**
     *
     *
     */
    public static class Builder {
        private Context mContext;
        private String mUrlProd;
        private String mUrlDev;
        private String mUrlStg;
        private String mPinCode;
        private String mTrustId;
        private int mImgLogo;



        public Builder(Context context){
            if(!Hawk.isBuilt()){ Hawk.init(context).build(); }
            this.mContext = context;
        }

        public Builder withProdURL(String prodURL){
            Hawk.put(Constants.URL_ENVIRONMENT_PROD_KEY,prodURL);
            this.mUrlProd = prodURL;
            return  this;
        }
        public Builder withDevURL(String devURL){
            Hawk.put(Constants.URL_ENVIRONMENT_TST_KEY,devURL);
            this.mUrlDev = devURL;
            return this;
        }
        public Builder withStgURL(String stgURL){
            Hawk.put(Constants.URL_ENVIRONMENT_STG_KEY,stgURL);
            this.mUrlStg = stgURL;
            return this;
        }
        public Builder withPinCode(String pintCode){
            this.mPinCode = pintCode;
            return this;
        }
        public Builder withImgLogo(int imgLogo){
            this.mImgLogo = imgLogo;
            return this;
        }
        public Builder withTrustId(String trustId){
            this.mTrustId = trustId;
            return this;
        }

        public TrustEnv build(){
            TrustEnv trustEnv = new TrustEnv();
            trustEnv.context = this.mContext;
            trustEnv.pinCode = this.mPinCode;
            trustEnv.trustId = this.mTrustId;
            trustEnv.urlDev = this.mUrlDev;
            trustEnv.urlProd = this.mUrlProd;
            trustEnv.urlStg = this.mUrlStg;
            trustEnv.imgLogo = this.mImgLogo;
            if(!Hawk.isBuilt()){ Hawk.init(trustEnv.context).build(); }
            return trustEnv;
        }
    }


    /**
     *
     */
   private TrustEnv(){

   }

    /**
     *
     */
   public void showEnvironmentModal(){

       final Dialog dialog = new Dialog(context);
       dialog.setCancelable(false);
       dialog.setContentView(R.layout.modal_environment_app);

       final LinearLayout linearCode = dialog.findViewById(R.id.linear_code);
       final LinearLayout linearEnv = dialog.findViewById(R.id.linear_environment);

       final RadioButton radioProd = dialog.findViewById(R.id.radio_prod);
       final RadioButton radioTst = dialog.findViewById(R.id.radio_tst);
       final RadioButton radioStg = dialog.findViewById(R.id.radio_stg);

       Button btnCancelEnv = dialog.findViewById(R.id.btn_cancel_environment);
       Button btnAcceptEnv = dialog.findViewById(R.id.btn_acept_environment);

       final EditText etPinCode = dialog.findViewById(R.id.et_code);

       final TextView tvTitle = dialog.findViewById(R.id.text_title_environment);
       final TextView tvTrustId = dialog.findViewById(R.id.txt_trustid);
       final TextView tvCurrentEnv = dialog.findViewById(R.id.txt_current_env);

       //initial view
       tvTitle.setText(Constants.TITLE_CODE);
       linearCode.setVisibility(View.VISIBLE);
       linearEnv.setVisibility(View.GONE);

       tvTrustId.setText(trustId);
       tvTrustId.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
               ClipData clip = ClipData.newPlainText("com.trust.TrustID", trustId);
               clipboard.setPrimaryClip(clip);
               Toast.makeText(context, "Copy to clipboard", Toast.LENGTH_SHORT).show();
               return true;
           }
       });

       if(Hawk.contains(ENVIRONMENT_KEY)){
            switch (Hawk.get(ENVIRONMENT_KEY).toString()){
                case  Constants.ENVIRONMENT_PROD_VALUE:{
                    tvCurrentEnv.setText("Current env: PROD");
                    break;
                }
                case  Constants.ENVIRONMENT_STG_VALUE:{
                    tvCurrentEnv.setText("Current env: STG");
                    break;
                }
                case  Constants.ENVIRONMENT_TST_VALUE:{
                    tvCurrentEnv.setText("Current env: TST");
                    break;
                }
                default:{
                    radioProd.setChecked(true);
                }
            }
        }



       btnAcceptEnv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(etPinCode.getText().toString().equals(pinCode)){
                    linearCode.setVisibility(View.GONE);
                    linearEnv.setVisibility(View.VISIBLE);
                    tvTitle.setText(Constants.TITLE_ENVIRONMENT);

                   if(radioProd.isChecked()){
                       Toast.makeText(context,"Environment PROD: ON",Toast.LENGTH_SHORT).show();
                       Hawk.put(ENVIRONMENT_KEY,ENVIRONMENT_PROD_VALUE);
                       dialog.dismiss();
                   }
                   if(radioTst.isChecked()){
                       Toast.makeText(context,"Environment TST: ON",Toast.LENGTH_SHORT).show();
                       Hawk.put(ENVIRONMENT_KEY,ENVIRONMENT_TST_VALUE);
                       dialog.dismiss();
                   }
                   if(radioStg.isChecked()){
                       Toast.makeText(context,"Environment STG: ON",Toast.LENGTH_SHORT).show();
                       Hawk.put(ENVIRONMENT_KEY,ENVIRONMENT_STG_VALUE);
                       dialog.dismiss();
                   }
               }
               else{
                   Toast.makeText(context, "Invalid PIN code.", Toast.LENGTH_SHORT).show();
               }
           }
       });

       btnCancelEnv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
           }
       });

       dialog.show();

   }

    /**
     *
     */
   public static String getUrlEnvironment(){
        try{
            if(Hawk.contains(ENVIRONMENT_KEY)){
                if (Hawk.get(ENVIRONMENT_KEY).toString().equals(Constants.ENVIRONMENT_PROD_VALUE)){
                    return Hawk.get(Constants.URL_ENVIRONMENT_PROD_KEY).toString();
                }
                if (Hawk.get(ENVIRONMENT_KEY).toString().equals(Constants.ENVIRONMENT_TST_VALUE)){
                    return Hawk.get(Constants.URL_ENVIRONMENT_TST_KEY).toString();
                }
                if (Hawk.get(ENVIRONMENT_KEY).toString().equals(Constants.ENVIRONMENT_STG_VALUE)){
                    return Hawk.get(Constants.URL_ENVIRONMENT_STG_KEY).toString();
                }
            }
            return Hawk.get(Constants.URL_ENVIRONMENT_PROD_KEY).toString();
        }catch (Exception ex){
            Log.d("error",ex.getMessage());
            return "error: " + ex.getMessage();
        }


   }
}
