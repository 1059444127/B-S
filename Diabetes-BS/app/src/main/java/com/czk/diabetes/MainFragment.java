package com.czk.diabetes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.czk.diabetes.DB.DBOpenHelper;
import com.czk.diabetes.signin.SignActivity;
import com.czk.diabetes.util.FontIconDrawable;
import com.czk.diabetes.util.TimeUtil;
import com.czk.diabetes.view.MeterView;

/**
 * Created by 陈忠凯 on 2017/3/7.
 */
public class MainFragment extends Fragment {

    private final static int SELECT_DATA_FROM_DB = 0;
    private View fragment;
    private ImageView userIV;
    private long currentTime;
    private MeterView meterOne;
    private MeterView meterTow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SELECT_DATA_FROM_DB:
                    float values[] = (float[]) msg.obj;
                    if (values.length> 0)
                        meterOne.setValue(values[0]);
                    if (values.length > 1)
                        meterTow.setValue(values[1]);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_mainpage, container, false);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        dealEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        //输入血糖
        currentTime = System.currentTimeMillis();
        setCircularTile(meterOne, meterTow, TimeUtil.getHourOfTheDay(System.currentTimeMillis()));
        setValue();
    }

    private void setValue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBOpenHelper helper = new DBOpenHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                float values[] = new float[2];
                Cursor c = db.query("blood_sugar_record"
                        , new String[]{"time_slot", "value"}
                        , "date=?"
                        , new String[]{TimeUtil.getYearMonthDay(currentTime)}
                        , null, null, null);
                while (c.moveToNext()) {
                    String timeSlot = c.getString(c.getColumnIndex("time_slot"));
                    if (timeSlot.equals(meterOne.getTitle()))
                        values[0] = c.getFloat(c.getColumnIndex("value"));
                    if (timeSlot.equals(meterTow.getTitle()))
                        values[1] = c.getFloat(c.getColumnIndex("value"));
                }
                Message message = new Message();
                message.obj = values;
                message.what = SELECT_DATA_FROM_DB;
                handler.sendMessage(message);
                c.close();
                db.close();
            }
        }).start();

    }

    private void setCircularTile(MeterView oneTile, MeterView towTile, int hourOfTheDay) {
        if (0 < hourOfTheDay && hourOfTheDay <= 6) {
            oneTile.setTile(getResources().getString(R.string.before_dawn));
            towTile.setTile(getResources().getString(R.string.before_breakfast));
        } else if (6 < hourOfTheDay && hourOfTheDay <= 8) {
            oneTile.setTile(getResources().getString(R.string.before_breakfast));
            towTile.setTile(getResources().getString(R.string.after_breakfast));
        } else if (8 < hourOfTheDay && hourOfTheDay <= 11) {
            oneTile.setTile(getResources().getString(R.string.after_breakfast));
            towTile.setTile(getResources().getString(R.string.before_lunch));
        } else if (11 < hourOfTheDay && hourOfTheDay <= 15) {
            oneTile.setTile(getResources().getString(R.string.before_lunch));
            towTile.setTile(getResources().getString(R.string.after_lunch));
        } else if (15 < hourOfTheDay && hourOfTheDay <= 17) {
            oneTile.setTile(getResources().getString(R.string.after_lunch));
            towTile.setTile(getResources().getString(R.string.before_dinner));
        } else if (17 < hourOfTheDay && hourOfTheDay <= 22) {
            oneTile.setTile(getResources().getString(R.string.before_dinner));
            towTile.setTile(getResources().getString(R.string.after_dinner));
        } else {
            oneTile.setTile(getResources().getString(R.string.after_dinner));
            towTile.setTile(getResources().getString(R.string.before_sleep));
        }
    }

    private void initView() {
        //登录卡片
        userIV = (ImageView) fragment.findViewById(R.id.user_icon);
        FontIconDrawable userIVfontDrawable = FontIconDrawable.inflate(getActivity(), R.xml.icon_user);
        userIVfontDrawable.setTextColor(getResources().getColor(R.color.background_white));
        userIV.setImageDrawable(userIVfontDrawable);

        ImageView chevronRightIV = (ImageView) fragment.findViewById(R.id.img_sig_chevron_right);
        FontIconDrawable chevronRightDrawable = FontIconDrawable.inflate(getActivity(), R.xml.icon_chevron_thin_right);
        chevronRightDrawable.setTextColor(getResources().getColor(R.color.theme_color_light));
        chevronRightIV.setImageDrawable(chevronRightDrawable);
        //每日一读
        ImageView cozyTipIcon = (ImageView) fragment.findViewById(R.id.cozy_tip_icon);
        FontIconDrawable cozyTipIconfontDrawable = FontIconDrawable.inflate(getActivity(), R.xml.icon_heart);
        cozyTipIconfontDrawable.setTextColor(getResources().getColor(R.color.background_white));
        cozyTipIcon.setImageDrawable(cozyTipIconfontDrawable);
        ImageView readTipIcon = (ImageView) fragment.findViewById(R.id.read_tip_icon);
        FontIconDrawable readTipIconfontDrawable = FontIconDrawable.inflate(getActivity(), R.xml.icon_book);
        readTipIconfontDrawable.setTextColor(getResources().getColor(R.color.background_white));
        readTipIcon.setImageDrawable(readTipIconfontDrawable);

        //输入血糖
        /*标题*/
        ImageView bloodTipIcon = (ImageView) fragment.findViewById(R.id.blood_tip_icon);
        FontIconDrawable bloodTipIconfontDrawable = FontIconDrawable.inflate(getActivity(), R.xml.icon_droplet);
        bloodTipIconfontDrawable.setTextColor(getResources().getColor(R.color.background_white));
        bloodTipIcon.setImageDrawable(bloodTipIconfontDrawable);
        chevronRightIV = (ImageView) fragment.findViewById(R.id.img_blood_chevron_right);
        chevronRightIV.setImageDrawable(chevronRightDrawable);

        /*表盘*/
        meterOne = (MeterView) fragment.findViewById(R.id.meter_one);
        meterTow = (MeterView) fragment.findViewById(R.id.meter_tow);
        bloodTipIconfontDrawable.setTextColor(getResources().getColor(R.color.background_white_light));
        meterOne.setIcon(bloodTipIconfontDrawable);
        meterTow.setIcon(bloodTipIconfontDrawable);
    }

    private void dealEvent() {
        //登录卡片
        View signCard = fragment.findViewById(R.id.sign_card);
        signCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignActivity.class));
            }
        });
        //每日一读


        //输入血糖
        meterOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddValueActivity.class);
                intent.putExtra("currentTime", currentTime);
                intent.putExtra("timeslot", meterOne.getTitle());
                startActivity(intent);
            }
        });
        meterTow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddValueActivity.class);
                intent.putExtra("currentTime", currentTime);
                intent.putExtra("timeslot", meterTow.getTitle());
                startActivity(intent);
            }
        });
        fragment.findViewById(R.id.blood_sugar_card_title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AddValueActivity.class);
                        intent.putExtra("currentTime", currentTime);
                        intent.putExtra("timeslot", meterTow.getTitle());
                        startActivity(intent);
                    }
                });
    }
}
