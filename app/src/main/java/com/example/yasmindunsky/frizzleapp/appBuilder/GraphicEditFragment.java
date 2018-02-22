package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yasmindunsky.frizzleapp.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;
import java.util.Map;

import petrov.kristiyan.colorpicker.ColorPicker;

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
    enum viewTypes {Button, TextView};

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

        if (views == null) {
            views = new HashMap<>();
            numOfButtons = 0;
            numOfTextViews = 0;
            nextViewIndex = 0;
        }
        else {
            // Load previously created views.
            for (UserCreatedView userCreatedView : views.values()) {
                View usersView = userCreatedView.getThisView();
                ((ViewGroup)usersView.getParent()).removeView(usersView);
                gridLayout.addView(usersView);
            }
        }

        TextView addText = view.findViewById(R.id.addText);
        addText.setOnClickListener(newTextOnClick);

        TextView addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(newButtonOnClick);

        expandableLayout = view.findViewById(R.id.expandable_layout);
        expandButton = view.findViewById(R.id.addView);

        expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout", "State: " + state);
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

//    EditText.OnFocusChangeListener finishedEditingText = new View.OnFocusChangeListener() {
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            if (!hasFocus) {
//                String text = String.valueOf(((EditText)v).getText());
//
//                int editedViewIndex = (int) v.getTag();
//                View editedView = views.get(editedViewIndex);
//
//                if (editedView.getClass().equals(TextView.class)) {
//                    TextView tv = (TextView)editedView;
//                    tv.setText(text);
//                }
//                else if (editedView.getClass().equals(Button.class)) {
//                    Button b = (Button)editedView;
//                    b.setText(text);
//                }
//            }
//        }
//    };

//    EditText.OnFocusChangeListener finishedEditingId = new View.OnFocusChangeListener() {
//        @Override
//        public void onFocusChange(View v, boolean hasFocus) {
//            if (!hasFocus) {
//                String id = String.valueOf(((EditText)v).getText());
//
//                int editedViewIndex = (int) v.getTag();
//                View editedView = views.get(editedViewIndex);
//
//                if (editedView.getClass().equals(TextView.class)) {
//                    TextView tv = (TextView)editedView;
//                    tv.setTag(R.id.usersViewId, id);
//                }
//                else if (editedView.getClass().equals(Button.class)) {
//                    Button b = (Button)editedView;
//                    b.setTag(R.id.usersViewId, id);
//                }
//            }
//        }
//    };
//

    View.OnClickListener newTextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final UserCreatedTextView userCreatedTextView = new UserCreatedTextView(getContext(), nextViewIndex, numOfTextViews);
            TextView newText = userCreatedTextView.getThisView();

            newText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show the popup window
                    popupWindow = userCreatedTextView.displayPropertiesTable(getContext());

                    Button deleteButton = popupWindow.getContentView().findViewById(R.id.deleteButton);
                    // To know what view to delete
                    deleteButton.setTag(R.id.viewToDelete, v.getId());
                    deleteButton.setOnClickListener(deleteView);
                    popupWindow.showAtLocation(gridLayout, Gravity.CENTER, 0, 0);
                }
            });

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
            final UserCreatedButton userCreatedButton = new UserCreatedButton(getContext(), nextViewIndex, numOfButtons);
            Button newText = userCreatedButton.getThisView();

            newText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show the popup window
                    popupWindow = userCreatedButton.displayPropertiesTable(getContext());

                    Button deleteButton = popupWindow.getContentView().findViewById(R.id.deleteButton);
                    // To know what view to delete
                    deleteButton.setTag(R.id.viewToDelete, v.getTag(R.id.usersViewId));
                    deleteButton.setOnClickListener(deleteView);
                    popupWindow.showAtLocation(gridLayout, Gravity.CENTER, 0, 0);
                }
            });

            // Add to GridLayout and to views map.
            gridLayout.addView(newText);
            views.put(nextViewIndex, userCreatedButton);
            nextViewIndex++;
            numOfButtons++;

//            int buttonStyle = R.style.usersButton;
//            Button newButton = new Button(new ContextThemeWrapper(getContext(), buttonStyle), null, buttonStyle);
//
//            // Set Position in GridLayout and Margins.
//            int row = nextViewIndex / 2;
//            int col = nextViewIndex % 2;
//            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(row),
//                    GridLayout.spec(col));
//            layoutParams.setMargins(10,10,10,10);
//            newButton.setLayoutParams(layoutParams);
//
//            // Set properties.
//             newButton.setText(R.string.newButtonText);
//
//            // Set properties as tags.
//            newButton.setTag(nextViewIndex);
//            newButton.setTag(R.id.viewType, viewTypes.Button);
//            newButton.setTag(R.id.usersViewId, "Button" + numOfButtons);
//            newButton.setTag(R.id.usersViewFontFamily, "serif");
//            newButton.setTag(R.id.usersViewRow, row);
//            newButton.setTag(R.id.usersViewCol, col);
//            newButton.setTag(R.id.usersViewBgColor, "white");
//
//            newButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    displayPropertiesTable(v, viewTypes.Button);
//                }
//            });
//
//            // Add to GridLayout and to views map.
//            gridLayout.addView(newButton);
//            views.put(nextViewIndex, newButton);
//            nextViewIndex++;
//            numOfButtons++;
        }
    };
