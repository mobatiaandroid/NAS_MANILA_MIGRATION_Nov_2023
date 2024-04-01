package com.mobatia.nasmanila.activities.enrichment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.CCAchoiceModel
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel
import com.mobatia.nasmanila.common.constants.AppController
import com.mobatia.nasmanila.recyclerviewmanager.RecyclerItemListener


class CCAsActivityAdapterNew : RecyclerView.Adapter<CCAsActivityAdapterNew.MyViewHolder> {
    //    ArrayList<String> mCcaArrayList;
    //    ArrayList<String> mCcaArrayListAdapter;
    //  /*  ArrayList<String> list = new ArrayList<String>() {{
    //        add("A");
    //        add("B");
    //        add("C");
    //    }};*/
    var mContext: Context
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recyclerViewLayoutManager2: GridLayoutManager? = null
    var mCCAmodelArrayList: ArrayList<CCADetailModel?>? = null
    var mCCAchoiceModel2: ArrayList<CCAchoiceModel?>? = null
    var mCCAchoiceModel1: ArrayList<CCAchoiceModel?>? = null
    var weekList: ArrayList<WeekListModel>? = null
    var recyclerWeek: RecyclerView? = null
    var dayPosition = 0
    var count = 2
    lateinit var submitBtn: Button
    lateinit var nextBtn: Button
     var filled: Boolean = false
    var ccaDetailpos:Int =0

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var listTxtView: TextView
        var recycler_review: RecyclerView
        var recycler_review2: RecyclerView

        init {
            listTxtView = view.findViewById<View>(R.id.textViewCCAaItem) as TextView
            recycler_review =
                view.findViewById<View>(R.id.recycler_view_adapter_cca) as RecyclerView
            recycler_review2 =
                view.findViewById<View>(R.id.recycler_view_adapter_cca2) as RecyclerView
        }
    }

    constructor(mContext: Context, mCcaArrayList: ArrayList<CCADetailModel?>?) {
        this.mContext = mContext
        mCCAmodelArrayList = mCcaArrayList
    }

    constructor(
        mContext: Context,
        mCCAchoiceModel1: ArrayList<CCAchoiceModel?>?,
        mCCAchoiceModel2: ArrayList<CCAchoiceModel?>?,
        mdayPosition: Int
    ) {
        this.mContext = mContext
        this.mCCAchoiceModel1 = mCCAchoiceModel1
        this.mCCAchoiceModel2 = mCCAchoiceModel2
        dayPosition = mdayPosition
    }

    constructor(
        mContext: Context,
        mCCAchoiceModel1: ArrayList<CCAchoiceModel?>?,
        mCCAchoiceModel2: ArrayList<CCAchoiceModel?>?,
        mdayPosition: Int,
        mWeekList: ArrayList<WeekListModel>?,
        recyclerView: RecyclerView?,
        submitBtn: Button, nextBtn: Button,
    ) {
        this.mContext = mContext
        this.mCCAchoiceModel1 = mCCAchoiceModel1
        this.mCCAchoiceModel2 = mCCAchoiceModel2
        dayPosition = mdayPosition
        weekList = mWeekList
        recyclerWeek = recyclerView
        count = 2
    }

    constructor(mContext: Context, mcount: Int) {
        this.mContext = mContext
        mCCAchoiceModel1 = ArrayList<CCAchoiceModel?>()
        mCCAchoiceModel2 = ArrayList<CCAchoiceModel?>()
        //        this.dayPosition=mdayPosition;
//        this.weekList=mWeekList;
        count = mcount
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CCAsActivityAdapterNew.MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_cca_activity, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (count != 0) {
            holder.recycler_review.setHasFixedSize(true)
            holder.recycler_review2.setHasFixedSize(true)
            recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
            recyclerViewLayoutManager2 = GridLayoutManager(mContext, 1)
            //        int spacing = 5; // 50px
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
//
//        holder.recycler_review.addItemDecoration(itemDecoration);
            holder.recycler_review.setLayoutManager(recyclerViewLayoutManager)
            holder.recycler_review2.setLayoutManager(recyclerViewLayoutManager2)
            //        mCcaArrayListAdapter=new ArrayList<>();
//        mCcaArrayListAdapter.add("CCA"+(position+1));
//        mCcaArrayListAdapter.add("CCA"+(position+2));
            if (position == 0) {
                if (mCCAchoiceModel1!!.size > 0) {
                    if (mCCAchoiceModel2!!.size <= 0) {
                        AppController.weekList!!.get(dayPosition).choiceStatus1=("1")
                    }
                    holder.listTxtView.setText("Choose First Choice : ") // + (position + 1)
                    val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                        mContext,
                        mCCAchoiceModel1!!,
                        dayPosition,
                        weekList,
                        0,
                        recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                    )
                    holder.recycler_review.setAdapter(mCCAsActivityAdapter)
                }
            } else {
                if (mCCAchoiceModel2!!.size > 0) {
                    if (mCCAchoiceModel1!!.size <= 0) {
                        AppController.weekList!!.get(dayPosition).choiceStatus=("1")
                    }
                    //                    holder.listTxtView.setText("Choice:" + (position + 1));
                    holder.listTxtView.setText("Choose Second Choice : ") // + (position + 1)
                    val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                        mContext,
                        mCCAchoiceModel1!!,
                        dayPosition,
                        weekList,
                        1,
                        recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                    )
                    holder.recycler_review2.setAdapter(mCCAsActivityAdapter)
                }
            }
            holder.recycler_review.addOnItemTouchListener(
                RecyclerItemListener(mContext, holder.recycler_review,
                    object : RecyclerItemListener.RecyclerTouchListener {
                        override fun onClickItem(v: View?, pos: Int) {
                            for (i in mCCAchoiceModel1!!.indices) {
                                if (pos == i) {
                                    mCCAchoiceModel1!![i]!!.status=("1")
                                    System.out.println("Choice1:" + mCCAchoiceModel1!![pos]!!.cca_item_name)
                                } else {
                                    mCCAchoiceModel1!![i]!!.status=("0")
                                    System.out.println("Choice1Else:" + mCCAchoiceModel1!![pos]!!.cca_item_name)
                                }
                            }
                            val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                                mContext,
                                mCCAchoiceModel1!!,
                                dayPosition,
                                weekList,
                                0,
                                recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                            )
                            mCCAsActivityAdapter.notifyDataSetChanged()
                            holder.recycler_review.setAdapter(mCCAsActivityAdapter)
                        }

                        override fun onLongClickItem(v: View?, position: Int) {
                            println("On Long Click Item interface")
                        }
                    })
            )
            holder.recycler_review2.addOnItemTouchListener(
                RecyclerItemListener(mContext, holder.recycler_review2,
                    object : RecyclerItemListener.RecyclerTouchListener{
                        override fun onClickItem(v: View?, pos: Int) {
                            for (i in mCCAchoiceModel2!!.indices) {
                                if (pos == i) {
                                    mCCAchoiceModel2!![i]!!.status=("1")
                                } else {
                                    mCCAchoiceModel2!![i]!!.status=("0")
                                }
                            }
                            val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                                mContext,
                                mCCAchoiceModel1!!,
                                dayPosition,
                                weekList,
                                1,
                                recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                            )
                            mCCAsActivityAdapter.notifyDataSetChanged()
                            holder.recycler_review2.setAdapter(mCCAsActivityAdapter)
                        }

                        override fun onLongClickItem(v: View?, position: Int) {
                            println("On Long Click Item interface")
                        }
                    })
            )
        }
    }

    override fun getItemCount(): Int {
//       System.out.println("Adapter---size" + mCcaArrayList.size());
        return count
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
