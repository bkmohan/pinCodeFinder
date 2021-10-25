package com.android.apps.pincodefinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.apps.pincodefinder.databinding.ActivityDetailsBinding;

/**
 * DetailsActivity displays all information regarding an individual PostOffice Object
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0f);
        actionBar.setTitle("");


        // View binding simplifies to write code that interacts with views.
        // Once view binding is enabled in a module, it generates a binding class for each XML layout file

        ActivityDetailsBinding binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        // Get the index of the clicked Post Office object in the recycler view
        int index = getIntent().getIntExtra("Index", 0);
        PostOffice office = SearchActivity.getPostOffice(index);

        // Instance of a binding class contains direct references to all views that have an ID in the corresponding layout.
        binding.officeName.setText(office.getName());
        binding.detailsPincode.setText(office.getPincode());
        binding.detailsBranchType.setText(office.getBranchType());
        binding.detailsDeliveryStatus.setText(office.getDeliveryStatus());
        binding.detailsCircle.setText(office.getCircle());
        binding.detailsDivision.setText(office.getDivision());
        binding.detailsRegion.setText(office.getRegion());
        binding.detailsDistrict.setText(office.getDistrict());
        binding.detailsState.setText(office.getState());

        StringBuilder share = new StringBuilder();
        share.append("Post Office:\n" + office.getName() + "\n\n");
        share.append("Pincode:\n" + office.getPincode() + "\n\n");
        share.append("Branch Type:\n" + office.getBranchType() + "\n\n");
        share.append("Address:\n" + office.getDivision() + " Division, " + office.getRegion() + " Region, ");
        share.append(office.getDistrict() + ", " + office.getState());

        //Sharing details of the post office to other apps in text format
        binding.detailsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, share.toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }
}