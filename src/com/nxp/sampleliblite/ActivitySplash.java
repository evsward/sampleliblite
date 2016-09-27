/*
 * =============================================================================
 *
 *                       Copyright (c), NXP Semiconductors
 *
 *                        (C)NXP Electronics N.V.2013
 *         All rights are reserved. Reproduction in whole or in part is
 *        prohibited without the written consent of the copyright owner.
 *    NXP reserves the right to make changes without notice at any time.
 *   NXP makes no warranty, expressed, implied or statutory, including but
 *   not limited to any implied warranty of merchantability or fitness for any
 *  particular purpose, or that the use will not infringe any third party patent,
 *   copyright or trademark. NXP must not be liable for any loss or damage
 *                            arising from its use.
 *
 * =============================================================================
 */
package com.nxp.sampleliblite;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

/**
 * Nfc lite API is supported. */
public class ActivitySplash extends Activity {
	/** Create CountDowntimer instance. */
	private CountDownTimer timer;
	/**Intent instance. */
	Intent intent = null;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		timer = new CountDownTimer(2000, 1000) {
			@Override
			public void onFinish() {
					intent = new Intent(ActivitySplash.this, MainLiteActivity.class);
					startActivity(intent);
				    finish();
			}

			@Override
			public void onTick(final long millisUntilFinished) {

			}
		} .start();
	}

	@Override
	public void onBackPressed() {
		timer.cancel();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		timer.cancel();
		super.onStop();
	}
}
