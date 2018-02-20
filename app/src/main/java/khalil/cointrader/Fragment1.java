package khalil.cointrader;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by khalil on 12/22/17.
 */

public class Fragment1 extends Fragment {
    private ListView listView;
    private Toast mToast;
    private Bittrex wrapper;
    public String[] mTickerPrice;
    public String[] mPercentChange;
    private String[] mMarket;
    private String[] mCoinName;
    private String[] mCoinSymbol;
    private String[] mMarketName;
    private String[] mHoldings;
    private String[] mHoldingsUSD;
    private Integer[] mImageID;
    private Integer[] mImageIDOrig;
    private String[] mLogoURL;
    private int index;
    private int NUM_COINS = 12;
    private ProgressDialog mProgressBar;
    private ProgressBar mProgressSpin;
    private Timer mTimer;
    private Controller Controller;
    public JSONObject mJSONResponse;
    private List<Ticker> mTickerList;
    private boolean isInitialized = false;

    /*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mTimer != null) {
                System.out.println("STOPPING FRAGMENT 1 TIMER");
                mTimer.cancel();
                mTimer = null;
            }
        } else {
            if (mTimer == null && isInitialized) {
                System.out.println("STARTING FRAGMENT 1 TIMER");

                startPriceUpdateTimer();
            }
        }
    }
    */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mMarket = getResources().getStringArray(R.array.Markets);
        mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setMessage("Updating Data ...");

        mProgressSpin = (ProgressBar) view.findViewById(R.id.price_progressBar);
        mProgressSpin.setVisibility(View.INVISIBLE);
        wrapper = new Bittrex();
        wrapper.setAuthKeysFromTextFile("keys.txt");
        listView = (ListView) view.findViewById(R.id.list);
        Controller = new Controller(getActivity(), mMarket);

        String[] coin_name = getResources().getStringArray(R.array.CoinName);
        String[] coin_symbol = getResources().getStringArray(R.array.Symbols);

