package com.marchtech.SimplePedometer_v3;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.marchtech.SimplePedometer_v3.SensorPreference.setServiceRunning;

public class MarchService extends Service {
    private static final String TAG = "SimplePedometer";
    private static final String PREFS_NAME = "SimplePedometerPrefs";

    private static final String CHANNEL_ID = "SimplePedometer Service";
    private static final int NOTIFICATION_ID = 1001;
    private static final String STARTED = "com.marctech.SimplePedometer_v3.started";

    private static final int INTERVAL_VARIATION = 250;
    private static final int NUM_INTERVALS = 2;
    private static final int WIN_SIZE = 100;
    private static final float STRIDE_LENGTH = (float) 0.73;
    private static final float PEAK_VALLEY_RANGE = (float) 40.0;

    private Context context;
    private SensorManager sensorManager;

    private int stopDetectionTimeout = 2000;
    private int winPos = 0;
    private int intervalPos = 0;
    private int numStepsWithFilter = 0;
    private int numStepsRaw = 0;
    private int avgPos = 0;

    private float lastValley = 0;
    private float strideLength = STRIDE_LENGTH;
    private float totalDistance = 0;
    private float[] lastValues = new float[WIN_SIZE];
    private float[] avgWindow = new float[10];

    private long stepTimestamp = 0;
    private long startTime = 0;
    private long prevStopClockTime = 0;
    private long[] stepInterval = new long[NUM_INTERVALS];

    private boolean foundValley = false;
    private boolean startPeaking = false;
    private boolean foundNonStep = true;
    private boolean pedometerPaused = true;

    private boolean isChanging = false;

    private Handler handler;
    private Thread thread;

    private HandlerThread sensorThread;
    private Handler mHandler;

    private Sensor sensor;
    private SensorEventListener listener;

    private SensorModel sensorData;

