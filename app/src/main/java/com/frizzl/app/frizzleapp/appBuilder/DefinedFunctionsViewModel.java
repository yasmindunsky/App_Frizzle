package com.frizzl.app.frizzleapp.appBuilder;

import android.arch.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Noga on 24/10/2018.
 */

public class DefinedFunctionsViewModel extends ViewModel {
    private final Set<String> functions = new HashSet<>();

    public void addFunction(String function) {
        functions.add(function);
    }

    public Set<String> getFunctions() {
        return functions;
    }

    public void clearFunctions() {
        functions.clear();
    }
}
