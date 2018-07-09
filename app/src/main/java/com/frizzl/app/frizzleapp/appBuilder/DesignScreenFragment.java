package com.frizzl.app.frizzleapp.appBuilder;

import android.content.ClipData;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.frizzl.app.frizzleapp.R;
import com.frizzl.app.frizzleapp.Support;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DesignScreenFragment extends Fragment {
    private GridLayout gridLayout;
    private PopupWindow popupWindow;
    Map<Integer, UserCreatedView> views;

    private ExpandableLayout expandableLayout;
    private FloatingActionButton expandButton;

    private UserCreatedViewsPresenter userCreatedViewsPresenter;

    public DesignScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphic_edit, container, false);

        userCreatedViewsPresenter = new UserCreatedViewsPresenter(this);

        gridLayout = view.findViewById(R.id.gridLayout);
        gridLayout.setOnDragListener(new DragListener());

        views = UserCreatedViewsPresenter.getViews(getContext());
        presentViewsOnGridLayout();

        TextView addText = view.findViewById(R.id.addText);
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCreatedViewsPresenter.addNewUserCreatedView(getContext(), UserCreatedViewsPresenter.viewTypes.TextView);
            }
        });

        TextView addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCreatedViewsPresenter.addNewUserCreatedView(getContext(), UserCreatedViewsPresenter.viewTypes.Button);
            }
        });

        expandableLayout = view.findViewById(R.id.plusExpandableLayout);
        // Set LinearLayout direction to be opposite from device's direction.
        // This is needed for correct expand animation direction.
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        int currentDirection = view.getLayoutDirection();
        if (currentDirection == View.LAYOUT_DIRECTION_LTR) {
            linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        expandButton = view.findViewById(R.id.addView);
        expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                expandButton.setRotation(expansionFraction * 135);
            }
        });
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();
            }
        });
        return view;
    }

    private void presentViewsOnGridLayout() {
        gridLayout.removeAllViews();
        for (final UserCreatedView userCreatedView : views.values()) {
            View usersView = userCreatedView.getThisView();

            if (usersView.getParent() != null) {
                ((ViewGroup) usersView.getParent()).removeView(usersView);
            }

            gridLayout.addView(usersView);
            if (userCreatedView.getViewType().equals(UserCreatedView.ViewType.Button)) {
                final UserCreatedButton userCreatedButton = (UserCreatedButton) userCreatedView;
                setButtonOnClicks(userCreatedButton);
            } else {
                final UserCreatedTextView userCreatedTextView = (UserCreatedTextView) userCreatedView;
                setTextOnClicks(userCreatedTextView);
            }
        }
    }

    View.OnClickListener deleteView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            int viewToDeleteIndex = (int) v.getTag();
            UserCreatedViewsPresenter.deleteView(viewToDeleteIndex);
        }
    };

    private void setButtonOnClicks(final UserCreatedButton userCreatedButton){
        Button button = userCreatedButton.getThisView();
        button.setOnLongClickListener(new LongPressListener());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // show the popup window
                popupWindow = userCreatedButton.displayPropertiesTable(getContext());

                ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
                // To know what view to delete
                deleteButton.setTag(R.id.viewToDelete, v.getTag(R.id.usersViewId));
                deleteButton.setOnClickListener(deleteView);
                v.post(new Runnable() {
                    public void run() {
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                    }
                });
            }
        });
    }

    private void setTextOnClicks(final UserCreatedTextView userCreatedTextView){
        TextView textView = userCreatedTextView.getThisView();

        textView.setOnLongClickListener(new LongPressListener());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // show the popup window
                popupWindow = userCreatedTextView.displayPropertiesTable(getContext());

                ImageButton deleteButton = popupWindow.getContentView().findViewById(R.id.delete);
                // To know what view to delete
                deleteButton.setTag(R.id.viewToDelete, v.getId());
                deleteButton.setOnClickListener(deleteView);
                v.post(new Runnable() {
                    public void run() {
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
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
        userCreatedViewsPresenter.saveState();
    }

    public void showError(String errorMessage) {
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    public void getViewsAndPresent(Map<Integer, UserCreatedView> views) {
        views = views;
        presentViewsOnGridLayout();
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
                    // get the new list index
//                    final int index = calculateNewIndex(event.getX(), event.getY());
                    break;
                case DragEvent.ACTION_DROP:
                    // remove the view from the old position
                    gridLayout.removeView(view);
                    // and push to the new
                    GridLayout.LayoutParams layoutParams = getLayoutParams(event.getX(), event.getY(), view);

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

        private GridLayout.LayoutParams getLayoutParams(float x, float y, View view) {
            // calculate which column to move to
            final float cellWidth = gridLayout.getWidth() / gridLayout.getColumnCount();
            int column = (int) (x / cellWidth);

            // calculate which row to move to
            final float cellHeight = gridLayout.getHeight() / gridLayout.getRowCount();
            final int row = (int) Math.floor(y / cellHeight);

            // RTL X coordinates are flipped.
            if (Support.isRTL()) {
                column =  gridLayout.getColumnCount() - column - 1;
            }

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),  GridLayout.spec(column));
            layoutParams.setMargins(10, 10, 10, 10);
            layoutParams.width = (int) getResources().getDimension(R.dimen.user_created_button_width);

            view.setTag(R.id.usersViewRow, row);
            view.setTag(R.id.usersViewCol, column);
            return layoutParams;
        }
    }


    public UserCreatedView.ViewType getViewType(String viewType) {
        switch (viewType) {
            case "Button":
                return UserCreatedView.ViewType.Button;
            case "TextView":
                return UserCreatedView.ViewType.TextView;
        }
        return null;
    }
}