        Integer[] ImageID = {
                R.drawable.btc_1st,
                R.drawable.btc_2give,
                R.drawable.btc_aby,
                R.drawable.btc_ada,
                R.drawable.btc_adt,
                R.drawable.btc_adx,
                R.drawable.btc_aeon,
                R.drawable.btc_agrs,
                R.drawable.btc_amp,
                R.drawable.btc_ant,
                R.drawable.btc_apx,
                R.drawable.btc_ardr,
                R.drawable.btc_ark,
                R.drawable.btc_aur,
                R.drawable.btc_bat,
                R.drawable.btc_bay,
                R.drawable.btc_bcc,
                R.drawable.btc_bcy,
                R.drawable.btc_bitb,
                R.drawable.btc_blitz,
                R.drawable.btc_blk,
                R.drawable.btc_block,
                R.drawable.btc_bnt,
                R.drawable.btc_brk,
                R.drawable.btc_brx,
                R.drawable.btc_bsd,
                R.drawable.btc_btcd,
                R.drawable.btc_btg,
                R.drawable.btc_burst,
                R.drawable.btc_byc,
                R.drawable.btc_cann,
                R.drawable.btc_cfi,
                R.drawable.btc_clam,
                R.drawable.btc_cloak,
                R.drawable.btc_club,
                R.drawable.btc_coval,
                R.drawable.btc_cpc,
                R.drawable.btc_crb,
                R.drawable.btc_crw,
                R.drawable.btc_cure,
                R.drawable.btc_cvc,
                R.drawable.btc_dash,
                R.drawable.btc_dcr,
                R.drawable.btc_dct,
                R.drawable.btc_dgb,
                R.drawable.btc_dgd,
                R.drawable.btc_dmd,
                R.drawable.btc_dnt,
                R.drawable.btc_doge,
                R.drawable.btc_dope,
                R.drawable.btc_dtb,
                R.drawable.btc_dyn,
                R.drawable.btc_ebst,
                R.drawable.btc_edg,
                R.drawable.btc_efl,
                R.drawable.btc_egc,
                R.drawable.btc_emc,
                R.drawable.btc_emc2,
                R.drawable.btc_eng,
                R.drawable.btc_enrg,
                R.drawable.btc_erc,
                R.drawable.btc_etc,
                R.drawable.btc_eth,
                R.drawable.btc_excl,
                R.drawable.btc_exp,
                R.drawable.btc_fair,
                R.drawable.btc_fct,
                R.drawable.btc_fldc,
                R.drawable.btc_flo,
                R.drawable.btc_ftc,
                R.drawable.btc_fun,
                R.drawable.btc_gam,
                R.drawable.btc_game,
                R.drawable.btc_gbg,
                R.drawable.btc_gbyte,
                R.drawable.btc_gcr,
                R.drawable.btc_geo,
                R.drawable.btc_gld,
                R.drawable.btc_gno,
                R.drawable.btc_gnt,
                R.drawable.btc_golos,
                R.drawable.btc_grc,
                R.drawable.btc_grs,
                R.drawable.btc_gup,
                R.drawable.btc_hmq,
                R.drawable.btc_incnt,
                R.drawable.btc_infx,
                R.drawable.btc_ioc,
                R.drawable.btc_ion,
                R.drawable.btc_iop,
                R.drawable.btc_kmd,
                R.drawable.btc_kore,
                R.drawable.btc_lbc,
                R.drawable.btc_lgd,
                R.drawable.btc_lmc,
                R.drawable.btc_lsk,
                R.drawable.btc_ltc,
                R.drawable.btc_lun,
                R.drawable.btc_maid,
                R.drawable.btc_mana,
                R.drawable.btc_mco,
                R.drawable.btc_meme,
                R.drawable.btc_mer,
                R.drawable.btc_mln,
                R.drawable.btc_mona,
                R.drawable.btc_mtl,
                R.drawable.btc_mue,
                R.drawable.btc_music,
                R.drawable.btc_myst,
                R.drawable.btc_nav,
                R.drawable.btc_nbt,
                R.drawable.btc_neo,
                R.drawable.btc_neos,
                R.drawable.btc_nlg,
                R.drawable.btc_nmr,
                R.drawable.btc_nxc,
                R.drawable.btc_nxs,
                R.drawable.btc_nxt,
                R.drawable.btc_ok,
                R.drawable.btc_omg,
                R.drawable.btc_omni,
                R.drawable.btc_part,
                R.drawable.btc_pay,
                R.drawable.btc_pdc,
                R.drawable.btc_pink,
                R.drawable.btc_pivx,
                R.drawable.btc_pkb,
                R.drawable.btc_pot,
                R.drawable.btc_powr,
                R.drawable.btc_ppc,
                R.drawable.btc_ptc,
                R.drawable.btc_ptoy,
                R.drawable.btc_qrl,
                R.drawable.btc_qtum,
                R.drawable.btc_qwark,
                R.drawable.btc_rads,
                R.drawable.btc_rby,
                R.drawable.btc_rcn,
                R.drawable.btc_rdd,
                R.drawable.btc_rep,
                R.drawable.btc_rise,
                R.drawable.btc_rlc,
                R.drawable.btc_salt,
                R.drawable.btc_sbd,
                R.drawable.btc_sc,
                R.drawable.btc_seq,
                R.drawable.btc_shift,
                R.drawable.btc_sib,
                R.drawable.btc_slr,
                R.drawable.btc_sls,
                R.drawable.btc_snrg,
                R.drawable.btc_snt,
                R.drawable.btc_sphr,
                R.drawable.btc_spr,
                R.drawable.btc_start,
                R.drawable.btc_steem,
                R.drawable.btc_storj,
                R.drawable.btc_strat,
                R.drawable.btc_swift,
                R.drawable.btc_swt,
                R.drawable.btc_synx,
                R.drawable.btc_sys,
                R.drawable.btc_thc,
                R.drawable.btc_tix,
                R.drawable.btc_tks,
                R.drawable.btc_trig,
                R.drawable.btc_trst,
                R.drawable.btc_trust,
                R.drawable.btc_tx,
                R.drawable.btc_ubq,
                R.drawable.btc_unb,
                R.drawable.btc_via,
                R.drawable.btc_vib,
                R.drawable.btc_vox,
                R.drawable.btc_vrc,
                R.drawable.btc_vrm,
                R.drawable.btc_vtc,
                R.drawable.btc_vtr,
                R.drawable.btc_waves,
                R.drawable.btc_wings,
                R.drawable.btc_xcp,
                R.drawable.btc_xdn,
                R.drawable.btc_xel,
                R.drawable.btc_xem,
                R.drawable.btc_xlm,
                R.drawable.btc_xmg,
                R.drawable.btc_xmr,
                R.drawable.btc_xmy,
                R.drawable.btc_xrp,
                R.drawable.btc_xst,
                R.drawable.btc_xvc,
                R.drawable.btc_xvg,
                R.drawable.btc_xwc,
                R.drawable.btc_xzc,
                R.drawable.btc_zcl,
                R.drawable.btc_zec,
                R.drawable.btc_zen,
                R.drawable.eth_1st,
                R.drawable.eth_ada,
                R.drawable.eth_adt,
                R.drawable.eth_adx,
                R.drawable.eth_ant,
                R.drawable.eth_bat,
                R.drawable.eth_bcc,
                R.drawable.eth_bnt,
                R.drawable.eth_btg,
                R.drawable.eth_cfi,
                R.drawable.eth_crb,
                R.drawable.eth_cvc,
                R.drawable.eth_dash,
                R.drawable.eth_dgb,
                R.drawable.eth_dgd,
                R.drawable.eth_dnt,
                R.drawable.eth_eng,
                R.drawable.eth_etc,
                R.drawable.eth_fct,
                R.drawable.eth_fun,
                R.drawable.eth_gno,
                R.drawable.eth_gnt,
                R.drawable.eth_gup,
                R.drawable.eth_hmq,
                R.drawable.eth_lgd,
                R.drawable.eth_ltc,
                R.drawable.eth_lun,
                R.drawable.eth_mana,
                R.drawable.eth_mco,
                R.drawable.eth_mtl,
                R.drawable.eth_myst,
                R.drawable.eth_neo,
                R.drawable.eth_nmr,
                R.drawable.eth_omg,
                R.drawable.eth_pay,
                R.drawable.eth_powr,
                R.drawable.eth_ptoy,
                R.drawable.eth_qrl,
                R.drawable.eth_qtum,
                R.drawable.eth_rcn,
                R.drawable.eth_rep,
                R.drawable.eth_rlc,
                R.drawable.eth_salt,
                R.drawable.eth_sc,
                R.drawable.eth_snt,
                R.drawable.eth_storj,
                R.drawable.eth_strat,
                R.drawable.eth_tix,
                R.drawable.eth_trst,
                R.drawable.eth_vib,
                R.drawable.eth_waves,
                R.drawable.eth_wings,
                R.drawable.eth_xem,
                R.drawable.eth_xlm,
                R.drawable.eth_xmr,
                R.drawable.eth_xrp,
                R.drawable.eth_zec,
                R.drawable.usdt_bcc,
                R.drawable.usdt_btc,
                R.drawable.usdt_btg,
                R.drawable.usdt_dash,
                R.drawable.usdt_etc,
                R.drawable.usdt_eth,
                R.drawable.usdt_ltc,
                R.drawable.usdt_neo,
                R.drawable.usdt_omg,
                R.drawable.usdt_xmr,
                R.drawable.usdt_xrp,
                R.drawable.usdt_zec};

