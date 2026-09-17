package vn.currencyconverter.feartures.exchange_rate.parser;
import vn.currencyconverter.feartures.exchange_rate.model.ExchangeRate;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
public class ExchangeRateXmlParser {
    public static List<ExchangeRate> parse(String xml) {
        try {
            var f = DocumentBuilderFactory.newInstance();
            f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            f.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            f.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            var doc = f.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            var nodes = doc.getElementsByTagName("Exrate");
            List<ExchangeRate> rates = new ArrayList<>();
            for (int i=0; i<nodes.getLength(); i++) {
                Element e = (Element)nodes.item(i);
                var rate = new ExchangeRate(e.getAttribute("CurrencyCode"), e.getAttribute("CurrencyName"), number(e.getAttribute("Buy")), number(e.getAttribute("Sell")));
                rate.setTransferRate(number(e.getAttribute("Transfer")));
                rates.add(rate);
            }
            if (rates.isEmpty()) throw new IllegalArgumentException("File không chứa tỷ giá.");
            return rates;
        } catch (Exception e) { throw new IllegalArgumentException("Không đọc được XML: " + e.getMessage(), e); }
    }
    private static BigDecimal number(String s) {
        s=s.trim().replace(",", "");
        if (s.isEmpty() || s.equals("-")) return null;
        BigDecimal n = new BigDecimal(s);
        return n.signum()>0 ? n : null;
    }
}
