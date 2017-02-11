package intern.zappos.amko0l.ilovezappos.helper;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

import intern.zappos.amko0l.ilovezappos.model.Item;

/**
 * Created by amitn on 10-02-2017.
 */

public class CartController extends Application {
    private static Context mContext;
    private static CartController instance;

    public static CartController getInstance() {
        return instance;
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    private ArrayList<Item> myproducts = new ArrayList<Item>();

    public Item getProducts(int pPosition) {
        return myproducts.get(pPosition);
    }

    public void setProducts(Item products) {
        myproducts.add(products);
    }

    public int getProductArraylistsize() {
        return myproducts.size();
    }
}
