package com.madworld.view.Fragments;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// UI stuff
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

// Mad world models
import com.madworld.model.MapElement;
import com.madworld.model.GameData;
import com.madworld.R;

/* AUTHOR: Max Barker
 * ID: 19624729
 * PURPOSE: MapFragment is the standard implementation of a Recycler view
 *          fragment however it has been adjusted to work with the 2D map.
 *
 *          Will display each grid cell and structures on the map, and update
 *          the data where appropriate.
 */
public class MapFragment extends Fragment
{
    private MapAdapter mapAdpt;

    // Listens for map clicks and calls the mapClickListener in MapActivity
    public interface MapFragmentListener
    {
        public void mapClickListener(MapElement e);
    }

    MapFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.fragment_map, ui, false);

        // Do all the standard Adapter ViewHolder stuff
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.map_fragment);
        GameData map = GameData.get();
        mapAdpt = new MapAdapter(map);
        GameData gd = GameData.get();
        rv.setAdapter(mapAdpt);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), gd.getHEIGHT(), GridLayoutManager.HORIZONTAL, false));
        return view;
    }

    public void updateData()
    {
        mapAdpt.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof MapFragmentListener)
        {
            listener = (MapFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Interface was not implemented.");
        }
    }


    /**
     * ADAPTER IMPLEMENTATION
     */
    private class MapAdapter extends RecyclerView.Adapter<MapViewHolder>
    {
        private GameData md;

        public MapAdapter(GameData md)
        {
            this.md = md;
        }

        public int getItemCount()
        {
            return md.getHEIGHT() * md.getWIDTH();
        }

        @NonNull
        @Override
        // ViewHolder onCreateView
        public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new MapViewHolder(li, parent);
        }

        @Override
        // Viewholder Binder
        public void onBindViewHolder(MapViewHolder mvh, int idx)
        {
            int row = idx% md.getHEIGHT();
            int col = idx/ md.getHEIGHT();
            mvh.bind(md.get(row,col));
        }
    }

    /**
     * VIEW HOLDER CLASS
     */
    private class MapViewHolder extends RecyclerView.ViewHolder
    {
        private MapElement curElement;
        private ImageView grid1;
        private ImageView grid2;
        private ImageView grid3;
        private ImageView grid4;
        private ImageView gridS;


        // VIEWHOLDER CONSTRUCTOR
        public MapViewHolder(LayoutInflater li, ViewGroup parent)
        {
            super(li.inflate(R.layout.grid_cells, parent, false));
            GameData gd = GameData.get();

            // ENSURE CORRECTLY SIZED RECYCLERVIEW
            int size = parent.getMeasuredHeight()/ gd.getHEIGHT()+1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            // INFLATE THEE DATA
            grid1 = (ImageView)itemView.findViewById(R.id.grid1);
            grid2 = (ImageView)itemView.findViewById(R.id.grid2);
            grid3 = (ImageView)itemView.findViewById(R.id.grid3);
            grid4 = (ImageView)itemView.findViewById(R.id.grid4);
            gridS = (ImageView)itemView.findViewById(R.id.gridS);

            // REACT WHEN THE GRID ELEMENT IS CLICKED
            gridS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.mapClickListener(curElement);
                    getBindingAdapter().notifyItemChanged(getAdapterPosition());
                }
            });
        }

        // BIND IMPLEMENTATION
        public void bind(MapElement mapElement)
        {
            // SET THE IMAGE RESOURCES
            curElement = mapElement;
            grid1.setImageResource(mapElement.getNorthWest());
            grid2.setImageResource(mapElement.getNorthEast());
            grid3.setImageResource(mapElement.getSouthWest());
            grid4.setImageResource(mapElement.getSouthEast());

            // DECIDE IF THE IMAGE IS THE INT RESOURCE OR THE BITMAP
            if(mapElement.getImageBitmap() != null)
            {
                ImageView img;
                img = (ImageView)itemView.findViewById(R.id.gridS);
                img.setImageDrawable(new BitmapDrawable(getResources(), mapElement.getImageBitmap()));
            }
            else if(mapElement.getStructure() != null)
            {
                gridS.setImageResource(mapElement.getStructure().getDrawableId());
            }
           else
            {
                gridS.setImageResource(0);
            }
        }
    }
}