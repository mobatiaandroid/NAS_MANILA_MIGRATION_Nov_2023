package com.mobatia.nasmanila.activities.contact_us.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.contact_us.model.Categori

class StaffAdapterAdapterNew(
    var mContext: Context, var mStaffModels: ArrayList<Categori>

) :
    RecyclerView.Adapter<StaffAdapterAdapterNew.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.customadapter_staffdept_listitem, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        // System.out.println("Adapter---" + position + "--" + hashmap.get(deptArrayList.get(position)).size());
        if (mStaffModels[position].staffList!!.size > 0) {
            holder.deptName.setText(mStaffModels[position].departmentName)
        } /*else if(hashmap.get(deptArrayList.get(position)).size()>0&&deptArrayList.size()==1){
           holder.deptLayout.setVisibility(View.GONE);
           //Toast.makeText(mContext)
           //AppUtils.showDialogAlertFinish();
           AppUtils.showDialogAlertFinish((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.no_details_available), R.drawable.exclamationicon, R.drawable.round);

       }*/ else {
            holder.deptLayout.visibility = View.GONE
        }

        holder.separator.visibility = View.GONE
        val customStaffDeptRecyclerAdapter = CustomStaffDeptRecyclerAdapter(
            mContext,
            mStaffModels[position].staffList!!, "list"
        )
        holder.mStaffDeptListView.adapter = customStaffDeptRecyclerAdapter
    }

    override fun getItemCount(): Int {
        return mStaffModels!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var deptLayout: LinearLayout
        var separator: View
        var deptName: TextView
        var mStaffDeptListView: RecyclerView

        init {

            deptLayout = view.findViewById(R.id.deptLayout)
            mStaffDeptListView = view.findViewById(R.id.mStaffDepListView)
            deptName = view.findViewById(R.id.departmentName)
            separator = view.findViewById(R.id.separator)
            //mStaffDeptListView = (RecyclerView) view.findViewById(R.id.mStaffListView);
            //mStaffDeptListView = (RecyclerView) view.findViewById(R.id.mStaffListView);
            mStaffDeptListView.setHasFixedSize(true)
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            mStaffDeptListView.layoutManager = llm
        }
    }
}