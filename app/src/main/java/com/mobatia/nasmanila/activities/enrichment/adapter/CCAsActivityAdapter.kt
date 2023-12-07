package com.mobatia.nasmanila.activities.enrichment.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.CCAchoiceModel
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.AppController

class CCAsActivityAdapter(
    var mContext: Context, var mcount: Int,
    var submitBtn:Button,
    var nextBtn:Button,
    var mCCAmodelArrayList: ArrayList<CCADetailModel?>? = null,
    var filled: Boolean,var ccaDetailpos:Int) :
    RecyclerView.Adapter<CCAsActivityAdapter.ViewHolder>() {

    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recyclerViewLayoutManager2: GridLayoutManager? = null
    //var mCCAmodelArrayList: ArrayList<CCADetailModel?>? = null
    var mCCAchoiceModel2: ArrayList<CCAchoiceModel?>? = null
    var mCCAchoiceModel1: ArrayList<CCAchoiceModel?>? = null
    var weekList: ArrayList<WeekListModel>? = null
    var recyclerWeek: RecyclerView? = null
    var dayPosition = 0
    var count = 2
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_cca_activity, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (count != 0) {
            holder.recycler_review.setHasFixedSize(true)
            holder.recycler_review2.setHasFixedSize(true)
            recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
            recyclerViewLayoutManager2 = GridLayoutManager(mContext, 1)
            //        int spacing = 5; // 50px
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
//
//        holder.recycler_review.addItemDecoration(itemDecoration);
            holder.recycler_review.layoutManager = recyclerViewLayoutManager
            holder.recycler_review2.layoutManager = recyclerViewLayoutManager2
            //        mCcaArrayListAdapter=new ArrayList<>();
//        mCcaArrayListAdapter.add("CCA"+(position+1));
//        mCcaArrayListAdapter.add("CCA"+(position+2));
            if (position == 0) {
                if (mCCAchoiceModel1!!.size > 0) {
                    if (mCCAchoiceModel2!!.size <= 0) {
                        AppController.weekList!!.get(dayPosition).choiceStatus1="1"
                    }
                    holder.listTxtView.text = "Choose First Choice : " // + (position + 1)
                    val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                        mContext,
                        mCCAchoiceModel1!!,
                        dayPosition,
                        weekList,
                        0,
                        recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                    )
                    holder.recycler_review.adapter = mCCAsActivityAdapter
                }
            } else {
                if (mCCAchoiceModel2!!.size > 0) {
                    if (mCCAchoiceModel1!!.size <= 0) {
                        AppController.weekList!!.get(dayPosition).choiceStatus="1"
                    }
                    //                    holder.listTxtView.setText("Choice:" + (position + 1));
                    holder.listTxtView.text = "Choose Second Choice : " // + (position + 1)
                    val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                        mContext,
                        mCCAchoiceModel2!!,
                        dayPosition,
                        weekList,
                        1,
                        recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                    )
                    holder.recycler_review2.adapter = mCCAsActivityAdapter
                }
            }
            holder.recycler_review.addOnItemClickListener(object :OnItemClickListener{
                override fun onItemClicked(pos: Int, view: View) {
                    for (i in mCCAchoiceModel1!!.indices) {
                        if (pos == i) {
                            mCCAchoiceModel1!!.get(i)!!.status="1"
                            System.out.println(
                                "Choice1:" + mCCAchoiceModel1!!.get(pos)!!.cca_item_name
                            )
                        } else {
                            mCCAchoiceModel1!!.get(i)!!.status="0"
                            System.out.println(
                                "Choice1Else:" + mCCAchoiceModel1!!.get(pos)!!
                                    .cca_item_name
                            )
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
                    holder.recycler_review.adapter = mCCAsActivityAdapter
                }

            })
           holder.recycler_review2.addOnItemClickListener(object :OnItemClickListener{
               override fun onItemClicked(pos: Int, view: View) {
                   for (i in mCCAchoiceModel2!!.indices) {
                       if (pos == i) {
                           mCCAchoiceModel2!!.get(i)!!.status="1"
                       } else {
                           mCCAchoiceModel2!!.get(i)!!.status="0"
                       }
                   }
                   val mCCAsActivityAdapter = CCAsChoiceListActivityAdapter(
                       mContext,
                       mCCAchoiceModel2!!,
                       dayPosition,
                       weekList,
                       1,
                       recyclerWeek,submitBtn,nextBtn,mCCAmodelArrayList,filled,ccaDetailpos
                   )
                   mCCAsActivityAdapter.notifyDataSetChanged()
                   holder.recycler_review2.adapter = mCCAsActivityAdapter
               }

           })

        }

    }

    override fun getItemCount(): Int {
        return count
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var listTxtView: TextView
        var recycler_review: RecyclerView
        var recycler_review2: RecyclerView

        init {

            listTxtView = view.findViewById(R.id.textViewCCAaItem) as TextView
            recycler_review =
                view.findViewById(R.id.recycler_view_adapter_cca) as RecyclerView
            recycler_review2 =
                view.findViewById(R.id.recycler_view_adapter_cca2) as RecyclerView
        }
    }
}