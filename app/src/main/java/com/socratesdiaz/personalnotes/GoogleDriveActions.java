package com.socratesdiaz.personalnotes;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by socratesdiaz on 11/15/16.
 */
final public class GoogleDriveActions {
    private GoogleDriveActions() {

    }

    private static GoogleApiClient mGAC;
    private static Drive mGoogleService;

    static void init(NoteDeatilActivity context, String email) {
        if(context != null && email != null) {
            mGAC = new GoogleApiClient.Builder(context).addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE).setAccountName(email)
                    .addConnectionCallbacks(context).addOnConnectionFailedListener(context).build();

            mGoogleService = new Drive.Builder(
                    AndroidHttp.newCompatibleTransport(), new GsonFactory(), GoogleAccountCredential
            .usingOAuth2(context, Arrays.asList(DriveScopes.DRIVE_FILE))
            .setSelectedAccountName(email))
            .build();
        }
    }

}
