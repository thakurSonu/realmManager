/*
package com.sonu.realm;


import java.io.Closeable;

import io.realm.Realm;

/**
 * The RealmManager allows creating a singleton Realm manager which can open thread-local instances.
 *
 * It also allows obtaining the open thread-local instance without incrementing the reference count.
 */
public class RealmManager {
    private final ThreadLocal<Realm> localRealms = new ThreadLocal<>();

    /**
     * Opens a reference-counted local Realm instance.
     *
     * @return the open Realm instance
     */
    private Realm openLocalInstance() {
       // checkDefaultConfiguration();
        Realm realm = Realm.getInstance(getContext()); // <-- maybe this should be a parameter
        Realm localRealm = localRealms.get();
        if(localRealm == null || localRealm.isClosed()) {
            localRealms.set(realm);
        }
        return realm;
    }

    /**
     * Returns the local Realm instance without adding to the reference count.
     *
     * @return the local Realm instance
     * @throws IllegalStateException when no Realm is open
     */
    public Realm getLocalInstance() {
        Realm realm = localRealms.get();
        if(realm == null || realm.isClosed()) {

            realm = openLocalInstance();
            /*throw new IllegalStateException(
                    "No open Realms were found on this thread.");*/
        }

        return realm;
    }

    /**
     * Closes local Realm instance, decrementing the reference count.
     *
     * @throws IllegalStateException if there is no open Realm.
     */
    public void closeLocalInstance() {
        try {
            // checkDefaultConfiguration();
            Realm realm = localRealms.get();
            if (realm == null || realm.isClosed()) {
                throw new IllegalStateException(
                        "Cannot close a Realm that is not open.");
            }
            realm.close();
            // noinspection ConstantConditions
            if (Realm.getLocalInstanceCount(DocApplication.getDefaultConfig()) <= 0) {
                localRealms.set(null);
            }
        }catch (Exception ex){
            LogEx.displayRetrofitError("main", ex);
        }
    }

   /* private void checkDefaultConfiguration() {
        if(Realm.getDefaultConfiguration() == null) {
            throw new IllegalStateException("No default configuration is set.");
        }
    }*/
}
