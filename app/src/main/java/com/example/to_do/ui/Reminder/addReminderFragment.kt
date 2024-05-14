package com.example.to_do.ui.Reminder
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Instrumentation.ActivityResult
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.DialogInterface
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
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.to_do.R
import com.example.to_do.databinding.FragmentAddreminderBinding
import com.example.to_do.databinding.ReminderDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class addReminderFragment : Fragment() {

    private var _binding: FragmentAddreminderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ):
            View {
        _binding = FragmentAddreminderBinding.inflate(inflater, container, false)
        val root: View = binding.root

      //  val textView: TextView = binding.textGallery
      //  addReminderViewModel.text.observe(viewLifecycleOwner) {
       //     textView.text = it
       // }

        binding.addReminder.setOnClickListener(View.OnClickListener { addReminder() })
        return root

    }

        private fun addReminder() {
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

          private fun addReminderDialog()
          {
              val dialogBinding = ReminderDialogBinding.bind(layoutInflater.inflate(R.layout.reminder_dialog,null))
              val dialog = AlertDialog.Builder(requireContext())
                  .setView(dialogBinding.root)
                  .show()
              dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
              dialogBinding.reminderType.adapter = ArrayAdapter<String>(requireContext(),
                                                      android.R.layout.simple_spinner_dropdown_item,
                                                      resources.getStringArray(R.array.ReminderTypes))
              val pickedDate  = Calendar.getInstance()

              dialogBinding.select.setOnClickListener {

                  val year = pickedDate.get(Calendar.YEAR)
                  val month = pickedDate.get(Calendar.MONTH)
                  val day= pickedDate.get(Calendar.DAY_OF_MONTH)
                  val hour = pickedDate.get(Calendar.HOUR_OF_DAY)
                  val minute = pickedDate.get(Calendar.MINUTE)
                 // val year = pickedDate.get(Calendar.YEAR)
                  DatePickerDialog(requireContext(),
                      DatePickerDialog.OnDateSetListener
                      {
                          view, year, month, dayOfMonth ->
                          // show Time Picker
                      TimePickerDialog(requireContext(),
                          TimePickerDialog.OnTimeSetListener
                          {
                              view, hourOfDay, minute ->
                              pickedDate.set(year,month,dayOfMonth,hourOfDay,minute)
                              Log.d("Date and Time","Picked date and time $pickedDate")
                              dialogBinding.dateTime.text = getCurrentDateAndTime(pickedDate.timeInMillis)
                          }, hour, minute, false).show()
                      }
                  ,year,month,day).show()

              }
              dialogBinding.submit.setOnClickListener {
                  if(dialogBinding.editTitle.text.isNullOrEmpty())
                  {

                      dialogBinding.editTitle.requestFocus()
                      dialogBinding.editTitle.setError("Please provide title!")

                  }
                  else if( dialogBinding.dateTime.text == resources.getString(R.string.date_and_time))
                      {
                      dialogBinding.dateTime.setError("Please select date and time!")
                     }
                  else
                   {
                      //add reminder
                        val timeDelayInSeconds = (pickedDate.timeInMillis/ 1000L) - (Calendar.getInstance().timeInMillis/1000L)
                       if(timeDelayInSeconds < 0)
                       {
                           Toast.makeText(requireContext(),"Cannot set remainders for the past time",Toast.LENGTH_SHORT).show()
                           return@setOnClickListener
                       }

                       createWorkRequest(dialogBinding.editTitle.text.toString(),
                           resources.getStringArray(R.array.ReminderTypes)[dialogBinding.reminderType.selectedItemPosition],
                           timeDelayInSeconds)
                       Toast.makeText(requireContext(),"Remainder Added",Toast.LENGTH_SHORT).show()
                       dialog.dismiss()

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
