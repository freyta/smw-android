
package net.freyta.smw;
import org.libsdl.app.SDLActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


//This class is the main SDLActivity and just sets up a bunch of default files
public class MainActivity extends SDLActivity{


    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if storage permissions are granted
        if (hasStoragePermission()) {
            setupFiles();
        } else {
            requestStoragePermission();
        }
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
//                File smwAssets = new File(externalDir, "smw_assets.dat");

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
