package com.example.to_do.ui.reminder
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.to_do.R
import com.example.to_do.databinding.FragmentAddreminderBinding
import com.example.to_do.databinding.ReminderDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReminderFragment : Fragment() {

    private var _binding: FragmentAddreminderBinding? = null
    private val db=Firebase.firestore
    private var eventT=HashMap<Any,Any>()
    private var timeDelayInSeconds:Long = 0
    private var eventR=HashMap<Any,Any>()
    private var color= hashMapOf(
        "To Do" to "#FFFF00",
        "Alert" to "#FF5C5C",
        "Reminder" to "#00A2F3",
        "Must Do" to "#928000"
    )
    private val binding get() = _binding!!
    private lateinit var pickedDateTime : Calendar

    private lateinit var pickedTimeText : String

    private lateinit var pickedDateText : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ):
            View {

        db.collection("Color").document("Event Type").set(color)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        _binding = FragmentAddreminderBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.addReminder.setOnClickListener(View.OnClickListener { addReminder() })

        binding.fabReminder.setOnClickListener{ viewHome ->
            Snackbar.make(viewHome, "Do not forget to set a reminder to keep up with your task!", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null)
                .setAnchorView(R.id.fabReminder).show()
            Navigation.findNavController(viewHome).navigate(R.id.nav_home)

        }
        return binding.root
    }


    private  fun addReminder() {
        if (Build.VERSION.SDK_INT >= 33 && !NotificationManagerCompat.from(requireContext())
                .areNotificationsEnabled()
        ) {
            showNotificationPermissionDialog()
        } else {
            addReminderDialog()

        }
    }

     private fun showNotificationPermissionDialog() {
        MaterialAlertDialogBuilder(requireContext(),com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle(("Notification Permission"))
            .setMessage("Notification Permissions are required to set reminders!")
            .setPositiveButton("Ok")
            {
                _, _ ->
                //notification Launcher
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU)
                {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }

            }
            .setNegativeButton("Cancel", null).show()

    }
     private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
{
isGranted->
if(!isGranted)
{
    if(Build.VERSION.SDK_INT>=33)
    {
        if(shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS))
        {
            showNotificationPermissionDialog()
        }
        else
        {
            showSettingsDialog()
        }
    }
}
else
{
    Toast.makeText(requireContext(),"Notification permission granted",Toast.LENGTH_SHORT)
}
}

    private fun showSettingsDialog() {

MaterialAlertDialogBuilder(requireContext(),com.google.android.material.R.style.MaterialAlertDialog_Material3)
    .setTitle(("Notification Permission"))
    .setMessage("Notification Permissions are required to set reminders!")
    .setPositiveButton("Ok")
    {
            _, _ ->
        //notification Launcher
       val intent  = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${this.requireContext().packageName}")
        startActivity(intent)

    }
    .setNegativeButton("Cancel", null).show()

}

  @SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
    private fun addReminderDialog() {
      val dialogBinding =
          ReminderDialogBinding.bind(layoutInflater.inflate(R.layout.reminder_dialog, null))


      val dialog = AlertDialog.Builder(requireContext())
          .setView(dialogBinding.root)
          .show()
      dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
      dialogBinding.reminderType.adapter = ArrayAdapter<String>(
          requireContext(),
          android.R.layout.simple_spinner_dropdown_item,
          resources.getStringArray(R.array.ReminderTypes)
      )


      dialogBinding.reminderType.setPopupBackgroundDrawable(resources.getDrawable(R.drawable.bottom_navigation_bg))



      pickedDateTime = Calendar.getInstance()

      dialogBinding.select.setOnClickListener {

          val year = pickedDateTime.get(Calendar.YEAR)
          val month = pickedDateTime.get(Calendar.MONTH)
          val day = pickedDateTime.get(Calendar.DAY_OF_MONTH)
          val hour = pickedDateTime.get(Calendar.HOUR_OF_DAY)
          val minute = pickedDateTime.get(Calendar.MINUTE)
          // val year = pickedDateTime.get(Calendar.YEAR)

          DatePickerDialog(requireContext(),
              R.style.CustomDatePickerDialogTheme,

              DatePickerDialog.OnDateSetListener
              {

                      _, _, _, dayOfMonth ->
                  // show Time Picker
                  TimePickerDialog(requireContext(), R.style.CustomTimePickerDialogTheme,

                      TimePickerDialog.OnTimeSetListener
                      { _, hourOfDay, minute ->
                          pickedDateTime.set(year, month, dayOfMonth, hourOfDay, minute)

                          Log.d("Date and Time", "Picked date and time $pickedDateTime")
                          dialogBinding.dateTime.text =
                              getCurrentDateAndTime(pickedDateTime.timeInMillis)
                          pickedDateText =
                              SimpleDateFormat("EEE, MMM d, ''yy", Locale.getDefault()).format(
                                  Date(pickedDateTime.timeInMillis)
                              )
                          pickedTimeText = SimpleDateFormat("h:mm a", Locale.getDefault()).format(
                              Date(pickedDateTime.timeInMillis)
                          )
                      }, hour, minute, false
                  ).show()
              }, year, month, day).show()


      }



      dialogBinding.submit.setOnClickListener {


          if (dialogBinding.editTitle.text.isNullOrEmpty()) {

              dialogBinding.editTitle.requestFocus()
              dialogBinding.editTitle.setError("Please provide title!")

          }


          if (dialogBinding.dateTime.text == resources.getString(R.string.date_and_time)) {
              dialogBinding.dateTime.setError("Please select date and time!")
          } else {
              //add reminder
              timeDelayInSeconds =
                  (pickedDateTime.timeInMillis / 1000L) - (Calendar.getInstance().timeInMillis / 1000L)
              if (timeDelayInSeconds < 0) {
                  Toast.makeText(
                      requireContext(),
                      "Cannot set remainders for the past time",
                      Toast.LENGTH_SHORT
                  ).show()
                  return@setOnClickListener
              }
              Toast.makeText(requireContext(), "Remainder Added", Toast.LENGTH_SHORT)
                  .show()
              dialog.dismiss()

              createWorkRequest(
                  dialogBinding.editTitle.text.toString(),
                  resources.getStringArray(R.array.ReminderTypes)[dialogBinding.reminderType.selectedItemPosition],
                  timeDelayInSeconds
              )

              //Adding to cloud database  ---

              val nestedReminderEvent = hashMapOf(
                  "Title" to dialogBinding.editTitle.text.toString(),
                  "Reminder Type" to resources.getStringArray(R.array.ReminderTypes)[dialogBinding.reminderType.selectedItemPosition].toString(),
                  "Time" to pickedTimeText,
                  "Note" to dialogBinding.editNote.text.toString()
              )
              eventR = hashMapOf(
                  pickedDateText to nestedReminderEvent
              )
              db.collection("event").document("Reminder event")
                  .set(eventT, SetOptions.merge())
                  .addOnSuccessListener {
                      Log.d(
                          TAG,
                          "DocumentSnapshot successfully written!"
                      )
                  }
                  .addOnFailureListener { e ->
                      Log.w(TAG, "Error writing document", e)
                  }


              // ---
          }
      }
  }




    private fun createWorkRequest(title : String, remainderType : String, delay : Long)
    {
        val remainderWorkRequest =  OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay,TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "Title" to "Todo: $remainderType",
                    "Message" to title
                )
            ).build()

        WorkManager.getInstance(requireContext()).enqueue(remainderWorkRequest)
    }

    private fun getCurrentDateAndTime(millis: Long):
    String{
        return SimpleDateFormat("dd--MM--yyyy hh:mm a", Locale.getDefault()).format(Date(millis))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        }

}



        /**
        val nestedTodoEvent = hashMapOf(

        "Title" to dialogBinding.editTitle.text.toString(),
        "Reminder Type" to resources.getStringArray(R.array.ReminderTypes)[dialogBinding.reminderType.selectedItemPosition].toString(),
        "Note" to dialogBinding.editNote.text.toString()
        )
        eventT = hashMapOf(
        dialogBinding.editTitle.text.toString()  to nestedTodoEvent
        )

        db.collection("event").document("To-Do event")
        .set(eventT, SetOptions.merge())
        .addOnSuccessListener {
        Log.d(
        TAG,
        "DocumentSnapshot successfully written!"
        )
        }
        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
**/