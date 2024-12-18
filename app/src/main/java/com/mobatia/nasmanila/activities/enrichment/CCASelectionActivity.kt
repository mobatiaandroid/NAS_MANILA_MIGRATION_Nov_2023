package com.mobatia.nasmanila.activities.enrichment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.enrichment.adapter.CCAsActivityAdapter
import com.mobatia.nasmanila.activities.enrichment.adapter.CCAsActivityAdapterr
import com.mobatia.nasmanila.activities.enrichment.adapter.CCAsWeekListAdapter
import com.mobatia.nasmanila.activities.enrichment.model.CCADetailModel
import com.mobatia.nasmanila.activities.enrichment.model.WeekListModel
import com.mobatia.nasmanila.activities.home.HomeListAppCompatActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.AppController
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration

class CCASelectionActivity :AppCompatActivity(){
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var CCADetailModelArrayList: ArrayList<CCADetailModel?>? = null

    //    ArrayList<String> weekList;
    var headermanager: HeaderManager? = null
    var relativeHeader: RelativeLayout? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var tab_type = "CCAs"
    var extras: Bundle? = null

    //    ArrayList<String> mCcaArrayList;
    var recycler_review: RecyclerView? = null
    var weekRecyclerList: RecyclerView? = null
    var recyclerViewLayoutManager: GridLayoutManager? = null
    var recyclerweekViewLayoutManager: GridLayoutManager? = null
    var pos = 0
    var ccaDetailpos = 0
    var submitBtn: Button? = null
    var nextBtn: Button? = null
   // var AppController.filled = false
    var weekSelected = false
    var weekPosition = 0
    var mCCAsWeekListAdapter: CCAsWeekListAdapter? = null
    var TVselectedForWeek: TextView? = null
    var textViewCCAaSelect: TextView? = null
    var textViewStudName: TextView? = null
    var mCCAsActivityAdapter: CCAsActivityAdapter? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cca_selection)
        mContext = this
        progressBarDialog = ProgressBarDialog(mContext!!)
        extras = intent.extras
        if (extras != null) {
            tab_type = extras!!.getString("tab_type")!!
            //            pos = extras.getInt("pos");
           /* CCADetailModelArrayList =
                extras!!.getSerializable("CCA_Detail") as ArrayList<CCADetailModel?>?*/
        }
        CCADetailModelArrayList=PreferenceManager.getccadetailarray(mContext)
        AppController.weekList = ArrayList()
        AppController.weekListWithData = ArrayList()
//        weekList.add("Sunday");
//        weekList.add("Monday");
//        weekList.add("Tuesday");
//        weekList.add("Wednesday");
//        weekList.add("Thursday");
//        weekList.add("Friday");
//        weekList.add("Saturday");
        //        weekList.add("Sunday");