    private final IBinder binder = new LocalBinder();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                getSensorValue();
            } finally {
                handler.postDelayed(runnable, 30000);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        thread = new Thread(runnable);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        winPos = 0;
        startPeaking = false;
        numStepsWithFilter = 0;
        numStepsRaw = 0;

        foundValley = false;
        lastValley = 0;

        handler = new Handler();

        sensorThread = new HandlerThread("sensor_thread");
        sensorThread.start();
        mHandler = new Handler(sensorThread.getLooper());

        runnable.run();
        Log.d(TAG, "SimplePedometer Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean started = intent.getBooleanExtra(STARTED, false);
        if (started) stopSelf();
/*
        createChannel();
        Notification.Builder notification = new Notification.Builder(this)
            .setContentTitle("SimplePedometer")
            .setContentText("Steps");

        startForeground(NOTIFICATION_ID, buildNotification("Steps"));
        update();
*/        
        return START_NOT_STICKY;
    }
/*
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }

    private Notification buildNotification(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SimplePedometer")
            .setContentText(text)
            .setOngoing(true);

        return builder.build();
    }

    private void update() {
        String text = "Steps: " + numStepsRaw + "\n" + "Distances: " + totalDistance;
        boolean isVisible = false;

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = manager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == NOTIFICATION_ID) {
                isVisible = true;
                break;
            }
        }

        Log.v(TAG, "Is Foreground Visible: " + isVisible);
        if (isVisible) manager.notify(NOTIFICATION_ID, buildNotification(text));
        else startForeground(NOTIFICATION_ID, buildNotification(text));
    }
*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isChanging = true;
    }

    private void getSensorValue() {
        boolean isOnUiThread = Thread.currentThread() == Looper.getMainLooper().getThread();
        listener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.d(TAG, "Accelerometer accuracy changed");
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

                float[] values = event.values;
                float magnitude = 0;
                for (float v : values) magnitude += v * v;

                int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;

                if (startPeaking && isPeak()) {
                    if (foundValley && lastValues[mid] - lastValley > PEAK_VALLEY_RANGE) {
                        long timestamp = System.currentTimeMillis();
                        stepInterval[intervalPos] = timestamp - stepTimestamp;
                        intervalPos = (intervalPos + 1) % NUM_INTERVALS;
                        stepTimestamp = timestamp;
                        if (areStepsEquallySpaced()) {
                            if (foundNonStep) {
                                numStepsWithFilter += NUM_INTERVALS;
                                totalDistance += strideLength * NUM_INTERVALS;
                                foundNonStep = false;
                            }

                            numStepsWithFilter++;
                            totalDistance += strideLength;
                        } else foundNonStep = true;

                        numStepsRaw++;
                        foundValley = false;
                    }
                }

                if (startPeaking && isValley()) {
                    foundValley = true;
                    lastValley = lastValues[mid];
                }

                avgWindow[avgPos] = magnitude;
                avgPos = (avgPos + 1) % avgWindow.length;
                lastValues[winPos] = 0;
                for (float m : avgWindow) lastValues[winPos] += m;

                lastValues[winPos] /= avgWindow.length;
                if (startPeaking || winPos > 1) {
                    int i = winPos;
                    if (--i < 0) i += WIN_SIZE;

                    lastValues[winPos] += 2 * lastValues[i];
                    if (--i < 0) i += WIN_SIZE;
                    lastValues[winPos] += lastValues[i];
                    lastValues[winPos] /= 4;
                } else if (!startPeaking && winPos == 1) lastValues[1] = (lastValues[1] + lastValues[0]) / 2f;

                long elapsedTimestamp = System.currentTimeMillis();
                if (elapsedTimestamp - stepTimestamp > stopDetectionTimeout) stepTimestamp = elapsedTimestamp;

                if (winPos == WIN_SIZE - 1 && !startPeaking) startPeaking = true;

                winPos = (winPos + 1) % WIN_SIZE;

                sensorManager.unregisterListener(listener, sensor);

                sensorData = new SensorModel(77, numStepsRaw, numStepsWithFilter, totalDistance, strideLength, prevStopClockTime, System.currentTimeMillis());

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sensorManager.unregisterListener(listener);
                        handler.removeCallbacks(this);
                    }
                }, 12000);
            }

            private boolean areStepsEquallySpaced() {
                float avg = 0;
                int num = 0;
                for (long interval : stepInterval) {
                    if (interval > 0) {
                        num++;
                        avg += interval;
                    }
                }

                avg = avg / num;
                for (long interval : stepInterval) {
                    if (Math.abs(interval - avg) > INTERVAL_VARIATION) return false;
                }

                return true;
            }

            private boolean isPeak() {
                int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;
                for (int i = 0; i < WIN_SIZE; i++) {
                    if (i != mid && lastValues[i] > lastValues[mid]) return false;
                }

                return true;
            }

            private boolean isValley() {
                int mid = (winPos + WIN_SIZE / 2) % WIN_SIZE;
                for (int i = 0; i < WIN_SIZE; i++) {
                    if (i != mid && lastValues[i] < lastValues[mid]) return false;
                }

                return true;
            }
        };

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST, mHandler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        stopForeground(true);
        isChanging = false;
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        stopForeground(true);
        isChanging = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (!isChanging && !serviceIsRunning(this)) {
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean serviceIsRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }

        return false;
    }

    public void startService() {
        startService(new Intent(getApplicationContext(), MarchService.class));
    }

    public void stopService() {
        stopSelf();
        setServiceRunning(this, false);
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, MarchService.class);
        intent.putExtra(STARTED, true);

        PendingIntent serviceIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SimplePedometer")
            .setOnlyAlertOnce(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
            .setChannelId(CHANNEL_ID)
            .setSound(null)
            .setWhen(System.currentTimeMillis());

        return builder.build();
    }

    public class LocalBinder extends Binder {
        public MarchService getService() {
            return MarchService.this;
        }
    }
}
