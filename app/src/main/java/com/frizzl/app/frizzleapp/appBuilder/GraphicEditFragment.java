package com.frizzl.app.frizzleapp.appBuilder;

import android.content.ClipData;
import android.content.Context;
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
import com.frizzl.app.frizzleapp.UserProfile;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphicEditFragment extends Fragment {

    private GridLayout gridLayout;
    private int numOfButtons;
    private int numOfTextViews;
    private int nextViewIndex;
    private PopupWindow popupWindow;
    Map<Integer, UserCreatedView> views;
    LayoutXmlWriter layoutXmlWriter;
    enum viewTypes {Button, TextView}

    private ExpandableLayout expandableLayout;
    private FloatingActionButton expandButton;

    public GraphicEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graphic_edit, container, false);

        gridLayout = view.findViewById(R.id.gridLayout);
        gridLayout.setOnDragListener(new DragListener());

//        views = UserProfile.user.getViews();
        views = UserProfile.user.views;
        if (views.isEmpty()) {
            numOfButtons = 0;
            numOfTextViews = 0;
            nextViewIndex = 0;

            if (1 == UserProfile.user.getTopLessonID()) {
                // Add hello world view
                // TODO: this is a duplicate of the onclick create new button, unite those.
                final UserCreatedButton userCreatedButton = new UserCreatedButton(getContext(), nextViewIndex, numOfButtons);
                Button button = userCreatedButton.getThisView();
                setButtonOnClicks(userCreatedButton);

                // Add to GridLayout and to views map.
                gridLayout.addView(button);
                views.put(nextViewIndex, userCreatedButton);
                nextViewIndex++;
                numOfTextViews++;
            }
        }
        else {
            // Load previously created views.
            for (final UserCreatedView userCreatedView : views.values()) {
                View usersView = userCreatedView.getThisView();

                if(usersView.getParent() != null) {
                    ((ViewGroup) usersView.getParent()).removeView(usersView);
                }

                gridLayout.addView(usersView);
                if (userCreatedView.getViewType().equals(UserCreatedView.ViewType.Button)){
                    numOfButtons++;

                    final UserCreatedButton userCreatedButton = (UserCreatedButton) userCreatedView;
                    setButtonOnClicks(userCreatedButton);
                }
                else {
                    numOfTextViews++;
                    final UserCreatedTextView userCreatedTextView = (UserCreatedTextView) userCreatedView;
                    setTextOnClicks(userCreatedTextView);
                }
                nextViewIndex++;
            }
        }

        TextView addText = view.findViewById(R.id.addText);
        addText.setOnClickListener(newTextOnClick);

        TextView addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(newButtonOnClick);

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

    View.OnClickListener newTextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Check if reached max num of elements.
            if (nextViewIndex >= gridLayout.getColumnCount() * gridLayout.getRowCount()) {
                Toast.makeText(getActivity(), "אופס, נגמר המקום!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            final UserCreatedTextView userCreatedTextView = new UserCreatedTextView(getContext(), nextViewIndex, numOfTextViews);
            TextView newText = userCreatedTextView.getThisView();
            setTextOnClicks(userCreatedTextView);

            // Add to GridLayout and to views map.
            gridLayout.addView(newText);
            views.put(nextViewIndex, userCreatedTextView);
            nextViewIndex++;
            numOfTextViews++;
        }
    };

    View.OnClickListener newButtonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Check if reached max num of elements.
            if (nextViewIndex >= gridLayout.getColumnCount() * gridLayout.getRowCount()) {
                Toast.makeText(getActivity(), "אופס, נגמר המקום!",
                        Toast.LENGTH_LONG).show();
                return;
            }

            final UserCreatedButton userCreatedButton = new UserCreatedButton(getContext(), nextViewIndex, numOfButtons);
            Button newButton = userCreatedButton.getThisView();

            setButtonOnClicks(userCreatedButton);

            // Add to GridLayout and to views map.
            gridLayout.addView(newButton);
            views.put(nextViewIndex, userCreatedButton);
            nextViewIndex++;
            numOfButtons++;
        }
    };

    View.OnClickListener deleteView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            int viewToDeleteIndex = (int) v.getTag();
            UserCreatedView viewToDelete = views.get(viewToDeleteIndex);

            if (viewToDelete.getClass().equals(UserCreatedTextView.class)) {
                numOfTextViews--;
            }
            else if (viewToDelete.getClass().equals(UserCreatedButton.class)) {
                numOfButtons--;
            }

            View thisView = viewToDelete.getThisView();
            ((ViewGroup)thisView.getParent()).removeView(thisView);
            views.remove(viewToDeleteIndex);
        }
    };

    private void setButtonOnClicks(final UserCreatedButton userCreatedButton){
        Button newButton = userCreatedButton.getThisView();
        newButton.setOnLongClickListener(new LongPressListener());

        newButton.setOnClickListener(new View.OnClickListener() {
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
        TextView newText = userCreatedTextView.getThisView();

        newText.setOnLongClickListener(new LongPressListener());

        newText.setOnClickListener(new View.OnClickListener() {
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

    public String getXml(){
        layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeXml(views);
    }

    public Map<Integer, UserCreatedView> getViews() {
        return views;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
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

    JSONObject viewsToJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject finalObject = new JSONObject();

        views = UserProfile.user.getViews();

        if(views != null) {
            try {
                for (int i = 0; i < views.size(); i++) {
                    json = new JSONObject();
                    json.put("id", i);
                    UserCreatedView userCreatedView = views.get(i);
                    json.put("viewType", userCreatedView.getViewType().toString());
                    Map<String, String> properties = userCreatedView.getProperties();
                    for (Map.Entry<String, String> property : properties.entrySet()) {
                        json.put(property.getKey(), property.getValue());
                    }
                    jsonArray.put(json);
                }

                finalObject.put("views", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return finalObject;
    }

    public Map<Integer, UserCreatedView> jsonToViews(Context context, String viewsJsonString) {
        views = new HashMap<>();
        numOfButtons = 0;
        numOfTextViews = 0;
        nextViewIndex = 0;

        try {
            JSONArray viewsJson = new JSONArray(viewsJsonString);
            for(int i = 0 ; i < viewsJson.length(); i++){
                JSONObject viewJson = viewsJson.getJSONObject(i);
                UserCreatedView.ViewType viewType = getViewType(viewJson.getString("viewType"));
                int index = Integer.parseInt(viewJson.getString("id"));
                Map<String,String> properties = new HashMap<>();
                for (Iterator<String> it = viewJson.keys(); it.hasNext(); ) {
                    String key = it.next();
                    properties.put(key.toString(), viewJson.get(key).toString());
                }
                UserCreatedView userCreatedView = null;
                switch (viewType) {
                    case TextView:
                        userCreatedView = new UserCreatedTextView(context, properties, i);
                        numOfTextViews++;
                        break;
                    case Button:
                        userCreatedView = new UserCreatedButton(context, properties, i);
                        numOfButtons++;
                        break;
                }
                views.put(index, userCreatedView);
            }
            nextViewIndex = viewsJson.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return views;
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
