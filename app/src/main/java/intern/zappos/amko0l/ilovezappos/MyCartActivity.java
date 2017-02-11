package intern.zappos.amko0l.ilovezappos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import intern.zappos.amko0l.ilovezappos.adapter.CartAdapter;
import intern.zappos.amko0l.ilovezappos.helper.CartController;
import intern.zappos.amko0l.ilovezappos.model.Item;

/**
 * Created by amitn on 10-02-2017.
 */

public class MyCartActivity extends AppCompatActivity {
    private List<Item> cartList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CartAdapter mAdapter;

    @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new CartAdapter(cartList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareItemData();
    }

    private void prepareItemData() {
        final CartController ct = (CartController) getApplicationContext();
        int productsize = ct.getProductArraylistsize();

        for (int j = 0; j < productsize; j++){
            cartList.add(ct.getProducts(j));
        }

        mAdapter.notifyDataSetChanged();
    }
}