package beans;

import java.io.Serializable;

/**
 * 行情信息
 * Created by zyy on 2016/6/16.
 */
public class PriceData  implements Serializable{

    private static final long serialVersionUID = -6059814512408932714L;

    private String stockCode;   //股票代码

    private String stockName;   //股票名称

    private float stockPrice;   //股票价格

    public PriceData() {
    }

    public PriceData(String stockCode, String stockName, float stockPrice) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.stockPrice = stockPrice;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public float getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(float stockPrice) {
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return "PriceData{" +
                "stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                '}';
    }
}
