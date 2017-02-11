package intern.zappos.amko0l.ilovezappos;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import intern.zappos.amko0l.ilovezappos.adapter.ItemCardAdapter;
import intern.zappos.amko0l.ilovezappos.helper.CartController;
import intern.zappos.amko0l.ilovezappos.helper.Connectivity;
import intern.zappos.amko0l.ilovezappos.model.Item;
import intern.zappos.amko0l.ilovezappos.model.ItemResponse;
import intern.zappos.amko0l.ilovezappos.retro.ApiInterface;
import intern.zappos.amko0l.ilovezappos.retro.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";
    private static String API_TERM = "";

    private RecyclerView recyclerView;
    private ItemCardAdapter adapter;
    List<Item> items;
    CartController ct;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ct = (CartController) getApplicationContext();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        final Intent intent = new Intent(this, MyCartActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.items_recycler_view);
        items = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        ApiInterface apiService =   RetroClient.getClient().create(ApiInterface.class);
        Log.d(TAG, "apiService " + apiService);
        Call<ItemResponse> call = apiService.getSearch(API_TERM, API_KEY);
        Log.d(TAG, "call " + call);
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse>call, Response<ItemResponse> response) {
                Log.d(TAG, "onResponse" );
                items = response.body().get_results();
                Log.d(TAG, "Number of items received: " + items.size());
                adapter = new ItemCardAdapter(getBaseContext(), items);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Number of items received: " + items.size());
            }

            @Override
            public void onFailure(Call<ItemResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        Log.d(TAG, "cart size is: " + ct.getProductArraylistsize());
        if(ct.getProductArraylistsize()==0)
            fab.setVisibility(View.GONE);
        else
            fab.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "cart size is resume: " + ct.getProductArraylistsize());
        if(ct.getProductArraylistsize()==0)
            fab.setVisibility(View.GONE);
        else
            fab.setVisibility(View.VISIBLE);

        // Show Offline Error Message
        if (!Connectivity.isConnected(getApplicationContext())) {
            Toast.makeText(getBaseContext(), R.string.no_connection, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
