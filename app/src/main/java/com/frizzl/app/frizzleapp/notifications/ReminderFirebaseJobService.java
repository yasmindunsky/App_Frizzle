//package com.frizzl.app.frizzleapp.notifications;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.firebase.jobdispatcher.JobParameters;
//import com.firebase.jobdispatcher.JobService;
//
///**
// * Created by Noga on 18/06/2018.
// */
//
//public class ReminderFirebaseJobService extends JobService {
//    private AsyncTask mBackgroundTask;
//
//
//    @Override
//    public boolean onStartJob(final JobParameters jobParameters) {
//        mBackgroundTask = new AsyncTask() {
//
//            @Override
//            protected Object doInBackground(Object[] params) {
//                Context context = ReminderFirebaseJobService.this;
//                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CHARGING_REMINDER);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                /*
//                 * Once the AsyncTask is finished, the job is finished. To inform JobManager that
//                 * you're done, you call jobFinished with the jobParamters that were passed to your
//                 * job and a boolean representing whether the job needs to be rescheduled. This is
//                 * usually if something didn't work and you want the job to try running again.
//                 */
//
//                jobFinished(jobParameters, false);
//            }
//        };
//
//        // COMPLETED (9) Execute the AsyncTask
//        mBackgroundTask.execute();
//        // COMPLETED (10) Return true
//        return true;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters jobParameters) {
//        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
//        return true;
//    }
//}
