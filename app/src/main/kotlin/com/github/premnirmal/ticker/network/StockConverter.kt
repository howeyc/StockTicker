package com.github.premnirmal.ticker.network

import java.util.*

/**
 * Created by premnirmal on 3/21/16.
 */
internal object StockConverter {

  fun convert(gStock: GStock): Stock {
    val stock = Stock()
    val name = if (gStock.e != null) gStock.e.replace("INDEX", "") else gStock.t
    stock.symbol = gStock.t
    stock.Name = name
    stock.LastTradePriceOnly =
        if (gStock.lCur != null) {
          try {
            (gStock.lCur.replace(",", "")).toFloat()
          } catch(e: Exception) {
            0f
          }
        } else {
          0f
        }
    var changePercent: Double
    if (gStock.cp == null || gStock.cp.isNullOrEmpty()) {
      changePercent = 0.0
    } else {
      try {
        changePercent = gStock.cp.toDouble()
      } catch (e: Exception) {
        changePercent = 0.0
      }
    }
    if (changePercent >= 0) {
      stock.ChangeinPercent = "+$changePercent%"
    } else {
      stock.ChangeinPercent = "$changePercent%"
    }
    stock.Change = gStock.c
    stock.StockExchange = name

    stock.AverageDailyVolume = "0"
    stock.YearLow = 0.0f
    stock.YearHigh = 0.0f

    return stock
  }

  fun convertResponseQuotes(quotes: List<Stock>): List<Stock> {
    for (quote in quotes) {
      val newSymbol = if (quote != null && quote.symbol != null) {
        quote.symbol.replace(".", "^")
      } else {
        ""
      }
      quote.symbol = newSymbol
    }
    return quotes
  }

  fun convertRequestSymbols(symbols: List<String>): ArrayList<String> {
    val newSymbols = ArrayList<String>()
    for (symbol in symbols) {
      if (symbol != null) {
        if (symbol.equals(Stock.GDAXI_TICKER)) {
          newSymbols.add(symbol)
        } else {
          newSymbols.add(symbol
              .replace("^DJI", ".DJI")
              .replace("^IXIC", ".IXIC")
              .replace("^XAU", "XAU")
              .replace("^SPY", "SPY") // for symbols like ^SPY for yahoo
              .replace("^", ".")
          )
        }
      }
    }
    return newSymbols
  }
}