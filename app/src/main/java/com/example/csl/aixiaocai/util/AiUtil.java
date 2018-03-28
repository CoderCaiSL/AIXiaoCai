package com.example.csl.aixiaocai.util;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.phoneNumber;

/**
 * 通过这个类实现一些特殊的指令
 * Created by csl on 2018/3/27.
 */

public class AiUtil {
    protected Context context;
    protected String text;
    protected String SIM1 = "卡1";
    protected String SIM2 = "卡2";
    protected int SIMTYPE = 0;
    protected List<PhoneAccountHandle> phoneAccountHandleList;

    public AiUtil(Context context){
        this.context = context;

    }
    /**
     * 拨打电话
     */
    public void callPhone(String name){
        if (isMultiSim(context) && name.contains(SIM2)){
            SIMTYPE = 1;
        }else {
            SIMTYPE = 0;
        }
        //正则表达式，提取出字段的号码
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(name);
        String pNumber = m.replaceAll("").trim();
        if (pNumber.length() > 11){
            pNumber.substring(1,pNumber.length()-1);
        }
        if (!pNumber.equals("")){
            call(context,SIMTYPE,pNumber);
        }else {
            Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (name.contains(contactName)) {
                    Cursor phone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    if (phone.moveToNext()) {
                        String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        call(context,SIMTYPE,phoneNumber);
                        break;
                    }
                }else {
                    //TODO 根据姓名没有查找到联系人给用户做一个友好提示
                    Toast.makeText(context,"找不到联系人",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    /**
     * 获取sim卡的数量
     * @param context
     * @return
     */
    public boolean isMultiSim(Context context){
        boolean result = false;
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if(telecomManager != null){
            phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            result = phoneAccountHandleList.size() >= 2;
        }
        return result;
    }
    /**
     * @param simId
     * @return
     */
    public int getIdInDBBySimId(int simId){
        Uri uri = Uri.parse("content://telephony/siminfo");
        Cursor cursor = null;
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        try {
            cursor = contentResolver.query(uri,new String[]{"_id","sim_id"},"sim_id=?",
                    new String[]{String.valueOf(simId)},null);
            if (null != cursor){
                if (cursor.moveToFirst()){
                    return cursor.getInt(cursor.getColumnIndex("_id"));
                }
            }
        }finally {
            if (null != cursor){
                cursor.close();
            }
        }
        return -1;
    }

    /**
     * 指定的卡来拨打电话
     * @param context
     * @param id 卡1或者卡2
     * @param telNum
     */
    public void call(Context context, int id, String telNum){
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if(telecomManager != null){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telNum));
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(id));
            context.startActivity(intent);
        }
    }


}
