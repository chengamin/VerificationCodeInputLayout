package com.rj.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 将要实现:
 * 本文将要实现的是类似滴滴一样输入验证码的控件
 * 解决思路:
 * 1. 根据用户传入的输入框数量动态添加至布局
 * 2.
 */
public class VerificationCodeInputLayout extends LinearLayout implements TextWatcher, View.OnKeyListener {

    private final static String TYPE_NUMBER = "number";
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_PASSWORD = "password";
    private final static String TYPE_PHONE = "phone";


    // 父布局宽度
    private int parentWidth = 0;
    // 输入框宽度
    private int boxWidth = 0;
    // 当前选中position
    private int currentPosition = 0;
    // 当前存储所有EditText的集合
    private List<EditText> editTextList = new ArrayList<>();


    // 输入框数量
    private int boxCount = 0;
    // 选中背景
    private Drawable drawableFocus = null;
    // 未选中背景
    private Drawable drawableNormal = null;
    // 输入框间距
    private float boxSpacing = 0;
    // 文字大小
    private float textSize = 0;
    // 文字颜色
    private int textColor = 0;
    // 输入方式
    private String inputType = TYPE_NUMBER;

    public VerificationCodeInputLayout(Context context) {
        this(context, null);
    }

    public VerificationCodeInputLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setGravity(Gravity.CENTER);
        this.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        initAttrs(context, attrs);
        initViews(context);
    }


    /**
     * 获取设置的数据
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInputLayout);
        boxCount = typedArray.getInteger(R.styleable.VerificationCodeInputLayout_boxCount, 6);
        drawableFocus = typedArray.getDrawable(R.styleable.VerificationCodeInputLayout_boxDrawableFocus);
        if (drawableFocus == null) {
            drawableFocus = getResources().getDrawable(R.drawable.verification_edit_bg_focus);
        }
        drawableNormal = typedArray.getDrawable(R.styleable.VerificationCodeInputLayout_boxDrawableNormal);
        if (drawableNormal == null) {
            drawableNormal = getResources().getDrawable(R.drawable.verification_edit_bg_normal);
        }
        boxSpacing = typedArray.getDimension(R.styleable.VerificationCodeInputLayout_boxSpacing, 8);
        textSize = typedArray.getDimension(R.styleable.VerificationCodeInputLayout_textSize, 14);
        textColor = typedArray.getColor(R.styleable.VerificationCodeInputLayout_textColor, getResources().getColor(android.R.color.black));
        inputType = typedArray.getString(R.styleable.VerificationCodeInputLayout_inputType);
        typedArray.recycle();
    }

    /**
     * 初始化所有的子View,并添加至父布局
     */
    private void initViews(Context context) {
        for (int i = 0; i < boxCount; i++) {
            EditText editText = new EditText(context);
            editText.setMaxEms(1);
            editText.setSingleLine(true);
            editText.setMaxLines(1);
            editText.setTextSize(textSize);
            editText.setTextColor(textColor);
            if (TYPE_NUMBER.equals(inputType)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD.equals(inputType)){
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT.equals(inputType)){
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE.equals(inputType)){
                editText.setInputType(InputType.TYPE_CLASS_PHONE);

            }
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.addTextChangedListener(this);
            editText.setOnKeyListener(this);
            if (i == 0) {
                editText.setBackground(drawableFocus);
            } else {
                editText.setBackground(drawableNormal);
            }
            editText.setGravity(Gravity.CENTER);
            addView(editText, i);

            editTextList.add(i, editText);
        }
    }

    /**
     * 代码解释:
     * 1. 根据父布局的宽度计算出每个输入框的宽度,因为在经历onMeasure()方法以后,我们就可以确定父控件的大小
     * 2. 获取到每个输入控件的大小以后,我们需要对已经添加的子View进行重新换算
     * 3. 确定子View的大小
     * 4. 根据所有的控件,边距以及间距确定父View的大小以及高低
     * 5. setMeasuredDimension()方法存储父View的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*-----------------------------获取子控件尺寸大小-------------------------------------------*/
        // 根据父布局的宽度计算出每个输入框的宽度
        boxWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - (((int) boxSpacing) * (boxCount + 1))) / boxCount;
        LinearLayout.LayoutParams layoutParams = null;
        //重新计算子控件的宽度以及高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            layoutParams = new LinearLayout.LayoutParams(boxWidth, boxWidth);
            child.setLayoutParams(layoutParams);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        /*-----------------------------获取父布局尺寸大小-------------------------------------------*/
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            int childWidth = (getMeasuredWidth() * boxCount) + (int) boxSpacing * (boxCount + 1) + getPaddingLeft() + getPaddingRight();
            int childHeight = child.getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(resolveSize(childWidth, widthMeasureSpec), resolveSize(childHeight, heightMeasureSpec));
        }
    }

    /**
     * 左宽度规律:
     * 例如:  当前paddingLeft = 20 , 间距 = 16 ,  控件宽为68
     * 第一个输入框的左的X应为: 20 + 16
     * 第二个输入框的左的X应为: 20 + 16 + 68 + 16
     * 第三个输入框的左的X应为: 20 + 16 + 68 + 16 + 68 +16
     * 总结规律应为:
     * (左边距 + 间距) + ((控件宽度+间距) * i )
     * 解释:
     * 因为第一次的时候,i==0,无论谁乘0,结果自然为0
     * 右宽度规律:
     * 依据上面的解释,在确定左距离的时候,已经加入了间距,所以此时只需要用确定好的左距离 + 控件宽度 即可.
     * <p>
     * 调整子布局的所有布局位置:
     * 1. measureWidth:通过onMeasure()方法获取到每个输入控件的宽度
     * 2. measureHeight:通过onMeasure()方法获取到每个输入控件的高度
     * 3. lLeft:
     * paddingLeft+间距+(控件宽度+间距)*i
     * lRight:
     * lLeft + 控件宽度
     * lTop  :
     * paddingTop()
     * lBottom:
     * paddingTop() + 控件高度
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            int measureWidth = child.getMeasuredWidth();
            int measureHeight = child.getMeasuredHeight();
            int lLeft = getPaddingLeft() + (int) boxSpacing + (i * (measureWidth + (int) boxSpacing));
            int lRight = lLeft + measureWidth;
            int lTop = getPaddingTop();
            int lBottom = lTop + measureHeight;
            child.layout(lLeft, lTop, lRight, lBottom);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != editTextList.size() - 1) {
            currentPosition++;
            editTextList.get(currentPosition).requestFocus();
            //设置现在的输入框的背景
            editTextList.get(currentPosition).setBackground(drawableFocus);
            //设置之前的输入框的背景
            editTextList.get(currentPosition - 1).setBackground(drawableNormal);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString()) && s.length() != 0) {
            requestEditFocus();
            isComplete();
        }
    }

    /**
     * 通过判断某个EditText没有内容来获取焦点
     */
    private void requestEditFocus() {
        EditText editText = null;
        for (int i = 0; i < getChildCount(); i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().toString().trim().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    /**
     * 是否输入完成
     */
    private void isComplete() {
        StringBuffer stringBuffer = new StringBuffer();
        boolean full = true;
        for (int i = 0; i < editTextList.size(); i++) {
            EditText editText = editTextList.get(i);
            String content = editText.getText().toString().trim();
            if (TextUtils.isEmpty(content) || content.length() == 0) {
                full = false;
                return;
            } else {
                stringBuffer.append(content);
            }
        }
        if (full && onInputCompleteListener != null) {
            onInputCompleteListener.onInputComplete(stringBuffer.toString());
            setEnabled(false);
        }

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        EditText editText = (EditText) v;
        // 如果按下的是删除按钮,并且此时的文本为空的话
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            //如果此时的动作是按下,并且现在的索引不为第一个输入框,则进行操作
            if (event.getAction() == KeyEvent.ACTION_DOWN && currentPosition != 0) {
                currentPosition--;
                editTextList.get(currentPosition).requestFocus();
                //设置现在的输入框的背景
                editTextList.get(currentPosition).setBackground(drawableFocus);
                //设置删除之前的输入框的背景
                editTextList.get(currentPosition + 1).setBackground(drawableNormal);
                editTextList.get(currentPosition).setText("");
            }
        }
        return false;
    }


    public interface onInputCompleteListener {
        void onInputComplete(String content);
    }

    private onInputCompleteListener onInputCompleteListener = null;

    public void setOnInputCompleteListener(onInputCompleteListener onInputCompleteListener) {
        this.onInputCompleteListener = onInputCompleteListener;
    }
}
