package com.android.apps.pincodefinder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for binding data set to views that are displayed within a RecyclerView.
 */
public class PostofficeAdapter extends RecyclerView.Adapter<PostofficeAdapter.ViewHolder> {
    /**
     * An on-click handler that is defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;
    private List<PostOffice> postOffices;
    private Context context;

    public PostofficeAdapter(Context context, List<PostOffice> postOffices) {
        this.context = context;
        this.postOffices = postOffices;
        mOnClickListener = (ListItemClickListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.office_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostOffice office = postOffices.get(position);
        holder.getName().setText(office.getName());
        holder.getAddress().setText(office.getAddress());
        holder.getPincode().setText(office.getPincode());


        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable background = (GradientDrawable) holder.branchType.getBackground();

        int color;
        String officetype;

        if (office.getBranchType().equals("Sub Post Office")) {
            officetype = "S.O";
            color = R.color.s_0;
        } else if (office.getBranchType().equals("Branch Post Office")) {
            officetype = "B.O";
            color = R.color.b_0;
        } else {
            officetype = "H.O";
            color = R.color.h_0;
        }
        holder.branchType.setText(officetype);
        // Set the color on the branch type circle
        background.setColor(context.getResources().getColor(color));
    }

    @Override
    public int getItemCount() {
        if (postOffices != null) {
            return postOffices.size();
        } else {
            return 0;
        }
    }

    /**
     * Adds new postoffice objects to the recyclerview adapter
     *
     * @param postOffices
     */
    public void add(List<PostOffice> postOffices) {
        this.postOffices = postOffices;
        notifyDataSetChanged();
    }

    /**
     * Clears all Post Office objects in the list
     */
    public void clear() {
        postOffices.clear();
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * ViewHolder describing an item view and metadata about its place within the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView branchType;
        private final TextView name;
        private final TextView address;
        private final TextView pincode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            branchType = itemView.findViewById(R.id.office_type);
            name = itemView.findViewById(R.id.office_name);
            address = itemView.findViewById(R.id.office_address);
            pincode = itemView.findViewById(R.id.office_pincode);

            itemView.setOnClickListener(this);
        }

        public TextView getBranchType() {
            return branchType;
        }

        public TextView getName() {
            return name;
        }

        public TextView getAddress() {
            return address;
        }

        public TextView getPincode() {
            return pincode;
        }

        /**
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
