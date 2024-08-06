package com.mobatia.nasmanila.fragments.contact_us

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.contact_us.StaffDirectoryActivity
import com.mobatia.nasmanila.common.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.contact_us.adapter.ContactUsAdapter
import com.mobatia.nasmanila.fragments.contact_us.model.ContactUsListModel
import com.mobatia.nasmanila.fragments.contact_us.model.ContactUsResponseModel
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactUsFragment() : Fragment(), LocationListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {
    // TODO: Rename and change types of parameters
    lateinit var mContext: Context
    var progressBarDialog: ProgressBarDialog? = null
    var mTitleTextView: TextView? = null
    var desc:android.widget.TextView? = null

    //private RelativeLayout mProgressRelLayout;
    var anim: RotateAnimation? = null
    lateinit var locationManager: LocationManager
    private val loadingFlag = true
    var mLoadUrl: String? = null
    private val mErrorFlag = false
    var latitude: String? =
        null
    var longitude:String? = null
    var description:String? = null
    var c_latitude:String? = null
    var c_longitude:String? = null
    lateinit var contactUsModelsArrayList: ArrayList<ContactUsListModel>
    var contactList: RecyclerView? = null
    lateinit var mMap: GoogleMap
    var mapFragment: SupportMapFragment? = null
    private val lm: LocationManager? = null
    var isGPSEnabled = false
    var isNetworkEnabled = false
    var lat: Double? = null
    var lng:Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = requireContext()
        progressBarDialog = ProgressBarDialog(mContext!!)
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }

        initialiseUI()
        if (AppUtils.isNetworkConnected(mContext)) {
            getlatlong()
            callcontactUsApi()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }

        // permissioncheck()


    }

    private fun callcontactUsApi() {

        anim = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim!!.setInterpolator(mContext, android.R.interpolator.linear)
        anim!!.repeatCount = Animation.INFINITE
        anim!!.duration = 1000
        progressBarDialog!!.show()
        val call: Call<ContactUsResponseModel> = ApiClient.getClient.contact_us("Bearer "+ PreferenceManager.getAccessToken(mContext))
        call.enqueue(object : Callback<ContactUsResponseModel> {
            override fun onResponse(
                call: Call<ContactUsResponseModel>,
                response: Response<ContactUsResponseModel>
            ) {
                val responseData = response.body()
                if (responseData!!.responsecode.equals("200")) {
                    progressBarDialog!!.dismiss()
                    var status_code = responseData!!.response.statuscode
                    if (status_code.equals("303")) {

                        latitude = responseData!!.response.data.latitude
                        longitude = responseData!!.response.data.longitude
                        description = responseData!!.response.data.description
                        desc!!.text = description
                        if (responseData!!.response.data.contacts.size > 0) {
                            for (i in 0..responseData!!.response.data.contacts.size) {
                                //	System.out.println("length"+contact.length());
                                if (i < responseData!!.response.data.contacts.size) {
                                    val questionsObject: ContactUsListModel=
                                        responseData!!.response.data.contacts[i]
                                    println("working")
                                    //JSONObject cObj = contact.getJSONObject(i);
                                    val contactUsModel = ContactUsListModel()
                                    contactUsModel.email=questionsObject.email
                                    contactUsModel.phone=questionsObject.phone
                                    contactUsModel.name=questionsObject.name
                                    contactUsModelsArrayList.add(contactUsModel)
                                } else if (i == responseData!!.response.data.contacts.size
                                ) {
                                    println("working ****")
                                    val contactUsModel = ContactUsListModel()
                                    contactUsModel.phone=""
                                    contactUsModel.email=""
                                    contactUsModel.name="Staff Directory"
                                    contactUsModelsArrayList.add(contactUsModel)
                                }
                            }
                            mapFragment!!.getMapAsync { googleMap ->
                                mMap = googleMap
                                mMap!!.uiSettings.isMapToolbarEnabled = false
                                mMap!!.uiSettings.isZoomControlsEnabled = false
                                val latLng = LatLng(latitude!!.toDouble(), longitude!!.toDouble())
                                mMap!!.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(latitude!!.toDouble(), longitude!!.toDouble()))
                                        .title("NAS Manila")
                                )

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                                mMap.animateCamera(CameraUpdateFactory.zoomTo(13f))
                            /*    if (!isGPSEnabled!!) {
                                    val callGPSSettingIntent = Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                    )
                                    startActivity(callGPSSettingIntent)
                                } else {
                                    val url =
                                        "http://maps.google.com/maps?saddr=$c_latitude,$c_longitude&daddr=Nord Anglia International School Manila - Manila"

                                    val i = Intent(Intent.ACTION_VIEW)
                                    i.data = Uri.parse(url)
                                    startActivity(i)
                                }*/
                                mMap.setOnInfoWindowClickListener {

                                    if (!isGPSEnabled!!) {
                                        val callGPSSettingIntent = Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                        )
                                        startActivity(callGPSSettingIntent)
                                    } else {
                                        val url =
                                            "http://maps.google.com/maps?saddr=$c_latitude,$c_longitude&daddr=Nord Anglia International School Manila - Manila"

                                        val i = Intent(Intent.ACTION_VIEW)
                                        i.data = Uri.parse(url)
                                        startActivity(i)
                                    }
                                }
                                /*mMap.setOnInfoWindowClickListener {
                                    if (AppUtils.isNetworkConnected(mContext)) {
                                        if (!isGPSEnabled) {
                                            val callGPSSettingIntent = Intent(
                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                            )
                                            startActivity(callGPSSettingIntent)
                                        } else {
                                            val intent =
                                                Intent(mContext, LoadUrlWebViewActivity::class.java)
                                            intent.putExtra(
                                                "url",
                                                "http://maps.google.com/maps?saddr=$c_latitude,$c_longitude&daddr=Nord Anglia International School Manila - Manila"
                                            )
                                            intent.putExtra("tab_type", "Contact Us")
                                            startActivity(intent)
                                        }
                                        //startActivity(intent);
                                    } else {
                                        AppUtils.showDialogAlertDismiss(
                                            mContext as Activity,
                                            "Network Error",
                                            getString(R.string.no_internet),
                                            R.drawable.nonetworkicon,
                                            R.drawable.roundred
                                        )
                                    }
                                }*/

                         /*       mMap.setOnInfoWindowClickListener {
                                    if (!isGPSEnabled!!) {
                                        val callGPSSettingIntent = Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                        )
                                        startActivity(callGPSSettingIntent)
                                    } else {
                                        //val url = "http://maps.google.com/maps?saddr=$c_latitude,$c_longitude&daddr=The British International School,Abudhabi"
                                        val url = "http://maps.google.com/maps?saddr=" + c_latitude + "," + c_longitude + "&daddr=Nord Anglia International School Manila - Manila"

                                        val i = Intent(Intent.ACTION_VIEW)
                                        i.data = Uri.parse(url)
                                        startActivity(i)
                                    }


                                }*/
                            }


                            val contactUsAdapter =
                                ContactUsAdapter(mContext, contactUsModelsArrayList)
                            contactList!!.adapter = contactUsAdapter
                        }

                    } else if (status_code.equals("301")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext,
                            getString(R.string.error_heading),
                            getString(R.string.missing_parameter),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("304")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.email_exists),
                            R.drawable.infoicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("305")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.incrct_usernamepswd),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (status_code.equals("306")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.invalid_email),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            getString(R.string.error_heading),
                            getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    }
                } else {
                    progressBarDialog!!.dismiss()
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        getString(R.string.error_heading),
                        getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<ContactUsResponseModel>, t: Throwable) {

            }
        })

    }

    private fun getlatlong() {
        var location: Location
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGPSEnabled!! && !isNetworkEnabled!!) {

        } else {
            if (isNetworkEnabled as Boolean) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0L,
                        0.0F,
                        this
                    )

                    location =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                    if (location != null) {
                        lat = location.latitude
                        lng = location.longitude
                    }
                }
            }
            if (isGPSEnabled as Boolean) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0.0F,
                    this
                )
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
                if (location != null) {
                    lat = location.latitude
                    lng = location.longitude
                    println("lat---$lat")
                    println("lat---$lng")
                }
            }
        }
        c_latitude = lat.toString()
        c_longitude = lng.toString()
    }

    private fun initialiseUI() {
        mTitleTextView = requireView().findViewById(R.id.titleTextView)
        mTitleTextView!!.text=NaisClassNameConstants.CONTACT_US
        contactList = requireView().findViewById(R.id.mnewsLetterListView)
        desc = requireView().findViewById(R.id.description)
        mapFragment = childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        contactUsModelsArrayList= ArrayList()
        contactList!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        contactList!!.layoutManager = llm
        val spacing = 10 // 50px

        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        contactList!!.addItemDecoration(itemDecoration)
        contactList!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))
contactList!!.addOnItemClickListener(object :OnItemClickListener{
    override fun onItemClicked(position: Int, view: View) {
        if (position == contactUsModelsArrayList.size - 1) {
            val mIntent = Intent(activity, StaffDirectoryActivity::class.java)
            mContext.startActivity(mIntent)
        }
    }

})
        //relMain = mRootView.findViewById<View>(R.id.relMain) as RelativeLayout
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onMarkerDrag(p0: Marker) {

    }

    override fun onMarkerDragEnd(p0: Marker) {

    }

    override fun onMarkerDragStart(p0: Marker) {

    }

    override fun onMapLongClick(p0: LatLng) {

    }


}