        mImageIDOrig = ImageID;


        updatePrice();
        startPriceUpdateTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment1_layout, container, false);
        return v;
    }

    private void storeSelectedCoin(int position) {
        SharedPreferences mSharedIndex = getActivity().getSharedPreferences("coin_index", getActivity().MODE_PRIVATE);
        SharedPreferences mSharedPrice = getActivity().getSharedPreferences("selected_price", getActivity().MODE_PRIVATE);
        SharedPreferences mSharedPercent = getActivity().getSharedPreferences("selected_percent", getActivity().MODE_PRIVATE);
        SharedPreferences mSharedMarket = getActivity().getSharedPreferences("market", getActivity().MODE_PRIVATE);
        SharedPreferences mSharedMarketLong = getActivity().getSharedPreferences("marketlong", getActivity().MODE_PRIVATE);
        SharedPreferences mSharedLogoURL = getActivity().getSharedPreferences("logourl", getActivity().MODE_PRIVATE);

        SharedPreferences.Editor mEditIndex = mSharedIndex.edit();
        SharedPreferences.Editor mEditPrice = mSharedPrice.edit();
        SharedPreferences.Editor mEditPercent = mSharedPercent.edit();
        SharedPreferences.Editor mEditMarket = mSharedMarket.edit();
        SharedPreferences.Editor mEditMarketLong = mSharedMarketLong.edit();
        SharedPreferences.Editor mEditLogoURL = mSharedLogoURL.edit();


        mEditMarket.putString("market", mCoinSymbol[position]);
        mEditMarketLong.putString("marketlong", mCoinName[position]);
        mEditIndex.putInt("coin_index", position);
        mEditPrice.putString("selected_price", mTickerPrice[position]);
        mEditPercent.putString("selected_percent", mPercentChange[position]);
        mEditLogoURL.putString("logourl", mLogoURL[position]);


        mEditIndex.apply();
        mEditPrice.apply();
        mEditPercent.apply();
        mEditMarket.apply();
        mEditMarketLong.apply();
        mEditLogoURL.apply();
    }


    public void startPriceUpdateTimer() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially  time consuming task
                        updatePrice();
                    }
                }).start();
            }
        }, 5000, 4000); //Length of how often to update location
    }

    public void updatePrice() {
        //https://bittrex.com/api/v1.1/public/getticker?market=USDT-BTC
        String url = "https://bittrex.com/api/v1.1/public/getmarketsummaries";
        String url2 = "https://bittrex.com/api/v1.1/public/getmarkets";

        Controller.getMarketSummaryResponse(url, url2, new Controller.VolleyCallback() {
            @Override
            public void onSuccessResponse(String[] result) {
                System.out.println("MARKET SUMMARY: " + result[0]);
                System.out.println("MARKET LOGO: " + result[1]);
                mTickerList = Controller.populateTickers(result[0], result[1], mImageIDOrig, getResources().getStringArray(R.array.CoinName));
                updateListView();
            }
        }, mImageIDOrig);

    }


    public void updateBalance() {
        final Double mCurrentPriceDouble;


        for (index = 0; index < NUM_COINS; index++) {
            String rawResponse = wrapper.getBalance(mCoinSymbol[index]);
            final String coinBalance;
            final String balance2;
            List<HashMap<String, String>> responseMapList;

            if(rawResponse.contains("\"success\":true")) {
                responseMapList = Bittrex.getMapsFromResponse(rawResponse);
                final HashMap<String, String> currency1 = responseMapList.get(0);
                System.out.println("BALANCES " + rawResponse);
                coinBalance = currency1.get("Balance");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!(coinBalance == null) && !coinBalance.equals("0.00000000")) {
                            mTickerList.get(index).setBalance(Controller.formatDecimalSetoci(coinBalance));
                        } else {
                        }
                    }
                });
            }
            else {
                System.out.println("ERROR! Response: " + rawResponse);
            }

            }

        for(index = 0; index < NUM_COINS; index++){
            System.out.println("HOLDINGS FOR " + index + ": " + mTickerList.get(index).getBalance());
        }
    }


    public void updateListView(){
        System.out.println("SIZE OF MARKETS: " + mTickerList.size());

        if(!isInitialized) {
            mCoinName = new String[mTickerList.size()];
            mCoinSymbol = new String[mTickerList.size()];
            mTickerPrice = new String[mTickerList.size()];
            mPercentChange = new String[mTickerList.size()];
            mHoldings = new String[mTickerList.size()];
            mHoldingsUSD = new String[mTickerList.size()];
            mImageID = new Integer[mTickerList.size()];
            mLogoURL = new String[mTickerList.size()];

            CustomListAdapter adapter = new CustomListAdapter(getActivity(), mImageID, mCoinSymbol, mCoinName, mTickerPrice, mPercentChange, mHoldings, mHoldingsUSD, mLogoURL);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    storeSelectedCoin(position);

                    final Intent intent = new Intent(getActivity(), SecondActivity.class);
                    startActivity(intent);
                }
            });

            isInitialized = true;
        }

        for(int i = 0; i < mTickerList.size(); i++){
            mImageID[i] = mTickerList.get(i).getLogo();
            mCoinSymbol[i] = mTickerList.get(i).getMarketName();
            mCoinName[i] = mTickerList.get(i).getNameLong();
            mPercentChange[i] = mTickerList.get(i).getPercentChange();
            mTickerPrice[i] = mTickerList.get(i).getPrice();
            mLogoURL[i] = mTickerList.get(i).getLogoUrl();
        }

        /*
        updateBalance();

        for(int i = 0; i < NUM_COINS; i++){
            mHoldings[i] = mTickerList.get(i).getBalance();
            if(mHoldings[i] != null)
                mHoldingsUSD[i] = "$" + Controller.formatDecimal(Double.toString(Double.parseDouble(mHoldings[i])*Double.parseDouble(mTickerPrice[i].substring(1).replace(",",""))));
            System.out.println("UPDATING INDEX " + i + " WITH BALANCE: " + mHoldingsUSD[i]);
        }
        */

        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }



    @Override
    public void onStop(){
        super.onStop();
        System.out.println("STOPPING FRAGMENT 1 TIMER");
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mTimer == null){
            System.out.println("STARTING FRAGMENT 1 TIMER");
            startPriceUpdateTimer();
        }
    }

    public void stopTimer(){
        System.out.println("STOPPING FRAGMENT 1 TIMER");
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    public void onDestroy(){
        listView.setAdapter(null);
        super.onDestroy();
    }


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
                Thread.sleep(500);
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
            mProgressSpin.setVisibility(View.INVISIBLE);
            super.onPostExecute(result);
        }

        /**
         * Make the progress circle animation visible to
         * the user.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressSpin.setVisibility(View.VISIBLE);
        }
    }

}
