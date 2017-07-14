package com.jarrebnnee.connect;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "", mailTo = "jhgoswami086@gmail.com", mode = ReportingInteractionMode.TOAST, resToastText = (R.string.acra_toast))
public class CrashApplicationClass extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ACRA.init(this);
		super.onCreate();
	}

}
