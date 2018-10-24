package com.frizzl.app.frizzleapp.appBuilder;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.frizzl.app.frizzleapp.AnnotationUserCreatedViewType;
import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;
import com.frizzl.app.frizzleapp.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DesignScreenFragment extends Fragment {
    private GridLayout gridLayout;
    private PopupWindow popupWindow;
    private Map<Integer, UserCreatedView> views;
    private TextView appNameTitle;
    private ImageView appIcon;
    private Tutorial tutorial;
    private AllAngleExpandableButton plusButton;
    private Context context;

    private DesignScreenPresenter designScreenPresenter;
    private AppBuilderActivity appBuilderActivity;
    private UserCreatedViewsModel userCreatedViewsModel;
    private DefinedFunctionsViewModel definedFunctionsViewModel;

    public DesignScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design, container, false);

        FragmentActivity activity = getActivity();
        appBuilderActivity = (AppBuilderActivity) activity;
        context = appBuilderActivity;
        gridLayout = view.findViewById(R.id.gridLayout);
        appNameTitle = view.findViewById(R.id.app_name_title);
        appIcon = view.findViewById(R.id.app_icon);
        plusButton = view.findViewById(R.id.button_expandable);

        gridLayout.setOnDragListener(new DragListener());

        initAddMenu(view);
        tutorial = new Tutorial(getContext());

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        assert activity != null;
        userCreatedViewsModel = ViewModelProviders.of(activity).get(UserCreatedViewsModel.class);

        if (designScreenPresenter != null) {
            setAppName(designScreenPresenter.getAppName());
            setAppIcon(designScreenPresenter.getIcon());
            views = userCreatedViewsModel.getViews();
            if (views == null) {
                views = designScreenPresenter.getViewsFromUserProfile();
                userCreatedViewsModel.setViews(views);
            }
            presentViewsOnGridLayout();
        } else {
            Log.e("APP_BUILDER", "designScreenPresenter was not set.");
        }

        definedFunctionsViewModel = ViewModelProviders.of(getActivity()).get(DefinedFunctionsViewModel.class);

        return view;
    }

    private void initAddMenu(View view) {
        final List<ButtonData> buttonsData = new ArrayList<>();
        int[] drawable = {R.drawable.ic_add_plus, R.drawable.ic_add_text, R.drawable.ic_add_button, R.drawable.ic_add_image};

        Map<Integer, String> indexToViewType = new HashMap<>();
        indexToViewType.put(1, AnnotationUserCreatedViewType.TEXT_VIEW);
        indexToViewType.put(2, AnnotationUserCreatedViewType.BUTTON);
        indexToViewType.put(3, AnnotationUserCreatedViewType.IMAGE_VIEW);

        ButtonData buttonData = ButtonData.buildIconButton(getContext(), drawable[0], 10);
        buttonData.setBackgroundColor(getResources().getColor(R.color.frizzle_light_blue));
        buttonsData.add(buttonData);
        for (int i = 1; i < drawable.length; i++) {
            buttonData = ButtonData.buildIconButton(getContext(), drawable[i], 5);
            buttonData.setBackgroundColor(getResources().getColor(R.color.frizzle_blue));
            buttonsData.add(buttonData);
        }
        plusButton.setButtonDatas(buttonsData);
        plusButton.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                //do whatever you want,the param index is counted from startAngle to endAngle,
                //the value is from 1 to buttonCount - 1(buttonCount if aebIsSelectionMode=true)
                String viewType = indexToViewType.get(index);
                designScreenPresenter.addNewUserCreatedView(getContext(), viewType);
            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapse() {

            }
        });
    }

    private void presentViewsOnGridLayout() {
        gridLayout.removeAllViews();
        for (final UserCreatedView userCreatedView : views.values()) {
            View usersView = userCreatedView.getThisView();

            if (usersView.getParent() != null) {
                ((ViewGroup) usersView.getParent()).removeView(usersView);
            }

            gridLayout.addView(usersView);
            switch (userCreatedView.getViewType()) {
                case AnnotationUserCreatedViewType.BUTTON:
                    final UserCreatedButton userCreatedButton = (UserCreatedButton) userCreatedView;
                    userCreatedButton.setChangedTextListener(new UserCreatedButton.ChangedTextListener() {
                        @Override
                        public void onChangedText() {
                            if (UserProfile.user.getCurrentLevel() == Support.POLLY_APP_LEVEL_ID && UserProfile.user.getCurrentTaskNum()== 1) {
                                appBuilderActivity.taskCompleted();
                            }
                        }

                        @Override
                        public void onChangedOnClick(String onClickFuncName) {
                            if (UserProfile.user.getCurrentLevel() == Support.POLLY_APP_LEVEL_ID
                                    && !onClickFuncName.equals("")) {
                                if (UserProfile.user.getCurrentTaskNum() == 4) {
                                    if (appBuilderActivity != null)
                                        appBuilderActivity.taskCompleted();
                                } else if (UserProfile.user.getCurrentTaskNum() == 5) {
                                    if (appBuilderActivity != null)
                                        appBuilderActivity.taskCompleted();
                                }
                            }
                        }
                    });
                    setButtonOnClicks(userCreatedButton);
                    break;
                case AnnotationUserCreatedViewType.TEXT_VIEW:
                    final UserCreatedTextView userCreatedTextView = (UserCreatedTextView) userCreatedView;
                    setTextOnClicks(userCreatedTextView);
                    break;
                default:
                    final UserCreatedImageView userCreatedImageView = (UserCreatedImageView) userCreatedView;
                    setImageOnClicks(userCreatedImageView);
                    break;
            }
        }
    }

    View.OnClickListener deleteView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            int viewToDeleteIndex = (int) v.getTag();
            userCreatedViewsModel.deleteUserCreatedView(viewToDeleteIndex);
        }
    };

    private void setButtonOnClicks(final UserCreatedButton userCreatedButton){
        Button button = userCreatedButton.getThisView();
        button.setOnLongClickListener(new LongPressListener());

        button.setOnClickListener(v -> {
            // show the popup window
            userCreatedButton.setFunctions(definedFunctionsViewModel.getFunctions());
            popupWindow = userCreatedButton.getPropertiesTablePopupWindow(appBuilderActivity);

            ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
            // To know what view to delete
            deleteButton.setTag(R.id.viewToDelete, v.getTag(R.id.usersViewId));
            deleteButton.setOnClickListener(deleteView);
            v.post(() -> presentPopup(popupWindow));
        });
    }

    public void presentPopup(PopupWindow popupWindow) {
        if (appBuilderActivity != null) appBuilderActivity.presentPopup(popupWindow, null);
    }

    private void setTextOnClicks(final UserCreatedTextView userCreatedTextView){
        TextView textView = userCreatedTextView.getThisView();

        textView.setOnLongClickListener(new LongPressListener());

        textView.setOnClickListener(v -> {
            // show the popup window
            popupWindow = userCreatedTextView.getPropertiesTablePopupWindow(getContext());

            ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
            // To know what view to delete
            deleteButton.setTag(R.id.viewToDelete, v.getId());
            deleteButton.setOnClickListener(deleteView);
            v.post(() -> presentPopup(popupWindow));
        });
    }

    private void setImageOnClicks(final UserCreatedImageView userCreatedImageView){
        ImageView imageView = userCreatedImageView.getThisView();

        imageView.setOnLongClickListener(new LongPressListener());

        imageView.setOnClickListener(v -> {
            // show the popup window
            popupWindow = userCreatedImageView.getPropertiesTablePopupWindow(getContext());

            ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
            // To know what view to delete
            deleteButton.setTag(R.id.viewToDelete, v.getId());
            deleteButton.setOnClickListener(deleteView);
            v.post(() -> presentPopup(popupWindow));
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        designScreenPresenter.saveState();
    }

    @Override
    public void onResume() {
        super.onResume();
        views = userCreatedViewsModel.getViews();
        presentViewsOnGridLayout();
    }

    public void showError(String errorMessage) {
        Toast.makeText(appBuilderActivity, errorMessage, Toast.LENGTH_LONG).show();
    }

    public void getViewsAndPresent() {
        this.views = userCreatedViewsModel.getViews();
        presentViewsOnGridLayout();
    }

    public int getGridLayoutColCount() {
        return gridLayout.getColumnCount();
    }

    public int getGridLayoutRowCount() {
        return gridLayout.getRowCount();
    }

    public Map<Integer, UserCreatedView> getViews() {
        return views;
    }

    public String getXml() {
        return designScreenPresenter.getXml(userCreatedViewsModel.getViews());
    }

    public void setAppName(String appName) {
        if (appName.equals("")) {
            appName = "My Frizzl App";
        }
        appNameTitle.setText(appName);
    }

    public void setAppIcon(String iconDrawable) {
        if (iconDrawable != null && !iconDrawable.equals("")) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            if (appBuilderActivity != null) {
                int iconIdentifier = getResources().getIdentifier(iconDrawable, "drawable", appBuilderActivity.getPackageName());
                Drawable drawable = getResources().getDrawable(iconIdentifier);
                appIcon.setImageDrawable(drawable);
                appIcon.setLayoutParams(layoutParams);
            }
        }
    }

    public void presentTutorialMessage() {
        tutorial.presentTooltip(plusButton, getString(R.string.tooltip_add_elements), null, Gravity.TOP);
    }

    public void taskCompleted() {
        appBuilderActivity.taskCompleted();
    }

    public String getManifest() {
        return designScreenPresenter.getManifest();
    }

    public void setPresenter(DesignScreenPresenter designScreenPresenter) {
        this.designScreenPresenter = designScreenPresenter;
    }

    public UserCreatedViewsModel getUserCreatedViewsModel() {
        return userCreatedViewsModel;
    }

    public int getNumOfButtons() {
        return userCreatedViewsModel.getNumOfButtons();
    }

    public int getNumOfTextViews() {
        return userCreatedViewsModel.getNumOfTextViews();
    }

    public int getNextViewIndex() {
        return userCreatedViewsModel.getNextViewIndex();
    }

    public int getNumOfImageViews() {
        return userCreatedViewsModel.getNumOfImageViews();
    }

    public void addNewUserCreatedTextView() {
        userCreatedViewsModel.addNewUserCreatedTextView(context);
    }

    public void addNewUserCreatedButton() {
        userCreatedViewsModel.addNewUserCreatedButton(appBuilderActivity);
    }

    public void addNewUserCreatedImageView() {
        userCreatedViewsModel.addNewUserCreatedImageView(context);
    }

    class LongPressListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    // do nothing if hovering above own position
                    if (view == v) return true;
                    break;
                case DragEvent.ACTION_DROP:
                    // remove the view from the old position
                    gridLayout.removeView(view);
                    // and push to the new
                    GridLayout.LayoutParams layoutParams = getPostDragLayoutParams(event.getX(), event.getY(), view);
                    view.setLayoutParams(layoutParams);
                    gridLayout.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        }

        private GridLayout.LayoutParams getPostDragLayoutParams(float x, float y, View view) {
            // calculate which column to move to
            final float cellWidth = gridLayout.getWidth() / gridLayout.getColumnCount();
            int column = (int) (x / cellWidth);

            // calculate which row to move to
            final float cellHeight = gridLayout.getHeight() / gridLayout.getRowCount();
            final int row = (int) Math.floor(y / cellHeight);

            // RTL X coordinates are flipped.
//            if (Support.isRTL()) {
//                column =  gridLayout.getColumnCount() - column - 1;
//            }

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),  GridLayout.spec(column));
            layoutParams.setMargins(10, 10, 10, 10);
            layoutParams.width = (int) getResources().getDimension(R.dimen.user_created_button_width);
            if (view.getClass().equals(ImageView.class)){
                layoutParams.width = (int) getResources().getDimension(R.dimen.user_created_image_view_width);
                layoutParams.height = (int) getResources().getDimension(R.dimen.user_created_image_view_width);
            }

            view.setTag(R.id.usersViewRow, row);
            view.setTag(R.id.usersViewCol, column);
            return layoutParams;
        }
    }
}
