package khalil.cointrader;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] coinSymbol;
    //private final Integer[] imageID;
    private final String[] mTickerPrice;
    private final String[] mCoinName;
    private final String[] mPercentChange;
    private final String[] mHoldings;
    private final String[] mHoldingsUSD;
    private final String[] mLogoURL;

    private String mPriceTrimmed;
    private Integer[] mImageID;
    private ImageLoader imageLoader;

    static class ViewHolder {
        TextView symbol;
        TextView coin_name;
        TextView current_price;
        TextView percent;
        ImageView arrow;
        ImageView logo;
    }

    public CustomListAdapter(Activity context, Integer[] image_ID, String[] coin_symbol, String[] coin_name,
                             String[] tickerPrice, String[] percentChange, String[] holdings, String[] holdingsUSD,
                             String [] logoURL) {
        super(context, R.layout.row_layout, coin_symbol);
        // TODO Auto-generated constructor stub

        this.context= context;
        this.coinSymbol= coin_symbol;
        this.mCoinName = coin_name;
        this.mTickerPrice = tickerPrice;
        this.mPercentChange = percentChange;
        this.mHoldings = holdings;
        this.mHoldingsUSD = holdingsUSD;
        this.mImageID = image_ID;
        this.mLogoURL = logoURL;

        imageLoader = new ImageLoader(context.getApplicationContext());
    }

    public View getView(int position, View rowView, ViewGroup parent) {

        ViewHolder holder;
        if(rowView == null) {
            LayoutInflater inflater=context.getLayoutInflater();
            rowView=inflater.inflate(R.layout.row_layout, null,true);
            holder = new ViewHolder();
            holder.symbol = (TextView) rowView.findViewById(R.id.symbol);
            holder.coin_name = (TextView) rowView.findViewById(R.id.coin_name);
            holder.current_price = (TextView) rowView.findViewById(R.id.current_price);
            holder.percent = (TextView) rowView.findViewById(R.id.percent_gain);
            //holder.arrow = (ImageView) rowView.findViewById(R.id.arrow);
            holder.logo = (ImageView) rowView.findViewById(R.id.logo);
            rowView.setBackgroundResource(R.drawable.selector_list_background);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        if(mCoinName[position] != null)
            holder.coin_name.setText(mCoinName[position]);

        if(mImageID[position] != null)
            holder.logo.setBackgroundResource(mImageID[position]);

        if(mPercentChange[position] != null && mPercentChange[position].charAt(0) == '+') {
            //holder.percent.setTextColor(Color.parseColor("#4CA53B"));
            holder.percent.setBackgroundResource(R.drawable.green_percentage);
        }
        else if (mPercentChange[position] != null) {
            //holder.percent.setTextColor(Color.parseColor("#F34827"));
            holder.percent.setBackgroundResource(R.drawable.red_percentage);

        }

        updatePrice(holder, position);

        holder.percent.setText(mPercentChange[position]);
        holder.symbol.setText(coinSymbol[position]);
        //logo.setBackgroundResource(imageID[position]);

        ImageView image = holder.logo;
        imageLoader.displayImage(mLogoURL[position], image);
        return rowView;

    }

    public void updatePrice(ViewHolder holder, Integer position){
        final ObjectAnimator animator;



        if(mTickerPrice[position] != null && mTickerPrice[position].length() > 10)
            mPriceTrimmed = mTickerPrice[position].substring(0,10);
        else
            mPriceTrimmed = mTickerPrice[position];


        if(holder.current_price.getText() != null && !holder.current_price.getText().toString().equals("")){
            Double currentPrice = Double.parseDouble(holder.current_price.getText().toString().replaceAll(",",""));
            Double newPrice = Double.parseDouble(mPriceTrimmed.replaceAll(",",""));

            final Property<TextView, Integer> property = new Property<TextView, Integer>(int.class, "textColor"){
                @Override
                public Integer get(TextView object){
                    return object.getCurrentTextColor();
                }
                @Override
                public void set(TextView object, Integer value){
                    object.setTextColor(value);
                }
            };

            if (currentPrice > newPrice) {
                animator = ObjectAnimator.ofInt(holder.current_price, property, Color.GREEN, Color.WHITE);
                animator.setDuration(2000);
                animator.setEvaluator(new ArgbEvaluator());
                animator.start();
            } else if (currentPrice < newPrice){
                animator = ObjectAnimator.ofInt(holder.current_price, property, Color.RED, Color.WHITE);
                animator.setDuration(2000);
                animator.setEvaluator(new ArgbEvaluator());
                animator.start();
            }

            holder.current_price.setText(mPriceTrimmed);
        } else {
            holder.current_price.setText(mPriceTrimmed);
        }
    }
}
