package khalil.cointrader;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import khalil.cointrader.Controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity {

    private TextView mBanner;
    private ImageView mBannerBackground;
    private ImageView mBalance;
    private ImageView mOrderType;
    private ImageView mLogo;
    private TextView mAmount;
    private TextView mAmountType2;
    private TextView mAmountType;
    public TextView mLimitStop;
    public TextView mPrice;
    private TextView mPercentGain;
    private TextView mCoinBalance;
    private TextView mUSDBalance;
    private TextView mSymbol;
    private TextView mCoinName;
    public EditText mEditAmount;
    public EditText mEditLimitStop;
    public JSONObject mJSONresponse;
    private Button marketButton;
    private Button limitButton;
    private Button stopButton;
    private Button mBuyButton;
    private Button mSellButton;
    private Button mBackArrow;
    public String mOrder = "MARKET";
    public String mCurrentPrice = "-";
    public String mSelectedCurrency;
    public Toast mToast;
    private ProgressBar mPriceProgress;
    Timer mtimer;
    private Controller Controller;
    private Bittrex wrapper;
    private Double mCurrentPriceDouble;
    private ImageLoader imageLoader;

    private static final String API_KEY = "5573a4c7e31431ba97fb6163e6494d4a";
    private static final String API_SECRET = "O4IXSLZBO4f/l/WVJk9K6d4LyCgQBQ2LHNDzyBTszA2d+DWjR7lkdsYSRrFNqevAkqq64eRbYNjgfSbQsgNvvQ==";
    private static final String API_PASS = "sthtqgl8ocb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_layout);
        wrapper = new Bittrex();
        wrapper.setAuthKeysFromTextFile("keys.txt");
        mBanner = (TextView) findViewById(R.id.banner);
        mBannerBackground = (ImageView) findViewById(R.id.banner_background);
        mBalance = (ImageView) findViewById(R.id.balance);
        mOrderType = (ImageView) findViewById(R.id.order_type);
        mLogo = (ImageView) findViewById(R.id.logo);

        mAmount = (TextView) findViewById(R.id.amount);
        mAmountType = (TextView) findViewById(R.id.amount_currency);
        mAmountType2 = (TextView) findViewById(R.id.amount_usd);
        mLimitStop = (TextView) findViewById(R.id.limit);
        mPrice = (TextView) findViewById(R.id.current_price);
        mPercentGain = (TextView) findViewById(R.id.percent_gain);
        mCoinBalance = (TextView) findViewById(R.id.balance_coin);
        mUSDBalance = (TextView) findViewById(R.id.balance_usd);

        mSymbol = (TextView) findViewById(R.id.symbol);
        mCoinName = (TextView) findViewById(R.id.coin_name);
        imageLoader = new ImageLoader(getApplicationContext());

        String[] coins = getResources().getStringArray(R.array.Markets);
        String[] coin_symbol = getResources().getStringArray(R.array.Symbols);
        String[] fullCoinName = getResources().getStringArray(R.array.CoinName);

        int coin_index = getSelectedCoin();
        //mSelectedCurrency = coins[coin_index];
        mSymbol.setText(mSelectedCurrency);
        //mCoinName.setText(fullCoinName[coin_index]);
        //mLogo.setBackgroundResource(imageId[coin_index]);
        mAmount.setText("Amount (" + mSelectedCurrency.split("-")[1] + ")");



        mEditAmount = (EditText) findViewById(R.id.edit_amount);
        mEditLimitStop = (EditText) findViewById(R.id.edit_limit);
        mEditAmount.setBackgroundResource(R.drawable.selector_textwindow);
        mEditLimitStop.setBackgroundResource(R.drawable.selector_textwindow);
        mAmountType.setText(mSelectedCurrency.split("-")[1]);
        mAmountType2.setText(mSelectedCurrency.split("-")[0]);

        mPriceProgress = (ProgressBar) findViewById(R.id.price_progressBar);
        mPriceProgress.setVisibility(View.INVISIBLE);

        mBackArrow = (Button) findViewById(R.id.back_arrow);
        marketButton = (Button) findViewById(R.id.button_market);
        limitButton = (Button) findViewById(R.id.button_limit);
        stopButton = (Button) findViewById(R.id.button_stop);
        mBuyButton = (Button) findViewById(R.id.buy_button);
        mSellButton = (Button) findViewById(R.id.sell_button);
        mBuyButton.setBackgroundResource(R.drawable.selector_buy);
        mSellButton.setBackgroundResource(R.drawable.selector_sell);

        //Initialize Views
        mOrderType.setBackgroundResource(R.drawable.market_select);
        mBalance.setBackgroundResource(R.drawable.balance);

        mLimitStop.setText("Price");
        mEditAmount.setHint("0.00");
        mEditLimitStop.setHint(R.string.hint1);
        mEditLimitStop.setFocusable(false);
        mEditLimitStop.setAlpha(0.5f);

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.this.finish();
            }
        });

        marketButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mLimitStop.setText("Price");
                mEditLimitStop.setText("");
                mEditLimitStop.setHint(R.string.hint1);
                mEditLimitStop.setError(null);
                mEditLimitStop.setFocusable(false);
                mEditLimitStop.setAlpha(0.5f);

                //Limit changes to home tab only

                mOrderType.setBackgroundResource(R.drawable.market_select);
                mOrder = "MARKET";
                //SET TO MARKET ORDER
            }
        });

        limitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mLimitStop.setText("Limit Price");
                mEditLimitStop.setHint("0.00");
                mEditLimitStop.setFocusableInTouchMode(true);
                mEditLimitStop.setAlpha(1f);
                mEditAmount.setHint("0.00");

                //Limit changes to home tab only
                mOrderType.setBackgroundResource(R.drawable.limit_select);
                mOrder = "LIMIT";

                //SET TO LIMIT ORDER
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mLimitStop.setText("Stop Price");
                mEditLimitStop.setHint("0.00");
                mEditLimitStop.setFocusableInTouchMode(true);
                mEditLimitStop.setAlpha(1f);
                mEditAmount.setHint("0.00");

                //Limit changes to home tab only

                mOrderType.setBackgroundResource(R.drawable.stop_select);
                mOrder = "STOP";

                //SET TO STOP ORDER
            }
        });

        mEditAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    scrollDown();
                }

                String text1 = mEditAmount.getText().toString();
                if(!text1.equals("")) {
                    mEditAmount.setText(formatDecimalSetoci(text1));
                }
            }
        });

        mEditLimitStop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }

                String text2 = mEditLimitStop.getText().toString();

                if(!text2.equals("")) {
                    mEditLimitStop.setText(formatDecimalSetoci(text2));
                }

                String balance = mUSDBalance.getText().toString();

                if(!balance.equals("") && !text2.equals("")) {
                    Double numShares = Double.parseDouble(balance)/Double.parseDouble(text2);
                    mEditAmount.setText(Double.toString(numShares - numShares*.0025));
                }
            }

        });

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = getCurrentFocus();

                if (view != null) {
                    mEditLimitStop.clearFocus();
                    mEditAmount.clearFocus();
                    mLimitStop.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                //Limit changes to home tab only
                //checkAmount();
                buy(mSymbol.getText().toString());

                //SET TO MARKET ORDER
            }
        });

        mSellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    mEditLimitStop.clearFocus();
                    mEditAmount.clearFocus();
                    mLimitStop.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //Limit changes to home tab only
                //checkAmount();
                sell(mSymbol.getText().toString());

                //SET TO MARKET ORDER
            }
        });

        startPriceUpdateTimer();
        //testRequest();
    }

    private void scrollDown(){
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scroll_main));
        scrollview.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollview.smoothScrollTo(0, 500);
                System.out.println("SCROLL Y POSITION: " + scrollview.getScrollY());
            }
        }, 100);
    }

    private int getSelectedCoin() {
        SharedPreferences mSharedURL = getSharedPreferences("coin_index", MODE_PRIVATE);
        SharedPreferences mSharedPrice = getSharedPreferences("selected_price", MODE_PRIVATE);
        SharedPreferences mSharedPercent = getSharedPreferences("selected_percent", MODE_PRIVATE);
        SharedPreferences mSharedMarket = getSharedPreferences("market", MODE_PRIVATE);
        SharedPreferences mSharedMarketLong = getSharedPreferences("marketlong", MODE_PRIVATE);
        SharedPreferences mSharedLogo = getSharedPreferences("logourl", MODE_PRIVATE);


        String gain = mSharedPercent.getString("selected_percent", "-");

        mSelectedCurrency = mSharedMarket.getString("market", "-");
        mCoinName.setText(mSharedMarketLong.getString("marketlong", "-"));
        imageLoader.displayImage(mSharedLogo.getString("logourl", null), mLogo);

        System.out.println(mSelectedCurrency);
        mPercentGain.setText(gain);

        if(gain.charAt(0) == '+') {
            mPercentGain.setTextColor(getResources().getColor(R.color.greenColor));
        }
        else {
            mPercentGain.setTextColor(getResources().getColor(R.color.redColor));
        }

        mPrice.setText(mSharedPrice.getString("selected_price", "-"));

        return mSharedURL.getInt("coin_index", 0);
    }

    private void toastMessage(String message){
        if(mToast!=null)
            mToast.cancel();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_HORIZONTAL,0,900);
        mToast.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String formatDecimal(String value) {
        DecimalFormat df;
        if(!(value == null)) {
            df = new DecimalFormat("#,###,##0.00");
            return df.format(Double.valueOf(value));
        }
        else {
            return ("-");
        }
    }

    public String formatDecimalSetoci(String value) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00######");
        return df.format(Double.valueOf(value));
    }

    public boolean areFieldsValid(){
        boolean isValid = false;
        boolean isAmountEmpty = (mEditAmount.getText().toString().equals("") || mEditAmount.getText().toString().equals("0.00"));
        boolean isStopLimitEmpty = mEditLimitStop.getText().toString().equals("");
        boolean isMarketOrder = mOrder.equals("MARKET");

        if(isMarketOrder){
            if(isAmountEmpty) {
                mEditAmount.setError("Amount is required!");
            } else {
                isValid = true;
            }
        } else {
            if(isAmountEmpty || isStopLimitEmpty){
                if(isAmountEmpty)
                    mEditAmount.setError("Amount is required!");
                if(isStopLimitEmpty)
                    mEditLimitStop.setError(mLimitStop.getText() + " is required!");
            } else {
                isValid = true;
            }
        }
        return isValid;
    }

    public void startPriceUpdateTimer() {

        mtimer = new Timer();
        mtimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //System.out.println(mSelectedCurrency);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPriceProgress.setVisibility(View.VISIBLE);
                    }
                });

                new Thread(new Runnable() {
                    public void run() {
                        // a potentially  time consuming task
                        updateTicker(wrapper);
                        updateBalance(wrapper);
                    }
                }).start();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPriceProgress.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }, 0, 10000); //Length of how often to update location
    }

    public void updateTicker(Bittrex wrapper) {
        String rawResponse = wrapper.getMarketSummary(mSelectedCurrency);
        Double prevDay;
        final String currentPrice;
        final String percentChange;
        final String dollarChange;
        String sign;
        final String currency;

        List<HashMap<String, String>> responseMapList;
        if(!rawResponse.contains("\"success\":false")) {
            System.out.println(rawResponse);
            responseMapList = Bittrex.getMapsFromResponse(rawResponse);
            HashMap<String, String> onlyMap = responseMapList.get(0);
            prevDay = Double.parseDouble(onlyMap.get("PrevDay"));
            currentPrice = onlyMap.get("Last");
            mCurrentPriceDouble = Double.parseDouble(currentPrice);

            if((mCurrentPriceDouble - prevDay)>0) {
                sign = "+";
                mPercentGain.setTextColor(getResources().getColor(R.color.greenColor));
            }
            else {
                sign = "-";
                mPercentGain.setTextColor(getResources().getColor(R.color.redColor));
            }

            if(onlyMap.get("MarketName").split("-")[0].equals("USDT"))
                currency = "$";
            else
                currency = "";


            dollarChange = "(" + currency + formatDecimalSetoci(Double.toString(mCurrentPriceDouble - prevDay)) + ")";
            percentChange = sign + formatDecimal(Double.toString((Math.abs(prevDay - mCurrentPriceDouble)/prevDay)*100)) + "%";

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPercentGain.setText(percentChange + " " + dollarChange);
                    if(formatDecimalSetoci(currentPrice).length() > 11)
                        mPrice.setText(currency + formatDecimalSetoci(currentPrice).substring(0,9));
                    else
                        mPrice.setText(currency + formatDecimalSetoci(currentPrice));
                }
            });
            // Get wanted value using a key found in the KeySet
            //onlyMap.get("Volume");
        }
        else
            System.out.println("ERROR! Response: " + rawResponse);
    }

    public void updateBalance(Bittrex wrapper){
        String rawResponse = wrapper.getBalance(mSelectedCurrency.split("-")[1]);
        String rawResponse2 = wrapper.getBalance(mSelectedCurrency.split("-")[0]);
        final String coinBalance;
        final String balance2;
        List<HashMap<String, String>> responseMapList;

        if(rawResponse.contains("\"success\":true") && rawResponse2.contains("\"success\":true")) {
            responseMapList = Bittrex.getMapsFromResponse(rawResponse);
            final HashMap<String, String> currency1 = responseMapList.get(0);

            responseMapList = Bittrex.getMapsFromResponse(rawResponse2);
            final HashMap<String, String> currency2 = responseMapList.get(0);

            coinBalance = currency1.get("Balance");
            balance2 = currency2.get("Balance");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!(coinBalance == null)) {
                        String coinBalanceUSD = Double.toString(mCurrentPriceDouble * Double.parseDouble(coinBalance));
                        mCoinBalance.setText(formatDecimalSetoci(coinBalance) + " ($" + formatDecimal(coinBalanceUSD) + ")");
                    }
                    else
                        mCoinBalance.setText("0.000000 ($0.00)");

                    if(!(balance2 == null)) {
                        if(mAmountType2.getText().toString().equals("USDT"))
                            mUSDBalance.setText("$" + formatDecimal(balance2));
                        else
                            mUSDBalance.setText("" + formatDecimalSetoci(balance2));
                    }
                    else
                        mUSDBalance.setText("$0.00");
                }
            });
        }
        else
            System.out.println("ERROR! Response: " + rawResponse);
    }

    public void testRequest(){
        String rawResponse = wrapper.getCurrencies();
        List<HashMap<String, String>> responseMapList;
        if(!rawResponse.contains("\"success\":false")) {
            responseMapList = Bittrex.getMapsFromResponse(rawResponse);
            //responseMapList.get()
            HashMap<String, String> onlyMap;

            for(int i = 0; i < responseMapList.size(); i++){
                onlyMap = responseMapList.get(i);
                System.out.println(onlyMap.get("CurrencyLong") + " ("+onlyMap.get("Currency") + ")");
            }
            // See available information using present keys
            onlyMap = responseMapList.get(0);

            for(String key : onlyMap.keySet())
                System.out.print(key + " ");
            System.out.println();

            System.out.println(responseMapList.size());
        }
        else
            System.out.println("ERROR! Response: " + rawResponse);

    }
    /**
     * Async task which spins a progress circle for 1 second when the distance
     * has been recalculated due to an updated user location.
     */
    public class spinProgressBar extends AsyncTask<Void, Void, Void> {

        /**
         * Background task to sleep a thread for 1 second while the
         * progress circle spins.
         * upon successful check-in.
         * @param args no parameters needed for this task.
         * @return null
         */
        @Override
        protected Void doInBackground(Void... args) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After waiting one second, hide the progress circle.
         */
        @Override
        protected void onPostExecute(Void result) {
            mPriceProgress.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }

        /**
         * Make the progress circle animation visible to
         * the user.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPriceProgress.setVisibility(View.VISIBLE);
        }
    }

    public void buy(String market){
        if(mOrder.equals("MARKET")) {
            if(areFieldsValid()) {
                toastMessage("Placing " + mSelectedCurrency + " BUY Market Order for " + mEditAmount.getText());
                String response = wrapper.buyMarket(market, mEditAmount.getText().toString());
                toastMessage(response);

            }
        }
        else if(mOrder.equals("LIMIT")) {
            if(areFieldsValid()) {
                toastMessage("Placing " + mSelectedCurrency + " BUY Limit Order for " + mEditAmount.getText() +
                        " with a LIMIT of " + mEditLimitStop.getText());
                String response = wrapper.buyLimit(market, mEditAmount.getText().toString(), mEditLimitStop.getText().toString());
                toastMessage(response);
            }
        }
        else if(mOrder.equals("STOP")) {
            if(areFieldsValid()) {
                toastMessage("Sorry, stop order functionality is not available on the Bittrex API");
            }
        }
    }

    public void sell(String market){
        if(mOrder.equals("MARKET")) {
            if(areFieldsValid()) {
                toastMessage("Placing " + mSelectedCurrency + " SELL Market Order for " + mEditAmount.getText());
                String response = wrapper.sellMarket(market, mEditAmount.getText().toString());
                toastMessage(response);

            }
        }
        else if(mOrder.equals("LIMIT")) {
            if(areFieldsValid()) {
                toastMessage("Placing " + mSelectedCurrency + " SELL Limit Order for " + mEditAmount.getText() +
                        " with a LIMIT of " + mEditLimitStop.getText());
                String response = wrapper.sellLimit(market, mEditAmount.getText().toString(), mEditLimitStop.getText().toString());
                toastMessage(response);
            }
        }
        else if(mOrder.equals("STOP")) {
            if(areFieldsValid()) {
                toastMessage("Sorry, stop order functionality is not available on the Bittrex API");
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        this.finish();
        mtimer.cancel();
        mtimer = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mtimer==null)
            startPriceUpdateTimer();
    }
}
