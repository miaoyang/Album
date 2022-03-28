package com.ym.album.ui.fragment.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ym.album.R
import com.ym.album.ui.adapter.RecommendItemAdapter
import com.ym.album.ui.adapter.SearchAdapter
import com.ym.album.ui.adapter.SearchItemAdapter
import com.ym.album.ui.bean.RecommendItemBean
import com.ym.album.ui.bean.SearchBean
import com.ym.album.ui.widget.SearchView
import com.ym.album.utils.ImageMediaUtil
import com.ym.common_util.utils.LogUtil


class RecommendFragment : Fragment(),SearchView.SearchViewListener {
    companion object {
        private const val TAG = "RecommendFragment"
        private const val HINT_SIZE = 4

        @JvmStatic
        fun newInstance():RecommendFragment{
            return RecommendFragment()
        }
    }

    /**
     * 搜索view
     */
    private lateinit var searchView:SearchView

    /**
     * 滑动view
     */
    private lateinit var scrollView:ScrollView
    /**
     * 搜索显示listview
     */
    private lateinit var listView: ListView

    /**
     * 清除历史
     */
    private lateinit var mTvClearHistory:TextView

    /**
     * 热搜框adapter
     */
    private lateinit var hintAdapter:ArrayAdapter<String>

    /**
     * 自动补全adapter
     */
    private var autoCompleteAdapter:ArrayAdapter<String>?=null

    /**
     * 搜索结果adapter
     */
    private var searchAdapter:SearchAdapter?=null
    /**
     * 搜索列表item
     */
    private var searchItemAdapter:SearchItemAdapter?=null
    /**
     * 数据库数据
     */
    private lateinit var dbList:ArrayList<SearchBean>

    /**
     * 热搜板块数据
     */
    private lateinit var hintDataList:ArrayList<String>

    /**
     * 自动补全数据
     */
    private var completeDataList:ArrayList<String>?=null

    /**
     * 搜索结果数据
     */
    private  var searchResList:ArrayList<SearchBean>?=null

    /**
     * 推荐的图片
     */
    private var imgItemRecyclerView:RecyclerView?=null
    private var imgItemAdapter:RecommendItemAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_recommend, container, false)
        initView(v)
        val recommendItemBeanList:List<RecommendItemBean> = ImageMediaUtil.getRecommendItem();

        // 设置adapter
        imgItemAdapter = RecommendItemAdapter(context,recommendItemBeanList)
        imgItemRecyclerView?.adapter= imgItemAdapter

        // 设置布局manager
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        imgItemRecyclerView?.layoutManager = layoutManager

        // 设置加载动画
        imgItemRecyclerView?.scrollToPosition(0)
        imgItemRecyclerView?.layoutAnimation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_fall_down)
        imgItemRecyclerView?.scheduleLayoutAnimation()
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     */
    override fun onRefreshAutoComplete(text: String?) {
        if (text != null) {
            getAutoCompleteListData(text)
        }
    }

    override fun onStartSearch(text: String?) {
        // 更新搜索结果
        if (text != null){
            getSearchResListData(text)
        }
        scrollView.visibility = View.VISIBLE
        listView.visibility = View.VISIBLE
        if (listView.adapter == null){
//            listView.adapter = searchAdapter
            listView.adapter = searchItemAdapter
        }else{
            searchAdapter?.notifyDataSetChanged()
        }
    }

    private fun initView(v:View){
        scrollView = v.findViewById(R.id.sl_search_view)
        listView = v.findViewById(R.id.lv_search_result)
        mTvClearHistory = v.findViewById(R.id.tv_search_list_clear)
        searchView = v.findViewById(R.id.recommend_search_view)

        searchView.setHintAdapter(hintAdapter)
        searchView.setAutoCompleteAdapter(autoCompleteAdapter)

        imgItemRecyclerView = v.findViewById(R.id.rv_item_recommend_img)

    }

    private fun initData(){
        getDbListData()
        getHintListData()
        getAutoCompleteListData("")
        getSearchResListData("")
    }

    /**
     * 获取db 数据
     */
    private fun getDbListData() {
        val size = 100
        dbList = ArrayList<SearchBean>(size)
        for (i in 0 until size) {
            dbList.add(SearchBean(
                    R.drawable.iv_search_item_img_address,
                    "android开发必备技能" + (i + 1),
                    "Android自定义view——自定义搜索view",
                    (i * 20 + 2).toString()
                )
            )
        }
        LogUtil.d(TAG, "getDbListData(): $dbList")
    }

    /**
     * 获取热搜版data 和adapter
     */
    private fun getHintListData() {
        hintDataList = ArrayList<String>(HINT_SIZE)
        for (i in 1..HINT_SIZE) {
            hintDataList.add("热搜版$i：Android自定义View")
        }
        LogUtil.d(TAG,"getHintListData(): $hintDataList")
        hintAdapter = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, hintDataList) }!!
    }

    /**
     * 获取自动补全data 和adapter
     */
    private fun getAutoCompleteListData(text: String) {
        if (completeDataList == null) {
            //初始化
            completeDataList = ArrayList<String>(HINT_SIZE)
        } else {
            // 根据text 获取auto data
            completeDataList?.clear()
            var i = 0
            var count = 0
            // 查询dbDataList是否包含查询的数据
            while (i < dbList.size && count < HINT_SIZE) {
                if (dbList[i].title.contains(text.trim { it <= ' ' })) {
                    completeDataList?.add(dbList[i].title)
                    count++
                }
                i++
            }
        }
        LogUtil.d(TAG,"getAutoCompleteListData(): completeDataList=$completeDataList")
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1,
                    completeDataList!!
                ) }
        } else {
            autoCompleteAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * 获取搜索结果data和adapter
     */
    private fun getSearchResListData(text: String) {
        if ( searchResList== null) {
            // 初始化
            searchResList = ArrayList<SearchBean>()
        } else {
            searchResList?.clear()
            for (i in dbList.indices) {
                if (dbList[i].title.contains(text.trim { it <= ' ' })) {
                    searchResList?.add(dbList[i])
                }
            }
        }
        LogUtil.d(TAG,"getSearchResListData(): searchResList=$searchResList")
        searchItemAdapter = SearchItemAdapter(context,R.id.lv_search_result,searchResList)
        if (searchAdapter == null) {
            searchAdapter = SearchAdapter(context, searchResList, R.id.lv_search_result)
        } else {
            searchAdapter?.notifyDataSetChanged()
        }
    }
}

