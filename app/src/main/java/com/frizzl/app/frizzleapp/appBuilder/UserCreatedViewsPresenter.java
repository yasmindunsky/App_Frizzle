package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.widget.Toast;

import com.frizzl.app.frizzleapp.UserProfile;

import java.util.Map;

/**
 * Created by Noga on 09/07/2018.
 */

public class UserCreatedViewsPresenter {
    private static Map<Integer, UserCreatedView> views;
    private DesignScreenFragment designScreenFragment;
//    private UserCreatedViewsModel userCreatedViewsModel;

    enum viewTypes {Button, TextView}

    public UserCreatedViewsPresenter(DesignScreenFragment designScreenFragment) {
        this.designScreenFragment = designScreenFragment;
    }

    public void saveState() {
        views = UserCreatedViewsModel.getViews();
        UserProfile.user.setViews(views);
    }

    public static Map<Integer, UserCreatedView> getViews(Context context) {
        views = UserProfile.user.getViews();
        if (views.isEmpty()) {
            views = UserCreatedViewsModel.initializeViews(context);
        }
        return views;
    }

    public void addNewUserCreatedView(Context context, viewTypes viewType) {
        boolean canAddView = checkIfTheresRoom();
        if (canAddView) {
            switch (viewType) {
                case TextView:
                    UserCreatedViewsModel.addNewUserCreatedTextView(context);
                    break;
                case Button:
                    UserCreatedViewsModel.addNewUserCreatedButton(context);
                    break;
            }
            designScreenFragment.getViewsAndPresent(views);
        }
    }

    public static void deleteView(int viewToDeleteIndex) {
        UserCreatedViewsModel.deleteUserCreatedVIew(viewToDeleteIndex);
    }

    private boolean checkIfTheresRoom() {
        // Check if reached max num of elements.
        int maxNum = designScreenFragment.getGridLayoutColCount() * designScreenFragment.getGridLayoutRowCount();
        int nextViewIndex = UserCreatedViewsModel.getNextViewIndex();
        if (nextViewIndex >= maxNum ) {
            designScreenFragment.showError("אופס, נגמר המקום!");
            return false;
        }
        return true;
    }
}
