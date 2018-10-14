package com.frizzl.app.frizzleapp.appBuilder;

import android.content.Context;
import android.widget.PopupWindow;

import com.frizzl.app.frizzleapp.AnnotationUserCreatedViewType;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserApp;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.Map;

/**
 * Created by Noga on 09/07/2018.
 */

public class DesignScreenPresenter {
    private DesignScreenFragment designScreenFragment;

    public String getXml(Map<Integer, UserCreatedView> views) {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        String appIcon = currentUserApp.getIcon();
        String appName = currentUserApp.getName();
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeXml(views, appIcon, appName);
    }

    public DesignScreenPresenter(DesignScreenFragment designScreenFragment) {
        this.designScreenFragment = designScreenFragment;
    }

    public void saveState() {
        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
        currentUserApp.setViews(designScreenFragment.getViews(), designScreenFragment.getNumOfButtons(),
                designScreenFragment.getNumOfTextViews(),  designScreenFragment.getNumOfImageViews(),
                designScreenFragment.getNextViewIndex());
                UserProfile.user.setCurrentUserAppLevelID(currentUserApp);
    }

//    public Map<Integer, UserCreatedView> getViews(Context context) {
//        UserApp currentUserApp = UserProfile.user.getCurrentUserApp();
//        if (currentUserApp != null) {
//            views = currentUserApp.getViews();
//            userCreatedViewsModel.setViews(views);
//            userCreatedViewsModel.setNumOfButton(currentUserApp.getNumOfButtons());
//            userCreatedViewsModel.setNumOfTextViews(currentUserApp.getNumOfTextViews());
//            userCreatedViewsModel.setNumOfImageViews(currentUserApp.getNumOfImageViews());
//            userCreatedViewsModel.setNextViewIndex(currentUserApp.getNextIndex());
//        }
//        if (views == null || views.isEmpty()) {
//            views = userCreatedViewsModel.initializeViews(context);
//        }
//        return views;
//    }

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

                    designScreenFragment.addNewUserCreatedTextView();
                    break;
                case AnnotationUserCreatedViewType.BUTTON:
                    designScreenFragment.addNewUserCreatedButton();
                    break;
                case AnnotationUserCreatedViewType.IMAGE_VIEW:
                    designScreenFragment.addNewUserCreatedImageView();
                    int thisViewIndex = designScreenFragment.getNextViewIndex() - 1;
                    UserCreatedView userCreatedView = designScreenFragment.getViews().get(thisViewIndex);
                    PopupWindow propertiesTablePopupWindow = userCreatedView.getPropertiesTablePopupWindow(context);
                    designScreenFragment.presentPopup(propertiesTablePopupWindow);
                    // For temp testing
                    if (UserProfile.user.getCurrentLevel() == Support.POLLY_APP_LEVEL_ID && UserProfile.user.getCurrentTaskNum() == 0) {
                        designScreenFragment.taskCompleted();
                    }
            }
            designScreenFragment.getViewsAndPresent();
        }
    }

    private boolean checkIfTheresRoom() {
        // Check if reached max num of elements.
        int maxNum = designScreenFragment.getGridLayoutColCount() * designScreenFragment.getGridLayoutRowCount();
        int nextViewIndex = designScreenFragment.getNextViewIndex();
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
        LayoutXmlWriter layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeManifest(appIcon, appName);
    }

    public Map<Integer, UserCreatedView> getViewsFromUserProfile() {
        return UserProfile.user.getCurrentUserApp().getViews();
    }

//    public void setUserCreatedViewsModel(UserCreatedViewsModel userCreatedViewsModel) {
//        this.userCreatedViewsModel = userCreatedViewsModel;
//    }
}
