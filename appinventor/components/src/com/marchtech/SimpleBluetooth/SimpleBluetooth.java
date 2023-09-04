package com.marchtech.SimpleBluetooth;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;

import android.app.Activity;
import android.os.Build;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.BluetoothConnectionBase;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.PermissionResultHandler;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.util.BluetoothReflection;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.SdkLevel;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.marchtech.Icon;

@DesignerComponent( version = 1,
                    description = "Bluetooth component",
                    category = ComponentCategory.EXTENSION,
                    nonVisible = true,
                    iconName = Icon.ICON)
@SimpleObject(external = true)
@UsesPermissions({ACCESS_COARSE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN, BLUETOOTH_CONNECT, BLUETOOTH_SCAN})
public class SimpleBluetooth extends BluetoothConnectionBase {
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final String[] RUNTIME_PERMISSIONS = new String[] { BLUETOOTH_CONNECT, BLUETOOTH_SCAN };

    private final List<Component> attachedComponents = new ArrayList<Component>();
    private Set<Integer> acceptableDeviceClasses;

    private Timer timerRaw;
    private Timer timerText;
    private Timer timerSigned;
    private Timer timerUnsigned;
    private Activity activity = new Activity();

    private int textBytes = -1;
    private int signedBytes = -1;
    private int unsignedBytes = -1;
    private int INTERVAL = 500;

    private byte[] dataBytes;

    public SimpleBluetooth(ComponentContainer container) {
        super(container, "Bluetooth");
    }

    @SimpleEvent(description = "The incoming data event.")
    public void GotRaw(List<Byte> result, int totalBytes) {
        EventDispatcher.dispatchEvent(this, "GotRaw", result, totalBytes);
    }

    @SimpleEvent(description = "The incoming data event.")
    public void GotText(String result) {
        EventDispatcher.dispatchEvent(this, "GotText", result);
    }

    @SimpleEvent(description = "The incoming data event.")
    public void GotSigned(int result) {
        EventDispatcher.dispatchEvent(this, "GotSigned", result);
    }

    @SimpleEvent
    public void GotSigned(long result) {
        EventDispatcher.dispatchEvent(this, "GotSigned", result);
    }

    @SimpleEvent
    public void GotSigned(List<Integer> result) {
        EventDispatcher.dispatchEvent(this, "GotSigned", result);
    }

    @SimpleEvent(description = "The incoming data event.")
    public void GotUnsigned(int result) {
        EventDispatcher.dispatchEvent(this, "GotUnsigned", result);
    }

    @SimpleEvent
    public void GotUnsigned(long result) {
        EventDispatcher.dispatchEvent(this, "GotUnsigned", result);
    }

    @SimpleEvent
    public void GotUnsigned(List<Integer> result) {
        EventDispatcher.dispatchEvent(this, "GotUnsigned", result);
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "500")
    @SimpleProperty
    public void Interval(int interval) {
        INTERVAL = interval;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public int Interval() {
        return INTERVAL;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "-1")
    @SimpleProperty
    public void TextBytes(int bytes) {
        textBytes = bytes;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public int TextBytes() {
        return textBytes;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "-1")
    @SimpleProperty
    public void SignedBytes(int bytes) {
        signedBytes = bytes;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public int SignedBytes() {
        return signedBytes;
    }

    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_INTEGER, defaultValue = "-1")
    @SimpleProperty
    public void UnsignedBytes(int bytes) {
        unsignedBytes = bytes;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public int UnsignedBytes() {
        return unsignedBytes;
    }

    @SimpleFunction(description = "To start getting raw data.")
    public void StartGetRaw(int delay) {
        if (IsConnected()) {
            timerRaw = new Timer("raw");
            TimerTask getRaw = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int numBytes = BytesAvailableToReceive();
                            dataBytes = read("GotRaw", numBytes);
                            List<Byte> list = new ArrayList<Byte>();
                            for (int i = 0; i < dataBytes.length; i++) {
                                byte n = dataBytes[i];
                                list.add(n);
                            }
                            GotRaw(list, numBytes);
                        }
                    });
                }
            };
            
