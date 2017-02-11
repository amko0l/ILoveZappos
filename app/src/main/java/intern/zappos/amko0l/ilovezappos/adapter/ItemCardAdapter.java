package intern.zappos.amko0l.ilovezappos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import intern.zappos.amko0l.ilovezappos.R;
import intern.zappos.amko0l.ilovezappos.SearchResultsActivity;
import intern.zappos.amko0l.ilovezappos.model.Item;


/**
 * Created by amitn on 28-01-2017.
 */

public class ItemCardAdapter extends RecyclerView.Adapter<ItemCardAdapter.MyViewHolder>{
    private static final String TAG = ItemCardAdapter.class.getSimpleName();

    private static List<Item> items;
    private Context context;

    private static Item item;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productBrand;
        TextView productDesc;
        TextView price;
        public ImageView thumbnail;

        public MyViewHolder(final View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            productBrand = (TextView) view.findViewById(R.id.product_brand_card);
            productDesc = (TextView) view.findViewById(R.id.product_desc_card);
            price = (TextView) view.findViewById(R.id.price_card);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d(TAG, "on click card view");
                    item = items.get(position);
                    Intent intent = new Intent(view.getContext(), SearchResultsActivity.class);
                    intent.putExtra("itemparcel", item);
                    view.getContext().startActivity(intent);
                    //Toast.makeText(v.getContext(), "os version is: " + "card onclick", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public ItemCardAdapter(Context mContext, List<Item> itemList) {
        Log.d(TAG,"Adapter"+itemList);
        this.context = mContext;
        this.items = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder    "+position);
        item = items.get(position);
        Log.d(TAG,"holder    "   +holder);
        Picasso.with(context).load(item.get_thumbnailImageUrl()).into(holder.thumbnail);
        SpannableString styledBrandame = new SpannableString(item.get_brandName());
        styledBrandame.setSpan(new StyleSpan(Typeface.BOLD), 0, item.get_brandName().length() , 0);
        holder.productBrand.setText(styledBrandame);
        SpannableString styledproddesc = new SpannableString(item.get_productName());
        styledproddesc.setSpan(new StyleSpan(Typeface.ITALIC), 0, item.get_productName().length() , 0);
        holder.productDesc.setText(styledproddesc);

        if("0%".equals(item.get_percentOff())){
            SpannableString styledString = new SpannableString(item.get_price());
            styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, item.get_price().length() , 0);
            holder.price.setText(styledString);
        }else{
            String str1 = item.get_price();
            String str2 = item.get_percentOff();
            String str3 = item.get_originalPrice();

            SpannableString styledString = new SpannableString(str1+" "+str2+" "+str3);

            styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length()-1 , 0);
            styledString.setSpan(new ForegroundColorSpan(Color.GREEN), str1.length()+1, str1.length()+str2.length()+1 , 0);
            styledString.setSpan(new StrikethroughSpan(), str1.length()+str2.length()+2, str1.length()+str2.length()+2+str3.length() , 0);
            holder.price.setText(styledString);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
