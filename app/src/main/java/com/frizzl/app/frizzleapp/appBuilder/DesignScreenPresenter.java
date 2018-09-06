package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;

import com.frizzl.app.frizzleapp.AnnotationUserCreatedViewType;
import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.Map;

/**
 * Created by Noga on 09/07/2018.
 */

public class DesignScreenPresenter {
    private static Map<Integer, UserCreatedView> views;
    private DesignScreenFragment designScreenFragment;
//    private UserCreatedViewsModel userCreatedViewsModel;

    public String getXml() {
        return UserCreatedViewsModel.getXml();
    }

    public DesignScreenPresenter(DesignScreenFragment designScreenFragment) {
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

    public void addNewUserCreatedView(Context context, String viewType) {
        boolean canAddView = checkIfTheresRoom();
        if (canAddView) {
            // TODO: make UserCreatedViewsFactory
            switch (viewType) {
                case AnnotationUserCreatedViewType.TEXT_VIEW:
                    UserCreatedViewsModel.addNewUserCreatedTextView(context);
                    break;
                case AnnotationUserCreatedViewType.BUTTON:
                    UserCreatedViewsModel.addNewUserCreatedButton(context);
                    break;
                case AnnotationUserCreatedViewType.IMAGE_VIEW:
                    UserCreatedViewsModel.addNewUserCreatedImageView(context);
            }
            designScreenFragment.getViewsAndPresent(views);
        }
    }

    public static void deleteView(int viewToDeleteIndex) {
        UserCreatedViewsModel.deleteUserCreatedView(viewToDeleteIndex);
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

    public String getAppName() {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String appName = "";
        if (currentUserApp != null) {
            appName = currentUserApp.getName();
        }
        return appName;
    }

    public String getIcon() {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String icon = "";
        if (currentUserApp != null) {
            icon = currentUserApp.getIcon();
        }
        return icon;
    }
}
