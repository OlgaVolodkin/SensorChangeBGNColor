package com.moveo.olga.moveotask.ui;

import android.content.Context;

import com.moveo.olga.moveotask.Consts;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainModel implements MainInterface.Model {

    @Override
    public void realmInit(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(Consts.Realm.REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
