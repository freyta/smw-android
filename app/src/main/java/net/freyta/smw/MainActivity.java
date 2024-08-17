
package net.freyta.smw;
import org.libsdl.app.SDLActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


//This class is the main SDLActivity and just sets up a bunch of default files
public class MainActivity extends SDLActivity{


    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

    // Allows us to check for multiple presses
    boolean isStartPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if storage permissions are granted
        if (hasStoragePermission()) {
            setupFiles();
        } else {
            requestStoragePermission();
        }
        inflateOverlay();
    }

        // Check if storage permission is granted
        private boolean hasStoragePermission() {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED;
        }

        // Request storage permission
        private void requestStoragePermission() {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);

            try {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                startActivity(intent);
            } catch (Exception ex){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }

        // Handle permission request result
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupFiles();
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                }
            }
        }

    private void setupFiles() {
        // Check if external storage is available
        if (isExternalStorageWritable()) {
            // Get the root directory of the external storage
            File externalDir = getExternalFilesDir(null);

            if (externalDir != null) {

                // Create a file object for the config file
                File configFile = new File(externalDir, "smw.ini");

                // Uncomment this line to automatically copy the smw_assets
                // to the required folder. smw_assets.dat must be in the assets folder already
                // File smwAssets = new File(externalDir, "smw_assets.dat");

                File datNotice = new File(externalDir, "PLACE smw_assets.dat HERE");

                File saves_folder = new File(externalDir+ File.separator + "saves");

                File saves_ref_folder = new File(saves_folder + File.separator + "ref");

                File saves_playthrough_folder = new File(externalDir + File.separator + "playthrough");

                // Check if the folder doesn't exist, then create it
                saves_folder.mkdirs();

                saves_ref_folder.mkdirs();

                saves_playthrough_folder.mkdirs();


                //copy reference saves and config to external data dir so user can change if needed.

                try {
                    AssetCopyUtil.copyAssetsToExternal(this, "saves/ref", getExternalFilesDir(null).getAbsolutePath() + "/saves/ref");
                    datNotice.createNewFile();
                    if (configFile.createNewFile()) {
                        InputStream inputStream;
                        try {
                            inputStream = getAssets().open("smw.ini");  // Replace with your actual asset file name
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        // Write configuration data to configFile
                        writeDataToFile(configFile,inputStream);
                    }

                    // Copy the smw_assets.dat to the external dir
                    // Note: Uncomment these lines to copy smw_assets to the correct folder
//                    if (smwAssets.createNewFile()) {
//                        InputStream inputStream;
//                        try {
//                          inputStream = getAssets().open("smw_assets.dat");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            return;
//                        }
//                        // Write the data to file
//                        writeDataToFile(smwAssets, inputStream);
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        inflateOverlay();
    }
    public void inflateOverlay() {
        LayoutInflater inflater = getLayoutInflater();
        View overlayView = inflater.inflate(R.layout.layout, null);

        // Add the overlay
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.addView(overlayView);

        // The following mapping needs to match the one in zelda3.ini
        int keyCodeFor_START = KeyEvent.KEYCODE_N;
        int keyCodeFor_SELECT = KeyEvent.KEYCODE_V;
        int keyCodeFor_A = KeyEvent.KEYCODE_X;
        int keyCodeFor_B = KeyEvent.KEYCODE_Z;
        int keyCodeFor_X = KeyEvent.KEYCODE_S;
        int keyCodeFor_Y = KeyEvent.KEYCODE_A;
        int keyCodeFor_L = KeyEvent.KEYCODE_C;
        int keyCodeFor_R = KeyEvent.KEYCODE_BACK;
        int keyCodeFor_Up = KeyEvent.KEYCODE_Y;
        int keyCodeFor_Down = KeyEvent.KEYCODE_H;
        int keyCodeFor_Left = KeyEvent.KEYCODE_G;
        int keyCodeFor_Right = KeyEvent.KEYCODE_J;
        int keyCodeFor_Turbo = KeyEvent.KEYCODE_M;
        int keyCodeFor_LoadState = KeyEvent.KEYCODE_O;
        int keyCodeFor_SaveState = KeyEvent.KEYCODE_Q;

        final boolean[] hasScreenBeenTouched = {false};

        // Manage touch events from the keys
        overlayView.findViewById(R.id.ButtonKeyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SDLActivity.showTextInput(10, -200, 30, 40);
            }
        });
        overlayView.findViewById(R.id.Button_start).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_START);
                        isStartPressed = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        isStartPressed = false;
                        onNativeKeyUp(keyCodeFor_START);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_select).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_SELECT);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_SELECT);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_A).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_A);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_A);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_B).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_B);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_B);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_X).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_X);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_X);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_Y).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_Y);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_Y);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_L).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_L);
                        // Load the saved state
                        if (isStartPressed){
                            onNativeKeyDown(keyCodeFor_LoadState);
                        }

                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_L);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_R).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_R);
                        if (isStartPressed) {
                            onNativeKeyDown(keyCodeFor_SaveState);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_R);
                        return true;
                }
                return false;
            }
        });
        overlayView.findViewById(R.id.Button_Turbo).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onNativeKeyDown(keyCodeFor_Turbo);
                        return true;
                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_Turbo);
                        return true;
                }
                return false;
            }
        });

        overlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!hasScreenBeenTouched[0]) {
                    overlayView.setAlpha(1f);
                    hasScreenBeenTouched[0] = true;
                }
                return false;
            }
        });

        overlayView.findViewById(R.id.Button_Dpad).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (motionEvent.getX() > (view.getWidth()*0.67)) {
                            onNativeKeyDown(keyCodeFor_Right);
                        }else if (motionEvent.getX() < (view.getWidth()*0.33)) {
                            onNativeKeyDown(keyCodeFor_Left);
                        }
                        if (motionEvent.getY() > (view.getHeight()*0.67)) {
                            onNativeKeyDown(keyCodeFor_Down);
                        }else if (motionEvent.getY() < (view.getHeight()*0.33)) {
                            onNativeKeyDown(keyCodeFor_Up);
                        }else{
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        onNativeKeyUp(keyCodeFor_Left);
                        onNativeKeyUp(keyCodeFor_Right);
                        onNativeKeyUp(keyCodeFor_Down);
                        onNativeKeyUp(keyCodeFor_Up);
                        return true;

                    case MotionEvent.ACTION_MOVE: //Support sliding on the dpad
                        onNativeKeyUp(keyCodeFor_Left);
                        onNativeKeyUp(keyCodeFor_Right);
                        onNativeKeyUp(keyCodeFor_Down);
                        onNativeKeyUp(keyCodeFor_Up);
                        if (motionEvent.getX() > (view.getWidth()*0.67)) {
                            onNativeKeyDown(keyCodeFor_Right);
                        }else if (motionEvent.getX() < (view.getWidth()*0.33)) {
                            onNativeKeyDown(keyCodeFor_Left);
                        }
                        if (motionEvent.getY() > (view.getHeight()*0.67)) {
                            onNativeKeyDown(keyCodeFor_Down);
                        }else if (motionEvent.getY() < (view.getHeight()*0.33)) {
                            onNativeKeyDown(keyCodeFor_Up);
                        }else{
                        }
                        return true;
                }
                return false;
            }
        });
    }
    private void writeDataToFile(File file,InputStream inputStream) {
        try {
            // Copy the content from the asset InputStream to the target file
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Check if external storage is available and writable
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
