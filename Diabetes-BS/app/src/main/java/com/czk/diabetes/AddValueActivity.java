package com.czk.diabetes;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.czk.diabetes.DB.DBOpenHelper;
import com.czk.diabetes.util.FontIconDrawable;
import com.czk.diabetes.util.ThemeUtil;
import com.czk.diabetes.util.TimeUtil;
import com.czk.diabetes.util.ToastUtil;
import com.czk.diabetes.view.DateWheelPicker;
import com.czk.diabetes.view.Digitalkeyboard;
import com.czk.diabetes.view.MeterView;
import com.czk.diabetes.view.WheelPicker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 陈忠凯 on 2017/3/12.
 */
public class AddValueActivity extends BaseActivity {
    private ImageView ivIcon;
    private ImageView ivIconAdd;
    private EditText etValue;
    private Digitalkeyboard digitalkeyboard;
    private long currentTime;
    private MeterView meterView;
    private TextView tvDate;
    private WheelPicker wheelpicker;
    private KeyBoardType keyBoardType;
    private View viewValue;
    private TextView tvTimeSlot;
    private List<String> timeSlots;
    private DateWheelPicker dateWheelpicker;
    private String currentTimeSlotTitl;
    private TextView tvNext;
    private float value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);
        initData();
        initView();
        dealEvent();
    }

    private void initData() {
        currentTime = getIntent().getLongExtra("currentTime", System.currentTimeMillis());
        currentTimeSlotTitl = getIntent().getStringExtra("timeslot");
        value = getIntent().getFloatExtra("value", 0);
        keyBoardType = KeyBoardType.DIGITAL_TYPE;
        timeSlots = Arrays.asList(getResources().getStringArray(R.array.time_slots));
    }

    private void initView() {
        //头部
        ivIcon = (ImageView) findViewById(R.id.icon);
        FontIconDrawable iconArrowLeft = FontIconDrawable.inflate(getApplicationContext(), R.xml.icon_arrow_left);
        iconArrowLeft.setTextColor(getResources().getColor(R.color.white));
        ivIcon.setImageDrawable(iconArrowLeft);
        TextView tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText(getResources().getString(R.string.input_measured_value));
        //主体
        meterView = (MeterView) findViewById(R.id.meter);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvDate.setText(TimeUtil.getYearMonthDay(currentTime));
        tvTimeSlot = (TextView) findViewById(R.id.tv_time_slot);
        tvTimeSlot.setText(currentTimeSlotTitl);
        etValue = (EditText) findViewById(R.id.et_value);
        if (value != 0) {
            meterView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    meterView.setValue(value);
                    etValue.setText(String.valueOf(value));
                }
            },100);
        }
        viewValue = findViewById(R.id.value_range);
        tvNext = (TextView) findViewById(R.id.button_next);
         /*设置EditText光标可见但不弹出软键盘*/
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            etValue.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(etValue, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //键盘
        digitalkeyboard = (Digitalkeyboard) findViewById(R.id.digitalkeyboard);
        wheelpicker = (WheelPicker) findViewById(R.id.wheelpicker);
        wheelpicker.setData(timeSlots);
        wheelpicker.setSelectedItemPosition(timeSlots.indexOf(tvTimeSlot.getText()));
        dateWheelpicker = (DateWheelPicker) findViewById(R.id.date_wheelpicker);
        dateWheelpicker.setSelectedYear(TimeUtil.getYear(currentTime));
        dateWheelpicker.setSelectedMonth(TimeUtil.getMonth(currentTime));
        dateWheelpicker.setSelectedDay(TimeUtil.getDay(currentTime));
    }

    private void dealEvent() {
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //主体
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyBoardType == KeyBoardType.DIGITAL_TYPE) {
                    keyBoardType = keyBoardType.DATE_TYPE;
                    alphaAnimator(digitalkeyboard, dateWheelpicker);
                    tvDate.setTextColor(ThemeUtil.getThemeColor());
                    etValue.setEnabled(false);
                } else if (keyBoardType == KeyBoardType.TIME_TYPE) {
                    keyBoardType = keyBoardType.DATE_TYPE;
                    alphaAnimator(wheelpicker, dateWheelpicker);
                    tvTimeSlot.setTextColor(getResources().getColor(R.color.txt_light_color));
                    tvDate.setTextColor(ThemeUtil.getThemeColor());
                    etValue.setEnabled(false);
                }
            }
        });
        tvTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyBoardType == KeyBoardType.DIGITAL_TYPE) {
                    keyBoardType = keyBoardType.TIME_TYPE;
                    alphaAnimator(digitalkeyboard, wheelpicker);
                    tvTimeSlot.setTextColor(ThemeUtil.getThemeColor());
                    etValue.setEnabled(false);
                } else if (keyBoardType == KeyBoardType.DATE_TYPE) {
                    keyBoardType = keyBoardType.TIME_TYPE;
                    alphaAnimator(dateWheelpicker, wheelpicker);
                    tvTimeSlot.setTextColor(ThemeUtil.getThemeColor());
                    tvDate.setTextColor(getResources().getColor(R.color.txt_light_color));
                    etValue.setEnabled(false);
                }
            }
        });

        viewValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyBoardType == KeyBoardType.DATE_TYPE) {
                    keyBoardType = keyBoardType.DIGITAL_TYPE;
                    alphaAnimator(dateWheelpicker, digitalkeyboard);
                    tvDate.setTextColor(getResources().getColor(R.color.txt_light_color));
                    etValue.setEnabled(true);
                } else if (keyBoardType == KeyBoardType.TIME_TYPE) {
                    keyBoardType = keyBoardType.DIGITAL_TYPE;
                    alphaAnimator(wheelpicker, digitalkeyboard);
                    tvTimeSlot.setTextColor(getResources().getColor(R.color.txt_light_color));
                    etValue.setEnabled(true);
                }
            }
        });

        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty())
                    meterView.setValue(Float.valueOf(s.toString()));
                else
                    meterView.setValue(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //键盘
        digitalkeyboard.setKeyClickListener(new Digitalkeyboard.OnKeyClickListener() {
            @Override
            public void onClick(int key) {
                String value = etValue.getText().toString();
                if (-1 == key) {
                    if (value.length() > 0)
                        value = value.substring(0, value.length() - 1);
                } else if (-2 == key) {
                    if (!value.contains(".") && value.length() > 0)
                        value = value + ".";
                } else if (value.length() < 2 || (value.contains(".") && value.substring(value.length()-1,value.length()).equals("."))) {
                    value = value + key;
                }
                if (value.length() > 0) {
                    if (Float.parseFloat(value) < 25) {
                        etValue.setText(value);
                        etValue.setSelection(value.length());
                    }
                } else {
                    etValue.setText(value);
                    //移动光标
                    etValue.setSelection(value.length());
                }
            }
        });

        wheelpicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                tvTimeSlot.setText(data.toString());
            }
        });

        dateWheelpicker.setOnItemSelectedListener(new DateWheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String data) {
                tvDate.setText(data);
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etValue.getText().toString();
                if (!value.isEmpty() && !value.substring(value.length()-1,value.length()).equals(".")) {
                    DBOpenHelper helper = new DBOpenHelper(AddValueActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();  //得到的是SQLiteDatabase对象
                    StringBuffer sBuffer = new StringBuffer();
                    sBuffer.append("REPLACE INTO blood_sugar_record ");
                    sBuffer.append("(_id,date,time_slot,value) values (");
                    sBuffer.append("'" + tvDate.getText() + tvTimeSlot.getText() + "',");
                    sBuffer.append("'" + tvDate.getText() + "',");
                    sBuffer.append(timeSlots.indexOf(tvTimeSlot.getText().toString()) + ",");
                    sBuffer.append(etValue.getText());
                    sBuffer.append(")");
                    // 执行创建表的SQL语句
                    db.execSQL(sBuffer.toString());
                    db.close();
                } else {
                    ToastUtil.showShortToast(AddValueActivity.this
                            , getResources().getString(R.string.check_value));
                }
            }
        });
    }

    /**
     * 虚实切换view
     *
     * @param view1
     * @param view2
     */
    private void alphaAnimator(final View view1, final View view2) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                view1,
                "alpha",
                1,
                0);
        animator.setDuration(300);
        animator.start();
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(
                view2,
                "alpha",
                0,
                1);
        animator2.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view1.setVisibility(View.GONE);
                view1.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view2.setVisibility(View.VISIBLE);
                view2.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private enum KeyBoardType {
        DIGITAL_TYPE, DATE_TYPE, TIME_TYPE
    }
}
