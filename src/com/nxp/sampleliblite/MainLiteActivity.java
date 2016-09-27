/**
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

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxp.nfclib.classic.IMFClassic;
import com.nxp.nfclib.classic.IMFClassicEV1;
import com.nxp.nfclib.exceptions.CloneDetectedException;
import com.nxp.nfclib.exceptions.PlusException;
import com.nxp.nfclib.exceptions.ReaderException;
import com.nxp.nfclib.exceptions.SAMException;
import com.nxp.nfclib.exceptions.SmartCardException;
import com.nxp.nfclib.icode.ICodeSLI;
import com.nxp.nfclib.icode.IICodeSLI;
import com.nxp.nfclib.icode.IICodeSLIL;
import com.nxp.nfclib.icode.IICodeSLIS;
import com.nxp.nfclib.icode.IICodeSLIX;
import com.nxp.nfclib.icode.IICodeSLIX2;
import com.nxp.nfclib.icode.IICodeSLIXL;
import com.nxp.nfclib.icode.IICodeSLIXS;
import com.nxp.nfclib.keystore.common.IKeyConstants;
import com.nxp.nfclib.keystore.common.IKeyStore;
import com.nxp.nfclib.keystore.common.KeyStoreFactory;
import com.nxp.nfclib.ndef.NdefMessageWrapper;
import com.nxp.nfclib.ndef.FormatException;
import android.nfc.NdefRecord;

import com.nxp.nfclib.ndef.NdefRecordWrapper;
import com.nxp.nfclib.ntag.INTag;
import com.nxp.nfclib.ntag.INTag203x;
import com.nxp.nfclib.ntag.INTag210;
import com.nxp.nfclib.ntag.INTag213215216;
import com.nxp.nfclib.ntag.INTag213F216F;
import com.nxp.nfclib.ntag.INTagI2C;
import com.nxp.nfclib.ntag.INTAGI2Cplus;
import com.nxp.nfclib.plus.IPlusSL1;
import com.nxp.nfclib.ultralight.IUltralight;
import com.nxp.nfclib.ultralight.IUltralightC;
import com.nxp.nfclib.ultralight.IUltralightEV1;
import com.nxp.nfclib.utils.NxpLogUtils;
import com.nxp.nfclib.utils.Utilities;

import com.nxp.nfcliblite.NxpNfcLibLite;
import com.nxp.nfcliblite.Nxpnfcliblitecallback;
import com.nxp.nfcliblite.cards.IDESFireEV1;
import com.nxp.nfcliblite.cards.IPlus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Nfc lite API is supported.
 */
public class MainLiteActivity extends Activity {

	/** Lite application Tag. */
	static final String TAG = "SampleNxpNfcLibLite";
	/** Create lib lite instance. */
	private NxpNfcLibLite libInstance = null;
	/** Mifare DESFire instance initiated. */
	private IDESFireEV1 mDESFire;

	/** Mifare MFClassic instance initiated. */
	private IMFClassic classic;

	/** Mifare Ultralight instance initiated. */
	private IUltralight mifareUL;
	/** Mifare Ultralight instance initiated. */
	private IUltralightC objUlCardC;
	/** Mifare Ultralight EV1 instance initiated. */
	private IUltralightEV1 objUlCardEV1;
	/** Mifare Plus instance initiated. */
	private IPlus plus;
	/**
	 * Constant for permission
	 */
	private static final int STORAGE_PERMISSION_READ = 113;
	/** Mifare Plus SL1 instance initiated. */
	private IPlusSL1 plusSL1;

	/** ICode SLI instance initiated. */
	private IICodeSLI iCodeSli;
	/** ICode SLI-L instance initiated. */
	private IICodeSLIL iCodeSliL;
	/** ICode SLI-S instance initiated. */
	private IICodeSLIS iCodeSliS;
	/** ICode SLI-X instance initiated. */
	private IICodeSLIX iCodeSliX;
	/** ICode SLI-XL instance initiated. */
	private IICodeSLIXL iCodeSliXL;
	/** ICode SLI-XS instance initiated. */
	private IICodeSLIXS iCodeSliXS;
	/** ICode SLIX2 instance initiated. */
	private IICodeSLIX2 iCodeSliX2;

	/** Create imageView instance. */
	private ImageView mImageView = null;
	// private static Handler mHandler;
	/** Create Textview instance initiated. */
	private TextView tv = null;
	/** Checkbox for write select */
	private CheckBox mCheckToWrite = null;

	/**
	 * Ultralight First User Memory Page Number.
	 */
	private static final int DEFAULT_PAGENO_ULTRALIGHT = 4;
	/**
	 * Variable DATA Contain a String.
	 */
	private static final String DATA = "This is the data";

	/**
	 * KEY_APP_MASTER key used for encrypt data.
	 */
	private static final String KEY_APP_MASTER = "This is my key  ";
	/** */
	private byte[] bytesKey = null;
	/** */
	private Cipher cipher = null;
	/** */
	private IvParameterSpec iv = null;

	private boolean bWriteAllowed = false;

	private Tag tag;

	private enum EnumCardType {
		EnumUltraLight, EnumClassic, EnumUltraLightC, EnumUltraLightEV1, EnumPlus, EnumPlusSL1, EnumNTag203x, EnumNTag210, EnumNTag213215216, EnumNTag213F216F, EnumNTagI2C, EnumNTagI2CPLUS, EnumICodeSLI, EnumICodeSLIS, EnumICodeSLIL, EnumICodeSLIX, EnumICodeSLIXS, EnumICodeSLIXL, EnumICodeSLIX2, EnumDESFireEV1, EnumPlusX, EnumPlusS, EnumClassicEV1, EnumNone

	}

	private EnumCardType mCardType = EnumCardType.EnumNone;

