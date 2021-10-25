package com.android.apps.pincodefinder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity implementing Loader class for performing asynchronous network requests,
 * implementing ListItemClickListener to identify the object clinked inside the Recyclerview.
 */
public class SearchActivity extends AppCompatActivity
        implements LoaderCallbacks<List<PostOffice>>, PostofficeAdapter.ListItemClickListener {

    private static List<PostOffice> postOfficeList;
    /**
     * Raw URL for post office data.
     */
    private final String BASE_URL = "https://api.postalpincode.in";
    /**
     * Constant value for the postoffice loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private final int LOADER_ID = 1;
    /**
     * View that is displayed SearchActivity
     */
    private EditText search;
    private ImageButton searchButton;
    private RadioGroup radioGroup;
    private RecyclerView rcv;
    private ProgressBar progressBar;
    private TextView errorMessage;
    /**
     * Adapter for the list of postffice
     */
    private PostofficeAdapter mAdapter;
    private String mUrl;

    /**
     * A static class to return a postoffice object based on the index in the list.
     *
     * @param index
     * @return
     */
    public static PostOffice getPostOffice(int index) {
        return postOfficeList.get(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the views in the layout
        search = findViewById(R.id.search);
        searchButton = findViewById(R.id.searchButton);
        radioGroup = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.progress_bar);
        errorMessage = findViewById(R.id.error_text);

        postOfficeList = new ArrayList<>();

        // Find a reference to the {@link RecyclerView} in the layout
        rcv = findViewById(R.id.post_office_list);

        // Create a new adapter that takes an empty list of postoffice as input
        mAdapter = new PostofficeAdapter(this, postOfficeList);

        // Set the adapter on the {@link RecyclerView}
        // so the list can be populated in the user interface
        rcv.setAdapter(mAdapter);

        //Set linear layout for the list items
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //Adding dividers between items in recycler view
        rcv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        //Perform search activity on search botton clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnection()) performSearch();
                else displayError();
            }
        });

        //Performs search activity on clicking enter button in keyboard
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //Perform search activity
                    if (checkConnection()) performSearch();
                    else displayError();

                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //Change the keyboard input type for pincode input and office name input
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_pincode) {
                    search.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else {
                    search.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);

    }

    /**
     * Checks the network status and return true if active network found.
     *
     * @return
     */
    private boolean checkConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null &&
                networkInfo.isConnectedOrConnecting();

        // If there is a network connection, fetch data
        return isConnected;
    }

    /**
     * Display error message if no network connection found.
     */
    private void displayError() {
        errorMessage.setText(R.string.no_network);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void performSearch() {
        String searchItem = search.getText().toString();

        int checked = radioGroup.getCheckedRadioButtonId();

        // Check the radio buttons selected to create the query url
        if (checked == R.id.radio_pincode) {
            mUrl = BASE_URL + "/pincode/" + searchItem;
        } else {
            mUrl = BASE_URL + "/postoffice/" + searchItem;
        }

        progressBar.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);

        // Restart the loader to perform the network request asynchronously.
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, this);

    }

    /**
     * On clicking an item in the Recyclerview starts DetailActivity to
     * display more info on the selected post office
     *
     * @param clickedItemIndex
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("Index", clickedItemIndex);
        startActivity(intent);
    }

    /**
     * Overriding Loader methods to perform asynchronous network requests.
     *
     * @param id
     * @param args
     * @return
     */
    @NonNull
    @Override
    public Loader<List<PostOffice>> onCreateLoader(int id, @Nullable Bundle args) {
        return new PostOfficeLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<PostOffice>> loader, List<PostOffice> postOffices) {
        progressBar.setVisibility(View.GONE);

        if (postOffices != null && !postOffices.isEmpty()) {
            postOfficeList = postOffices;
            mAdapter.add(postOfficeList);
            errorMessage.setVisibility(View.GONE);
        } else {
            if (mUrl != null) {
                errorMessage.setText(R.string.empty_list);
                errorMessage.setVisibility(View.VISIBLE);
                mAdapter.clear();
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<PostOffice>> loader) {
        mAdapter.clear();
    }


}