package khalil.cointrader;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

/**
 * Created by khalil on 12/22/17.
 */

public class Fragment2 extends Fragment {
    private ListView listView;
    private Toast mToast;
    private Bittrex wrapper;
    public JSONObject mJSONResponse;
    private List<Ticker> mTickerList;
    private String[] mTickers;
    private String[] mTickerBalances;
    private String[] mTickerBalancesUSD;
    private TextView mTicker;
    private TextView mTickerBalance;
    private TextView mTickerBalanceUSD;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mTicker = (TextView) getActivity().findViewById(R.id.balance_tickers);
        mTickerBalance = (TextView) getActivity().findViewById(R.id.balance_holdings);
        mTickerBalanceUSD = (TextView) getActivity().findViewById(R.id.balance_holdingsUSD);
        wrapper = new Bittrex();
        wrapper.setAuthKeysFromTextFile("keys.txt");

        new Thread(new Runnable() {
            public void run() {
                // a potentially  time consuming task
                pullBalances();
                updateTextViews();
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment2_layout, container, false);

        //Initialize the views
        //Pull Bitcoin-USD Data
        //Pull Ticker Data
        //Convert Ticker share to bitcoin shares, then bitcoin shares to USD
        return v;
    }

    private void pullBalances() {
        String rawResponse = wrapper.getBalances();
        String marketResponse = wrapper.getMarketSummaries();

        System.out.println("BALANCE INFORMATION: " + rawResponse);
        List<HashMap<String, String>> responseMapList;

        if(rawResponse.contains("\"success\":true")) {
            responseMapList = Bittrex.getMapsFromResponse(rawResponse);

            mTickers = new String[responseMapList.size()];
            mTickerBalances = new String[responseMapList.size()];
            mTickerBalancesUSD = new String[responseMapList.size()];

            for(int i = 0; i < responseMapList.size(); i++){
                final HashMap<String, String> balance = responseMapList.get(i);
                    mTickers[i] = balance.get("Currency");
                    mTickerBalances[i] = balance.get("Balance");
            }
        }
        else
            System.out.println("ERROR! Response: " + rawResponse);
    }

    private void updateTextViews() {
        String TickerText = "";
        String TickerBalances = "";

        for(int i = 0; i < mTickers.length; i++){
           if(!mTickerBalances[i].equals("0.00000000")) {
               mTicker.append(mTickers[i] + "\n");
               mTickerBalance.append(mTickerBalances[i].substring(0, 8) + "\n");
           }
        }
    }
}
