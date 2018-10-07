package com.frizzl.app.frizzleapp.appBuilder;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private DesignScreenPresenter designScreenPresenter;
    private TextView appNameTitle;
    private ImageView appIcon;
    private Tutorial tutorial;
    private AllAngleExpandableButton plusButton;
    private AppBuilderActivity appBuilderActivity;

    public DesignScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design, container, false);

        appBuilderActivity = (AppBuilderActivity) getActivity();
        gridLayout = view.findViewById(R.id.gridLayout);
        appNameTitle = view.findViewById(R.id.app_name_title);
        appIcon = view.findViewById(R.id.app_icon);
        plusButton = view.findViewById(R.id.button_expandable);

        gridLayout.setOnDragListener(new DragListener());

        initAddMenu(view);
        tutorial = new Tutorial(getContext());

        if (designScreenPresenter != null) {
            setAppName(designScreenPresenter.getAppName());
            setAppIcon(designScreenPresenter.getIcon());

            views = designScreenPresenter.getViews(getContext());
            presentViewsOnGridLayout();
        } else {
            Log.e("APP_BUILDER", "designScreenPresenter was not set.");
        }

        return view;
    }

    private void initAddMenu(View view) {
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.ic_add_plus, R.drawable.ic_add_text, R.drawable.ic_add_button, R.drawable.ic_add_image};

        Map<Integer, String> indexToViewType = new HashMap<>();
        indexToViewType.put(1, AnnotationUserCreatedViewType.TEXT_VIEW);
        indexToViewType.put(2, AnnotationUserCreatedViewType.BUTTON);
        indexToViewType.put(3, AnnotationUserCreatedViewType.IMAGE_VIEW);

        ButtonData buttonData = ButtonData.buildIconButton(getContext(), drawable[0], 10);
        buttonData.setBackgroundColor(getResources().getColor(R.color.frizzle_light_blue));
        buttonDatas.add(buttonData);
        for (int i = 1; i < drawable.length; i++) {
            buttonData = ButtonData.buildIconButton(getContext(), drawable[i], 5);
            buttonData.setBackgroundColor(getResources().getColor(R.color.frizzle_blue));
            buttonDatas.add(buttonData);
        }
        plusButton.setButtonDatas(buttonDatas);
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
            if (userCreatedView.getViewType().equals(AnnotationUserCreatedViewType.BUTTON)) {
                final UserCreatedButton userCreatedButton = (UserCreatedButton) userCreatedView;
                setButtonOnClicks(userCreatedButton);
            } else if (userCreatedView.getViewType().equals(AnnotationUserCreatedViewType.TEXT_VIEW)) {
                final UserCreatedTextView userCreatedTextView = (UserCreatedTextView) userCreatedView;
                setTextOnClicks(userCreatedTextView);
            } else {
                final UserCreatedImageView userCreatedImageView = (UserCreatedImageView)userCreatedView;
                setImageOnClicks(userCreatedImageView);
            }
        }
    }

    View.OnClickListener deleteView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            int viewToDeleteIndex = (int) v.getTag();
            DesignScreenPresenter.deleteView(viewToDeleteIndex);
        }
    };

    private void setButtonOnClicks(final UserCreatedButton userCreatedButton){
        Button button = userCreatedButton.getThisView();
        button.setOnLongClickListener(new LongPressListener());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // show the popup window
                popupWindow = userCreatedButton.getPropertiesTablePopupWindow(getContext());

                ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
                // To know what view to delete
                deleteButton.setTag(R.id.viewToDelete, v.getTag(R.id.usersViewId));
                deleteButton.setOnClickListener(deleteView);
                v.post(new Runnable() {
                    public void run() {
                        presentPopup(popupWindow);
                    }
                });
            }
        });
    }

    public void presentPopup(PopupWindow popupWindow) {
        AppBuilderActivity activity = (AppBuilderActivity)getActivity();
        activity.presentPopup(popupWindow, null);
    }

    private void setTextOnClicks(final UserCreatedTextView userCreatedTextView){
        TextView textView = userCreatedTextView.getThisView();

        textView.setOnLongClickListener(new LongPressListener());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // show the popup window
                popupWindow = userCreatedTextView.getPropertiesTablePopupWindow(getContext());

                ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
                // To know what view to delete
                deleteButton.setTag(R.id.viewToDelete, v.getId());
                deleteButton.setOnClickListener(deleteView);
                v.post(new Runnable() {
                    public void run() {
                        presentPopup(popupWindow);
                    }
                });
            }
        });
    }

    private void setImageOnClicks(final UserCreatedImageView userCreatedImageView){
        ImageView imageView = userCreatedImageView.getThisView();

        imageView.setOnLongClickListener(new LongPressListener());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // show the popup window
                popupWindow = userCreatedImageView.getPropertiesTablePopupWindow(getContext());

                ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
                // To know what view to delete
                deleteButton.setTag(R.id.viewToDelete, v.getId());
                deleteButton.setOnClickListener(deleteView);
                v.post(new Runnable() {
                    public void run() {
                        presentPopup(popupWindow);
                    }
                });
            }
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
        views = DesignScreenPresenter.getViews(getContext());
        presentViewsOnGridLayout();
    }

    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    public void getViewsAndPresent(Map<Integer, UserCreatedView> views) {
        this.views = views;
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
        return designScreenPresenter.getXml();
    }

    public void setAppName(String appName) {
        if (appName.equals("")) {
            appName = "My Frizzl App";
        }
        appNameTitle.setText(appName);
    }

    public void setAppIcon(String iconDrawable) {
        if (!iconDrawable.equals("") && iconDrawable != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            int iconIdentifier = getResources().getIdentifier(iconDrawable, "drawable", getActivity().getPackageName());
            Drawable drawable = getResources().getDrawable(iconIdentifier);
            appIcon.setImageDrawable(drawable);
            appIcon.setLayoutParams(layoutParams);
        }
    }

    public void presentTuturialMessage() {
        tutorial.presentTooltip(plusButton, "Here you can\n add elements.", null, Gravity.TOP);
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