            timerRaw.scheduleAtFixedRate(getRaw, delay, INTERVAL);
        }
    }

    @SimpleFunction(description = "To start getting data.")
    public void StartGetText(int delay) {
        if (IsConnected()) {
            timerText = new Timer("text");
            TimerTask getText = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (textBytes == -1) GotText(ReceiveText(BytesAvailableToReceive()));
                            else GotText(ReceiveText(textBytes));
                        }
                    });
                }
            };

            timerText.scheduleAtFixedRate(getText, delay, INTERVAL);
        }
    }

    @SimpleFunction(description = "To start getting data.")
    public void StartGetSigned(int delay) {
        if (IsConnected()) {
            timerSigned = new Timer("signed");
            TimerTask getSigned = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (signedBytes == -1) GotSigned(ReceiveSignedBytes(BytesAvailableToReceive()));
                            else if (signedBytes == 1) GotSigned(ReceiveSigned1ByteNumber());
                            else if (signedBytes == 2) GotSigned(ReceiveSigned2ByteNumber());
                            else if (signedBytes == 4) GotSigned(ReceiveSigned4ByteNumber());
                            else GotSigned(ReceiveSignedBytes(signedBytes));
                        }
                    });
                }
            };

            timerSigned.scheduleAtFixedRate(getSigned, delay, INTERVAL);
        }
    }

    @SimpleFunction(description = "To start getting data.")
    public void StartGetUnsigned(int delay) {
        if (IsConnected()) {
            timerUnsigned = new Timer("unsigned");
            TimerTask getUnsigned = new TimerTask() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (unsignedBytes == -1) GotUnsigned(ReceiveUnsignedBytes(BytesAvailableToReceive()));
                            else if (unsignedBytes == 1) GotUnsigned(ReceiveUnsigned1ByteNumber());
                            else if (unsignedBytes == 2) GotUnsigned(ReceiveUnsigned2ByteNumber());
                            else if (unsignedBytes == 4) GotUnsigned(ReceiveUnsigned4ByteNumber());
                            else GotUnsigned(ReceiveUnsignedBytes(unsignedBytes));
                        }
                    });
                }
            };

            timerUnsigned.scheduleAtFixedRate(getUnsigned, delay, INTERVAL);
        }
    }

    @SimpleFunction(description = "To stop getting all data.")
    public void StopGetAll() {
        StopGetRaw();
        StopGetText();
        StopGetSigned();
        StopGetUnsigned();
    }

    @SimpleFunction(description = "To stop getting data.")
    public void StopGetRaw() {
        try {
            timerRaw.cancel();
            timerRaw.purge();
        } catch (NullPointerException e) {
            return;
        }
    }

    @SimpleFunction(description = "To stop getting data.")
    public void StopGetText() {
        try {
            timerText.cancel();
            timerText.purge();
        } catch (NullPointerException e) {
            return;
        }
        
    }

    @SimpleFunction(description = "To stop getting data.")
    public void StopGetSigned() {
        try {
            timerSigned.cancel();
            timerSigned.purge();
        } catch (NullPointerException e) {
            return;
        }
    }

    @SimpleFunction(description = "To stop getting data.")
    public void StopGetUnsigned() {
        try {
            timerUnsigned.cancel();
            timerUnsigned.purge();
        } catch (NullPointerException e) {
            return;
        }
        
    }

    @SimpleFunction(description = "Converting raw data to string")
    public String ToString(int numberOfBytes) {
        try {
            if (numberOfBytes < 0) {
                return new String(dataBytes, 0, dataBytes.length - 1, CharacterEncoding());
            } else {
                return new String(dataBytes, CharacterEncoding());
            }
        } catch (UnsupportedEncodingException e) {
            Log.w(logTag, "UnsupportedEncodingException: " + e.getMessage());
            return new String(dataBytes);
        }
    }

    @SimpleFunction(description = "Converting raw data to signed bytes")
    public List<Integer> ToSignedBytes(int numberOfBytes) {
        List<Integer> list = new ArrayList<Integer>();
        if (dataBytes.length > numberOfBytes) {
            for (int i = 0; i < numberOfBytes; i++) {
                int n = dataBytes[i];
                list.add(n);
            }
        } else {
            for (int i = 0; i < dataBytes.length; i++) {
                int n = dataBytes[i];
                list.add(n);
            }
        }
        return list;
    }

    @SimpleFunction(description = "Converting raw data to unsigned bytes")
    public List<Integer> ToUnsignedBytes(int numberOfBytes) {
        List<Integer> list = new ArrayList<Integer>();
        if (dataBytes.length > numberOfBytes) {
            for (int i = 0; i < numberOfBytes; i++) {
                int n = dataBytes[i] & 0xFF;
                list.add(n);
            }
        } else {
            for (int i = 0; i < dataBytes.length; i++) {
                int n = dataBytes[i] & 0xFF;
                list.add(n);
            }
        }
        return list;
    }

    boolean attachComponent(Component component, Set<Integer> acceptableDeviceClasses) {
        if (attachedComponents.isEmpty()) {
            this.acceptableDeviceClasses = (acceptableDeviceClasses == null) ? null : new HashSet<Integer>(acceptableDeviceClasses);
        } else {
            if (this.acceptableDeviceClasses == null) {
                if (acceptableDeviceClasses != null) {
                    return false;
                }
            } else {
                if (acceptableDeviceClasses == null) {
                    return false;
                }
                if (!this.acceptableDeviceClasses.containsAll(acceptableDeviceClasses)) {
                    return false;
                }
                if (!acceptableDeviceClasses.containsAll(this.acceptableDeviceClasses)) {
                    return false;
                }
            }
        }

        attachedComponents.add(component);
        return true;
    }

    void detachComponent(Component component) {
        attachedComponents.remove(component);
        if (attachedComponents.isEmpty()) {
            acceptableDeviceClasses = null;
        }
    }

    @SimpleFunction(description = "Checks whether the Bluetooth device with the specified address " +
        "is paired.")
    public boolean IsDevicePaired(String address) {
        String functionName = "IsDevicePaired";
        Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_AVAILABLE);
            return false;
        }

        if (!BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_ENABLED);
            return false;   
        }

        int firstSpace = address.indexOf(" ");
        if (firstSpace != -1) {
            address = address.substring(0, firstSpace);
        }

        if (!BluetoothReflection.checkBluetoothAddress(bluetoothAdapter, address)) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_INVALID_ADDRESS);
            return false;
        }

        Object bluetoothDevice = BluetoothReflection.getRemoteDevice(bluetoothAdapter, address);
        return BluetoothReflection.isBonded(bluetoothDevice);
    }

    @SimpleProperty(description = "The addresses and names of paired Bluetooth devices", category = PropertyCategory.BEHAVIOR)
    public List<String> AddressesAndNames() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            for (String permission : RUNTIME_PERMISSIONS) {
                if (form.isDeniedPermission(permission)) {
                    throw new PermissionException(permission);
                }
            }
        }

        List<String> addressesAndNames = new ArrayList<String>();

        Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
        if (bluetoothAdapter != null) {
            if (BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
                for (Object bluetoothDevice : BluetoothReflection.getBondedDevices(bluetoothAdapter)) {
                    if (isDeviceClassAcceptable(bluetoothDevice)) {
                        String name = BluetoothReflection.getBluetoothDeviceName(bluetoothDevice);
                        String address = BluetoothReflection.getBluetoothDeviceAddress(bluetoothDevice);
                        addressesAndNames.add(address + " " + name);
                    }
                }
            }
        }

        return addressesAndNames;
    }

    private boolean isDeviceClassAcceptable(Object bluetoothDevice) {
        if (acceptableDeviceClasses == null) {
            return true;
        }

        Object bluetoothClass = BluetoothReflection.getBluetoothClass(bluetoothDevice);
        if (bluetoothClass == null) {
            return false;
        }

        int deviceClass = BluetoothReflection.getDeviceClass(bluetoothClass);
        return acceptableDeviceClasses.contains(deviceClass);
    }

    @SimpleFunction(description = "Connect to the Bluetooth device with the specified address and " +
        "the Serial Port Profile (SPP). Returns true if the connection was successful.")
    public boolean Connect(String address) {
        return connect("Connect", address, SPP_UUID);
    }

    @SimpleFunction(description = "Connect to the Bluetooth device with the specified address and " +
        "UUID. Returns true if the connection was successful.")
    public boolean ConnectWithUUID(String address, String uuid) {
        return connect("ConnectWithUUID", address, uuid);
    }

    private boolean connect(final String functionName, String address, final String uuidString) {
        final String finalAddress = address;
        if (Util.requestPermissionsForConnecting(form, this, functionName,
            new PermissionResultHandler() {
                @Override
                public void HandlePermissionResponse(String permission, boolean granted) {
                    connect(functionName, finalAddress, uuidString);
                }
            })) {
            return false;
        }

        Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_AVAILABLE);
            return false;
        }

        if (!BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_ENABLED);
            return false;
        }

        int firstSpace = address.indexOf(" ");
        if (firstSpace != -1) {
            address = address.substring(0, firstSpace);
        }

        if (!BluetoothReflection.checkBluetoothAddress(bluetoothAdapter, address)) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_INVALID_ADDRESS);
            return false;
        }

        Object bluetoothDevice = BluetoothReflection.getRemoteDevice(bluetoothAdapter, address);
        if (!BluetoothReflection.isBonded(bluetoothDevice)) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_PAIRED_DEVICE);
            return false;
        }

        if (!isDeviceClassAcceptable(bluetoothDevice)) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_REQUIRED_CLASS_OF_DEVICE);
            return false;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_INVALID_UUID, uuidString);
            return false;
        }

        Disconnect();

        try {
            connect(bluetoothDevice, uuid);
            return true;
        } catch (IOException e) {
            Disconnect();
            form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_CONNECT);
            return false;
        }
    }

    private void connect(Object bluetoothDevice, UUID uuid) throws IOException {
        Object bluetoothSocket;
        if (!secure && SdkLevel.getLevel() >= SdkLevel.LEVEL_GINGERBREAD_MR1) {
            bluetoothSocket = BluetoothReflection.createInsecureRfcommSocketToServiceRecord(bluetoothDevice, uuid);
        } else {
            bluetoothSocket = BluetoothReflection.createRfcommSocketToServiceRecord(bluetoothDevice, uuid);
        }
        BluetoothReflection.connectToBluetoothSocket(bluetoothSocket);
        setConnection(bluetoothSocket);
        Log.i(logTag, "Connected to Bluetooth device " +
            BluetoothReflection.getBluetoothDeviceAddress(bluetoothDevice) + " " +
            BluetoothReflection.getBluetoothDeviceName(bluetoothDevice) + ".");
    }
}
