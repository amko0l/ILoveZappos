package intern.zappos.amko0l.ilovezappos;

import android.app.SearchManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import intern.zappos.amko0l.ilovezappos.databinding.ActivitySearchBinding;
import intern.zappos.amko0l.ilovezappos.helper.CartController;
import intern.zappos.amko0l.ilovezappos.model.Item;
import intern.zappos.amko0l.ilovezappos.model.ItemResponse;
import intern.zappos.amko0l.ilovezappos.retro.ApiInterface;
import intern.zappos.amko0l.ilovezappos.retro.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amitn on 28-01-2017.
 */

public class SearchResultsActivity extends AppCompatActivity {
    private static final String TAG = SearchResultsActivity.class.getSimpleName();

    private final static String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";
    List<Item> items;
    Item tocart;

    private String postUrl = "";
    TextView prodbrand;
    TextView proddesc;
    TextView prodprice;
    TextView proddiscount;
    TextView prodoriginalprice;
    ImageView prodthumb;
    //Button produrl;
    CartController ct;
    FloatingActionButton fab;

    ActivitySearchBinding activitySearchBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        activitySearchBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        //activitySearchBinding = DataBindingUtil.findBinding(findViewById(R.id.fab_search));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ct = (CartController) getApplicationContext();
        Log.d(TAG, "ct value is " + ct);

        fab = activitySearchBinding.fabSearch;

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ct.setProducts(tocart);

                ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
                scale.setDuration(500);
                scale.setInterpolator(new OvershootInterpolator());
                fab.startAnimation(scale);

                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);

                Snackbar snackbar = Snackbar
                        .make(view.getRootView(), R.string.item_added_cart, Snackbar.LENGTH_LONG);

                snackbar.show();

                *//*AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                animation1.setDuration(1000);
                animation1.setStartOffset(1000);
                animation1.setFillAfter(true);
                fab.startAnimation(animation1);

                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);*//*
            }
        });
*/

