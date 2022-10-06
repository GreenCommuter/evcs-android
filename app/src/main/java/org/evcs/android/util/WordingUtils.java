package org.evcs.android.util;

import org.evcs.android.model.shared.Wording;

public final class WordingUtils {

    public static void saveWordings(Wording[] wordings) {
        for (Wording w : wordings) {
            StorageUtils.storeInSharedPreferences(w.getKey(), w.getWording());
        }
    }

    public static String getWording(String key) {
        return StorageUtils.getStringFromSharedPreferences(key, "");
    }

}

