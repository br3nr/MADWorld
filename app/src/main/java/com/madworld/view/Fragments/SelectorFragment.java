package com.madworld.view.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.madworld.R;
import com.madworld.model.structures.Structure;
import com.madworld.model.structures.StructureData;


/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: SelectorFragment is a standard Recycler view that allows the user to
 *          select a structure from a 1d list and put it onto the map.
 *          Calls back to the Map Activity when a structure is clicked.
 */
public class SelectorFragment extends Fragment {

    private Structure currentStructure;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SelectorFragmentListener listener;

    public interface SelectorFragmentListener
    {
        void onStructureClick(Structure structure);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        //INFLATION
        View view = inflater.inflate(R.layout.fragment_selector, ui, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.selector_fragment);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        StructureData sd = new StructureData();
        SelectorAdapter sa = new SelectorAdapter(sd);
        rv.setAdapter(sa);

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof SelectorFragmentListener)
        {
            listener = (SelectorFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Interface was not implemented.");
        }
    }

    public Structure getCurrentStructure()
    {
        return currentStructure;
    }

    /**
     * SELECTOR ADAPTER CLASS
     */

    private class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder>
    {
        private StructureData sd;

        public SelectorAdapter(StructureData sd)
        {
            this.sd = sd;
        }

        public int getItemCount()
        {
            return sd.size();
        }

        @NonNull
        @Override
        public SelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new SelectorViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(SelectorViewHolder svh, int idx)
        {
            svh.bind(sd.get(idx));
        }
    }

    /**
     * SELECTOR VIEW HOLDER CLASS
     */

    private class SelectorViewHolder extends RecyclerView.ViewHolder
    {
        private Structure curStructure;
        private TextView textView;
        private ImageView imageView;

        public SelectorViewHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.list_selection, parent, false));
            textView = (TextView)itemView.findViewById(R.id.list_text);
            imageView = (ImageView)itemView.findViewById(R.id.list_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStructureClick(curStructure);
                }
            });
        }

        public Structure getCurStructure()
        {
            return curStructure;
        }

        public void bind(Structure structure)
        {
            curStructure = structure;
            textView.setText(structure.getLabel());
            imageView.setImageResource(structure.getDrawableId());
        }
    }
}