	private boolean mIsPerformingCardOperations = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_lite);

		/* Get image view handle to be used further */
		mImageView = (ImageView) findViewById(R.id.cardSnap);

		mCheckToWrite = (CheckBox) findViewById(R.id.checkToWrite);

		mCheckToWrite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!bWriteAllowed) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainLiteActivity.this);
					alertDialog.setCancelable(false);

					// Setting Dialog Title
					alertDialog.setTitle(R.string.write_ops_title);

					// Setting Dialog Message
					alertDialog.setMessage(getString(R.string.write_ops_message));

					// Setting Icon to Dialog
					alertDialog.setIcon(R.drawable.mifaresdk);

					// Setting Positive "Yes" Button
					alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, final int which) {

							bWriteAllowed = true;

						}

					});

					// Setting Negative "NO" Button
					alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, final int which) {
							bWriteAllowed = false;
							// Write your code here to invoke NO event
							mCheckToWrite.setChecked(false);
							dialog.cancel();
						}
					});

					// Showing Alert Message
					alertDialog.show();

				} else {
					bWriteAllowed = false;
				}
			}
		});

		/* Get text view handle to be used furhter */
		tv = (TextView) findViewById(R.id.tvLog);
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setText(R.string.info_string);

		libInstance = NxpNfcLibLite.getInstance();
		libInstance.registerActivity(this);

		// Registering the activity for the NFC tag detection mode.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// Registering the activity for the NFC tag detection mode.

			final Handler mLibhandler = new Handler(mLibhandlercb);

			try {
				libInstance.setNfcControllerMode(1000, new NfcAdapter.ReaderCallback() {

					@Override
					public void onTagDiscovered(final Tag tagObject) {
						NxpLogUtils.d(TAG, "TAG is Discovered from ReaderCallBack...");
						tag = tagObject;
						mLibhandler.sendEmptyMessage(0);

					}
				}, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_V | NfcAdapter.FLAG_READER_NFC_B
						| NfcAdapter.FLAG_READER_NFC_F);
			} catch (SmartCardException e) {

			}
		}

		boolean readPermission = (ContextCompat.checkSelfPermission(MainLiteActivity.this,
				Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

		if (!readPermission) {
			ActivityCompat.requestPermissions(MainLiteActivity.this,
					new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, STORAGE_PERMISSION_READ);
		}
		/* Initialize the Cipher and init vector of 16 bytes with 0xCD.. */
		initializeCipherinitVector();

		/* Intialize UI Handler. */
		initializeUIhandler();

		/* Help and About Screen. */
		readHelpAbout();

		/* Disclaimer screen. */
		showDisclaimer();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * Initialize the Cipher and init vector of 16 bytes with 0xCD.
	 */

	private void initializeCipherinitVector() {

		/* Initialize the Cipher */
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		}

		/* set Application Master Key */
		bytesKey = KEY_APP_MASTER.getBytes();

		/* Initialize init vector of 16 bytes with 0xCD. It could be anything */
		byte[] ivSpec = new byte[16];
		Arrays.fill(ivSpec, (byte) 0xCD);
		iv = new IvParameterSpec(ivSpec);

	}

	/**
	 * Disclaimer Section contain Details About product.
	 */
	private void showDisclaimer() {

		AlertDialog.Builder alert = new AlertDialog.Builder(MainLiteActivity.this);
		alert.setTitle("About");
		String[] cards = libInstance.getSupportedCards();
		NxpLogUtils.i(TAG, "Supported Cards" + Arrays.toString(cards));
		String message = getString(R.string.about_text);

		String alertMessage = message + "\n";

		alertMessage += "\n";
		String appVer = getApplicationVersion();
		if (appVer != null)
			alertMessage += "Application version is: " + appVer + "\n";

		alertMessage += "SDK Library Version : " + NxpNfcLibLite.getLibraryVersion() + "\n\n" + "Supported Cards: "
				+ Arrays.toString(cards) + "\n";

		alert.setMessage(alertMessage);
		alert.setIcon(R.drawable.mifaresdk);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int whichButton) {

			}
		});

		alert.show();
	}

	private String getApplicationVersion() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read Me section contain Help and About product.
	 */

	private void readHelpAbout() { // TODO Auto-generated method stub
		((TextView) findViewById(R.id.textPreferences)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				showDisclaimer();
			}
		});
	}

	/**
	 * Initializing the UI thread.
	 */
	private void initializeUIhandler() {

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mImageView.getLayoutParams().width = (size.x * 2) / 3;
		mImageView.getLayoutParams().height = size.y / 3;

		/*
		 * mHandler = new Handler() {
		 * 
		 * @Override public void handleMessage(Message msg) { Log.i(TAG,
		 * "inside handlemessge, msg.what is " + msg.what); String
		 * messageResponse = msg.getData().getString("message"); char where =
		 * msg.getData().getChar("where"); if (messageResponse != null) {
		 * showMessage(messageResponse, where); } } };
		 */

		mImageView.setImageResource(R.drawable.mifare_p);
	}

	@Override
	protected void onPause() {
		libInstance.stopForeGroundDispatch();
		super.onPause();
	}

	@Override
	protected void onResume() {
		libInstance.startForeGroundDispatch();
		super.onResume();
	}

	@Override
	protected void onNewIntent(final Intent intent) {

		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] duration = { 50, 100, 200, 300 };
		vib.vibrate(duration, -1);

		// MifareUltralight.get(tag)
		try {
			tv.setText(" ");
			libInstance.filterIntent(intent, mCallback);
		} catch (CloneDetectedException e) {
			showMessage("Clone Detected", 't');
		}
	}

	/**
	 * Callback Class which contains methods that are called when Cards are
	 * Detected.
	 */
	private Nxpnfcliblitecallback mCallback = new Nxpnfcliblitecallback() {

		@Override
		public void onNewTagDetected(Tag tag) {
			Log.d(TAG, "-------------- onNewTagDetected ------");
		}

		@Override
		public void onUltraLightCardDetected(final IUltralight objUlCard) {

			if (mCardType == EnumCardType.EnumUltraLight && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumUltraLight;
			mifareUL = objUlCard;
			/* Insert your logic here by commenting the function call below. */
			try {
				mifareUL.getReader().connect();
				ultralightCardLogic();
			} catch (Throwable t) {
				t.printStackTrace();
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onUltraLightCCardDetected(final IUltralightC ulC) {

			if (mCardType == EnumCardType.EnumUltraLightC && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumUltraLightC;
			objUlCardC = ulC;
			/*
			 * Insert your logic here by commenting the function call below
			 */
			try {
				objUlCardC.getReader().connect();
				ultralightcCardLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onUltraLightEV1CardDetected(final IUltralightEV1 ulEV1) {

			if (mCardType == EnumCardType.EnumUltraLightEV1 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumUltraLightEV1;

			objUlCardEV1 = ulEV1;
			/*
			 * Insert your logic here by commenting the function call below
			 */
			try {
				objUlCardEV1.getReader().connect();
				ultralightEV1CardLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onClassicCardDetected(final IMFClassic objMFCCard) {
			Log.e(TAG, "--------------- onClassicCardDetected --------------");
			if (mCardType == EnumCardType.EnumClassic && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumClassic;

			classic = objMFCCard;
			/* Insert your logic here by commenting the function call below. */
			try {
				classic.getReader().connect();
				classicCardLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onClassicEV1CardDetected(final IMFClassicEV1 objMFCEV1Card) {

			Log.e(TAG, "--------------- onClassicEV1CardDetected --------------");
			if (mCardType == EnumCardType.EnumClassicEV1 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumClassicEV1;

			classic = objMFCEV1Card;
			/* Insert your logic here by commenting the function call below. */
			try {
				classic.getReader().connect();
				classicEV1CardLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onDESFireCardDetected(final IDESFireEV1 objDESFire) {

			if (mCardType == EnumCardType.EnumDESFireEV1 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumDESFireEV1;

			mDESFire = objDESFire;
			/* Insert your logic here by commenting the function call below. */
			try {
				mDESFire.getReader().close();
				mDESFire.getReader().connect();
				desfireCardLogic();
			} catch (Throwable t) {
				t.printStackTrace();
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onPlusCardDetected(final IPlus objMFPlus) {

			if (mCardType == EnumCardType.EnumPlus && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumPlus;

			plus = objMFPlus;
			try {
				plus.getReader().connect();
				plusCardLogic();
			} catch (Throwable t) {
				t.printStackTrace();
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onPlusSL1CardDetected(IPlusSL1 objPlusSL1) {
			if (mCardType == EnumCardType.EnumPlusSL1 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumPlusSL1;

			plusSL1 = objPlusSL1;
			classic = objPlusSL1; // Plus SL1 is completely compatible with
			// Classic!!
			try {
				plusSL1.getReader().connect();
				PlusSL1CardLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLIDetected(final IICodeSLI objiCodesli) {

			if (mCardType == EnumCardType.EnumICodeSLI && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLI;

			iCodeSli = objiCodesli;

			try {
				iCodeSli.getReader().connect();
				iCodeSLILogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLILDetected(final IICodeSLIL objiCodeslil) {
			if (mCardType == EnumCardType.EnumICodeSLIL && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLIL;

			iCodeSliL = objiCodeslil;

			try {
				iCodeSliL.getReader().connect();
				iCodeSLIlLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLISDetected(final IICodeSLIS objiCodeslis) {

			if (mCardType == EnumCardType.EnumICodeSLIS && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLIS;

			iCodeSliS = objiCodeslis;

			try {
				iCodeSliS.getReader().connect();
				iCodeSLIsLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLIXDetected(final IICodeSLIX objiCodeslix) {
			if (mCardType == EnumCardType.EnumICodeSLIX && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLIX;

			iCodeSliX = objiCodeslix;

			try {
				iCodeSliX.getReader().connect();
				iCodeSLIxLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLIXLDetected(final IICodeSLIXL objiCodeslixl) {
			if (mCardType == EnumCardType.EnumICodeSLIXL && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLIXL;

			iCodeSliXL = objiCodeslixl;

			try {
				iCodeSliXL.getReader().connect();
				iCodeSLIxlLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLIXSDetected(final IICodeSLIXS objiCodeslixs) {
			if (mCardType == EnumCardType.EnumICodeSLIXS && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLIXS;

			iCodeSliXS = objiCodeslixs;

			try {
				iCodeSliXS.getReader().connect();
				iCodeSLIxsLogic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onICodeSLIX2Detected(final IICodeSLIX2 objiCodeslix2) {
			if (mCardType == EnumCardType.EnumICodeSLIX2 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumICodeSLIX2;

			iCodeSliX2 = objiCodeslix2;

			try {
				iCodeSliX2.getReader().connect();
				iCodeSLIx2Logic();
			} catch (Throwable t) {
				showMessage("Unknown Error Tap Again!", 't');
			}

			mIsPerformingCardOperations = false;

		}

		@Override
		public void onNTag203xCardDetected(final INTag203x objnTag203x) {

			if (mCardType == EnumCardType.EnumNTag203x && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumNTag203x;

			try {
				objnTag203x.getReader().connect();
				ntagCardLogic(objnTag203x);
			} catch (ReaderException e) {

				e.printStackTrace();
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onNTag210CardDetected(final INTag210 objnTag210) {

			if (mCardType == EnumCardType.EnumNTag210 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumNTag210;

			try {
				objnTag210.getReader().connect();
				ntagCardLogic(objnTag210);
			} catch (ReaderException e) {

				e.printStackTrace();
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onNTag213215216CardDetected(final INTag213215216 objnTag213215216) {

			if (mCardType == EnumCardType.EnumNTag213215216 && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}
			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumNTag213215216;

			try {
				objnTag213215216.getReader().connect();
				ntagCardLogic(objnTag213215216);
			} catch (ReaderException e) {

				e.printStackTrace();
			}

			mIsPerformingCardOperations = false;

		}

		@Override
		public void onNTag213F216FCardDetected(final INTag213F216F objnTag213216f) {

			if (mCardType == EnumCardType.EnumNTag213F216F && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = true;
			mCardType = EnumCardType.EnumNTag213F216F;

			try {
				objnTag213216f.getReader().connect();
				ntagCardLogic(objnTag213216f);
			} catch (ReaderException e) {

				e.printStackTrace();
			}

			mIsPerformingCardOperations = false;

		}

		@Override
		public void onNTagI2CCardDetected(final INTagI2C objnTagI2c) {

			if (mCardType == EnumCardType.EnumNTagI2C && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = false;
			mCardType = EnumCardType.EnumNTagI2C;

			try {
				objnTagI2c.getReader().connect();
				ntagCardLogic(objnTagI2c);
			} catch (Exception e) {

				e.printStackTrace();
			}

			mIsPerformingCardOperations = false;
		}

		@Override
		public void onCardNotSupported(Tag tag) {
			tv.setText(" ");
			showImageSnap(R.drawable.mifare_p);
			showMessage("Card NOT supported", 'n');
		}

		@Override
		public void onNTagI2CplusCardDetected(final INTAGI2Cplus objnTagI2Cplus) {

			if (mCardType == EnumCardType.EnumNTagI2CPLUS && mIsPerformingCardOperations) {
				// Already Some Operations are happening in the same card,
				// discard the callback
				Log.d(TAG, "----- Already Some Operations are happening in the same card, discard the callback: "
						+ mCardType.toString());
				return;
			}

			mIsPerformingCardOperations = false;
			mCardType = EnumCardType.EnumNTagI2CPLUS;

			try {
				objnTagI2Cplus.getReader().connect();
				ntagCardLogic(objnTagI2Cplus);
			} catch (Exception e) {

				e.printStackTrace();
			}

			mIsPerformingCardOperations = false;
		}
	};

	/**
	 * iCode SLIXS Card Logic.
	 */
	protected void iCodeSLIxsLogic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSliXS.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSliXS.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSliXS.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSliXS.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			// showCardDetails(iCodeSliXS.getCardDetails());
			/* Close the connection. */
			iCodeSliXS.getReader().close();

		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}

	}

	protected void iCodeSLIxlLogic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSliXL.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSliXL.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSliXL.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSliXL.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			// showCardDetails(iCodeSliXL.getCardDetails());
			/* Close the connection. */
			iCodeSliXL.getReader().close();

		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}

	}

	/**
	 * iCode SLIX Card Logic.
	 */
	protected void iCodeSLIxLogic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSliX.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSliX.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSliX.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSliX.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			// showCardDetails(iCodeSliX.getCardDetails());
			/* Close the connection. */
			iCodeSliX.getReader().close();

		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}
	}

	/**
	 * iCode slis Card Logic.
	 */
	protected void iCodeSLIsLogic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSliS.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSliS.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSliS.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSliS.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			// showCardDetails(iCodeSliS.getCardDetails());
			/* Close the connection. */
			iCodeSliS.getReader().close();

		} catch (TagLostException e) {
			showMessage("TagLost Exception - Tap Again!", 't');
			e.printStackTrace();
		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}

	}

	/**
	 * icode sli card logic.
	 */
	protected void iCodeSLILogic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSli.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSli.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSli.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSli.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			// showCardDetails(iCodeSli.getCardDetails());
			/* Close the connection. */
			iCodeSli.getReader().close();

		} catch (TagLostException e) {
			showMessage("TagLost Exception - Tap Again!", 't');
			e.printStackTrace();
		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}
	}

	/**
	 * icode sli card logic.
	 */
	protected void iCodeSLIlLogic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSliL.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSliL.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSliL.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSliL.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');
			// showCardDetails(iCodeSliL.getCardDetails());
			/* Close the connection. */
			iCodeSliL.getReader().close();

		} catch (TagLostException e) {
			showMessage("TagLost Exception - Tap Again!", 't');
			e.printStackTrace();
		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}

	}

	/**
	 * icode slix2 card logic.
	 */
	protected void iCodeSLIx2Logic() {

		showImageSnap(R.drawable.icode_p);
		tv.setText(" ");
		/* Get the Icode label name. */
		showMessage("Card Detected : " + iCodeSliX2.getTagName(), 'n');

		try {
			/* Get the UID */
			byte[] uid = iCodeSliX2.getUID();
			/* Display message in text view */
			showMessage("uid: " + Utilities.dumpBytes(uid), 'd');
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				/* It should contain four bytes of data to be write. */
				byte[] writeData = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 };
				/* Display message in text view */
				showMessage("Write: " + Utilities.dumpBytes(writeData), 'd');
				/* Write the data in specified block. */
				iCodeSliX2.writeSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5, writeData);
			}
			/* Read the data in specified block. */
			byte[] read = iCodeSliX2.readSingleBlock(ICodeSLI.NFCV_FLAG_ADDRESS, (byte) 5);
			/* Display message in text view. */
			showMessage("Read: " + Utilities.dumpBytes(read), 'd');

			/* Close the connection. */
			iCodeSliX2.getReader().close();

		} catch (TagLostException e) {
			showMessage("TagLost Exception - Tap Again!", 't');
			e.printStackTrace();
		} catch (IOException e) {
			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}

	}

	/**
	 * Mifare DESFire Card Logic.
	 * 
	 * @throws SmartCardException
	 */
	protected void desfireCardLogic() throws SmartCardException {

		showImageSnap(R.drawable.desfire_ev1);
		tv.setText(" ");
		showMessage("Card Detected : " + mDESFire.getCardDetails().cardName, 'n');

		try {
			mDESFire.getReader().setTimeout(2000);
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				testDESFirepersonalize();
				testDESFireauthenticate();
				testDESFireupdatePICCMasterKey();
				testDESFireauthenticate();
				testDESFireupdateApplicationMasterKey();
				testDESFireauthenticate();
				testDESFireWrite();
				testDESFireRead();
			}
			mDESFire.getReader().setTimeout(2000);
			// showCardDetails(mDESFire.getCardDetails());
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				testDESFireFormat();
			}
			mDESFire.getReader().close();
		} catch (ReaderException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Ntag Operations are, getTagname(), getUID(), Write and Read.
	 * 
	 * @param tag
	 *            object
	 */
	private void ntagCardLogic(final INTag tag) {
		Log.d(TAG, "((((((((((((((((((((((("+tag.getClass().getName());
		showImageSnap(R.drawable.ntag_p);
		tv.setText(" ");
		showMessage("Card Detected : " + tag.getTagName(), 'd');

		try {
			NxpLogUtils.d(TAG, "testBasicNtagFunctionality, start");

			showMessage("UID of the Tag: " + Utilities.dumpBytes(tag.getUID()), 'd');

			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				for (int idx = tag.getFirstUserpage(); (idx < tag.getLastUserPage()) && idx <= 5; idx++) {
					byte[] data = new byte[] { (byte) idx, (byte) idx, (byte) idx, (byte) idx };
					tag.write(idx, data);
					showMessage("Written 4 Bytes Data at page No= " + idx + " " + Utilities.dumpBytes(data), 'd');
				}
			}
			for (int idx = tag.getFirstUserpage(); (idx < tag.getLastUserPage()) && idx <= 5; idx++) {
				showMessage("Read 16 Bytes of Data from page No= " + idx + " " + Utilities.dumpBytes(tag.read(idx)),
						'd');
			}
			// showCardDetails(tag.getCardDetails());
			tag.getReader().close();
			NxpLogUtils.d(TAG, "testBasicNtagFunctionality, End");
		} catch (TagLostException e) {
			showMessage("TagLost Exception - Tap Again!", 't');
			e.printStackTrace();
		} catch (IOException e) {

			showMessage("IO Exception -  Check logcat!", 't');
			e.printStackTrace();
		} catch (SmartCardException e) {
			showMessage("SmartCard Exception - Check logcat!", 't');
			e.printStackTrace();
		} catch (Throwable t) {
			showMessage("Exception - Check logcat!", 't');
			t.printStackTrace();
		}
	}

	/** DESFire read IO Operations. */
	private void testDESFireRead() {

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFireRead, start");
			byte[] data = mDESFire.read(5);
			res = true;
			showMessage("Data Read from the card..." + Utilities.dumpBytes(data), 'd');
		} catch (SmartCardException e) {
			showMessage("Data Read from the card: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFireRead, result is " + res);
		NxpLogUtils.d(TAG, "testDESFireRead, End");
	}

	/** DESFire Write IO Operations. */
	private void testDESFireWrite() {

		byte[] data = new byte[] { 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11 };

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFireWrite, start");
			mDESFire.write(data);
			res = true;
			showMessage("Data Written: " + Utilities.dumpBytes(data), 'd');
		} catch (SmartCardException e) {
			showMessage("Data Written: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFireWrite, result is " + res);
		NxpLogUtils.d(TAG, "testDESFireWrite, End");

	}

	/** DESFire Update Application master key IO Operations. */
	private void testDESFireupdateApplicationMasterKey() {
		byte[] oldKey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };
		byte[] newKey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };

		byte[] masterKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00 };

		byte[] appId = { 0x12, 0x12, 0x12 };
		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFireupdateApplicationMasterKey, start");
			mDESFire.updateApplicationMasterKey(masterKey, appId, oldKey, newKey);
			res = true;
			showMessage("Update Application MasterKey: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("Update Application MasterKey: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFireupdateApplicationMasterKey, result is " + res);
		NxpLogUtils.d(TAG, "testDESFireupdateApplicationMasterKey, End");
	}

	/** DESFire Authenticate IO Operations . */
	private void testDESFireauthenticate() {
		byte[] masterKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00 };
		byte[] appId = { 0x12, 0x12, 0x12 };
		byte[] appkey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFireauthenticate, start");
			mDESFire.authenticate(masterKey, appId, appkey);
			res = true;
			showMessage("Authenticate: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("Authenticate: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFireauthenticate, result is " + res);
		NxpLogUtils.d(TAG, "testDESFireauthenticate, End");
	}

	/** DESFire personalize Operations. */
	private void testDESFirepersonalize() {
		byte[] mykey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };
		byte[] appKey = new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFirepersonalize, start");

			mDESFire.personalize(mykey, new byte[] { 0x12, 0x12, 0x12 }, appKey);
			res = true;
			showMessage("personalize: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("personalize: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFirepersonalize, result is " + res);
		NxpLogUtils.d(TAG, "testDESFirepersonalize, End");

	}

	/** DESFire update PICC Master key Operations . */
	private void testDESFireupdatePICCMasterKey() {
		byte[] oldKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };
		byte[] newKey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };
		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFireupdatePICCMasterKey, start");
			mDESFire.updatePICCMasterKey(oldKey, newKey);
			res = true;
			showMessage("DESFire Update PICC Master Key: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("DESFire Update PICC Master Key: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFireupdatePICCMasterKey, result is " + res);
		NxpLogUtils.d(TAG, "testDESFireupdatePICCMasterKey, End");

	}

	/** DESFire Format Operations . */
	private void testDESFireFormat() {
		byte[] mykey = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00 };

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testDESFireFormat, start");
			mDESFire.format(mykey);
			res = true;
			showMessage("Format: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("Format: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testDESFireFormat, result is " + res);
		NxpLogUtils.d(TAG, "testDESFireFormat, End");
	}

	/**
	 * Mifare classic Card Logic.
	 * 
	 * @throws SmartCardException
	 */
	public void classicCardLogic() throws SmartCardException {

		showImageSnap(R.drawable.classic);
		tv.setText(" ");
		showMessage("Card Detected : " + classic.getCardDetails().cardName, 'n');

		try {
			showMessage("Uid :" + Utilities.dumpBytes(classic.getUID()), 'd');
			classic.getReader().setTimeout(2000);
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				testClassicformat();
				testClassicpersonalize();
				testClassicupdateMasterKey();
				testClassicauthenticate();
				testClassicWrite();
				testClassicRead();
			} else {
				testClassicauthenticate();
				testClassicRead();
			}

			// byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte)
			// 0xFF,
			// (byte) 0xFF, (byte) 0xFF };
			//
			// classic.authenticateSectorWithKeyA(0, key);
			// showCardDetails(classic.getCardDetails());
			classic.getReader().close();
		} catch (ReaderException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Mifare classic Card Logic.
	 *
	 * @throws SmartCardException
	 */
	public void classicEV1CardLogic() throws SmartCardException {

		showImageSnap(R.drawable.classicev1);
		tv.setText(" ");
		showMessage("Card Detected : " + classic.getCardDetails().cardName, 'n');

		try {
			showMessage("Uid :" + Utilities.dumpBytes(classic.getUID()), 'd');
			classic.getReader().setTimeout(2000);
			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				testClassicformat();
				testClassicpersonalize();
				testClassicupdateMasterKey();
				testClassicauthenticate();
				testClassicWrite();
				testClassicRead();
			} else {
				testClassicauthenticate();
				testClassicRead();
			}
			// byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte)
			// 0xFF,
			// (byte) 0xFF, (byte) 0xFF };
			//
			// classic.authenticateSectorWithKeyA(0, key);
			// showCardDetails(classic.getCardDetails());
			classic.getReader().close();
		} catch (ReaderException e) {

			e.printStackTrace();
		}
	}

	/** Classic Write IO Operations. */
	private void testClassicWrite() {
		byte[] bdata = null;
		boolean res = false;

		try {
			NxpLogUtils.d(TAG, "testClassicWrite, start");

			bdata = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00 };
			classic.write(bdata);
			res = true;
		} catch (SmartCardException e) {
			e.printStackTrace();
		}

		showMessage("Write :" + Utilities.dumpBytes(bdata), 'd');

		NxpLogUtils.d(TAG, "testClassicWrite, result is " + res);
		NxpLogUtils.d(TAG, "testClassicWrite, End");
	}

	/** Classic Read IO Operations. */
	private void testClassicRead() {
		byte[] read = null;
		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testClassicRead, start");
			read = classic.read(16);
			res = true;
		} catch (SmartCardException e) {
			e.printStackTrace();
		}
		showMessage("Read :" + Utilities.dumpBytes(read), 'd');
		NxpLogUtils.d(TAG, "testClassicRead, result is " + res);
		NxpLogUtils.d(TAG, "testClassicRead, End");
	}

	/** Classic Authenticate Operations. */
	private void testClassicauthenticate() {
		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testClassicauthenticate, start");
			byte sectorNo = 2;
			byte[] appId = new byte[] { 0x11, 0x11, 0x11 };
			byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

			classic.authenticate(sectorNo, appId, key);
			res = true;
		} catch (SmartCardException e) {
			e.printStackTrace();
		}
		showMessage("authenticate :" + res, 'd');
		NxpLogUtils.d(TAG, "testClassicauthenticate, result is " + res);
		NxpLogUtils.d(TAG, "testClassicauthenticate, End");
	}

	/** Classic Update Master key Operations. */
	private void testClassicupdateMasterKey() {
		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testClassicupdateMasterKey, start");
			byte sectorNo = 2;
			byte[] oldKey = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
			byte[] newKey = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
			classic.updateApplicationMasterKey(sectorNo, oldKey, newKey);
			res = true;
		} catch (SmartCardException e) {
			e.printStackTrace();
		}

		showMessage("updateMasterKey : " + res, 'd');
		NxpLogUtils.d(TAG, "testClassicupdateMasterKey, result is " + res);
		NxpLogUtils.d(TAG, "testClassicupdateMasterKey, End");
	}

	/** Classic personalize Operations. */
	private void testClassicpersonalize() {
		byte sectorNo = 2;
		byte[] appId = new byte[] { 0x11, 0x11, 0x11 };
		byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testClassicpersonalize, start");
			classic.personalize(sectorNo, appId, key);
			res = true;
		} catch (SmartCardException e) {
			e.printStackTrace();
		}
		showMessage("persionalize :" + res, 'd');
		NxpLogUtils.d(TAG, "testClassicpersonalize, result is " + res);
		NxpLogUtils.d(TAG, "testClassicpersonalize, End");
	}

	/** Classic Format Operations. */
	private void testClassicformat() {
		byte sectorNo = 2;
		byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testClassicformat, start");
			classic.format(sectorNo, key);
			res = true;
		} catch (SmartCardException e) {

			e.printStackTrace();
		}
		showMessage("farmat :" + res, 'd');
		NxpLogUtils.d(TAG, "testClassicformat, result is " + res);
		NxpLogUtils.d(TAG, "testClassicformat, End");
	}

	/**
	 * Plus lite operations.
	 * 
	 * @throws SmartCardException
	 *             when exception occur.
	 */
	public void plusCardLogic() throws SmartCardException {

		byte sectorNo = 8;
		byte[] appId = new byte[] { 0x1, 0x1, 0x8 };
		byte[] appKey = new byte[] { (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00 };
		byte[] newAppKey = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF };

		showImageSnap(R.drawable.plus);
		tv.setText(" ");
		showMessage("Card Detected: " + plus.getCardDetails().cardName + " " + plus.getCardDetails().securityLevel,
				'n');

		try {

			// this api will switch from plus sl0 to sl1. plus sl1 will be
			// detected as the classic card.
			// plus.personalizeCardToSL1();

			/* Do the following only if write checkbox is selected */
			if (bWriteAllowed) {
				plus.personalizeSector(sectorNo, appId, appKey);
				NxpLogUtils.d(TAG, "Card personalize successful");
				byte[] testByte = new byte[] { (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xE9,
						(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x04, (byte) 0xFB, (byte) 0x04, (byte) 0xFB, (byte) 0x21, (byte) 0x00, (byte) 0x00,
						(byte) 0x00, (byte) 0xDE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x21, (byte) 0x00,
						(byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0xFA, (byte) 0x05, (byte) 0xFA, (byte) 0x2C,
						(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xD3, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
				testPlusWriteBlock(testByte);
				testPlusReadBlock();
				plus.updateApplicationMasterKey(sectorNo, appId, appKey, newAppKey);
				showMessage("Restore sector app key to factory default", 'n');
				showMessage("Performing write/read again", 'n');
				testByte = new byte[] { (byte) 0xFF, (byte) 0xAA, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF,
						(byte) 0xFF, (byte) 0xFF, (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04,
						(byte) 0xFB, (byte) 0x04, (byte) 0xFB, (byte) 0x21, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0xDE, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x21, (byte) 0x00, (byte) 0x00,
						(byte) 0x00, (byte) 0x05, (byte) 0xFA, (byte) 0x05, (byte) 0xFA, (byte) 0x2C, (byte) 0x00,
						(byte) 0x00, (byte) 0x00, (byte) 0xD3, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
				testPlusWriteBlock(testByte);
			} else {
				plus.authenticate(sectorNo, appId, newAppKey);
				testPlusReadBlock();
			}

			showMessage("UID: " + Utilities.dumpBytes(plus.getUID()), 'd');
			// showCardDetails(plus.getCardDetails());
			NxpLogUtils.d(TAG, "Card key application change successful");

			plus.getReader().close();
		} catch (ReaderException e1) {

			e1.printStackTrace();
		} catch (GeneralSecurityException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mifare plus card write.
	 * 
	 */
	private void PlusSL1CardLogic() {

		showImageSnap(R.drawable.plus);
		tv.setText(" ");
		showMessage("Card Detected: " + plusSL1.getTagName() + " " + "Security Level 1", 'n');
		showMessage("Uid :" + Utilities.dumpBytes(classic.getUID()), 'd');
		classic.getReader().setTimeout(2000);
		/* Do the following only if write checkbox is selected */
		if (bWriteAllowed) {
			testClassicformat();
			testClassicpersonalize();
			testClassicupdateMasterKey();
			testClassicauthenticate();
			testClassicWrite();
			testClassicRead();
		} else {
			testClassicauthenticate();
			testClassicRead();
		}

		// byte[] key = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte)
		// 0xFF,
		// (byte) 0xFF, (byte) 0xFF };
		//
		// classic.authenticateSectorWithKeyA(0, key);
		// showCardDetails(classic.getCardDetails());
		try {
			plusSL1.getReader().close();
		} catch (ReaderException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Mifare plus card write.
	 * 
	 * @param testByte
	 *            byte array.
	 * @throws SmartCardException
	 *             when exception occur.
	 */
	private void testPlusWriteBlock(final byte[] testByte) throws SmartCardException {
		boolean resp = false;

		try {
			plus.write(testByte);
			resp = true;
		} catch (PlusException e) {
			e.printStackTrace();
		} catch (SAMException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		showMessage("Write: " + resp, 'd');
	}

	/**
	 * Mifare plus read block.
	 * 
	 * @throws SmartCardException
	 *             when exception occur.
	 */
	private void testPlusReadBlock() throws SmartCardException {

		// boolean resp = false ;
		byte[] read = null;
		try {
			read = plus.read(40);
			// resp = true;
		} catch (PlusException e) {
			e.printStackTrace();
		} catch (SAMException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		showMessage("Read Sector8: " + Utilities.dumpBytes(read), 'd');
	}

	/**
	 * creating the text record for NDEF Data.
	 * 
	 * @param payload
	 *            NDEF Data
	 * @param locale
	 *            locale
	 * @param encodeInUtf8
	 *            true/false
	 * @return ndefrecord instance.
	 */
	public static NdefRecordWrapper createTextRecord(final String payload, final Locale locale,
			final boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
		byte[] textBytes = payload.getBytes(utfEncoding);
		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
		NdefRecordWrapper record = new NdefRecordWrapper(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0],
				data);
		return record;
	}

	/**
	 * Mifare classic Card Logic.
	 * 
	 * @throws SmartCardException
	 */
	public void ultralightCardLogic() throws SmartCardException {

		showImageSnap(R.drawable.ultralight);
		tv.setText(" ");
		showMessage("Card detected : " + mifareUL.getCardDetails().cardName, 'n');

		try {
			if (bWriteAllowed) {
				testULformat();
				testWriteNdef();
			}
			testULreadNdef();
			// showCardDetails(mifareUL.getCardDetails());
			mifareUL.getReader().close();
		} catch (ReaderException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Mifare Ultralight-C Card Logic.
	 * 
	 * @throws SmartCardException
	 */
	protected void ultralightcCardLogic() throws SmartCardException {

		showImageSnap(R.drawable.ultralight_c);
		tv.setText(" ");
		showMessage("Card Detected : " + objUlCardC.getCardDetails().cardName, 'n');

		byte[] data;

		try {

			data = objUlCardC.readAll();
			showMessage("Read All o/p is : " + Utilities.dumpBytes(data), 'd');
			// showCardDetails(objUlCardC.getCardDetails());
		} catch (IOException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("IOException occured... check LogCat", 't');
			e.printStackTrace();
		}

	}

	/**
	 * Mifare Ultralight EV1 CardLogic.
	 * 
	 * @throws SmartCardException
	 */
	protected void ultralightEV1CardLogic() throws SmartCardException {

		showImageSnap(R.drawable.ultralight_ev1);
		tv.setText(" ");
		String str = "Card Detected : " + objUlCardEV1.getCardDetails().cardName;
		showMessage(str, 'n');

		byte[] data;
		try {
			/** connect to card, authenticate and read data */

			data = objUlCardEV1.readAll();
			data = objUlCardEV1.read(DEFAULT_PAGENO_ULTRALIGHT);

			str = Utilities.dumpBytes(data);
			showMessage("Data read from card @ " + "page " + DEFAULT_PAGENO_ULTRALIGHT + " is " + str, 'd');

			byte[] bytesData = DATA.getBytes();
			String s1 = new String(bytesData);
			showMessage("Input String is: " + s1, 'd');
			byte[] bytesEncData = encryptAESData(bytesData, bytesKey);
			str = "Enctrypted string is " + Utilities.dumpBytes(bytesEncData);
			showMessage(str, 'd');
			if (bWriteAllowed) {
				objUlCardEV1.write(4, Arrays.copyOfRange(bytesEncData, 0, 4));
				objUlCardEV1.write(5, Arrays.copyOfRange(bytesEncData, 4, 8));
				objUlCardEV1.write(6, Arrays.copyOfRange(bytesEncData, 8, 12));
				objUlCardEV1.write(7, Arrays.copyOfRange(bytesEncData, 12, 16));

				byte[] bytesDecData = decryptAESData(data, bytesKey);
				String s = new String(bytesDecData);
				str = "Decrypted string is " + s;
				showMessage(str, 'd');

				if (Arrays.equals(bytesData, bytesDecData)) {
					showMessage("Matches", 't');
				}
			}

			// showCardDetails(objUlCardEV1.getCardDetails());
		} catch (IOException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("IOException occured... check LogCat", 't');
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("InvalidKeyException occured... check LogCat", 't');
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("NoSuchAlgorithmException occured... check LogCat", 't');
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("NoSuchPaddingException occured... check LogCat", 't');
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("IllegalBlockSizeException occured ... check LogCat", 't');
			e.printStackTrace();
		} catch (BadPaddingException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("BadPaddingException occured ... check LogCat", 't');
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			showMessage(e.getMessage(), 'l');
			showMessage("InvalidAlgorithmParameterException ... check LogCat", 't');
			e.printStackTrace();
		}
		/* Save the logs in \sdcard\NxpLogDump\logdump.xml */
		NxpLogUtils.save();
	}

	/** Ultralight Read Ndef Operations. */
	private void testULreadNdef() {

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testULreadNdef, start");
			NdefMessageWrapper msgread = null;
			msgread = new NdefMessageWrapper(mifareUL.readNDEF().toByteArray());
			String sMsg = new String(msgread.getRecords()[0].getPayload());
			res = true;
			showMessage("Read NDEF Data: " + Utilities.dumpHexAscii(sMsg.getBytes()), 'd');
			NxpLogUtils.i(TAG, Utilities.dumpBytes(msgread.getRecords()[0].getPayload()));
			NxpLogUtils.i(TAG, sMsg);

		} catch (SmartCardException e) {
			showMessage("Read NDEF: " + res, 'd');
			e.printStackTrace();
		} catch (FormatException e) {

			e.printStackTrace();
		}

		NxpLogUtils.d(TAG, "testULreadNdef, result is " + res);
		NxpLogUtils.d(TAG, "testULreadNdef, End");
	}

	/** Ultralight Format Operations. */
	private void testULformat() {

		boolean res = false;
		try {
			NxpLogUtils.d(TAG, "testULformat, start");
			mifareUL.getReader().setTimeout(500);
			mifareUL.format();
			res = true;
			showMessage("Format: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("Format: " + res, 'd');
			e.printStackTrace();
		}
		NxpLogUtils.d(TAG, "testULformat, result is " + res);
		NxpLogUtils.d(TAG, "testULformat, End");
	}

	/** Ultralight write ndef Operations. */
	public void testWriteNdef() {
		NdefRecordWrapper textRecord = createTextRecord("MIFARE SDK by NXP Semiconductors Inc.", Locale.ENGLISH, true);
		NdefMessageWrapper message = new NdefMessageWrapper(new NdefRecordWrapper[] { textRecord });

		boolean res = false;

		try {

			NxpLogUtils.d(TAG, "testWriteNdef, start");
			mifareUL.formatT2T();
			mifareUL.writeNDEF(message);
			res = true;
			showMessage("NDEF - Create Text Record: " + res, 'd');
		} catch (SmartCardException e) {
			showMessage("NDEF - Create Text Record: " + res, 'd');
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		NxpLogUtils.d(TAG, "testWriteNdef, result is " + res);
		NxpLogUtils.d(TAG, "testWriteNdef, End");

	}

	/**
	 * Update the card image on the screen.
	 * 
	 * @param cardTypeId
	 *            resource image id of the card image
	 * 
	 */

	private void showImageSnap(final int cardTypeId) {

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mImageView.getLayoutParams().width = (size.x * 2) / 3;
		mImageView.getLayoutParams().height = size.y / 3;
		Handler mHandler = new Handler();

		mHandler.postDelayed(new Runnable() {
			public void run() {
				mImageView.setImageResource(cardTypeId);
			}
		}, 200);

		mImageView.setImageResource(R.drawable.mifare_p);
		mImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade));
	}

	/**
	 * This will display message in toast or logcat .
	 * 
	 * @param str
	 *            String to be logged or displayed
	 * @param where
	 *            't' for Toast; 'l' for Logcat; 'd' for Display in UI; 'n' for
	 *            logcat and textview 'a' for All
	 * 
	 */
	protected void showMessage(final String str, final char where) {

		switch (where) {

		case 't':
			Toast.makeText(MainLiteActivity.this, "\n" + str, Toast.LENGTH_SHORT).show();
			break;
		case 'l':
			NxpLogUtils.i(TAG, "\n" + str);
			break;
		case 'd':
			tv.setText(tv.getText() + "\n-----------------------------------\n" + str);
			break;
		case 'a':
			Toast.makeText(MainLiteActivity.this, "\n" + str, Toast.LENGTH_SHORT).show();
			NxpLogUtils.i(TAG, "\n" + str);
			tv.setText(tv.getText() + "\n-----------------------------------\n" + str);
			break;
		case 'n':
			NxpLogUtils.i(TAG, "Dump Data: " + str);
			tv.setText(tv.getText() + "\n-----------------------------------\n" + str);
			break;
		default:
			break;
		}
		return;
	}

	/**
	 * Encrypt the supplied data with key provided.
	 * 
	 * @param data
	 *            data bytes to be encrypted
	 * @param key
	 *            Key to encrypt the buffer
	 * @return encrypted data bytes
	 * @throws NoSuchAlgorithmException
	 *             NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 *             NoSuchPaddingException
	 * @throws InvalidKeyException
	 *             InvalidKeyException
	 * @throws IllegalBlockSizeException
	 *             IllegalBlockSizeException
	 * @throws BadPaddingException
	 *             eption BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 *             InvalidAlgorithmParameterException
	 */
	protected byte[] encryptAESData(final byte[] data, final byte[] key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		byte[] encdata = cipher.doFinal(data);
		return encdata;
	}

	/**
	 * @param encdata
	 *            Encrypted input buffer.
	 * @param key
	 *            Key to decrypt the buffer.
	 * @return byte array.
	 * @throws NoSuchAlgorithmException
	 *             No such algorithm exce.
	 * @throws NoSuchPaddingException
	 *             NoSuchPaddingException.
	 * @throws InvalidKeyException
	 *             if key is invalid.
	 * @throws IllegalBlockSizeException
	 *             if block size is illegal.
	 * @throws BadPaddingException
	 *             if padding is bad.
	 * @throws InvalidAlgorithmParameterException
	 *             if algo. is not avaliable or not present.
	 */
	protected byte[] decryptAESData(final byte[] encdata, final byte[] key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {

		final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		byte[] decdata = cipher.doFinal(encdata);
		return decdata;
	}

	/** Message Handler call back for enableReaderMode API. */
	private Handler.Callback mLibhandlercb = new Handler.Callback() {

		@Override
		public boolean handleMessage(final Message arg0) {

			try {
				libInstance.filterIntent(tag, mCallback);
			} catch (CloneDetectedException e) {
				NxpLogUtils.e(TAG, e.getMessage(), e);
			}
			return false;
		}

	};

	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
		case STORAGE_PERMISSION_READ: {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				finish();
				startActivity(getIntent());
			} else {
				Toast.makeText(MainLiteActivity.this,
						"The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission",
						Toast.LENGTH_LONG).show();
			}
		}
		}

	}
}