/*        Log.d(TAG, "searchBinding  " + searchBinding);
        prodbrand = searchBinding.prodBrand;
        Log.d(TAG, "prodbrand  " + prodbrand);
        proddesc = searchBinding.prodDesc;
        prodprice = searchBinding.prodPrice;
        prodoriginalprice = searchBinding.prodOriginalPrice;
        proddiscount = searchBinding.prodDiscount;
        prodthumb = searchBinding.prodPic;*/

        prodbrand = (TextView) findViewById(R.id.prod_brand);
        Log.d(TAG, "prodbrand  " + prodbrand);
        proddesc = (TextView) findViewById(R.id.prod_desc);
        prodprice = (TextView) findViewById(R.id.prod_price);
        prodoriginalprice = (TextView) findViewById(R.id.prod_original_price);
        proddiscount = (TextView) findViewById(R.id.prod_discount);
        prodthumb = (ImageView) findViewById(R.id.prod_pic);
        activitySearchBinding.setFabHandler(new FabHandler());
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, " query is " + query);

            //extracting URL
            ApiInterface apiService = RetroClient.getClient().create(ApiInterface.class);
            Log.d(TAG, "apiService " + apiService);
            Call<ItemResponse> call = apiService.getSearch(query, API_KEY);
            Log.d(TAG, "call " + call);
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    Log.d(TAG, "onResponse");
                    items = response.body().get_results();
                    Log.d(TAG, "Number of items received: " + items.size());
                    if (items == null || items.size() == 0) {
                        prodbrand.setText(R.string.no_product_found);
                        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                        p.setAnchorId(View.NO_ID);
                        fab.setLayoutParams(p);
                        fab.setVisibility(View.GONE);
                    }
                    else{
                        tocart = items.get(0);

                        SpannableString styledBrandame = new SpannableString(items.get(0).get_brandName());
                        styledBrandame.setSpan(new StyleSpan(Typeface.BOLD), 0, items.get(0).get_brandName().length(), 0);
                        prodbrand.setText(styledBrandame);
                        SpannableString styledproddesc = new SpannableString(items.get(0).get_productName());
                        styledproddesc.setSpan(new StyleSpan(Typeface.ITALIC), 0, items.get(0).get_productName().length() , 0);
                        proddesc.setText(styledproddesc);


                        SpannableString styledprice = new SpannableString(items.get(0).get_price());
                        styledprice.setSpan(new StyleSpan(Typeface.BOLD), 0, items.get(0).get_price().length(), 0);
                        prodprice.setText(styledprice);

                        SpannableString styleddiscount = new SpannableString(items.get(0).get_percentOff());
                        styleddiscount.setSpan(new ForegroundColorSpan(Color.GREEN), 0, items.get(0).get_percentOff().length(), 0);
                        proddiscount.setText(styleddiscount);


                        SpannableString styledoriginal = new SpannableString(items.get(0).get_originalPrice());
                        styledoriginal.setSpan(new StrikethroughSpan(), 0, items.get(0).get_originalPrice().length(), 0);
                        prodoriginalprice.setText(styledoriginal);


                        if ("0%".equals(items.get(0).get_percentOff())) {
                            proddiscount.setVisibility(View.GONE);
                            prodoriginalprice.setVisibility(View.GONE);
                        }

                        Picasso.with(getBaseContext()).load(items.get(0).get_thumbnailImageUrl()).into(prodthumb);
                        postUrl = items.get(0).get_productUrl();
                    }
                }

                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
            //use the query to search
        } else {
            tocart = getIntent().getParcelableExtra("itemparcel");
            Log.d(TAG, " item is   " + tocart);
            ct = (CartController) getApplicationContext();
            if (tocart == null)
                prodbrand.setText(R.string.no_product_found);
            else {
                SpannableString styledBrandame = new SpannableString(tocart.get_brandName());
                styledBrandame.setSpan(new StyleSpan(Typeface.BOLD), 0, tocart.get_brandName().length(), 0);
                prodbrand.setText(styledBrandame);
                SpannableString styledproddesc = new SpannableString(tocart.get_productName());
                styledproddesc.setSpan(new StyleSpan(Typeface.ITALIC), 0, tocart.get_productName().length() , 0);
                proddesc.setText(styledproddesc);


                SpannableString styledprice = new SpannableString(tocart.get_price());
                styledprice.setSpan(new StyleSpan(Typeface.BOLD), 0, tocart.get_price().length(), 0);
                prodprice.setText(styledprice);

                SpannableString styleddiscount = new SpannableString(tocart.get_percentOff());
                styleddiscount.setSpan(new ForegroundColorSpan(Color.GREEN), 0, tocart.get_percentOff().length(), 0);
                proddiscount.setText(styleddiscount);


                SpannableString styledoriginal = new SpannableString(tocart.get_originalPrice());
                styledoriginal.setSpan(new StrikethroughSpan(), 0, tocart.get_originalPrice().length(), 0);
                prodoriginalprice.setText(styledoriginal);

                //styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length()-1 , 0);
                        /*styledString.setSpan(new ForegroundColorSpan(Color.GREEN), str1.length()+1, str1.length()+str2.length()+1 , 0);
                        styledString.setSpan(new StrikethroughSpan(), str1.length()+str2.length()+2, str1.length()+str2.length()+2+str3.length() , 0);*/

                        /*SpannableString styledoriginalprice = new SpannableString(items.get(0).get_originalPrice());
                        styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, items.get(0).get_price().length(), 0);
                        prodbrand.setText(styledString);*/


                if ("0%".equals(tocart.get_percentOff())) {
                    proddiscount.setVisibility(View.GONE);
                    prodoriginalprice.setVisibility(View.GONE);
                }

                Picasso.with(getBaseContext()).load(tocart.get_thumbnailImageUrl()).into(prodthumb);
                postUrl = tocart.get_productUrl();

            }
        }
    }

    public class FabHandler {

        public void onBaseFabClick(View view) {
            Log.d(TAG," onFabClick");
            ct.setProducts(tocart);

            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
            scale.setDuration(500);
            scale.setInterpolator(new OvershootInterpolator());
            fab.startAnimation(scale);

            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            fab.setLayoutParams(p);
            fab.setVisibility(View.GONE);

            Snackbar snackbar = Snackbar
                    .make(view.getRootView(), R.string.item_added_cart, Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }
}
