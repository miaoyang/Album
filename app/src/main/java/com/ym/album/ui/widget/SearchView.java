package com.ym.album.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ym.album.R;
import com.ym.common_util.utils.LogUtil;

/**
 * Author:Yangmiao
 * Desc:
 * Time:2022/3/3 14:51
 */
public class SearchView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "SearchView";
    private EditText mEtSearchInput;
    private ImageView mIvDelete;
    private Context mContext;
    private ListView mLvSearchResult;
    private ArrayAdapter<String> mHintAdapter;
    private ArrayAdapter<String> mCompleteAdapter;
    private SearchViewListener listener;

    public SearchView(Context context) {
        super(context);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);
        initView();
        initData();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_search_input:
                mLvSearchResult.setVisibility(VISIBLE);
                break;
            case R.id.iv_search_delete:
                mEtSearchInput.setText("");
                mIvDelete.setVisibility(GONE);
                break;
        }
    }

    private void initView(){
        mEtSearchInput = findViewById(R.id.et_search_input);
        mIvDelete = findViewById(R.id.iv_search_delete);
        mLvSearchResult = findViewById(R.id.lv_search_result);

        mEtSearchInput.setOnClickListener(this);

        mEtSearchInput.addTextChangedListener(new EditTextWatcher());
        mIvDelete.setOnClickListener(this);
        mEtSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null){
                    if (keyEvent.getAction() == EditorInfo.IME_ACTION_SEARCH){
                        mLvSearchResult.setVisibility(GONE);
                        notifyStartChange(mEtSearchInput.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void initData(){
        mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String inputText = mLvSearchResult.getAdapter().getItem(i).toString();
                if (!TextUtils.isEmpty(inputText)){
                    mEtSearchInput.setText(inputText);
                    mEtSearchInput.setSelection(inputText.length());
                    // hide listview
                    mLvSearchResult.setVisibility(View.GONE);
                    notifyStartChange(inputText);
                }
            }
        });


    }

    private void notifyStartChange(String inputText){
        if (listener != null){
            listener.onStartSearch(inputText);
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置热搜版块的adapter
     * @param adapter
     */
    public void setHintAdapter(ArrayAdapter<String> adapter){
        this.mHintAdapter = adapter;
        if (mLvSearchResult.getAdapter() == null){
            mLvSearchResult.setAdapter(adapter);
        }
    }

    /**
     * 设置自动补全的adapter
     * @param autoCompleteAdapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> autoCompleteAdapter){
        this.mCompleteAdapter = autoCompleteAdapter;
    }

    public void setSearchRefreshListen(SearchViewListener listener){
        this.listener = listener;
    }

    private class EditTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = charSequence.toString();
            LogUtil.d(TAG,"beforeTextChanged(): text="+text);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = charSequence.toString();
            if (!TextUtils.isEmpty(text)){
                mIvDelete.setVisibility(View.VISIBLE);
                mLvSearchResult.setVisibility(View.VISIBLE);
                if (mCompleteAdapter != null && mLvSearchResult.getAdapter()!= mCompleteAdapter){
                    mLvSearchResult.setAdapter(mCompleteAdapter);
                }
                // 更新数据
                if (listener != null){
                    listener.onRefreshAutoComplete(charSequence.toString());
                }
            }else {
                mIvDelete.setVisibility(GONE);
                if (mHintAdapter != null){
                    mLvSearchResult.setAdapter(mHintAdapter);
                }
                mLvSearchResult.setVisibility(GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public interface SearchViewListener{
        /**
         * 更新补全的内容
         * @param text
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         * @param text
         */
        void onStartSearch(String text);
    }
}
