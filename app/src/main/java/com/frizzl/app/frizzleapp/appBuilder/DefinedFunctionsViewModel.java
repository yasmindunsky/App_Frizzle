package com.frizzl.app.frizzleapp.appBuilder;

import android.arch.lifecycle.ViewModel;

import com.frizzl.app.frizzleapp.UserProfile;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Noga on 24/10/2018.
 */

public class DefinedFunctionsViewModel extends ViewModel {
    private final Set<String> functions = new HashSet<>();

    public void addFunction(String function) {
        functions.add(function);
        UserProfile.user.getCurrentUserApp().setDefinedFunctions(functions);
    }

    public Set<String> getFunctions() {
        return UserProfile.user.getCurrentUserApp().getDefinedFunctions();
    }

    public void clearFunctions() {
        functions.clear();
    }
}