//
//    private void displayPropertiesTable(final View v, viewTypes textView) {
//        // inflate the layout of the popup window
//        LayoutInflater inflater = (LayoutInflater)
//                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Map<viewTypes, Integer> viewTypesLayoutMap = new HashMap<>();
//        viewTypesLayoutMap.put(viewTypes.TextView, (Integer)R.layout.popup_properties_text_view);
//        viewTypesLayoutMap.put(viewTypes.Button, (Integer)R.layout.popup_properties_button);
//        Integer layout = viewTypesLayoutMap.get(textView);
//        View popupView = inflater.inflate(layout, null);
//
//        // create the popup window
//        int width = GridLayout.LayoutParams.WRAP_CONTENT;
//        int height = GridLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        popupWindow = new PopupWindow(popupView, width, height, focusable);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//
//        // show the popup window
//        popupWindow.showAtLocation(gridLayout, Gravity.CENTER, 0, 0);
//
//        // ID
//        EditText viewId = (EditText)popupView.findViewById(R.id.viewIdValue);
//        viewId.setTag(v.getTag());
//        viewId.setOnFocusChangeListener(finishedEditingId);
//        viewId.setText(v.getTag(R.id.usersViewId).toString());
//
//        String text = "";
//        Drawable background = null;
//        if (v.getClass().equals(TextView.class)) {
//            TextView tv = (TextView)v;
//            text = (String) tv.getText();
//            background = tv.getBackground();
//        }
//        else if (v.getClass().equals(Button.class)) {
//            Button b = (Button)v;
//            text = (String) b.getText();
//            background = b.getBackground();
//        }
//        EditText viewText = (EditText)popupView.findViewById(R.id.viewTextValue);
//        viewText.setTag(v.getTag());
//        viewText.setOnFocusChangeListener(finishedEditingText);
//        viewText.setText(text);
//
//        // FONT
//        Spinner fontSpinner = (Spinner) popupView.findViewById(R.id.viewFontValue);
//        // Create an ArrayAdapter using the string array and a default spinner layout.
//        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.fonts, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        fontSpinner.setAdapter(fontAdapter);
//        fontSpinner.setTag(v.getTag());
//        fontSpinner.setOnItemSelectedListener(onFontPicked);
//
//        // BG COLOR
//        Spinner colorSpinner = (Spinner) popupView.findViewById(R.id.viewFontColorValue);
//        // Create an ArrayAdapter using the string array and a default spinner layout.
//        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.colors, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        colorSpinner.setAdapter(colorAdapter);
//        colorSpinner.setTag(v.getTag());
//        colorSpinner.setOnItemSelectedListener(onColorPicked);
//
//        //DELETE
//        Button deleteButton = (Button)popupView.findViewById(R.id.deleteButton);
//        deleteButton.setTag(v.getTag());
//        deleteButton.setOnClickListener(deleteView);
//    }

    View.OnClickListener deleteView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            int viewToDeleteIndex = (int) v.getTag();
            UserCreatedView viewToDelete = views.get(viewToDeleteIndex);

            if (viewToDelete.getClass().equals(UserCreatedTextView.class)) {
                numOfTextViews--;
            }
            else if (viewToDelete.getClass().equals(Button.class)) {
                numOfButtons--;
            }

            gridLayout.removeView(viewToDelete.getThisView());
            views.remove(viewToDeleteIndex);
        }
    };

//    AdapterView.OnItemSelectedListener onFontPicked = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Object tag = parent.getTag();
//            if (tag == null) {
//                return;
//            }
//
//            int editedViewIndex = (int)tag;
//            String fontFamily = parent.getItemAtPosition(position).toString();
//            View editedView = views.get(editedViewIndex);
//
//            if (editedView.getClass().equals(TextView.class)) {
//                TextView tv = (TextView)editedView;
//                tv.setTypeface(Typeface.create(fontFamily, Typeface.NORMAL));
//                tv.setTag(R.id.usersViewFontFamily, fontFamily);
//            }
//            else if (editedView.getClass().equals(Button.class)) {
//                Button b = (Button)editedView;
//                b.setTypeface(Typeface.create(fontFamily, Typeface.NORMAL));
//                b.setTag(R.id.usersViewFontFamily, fontFamily);
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };

//    AdapterView.OnItemSelectedListener onColorPicked = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            Object tag = parent.getTag();
//            if (tag == null) {
//                return;
//            }
//            int editedViewIndex = (int)tag;
//            View editedView = views.get(editedViewIndex);
//            String color = parent.getItemAtPosition(position).toString();
//            String fullColorName = getColorFromString(color);
//            int colorIdentifier = getResources().getColor(getResources().getIdentifier(fullColorName, "color", getActivity().getPackageName()));
//            viewTypes viewType = (viewTypes) editedView.getTag(R.id.viewType);
//            int originalDrawable = 0;
//            switch (viewType) {
//                case Button:
//                    originalDrawable = R.drawable.user_button_background;
//                    break;
//                case TextView:
//                    originalDrawable = R.drawable.user_text_view_background;
//                    break;
//                default:
//                    Log.e("Error", "Wrong viewType: " + viewType);
//            }
//            Drawable drawable = ContextCompat.getDrawable(getActivity(), originalDrawable);
//            drawable.setColorFilter(colorIdentifier, PorterDuff.Mode.DARKEN);
//            editedView.setBackground(drawable);
//            editedView.setTag(R.id.usersViewBgColor, fullColorName);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//
//        }
//    };

//    private String getColorFromString(String color) {
//        Map<String, String> colorNamesMap =  new HashMap<>();
//        colorNamesMap.put("white", "white");
//        colorNamesMap.put("red", "holo_red_light");
//        colorNamesMap.put("blue", "holo_blue_light");
//        colorNamesMap.put("orange", "holo_orange_light");
//        colorNamesMap.put("green", "holo_green_light");
//
//        return colorNamesMap.get(color);
//
//    }

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
}
