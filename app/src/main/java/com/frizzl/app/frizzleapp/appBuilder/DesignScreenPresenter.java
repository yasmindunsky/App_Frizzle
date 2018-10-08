package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.widget.PopupWindow;

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
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String appIcon = currentUserApp.getIcon();
        String appName = currentUserApp.getName();
        return UserCreatedViewsModel.getXml(appIcon, appName);
    }

    public DesignScreenPresenter(DesignScreenFragment designScreenFragment) {
        this.designScreenFragment = designScreenFragment;
    }

    public void saveState() {
        views = UserCreatedViewsModel.getViews();
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setViews(UserCreatedViewsModel.getViews(), UserCreatedViewsModel.getNumOfButtons(),
                UserCreatedViewsModel.getNumOfTextViews(),  UserCreatedViewsModel.getNumOfImageViews(),
                UserCreatedViewsModel.getNextViewIndex());
                UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
    }

    public static Map<Integer, UserCreatedView> getViews(Context context) {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        if (currentUserApp != null) {
            views = currentUserApp.getViews();
            UserCreatedViewsModel.setViews(views);
            UserCreatedViewsModel.setNumOfButton(currentUserApp.getNumOfButtons());
            UserCreatedViewsModel.setNumOfTextViews(currentUserApp.getNumOfTextViews());
            UserCreatedViewsModel.setNumOfImageViews(currentUserApp.getNumOfImageViews());
            UserCreatedViewsModel.setNextViewIndex(currentUserApp.getNextIndex());
        }
        if (views == null || views.isEmpty()) {
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

                    // For temp testing
                    if (UserProfile.user.getCurrentLevel() == 0 && UserProfile.user.getCurrentTaskNum() == 0) {
                        designScreenFragment.taskCompleted();
                    }

                    UserCreatedViewsModel.addNewUserCreatedTextView(context);
                    break;
                case AnnotationUserCreatedViewType.BUTTON:
                    UserCreatedViewsModel.addNewUserCreatedButton(context);
                    break;
                case AnnotationUserCreatedViewType.IMAGE_VIEW:
                    UserCreatedViewsModel.addNewUserCreatedImageView(context);
                    int thisViewIndex = UserCreatedViewsModel.getNextViewIndex() - 1;
                    UserCreatedView userCreatedView = UserCreatedViewsModel.getViews().get(thisViewIndex);
                    PopupWindow propertiesTablePopupWindow = userCreatedView.getPropertiesTablePopupWindow(context);
                    designScreenFragment.presentPopup(propertiesTablePopupWindow);
                    // For temp testing
                    if (UserProfile.user.getCurrentLevel() == 3 && UserProfile.user.getCurrentTaskNum() == 0) {
                        designScreenFragment.taskCompleted();
                    }
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

    public String getManifest() {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String appIcon = currentUserApp.getIcon();
        String appName = currentUserApp.getName();
        return UserCreatedViewsModel.getManifest(appIcon, appName);
    }
}