//        weekList.add("Monday");
//        weekList.add("Tuesday");
//        weekList.add("Wednesday");
//        weekList.add("Thursday");
//        weekList.add("Friday");
//        weekList.add("Saturday");

        var weektemparray:ArrayList<WeekListModel>
        weektemparray= ArrayList()
        for (i in 0..6) {
            val mWeekListModel = WeekListModel()
            mWeekListModel.weekDay=getWeekday(i)
            mWeekListModel.weekDayMMM=getWeekdayMMM(i)
            mWeekListModel.choiceStatus="0"
            mWeekListModel.choiceStatus1="0"
            weektemparray.add(mWeekListModel)
            //AppController.weekList!!.add(mWeekListModel)
        }
        AppController.weekList!!.addAll(weektemparray)
        relativeHeader = findViewById(R.id.relativeHeader)
        recycler_review = findViewById(R.id.recycler_view_cca) as RecyclerView
        weekRecyclerList = findViewById(R.id.weekRecyclerList) as RecyclerView
        TVselectedForWeek = findViewById(R.id.TVselectedForWeek) as TextView
        textViewCCAaSelect = findViewById(R.id.textViewCCAaSelect) as TextView
        textViewStudName = findViewById(R.id.textViewStudName) as TextView
        submitBtn = findViewById(R.id.submitBtn) as Button
        nextBtn = findViewById(R.id.nextBtn) as Button
        nextBtn!!.getBackground().setAlpha(255)
        submitBtn!!.getBackground().setAlpha(150)
        headermanager = HeaderManager(this@CCASelectionActivity, tab_type)
        headermanager!!.getHeader(relativeHeader, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        back!!.setOnClickListener {
            AppUtils.hideKeyBoard(mContext)
            finish()
        }
        if (PreferenceManager.getStudClassForCCA(mContext).equals("")) {
            textViewStudName!!.setText(PreferenceManager.getStudNameForCCA(mContext))
        } else {
            textViewStudName!!.text = Html.fromHtml(
                PreferenceManager.getStudNameForCCA(mContext) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(
                    mContext
                )
            )
        }
        submitBtn!!.getBackground().setAlpha(150)
       submitBtn!!.setVisibility(View.INVISIBLE)
        submitBtn!!.setOnClickListener(View.OnClickListener { //              for (int i=0;i<CCADetailModelArrayList!!.size();i++)

            if (AppController.filled) {

                val mInent = Intent(mContext, CCAsReviewActivity::class.java)
                AppController.CCADetailModelArrayList.clear()
                for (i in CCADetailModelArrayList!!.indices){
                    AppController.CCADetailModelArrayList.add(CCADetailModelArrayList!![i]!!)
                }
                startActivity(mInent)
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity,
                    "Alert",
                    "Select choice for all available days.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        })
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mContext, HomeListAppCompatActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }
        recycler_review!!.setHasFixedSize(true)
        recyclerViewLayoutManager = GridLayoutManager(mContext, 1)
        val spacing = 5 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
//        recycler_review.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        //        recycler_review.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        recycler_review!!.addItemDecoration(itemDecoration)
        recycler_review!!.layoutManager = recyclerViewLayoutManager
//        for (int i = 0; i < CCADetailModelArrayList!!.size(); i++)
//            if (CCADetailModelArrayList!!.get(i).getDay().equals("Sunday")) {
//                {
//                    ccaDetailpos=i;
//                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList!!.get(i).getCcaChoiceModel(), CCADetailModelArrayList!!.get(i).getCcaChoiceModel2(),0,AppController.weekList);
//                    recycler_review.setAdapter(mCCAsActivityAdapter);
//                    break;
//                }
//            }

        //        for (int i = 0; i < CCADetailModelArrayList!!.size(); i++)
//            if (CCADetailModelArrayList!!.get(i).getDay().equals("Sunday")) {
//                {
//                    ccaDetailpos=i;
//                    CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList!!.get(i).getCcaChoiceModel(), CCADetailModelArrayList!!.get(i).getCcaChoiceModel2(),0,AppController.weekList);
//                    recycler_review.setAdapter(mCCAsActivityAdapter);
//                    break;
//                }
//            }
        TVselectedForWeek!!.text = "Sunday"
//        for (int j = 0; j < AppController.weekList.size(); j++) {
//            for (int i = 0; i < CCADetailModelArrayList!!.size(); i++) {
//                if (!AppController.weekList.get(j).getWeekDay().equals(CCADetailModelArrayList!!.get(i).getDay())) {
//                    AppController.weekList.get(j).setChoiceStatus("2");
//                    AppController.weekList.get(j).setChoiceStatus1("2");
//                }
//                else
//                {
//                    AppController.weekList.get(j).setChoiceStatus("0");
//                    AppController.weekList.get(j).setChoiceStatus1("0");
//                }
//            }
//        }

        //        for (int j = 0; j < AppController.weekList.size(); j++) {
//            for (int i = 0; i < CCADetailModelArrayList!!.size(); i++) {
//                if (!AppController.weekList.get(j).getWeekDay().equals(CCADetailModelArrayList!!.get(i).getDay())) {
//                    AppController.weekList.get(j).setChoiceStatus("2");
//                    AppController.weekList.get(j).setChoiceStatus1("2");
//                }
//                else
//                {
//                    AppController.weekList.get(j).setChoiceStatus("0");
//                    AppController.weekList.get(j).setChoiceStatus1("0");
//                }
//            }
//        }
        for (i in 0 until AppController.weekList!!.size) {
            AppController.weekList!!.get(i).choiceStatus="2"
            AppController.weekList!!.get(i).choiceStatus1="2"
            AppController.weekList!!.get(i).dataInWeek="0"
        }


        for (i in 0 until AppController.weekList!!.size) {
            for (j in CCADetailModelArrayList!!.indices) {
                if (AppController.weekList!!.get(i).weekDay.equals(
                        CCADetailModelArrayList!!.get(j)!!.day
                    )
                ) {
                    AppController.weekList!!.get(i).choiceStatus="0"
                    AppController.weekList!!.get(i).choiceStatus1="0"
                    AppController.weekList!!.get(i).dataInWeek="1"
                    AppController.weekListWithData!!.add(i)
                }
            }
        }
        for (i in CCADetailModelArrayList!!.indices) {
            if (CCADetailModelArrayList!!.get(i)!!.day
                    .equals("Sunday")
            ) {
                ccaDetailpos = i
                textViewCCAaSelect!!.visibility = View.VISIBLE
                TVselectedForWeek!!.visibility = View.VISIBLE

               var mCCAsActivityAdapter = CCAsActivityAdapterr(mContext,
                    CCADetailModelArrayList!!.get(i)!!.choice1,
                   CCADetailModelArrayList!!.get(i)!!.choice2,
                    0,
                    AppController.weekList,
                    weekRecyclerList!!, submitBtn!!, nextBtn!!,CCADetailModelArrayList!!,AppController.filled,ccaDetailpos
                )
                recycler_review!!.adapter = mCCAsActivityAdapter
                break
            } else if (i == CCADetailModelArrayList!!.size - 1) {
                if (!CCADetailModelArrayList!!.get(i)!!.day
                        .equals("Sunday")
                ) {
                    mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0, submitBtn!!, nextBtn!!,
                        CCADetailModelArrayList!!,AppController.filled,ccaDetailpos)
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    textViewCCAaSelect!!.visibility = View.GONE
                    TVselectedForWeek!!.visibility = View.GONE
                    AppController.weekList!!.get(0).choiceStatus="2"
                    AppController.weekList!!.get(0).choiceStatus1="2"
                    //                    Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show();
                }
            }
        }


//        CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList!!.get(0).getCcaChoiceModel(), CCADetailModelArrayList!!.get(0).getCcaChoiceModel2());
//        recycler_review.setAdapter(mCCAsActivityAdapter);


//        CCAsActivityAdapter mCCAsActivityAdapter = new CCAsActivityAdapter(mContext, CCADetailModelArrayList!!.get(0).getCcaChoiceModel(), CCADetailModelArrayList!!.get(0).getCcaChoiceModel2());
//        recycler_review.setAdapter(mCCAsActivityAdapter);
        weekRecyclerList!!.setHasFixedSize(true)
//        recyclerweekViewLayoutManager = new GridLayoutManager(mContext, 7);
        //        recyclerweekViewLayoutManager = new GridLayoutManager(mContext, 7);
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.HORIZONTAL
//        weekRecyclerList.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
//        weekRecyclerList.addItemDecoration(itemDecoration);
        //        weekRecyclerList.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
//        weekRecyclerList.addItemDecoration(itemDecoration);
        weekRecyclerList!!.layoutManager = llm
//        weekRecyclerList.setLayoutManager(recyclerweekViewLayoutManager);
        //        weekRecyclerList.setLayoutManager(recyclerweekViewLayoutManager);
        mCCAsWeekListAdapter = CCAsWeekListAdapter(mContext,
            AppController.weekList!!, weekPosition)
        weekRecyclerList!!.adapter = mCCAsWeekListAdapter
weekRecyclerList!!.addOnItemClickListener(object :OnItemClickListener{
    override fun onItemClicked(position: Int, view: View) {
        for (i in CCADetailModelArrayList!!.indices) {
            if (AppController.weekList!!.get(position).weekDay
                    .equals(CCADetailModelArrayList!!.get(i)!!.day)
            ) {
                pos = i
                ccaDetailpos = i
                weekSelected = true
                break
            } else {
                weekSelected = false
            }
            if (weekSelected) {
                break
            }
        }
        if (!weekSelected) {
            textViewCCAaSelect!!.visibility = View.GONE
            TVselectedForWeek!!.visibility = View.GONE
            val mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0,submitBtn!!,nextBtn!!,
                CCADetailModelArrayList!!,AppController.filled,ccaDetailpos)
            recycler_review!!.adapter = mCCAsActivityAdapter
            mCCAsActivityAdapter.notifyDataSetChanged()
            AppController.weekList!!.get(position).choiceStatus="2"
            AppController.weekList!!.get(position).choiceStatus1="2"
            Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show()
        } else {
            textViewCCAaSelect!!.visibility = View.VISIBLE
            TVselectedForWeek!!.visibility = View.VISIBLE

            val mCCAsActivityAdapter = CCAsActivityAdapterr(
                mContext,
                CCADetailModelArrayList!!.get(pos)!!.choice1,
                CCADetailModelArrayList!!.get(pos)!!.choice2,
                position,
                AppController.weekList,
                weekRecyclerList!!, submitBtn!!, nextBtn!!,CCADetailModelArrayList!!,AppController.filled,ccaDetailpos
            )
            recycler_review!!.adapter = mCCAsActivityAdapter
            mCCAsActivityAdapter.notifyDataSetChanged()
        }
        for (j in 0 until AppController.weekList!!.size) {
            if (AppController.weekList!!.get(j).choiceStatus
                    .equals("0") || AppController.weekList!!.get(j).choiceStatus1
                    .equals("0")
            ) {
                AppController.filled = false
                break
            } else {
                AppController.filled = true
            }
            if (!AppController.filled) {
                break
            }
        }
        if (AppController.filled) {
            submitBtn!!.getBackground().setAlpha(255)
            submitBtn!!.setVisibility(View.VISIBLE)
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.GONE)
           // AppController.AppController.filledFlag = 1
        } else {
            submitBtn!!.getBackground().setAlpha(150)
            submitBtn!!.setVisibility(View.INVISIBLE)
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.VISIBLE)
        }
        weekPosition = position
        mCCAsWeekListAdapter = CCAsWeekListAdapter(mContext,
            AppController.weekList!!, weekPosition)
        weekRecyclerList!!.adapter = mCCAsWeekListAdapter
        TVselectedForWeek!!.setText(AppController.weekList!!.get(position).weekDay)
//                        horizontalScrollView
        //                        horizontalScrollView
        if (weekPosition == 6) weekRecyclerList!!.scrollToPosition(6) else weekRecyclerList!!.scrollToPosition(
            0
        )
    }

})
        for (j in 0 until AppController.weekList!!.size) {
            if (AppController.weekList!!.get(j).dataInWeek.equals("1")) {
                for (i in CCADetailModelArrayList!!.indices) {
                    if (AppController.weekList!!.get(j).weekDay.equals(
                            CCADetailModelArrayList!!.get(i)!!.day
                        )
                    ) {
                        pos = i
                        ccaDetailpos = i
                        weekSelected = true
                        break
                    } else {
                        weekSelected = false
                    }
                    if (weekSelected) {
                        break
                    }
                }
                if (!weekSelected) {
                    textViewCCAaSelect!!.visibility = View.GONE
                    TVselectedForWeek!!.visibility = View.GONE
                    val mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0,submitBtn!!,nextBtn!!,
                        CCADetailModelArrayList!!,AppController.filled,ccaDetailpos)
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    mCCAsActivityAdapter.notifyDataSetChanged()
                    AppController.weekList!!.get(j).choiceStatus="2"
                    AppController.weekList!!.get(j).choiceStatus1="2"
                    Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show()
                } else {
                    textViewCCAaSelect!!.visibility = View.VISIBLE
                    TVselectedForWeek!!.visibility = View.VISIBLE

                    val mCCAsActivityAdapter = CCAsActivityAdapterr(
                        mContext,
                        CCADetailModelArrayList!!.get(pos)!!.choice1,
                        CCADetailModelArrayList!!.get(pos)!!.choice2,
                        j,
                        AppController.weekList,
                        weekRecyclerList!!, submitBtn!!, nextBtn!!,CCADetailModelArrayList!!,AppController.filled,ccaDetailpos
                    )
                    recycler_review!!.adapter = mCCAsActivityAdapter
                    mCCAsActivityAdapter.notifyDataSetChanged()
                }
                for (k in 0 until AppController.weekList!!.size) {
                    if (AppController.weekList!!.get(k).choiceStatus
                            .equals("0") || AppController.weekList!!.get(k)
                            .choiceStatus1.equals("0")
                    ) {
                        AppController.filled = false
                        break
                    } else {
                        AppController.filled = true
                    }
                    if (!AppController.filled) {
                        break
                    }
                }
                if (AppController.filled) {
                    submitBtn!!.getBackground().setAlpha(255)
                    submitBtn!!.setVisibility(View.VISIBLE)
                    nextBtn!!.getBackground().setAlpha(255)
                    nextBtn!!.setVisibility(View.GONE)
                   // AppController.AppController.filledFlag = 1
                } else {
                    submitBtn!!.getBackground().setAlpha(150)
                    submitBtn!!.setVisibility(View.INVISIBLE)
                    nextBtn!!.getBackground().setAlpha(255)
                    nextBtn!!.setVisibility(View.VISIBLE)
                }
                weekPosition = j
                mCCAsWeekListAdapter =
                    CCAsWeekListAdapter(mContext, AppController.weekList!!, weekPosition)
                weekRecyclerList!!.adapter = mCCAsWeekListAdapter
                TVselectedForWeek!!.setText(AppController.weekList!!.get(j).weekDay)
                break
            }
        }

        if (AppController.weekListWithData!!.size > 0) {
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.VISIBLE)
        } else {
            nextBtn!!.getBackground().setAlpha(255)
            nextBtn!!.setVisibility(View.GONE)
        }

        nextBtn!!.setOnClickListener(View.OnClickListener {
            weekPosition = weekPosition + 1
            if (AppController.weekListWithData!!.contains(weekPosition)) {
                for (a in 0 until AppController.weekListWithData!!.size) {
                    if (AppController.weekListWithData!!.get(a) === weekPosition) {
                        //weekPosition = a;
                        weekPosition = AppController.weekListWithData!!.get(a)
                        break
                    }
                }

                /*           for (int a=0;a<AppController.weekListWithData.size();a++)
                        {
                            if (weekPosition==AppController.weekListWithData.get(a)) {
                                weekPosition = AppController.weekListWithData.get(a);
                            }
                        }
                        weekPosition = AppController.weekListWithData.get(weekPosition);*/
            } else {
                if (weekPosition >= AppController.weekList!!.size - 1) {
                    weekPosition = 0
                }
                if (AppController.weekListWithData!!.contains(weekPosition)) {
                    //                        weekPosition = AppController.weekListWithData.get(weekPosition);
                    for (a in 0 until AppController.weekListWithData!!.size) {
                        //                            if (AppController.weekListWithData.contains(weekPosition)) {
                        if (AppController.weekListWithData!!.get(a) === weekPosition) {
                            //                                weekPosition = a;
                            weekPosition = AppController.weekListWithData!!.get(a)
                            break
                        }
                    }
                } else {
                    for (m in weekPosition until AppController.weekList!!.size) {
                        if (AppController.weekListWithData!!.contains(m)) {
                            weekPosition = m
                            break
                        }
                    }
                    if (!AppController.weekListWithData!!.contains(weekPosition)) {
                        weekPosition = 0
                    }
                }
            }
            for (j in weekPosition until AppController.weekList!!.size) {
                if (AppController.weekList!!.get(j).dataInWeek.equals("1")) {
                    for (i in CCADetailModelArrayList!!.indices) {
                        if (AppController.weekList!!.get(j).weekDay.equals(
                                CCADetailModelArrayList!!.get(i)!!.day
                            )
                        ) {
                            pos = i
                            ccaDetailpos = i
                            weekSelected = true
                            break
                        } else {
                            weekSelected = false
                        }
                        if (weekSelected) {
                            break
                        }
                    }
                    if (!weekSelected) {
                        textViewCCAaSelect!!.visibility = View.GONE
                        TVselectedForWeek!!.visibility = View.GONE
                        val mCCAsActivityAdapter = CCAsActivityAdapter(mContext, 0,submitBtn!!,nextBtn!!,
                            CCADetailModelArrayList!!,AppController.filled,ccaDetailpos)
                        recycler_review!!.adapter = mCCAsActivityAdapter
                        mCCAsActivityAdapter.notifyDataSetChanged()
                        AppController.weekList!!.get(j).choiceStatus="2"
                        AppController.weekList!!.get(j).choiceStatus1="2"
                        //                            Toast.makeText(mContext, "CCA choice not available.", Toast.LENGTH_SHORT).show();
                    } else {
                        textViewCCAaSelect!!.visibility = View.VISIBLE
                        TVselectedForWeek!!.visibility = View.VISIBLE

                        val mCCAsActivityAdapter = CCAsActivityAdapterr(
                            mContext,
                            CCADetailModelArrayList!!.get(pos)!!.choice1,
                            CCADetailModelArrayList!!.get(pos)!!
                                .choice2,
                            j,
                            AppController.weekList,
                            weekRecyclerList!!, submitBtn!!, nextBtn!!,CCADetailModelArrayList!!,AppController.filled,ccaDetailpos
                        )
                        recycler_review!!.adapter = mCCAsActivityAdapter
                        mCCAsActivityAdapter.notifyDataSetChanged()
                    }
                    for (k in 0 until AppController.weekList!!.size) {
                        if (AppController.weekList!!.get(k).choiceStatus
                                .equals("0") || AppController.weekList!!.get(k)
                                .choiceStatus1.equals("0")
                        ) {
                            AppController.filled = false
                            break
                        } else {
                            AppController.filled = true
                        }
                        if (!AppController.filled) {
                            break
                        }
                    }
                    if (AppController.filled) {
                        submitBtn!!.getBackground().setAlpha(255)
                        submitBtn!!.setVisibility(View.VISIBLE)
                        nextBtn!!.getBackground().setAlpha(255)
                        nextBtn!!.setVisibility(View.GONE)
                        //AppController.AppController.filledFlag = 1
                    } else {
                        submitBtn!!.getBackground().setAlpha(150)
                        submitBtn!!.setVisibility(View.INVISIBLE)
                        nextBtn!!.getBackground().setAlpha(255)
                        nextBtn!!.setVisibility(View.VISIBLE)
                    }
                    weekPosition = j
                    mCCAsWeekListAdapter =
                        CCAsWeekListAdapter(mContext, AppController.weekList!!, weekPosition)
                    weekRecyclerList!!.adapter = mCCAsWeekListAdapter
                    TVselectedForWeek!!.setText(AppController.weekList!!.get(j).weekDay)
                    break
                }
            }
            if (weekPosition == 6) {
                weekRecyclerList!!.layoutManager!!.scrollToPosition(weekPosition)
            } else {
                weekRecyclerList!!.layoutManager!!.scrollToPosition(0)
            }
        })

    }
    fun getWeekday(weekDay: Int): String? {
        var day = ""
        when (weekDay) {
            0 -> day = "Sunday"
            1 -> day = "Monday"
            2 -> day = "Tuesday"
            3 -> day = "Wednesday"
            4 -> day = "Thursday"
            5 -> day = "Friday"
            6 -> day = "Saturday"
        }
        return day
    }

    fun getWeekdayMMM(weekDay: Int): String? {
        var day = ""
        when (weekDay) {
            0 -> day = "SUN"
            1 -> day = "MON"
            2 -> day = "TUE"
            3 -> day = "WED"
            4 -> day = "THU"
            5 -> day = "FRI"
            6 -> day = "SAT"
        }
        return day
    }

}
