package khalil.cointrader;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;

import javax.crypto.Mac;

/**
 * Created by khalil on 12/16/17.
 */

public class Controller extends MainActivity {

    private RequestQueue mRequestQueue;
    public JsonObjectRequest stringRequest;
    private String coinURL = "https://api.coinbase.com/v2/prices/BTC-USD/buy";
    public String BTCURL = "https://api.coinbase.com/v2/prices/BTC-USD/buy";
    public String ETHURL = "https://api.coinbase.com/v2/prices/ETH-USD/buy";
    public String LTCURL = "https://api.coinbase.com/v2/prices/LTC-USD/buy";
    private static final String TAG = MainActivity.class.getName();
    private static final String REQUESTTAG = "Price Request";
    private Context mContext;
    private String[] mMarkets;
    private ListView listView;
    private List<Ticker> mTicker;
    Mac sha512_HMAC = null;
    String result = null;
    String key = "YOUR_KEY_HERE";


    public Controller(Context context, String[] mMarket){
        this.mContext=context;
        this.mMarkets = mMarkets;
        this.listView = listView;
        this.mMarkets = mMarket;
    }

    public interface VolleyCallback {
        void onSuccessResponse(String[] result);
    }

    public void getMarketSummaryResponse(final String url, final String url2, final VolleyCallback callback, final Integer[] imageID){
        mRequestQueue = Volley.newRequestQueue(mContext);
        stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getMarketResponse(response.toString(), url2, callback, imageID);
                //callback.onSuccessResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "HTTP Error: " + error.toString());
            }
        });

        stringRequest.setTag(REQUESTTAG);
        mRequestQueue.add(stringRequest);
    }

    public void getMarketResponse(final String response0, String url, final VolleyCallback callback, final Integer[] imageID){
        mRequestQueue = Volley.newRequestQueue(mContext);
        stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String[] responses = new String[2];
                responses[0] = response0;
                responses[1] = response.toString();
                callback.onSuccessResponse(responses);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "HTTP Error: " + error.toString());
            }
        });

        stringRequest.setTag(REQUESTTAG);
        mRequestQueue.add(stringRequest);
    }

    public String formatDecimalPrice(String value) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        return df.format(Double.valueOf(value));
    }

    public List<Ticker> populateTickers(String marketSummaryResponse, String marketResponse, Integer[] imageID, String[] NameLong) {
        String MarketName;
        String FullMarketName;
        String High;
        String Low;
        Double Volume;
        String Last;
        Double BaseVolume;
        String TimeStamp;
        String Ask;
        String OpenBuyOrders;
        String OpenSellOrders;
        String PrevDay;
        String PercentChange;
        String Price;
        Double prevDay;
        Double currentPriceDouble;
        String sign;
        String dollarChange;
        String percentChange;
        String currency;
        Integer Logo;

        mTicker = new ArrayList<Ticker>();

        List<HashMap<String, String>> summaryMap;

        if (!marketSummaryResponse.contains("\"success\":false")) {
            summaryMap = Bittrex.getMapsFromResponse(marketSummaryResponse);

            for(int i = 0; i < summaryMap.size(); i++){
                HashMap<String, String> onlyMap = summaryMap.get(i);
                MarketName = onlyMap.get("MarketName");
                FullMarketName = "";
                High = onlyMap.get("High");
                Low = onlyMap.get("Low");
                Volume = Double.parseDouble(onlyMap.get("Volume"));
                Last = onlyMap.get("Last");
                BaseVolume = Double.parseDouble(onlyMap.get("BaseVolume"));
                TimeStamp = onlyMap.get("TimeStamp");
                Ask = onlyMap.get("Ask");
                OpenBuyOrders = onlyMap.get("OpenBuyOrders");
                OpenSellOrders = onlyMap.get("OpenSellOrders");
                PrevDay = onlyMap.get("PrevDay");
                Logo = 0;

                prevDay = Double.parseDouble(onlyMap.get("PrevDay"));
                currentPriceDouble = Double.parseDouble(Last);

                if ((currentPriceDouble - prevDay) > 0) {
                    sign = "+";
                    //mPercentGain.setTextColor(getResources().getColor(R.color.greenColor));
                } else {
                    sign = "-";
                    //mPercentGain.setTextColor(getResources().getColor(R.color.redColor));
                }

                if(MarketName.split("-")[0].equals("USDT"))
                    currency = "$";
                else
                    currency = "";

                dollarChange = "(" + currency + formatDecimalSetoci(Double.toString(currentPriceDouble - prevDay)) + ")";
                percentChange = sign + formatDecimal(Double.toString((Math.abs(prevDay - currentPriceDouble) / prevDay) * 100)) + "%";

                //PercentChange = (percentChange + " " + dollarChange);
                PercentChange = percentChange;
                Price = formatDecimalSetoci(Last);

                Ticker ticker;
                ticker = new Ticker(Logo, FullMarketName, MarketName, High, Low, Volume, Last, BaseVolume, TimeStamp, Ask,
                        OpenBuyOrders, OpenSellOrders, PrevDay, PercentChange, Price, null, null);

                mTicker.add(ticker);
            }
            populateLogoUrls(marketResponse);
        } else
            System.out.println("ERROR! Response: " + marketSummaryResponse);


        Collections.sort(mTicker, new Comparator<Ticker>() {
            @Override
            public int compare(Ticker t1, Ticker t2) {
                return Double.compare(t2.getBaseVolume(), t1.getBaseVolume());
            }
        });

        return mTicker;
    }

    private void populateLogoUrls(String marketResponse) {
        List<HashMap<String, String>> marketMap;

        if (!marketResponse.contains("\"success\":false")) {
            marketMap = Bittrex.getMapsFromResponse(marketResponse);

            Collections.sort(marketMap, new Comparator<HashMap<String, String>>(){
                public int compare(HashMap<String, String> one, HashMap<String, String> two) {
                    return one.get("MarketName").compareTo(two.get("MarketName"));
                }
            });

            for(int i = 0; i < mTicker.size(); i++) {
                HashMap<String, String> onlyMap = marketMap.get(i);
                String url = onlyMap.get("LogoUrl");
                String coin_name = onlyMap.get("MarketCurrencyLong");

                mTicker.get(i).setLogoUrl(url);
                mTicker.get(i).setNameLong(coin_name);
                if(mTicker.get(i).getLogoUrl() != null && mTicker.get(i).getLogoUrl().equals("https://bittrex.com/Content/img/symbols/BTC.png"))
                    mTicker.get(i).setLogoUrl("https://cdn3.iconfinder.com/data/icons/circle-payment-methods-4/512/Bitcoin-512.png");

            }
        }
    }

    public String formatDecimal(String value) {
        DecimalFormat df;
        if (!(value == null)) {
            df = new DecimalFormat("#,###,##0.00");
            return df.format(Double.valueOf(value));
        } else {
            return ("-");
        }
    }

    public String formatDecimalSetoci(String value) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00######");
        return df.format(Double.valueOf(value));
    }


    @Override
    protected void onStop(){
        super.onStop();
        if(mRequestQueue!=null){
            mRequestQueue.cancelAll(REQUESTTAG);
        }
    }
}
