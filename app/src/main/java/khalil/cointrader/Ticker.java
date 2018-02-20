package khalil.cointrader;

/**
 * Created by khalil on 12/22/17.
 */

public class Ticker {
    private String MarketName;
    private String NameLong;
    private String High;
    private String Low;
    private Double Volume;
    private String Last;
    private Double BaseVolume;
    private String TimeStamp;
    private String Ask;
    private String OpenBuyOrders;
    private String OpenSellOrders;
    private String PrevDay;
    private String PercentChange;
    private String Price;
    private String LogoUrl;
    private Integer Logo;

    private String Balance;
    private String BalanceUSD;

    public Ticker(Integer Logo, String NameLong, String MarketName, String High, String Low, Double Volume, String Last,
                  Double BaseVolume, String TimeStamp, String Ask, String OpenBuyOrders,
                  String OpenSellOrders, String PrevDay, String PercentChange, String Price,
                  String Balance, String BalanceUSD){

        this.NameLong = NameLong;
        this.MarketName = MarketName;
        this.High = High;
        this.Low = Low;
        this.Volume = Volume;
        this.Last = Last;
        this.BaseVolume = BaseVolume;
        this.TimeStamp = TimeStamp;
        this.Ask = Ask;
        this.OpenBuyOrders = OpenBuyOrders;
        this.OpenSellOrders = OpenSellOrders;
        this.PrevDay = PrevDay;
        this.PercentChange = PercentChange;
        this.Price = Price;
        this.Balance = Balance;
        this.BalanceUSD=BalanceUSD;
        this.Logo = Logo;
    }

    public String getNameLong() {
        return NameLong;
    }

    public void setNameLong(String nameLong) {
        NameLong = nameLong;
    }

    public Integer getLogo() {
        return Logo;
    }

    public void setLogo(Integer mLogo) {
        this.Logo = mLogo;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    public Double getVolume() {
        return Volume;
    }

    public void setVolume(Double volume) {
        Volume = volume;
    }

    public String getLast() {
        return Last;
    }

    public void setLast(String last) {
        Last = last;
    }

    public Double getBaseVolume() {
        return BaseVolume;
    }

    public void setBaseVolume(Double baseVolume) {
        BaseVolume = baseVolume;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getAsk() {
        return Ask;
    }

    public void setAsk(String ask) {
        Ask = ask;
    }

    public String getOpenBuyOrders() {
        return OpenBuyOrders;
    }

    public void setOpenBuyOrders(String openBuyOrders) {
        OpenBuyOrders = openBuyOrders;
    }

    public String getOpenSellOrders() {
        return OpenSellOrders;
    }

    public void setOpenSellOrders(String openSellOrders) {
        OpenSellOrders = openSellOrders;
    }

    public String getPrevDay() {
        return PrevDay;
    }

    public void setPrevDay(String prevDay) {
        PrevDay = prevDay;
    }

    public String getPercentChange() {
        return PercentChange;
    }

    public void setPercentChange(String percentChange) {
        PercentChange = percentChange;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void viewTickers(){
        System.out.println(this.getMarketName());
    }

    public String getBalanceUSD() {
        return BalanceUSD;
    }

    public void setBalanceUSD(String balanceUSD) {
        BalanceUSD = balanceUSD;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }
}
