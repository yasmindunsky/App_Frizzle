package com.example.yasmindunsky.frizzleapp.appBuilder;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphicEditFragment extends Fragment {

    private GridLayout gridLayout;
    private int numOfButtons;
    private int numOfTextViews;
    private PopupWindow popupWindow;
    List<View> views;
    LayoutXmlWriter layoutXmlWriter;

    public GraphicEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graphic_edit, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);

        Button addText = view.findViewById(R.id.addText);
        addText.setOnClickListener(newTextOnClick);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(newButtonOnClick);

        gridLayout.setOnDragListener(dragListener);

        views = new ArrayList<>();
        numOfButtons = 0;
        numOfTextViews = 0;

        return view;
    }

    EditText.OnFocusChangeListener finishedEditingText = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String text = String.valueOf(((EditText)v).getText());

                int editedViewIndex = (int) v.getTag();
                View editedView = ((View)views.get(editedViewIndex));

                if (editedView.getClass().equals(TextView.class)) {
                    TextView tv = (TextView)editedView;
                    tv.setText(text);
                }
                else if (editedView.getClass().equals(Button.class)) {
                    Button b = (Button)editedView;
                    b.setText(text);
                }
            }
        }
    };

    EditText.OnFocusChangeListener finishedEditingId = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String id = String.valueOf(((EditText)v).getText());

                int editedViewIndex = (int) v.getTag();
                View editedView = ((View)views.get(editedViewIndex));

                if (editedView.getClass().equals(TextView.class)) {
                    TextView tv = (TextView)editedView;
                    tv.setTag(R.id.usersViewId, id);
                }
                else if (editedView.getClass().equals(Button.class)) {
                    Button b = (Button)editedView;
                    b.setTag(R.id.usersViewId, id);
                }
            }
        }
    };


    View.OnClickListener newTextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView newText = new TextView(view.getContext());
            newText.setTag(R.id.usersViewId, "TextView" + numOfTextViews);
            newText.setTag(R.id.usersViewFontFamily, "serif");
            newText.setText(R.string.newTextViewText);
            int numOfViews = numOfTextViews + numOfButtons;
            newText.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(numOfViews/2),
                    GridLayout.spec(numOfViews%2)));
            newText.setBackgroundColor(Color.WHITE);
            newText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayPropertiesTable(v);
                }
            });
            newText.setOnLongClickListener(onLongClickListener);
            newText.setTag(numOfViews);
            gridLayout.addView(newText);
            views.add(newText);
            numOfTextViews++;
        }
    };

    View.OnClickListener newButtonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int buttonStyle = R.style.usersButton;
            Button newButton = new Button(new ContextThemeWrapper(getContext(), buttonStyle), null, buttonStyle);
            newButton.setTag(R.id.usersViewId, "Button" + numOfButtons);
            newButton.setTag(R.id.usersViewFontFamily, "serif");
            int numOfViews = numOfTextViews + numOfButtons;
            newButton.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(numOfViews/2),
                    GridLayout.spec(numOfViews%2)));
            newButton.setText(R.string.newButtonText);
            newButton.setOnLongClickListener(onLongClickListener);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayPropertiesTable(v);
                }
            });
            newButton.setTag(numOfViews);
            gridLayout.addView(newButton);
            views.add(newButton);
            numOfButtons++;
        }
    };

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();

            final View view = (View) event.getLocalState();

            switch (dragEvent) {

                case DragEvent.ACTION_DRAG_ENTERED:
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    break;

                case DragEvent.ACTION_DROP:
                    gridLayout.removeView(view);
                    gridLayout.addView(view);
                    break;
            }

            return true;
        }
    };

    private void displayPropertiesTable(final View v) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popwindow, null);

        // create the popup window
        int width = GridLayout.LayoutParams.WRAP_CONTENT;
        int height = GridLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // show the popup window
        popupWindow.showAtLocation(gridLayout, Gravity.CENTER, 0, 0);

        // ID
        EditText viewId = (EditText)popupView.findViewById(R.id.viewIdValue);
        viewId.setTag(v.getTag());
        viewId.setOnFocusChangeListener(finishedEditingId);
        viewId.setText(v.getTag(R.id.usersViewId).toString());

        String text = "";
        Drawable background = null;
        if (v.getClass().equals(TextView.class)) {
            TextView tv = (TextView)v;
            text = (String) tv.getText();
            background = tv.getBackground();
        }
        else if (v.getClass().equals(Button.class)) {
            Button b = (Button)v;
            text = (String) b.getText();
            background = b.getBackground();
        }
        EditText viewText = (EditText)popupView.findViewById(R.id.viewTextValue);
        viewText.setTag(v.getTag());
        viewText.setOnFocusChangeListener(finishedEditingText);
        viewText.setText(text);

        // FONT
        Spinner fontSpinner = (Spinner) popupView.findViewById(R.id.viewFontValue);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.fonts, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        fontSpinner.setAdapter(fontAdapter);
        fontSpinner.setTag(v.getTag());
        fontSpinner.setOnItemSelectedListener(onFontPicked);

        // COLOR
        Spinner colorSpinner = (Spinner) popupView.findViewById(R.id.viewColorValue);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.colors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setTag(v.getTag());
        colorSpinner.setOnItemSelectedListener(onColorPicked);

    }

    AdapterView.OnItemSelectedListener onFontPicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Object tag = parent.getTag();
            if (tag == null) {
                return;
            }

            int editedViewIndex = (int)tag;
            String fontFamily = parent.getItemAtPosition(position).toString();
            View editedView = views.get(editedViewIndex);

            if (editedView.getClass().equals(TextView.class)) {
                TextView tv = (TextView)editedView;
                tv.setTypeface(Typeface.create(fontFamily, Typeface.NORMAL));
                tv.setTag(R.id.usersViewFontFamily, fontFamily);
            }
            else if (editedView.getClass().equals(Button.class)) {
                Button b = (Button)editedView;
                b.setTypeface(Typeface.create(fontFamily, Typeface.NORMAL));
                b.setTag(R.id.usersViewFontFamily, fontFamily);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener onColorPicked = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Object tag = parent.getTag();
            if (tag == null) {
                return;
            }
            int editedViewIndex = (int)tag;
            View editedView = views.get(editedViewIndex);
            String color = parent.getItemAtPosition(position).toString();
            int colorIdentifier = getResources().getColor(getResources().getIdentifier(color, "color", getActivity().getPackageName()));
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.user_button_background);
            drawable.setColorFilter(colorIdentifier, PorterDuff.Mode.DARKEN);
            editedView.setBackground(drawable);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public String getXml(){
        layoutXmlWriter = new LayoutXmlWriter();
        return layoutXmlWriter.writeXml(views);
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
