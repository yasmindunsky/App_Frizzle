package com.example.yasmindunsky.frizzleapp.appBuilder;


import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;


import com.example.yasmindunsky.frizzleapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphicEditFragment extends Fragment {

    TableLayout tableLayout;

    public GraphicEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graphic_edit, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);

        Button addText = view.findViewById(R.id.addText);
        addText.setOnClickListener(newTextOnClick);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(newButtonOnClick);

        tableLayout.setOnDragListener(dragListener);

        return view;
    }

    View.OnClickListener newTextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView newText = new TextView(view.getContext());
            newText.setText("NEW TEXT");
            newText.setBackgroundColor(Color.WHITE);
            newText.setOnLongClickListener(onLongClickListener);
            tableLayout.addView(newText);
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
                    tableLayout.removeView(view);
                    tableLayout.addView(view);
                    break;
            }

            return true;
        }
    };

    View.OnClickListener newButtonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button newButton = new Button(view.getContext());
            newButton.setText("NEW BUTTON");
            newButton.setOnLongClickListener(onLongClickListener);

            tableLayout.addView(newButton);
        }
    };

}
