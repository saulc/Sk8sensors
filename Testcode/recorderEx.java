public class record extends Activity implements OnClickListener, SurfaceHolder.Callback{

/*
https://stackoverflow.com/questions/9238383/using-surfaceview-to-capture-a-video
*/
MediaRecorder recorder;
SurfaceHolder holder;
boolean recording=false;
public static final String TAG = "VIDEOCAPTURE";
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
     WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    recorder = new MediaRecorder();// Instantiate our media recording object
    initRecorder();
    setContentView(R.layout.view);

    SurfaceView cameraView = (SurfaceView) findViewById(R.id.surface_view);
    holder = cameraView.getHolder();
    holder.addCallback(this);
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    cameraView.setClickable(true);// make the surface view clickable
    cameraView.setOnClickListener((OnClickListener) this);// onClicklistener to be called when the surface view is clicked
}

private void initRecorder() {// this takes care of all the mediarecorder settings
    File OutputFile = new File(Environment.getExternalStorageDirectory().getPath());
    String video= "/DCIM/100MEDIA/Video";
    CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
     recorder.setProfile(cpHigh);

    //recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
    // default microphone to be used for audio
   // recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// default camera to be used for video capture.
    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// generally used also includes h264 and best for flash
   // recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); //well known video codec used by many including for flash
    //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// typically amr_nb is the only codec for mobile phones so...

    //recorder.setVideoFrameRate(15);// typically 12-15 best for normal use. For 1080p usually 30fms is used.
   // recorder.setVideoSize(720,480);// best size for resolution.
    //recorder.setMaxFileSize(10000000);
    recorder.setOutputFile(OutputFile.getAbsolutePath()+video+".3gp");
    //recorder.setVideoEncodingBitRate(256000);//
    //recorder.setAudioEncodingBitRate(8000);
   recorder.setMaxDuration(600000);


}

/*if(record.setMaxDuration>60000){

    recorder.stop();
    MediaRecorder.OnInfoListener;
    Toast display = Toast.makeText(this, "You have exceeded the record time", Toast.LENGTH_SHORT);// toast shows a display of little sorts
    display.show();
    return true;
}*/

private void prepareRecorder() {
    recorder.setPreviewDisplay(holder.getSurface());

    try {
        recorder.prepare();
    } catch (IllegalStateException e) {
        e.printStackTrace();
        finish();
    } catch (IOException e) {
        e.printStackTrace();
        finish();
    }
}

public void onClick(View v) {
    if (recording) {
        recorder.stop();
        recording = false;

        // Let's initRecorder so we can record again
        initRecorder();
        prepareRecorder();
        Toast display = Toast.makeText(this, "Stopped Recording", Toast.LENGTH_SHORT);// toast shows a display of little sorts
        display.show();


    } else {

        recorder.start();
        Log.v(TAG,"Recording Started");
        recording = true;

    }
}

public void surfaceCreated(SurfaceHolder holder) {
    initRecorder();
    Log.v(TAG,"surfaceCreated");
    prepareRecorder();
}

public void surfaceChanged(SurfaceHolder holder, int format, int width,
        int height) {
}

public void surfaceDestroyed(SurfaceHolder holder) {
    if (recording) {
        recorder.stop();
        recording = false;
    }
    recorder.release();
    finish();

}
